package com.bstek.bdf2.wizard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.wizard.model.AddonInfo;
import com.bstek.bdf2.wizard.model.Wizard;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Service(WizardService.BEAN_ID)
public class WizardService {
	public static final String BEAN_ID = "bdf2.wizard.wizardService";

	private Map<String, List<AddonInfo>> addonInfoCache = new HashMap<String, List<AddonInfo>>();

	@Resource(name = NexusAddonParser.BEAN_ID)
	private NexusAddonParser nexusAddonParser;

	@Resource(name = ProjectBuilder.BEAN_ID)
	private ProjectBuilder projectBuilder;

	/**
	 * 延时2s启动，每隔一个小时执行一次更新
	 * 
	 * @throws Exception
	 */
	@Scheduled(initialDelay = 2000, fixedRate = 60000 * 60)
	public void doRetrieveAddonInfosTask() throws Exception {
		List<AddonInfo> releaseResult = nexusAddonParser.execute(true);
		if (!releaseResult.isEmpty()) {
			addonInfoCache.put(Constants.RELEASE_VERSION, releaseResult);
		}
		List<AddonInfo> snapshotResult = nexusAddonParser.execute(false);
		if (!snapshotResult.isEmpty()) {
			addonInfoCache.put(Constants.SNAPSHOT_VERSION, snapshotResult);
		}
	}

	public List<AddonInfo> loadAddonInfoCache(String versionType) {
		if (StringUtils.hasText(versionType) && versionType.equals(Constants.SNAPSHOT_VERSION)) {
			return loadAddonInfoCacheForSnapshot();
		} else {
			return loadAddonInfoCacheForRelease();
		}
	}

	public String createProject(Wizard wizard) throws Exception {
		String fileVmid = projectBuilder.execute(wizard);
		return fileVmid;
	}

	public List<AddonInfo> loadAddonInfoCacheForRelease() {
		return addonInfoCache.get(Constants.RELEASE_VERSION);
	}

	public List<AddonInfo> loadAddonInfoCacheForSnapshot() {
		return addonInfoCache.get(Constants.SNAPSHOT_VERSION);
	}

}
