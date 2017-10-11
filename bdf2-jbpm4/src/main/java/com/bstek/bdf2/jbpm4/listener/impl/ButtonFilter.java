package com.bstek.bdf2.jbpm4.listener.impl;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.model.ControlType;
import com.bstek.dorado.view.widget.base.Button;

/**
 * @author Jacky.gao
 * @since 2013-7-1
 */
@Component("com.bstek.bdf2.jbpm4.listener.impl.ButtonFilter")
public class ButtonFilter extends AbstractFilter {

	public void filter(Object component,Collection<ComponentControl> controls) {
		Button button=(Button)component;
		String id=button.getId();
		if(StringUtils.isEmpty(id)){
			id=button.getCaption();
		}
		if(StringUtils.isEmpty(id)){
			return;
		}
		ComponentControl cc=this.match(controls, component, id);
		if(cc==null){
			return;
		}
		if(cc.getControlType()==null || cc.getControlType().equals(ControlType.ignore)){
			button.setIgnored(true);				
		}else{
			button.setDisabled(true);
		}
	}

	public boolean support(Object component) {
		return component instanceof Button;
	}
}
