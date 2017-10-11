package com.bstek.bdf2.jasperreports.controller;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.bstek.bdf2.core.business.IMenuGenerator;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.orm.hibernate.HibernateDao;


/**
 * @author Jacky.gao
 * @since 2013-5-22
 */
public class JasperreportsMenuGenerator extends HibernateDao implements IMenuGenerator {

	public void generate(Session session,String rootId) {
		Url secondUrl=this.createUrl("报表管理", "url(skin>common/icons.gif) -162px -41px", rootId, 10,null);
		session.save(secondUrl);
		Url childUrl=this.createUrl("报表维护", "url(skin>common/icons.gif) -282px -41px", secondUrl.getId(), 1,"bdf2.jasperreports.view.report.ReportMaintain.d");
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
