package com.bstek.bdf2.profile.listener.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.profile.model.ComponentEvent;
import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.model.ComponentProperty;
import com.bstek.bdf2.profile.model.ComponentSort;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public abstract class AbstractComponentHandler implements IComponentHandler{
	@SuppressWarnings("unchecked")
	protected <T> T getControl(Object obj){
		return (T)obj;
	}
	protected String setControlPropertiesAndEvents(IComponentService componentService,
			String assignTargetId,Object control, String controlId,String type,Map<String,ComponentInfo> mapInCache) {
		String viewId=this.getViewId();
		ComponentInfo component=componentService.loadComponentFromCache(viewId, controlId,assignTargetId,type,mapInCache);
		if(component==null)return null;
		List<ComponentProperty> properties=component.getProperties();
		for(ComponentProperty prop:properties){
			String name=prop.getName();
			String value=prop.getValue();
			if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(value)){
				try {
					BeanUtils.setProperty(control, name, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(!(control instanceof ClientEventSupported)){
			return component.getId();
		}
		ClientEventSupported obj=(ClientEventSupported)control;
		List<ComponentEvent> events=component.getEvents();
		for(ComponentEvent event:events){
			String name=event.getName();
			String content=event.getContent();
			if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(content)){
				DefaultClientEvent clientEvent=new DefaultClientEvent();
				clientEvent.setScript(content);
				obj.addClientEventListener(name,clientEvent);
			}
		}
		return component.getId();
	}
	
	@SuppressWarnings("unchecked")
	protected <T> void sortChildControls(IComponentService componentService,
			String assignTargetId, String controlId,String type, List<T> children,Map<String,ComponentInfo> mapInCache) {
		ComponentInfo component=componentService.loadComponentFromCache(this.getViewId(), controlId, assignTargetId,type,mapInCache);
		if(component==null)return;
		List<ComponentSort> sorts=component.getSorts();
		if(sorts.size()==0)return;
		List<Object> tempChildren=new ArrayList<Object>();
		tempChildren.addAll(children);
		children.clear();
		for(ComponentSort sort:sorts){
			String componentId=sort.getControlId();
			for(Object obj:tempChildren){
				String name=this.getChildrenId(obj);
				if(StringUtils.isEmpty(name))continue;
				if(name.equals(componentId)){
					children.add((T)obj);
					break;
				}
			}
		}
	}
	
	protected String getChildrenId(Object obj){
		return null;
	}
	
	protected String getViewId(){
		HttpServletRequest request=DoradoContext.getCurrent().getRequest();
		String url=request.getRequestURI();
		String contextPath=request.getContextPath();
		if(contextPath.length()>1){
			url=url.substring(contextPath.length()+1,url.length());			
		}else{
			url=url.substring(contextPath.length(),url.length());			
		}
		return url;
	}
}
