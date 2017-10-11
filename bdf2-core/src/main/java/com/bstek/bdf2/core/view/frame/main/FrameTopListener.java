package com.bstek.bdf2.core.view.frame.main;

import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.view.widget.Container;

/**
 * @author Jacky.gao
 * @since 2013-2-6
 */
@Component("bdf2.frameTopListener")
public class FrameTopListener extends AbstractFrameListener {
	private String width = Configure.getString("bdf2.mainFrameTopView.registerWidth");

	public void onInit(Container container) {
		container.setWidth(width != null ? width : "120");
		for (IFrameShortcutActionRegister reg : registers) {
			if (!reg.isDisabled()) {
				reg.registerToFrameTop(container);
			}
		}
	}
}
