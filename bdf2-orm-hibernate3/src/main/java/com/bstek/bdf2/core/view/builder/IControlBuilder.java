package com.bstek.bdf2.core.view.builder;

import com.bstek.bdf2.core.view.ViewComponent;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
public interface IControlBuilder {
	void build(Object control,ViewComponent parentViewComponent);
	boolean support(Object control);
}
