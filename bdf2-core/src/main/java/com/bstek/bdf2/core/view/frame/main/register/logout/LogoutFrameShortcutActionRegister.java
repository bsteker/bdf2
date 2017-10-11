package com.bstek.bdf2.core.view.frame.main.register.logout;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.SubViewHolder;

/**
 * @author Jacky.gao
 * @since 2013-2-5
 */
@Component
public class LogoutFrameShortcutActionRegister implements
		IFrameShortcutActionRegister {
	@Value("${bdf2.disableLogoutShortcutRegister}")
	private boolean disabled;
	public void registerToFrameTop(Container container) {
		SubViewHolder subviewHolder=new SubViewHolder();
		subviewHolder.setSubView("bdf2.core.view.frame.main.register.logout.Logout");
		container.addChild(subviewHolder);
	}

	public void registerToStatusBar(Container container) {

	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public int order() {
		return 50;
	}
}
