package com.bstek.bdf2.rapido.controlller;

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
public class RapidoMenuGenerator extends HibernateDao implements IMenuGenerator {

	public void generate(Session session,String rootId) {
		Url secondUrl=this.createUrl("Rapido", "url(skin>common/icons.gif) -142px -61px", rootId,4,null);
		session.save(secondUrl);
		Url childUrl=this.createUrl("Rapido页面设计", "url(skin>common/icons.gif) -182px -61px", secondUrl.getId(), 1,"bdf2.rapido.view.Workspace.d");
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
