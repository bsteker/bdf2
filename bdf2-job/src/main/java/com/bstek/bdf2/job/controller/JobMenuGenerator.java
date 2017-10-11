package com.bstek.bdf2.job.controller;

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
public class JobMenuGenerator extends HibernateDao implements IMenuGenerator {

	public void generate(Session session,String rootId) {
		Url secondUrl=this.createUrl("任务调度", "url(skin>common/icons.gif) -122px -101px", rootId, 3,null);
		session.save(secondUrl);
		Url childUrl=this.createUrl("Job定义", "url(skin>common/icons.gif) -122px -41px", secondUrl.getId(), 1,"bdf2.job.view.job.JobMaintain.d");
		session.save(childUrl);
		childUrl=this.createUrl("调度服务监控", "url(skin>common/icons.gif) -222px -81px", secondUrl.getId(), 3,"bdf2.job.view.analysis.SchedulerAnalysis.d");
		session.save(childUrl);
		childUrl=this.createUrl("节假日设定", "url(skin>common/icons.gif) -102px -41px", secondUrl.getId(), 2,"bdf2.job.view.calendar.CalendarMaintain.d");
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
