package com.bstek.bdf2.jbpm4.pro.security.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.form.FormElement;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;

@org.springframework.stereotype.Component
public class AutoFormFilter implements IComponentFilter {
	private ComponentFilter componentFilter;

	private void filterFormElements(String processDefinitionId, String taskName, Component component,
			Map<String, PropertyDef> dataTypePropertyDefs) throws Exception {
		if (!(component instanceof FormElement)) {
			return;
		}

		FormElement element = (FormElement) component;
		Set<String> componentSignature = new HashSet<String>();
		componentSignature.add(getFormElementLabel(element, dataTypePropertyDefs));
		componentFilter.filter(processDefinitionId, taskName, component, componentSignature);

	}

	private String getFormElementLabel(FormElement element, Map<String, PropertyDef> dataTypePropertyDefs) {
		String label = element.getLabel();
		if (StringUtils.isNotEmpty(label)) {
			return label;
		}
		String property = element.getProperty();
		if (StringUtils.isNotEmpty(property) && dataTypePropertyDefs != null) {
			PropertyDef pd = dataTypePropertyDefs.get(property);
			if (pd != null && StringUtils.isNotEmpty(pd.getLabel())) {
				return pd.getLabel();
			}
		}
		return property;
	}

	public void filter(String processDefinitionId, String taskName, Component component) throws Exception {
		AutoForm form = (AutoForm) component;
		EntityDataType entityDataType = form.getDataType();
		Map<String, PropertyDef> dataTypePropertyDefs = null;
		if (entityDataType != null) {
			dataTypePropertyDefs = entityDataType.getPropertyDefs();
		}
		for (Control control : form.getElements()) {
			this.filterFormElements(processDefinitionId, taskName, control, dataTypePropertyDefs);
		}
	}

	public String getSupportType() {
		return AutoForm.class.getName();
	}

	public void setComponentFilter(ComponentFilter componentFilter) {
		this.componentFilter = componentFilter;
	}
}
