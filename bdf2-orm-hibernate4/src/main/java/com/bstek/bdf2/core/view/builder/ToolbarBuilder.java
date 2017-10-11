package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.base.AbstractButton;
import com.bstek.dorado.view.widget.base.Button;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Component
public class ToolbarBuilder extends AbstractControlBuilder {
	public void build(Object control, ViewComponent parentViewComponent) {
		AbstractButton button=(AbstractButton)control;
		String id=button.getId();
		ViewComponent component=generateViewComponent(id, button.getClass());
		String caption = null;
		if (button instanceof Button){
			caption = ((Button)button).getCaption();
		}
		if (StringUtils.isEmpty(caption)){
			component.setDesc(id);
		}else{
			component.setDesc(caption);
		}
		if(StringUtils.isEmpty(component.getId())){
			component.setEnabled(false);
		}
		parentViewComponent.addChildren(component);
	}
	public boolean support(Object control){
		if (control instanceof AbstractButton){
			AbstractButton button = (AbstractButton)control;
			return StringUtils.isNotEmpty(button.getId());
		}
		return false;
	}

}
