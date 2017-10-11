package com.bstek.bdf2.core.view.role.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.model.ComponentDefinition;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.model.UrlComponent;
import com.bstek.bdf2.core.service.IUrlService;
import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.bdf2.core.view.ViewManagerHelper;
import com.bstek.bdf2.core.view.builder.IControlBuilder;
import com.bstek.bdf2.core.view.role.RoleMaintain;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewState;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Component("bdf2.roleComponentMaintain")
public class RoleComponentMaintain extends RoleMaintain implements InitializingBean {
	@Autowired
	@Qualifier(IUrlService.BEAN_ID)
	private IUrlService urlService;
	private Collection<IControlBuilder> builders;
	@Autowired
	@Qualifier(ViewManagerHelper.BEAN_ID)
	private ViewManagerHelper viewManagerHelper;

	@DataProvider
	public Collection<Url> loadUrls(String parentId, String roleId) throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new NoneLoginException("Please login first");
		}
		String companyId = user.getCompanyId();
		if (StringUtils.isNotEmpty(getFixedCompanyId())) {
			companyId = getFixedCompanyId();
		}
		String hql = "from " + Url.class.getName() + " u where u.companyId=:companyId";
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("companyId", companyId);
		if (StringUtils.isEmpty(parentId)) {
			hql += " and u.parentId is null order by u.order asc";
		} else {
			hql += " and u.parentId=:parentId order by u.order asc";
			parameterMap.put("parentId", parentId);
		}
		List<Url> urls = this.query(hql, parameterMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		List<UrlComponent> urlComponents = this.query("from " + UrlComponent.class.getName() + " uc where uc.roleId=:roleId", map);
		for (Url url : urls) {
			for (UrlComponent uc : urlComponents) {
				if (url.getId().equals(uc.getUrlId())) {
					url.setUse(true);
					break;
				}
			}
		}
		return urls;
	}

	@SuppressWarnings("unchecked")
	@DataProvider
	public List<ViewComponent> loadViewComponents(String viewName, String urlId, String roleId) throws Exception {
		if (StringUtils.isEmpty(viewName)) {
			return Collections.EMPTY_LIST;
		}
		List<ViewComponent> components = new ArrayList<ViewComponent>();
		String suffix = Configure.getString("viewPageSuffix", ".d");
		String suffix2 = suffix + "?";
		if (viewName.toLowerCase().endsWith(suffix)) {
			viewName = viewName.substring(0, viewName.length() - 2);
		} else if (viewName.indexOf(suffix2) > -1) {
			viewName = viewName.substring(0, viewName.indexOf(suffix2));
		}
		String VIEWSTATE_KEY = ViewState.class.getName();
		DoradoContext context = DoradoContext.getCurrent();
		context.setAttribute(VIEWSTATE_KEY, ViewState.rendering);
		try {
			ViewConfig viewConfig = viewManagerHelper.getViewConfig(context, viewName);
			if (viewConfig == null) {
				return components;
			}
			ViewComponent root = new ViewComponent();
			components.add(root);
			root.setName(View.class.getSimpleName());
			root.setIcon("url(skin>common/icons.gif) 0px -20px");
			root.setEnabled(false);
			View view = viewConfig.getView();
			if (view == null)
				return Collections.EMPTY_LIST;
			for (com.bstek.dorado.view.widget.Component component : view.getChildren()) {
				for (IControlBuilder builder : builders) {
					if (builder.support(component)) {
						builder.build(component, root);
						break;
					}
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("urlId", urlId);
			map.put("roleId", roleId);
			List<UrlComponent> urlComponents = this.query("from " + UrlComponent.class.getName() + " uc where uc.urlId=:urlId and uc.roleId=:roleId", map);
			buildViewComponents(root.getChildren(), urlComponents);
			return components;
		} finally {
			context.setAttribute(VIEWSTATE_KEY, ViewState.servicing);
		}
	}

	@SuppressWarnings("unchecked")
	@Expose
	public void insertUrlComponents(Map<String, Object> parameter) throws Exception {
		Session session = this.getSessionFactory().openSession();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String roleId = (String) parameter.get("roleId");
			String urlId = (String) parameter.get("urlId");
			map.put("roleId", roleId);
			map.put("urlId", urlId);
			List<UrlComponent> list = this.query("from " + UrlComponent.class.getName() + " uc where uc.urlId=:urlId and uc.roleId=:roleId", map);
			for (UrlComponent uc : list) {
				session.delete(uc.getComponent());
				session.delete(uc);
			}
			Collection<Map<String, Object>> components = (Collection<Map<String, Object>>) parameter.get("components");
			for (Map<String, Object> param : components) {
				String componentId = (String) param.get("id");
				String authorityType = (String) param.get("authorityType");
				ComponentDefinition component = new ComponentDefinition();
				component.setId(UUID.randomUUID().toString());
				component.setComponentId(componentId);
				UrlComponent uc = new UrlComponent();
				uc.setComponent(component);
				uc.setRoleId(roleId);
				uc.setUrlId(urlId);
				uc.setAuthorityType(AuthorityType.valueOf(authorityType));
				uc.setId(UUID.randomUUID().toString());
				session.save(uc);
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	private void buildViewComponents(Collection<ViewComponent> viewComponents, List<UrlComponent> urlComponents) {
		for (ViewComponent vc : viewComponents) {
			for (UrlComponent uc : urlComponents) {
				if (vc.getId() != null && vc.getId().equals(uc.getComponent().getComponentId())) {
					vc.setUse(true);
					vc.setAuthorityType(uc.getAuthorityType());
					break;
				}
			}
			buildViewComponents(vc.getChildren(), urlComponents);
		}
	}

	public void afterPropertiesSet() throws Exception {
		builders = this.getApplicationContext().getBeansOfType(IControlBuilder.class).values();
	}
}
