package com.bstek.bdf2.jbpm4.listener.impl;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.model.ControlType;
import com.bstek.dorado.view.widget.base.tab.ControlTab;

/**
 * @author Jacky.gao
 * @since 2013-7-1
 */
@Component("com.bstek.bdf2.jbpm4.listener.impl.ControlTabFilter")
public class ControlTabFilter extends AbstractFilter {

	public void filter(Object component, Collection<ComponentControl> controls) {
		ControlTab tab=(ControlTab)component;
		String id=tab.getName();
		if(StringUtils.isEmpty(id)){
			id=tab.getCaption();
		}
		if(StringUtils.isEmpty(id)){
			return;
		}
		ComponentControl cc=this.match(controls, component, id);
		if(cc==null){
			return;
		}
		if(cc.getControlType()==null || cc.getControlType().equals(ControlType.ignore)){
			tab.setIgnored(true);
		}else{
			tab.setDisabled(true);			
		}
	}

	public boolean support(Object component) {
		return component instanceof ControlTab;
	}
}
