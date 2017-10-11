package com.bstek.bdf2.authoritydelegation.view.role.url;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.authoritydelegation.model.ResourceAllocation;
import com.bstek.bdf2.authoritydelegation.service.IAuthoritydelegationService;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.RoleResource;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.service.IUrlService;
import com.bstek.bdf2.core.view.role.RoleMaintain;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;

/**
 * @author lucas.yue@bstek.com
 * 
 *         2013-4-20 下午11:31:19
 */
@Component("bdf2.roleUrlMaintainAd")
public class RoleUrlMaintain extends RoleMaintain {
	@Autowired
	@Qualifier(IUrlService.BEAN_ID)
	private IUrlService urlService;

	@DataProvider
	public List<Url> loadUrls(String parentId, String roleId) throws Exception {
		IUser user = getLoginUser();
		String companyId = user.getCompanyId();
		if (StringUtils.isNotEmpty(getFixedCompanyId())) {
			companyId = getFixedCompanyId();
		}
		if (user.isAdministrator()) {
			String hql = "from " + Url.class.getName()
					+ " u where u.companyId=:companyId";
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("companyId", companyId);
			if (StringUtils.isEmpty(parentId)) {
				hql += " and u.parentId is null order by u.order asc";
			} else {
				hql += " and u.parentId=:parentId order by u.order asc";
				parameterMap.put("parentId", parentId);
			}
			List<Url> urls = this.query(hql, parameterMap);
			List<Url> allUrls = urlService.loadUrlsByRoleId(roleId);
			for (Url url : urls) {
				for (Url roleUrl : allUrls) {
					if (url.getId().equals(roleUrl.getId())) {
						url.setUse(true);
						break;
					}
				}
			}
			return urls;
		} else {
			IAuthoritydelegationService ad = ContextHolder
					.getBean(IAuthoritydelegationService.BEAN_ID_);
			List<Url> controlUrls = (List<Url>) ad.findControllableUrls(
					parentId, user.getUsername());
			List<Url> roleUrls = urlService.loadUrlsByRoleId(roleId);
			for (Url url : controlUrls) {
				for (Url roleUrl : roleUrls) {
					if (url.getId().equals(roleUrl.getId())) {
						url.setUse(true);
						break;
					}
				}
			}
			return controlUrls;
		}
	}

	@Expose
	public void saveRoleUrls(String roleId, Collection<String> ids)
			throws Exception {
		Session session = this.getSessionFactory().openSession();
		try {
			String username = getLoginUser().getUsername();
			Query query = session
					.createQuery(
							"delete "
									+ RoleResource.class.getName()
									+ " rr where rr.roleId=:roleId and urlId in(select resourceId from "
									+ ResourceAllocation.class.getName()
									+ " where resourceOwner=:resourceOwner and resourceType=:resourceType)")
					.setString("roleId", roleId)
					.setString("resourceOwner", username)
					.setString("resourceType",
							ResourceAllocation.ResourceType.U.name());
			query.executeUpdate();
			for (String urlId : ids) {
				RoleResource rr = new RoleResource();
				rr.setId(UUID.randomUUID().toString());
				rr.setRoleId(roleId);
				rr.setUrlId(urlId);
				session.save(rr);
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	private IUser getLoginUser() {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new NoneLoginException("Please login first");
		}
		return user;
	}
}
