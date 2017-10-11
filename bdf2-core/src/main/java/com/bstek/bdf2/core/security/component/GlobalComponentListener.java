package com.bstek.bdf2.core.security.component;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.ConfigAttribute;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.bdf2.core.security.metadata.ComponentMetadataSource;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.core.Configure;

/**
 * 一个用于对全局所有Component进行权限处理
 * 
 * @since 2013-1-31
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class GlobalComponentListener extends GenericObjectListener<View>
		implements ApplicationContextAware {
	private Collection<IComponentFilter> componentFilterList;
	private ComponentMetadataSource metadataSource;
	private String applicationTitle;
	
	private static final String SECURITY_KEY = "security";
	private boolean needFilter(View view) {
		String securityKey = Configure.getString("bdf2.disableComponentPermission.key", SECURITY_KEY);
		Map<String, Object> metaData = view.getMetaData();
		if (null != metaData && metaData.containsKey(securityKey)){
			return (Boolean)metaData.get(securityKey);
		}
		return true;
	}

	@Override
	public boolean beforeInit(View view) throws Exception {
		return true;
	}

	@Override
	public void onInit(View view) throws Exception {
		if (StringUtils.isEmpty(view.getTitle())) {
			view.setTitle(applicationTitle);
		}
		IUser user = ContextHolder.getLoginUser();
		if (user == null)return;
		
		if (!needFilter(view)) return;
		
		UserAuthentication authentication = new UserAuthentication(user);
		String url = this.getRequestPath();
		String urlId=ComponentMetadataSource.getUrlId(user.getCompanyId(), url);
		try{
			Map<String,Collection<ConfigAttribute>> map=metadataSource.getMetadata(urlId);
			if(map==null){
				url=url.substring(1);
				urlId=ComponentMetadataSource.getUrlId(user.getCompanyId(), url);
				map=metadataSource.getMetadata(urlId);
			}
			SecurityUtils.setComponentsMetadata(map);
			filterComponents(view, authentication, url);			
		}finally{
			SecurityUtils.cleanComponentsMetadata();
		}
	}

	private void filterComponents(ViewElement viewElement, UserAuthentication authentication,
			String url) throws Exception {
		if(viewElement==null || viewElement.getInnerElements()==null || viewElement.getInnerElements().size()==0){
			return;
		}
		for(ViewElement element:viewElement.getInnerElements()){
			if(element instanceof Component){
				Component component=(Component)element;
				for (IComponentFilter filter : componentFilterList) {
					if (filter.support(component)) {
						filter.filter(url, component, authentication);
					}
				}			
			}
			this.filterComponents(element, authentication, url);
		}
	}
	

	private String getRequestPath() {
		String url = ContextHolder.getRequest().getServletPath();
		if (ContextHolder.getRequest().getPathInfo() != null) {
			url += ContextHolder.getRequest().getPathInfo();
		}
		boolean hasContextPath=Configure.getBoolean("bdf2.checkUrlWithContextPath");
	    if(hasContextPath){
	        url = ContextHolder.getRequest().getContextPath()+url;
	    }
		return url;
	}


	public void setMetadataSource(ComponentMetadataSource metadataSource) {
		this.metadataSource = metadataSource;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.componentFilterList = applicationContext.getBeansOfType(
				IComponentFilter.class).values();
	}

	public String getApplicationTitle() {
		return applicationTitle;
	}

	public void setApplicationTitle(String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}
}
