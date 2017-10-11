package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.base.accordion.Accordion;
import com.bstek.dorado.view.widget.base.accordion.Section;

@Component
public class AccordionBuilder extends AbstractControlBuilder {

	public void build(Object control, ViewComponent parentViewComponent) {
		Accordion accordion = (Accordion) control;
		String id = accordion.getId();
		ViewComponent component = new ViewComponent();
		component.setId(id);
		component.setIcon(">dorado/res/"+Accordion.class.getName().replaceAll("\\.", "/")+".png");
		if (StringUtils.isEmpty(id)) {
			component.setEnabled(false);
		}
		component.setName(Accordion.class.getSimpleName());
		parentViewComponent.addChildren(component);

		for (Section section : accordion.getSections()) {
			String name = section.getCaption();
			if (StringUtils.isEmpty(name)) {
				name = section.getName();
			}
			ViewComponent subComponent = new ViewComponent();
			subComponent.setId(name);
			subComponent.setIcon(section.getIcon());
			if (StringUtils.isEmpty(name)) {
				subComponent.setEnabled(false);
			}

			Control c = section.getControl();
			for (IControlBuilder builder : builders) {
				if (builder.support(c)) {
					builder.build(c, subComponent);
					break;
				}
			}
			component.addChildren(subComponent);
		}
	}

	public boolean support(Object control) {
		return control instanceof Accordion;
	}

}
