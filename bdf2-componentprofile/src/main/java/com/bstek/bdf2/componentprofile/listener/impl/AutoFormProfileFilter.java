/*
 * @author Bing
 * @since 2013-03-05
 */

package com.bstek.bdf2.componentprofile.listener.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.componentprofile.listener.ComponentProfileFilter;
import com.bstek.bdf2.componentprofile.model.ComponentConfig;
import com.bstek.bdf2.componentprofile.model.ComponentConfigMember;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.HideMode;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;
import com.bstek.dorado.view.widget.layout.CommonLayoutConstraint;

// TODO: Auto-generated Javadoc
/**
 * The Class AutoFormProfileListener.
 */
public class AutoFormProfileFilter extends ComponentProfileFilter<AutoForm> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bstek.bdf2.componentprofile.listener.ComponentProfileListener#
	 * rebuildComponentConfig(java.lang.Object,
	 * com.bstek.bdf2.componentprofile.model.ComponentConfig)
	 */
	@Override
	protected void rebuildComponentConfig(AutoForm autoForm, ComponentConfig config) {
		autoForm.setCols(config.getCols());
		if (StringUtils.isNotEmpty(config.getHideMode())
				&& config.getHideMode().equals(ComponentConfig.HIDE_MODE_VISIBILITY)) {
			autoForm.setHideMode(HideMode.visibility);
		} else {
			autoForm.setHideMode(HideMode.display);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bstek.bdf2.componentprofile.listener.ComponentProfileListener#
	 * rebuildComponentConfigMember(java.lang.Object, java.util.Collection)
	 */
	@Override
	protected void rebuildComponentConfigMember(AutoForm autoForm, Collection<ComponentConfigMember> members) {
		List<Control> elements = autoForm.getElements();
		Map<String, Control> sources = new HashMap<String, Control>();
		for (Control control : elements) {
			if (control instanceof AutoFormElement) {
				sources.put(((AutoFormElement) control).getName(), control);
			}
		}
		List<Control> customElements = new ArrayList<Control>();
		Control control = null;
		CommonLayoutConstraint commonLayoutConstraint = null;
		AutoFormElement autoFormElement = null;
		HideMode hideMode = autoForm.getHideMode();
		for (ComponentConfigMember member : members) {
			control = sources.get(member.getControlName());
			if (control != null) {
				control.setHideMode(hideMode);
				customElements.add(control);
				if (control instanceof AutoFormElement) {
					autoFormElement = (AutoFormElement) control;
					autoFormElement.setLabel(member.getCaption());
				}
				Object layoutConstraint = control.getLayoutConstraint();
				if (layoutConstraint == null && (member.getColSpan() != 1 || member.getRowSpan() != 1)) {
					commonLayoutConstraint = new CommonLayoutConstraint();
					commonLayoutConstraint.put("colSpan", member.getColSpan());
					commonLayoutConstraint.put("rowSpan", member.getRowSpan());
					control.setLayoutConstraint(commonLayoutConstraint);
				} else if (layoutConstraint instanceof CommonLayoutConstraint) {
					commonLayoutConstraint = (CommonLayoutConstraint) layoutConstraint;
					commonLayoutConstraint.put("colSpan", member.getColSpan());
					commonLayoutConstraint.put("rowSpan", member.getRowSpan());
				}
				control.setVisible(member.getVisible());
			}
		}
		elements.clear();
		for (Control element : customElements) {
			autoForm.addElement(element);
		}
	}

}
