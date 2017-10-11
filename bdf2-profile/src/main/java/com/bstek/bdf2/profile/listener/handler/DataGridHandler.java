package com.bstek.bdf2.profile.listener.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.DataColumn;
import com.bstek.dorado.view.widget.grid.DataGrid;
import com.bstek.dorado.view.widget.grid.GridSupport;

/**
 * @author Jacky.gao
 * @since 2013-3-4
 */
@org.springframework.stereotype.Component
public class DataGridHandler extends AbstractComponentHandler {

	public void handle(IComponentService componentService, Object control,
			String assignTargetId,Map<String,ComponentInfo> mapInCache) {
		GridSupport grid=this.getControl(control);
		String controlId=grid.getId();
		if(StringUtils.isEmpty(controlId)){
			setControlPropertiesAndEvents(componentService, assignTargetId, grid, controlId,"DataGrid",mapInCache);
		}
		List<Column> columns=grid.getColumns();
		if(columns==null)return;
		sortChildControls(componentService, assignTargetId, controlId,"DataGrid",columns,mapInCache);
		for(Column col:columns){
			if(!(col instanceof DataColumn))continue;
			DataColumn dataColumn=(DataColumn)col;
			String id=dataColumn.getName();
			if(StringUtils.isEmpty(id)){
				id=dataColumn.getProperty();
			}
			if(StringUtils.isNotEmpty(id)){
				setControlPropertiesAndEvents(componentService, assignTargetId, col,id,"DataColumn",mapInCache);				
			}
			Control editor=dataColumn.getEditor();
			if(editor==null || !(editor instanceof Container))continue;
			Container container=(Container)editor;
			id=container.getId();
			if(StringUtils.isNotEmpty(id)){
				sortChildControls(componentService, assignTargetId,id,editor.getClass().getSimpleName(),container.getChildren(),mapInCache);				
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof DataGrid;
	}

	@Override
	protected String getChildrenId(Object obj) {
		String name=null;
		if(obj instanceof Column){
			name=((Column)obj).getName();
			if(StringUtils.isEmpty(name) && obj instanceof DataColumn){
				DataColumn dc=(DataColumn)obj;
				name=dc.getProperty();
			}
		}
		if(obj instanceof Component){
			name=((Component)obj).getId();
		}
		return name;
	}

}
