package com.bstek.bdf2.wizard.view;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.bdf2.wizard.service.WizardService;
import com.bstek.dorado.annotation.Expose;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Controller("bdf2.wizard.refreshMaintain")
public class RefreshMaintain {

	@Resource(name = WizardService.BEAN_ID)
	private WizardService wizardService;

	@Expose
	public void refresh() throws Exception {
		wizardService.doRetrieveAddonInfosTask();
	}

}
