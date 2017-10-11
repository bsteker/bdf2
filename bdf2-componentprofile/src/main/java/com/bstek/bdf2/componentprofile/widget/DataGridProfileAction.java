/*
 * @author Bing
 * @since 2013-03-05
 */
package com.bstek.bdf2.componentprofile.widget;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.action.AjaxAction;

@Widget(name = "DataGridProfileAction", category = "BDF2", dependsPackage = "dataGridProfileAction", autoGenerateId = true)
@ClientObject(prototype = "dorado.widget.DataGridProfileAction", shortTypeName = "DataGridProfileAction")
public class DataGridProfileAction extends AjaxAction {
	private String dataGrid;

	@IdeProperty(visible = false)
	public String getService() {
		return null;
	}

	@IdeProperty(highlight = 1)
	@ComponentReference("DataGrid")
	public String getDataGrid() {
		return dataGrid;
	}

	public void setDataGrid(String dataGrid) {
		this.dataGrid = dataGrid;
	}
}
