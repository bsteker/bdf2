package com.bstek.bdf2.core.security;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.decision.ComponentAccessDecisionManager;
import com.bstek.bdf2.core.security.decision.UrlAccessDecisionManager;
import com.bstek.bdf2.core.security.metadata.ComponentMetadataSource;
import com.bstek.bdf2.core.security.metadata.UrlMetadataSource;
import com.bstek.dorado.core.Configure;

/**
 * @since 2013-1-28
 * @author Jacky.gao
 */
public class SecurityUtils {
	private static final ThreadLocal<Map<String,Collection<ConfigAttribute>>> componentsMetadataThreadLocal=new ThreadLocal<Map<String,Collection<ConfigAttribute>>>();
    protected static final Log logger = LogFactory.getLog(SecurityUtils.class);
	
	public static void setComponentsMetadata(Map<String,Collection<ConfigAttribute>> map){
		componentsMetadataThreadLocal.set(map);
	}
	public static Map<String,Collection<ConfigAttribute>> getComponentsMetadata(){
		return componentsMetadataThreadLocal.get();
	}
	
	public static void cleanComponentsMetadata(){
		componentsMetadataThreadLocal.remove();
	}
	
	/**
	 * 用于检查当前给定的用户是否有权限访问给定的URL
	 * @param authentication 要访问该资源的用户认证对象
	 * @param url 目标URL
	 * @throws AccessDeniedException
	 */
	public static void checkUrl(UserAuthentication authentication,String url) throws AccessDeniedException{
		UrlMetadataSource metadata=ContextHolder.getBean(UrlMetadataSource.BEAN_ID);
		Collection<ConfigAttribute> configAttributes=metadata.getAttributes(url);
        if (logger.isDebugEnabled()) {
            logger.debug("url:"+url + ", authentication:" + authentication + ", configAttributes:"+ configAttributes);
        }
		if(configAttributes==null || configAttributes.size()==0){
			return;
		}
		UrlAccessDecisionManager decisionManager=ContextHolder.getBean(UrlAccessDecisionManager.BEAN_ID);
		decisionManager.decide(authentication,null, configAttributes);
	}
	/**
	 * 判断指定人对指定的URL下的组件有没有访问权限
	 * @param authentication 要访问该URL下的组件的用户认证对象
	 * @param type 要访问该目标组件的哪个权限属性，目前有两个，既读和写
	 * @param url 当前组件所在页面的URL
	 * @param componentId 组件的ID
	 * @return 返回true表示有权限，false则表示无权限
	 */
	public static boolean checkComponent(UserAuthentication authentication,AuthorityType type,String url,String componentId){
		boolean componentPermissionWithoutURL=Configure.getBoolean("bdf2.enableComponentPermissionWithoutURL");
		boolean isRapidoComponent=false;
		String checkComponentId=componentId+"|"+type.toString();
		Collection<ConfigAttribute> configAttributes = getAttribute(url,checkComponentId);
        if (logger.isDebugEnabled() && configAttributes!=null && configAttributes.size()>0) {
            logger.debug("url:"+url + ", checkComponentId:" + checkComponentId + ", authentication:" + authentication + ", configAttributes:"+ configAttributes+ ", componentPermissionWithoutURL:"+componentPermissionWithoutURL);
        }
		if(configAttributes==null || configAttributes.size()==0){
			if(componentPermissionWithoutURL){
				String rapidoCompoentId=checkComponentId.substring(checkComponentId.indexOf("|"));
				configAttributes = getAttribute(url,rapidoCompoentId);
		        if (logger.isDebugEnabled()) {
		            logger.debug("url:"+url + ", checkComponentId:" + checkComponentId + ", authentication:" + authentication + ", configAttributes:"+ configAttributes+ ", componentPermissionWithoutURL:"+componentPermissionWithoutURL);
		        }
				if(configAttributes==null || configAttributes.size()==0){
					return true;
				}else{
					isRapidoComponent=true;
				}
			}else{
				return true;
			}
		}
		ComponentAccessDecisionManager decisionManager=ContextHolder.getBean(ComponentAccessDecisionManager.BEAN_ID);
		boolean granted=decisionManager.decide(authentication,configAttributes);
		if(type.equals(AuthorityType.read) && !granted){
			Collection<ConfigAttribute> writeConfigAttributes;
			checkComponentId=componentId+"|"+AuthorityType.write.toString();
			if(isRapidoComponent){
				String rapidoCompoentId=checkComponentId.substring(checkComponentId.indexOf("|"));
				writeConfigAttributes = getAttribute(url,rapidoCompoentId);
				if(configAttributes!=null && configAttributes.size()>0){
					if(!decisionManager.decide(authentication,writeConfigAttributes)){
						//在读写权限都没有的情况下，默认将直接给用户不能读的权限，也就是不允许用户看到组件
						return false;					
					}else{
						return true;
					}
				}
			}else{
				writeConfigAttributes = getAttribute(url,checkComponentId);
				if(writeConfigAttributes!=null && writeConfigAttributes.size()>0){
					if(!decisionManager.decide(authentication,writeConfigAttributes)){
						//在读写权限都没有的情况下，默认将直接给用户不能读的权限，也就是不允许用户看到组件
						return false;					
					}else{
						return true;
					}
				}
			}
		}
		return granted;
	}
	
	public static int getComponentAttributeSize(String url,String componentId,AuthorityType type){
		String checkComponentId=componentId+"|"+type.toString();
		Collection<ConfigAttribute> configAttributes = getAttribute(url,checkComponentId);
		if(configAttributes==null || configAttributes.size()==0){
			return 0;
		}else{
			return configAttributes.size();
		}
	}
	
	private static Collection<ConfigAttribute> getAttribute(String url,String componentId) {
		ComponentMetadataSource metadata=ContextHolder.getBean(ComponentMetadataSource.BEAN_ID);
		Collection<ConfigAttribute> configAttributes=metadata.getAttributes(url,componentId);
		if(configAttributes==null || configAttributes.size()==0){
			componentId=componentId.substring(1,componentId.length());
			configAttributes=metadata.getAttributes(url,componentId);
		}
		return configAttributes;
	}
	
	public static void refreshUrlSecurityMetadata(){
		UrlMetadataSource metadata=ContextHolder.getBean(UrlMetadataSource.BEAN_ID);
		metadata.initUrlMetaData();
	}
	
	public static void refreshComponentSecurityMetadata(){
		ComponentMetadataSource metadata=ContextHolder.getBean(ComponentMetadataSource.BEAN_ID);
		metadata.initComponentMetadata();
	}
}
