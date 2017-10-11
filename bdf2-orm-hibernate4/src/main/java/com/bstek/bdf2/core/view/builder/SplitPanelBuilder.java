package com.bstek.bdf2.core.view.builder;

import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.base.SplitPanel;

/**
 * @author Jacky.gao
 * @since 2013-2-21
 */
@Component
public class SplitPanelBuilder extends AbstractControlBuilder {

	public void build(Object control, ViewComponent parentViewComponent) {
		SplitPanel sp=(SplitPanel)control;
		String id=sp.getId();
		ViewComponent component=this.generateViewComponent(id, SplitPanel.class);
		parentViewComponent.addChildren(component);
		buildChild(component, sp.getMainControl());
		buildChild(component, sp.getSideControl());
	}

	private void buildChild(ViewComponent component, Control control) {
		if(control==null)return;
		for(IControlBuilder builder:this.builders){
			if(builder.support(control)){
				builder.build(control, component);
				break;
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof SplitPanel;
	}

}
