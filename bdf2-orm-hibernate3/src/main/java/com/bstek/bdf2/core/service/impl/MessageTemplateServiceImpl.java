package com.bstek.bdf2.core.service.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.MessageTemplate;
import com.bstek.bdf2.core.service.IMessageTemplateService;
import com.bstek.bdf2.core.view.OrmHibernateDao;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

public class MessageTemplateServiceImpl extends OrmHibernateDao implements IMessageTemplateService{
	public void loadMessageTemplates(Page<MessageTemplate> page,Criteria criteria,String type){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		DetachedCriteria dc=this.buildDetachedCriteria(criteria, MessageTemplate.class);
		if(StringUtils.isNotEmpty(type)){
			dc.add(Property.forName("type").eq(type));
		}
		Property p=Property.forName("companyId");
		dc.add(p.eq(companyId));
		this.pagingQuery(page, dc);
	}
}
