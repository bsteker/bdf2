package com.bstek.bdf2.core.security.component;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.form.FormElement;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;

/**
 * 针对AutoForm及其子元素进行权限过滤
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class AutoFormFilter implements IComponentFilter {
	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		boolean authority=true;
		AutoForm form=(AutoForm)component;
		String id=form.getId();
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication,AuthorityType.read, url,id);
		}
		if(!authority){
			form.setIgnored(true);
			return;
		}
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication,AuthorityType.write, url,id);
		}
		if(!authority){
			form.setReadOnly(true);
		}
		EntityDataType entityDataType = form.getDataType();
		Map<String,PropertyDef> dataTypePropertyDefs = null;
		if(entityDataType != null){
			dataTypePropertyDefs = entityDataType.getPropertyDefs();
		}
		
		for(Control control : form.getElements()){
			this.filterFormElements(url,control,dataTypePropertyDefs,authentication);
		}
	}
	
	private void filterFormElements(String url,Component component,Map<String,PropertyDef> dataTypePropertyDefs,UserAuthentication authentication)
			throws Exception {
		if(!(component instanceof FormElement)){
			return;
		}
		boolean authority=true;
		FormElement element=(FormElement)component;
		String label=getFormElementLabel(element,dataTypePropertyDefs);
		if(StringUtils.isNotEmpty(label)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read,url,label);
		}
		if(!authority){
			element.setIgnored(true);
			return;
		}
		
		if(StringUtils.isNotEmpty(label)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.write,url,label);
		}
		if(!authority){
			element.setReadOnly(true);
			return;
		}
		if(!(element instanceof AutoFormElement)){
			return;
		}
		AutoFormElement autoFormElement=(AutoFormElement)element;
		String name=autoFormElement.getName();
		if(StringUtils.isNotEmpty(name)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read,url,name);
		}
		if(!authority){
			element.setIgnored(true);
			return;
		}
		if(StringUtils.isNotEmpty(name)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.write,url,name);
		}
		if(!authority){
			element.setReadOnly(true);
			return;
		}
	}
	private String getFormElementLabel(FormElement element,Map<String,PropertyDef> dataTypePropertyDefs){
		String label=element.getLabel();
		if(StringUtils.isNotEmpty(label)){
			return label;
		}
		String property=element.getProperty();
		if(StringUtils.isNotEmpty(property) && dataTypePropertyDefs!=null){
			PropertyDef pd=dataTypePropertyDefs.get(property);
			if(pd!=null && StringUtils.isNotEmpty(pd.getLabel())){
				return pd.getLabel();
			}
		}
		return property;
	}

	public boolean support(Component component){
		return component instanceof AutoForm;
	}
}
