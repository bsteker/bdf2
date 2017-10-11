package com.bstek.bdf2.authoritydelegation.view;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.authoritydelegation.AuthoritydelegationHibernateDao;
import com.bstek.bdf2.core.business.IMenuGenerator;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Url;

@Component("bdf.authorityDelegationMenuGenerator")
public class AuthorityDelegationMenuGenerator extends
		AuthoritydelegationHibernateDao implements IMenuGenerator {
	public void generate(Session session, String rootId) {
		Url secondUrl = this.createUrl("权限下放",
				"url(skin>common/icons.gif) -122px -81px", rootId, 5, null);
		session.save(secondUrl);
		Url childUrl = this
				.createUrl("下放资源管理",
						"url(skin>common/icons.gif) -262px -41px",
						secondUrl.getId(), 1,
						"bdf2.authoritydelegation.view.allocation.AllocationMaintain.d");
		session.save(childUrl);
		childUrl = this.createUrl("角色资源管理",
				"url(skin>common/icons.gif) -242px -141px", secondUrl.getId(),
				2, "bdf2.authoritydelegation.view.role.url.RoleUrlMaintain.d");
		session.save(childUrl);
		childUrl = this
				.createUrl("角色成员管理", "url(skin>common/icons.gif) -262px -141px",
						secondUrl.getId(), 3,
						"bdf2.authoritydelegation.view.role.member.RoleMemberMaintain.d");
		session.save(childUrl);
	}

	private Url createUrl(String name, String icon, String parentId, int order,
			String targetUrl) {
		String companyId = ContextHolder.getLoginUser().getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		Url url = new Url();
		url.setId(UUID.randomUUID().toString());
		url.setName(name);
		url.setIcon(icon);
		url.setUrl(targetUrl);
		url.setParentId(parentId);
		url.setCompanyId(companyId);
		url.setForNavigation(true);
		url.setOrder(order);
		return url;
	}

}
