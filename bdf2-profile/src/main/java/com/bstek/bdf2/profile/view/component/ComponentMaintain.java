package com.bstek.bdf2.profile.view.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.bdf2.core.view.ViewManagerHelper;
import com.bstek.bdf2.core.view.builder.IControlBuilder;
import com.bstek.bdf2.profile.ProfileHibernateDao;
import com.bstek.bdf2.profile.model.AssignTarget;
import com.bstek.bdf2.profile.model.ComponentEvent;
import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.model.ComponentProperty;
import com.bstek.bdf2.profile.model.ComponentSort;
import com.bstek.bdf2.profile.model.ComponentValidator;
import com.bstek.bdf2.profile.model.UrlDefinition;
import com.bstek.bdf2.profile.model.ValidatorDef;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.bdf2.profile.service.IProfileDataService;
import com.bstek.bdf2.profile.service.IValidatorService;
import com.bstek.bdf2.profile.view.RuleSetHelper;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewState;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Jacky.gao
 * @since 2013-3-4
 */
@Component("bdf2.profile.componentMaintain")
public class ComponentMaintain extends ProfileHibernateDao implements InitializingBean{
	private IProfileDataService dataService;
	@Autowired
	@Qualifier(IComponentService.BEAN_ID)
	private IComponentService componentService;
	@Autowired
	@Qualifier(IValidatorService.BEAN_ID)
	private IValidatorService validatorService;
	@Autowired
	@Qualifier(ViewManagerHelper.BEAN_ID)
	private ViewManagerHelper viewManagerHelper;
	private Collection<IControlBuilder> builders;
	
	@Autowired
	@Qualifier(RuleSetHelper.BEAN_ID)
	private RuleSetHelper ruleSetHelper;
	
	@DataProvider
	public Collection<UrlDefinition> loadUrls(Map<String,Object> parameter){
		String parentId=(String)parameter.get("parentId");
		String companyId=(String)parameter.get("companyId");
		return dataService.loadUrls(companyId,parentId);
	}
	
	@Expose
	public void refreshCache(){
		componentService.initComponentToCache();
		validatorService.initValidatorsToCache();
	}
	
	@Expose
	public void deleteValidators(String validatorId,String componentId){
		Session session=this.getSessionFactory().openSession();
		try{
			String hql="delete "+ComponentValidator.class.getName()+" where validator.id=:validatorId and componentId=:componentId";
			session.createQuery(hql).setString("validatorId", validatorId).setString("componentId", componentId).executeUpdate();
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public String insertValidators(String validatorId,String url,String componentInfoId,String controlId,String assignTargetId,String type){
		String msg=null;
		boolean insertAble=true;
		String hql="select count(*) from "+ComponentValidator.class.getName()+" where validator.id=:validatorId and componentId=:componentId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("validatorId",validatorId);
		map.put("componentId",componentInfoId);
		int count=this.queryForInt(hql, map);
		if(count>0){
			msg=validatorId;
			insertAble=false;
		}
		if(insertAble){
			Session session=this.getSessionFactory().openSession();
			try{
				Object obj=session.get(ComponentInfo.class,componentInfoId);
				if(obj==null){
					ComponentInfo c=new ComponentInfo();
					c.setId(componentInfoId);
					c.setControlId(controlId);
					c.setAssignTargetId(assignTargetId);
					c.setType(type);
					c.setUrl(url);					
					session.save(c);
				}
				ComponentValidator cv=new ComponentValidator();
				cv.setComponentId(componentInfoId);
				cv.setId(UUID.randomUUID().toString());
				ValidatorDef def=new ValidatorDef();
				def.setId(validatorId);
				cv.setValidator(def);
				session.save(cv);				
			}finally{
				session.flush();
				session.close();
			}
		}
		return msg;
	}
	
	@DataProvider
	public void loadAssignTargets(Page<AssignTarget> page,Criteria criteria){
		dataService.loadAssignTargets(page, criteria);
	}
	
	@DataProvider
	public void loadAllValidators(Page<ValidatorDef> page,Criteria criteria) throws Exception{
		String hql="from "+ValidatorDef.class.getName()+" v ";
		ParseResult result=this.parseCriteria(criteria, true, "v");
		if(result!=null){
			hql+="where "+result.getAssemblySql().toString();
			this.pagingQuery(page, hql,"select count(*) "+hql,result.getValueMap());
		}else{
			this.pagingQuery(page, hql,"select count(*) "+hql);
		}
	}
	
	@DataProvider
	public Collection<ValidatorDef> loadValidators(String componentId){
		List<ValidatorDef> validators=new ArrayList<ValidatorDef>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("componentId", componentId);
		List<ComponentValidator> result=this.query("from "+ComponentValidator.class.getName()+" where componentId=:componentId", map);
		for(ComponentValidator validator:result){
			validators.add(validator.getValidator());
		}
		return validators;
	}
	
	@DataProvider
	public Collection<NameData> loadControlProperties(String componentClassName){
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule(componentClassName);
		if(rule==null)return null;
		List<NameData> list=new ArrayList<NameData>();
		for(String name:rule.getProperties().keySet()){
			NameData data=new NameData();
			data.setName(name);
			list.add(data);
		}
		return list;
	}
	
	@DataProvider
	public Collection<NameData> loadControlEvents(String componentClassName){
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule(componentClassName);
		if(rule==null)return null;
		List<NameData> list=new ArrayList<NameData>();
		for(String name:rule.getClientEvents().keySet()){
			NameData data=new NameData();
			data.setName(name);
			list.add(data);
		}
		return list;
	}
	
	
	@DataProvider
	public Collection<ComponentSort> loadSorts(Collection<Map<String,Object>> children,String parentComponentId){
		List<ComponentSort> result=componentService.loadComponentSorts(parentComponentId);
		int order=0;
		for(Map<String,Object> map:children){
			String controlId=(String)map.get("id");
			boolean add=true;
			for(ComponentSort s:result){
				if(controlId.equals(s.getControlId())){
					add=false;
					break;
				}
			}
			if(!add)continue;
			ComponentSort sort=new ComponentSort();
			sort.setParentComponentId(parentComponentId);
			sort.setControlId(controlId);
			sort.setOrder(order);
			order++;
			result.add(sort);
		}
		return result;
	}
	
	@Expose
	public void resetSorts(String parentComponentId){
		String hql="delete "+ComponentSort.class.getName()+" where parentComponentId=:parentComponentId";
		Session session=this.getSessionFactory().openSession();
		try{
			session.createQuery(hql).setString("parentComponentId", parentComponentId).executeUpdate();
		}finally{
			session.flush();
			session.close();
		}
	}
	
	
	@DataResolver
	public void saveEvents(Collection<ComponentEvent> events,String url,String parentComponentId,String controlId,String assignTargetId,String type){
		Session session=this.getSessionFactory().openSession();
		try{
			this.checkCompoentInfo(session, url, parentComponentId, controlId, assignTargetId, type);
			session.createQuery("delete "+ComponentEvent.class.getName()+" where componentId=:parentComponentId").setString("parentComponentId",parentComponentId).executeUpdate();
			for(ComponentEvent event:events){
				event.setId(UUID.randomUUID().toString());
				event.setComponentId(parentComponentId);
				session.save(event);
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@DataResolver
	public void saveProperties(Collection<ComponentProperty> properties,String url,String parentComponentId,String controlId,String assignTargetId,String type){
		Session session=this.getSessionFactory().openSession();
		try{
			this.checkCompoentInfo(session, url, parentComponentId, controlId, assignTargetId, type);
			session.createQuery("delete "+ComponentProperty.class.getName()+" where componentId=:parentComponentId").setString("parentComponentId",parentComponentId).executeUpdate();
			for(ComponentProperty prop:properties){
				prop.setId(UUID.randomUUID().toString());
				prop.setComponentId(parentComponentId);
				session.save(prop);
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@DataResolver
	public void saveSorts(Collection<ComponentSort> sorts,String url,String parentComponentId,String controlId,String assignTargetId,String type){
		Session session=this.getSessionFactory().openSession();
		try{
			this.checkCompoentInfo(session, url, parentComponentId, controlId, assignTargetId, type);
			session.createQuery("delete "+ComponentSort.class.getName()+" where parentComponentId=:parentComponentId").setString("parentComponentId",parentComponentId).executeUpdate();
			for(ComponentSort sort:sorts){
				sort.setId(UUID.randomUUID().toString());
				session.save(sort);
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	private void checkCompoentInfo(Session session,String url,String parentComponentId,String controlId,String assignTargetId,String type){
		String hql="select count(*) from "+ComponentInfo.class.getName()+" where id=:id";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", parentComponentId);
		int count=this.queryForInt(hql, map);
		if(count==0){
			ComponentInfo component=new ComponentInfo();
			component.setId(parentComponentId);
			component.setControlId(controlId);
			component.setAssignTargetId(assignTargetId);
			component.setUrl(url);
			component.setType(type);
			session.save(component);
		}
	}
	
	@DataProvider
	public Collection<ComponentProperty> loadProperties(String componentId){
		if(componentId==null)return null;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("componentId",componentId);
		return this.query("from "+ComponentProperty.class.getName()+" where componentId=:componentId",map);
	}
	@DataProvider
	public Collection<ComponentProperty> loadEvents(String componentId){
		if(componentId==null)return null;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("componentId",componentId);
		return this.query("from "+ComponentEvent.class.getName()+" where componentId=:componentId",map);
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public List<ViewComponent> loadViewComponents(String viewName,String assignTargetId) throws Exception {
		if (StringUtils.isEmpty(viewName)) {
			return Collections.EMPTY_LIST;
		}
		String orgViewName=viewName;
		List<ViewComponent> components = new ArrayList<ViewComponent>();
		if (viewName.toLowerCase().endsWith(".d")) {
			viewName = viewName.substring(0, viewName.length() - 2);
		}

		String VIEWSTATE_KEY = ViewState.class.getName();
		DoradoContext context = DoradoContext.getCurrent();
		context.setAttribute(VIEWSTATE_KEY, ViewState.rendering);
		try {
			ViewConfig viewConfig = viewManagerHelper.getViewConfig(context,viewName);
			if (viewConfig == null) {
				return components;
			}
			ViewComponent model=new ViewComponent();
			model.setName("Modle");
			model.setIcon("url(skin>common/icons.gif) -262px -41px");
			model.setEnabled(false);
			components.add(model);
			
			for(String dataTypeName:viewConfig.getPrivateDataTypeNames()){
				viewConfig.getDataType(dataTypeName);
				ViewComponent dataTypeComponent=this.buildDataTypeViewComonent(viewConfig.getDataType(dataTypeName));
				if(dataTypeComponent!=null){
					model.addChildren(dataTypeComponent);
				}
			}
			
			View view = viewConfig.getView();
			if(view==null)return Collections.EMPTY_LIST;
			ViewComponent root = new ViewComponent();
			components.add(root);
			root.setName(View.class.getSimpleName());
			root.setIcon("url(skin>common/icons.gif) 0px -21px");
			root.setEnabled(false);
			for (com.bstek.dorado.view.widget.Component component : view.getChildren()) {
				for (IControlBuilder builder : builders) {
					if (!builder.support(component)) {
						continue;
					}
					builder.build(component, root);
					break;
				}
			}
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("viewName",orgViewName);
			map.put("assignTargetId",assignTargetId);
			List<ComponentInfo> urlComponents = this.query("from " + ComponentInfo.class.getName()+ " c where c.url=:viewName and c.assignTargetId=:assignTargetId", map);
			buildViewComponents(components,urlComponents);
			return components;
		} finally {
			context.setAttribute(VIEWSTATE_KEY, ViewState.servcing);
		}
	}
	
	private ViewComponent buildDataTypeViewComonent(DataType dataType){
		if(dataType==null || !(dataType instanceof EntityDataType))return null;
		ViewComponent component=new ViewComponent();
		EntityDataType dt=(EntityDataType)dataType;
		component.setId(dt.getName());
		component.setName("DataType");
		component.setEnabled(true);
		component.setIcon("url(skin>common/icons.gif) -302px 1px");
		for(String propertyDefName:dt.getPropertyDefs().keySet()){
			ViewComponent field=new ViewComponent();
			field.setId(propertyDefName);
			field.setName("PropertyDef");
			field.setIcon("url(skin>common/icons.gif) -182px -141px");
			field.setEnabled(true);
			component.addChildren(field);
		}
		return component;
	}
	
	private void buildViewComponents(Collection<ViewComponent> viewComponents,List<ComponentInfo> urlComponents){
		for(ViewComponent vc:viewComponents){
			for(ComponentInfo uc:urlComponents){
				if(vc.getId()!=null && vc.getName().equals(uc.getType()) && vc.getId().equals(uc.getControlId())){
					vc.setUse(true);
					vc.setComponentInfoId(uc.getId());
					break;
				}
			}
			if(vc.getName().equals("DataGrid") || vc.getName().equals("AutoForm")){
				vc.setSortabled(true);
			}
			if(vc.getComponentInfoId()==null){
				vc.setComponentInfoId(UUID.randomUUID().toString());
			}
			buildViewComponents(vc.getChildren(),urlComponents);
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		Collection<IProfileDataService> dataServices=this.getApplicationContext().getBeansOfType(IProfileDataService.class).values();
		if(dataServices.size()==0){
			throw new RuntimeException("You need implementation ["+IProfileDataService.class.getName()+"] interface when use bdf2-profile module!");
		}
		dataService=dataServices.iterator().next();
		builders = this.getApplicationContext().getBeansOfType(IControlBuilder.class).values();
	}
}
