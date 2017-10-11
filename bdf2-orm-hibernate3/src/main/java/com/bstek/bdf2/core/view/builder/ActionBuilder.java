package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.action.Action;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@org.springframework.stereotype.Component
public class ActionBuilder implements IControlBuilder {

	public boolean support(Object control) {
		return control instanceof Action;
	}

	public void build(Object control, ViewComponent parentViewComponent) {
		Action action = (Action) control;
		String id = action.getId();
		if (StringUtils.isNotEmpty(id)) {
			ViewComponent component = new ViewComponent();
			component.setId(id);
			component.setIcon(">dorado/res/" + Action.class.getName().replaceAll("\\.", "/") + ".png");
			component.setName(Action.class.getSimpleName());
			parentViewComponent.addChildren(component);
		}

	}

}
