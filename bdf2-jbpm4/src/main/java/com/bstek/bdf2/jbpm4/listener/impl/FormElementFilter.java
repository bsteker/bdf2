package com.bstek.bdf2.jbpm4.listener.impl;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.model.ControlType;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;

/**
 * @author Jacky.gao
 * @since 2013-6-28
 */
@Component("com.bstek.bdf2.jbpm4.listener.impl.FormElementFilter")
public class FormElementFilter extends AbstractFilter {

	public void filter(Object component,Collection<ComponentControl> controls) {
		AutoFormElement element=(AutoFormElement)component;
		String id=element.getId();
		if(StringUtils.isEmpty(id)){
			id=element.getName();
		}
		if(StringUtils.isEmpty(id)){
			id=element.getProperty();
		}
		if(StringUtils.isEmpty(id)){
			return;
		}
		ComponentControl cc=this.match(controls, component, id);
		if(cc==null){
			return;
		}
		if(cc.getControlType()==null || cc.getControlType().equals(ControlType.ignore)){
			element.setIgnored(true);
		}else{
			element.setReadOnly(true);
		}
	}

	public boolean support(Object component) {
		return component instanceof AutoFormElement;
	}
}
