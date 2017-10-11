package com.bstek.bdf2.core.view.frame;

import com.bstek.dorado.view.widget.Container;

/**
 * @since 2013-2-1
 * @author Jacky.gao
 */
public interface IFrameShortcutActionRegister {
	void registerToFrameTop(Container container);
	void registerToStatusBar(Container container);
	boolean isDisabled();
	int order();
}
