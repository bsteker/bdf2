package com.bstek.bdf2.core.view.builder;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.base.tab.ControlTab;
import com.bstek.dorado.view.widget.base.tab.Tab;
import com.bstek.dorado.view.widget.base.tab.TabControl;

@Component
public class TabBuilder extends AbstractControlBuilder {

	public void build(Object control, ViewComponent parentViewComponent) {
		TabControl tabControl = (TabControl) control;
		String id = tabControl.getId();
		ViewComponent component = this.generateViewComponent(id, TabControl.class);
		if (StringUtils.isEmpty(component.getId())) {
			component.setEnabled(false);
		}
		parentViewComponent.addChildren(component);
		List<Tab> tabs = tabControl.getTabs();
		for (Tab tab : tabs) {
			ViewComponent subViewComponent = null;
			String name = tab.getName();
			if (StringUtils.isNotEmpty(name)) {
				subViewComponent=this.generateViewComponent(name, tab.getClass());
				if(StringUtils.isNotEmpty(tab.getCaption())){
					subViewComponent.setDesc(tab.getCaption());
				}
			}else if(StringUtils.isNotEmpty(tab.getCaption())){
				subViewComponent=this.generateViewComponent(tab.getCaption(),tab.getClass());
			}
			if (subViewComponent!=null) {
				if(StringUtils.isEmpty(subViewComponent.getId())){
					subViewComponent.setEnabled(false);					
				}
				component.addChildren(subViewComponent);
			}
			if (tab instanceof ControlTab && subViewComponent!=null) {
				ControlTab controlTab = (ControlTab) tab;
				Control c = controlTab.getControl();
				for (IControlBuilder builder : builders) {
					if (builder.support(c)) {
						builder.build(c, subViewComponent);
						break;
					}
				}
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof TabControl;
	}

}
