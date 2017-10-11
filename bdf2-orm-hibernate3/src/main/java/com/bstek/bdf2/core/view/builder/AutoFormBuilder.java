package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;

@Component
public class AutoFormBuilder extends AbstractControlBuilder {

	public void build(Object control, ViewComponent parentViewComponent) {
		AutoForm autoForm = (AutoForm) control;
		String id = autoForm.getId();
		ViewComponent component = generateViewComponent(id,AutoForm.class);
		if (StringUtils.isEmpty(id)) {
			component.setEnabled(false);
		}
		parentViewComponent.addChildren(component);
		for (com.bstek.dorado.view.widget.Component c : autoForm.getElements()) {
			for (IControlBuilder builder : builders) {
				if (builder.support(c)) {
					builder.build(c, component);
					break;
				}
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof AutoForm;
	}

}
