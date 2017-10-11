package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.form.AbstractEditor;
import com.bstek.dorado.view.widget.form.TextEditor;

@Component
public class EditorBuilder implements IControlBuilder {

	public void build(Object control, ViewComponent parentViewComponent) {
		AbstractEditor editor = (AbstractEditor) control;
		String id = editor.getId();
		if (StringUtils.isNotEmpty(id)) {
			ViewComponent component = new ViewComponent();
			component.setId(id);
			component.setIcon(">dorado/res/"+TextEditor.class.getName().replaceAll("\\.", "/")+".png");
			component.setName(TextEditor.class.getSimpleName());
			parentViewComponent.addChildren(component);
		}

	}

	public boolean support(Object control) {
		return control instanceof AbstractEditor;
	}

}
