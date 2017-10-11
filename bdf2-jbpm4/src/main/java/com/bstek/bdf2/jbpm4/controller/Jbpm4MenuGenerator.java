package com.bstek.bdf2.jbpm4.controller;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.bstek.bdf2.core.business.IMenuGenerator;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.orm.hibernate.HibernateDao;

/**
 * @author Jacky.gao
 * @since 2013-4-11
 */
public class Jbpm4MenuGenerator extends HibernateDao implements IMenuGenerator {

	public void generate(Session session,String rootId) {
		Url secondUrl=this.createUrl("jBPM4流程管理", "dorado/res/icons/jbpm.gif", rootId, 2,null);
		session.save(secondUrl);
		Url childUrl=this.createUrl("配置与监控", "url(skin>common/icons.gif) -102px -21px", secondUrl.getId(),2,"bdf2.jbpm4.view.dashboard.ProcessDashboard.d");
		session.save(childUrl);
		childUrl=this.createUrl("任务分配管理", "url(skin>common/icons.gif) -62px -21px", secondUrl.getId(), 1,"bdf2.jbpm4.view.assignment.AssignmentMaintain.d");
		session.save(childUrl);
	}
	private Url createUrl(String name,String icon,String parentId,int order,String targetUrl){
		String companyId=ContextHolder.getLoginUser().getCompanyId();
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
