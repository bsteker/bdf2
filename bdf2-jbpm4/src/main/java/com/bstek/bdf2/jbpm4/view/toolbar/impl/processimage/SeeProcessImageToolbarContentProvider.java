package com.bstek.bdf2.jbpm4.view.toolbar.impl.processimage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.view.toolbar.IToolbarContentProvider;

/**
 * @author Jacky.gao
 * @since 2013-6-3
 */
@Component
public class SeeProcessImageToolbarContentProvider implements
		IToolbarContentProvider {
	@Value("${bdf2.jbpm4.disabledSeeProcessImageToolbarContentProvider}")
	private boolean disabled;

	public String getView(){
		return "bdf2.jbpm4.view.toolbar.impl.processimage.SeeProcessImageToolbarContentProvider";
	}
	public String key() {
		return "SeeProcessImage";
	}

	public String desc() {
		return "查看流程图";
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
