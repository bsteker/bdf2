package com.bstek.bdf2.core.security.component;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.form.AbstractEditor;

/**
 * 实现所有编辑器类型控件权限控制(texteditor,radio,checkbox etc.)
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class EditorFilter implements IComponentFilter {

	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		AbstractEditor editor=(AbstractEditor)component;
		String id=editor.getId();
		boolean authority=true;
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,id);
		}
		if(!authority){
			editor.setIgnored(true);
			return;
		}
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.write, url,id);
		}
		if(!authority){
			editor.setReadOnly(true);
		}
	}
	public boolean support(Component component) {
		return component instanceof AbstractEditor;
	}
}
