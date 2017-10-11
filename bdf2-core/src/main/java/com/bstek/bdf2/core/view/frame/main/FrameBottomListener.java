package com.bstek.bdf2.core.view.frame.main;

import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;
import com.bstek.dorado.view.widget.Container;

/**
 * @author Jacky.gao
 * @since 2013-2-6
 */
@Component("bdf2.frameBottomListener")
public class FrameBottomListener extends AbstractFrameListener{
	public void onInit(Container container) {
		for (IFrameShortcutActionRegister reg : registers) {
			if (!reg.isDisabled()) {
				reg.registerToStatusBar(container);
			}
		}
	}
}
