package com.bstek.bdf2.profile.view.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.profile.ProfileHibernateDao;
import com.bstek.bdf2.profile.model.ValidatorDef;
import com.bstek.bdf2.profile.model.ValidatorEvent;
import com.bstek.bdf2.profile.model.ValidatorProperty;
import com.bstek.bdf2.profile.view.RuleSetHelper;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
@Component("bdf2.profile.validatorMaintain")
public class ValidatorMaintain extends ProfileHibernateDao{
	@Autowired
	@Qualifier(RuleSetHelper.BEAN_ID)
	private RuleSetHelper ruleSetHelper;
	@DataProvider
	public void loadValidators(Page<ValidatorDef> page,Criteria criteria) throws Exception{
		String hql="from "+ValidatorDef.class.getName()+" v";
		ParseResult result=this.parseCriteria(criteria,true,"v");
		if(result!=null){
			Map<String,Object> map=result.getValueMap();
			hql+=" where "+result.getAssemblySql().toString();
			this.pagingQuery(page,hql,"select count(*) "+hql,map);
		}else{
			this.pagingQuery(page,hql,"select count(*) "+hql);			
		}
	}
	
	@DataResolver
	public void saveValidatorProperties(Collection<ValidatorProperty> properties){
		Session session=this.getSessionFactory().openSession();
		try{
			for(ValidatorProperty v:properties){
				EntityState state=EntityUtils.getState(v);
				if(state.equals(EntityState.NEW)){
					v.setId(UUID.randomUUID().toString());
					session.save(v);
				}
				if(state.equals(EntityState.DELETED)){
					session.delete(v);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(v);
				}
			}			
		}finally{
			session.flush();
			session.close();
		}
	}
	@DataResolver
	public void saveValidatorEvents(Collection<ValidatorEvent> events){
		Session session=this.getSessionFactory().openSession();
		try{
			for(ValidatorEvent v:events){
				EntityState state=EntityUtils.getState(v);
				if(state.equals(EntityState.NEW)){
					v.setId(UUID.randomUUID().toString());
					session.save(v);
				}
				if(state.equals(EntityState.DELETED)){
					session.delete(v);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(v);
				}
			}			
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@DataResolver
	public void saveValidators(Collection<ValidatorDef> validators){
		Session session=this.getSessionFactory().openSession();
		try{
			for(ValidatorDef v:validators){
				EntityState state=EntityUtils.getState(v);
				if(state.equals(EntityState.NEW)){
					v.setId(UUID.randomUUID().toString());
					session.save(v);
				}
				if(state.equals(EntityState.DELETED)){
					session.delete(v);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(v);
				}
			}			
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@DataProvider
	public Collection<ValidatorProperty> loadValidatorProperties(String validatorId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("validatorId", validatorId);
		return this.query("from "+ValidatorProperty.class.getName()+" v where validatorId=:validatorId", map);
	}
	@DataProvider
	public Collection<ValidatorEvent> loadValidatorEvents(String validatorId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("validatorId", validatorId);
		return this.query("from "+ValidatorEvent.class.getName()+" v where validatorId=:validatorId", map);
	}
	@DataProvider
	public Collection<PresetValidatorDef> loadPresetValidators(){
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule("BasePropertyDef");
		if(rule==null){
			return null;			
		}
		Collection<PresetValidatorDef> validators=new ArrayList<PresetValidatorDef>();
		Child child=rule.getChild("Validators");
		for(Rule r:child.getConcreteRules()){
			PresetValidatorDef def=new PresetValidatorDef();
			def.setName(r.getName());
			validators.add(def);
		}
		return validators;
		
	}
	@DataProvider
	public Collection<PresetValidatorDef> loadPresetValidatorProperties(String validatorName)throws Exception{
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule(validatorName);
		if(rule==null){
			return null;			
		}
		Collection<PresetValidatorDef> validators=new ArrayList<PresetValidatorDef>();
		for(String name:rule.getProperties().keySet()){
			PresetValidatorDef def=new PresetValidatorDef();
			def.setName(name);
			validators.add(def);
		}
		return validators;
	}
	@DataProvider
	public Collection<PresetValidatorDef> loadPresetValidatorEvents(String validatorName)throws Exception{
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule(validatorName);
		if(rule==null){
			return null;			
		}
		Collection<PresetValidatorDef> validators=new ArrayList<PresetValidatorDef>();
		for(String name:rule.getClientEvents().keySet()){
			PresetValidatorDef def=new PresetValidatorDef();
			def.setName(name);
			validators.add(def);
		}
		return validators;
	}
}
