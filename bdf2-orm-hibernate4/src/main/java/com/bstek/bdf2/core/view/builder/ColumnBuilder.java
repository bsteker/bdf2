package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.ColumnGroup;
import com.bstek.dorado.view.widget.grid.DataColumn;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Component
public class ColumnBuilder extends AbstractControlBuilder{
	public void build(Object control, ViewComponent parentViewComponent) {
		Column column=(Column)control;
		String id=column.getName();
		if(StringUtils.isEmpty(id)){
			id=column.getName();
		}
		if(StringUtils.isEmpty(id) && (column instanceof DataColumn)){
			id=((DataColumn)column).getProperty();
		}
		if(StringUtils.isEmpty(id)){
			id=column.getCaption();
		}
		ViewComponent component=generateViewComponent(id,column.getClass());
		component.setDesc(column.getCaption());
		if(StringUtils.isEmpty(component.getId())){
			component.setEnabled(false);				
		}
		parentViewComponent.addChildren(component);
		
		if(column instanceof ColumnGroup){
			for(Column col:((ColumnGroup)column).getColumns()){
				for(IControlBuilder builder:builders){
					if(builder.support(col)){
						builder.build(col, component);
						break;
					}
				}				
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof Column;
	}

}
