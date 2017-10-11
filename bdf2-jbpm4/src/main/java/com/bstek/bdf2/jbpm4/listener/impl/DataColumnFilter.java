package com.bstek.bdf2.jbpm4.listener.impl;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.model.ControlType;
import com.bstek.dorado.view.widget.grid.DataColumn;

/**
 * @author Jacky.gao
 * @since 2013-7-1
 */
@Component("com.bstek.bdf2.jbpm4.listener.impl.DataColumnFilter")
public class DataColumnFilter extends AbstractFilter {

	public void filter(Object component, Collection<ComponentControl> controls) {
		DataColumn col=(DataColumn)component;
		String id=col.getName();
		if(StringUtils.isEmpty(id)){
			id=col.getProperty();
		}
		if(StringUtils.isEmpty(id)){
			id=col.getCaption();
		}
		if(StringUtils.isEmpty(id)){
			return;
		}
		ComponentControl cc=this.match(controls, component, id);
		if(cc==null){
			return;
		}
		if(cc.getControlType()==null || cc.getControlType().equals(ControlType.ignore)){
			col.setIgnored(true);
		}else{
			col.setReadOnly(true);		
		}
	}

	public boolean support(Object component) {
		return component instanceof DataColumn;
	}

}
