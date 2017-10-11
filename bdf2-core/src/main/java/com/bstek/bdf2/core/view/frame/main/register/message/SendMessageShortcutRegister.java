package com.bstek.bdf2.core.view.frame.main.register.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.SubViewHolder;

@Component
public class SendMessageShortcutRegister implements IFrameShortcutActionRegister {
	@Value("${bdf2.disableSendMessageShortcutRegister}")
	private boolean disabled;
	public void registerToFrameTop(Container container) {
		SubViewHolder subviewHolder=new SubViewHolder();
		subviewHolder.setSubView("bdf2.core.view.frame.main.register.message.SendMessage");
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
		return 20;
	}
}
