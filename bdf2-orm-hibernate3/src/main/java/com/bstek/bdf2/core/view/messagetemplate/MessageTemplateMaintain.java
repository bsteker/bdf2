package com.bstek.bdf2.core.view.messagetemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IMessageVariableRegister;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.MessageTemplate;
import com.bstek.bdf2.core.model.MessageVariable;
import com.bstek.bdf2.core.service.IMessageTemplateService;
import com.bstek.bdf2.core.view.OrmHibernateDao;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
/**
 * @author Jacky.gao
 * @since 2013-3-24
 */
@Component("bdf2.messageTemplateMaintain")
public class MessageTemplateMaintain extends OrmHibernateDao implements InitializingBean{
	@Autowired
	@Qualifier(IMessageTemplateService.BEAN_ID)
	private IMessageTemplateService messageTemplateService;
	private Collection<IMessageVariableRegister> registers;
	@DataProvider
	public void loadMessageTemplates(Page<MessageTemplate> page,Criteria criteria){
		messageTemplateService.loadMessageTemplates(page, criteria,null);
	}
	@DataResolver
	public void saveMessageTemplates(Collection<MessageTemplate> templates){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		Session session=this.getSessionFactory().openSession();
		try{
			for(MessageTemplate t:templates){
				EntityState state=EntityUtils.getState(t);
				if(state.equals(EntityState.NEW)){
					t.setId(UUID.randomUUID().toString());
					t.setCompanyId(companyId);
					session.save(t);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(t);
				}
				if(state.equals(EntityState.DELETED)){
					session.delete(t);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@DataProvider
	public Collection<MessageVariable> loadMessageVariables(String type){
		List<MessageVariable> result=new ArrayList<MessageVariable>();
		for(IMessageVariableRegister reg:registers){
			if(type.equals(reg.getTypeId())){
				result.addAll(reg.getMessageVariables());
			}
		}
		return result;
	}
	
	@DataProvider
	public Collection<MessageType> loadMessageTypes(){
		List<MessageType> result=new ArrayList<MessageType>();
		for(IMessageVariableRegister reg:registers){
			MessageType type=new MessageType();
			type.setTypeId(reg.getTypeId());
			type.setTypeName(reg.getTypeName());
			result.add(type);
		}
		return result;
	}
	
	public void afterPropertiesSet() throws Exception {
		registers=this.getApplicationContext().getBeansOfType(IMessageVariableRegister.class).values();
	}
}
