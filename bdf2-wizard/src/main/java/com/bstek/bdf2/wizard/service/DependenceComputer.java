package com.bstek.bdf2.wizard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.wizard.model.AddonInfo;
import com.bstek.bdf2.wizard.model.Dependence;
import com.bstek.bdf2.wizard.model.Exclusion;
import com.bstek.bdf2.wizard.model.Wizard;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Service(DependenceComputer.BEAN_ID)
public class DependenceComputer {
	public static final String BEAN_ID = "bdf2.wizard.dependenceComputer";

	@Resource(name = WizardService.BEAN_ID)
	private WizardService wizardService;

	public List<Dependence> execute(Wizard wizard) {
		List<Dependence> dependencies = new ArrayList<Dependence>();
		List<AddonInfo> addonInfos = wizard.getAddonInfos();
		Map<String, String> addonVersions = new HashMap<String, String>();
		for (AddonInfo addonInfo : addonInfos) {
			addonVersions.put(addonInfo.getName(), addonInfo.getLastVersion());
		}
		Map<String, String> repeatAddonVersions = new HashMap<String, String>();
		for (AddonInfo addonInfo : addonInfos) {
			Map<String, String> result = new HashMap<String, String>();
			this.calculateRepeatDependence(wizard.getVersionType(), addonInfo.getName(), addonVersions.keySet(), result);
			if (!result.isEmpty()) {
				repeatAddonVersions.putAll(result);
			}
		}
		Map<String, List<Exclusion>> addonExclusions = new HashMap<String, List<Exclusion>>();
		for (AddonInfo addonInfo : addonInfos) {
			List<Dependence> addonDependencies = new ArrayList<Dependence>();
			this.calculateDependence(wizard.getVersionType(), addonInfo.getName(), addonDependencies);
			for (Dependence dependence : addonDependencies) {
				String version1 = dependence.getVersion();
				String version2 = addonVersions.get(dependence.getArtifactId());
				if (StringUtils.hasText(version2)) {
					int i = compareVersion(version2, version1);
					if (i == 1) {
						Exclusion exclusion = new Exclusion();
						exclusion.setGroupId(dependence.getGroupId());
						exclusion.setArtifactId(dependence.getArtifactId());
						List<Exclusion> tempExclusionList = new ArrayList<Exclusion>();
						if (addonExclusions.get(addonInfo.getName()) != null) {
							tempExclusionList = addonExclusions.get(addonInfo.getName());
						}
						boolean repeatFlag = false;
						for (Exclusion ex : tempExclusionList) {
							if (ex.getGroupId().equals(dependence.getGroupId()) && ex.getArtifactId().equals(dependence.getArtifactId())) {
								repeatFlag = true;
							}
						}
						if (!repeatFlag) {
							tempExclusionList.add(exclusion);
							addonExclusions.put(addonInfo.getName(), tempExclusionList);
						}
					}
				}
			}
		}
		for (AddonInfo addonInfo : addonInfos) {
			if (repeatAddonVersions.keySet().contains(addonInfo.getName())) {
				String version1 = addonInfo.getLastVersion();
				String version2 = repeatAddonVersions.get(addonInfo.getName());
				int i = compareVersion(version1, version2);
				if (i == 1) {
					Dependence dependence = this.buildDependence(addonInfo, addonExclusions);
					dependencies.add(dependence);
				}
			}
			if (!repeatAddonVersions.keySet().contains(addonInfo.getName())) {
				Dependence dependence = this.buildDependence(addonInfo, addonExclusions);
				dependencies.add(dependence);
			}
		}
		return dependencies;
	}

	private Dependence buildDependence(AddonInfo addonInfo, Map<String, List<Exclusion>> addonExclusions) {
		Dependence dependence = new Dependence();
		dependence.setGroupId(addonInfo.getGroupId());
		dependence.setArtifactId(addonInfo.getArtifactId());
		dependence.setVersion(addonInfo.getLastVersion());
		List<Exclusion> exclusions = addonExclusions.get(addonInfo.getName());
		if (exclusions != null) {
			dependence.setExclusions(exclusions);
		}
		return dependence;
	}

	private void calculateRepeatDependence(String versionType, String addonName, Set<String> selectedAddonNames, Map<String, String> calculateResult) {
		AddonInfo addonInfo = findAddonInfo(versionType, addonName);
		if (addonInfo != null) {
			List<Dependence> dependencies = addonInfo.getDependencies();
			for (Dependence dependence : dependencies) {
				String artifactId = dependence.getArtifactId();
				String version = dependence.getVersion();
				if (selectedAddonNames.contains(artifactId)) {
					if (calculateResult.get(artifactId) != null) {
						int i = compareVersion(version, calculateResult.get(artifactId));
						if (i == 1) {
							calculateResult.put(artifactId, version);
						}
					} else {
						calculateResult.put(artifactId, version);
					}
				}
				calculateRepeatDependence(versionType, artifactId, selectedAddonNames, calculateResult);
			}

		}
	}

	private AddonInfo findAddonInfo(String versionType, String artifactId) {
		List<AddonInfo> infos = wizardService.loadAddonInfoCache(versionType);
		for (AddonInfo addonInfo : infos) {
			if (addonInfo.getName().equals(artifactId)) {
				return addonInfo;
			}
		}
		return null;
	}

	private void calculateDependence(String versionType, String addonName, List<Dependence> resultDependencies) {
		AddonInfo addonInfo = findAddonInfo(versionType, addonName);
		if (addonInfo != null) {
			List<Dependence> dependencies = addonInfo.getDependencies();
			for (Dependence dependence : dependencies) {
				if (dependence.getGroupId().equals(addonInfo.getGroupId())) {
					resultDependencies.add(dependence);
				}
				calculateDependence(versionType, dependence.getArtifactId(), resultDependencies);
			}

		}
	}

	private int compareVersion(String version1, String version2) {
		if ("LATEST".equals(version2)) return 0;
		int number1 = Integer.valueOf(version1.split("-")[0].replace(".", ""));
		int number2 = Integer.valueOf(version2.split("-")[0].replace(".", ""));
		if (number1 == number2) {
			return 0;
		} else if (number1 > number2) {
			return 1;
		} else if (number1 < number2) {
			return -1;
		}
		return 2;
	}

}
