package com.bstek.bdf2.profile.listener.handler;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.base.Panel;

/**
 * @author Jacky.gao
 * @since 2013-5-17
 */
@Component
public class ContainerHandler extends AbstractComponentHandler {

	public void handle(IComponentService componentService, Object control,
			String assignTargetId,Map<String,ComponentInfo> mapInCache) {
		Container container=this.getControl(control);
		String controlId=container.getId();
		if(StringUtils.isEmpty(controlId)){
			if(container instanceof Panel){
				Panel p=(Panel)container;
				controlId=p.getCaption();
			}
		}
		if(StringUtils.isEmpty(controlId)){
			return;
		}
		setControlPropertiesAndEvents(componentService, assignTargetId, container, controlId,container.getClass().getSimpleName(),mapInCache);
	}

	public boolean support(Object control) {
		return control instanceof Container;
	}

}
