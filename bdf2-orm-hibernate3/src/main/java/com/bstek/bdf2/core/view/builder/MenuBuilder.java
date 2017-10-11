package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.base.menu.BaseMenuItem;
import com.bstek.dorado.view.widget.base.menu.Menu;
import com.bstek.dorado.view.widget.base.menu.TextMenuItem;

@Component
public class MenuBuilder extends AbstractControlBuilder {
	public void build(Object control, ViewComponent parentViewComponent) {
		Menu menu = (Menu) control;
		String id = menu.getId();
		ViewComponent component = this.generateViewComponent(id, menu.getClass());
		if (StringUtils.isEmpty(id)) {
			component.setEnabled(false);
		}
		for (BaseMenuItem c : menu.getItems()) {
			buildBaseMenuItem(c, component);
		}
		parentViewComponent.addChildren(component);
	}

	private void buildBaseMenuItem(BaseMenuItem baseMenuItem,
			ViewComponent parentViewComponent) {
		if (baseMenuItem instanceof TextMenuItem) {
			TextMenuItem textMenuItem = (TextMenuItem) baseMenuItem;
			String name = textMenuItem.getName();
			ViewComponent component = null;
			if (StringUtils.isNotEmpty(name)) {
				component = this.generateViewComponent(name, textMenuItem.getClass());
				if(StringUtils.isNotEmpty(textMenuItem.getCaption())){
					component.setDesc(textMenuItem.getCaption());
				}
			}else if(StringUtils.isNotEmpty(textMenuItem.getCaption())){
				component = this.generateViewComponent(textMenuItem.getCaption(), textMenuItem.getClass());
			}
			if (component!=null) {
				if(StringUtils.isEmpty(component.getId())){
					component.setEnabled(false);
				}
				parentViewComponent.addChildren(component);
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof Menu;
	}
}
