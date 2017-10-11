package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.form.Label;

@Component
public class LabelBuilder extends AbstractControlBuilder {

	public void build(Object control, ViewComponent parentViewComponent) {
		Label label = (Label) control;
		String id = label.getId();
		ViewComponent component = null;
		if (StringUtils.isNotEmpty(id)) {
			component = this.generateViewComponent(id,  Label.class);
			if (StringUtils.isEmpty(label.getText())) {
				component.setDesc(label.getText());
			}
		}else if (StringUtils.isEmpty(label.getText())) {
			component = this.generateViewComponent(label.getText(),Label.class);
		}
		if(component!=null){
			if(StringUtils.isEmpty(component.getId())){
				component.setEnabled(false);
			}
			parentViewComponent.addChildren(component);			
		}
	}

	public boolean support(Object control) {
		return control instanceof Label;
	}

}
