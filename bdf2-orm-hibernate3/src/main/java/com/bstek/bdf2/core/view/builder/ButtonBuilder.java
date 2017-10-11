package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.base.Button;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Component
public class ButtonBuilder extends AbstractControlBuilder {
	public void build(Object control, ViewComponent parentViewComponent) {
		Button button=(Button)control;
		String id=button.getId();
		if(StringUtils.isEmpty(id)){
			id=button.getCaption();
		}
		ViewComponent component = generateViewComponent(id,button.getClass());
		component.setDesc(button.getCaption());
		if(component!=null && StringUtils.isEmpty(component.getId())){
			component.setEnabled(false);
		}
		if(component!=null){
			parentViewComponent.addChildren(component);			
		}
	}
	public boolean support(Object control){
		return (control instanceof Button) && !(control instanceof com.bstek.dorado.view.widget.base.toolbar.Button);
	}
}
