package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.base.Panel;

@Component
public class PanelBuilder extends AbstractControlBuilder {

	public void build(Object control, ViewComponent parentViewComponent) {
		Panel panel = (Panel) control;
		String id = panel.getId();
		if(StringUtils.isEmpty(id)){
			id=panel.getCaption();
		}
		ViewComponent component =this.generateViewComponent(id, Panel.class);;
		component.setDesc(panel.getCaption());
		if(StringUtils.isEmpty(component.getId())){
			component.setEnabled(false);				
		}
		parentViewComponent.addChildren(component);
		for (com.bstek.dorado.view.widget.Component c : panel.getChildren()) {
			for (IControlBuilder builder : builders) {
				if (builder.support(c)) {
					builder.build(c, component);
					break;
				}
			}
		}
		
		for (com.bstek.dorado.view.widget.Component c : panel.getButtons()) {
			for (IControlBuilder builder : builders) {
				if (builder.support(c)) {
					builder.build(c, component);
					break;
				}
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof Panel;
	}

}
