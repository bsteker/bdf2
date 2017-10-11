package com.bstek.bdf2.wizard.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.wizard.model.AddonInfo;
import com.bstek.bdf2.wizard.model.Wizard;
import com.bstek.bdf2.wizard.service.DownloadCounter;
import com.bstek.bdf2.wizard.service.WizardService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.view.widget.base.Dialog;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Controller("bdf2.wizard.WizardMaintain")
public class WizardMaintain {

	@Resource(name = WizardService.BEAN_ID)
	private WizardService wizardService;
	@Resource(name = DownloadCounter.BEAN_ID)
	private DownloadCounter downloadCounter;

	private String exclusionAddon = Configure.getString("bdf2.wizard.exclusionAddon");

	public void onInit(Dialog dialog) {
		if (dialog != null) {
			dialog.setCaption("创建BDF2项目( 已创建下载" + downloadCounter.get() + "次 )");
		}
	}

	@DataProvider
	public List<AddonInfo> findAddonInfos(String versionType) throws Exception {
		List<AddonInfo> addonInfos = wizardService.loadAddonInfoCache(versionType);
		List<AddonInfo> result = new ArrayList<AddonInfo>();
		List<String> exclusionAddons = getExclusionAddon();
		for (AddonInfo addonInfo : addonInfos) {
			String name = addonInfo.getName();
			if (exclusionAddons.contains(name)) {
				continue;
			}
			result.add(addonInfo);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Expose
	public Map<String, Object> generateTemplate(Map<String, Object> parameter) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String projectName = (String) parameter.get("name");
		if (!StringUtils.hasText(projectName)) {
			projectName = "bdf2-project";
		}
		String wizardType = (String) parameter.get("wizardType");
		String ormType = (String) parameter.get("ormType");
		String versionType = (String) parameter.get("versionType");
		List<Map<String, Object>> list = (List<Map<String, Object>>) parameter.get("datas");
		Wizard wizard = new Wizard();
		wizard.setProjectName(projectName);
		wizard.setOrmType(ormType);
		wizard.setWizardType(wizardType);
		wizard.setGroupId("com.bstek.bdf2");
		wizard.setArtifactId(projectName);
		wizard.setVersion("1.0.0");
		wizard.setVersionType(versionType);
		AddonInfo addonInfo = null;
		for (Map<String, Object> map : list) {
			addonInfo = new AddonInfo();
			addonInfo.setName((String) map.get("name"));
			addonInfo.setGroupId((String) map.get("groupId"));
			addonInfo.setArtifactId((String) map.get("name"));
			addonInfo.setLastVersion((String) map.get("version"));
			wizard.getAddonInfos().add(addonInfo);
		}
		wizard.getAddonInfos().add(this.findHibernate3AddonInfo(wizard));
		String fileVmid = wizardService.createProject(wizard);
		result.put("id", fileVmid);
		result.put("name", projectName);
		result.put("count", downloadCounter.incrementAndGet());
		return result;
	}

	private AddonInfo findHibernate3AddonInfo(Wizard wizard) {
		List<AddonInfo> addonInfos = wizardService.loadAddonInfoCache(wizard.getVersionType());
		for (AddonInfo info : addonInfos) {
			if (info.getName().equals("bdf2-orm-hibernate3")) {
				return info;
			}
		}
		return null;
	}

	private List<String> getExclusionAddon() {
		List<String> list = Arrays.asList(exclusionAddon.split(","));
		return list;
	}

}
