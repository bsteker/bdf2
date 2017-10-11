package com.bstek.bdf2.jbpm4.view.assignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.AssignmentDef;
import com.bstek.bdf2.jbpm4.model.AssignmentInfo;
import com.bstek.bdf2.jbpm4.view.assignment.provider.IAssignmentProvider;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.SubViewHolder;
import com.bstek.dorado.view.widget.base.Dialog;

@Component("bdf2.jbpm4.assignmentMaintain")
public class AssignmentMaintain extends Jbpm4HibernateDao implements InitializingBean{
	private Collection<IAssignmentProvider> providers;
	@DataProvider
	public void loadAssignmentDefs(Page<AssignmentDef> page,Criteria criteria){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		DetachedCriteria dc=this.buildDetachedCriteria(criteria, AssignmentDef.class);
		Property p=Property.forName("companyId");
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			dc.add(p.eq(getFixedCompanyId()));						
		}else{
			dc.add(p.eq(user.getCompanyId()));			
		}
		this.pagingQuery(page, dc);
	}
	
	@DataProvider
	public Collection<AssignmentType> loadAssignmentTypes(){
		List<AssignmentType> types=new ArrayList<AssignmentType>();
		for(IAssignmentProvider p:providers){
			if(p.isDisabled())continue;
			AssignmentType type=new AssignmentType();
			type.setTypeId(p.getTypeId());
			type.setTypeName(p.getTypeName());
			types.add(type);
		}
		return types;
	}
	
	@DataProvider
	public Collection<AssignmentInfo> loadAssignmentInfos(String assignmentDefId,String type){
		List<AssignmentInfo> result=new ArrayList<AssignmentInfo>();
		for(IAssignmentProvider p:providers){
			if(p.isDisabled() || !p.getTypeId().equals(type))continue;
			Collection<AssignmentInfo> list=p.getAssignmentInfos(assignmentDefId);
			if(list!=null && list.size()>0){
				result.addAll(list);				
			}
		}
		return result;
	}
	
	@DataResolver
	public void saveAssignmentDefs(Collection<AssignmentDef> assignments){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		Session session=this.getSessionFactory().openSession();
		try{
			String companyId=user.getCompanyId();
			if(StringUtils.isNotEmpty(getFixedCompanyId())){
				companyId=getFixedCompanyId();
			}
			for(AssignmentDef def:assignments){
				EntityState state=EntityUtils.getState(def);
				if(state.equals(EntityState.NEW)){
					def.setId(UUID.randomUUID().toString());
					def.setCompanyId(companyId);
					session.save(def);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(def);
				}
				if(state.equals(EntityState.DELETED)){
					for(IAssignmentProvider p:providers){
						if(p.isDisabled() || !p.getTypeId().equals(def.getType()))continue;
						p.deleteAssignmentInfos(def.getId());
					}
					session.delete(def);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}

	public void initAssignmentSubviewHolder(View view){
		for(IAssignmentProvider p:providers){
			if(p.isDisabled() || StringUtils.isEmpty(p.getUrl()))continue;
			Dialog dialog=new Dialog();
			String dialogId="dialogAssignment"+p.getTypeId();
			dialog.setId(dialogId);
			SubViewHolder holder=new SubViewHolder();
			holder.setId("subViewHolder"+p.getTypeId());
			holder.setSubView(p.getUrl());
			StringBuffer script=new StringBuffer();
			script.append("self.hide=function(){view.id(\""+dialogId+"\").hide();};");
			script.append("self.reset=function(){view.id(\"dataSetAssignmentDef\").getData(\"#\").reset(\"infos\");};");
			script.append("self.getAssignmentDefId=function(){return view.id(\"dataSetAssignmentDef\").getData(\"#.id\");};");
			holder.addClientEventListener("onCreate",new DefaultClientEvent(script.toString()));
			dialog.addChild(holder);
			dialog.setCenter(true);
			dialog.setModal(true);
			dialog.setWidth("600");
			dialog.setHeight("500");
			dialog.setMaximizeable(true);
			
			view.addChild(dialog);
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		providers=this.getApplicationContext().getBeansOfType(IAssignmentProvider.class).values();
	}
}
