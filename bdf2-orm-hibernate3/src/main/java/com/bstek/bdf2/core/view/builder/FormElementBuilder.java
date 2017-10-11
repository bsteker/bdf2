package com.bstek.bdf2.core.view.builder;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.form.FormElement;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;

@Component
public class FormElementBuilder extends AbstractControlBuilder {

	public void build(Object control, ViewComponent parentViewComponent) {
		FormElement element = (FormElement) control;
		String id=element.getId();
		if(StringUtils.isEmpty(id)){
			id=element.getProperty();
		}
		if(StringUtils.isEmpty(id)){
			id = element.getLabel();
		}
		if (StringUtils.isEmpty(id)) {
			ViewElement viewElement = element.getParent();
			if (viewElement instanceof AutoForm) {
				EntityDataType entityDataType = ((AutoForm) viewElement).getDataType();
				Map<String, PropertyDef> dataTypePropertyDefs = null;
				if (entityDataType != null) {
					dataTypePropertyDefs = entityDataType.getPropertyDefs();
				}
				id = getFormElementLabel(element, dataTypePropertyDefs);
			}
		}
		
		ViewComponent component =generateViewComponent(id,element.getClass());
		if (StringUtils.isEmpty(id)) {
			component.setEnabled(false);
		}
		parentViewComponent.addChildren(component);

		Control c = element.getEditor();
		for (IControlBuilder builder : builders) {
			if (builder.support(c)) {
				builder.build(c, component);
				break;
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof FormElement;
	}

	private String getFormElementLabel(FormElement element, Map<String, PropertyDef> dataTypePropertyDefs) {
		String property = element.getProperty();
		if (StringUtils.isNotEmpty(property) && dataTypePropertyDefs != null) {
			PropertyDef pd = dataTypePropertyDefs.get(property);
			if (pd != null && StringUtils.isNotEmpty(pd.getLabel())) {
				return pd.getLabel();
			}
		}
		return property;
	}

}
