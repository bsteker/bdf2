package com.bstek.bdf2.wizard.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.bstek.bdf2.wizard.model.AddonInfo;
import com.bstek.bdf2.wizard.model.Dependence;
import com.bstek.bdf2.wizard.model.VersionInfo;
import com.bstek.bdf2.wizard.utils.HttpClientUtils;
import com.bstek.dorado.core.Configure;

@Service(NexusAddonParser.BEAN_ID)
public class NexusAddonParser implements InitializingBean {
	public static final String BEAN_ID = "bdf2.wizard.nexusAddonParser";
	private String dorado_snapshots_url = Configure.getString("bdf2.dorado_snapshots.url");
	private String dorado_releases_url = Configure.getString("bdf2.dorado_releases.url");
	private HttpClient httpClient;
	private SAXReader saxReader;

	public List<AddonInfo> execute(boolean isRelease) throws Exception {
		List<AddonInfo> list = new ArrayList<AddonInfo>();
		AddonInfo addonInfo;
		String url = isRelease == true ? dorado_releases_url : dorado_snapshots_url;
		String resultXml = HttpClientUtils.getHttpRequestResult(httpClient, url);
		List<Map<String, Object>> result = this.parseAddonInfos(resultXml);
		for (Map<String, Object> m : result) {
			String resourceURI = (String) m.get("resourceURI");
			if (!resourceURI.endsWith("/")) {
				continue;
			}
			String metaDataUrl = resourceURI + "maven-metadata.xml";
			resultXml = HttpClientUtils.getHttpRequestResult(httpClient, metaDataUrl);
			addonInfo = this.parseAddonInfoMetaData(resultXml);
			String lastVersionName = addonInfo.getLastVersion();
			String pomURL = null;
			if (isRelease) {
				pomURL = (String) m.get("resourceURI") + lastVersionName + "/" + addonInfo.getName() + "-" + lastVersionName + ".pom";
			} else {
				String lasVersionMetaDataUrl = (String) m.get("resourceURI") + lastVersionName + "/maven-metadata.xml";
				resultXml = HttpClientUtils.getHttpRequestResult(httpClient, lasVersionMetaDataUrl);
				Map<String, Object> lastVersionSnapshot = parseAddonInfoLastSnapshotVersionMetaData(resultXml);
				if (lastVersionSnapshot != null) {
					String timestamp = (String) lastVersionSnapshot.get("timestamp");
					String buildNumber = (String) lastVersionSnapshot.get("buildNumber");
					String pomName = lastVersionName.split("-")[0] + "-" + timestamp + "-" + buildNumber;
					pomURL = (String) m.get("resourceURI") + lastVersionName + "/" + addonInfo.getName() + "-" + pomName + ".pom";
				}
			}
			resultXml = HttpClientUtils.getHttpRequestResult(httpClient, pomURL);
			parseAddonInfoLastVersionPom(addonInfo, resultXml);
			list.add(addonInfo);
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> parseAddonInfos(String xml) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (xml == null) {
			return result;
		}
		InputStream inputStream = toInputStream(xml);
		try {
			Document document = saxReader.read(inputStream);
			List list = document.selectNodes("//content/data/content-item");
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				Element element = (Element) iter.next();
				for (Iterator iterInner = element.elementIterator(); iterInner.hasNext();) {
					Element elementInner = (Element) iterInner.next();
					map.put(elementInner.getName(), elementInner.getText());
				}
				result.add(map);
			}
		} finally {
			inputStream.close();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> parseAddonInfoLastSnapshotVersionMetaData(String xml) throws Exception {
		InputStream inputStream = toInputStream(xml);
		try {
			Document document = saxReader.read(inputStream);
			List list = document.selectNodes("//metadata/versioning/snapshot");
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				Map<String, Object> map = new HashMap<String, Object>();
				for (Iterator iterInner = element.elementIterator(); iterInner.hasNext();) {
					Element elementInner = (Element) iterInner.next();
					map.put(elementInner.getName(), elementInner.getText());
				}
				return map;
			}
		} finally {
			inputStream.close();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public void parseAddonInfoLastVersionPom(AddonInfo addonInfo, String xml) throws Exception {
		InputStream inputStream = toInputStream(xml);
		List<Dependence> dependencies = new ArrayList<Dependence>();
		try {
			Document document = saxReader.read(inputStream);
			Element descriptionElement = document.getRootElement().element("description");
			if (descriptionElement != null) {
				addonInfo.setDesc(descriptionElement.getText());
			}
			Element dependenciesElement = document.getRootElement().element("dependencies");
			if (dependenciesElement == null) {
				return;
			}
			List list = dependenciesElement.elements("dependency");
			Iterator iter = list.iterator();
			Dependence dependence = null;
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				dependence = new Dependence();
				for (Iterator iterInner = element.elementIterator(); iterInner.hasNext();) {
					Element elementInner = (Element) iterInner.next();
					if (elementInner.getName().equals("groupId")) {
						dependence.setGroupId(elementInner.getText());
					} else if (elementInner.getName().equals("artifactId")) {
						dependence.setArtifactId(elementInner.getText());
					} else if (elementInner.getName().equals("version")) {
						dependence.setVersion(elementInner.getText());
					} else if (elementInner.getName().equals("classifier")) {
						dependence.setClassifier(elementInner.getText());
					}
				}
				if ("com.bstek.bdf2".equals(dependence.getGroupId())) {
					dependencies.add(dependence);
				}
			}
			addonInfo.setDependencies(dependencies);
		} finally {
			inputStream.close();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AddonInfo parseAddonInfoMetaData(String xml) throws Exception {
		if (xml == null) {
			return null;
		}
		AddonInfo addonInfo = new AddonInfo();
		InputStream inputStream = toInputStream(xml);
		try {
			Document document = saxReader.read(inputStream);
			String groupId = document.getRootElement().element("groupId").getText();
			String artifactId = document.getRootElement().element("artifactId").getText();
			Element versioning = document.getRootElement().element("versioning");
			Element elementLatest = versioning.element("latest");
			if (elementLatest == null) {
				elementLatest = versioning.element("release");
			}
			String latest = "";
			if (elementLatest != null) {
				latest = elementLatest.getText();
			}
			String lastUpdated = versioning.element("lastUpdated").getText();
			addonInfo.setArtifactId(artifactId);
			addonInfo.setName(artifactId);
			addonInfo.setGroupId(groupId);
			addonInfo.setLastVersion(latest);
			addonInfo.setLastUpdated(lastUpdated);
			Element versions = versioning.element("versions");
			VersionInfo versionInfo;
			List<VersionInfo> versionInfos = new ArrayList<VersionInfo>();
			int index = 1;
			for (Iterator iterInner = versions.elementIterator(); iterInner.hasNext();) {
				Element elementInner = (Element) iterInner.next();
				versionInfo = new VersionInfo();
				versionInfo.setName(elementInner.getText());
				versionInfo.setLastVersion(false);
				if (latest.equals(elementInner.getText())) {
					//versionInfo.setLastVersion(true);
				}
				versionInfo.setIndex(index);
				versionInfos.add(versionInfo);
				index++;
			}
			Collections.sort(versionInfos, new SortVersionInfoClass());
			/*if (!versionInfos.isEmpty() && StringUtils.isEmpty(addonInfo.getLastVersion())) {
			}*/
			versionInfos.get(0).setLastVersion(true);
			addonInfo.setLastVersion(versionInfos.get(0).getName());
			if (versionInfos.size() > 4) {
				versionInfos = versionInfos.subList(0, 3);
			}
			addonInfo.setVersions(versionInfos);
		} finally {
			inputStream.close();
		}
		return addonInfo;
	}

	@SuppressWarnings("rawtypes")
	private class SortVersionInfoClass implements Comparator {
		public int compare(Object arg0, Object arg1) {
			VersionInfo versionInfo0 = (VersionInfo) arg0;
			VersionInfo versionInfo1 = (VersionInfo) arg1;
			int flag = versionInfo1.getIndex() - (versionInfo0.getIndex());
			return flag;
		}
	}

	public InputStream toInputStream(String xml) throws Exception {
		return IOUtils.toInputStream(xml, "UTF-8");
	}

	public void afterPropertiesSet() throws Exception {
		saxReader = new SAXReader();
		saxReader.setEncoding("UTF-8");
		httpClient = HttpClientUtils.getHttpClient();
	//	HttpClientUtils.testWithProxy(httpClient);
	}

}
