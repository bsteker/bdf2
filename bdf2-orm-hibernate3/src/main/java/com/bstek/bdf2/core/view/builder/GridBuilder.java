package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.GridSupport;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Component
public class GridBuilder  extends AbstractControlBuilder{
	public void build(Object control, ViewComponent parentViewComponent) {
		GridSupport grid=(GridSupport)control;
		String id=grid.getId();
		ViewComponent component=generateViewComponent(id,grid.getClass());
		parentViewComponent.addChildren(component);
		if(StringUtils.isEmpty(id)){
			component.setEnabled(false);
		}
		for(Column col:grid.getColumns()){
			for(IControlBuilder builder:builders){
				if(builder.support(col)){
					builder.build(col, component);
					break;
				}
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof GridSupport;
	}
}
