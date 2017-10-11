package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Component
public class ToolbarBuilder extends AbstractControlBuilder {
	public void build(Object control, ViewComponent parentViewComponent) {
		ToolBar toolbar=(ToolBar)control;
		String id=toolbar.getId();
		ViewComponent component=generateViewComponent(id, toolbar.getClass());
		parentViewComponent.addChildren(component);
		if(StringUtils.isEmpty(id)){
			component.setEnabled(false);
		}
		for(Control c:toolbar.getItems()){
			for(IControlBuilder builder:builders){
				if(builder.support(c)){
					builder.build(c, component);
					break;
				}
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof ToolBar;
	}

}
