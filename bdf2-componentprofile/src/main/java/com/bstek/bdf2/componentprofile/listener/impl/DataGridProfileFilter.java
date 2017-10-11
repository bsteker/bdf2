/*
 * @author Bing
 * @since 2013-03-05
 */
package com.bstek.bdf2.componentprofile.listener.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.componentprofile.listener.ComponentProfileFilter;
import com.bstek.bdf2.componentprofile.model.ComponentConfig;
import com.bstek.bdf2.componentprofile.model.ComponentConfigMember;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.ColumnGroup;
import com.bstek.dorado.view.widget.grid.DataColumn;
import com.bstek.dorado.view.widget.grid.DataGrid;

public class DataGridProfileFilter extends ComponentProfileFilter<DataGrid> {

	@Override
	protected void rebuildComponentConfig(DataGrid dataGrid, ComponentConfig config) {
		try {

			if (StringUtils.isNotEmpty(config.getCols())) {
				int fixedColumnCount = Integer.parseInt(config.getCols());
				dataGrid.setFixedColumnCount(fixedColumnCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void rebuildComponentConfigMember(DataGrid dataGrid, Collection<ComponentConfigMember> columnProfiles) {

		columnProfiles = buildDataBaseGridStructure(columnProfiles);
		List<Column> topColumns = dataGrid.getColumns();
		Map<String, Column> groupColumns = new HashMap<String, Column>();
		if (topColumns.size() > 0) {
			this.getDataGridColumns(topColumns, groupColumns);
		}

		List<Column> customColumns = new ArrayList<Column>();

		for (ComponentConfigMember cp : columnProfiles) {
			Column column = groupColumns.get(cp.getControlName());
			if (column != null) {
				column.setVisible(cp.getVisible());
			}
			if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_DATA_COLUMN) && column != null) {
				((DataColumn) column).setWidth(cp.getWidth());
				customColumns.add(column);
			} else if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_COLUMN_GROUP)) {
				if (column == null) {
					column = new ColumnGroup();
					column.setCaption(cp.getCaption());
					column.setName(cp.getControlName());
				}
				if (cp.getChildren() != null) {
					this.rebuildColumnGroup(cp.getChildren(), (ColumnGroup) column, groupColumns);
				}
				customColumns.add(column);
			} else {
				if (column != null) {
					if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_INDICATOR_COLUMN)) {
						customColumns.add(column);
					} else if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_ROW_NUM_COLUMN)) {
						customColumns.add(column);
					} else if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_ROW_SELECTOR_COLUMN)) {
						customColumns.add(column);
					}
				}
			}
		}
		dataGrid.getColumns().clear();
		for (Column c : customColumns) {
			dataGrid.addColumn(c);
		}

	}

	/**
	 * 获取系统默认DataGrid的所有列，包括ColumnGroup中的多有列到Map对象中
	 * 
	 * @param topColumns
	 * @param groupColumns
	 */
	private void getDataGridColumns(List<Column> topColumns, Map<String, Column> groupColumns) {

		for (Column column : topColumns) {
			if (column instanceof ColumnGroup) {
				this.getDataGridColumns(((ColumnGroup) column).getColumns(), groupColumns);
			}
			String name = column.getName();
			if (StringUtils.isNotEmpty(name)) {
				groupColumns.put(column.getName(), column);
			}
		}
	}

	private void rebuildColumnGroup(Collection<ComponentConfigMember> chilrenColumnProfiles, ColumnGroup columnGroup,
			Map<String, Column> totalColumns) {
		columnGroup.getColumns().clear();
		Column column = null;
		for (ComponentConfigMember cp : chilrenColumnProfiles) {
			column = totalColumns.get(cp.getControlName());
			if (column != null) {
				column.setVisible(cp.getVisible());
			} else {
				continue;
			}
			if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_DATA_COLUMN)) {
				((DataColumn) column).setWidth(cp.getWidth());
				columnGroup.addColumn(column);
			} else if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_COLUMN_GROUP)) {
				column = new ColumnGroup();
				column.setName(cp.getControlName());
				column.setCaption(cp.getCaption());
				if (cp.getChildren() != null) {
					this.rebuildColumnGroup(cp.getChildren(), (ColumnGroup) column, totalColumns);
				}
				columnGroup.addColumn(column);
			} else {
				if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_INDICATOR_COLUMN)) {
					columnGroup.addColumn(column);
				} else if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_ROW_NUM_COLUMN)) {
					columnGroup.addColumn(column);
				} else if (cp.getControlType().equals(ComponentConfigMember.CONTROL_TYPE_ROW_SELECTOR_COLUMN)) {
					columnGroup.addColumn(column);
				}
			}
		}
	}

	private List<ComponentConfigMember> buildDataBaseGridStructure(Collection<ComponentConfigMember> sources) {

		Map<String, ComponentConfigMember> members = new HashMap<String, ComponentConfigMember>();
		for (ComponentConfigMember componentConfigMember : sources) {
			members.put(componentConfigMember.getControlName(), componentConfigMember.clone());
		}

		for (ComponentConfigMember componentConfigMember : sources) {
			String parentControlName = componentConfigMember.getParentControlName();
			if (StringUtils.isNotEmpty(parentControlName)) {
				ComponentConfigMember parentMember = members.get(parentControlName);
				if (parentMember != null) {
					parentMember.addChildren(componentConfigMember);
				}
			}
		}

		List<ComponentConfigMember> targets = new ArrayList<ComponentConfigMember>();
		for (Entry<String, ComponentConfigMember> memberEntry : members.entrySet()) {
			ComponentConfigMember componentConfigMember = memberEntry.getValue();
			String parentControlName = componentConfigMember.getParentControlName();
			if (!StringUtils.isNotEmpty(parentControlName) || members.get(parentControlName) == null) {
				targets.add(componentConfigMember);
			}
		}

		Collections.sort(targets, new Comparator<ComponentConfigMember>() {
			public int compare(ComponentConfigMember o1, ComponentConfigMember o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		return targets;
	}
}
