package com.bstek.bdf2.profile.listener.handler;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.view.widget.base.Button;

/**
 * @author Jacky.gao
 * @since 2013-3-4
 */
@Component
public class ButtonHandler extends AbstractComponentHandler {

	public void handle(IComponentService componentService, Object control,
			String assignTargetId,Map<String,ComponentInfo> mapInCache) {
		Button button=this.getControl(control);
		String controlId=button.getId();
		if(StringUtils.isEmpty(controlId)){
			controlId=button.getCaption();
		}
		if(StringUtils.isEmpty(controlId)){
			return;
		}
		setControlPropertiesAndEvents(componentService, assignTargetId, button, controlId,"Button",mapInCache);
	}
	public boolean support(Object control) {
		return control instanceof Button;
	}
}
