package com.bstek.bdf2.jbpm4.pro.security.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.jbpm4.pro.security.SecurityCheck;
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
 * 
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class GridFilter implements IComponentFilter {
	private SecurityCheck securityCheck;

	private void filterDataColumn(String processDefinitionId, String taskName, Column column,
			Map<String, PropertyDef> dataTypePropertyDefs) throws Exception {
		Set<String> componentSignature = getColumnSignature(column);
		if (column instanceof DataColumn) {
			DataColumn dataColumn = (DataColumn) column;
			String caption = column.getCaption();
			if (!StringUtils.isNotEmpty(column.getCaption()) && dataTypePropertyDefs != null) {
				PropertyDef propertydef = dataTypePropertyDefs.get(dataColumn.getProperty());
				if (propertydef != null) {
					caption = propertydef.getLabel();
					if (StringUtils.isNotEmpty(caption)) {
						componentSignature.add(caption);
					}
				}
			}
		}

		int authority = securityCheck.checkComponent(processDefinitionId, taskName, componentSignature);

		if (authority == ComponentFilter.INVISIBLE) {
			column.setIgnored(true);
		} else if (authority > 0 && column instanceof DataColumn) {
			((DataColumn) column).setReadOnly(authority == ComponentFilter.READONLY);
		}
	}

	/**
	 * 过滤DataGrid中的GroupColumn类型的列的控件权限
	 * 
	 * @param component
	 * @param securityAdapter
	 * @param metaData
	 * @param dataTypePropertyDefs
	 * @throws Exception
	 */
	private void filterGroupColumn(String processDefinitionId, String taskName, ColumnGroup columnGroup,
			Map<String, PropertyDef> dataTypePropertyDefs) throws Exception {
		int authority = securityCheck.checkComponent(processDefinitionId, taskName, getColumnSignature(columnGroup));
		if (authority != -1) {
			columnGroup.setIgnored(authority == ComponentFilter.INVISIBLE);
		}
		for (Column column : columnGroup.getColumns()) {
			if (column instanceof ColumnGroup) {
				this.filterGroupColumn(processDefinitionId, taskName, (ColumnGroup) column, dataTypePropertyDefs);
			} else {
				this.filterDataColumn(processDefinitionId, taskName, column, dataTypePropertyDefs);
			}
		}
	}

	public Set<String> getColumnSignature(Column column) {
		Set<String> columns = new HashSet<String>();
		String token = column.getTags();
		if (StringUtils.isNotEmpty(token)) {
			columns.addAll(Arrays.asList(token.split(",")));
		}
		token = column.getName();
		if (StringUtils.isNotEmpty(token)) {
			columns.add(token);
		}
		token = column.getCaption();
		if (StringUtils.isNotEmpty(token)) {
			columns.add(token);
		}
		return columns;
	}

	public boolean support(Component component) {
		return component instanceof GridSupport;
	}

	public void filter(String processDefinitionId, String taskName, Component component) throws Exception {
		GridSupport grid = (GridSupport) component;

		Map<String, PropertyDef> dataTypePropertyDefs = null;
		if (grid instanceof DataGrid) {
			DataGrid dataGrid = (DataGrid) grid;
			EntityDataType entityDataType = dataGrid.getDataType();
			if (entityDataType != null)
				dataTypePropertyDefs = entityDataType.getPropertyDefs();
		}
		for (Column col : grid.getColumns()) {
			if (col instanceof ColumnGroup) {
				this.filterGroupColumn(processDefinitionId, taskName, (ColumnGroup) col, dataTypePropertyDefs);
			} else {
				this.filterDataColumn(processDefinitionId, taskName, col, dataTypePropertyDefs);
			}
		}

	}

	public String getSupportType() {
		return DataGrid.class.getName();
	}

	public void setSecurityCheck(SecurityCheck securityCheck) {
		this.securityCheck = securityCheck;
	}
}
