package com.bstek.bdf2.core.controller;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IMenuGenerator;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Url;

/**
 * @author Jacky.gao
 * @since 2013-3-20
 */
public class CoreMenuGenerator extends CoreHibernateDao implements IMenuGenerator {

	public void generate(Session session,String rootId) {
		Url secondUrl=this.createUrl("系统默认基本信息维护", "url(skin>common/icons.gif) -100px -20px", rootId, 1,null);
		session.save(secondUrl);
		Url childUrl=this.createUrl("菜单维护", "url(skin>common/icons.gif) -62px -141px", secondUrl.getId(),1,"bdf2.core.view.url.UrlMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("用户维护", "url(skin>common/icons.gif) -142px -101px", secondUrl.getId(),2,"bdf2.core.view.user.UserMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("部门维护", "url(skin>common/icons.gif) -42px -41px", secondUrl.getId(),3,"bdf2.core.view.dept.DeptMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("岗位维护", "url(skin>common/icons.gif) -262px -41px", secondUrl.getId(),4,"bdf2.core.view.position.PositionMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("消息模版维护", "url(skin>common/icons.gif) -302px -61px", secondUrl.getId(),5,"bdf2.core.view.messagetemplate.MessageTemplateMaintain.d");
		session.save(childUrl);
		
		secondUrl=this.createUrl("权限管理", "url(skin>common/icons.gif) -42px -41px", rootId, 2,null);
		session.save(secondUrl);
		childUrl=this.createUrl("角色维护", "url(skin>common/icons.gif) -240px -80px", secondUrl.getId(), 1,"bdf2.core.view.role.RoleMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("URL权限维护", "url(skin>common/icons.gif) -262px -100px", secondUrl.getId(), 2,"bdf2.core.view.role.url.RoleUrlMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("组件权限维护", "url(skin>common/icons.gif) -181px -41px", secondUrl.getId(),3,"bdf2.core.view.role.component.RoleComponentMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("角色成员维护", "url(skin>common/icons.gif) -102px -21px", secondUrl.getId(),4,"bdf2.core.view.role.member.RoleMemberMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("群组维护", "url(skin>common/icons.gif) -101px -121px", secondUrl.getId(),5,"bdf2.core.view.group.GroupMaintain.d");
		session.save(childUrl);
		
	}
	private Url createUrl(String name,String icon,String parentId,int order,String targetUrl){
		IUser user = ContextHolder.getLoginUser();
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		Url url=new Url();
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
