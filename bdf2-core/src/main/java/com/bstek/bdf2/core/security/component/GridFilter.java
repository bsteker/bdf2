package com.bstek.bdf2.core.security.component;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.ColumnGroup;
import com.bstek.dorado.view.widget.grid.DataColumn;
import com.bstek.dorado.view.widget.grid.DataGrid;
import com.bstek.dorado.view.widget.grid.GridSupport;

/**
 * 对Grid及DataGrid进行权限过滤
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class GridFilter implements IComponentFilter {

	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		GridSupport grid = (GridSupport) component;
		String id=grid.getId();
		boolean authority=true;
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,id);
		}
		if(!authority){
			grid.setIgnored(true);
			return;
		}
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.write, url,id);
		}
		if(!authority){
			grid.setReadOnly(true);
		}
		
		Map<String,PropertyDef> dataTypePropertyDefs = null;
		if(grid instanceof DataGrid){
			DataGrid dataGrid = (DataGrid)grid;
			EntityDataType entityDataType = dataGrid.getDataType();
			if(entityDataType != null)
				dataTypePropertyDefs = entityDataType.getPropertyDefs();
		}
		for (Column col : grid.getColumns()) {
			if(col instanceof ColumnGroup){
				this.filterGroupColumn(url,(ColumnGroup)col,authentication,dataTypePropertyDefs);
			}else{
				this.filterDataColumn(url,col,authentication,dataTypePropertyDefs);
			}
		}

	}

	private void filterDataColumn(String url,Column column,UserAuthentication authentication,Map<String,PropertyDef> dataTypePropertyDefs)
			throws Exception {
		String name=column.getName();
		boolean authority=true;
		if(StringUtils.isNotEmpty(name)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,name);
		}
		if(!authority){
			column.setIgnored(true);
			return;
		}
		if (column instanceof DataColumn && StringUtils.isNotEmpty(name)) {
			DataColumn dataColumn = (DataColumn) column;
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.write, url,name);
			if(!authority){
				dataColumn.setReadOnly(true);
				return;
			}
		}
		if (column instanceof DataColumn) {
			DataColumn dataColumn = (DataColumn) column;
			String caption = column.getCaption();
			if(!StringUtils.isNotEmpty(column.getCaption()) && dataTypePropertyDefs != null){
				PropertyDef propertydef = dataTypePropertyDefs.get(dataColumn.getProperty());
				if(propertydef != null){
					caption = propertydef.getLabel();
				}
			}
			if(StringUtils.isNotEmpty(caption)){
				authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,caption);				
			}
			if(!authority){
				dataColumn.setIgnored(true);
				return;
			}
			if(StringUtils.isNotEmpty(caption)){
				authority=SecurityUtils.checkComponent(authentication, AuthorityType.write, url,caption);				
			}
			if(!authority){
				dataColumn.setReadOnly(true);
				return;
			}
		}
	}
	
	/**
	 * 过滤DataGrid中的GroupColumn类型的列的控件权限
	 * @param component
	 * @param securityAdapter
	 * @param metaData
	 * @param dataTypePropertyDefs
	 * @throws Exception
	 */
	private void filterGroupColumn(String url,ColumnGroup columnGroup,UserAuthentication authentication,Map<String,PropertyDef> dataTypePropertyDefs)
			throws Exception {
		String caption=columnGroup.getCaption();
		boolean authority=true;
		if(StringUtils.isNotEmpty(caption)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,caption);
		}
		if(!authority){
			columnGroup.setIgnored(true);
		}
		for(Column column : columnGroup.getColumns()){
			if(column instanceof ColumnGroup){
				this.filterGroupColumn(url,(ColumnGroup)column,authentication, dataTypePropertyDefs);
			}else{
				this.filterDataColumn(url,column,authentication, dataTypePropertyDefs);
			}
		}
	}
	public boolean support(Component component) {
		return component instanceof GridSupport;
	}
}
