package com.bstek.bdf2.profile.listener.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;

/**
 * @author Jacky.gao
 * @since 2013-2-28
 */
@org.springframework.stereotype.Component
public class AutoFormHandler extends AbstractComponentHandler {

	public void handle(IComponentService componentService, Object control,
			String assignTargetId,Map<String,ComponentInfo> mapInCache) {
		AutoForm autoForm=this.getControl(control);
		String controlId=autoForm.getId();
		if(StringUtils.isNotEmpty(controlId)){
			setControlPropertiesAndEvents(componentService, assignTargetId,autoForm, controlId,"AutoForm",mapInCache);
		}
		List<Control> controls=autoForm.getElements();
		if(controls==null)return;
		for(Control con:controls){
			String id=con.getId();
			if(con instanceof AutoFormElement){
				if(StringUtils.isEmpty(id)){
					AutoFormElement element=(AutoFormElement)con;
					id=element.getId();
					if(StringUtils.isEmpty(id)){
						id=element.getName();
					}
					if(StringUtils.isEmpty(id)){
						id=element.getProperty();
					}
				}
			}
			if(StringUtils.isNotEmpty(id)){
				setControlPropertiesAndEvents(componentService, assignTargetId,con,id,"AutoFormElement",mapInCache);				
			}
			if(con==null || !(con instanceof Container) || StringUtils.isEmpty(id))continue;
			Container container=(Container)con;
			sortChildControls(componentService, assignTargetId,id,con.getClass().getSimpleName(),container.getChildren(),mapInCache);
		}
		sortChildControls(componentService, assignTargetId,controlId,"AutoForm",controls,mapInCache);
	}

	@Override
	protected String getChildrenId(Object obj) {
		String name=null;
		if(obj instanceof Component){
			name=((Component)obj).getId();
			if(StringUtils.isEmpty(name) && obj instanceof AutoFormElement){
				AutoFormElement element=(AutoFormElement)obj;
				name=element.getName();
				if(StringUtils.isEmpty(name)){
					name=element.getProperty();
				}
			}
		}
		return name;
	}

	public boolean support(Object control) {
		return control instanceof AutoForm;
	}

}
