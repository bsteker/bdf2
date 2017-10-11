package com.bstek.bdf2.jbpm4.listener.impl;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.model.ControlType;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;

/**
 * @author Jacky.gao
 * @since 2013-6-28
 */
@Component("com.bstek.bdf2.jbpm4.listener.impl.AutoFormFilter")
public class AutoFormFilter extends AbstractFilter {

	public void filter(Object component,Collection<ComponentControl> componentControls) {
		AutoForm form=(AutoForm)component;
		String id=form.getId();
		if(StringUtils.isEmpty(id)){
			return;
		}
		ComponentControl cc=this.match(componentControls, component, id);
		if(cc==null){
			return;
		}
		if(cc.getControlType()==null || cc.getControlType().equals(ControlType.ignore)){
			form.setIgnored(true);				
		}else{
			form.setReadOnly(true);						
		}
	}

	public boolean support(Object component) {
		return component instanceof AutoForm;
	}
}
