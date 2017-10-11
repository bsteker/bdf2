package com.bstek.bdf2.profile.listener.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.view.widget.base.tab.Tab;
import com.bstek.dorado.view.widget.base.tab.TabControl;

/**
 * @author Jacky.gao
 * @since 2013-5-17
 */
@Component
public class TabControlHandler extends AbstractComponentHandler {

	public void handle(IComponentService componentService, Object control,String assignTargetId,Map<String,ComponentInfo> mapInCache) {
		TabControl tabControl=this.getControl(control);
		String controlId=tabControl.getId();
		if(StringUtils.isEmpty(controlId)){
			return;
		}
		setControlPropertiesAndEvents(componentService, assignTargetId, tabControl, controlId,"TabControl",mapInCache);
		List<Tab> tabs=tabControl.getTabs();
		if(tabs!=null && tabs.size()>0){
			for(Tab tab:tabs){
				String id=tab.getName();
				if(StringUtils.isEmpty(id)){
					id=tab.getCaption();
				}
				setControlPropertiesAndEvents(componentService, assignTargetId, tab,id,tab.getClass().getSimpleName(),mapInCache);
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof TabControl;
	}

}
