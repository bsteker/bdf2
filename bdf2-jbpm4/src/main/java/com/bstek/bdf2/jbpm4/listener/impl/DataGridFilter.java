package com.bstek.bdf2.jbpm4.listener.impl;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.model.ControlType;
import com.bstek.dorado.view.widget.grid.DataGrid;

/**
 * @author Jacky.gao
 * @since 2013-7-1
 */
@Component("com.bstek.bdf2.jbpm4.listener.impl.DataGridFilter")
public class DataGridFilter extends AbstractFilter {

	public void filter(Object component,Collection<ComponentControl> controls) {
		DataGrid grid=(DataGrid)component;
		String id=grid.getId();
		if(StringUtils.isEmpty(id)){
			this.filterChildren(grid.getColumns(), controls);
			return;
		}
		ComponentControl cc=this.match(controls, component, id);
		if(cc==null){
			this.filterChildren(grid.getColumns(), controls);
			return;
		}
		if(cc.getControlType()==null || cc.getControlType().equals(ControlType.ignore)){
			grid.setIgnored(true);
			this.filterChildren(grid.getColumns(), controls);
		}else{
			grid.setReadOnly(true);			
		}
	}

	public boolean support(Object component) {
		return component instanceof DataGrid;
	}

}
