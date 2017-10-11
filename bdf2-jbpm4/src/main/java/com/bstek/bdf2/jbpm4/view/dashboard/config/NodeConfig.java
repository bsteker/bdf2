package com.bstek.bdf2.jbpm4.view.dashboard.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.jpdl.internal.activity.TaskActivity;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.task.TaskDefinitionImpl;
import org.jbpm.pvm.internal.wire.descriptor.ObjectDescriptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.message.IMessageSender;
import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.model.MessageTemplate;
import com.bstek.bdf2.core.service.IMessageTemplateService;
import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.bdf2.core.view.ViewManagerHelper;
import com.bstek.bdf2.core.view.builder.IControlBuilder;
import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.context.GenericTaskAssignmentHandler;
import com.bstek.bdf2.jbpm4.context.IJbpm4MessageVariableRegister;
import com.bstek.bdf2.jbpm4.controller.GenerateProcessImage;
import com.bstek.bdf2.jbpm4.job.ITaskOverdueProcessor;
import com.bstek.bdf2.jbpm4.model.AssignmentDef;
import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.model.ControlType;
import com.bstek.bdf2.jbpm4.model.OverdueMethod;
import com.bstek.bdf2.jbpm4.model.ReminderCalendar;
import com.bstek.bdf2.jbpm4.model.ReminderCategory;
import com.bstek.bdf2.jbpm4.model.ReminderType;
import com.bstek.bdf2.jbpm4.model.TaskAssignment;
import com.bstek.bdf2.jbpm4.model.TaskConfig;
import com.bstek.bdf2.jbpm4.model.TaskReminder;
import com.bstek.bdf2.jbpm4.model.ToolbarConfig;
import com.bstek.bdf2.jbpm4.model.ToolbarContent;
import com.bstek.bdf2.jbpm4.model.ToolbarPosition;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.jbpm4.view.toolbar.IToolbarContentProvider;
import com.bstek.bdf2.job.model.JobCalendar;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.common.event.ClientEvent;
import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewState;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.widget.HtmlContainer;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Jacky.gao
 * @since 2013-3-22
 */
@Component("bdf2.jbpm4.nodeConfig")
public class NodeConfig extends Jbpm4HibernateDao implements InitializingBean{
	@Autowired
	@Qualifier(IBpmService.BEAN_ID)
	private IBpmService bpmService;
	@Autowired
	@Qualifier(IMessageTemplateService.BEAN_ID)
	private IMessageTemplateService messageTemplateService;
	
	@Autowired
	@Qualifier(ViewManagerHelper.BEAN_ID)
	private ViewManagerHelper viewManagerHelper;
	
	private Collection<IControlBuilder> builders;
	private Collection<IMessageSender> senders;
	private Collection<String> processors;
	private Collection<Map<String,Object>> toolbarContentProviders;
	
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public List<ViewComponent> loadViewComponents(String url,String taskName,String processDefinitionId) throws Exception {
		if (StringUtils.isEmpty(url)) {
			return Collections.EMPTY_LIST;
		}
		List<ViewComponent> components = new ArrayList<ViewComponent>();
		if (url.toLowerCase().endsWith(".d")) {
			url = url.substring(0, url.length() - 2);
		}

		String VIEWSTATE_KEY = ViewState.class.getName();
		DoradoContext context = DoradoContext.getCurrent();
		context.setAttribute(VIEWSTATE_KEY, ViewState.rendering);
		try {
			ViewConfig viewConfig = viewManagerHelper.getViewConfig(context,url);
			if (viewConfig == null) {
				return components;
			}
			ViewComponent root = new ViewComponent();
			components.add(root);
			root.setName(View.class.getSimpleName());
			root.setIcon("url(skin>common/icons.gif) 0px -20px");
			root.setEnabled(false);
			View view = viewConfig.getView();
			if(view==null)return Collections.EMPTY_LIST;
			for (com.bstek.dorado.view.widget.Component component : view.getChildren()) {
				for (IControlBuilder builder : builders) {
					if (builder.support(component)) {
						builder.build(component, root);
						break;
					}
				}
			}
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("taskName",taskName);
			map.put("processDefinitionId", processDefinitionId);
			List<ComponentControl> componentControls = this.query("from " + ComponentControl.class.getName()+ " where taskName=:taskName and processDefinitionId=:processDefinitionId", map);
			buildViewComponents(root.getChildren(),componentControls);
			return components;
		} finally {
			context.setAttribute(VIEWSTATE_KEY, ViewState.servcing);
		}
	}
	
	private void buildViewComponents(List<ViewComponent> viewComponents,List<ComponentControl> componentControls){
		for(ViewComponent vc:viewComponents){
			for(ComponentControl cc:componentControls){
				if(vc.getId()==null)continue;
				if(vc.getId().equals(cc.getComponentId())){
					vc.setUse(true);
					if(cc.getControlType()==null || cc.getControlType().equals(ControlType.ignore)){
						vc.setAuthorityType(AuthorityType.read);
					}else{
						vc.setAuthorityType(AuthorityType.write);						
					}
					break;
				}
			}
			if(vc.getChildren()!=null){
				this.buildViewComponents(vc.getChildren(), componentControls);
			}
		}
	}
	
	@DataProvider
	public ToolbarConfig loadToolbarConfig(String taskName,String processDefinitionId){
		String hql="from "+ToolbarConfig.class.getName()+" t where t.processDefinitionId=:processDefinitionId and t.taskName=:taskName";
		Session session=this.getSessionFactory().openSession();
		try{
			@SuppressWarnings("unchecked")
			List<ToolbarConfig> configs=session.createQuery(hql).setString("processDefinitionId", processDefinitionId).setString("taskName", taskName).list();
			if(configs.size()>0){
				ToolbarConfig c=configs.get(0);
				c.setUseToolbar(true);
				return c;
			}
			ToolbarConfig config=new ToolbarConfig();
			config.setUseToolbar(false);
			config.setToolbarPosition(ToolbarPosition.top);
			return config;
		}finally{
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Collection<ToolbarContent> loadToolbarContent(String toolbarConfigId){
		String hql="from "+ToolbarContent.class.getName()+" t where t.toolbarConfigId=:toolbarConfigId order by t.order asc";
		Session session=this.getSessionFactory().openSession();
		try{
			return session.createQuery(hql).setString("toolbarConfigId", toolbarConfigId).list();
		}finally{
			session.close();
		}
	}
	
	@DataProvider
	public Collection<Map<String,Object>> loadToolbarContentProviders(){
		return toolbarContentProviders;
	}
	
	private void saveToolbarConfig(ToolbarConfig config,Session session){
		if(config.isUseToolbar()){
			if(StringUtils.isNotEmpty(config.getId())){
				session.update(config);
			}else{
				config.setId(UUID.randomUUID().toString());
				session.save(config);
			}
			if(config.getContents()==null){
				return;
			}
			for(ToolbarContent content:config.getContents()){
				EntityState state=EntityUtils.getState(content);
				if(state.equals(EntityState.NEW)){
					content.setId(UUID.randomUUID().toString());
					content.setToolbarConfigId(config.getId());
					session.save(content);
				}
				if(state.equals(EntityState.MODIFIED) || state.equals(EntityState.MOVED)){
					session.update(content);
				}
				if(state.equals(EntityState.DELETED)){
					session.delete(content);
				}
			}
		}else{
			if(StringUtils.isNotEmpty(config.getId())){
				String hql="delete "+ToolbarContent.class.getName()+" t where t.toolbarConfigId=:toolbarConfigId";
				session.createQuery(hql).setString("toolbarConfigId",config.getId()).executeUpdate();
				session.delete(config);
			}
		}
	}
	
	@DataProvider
	public Collection<Map<String,Object>> loadMessageSenders(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for(IMessageSender sender:senders){
			if(sender.isDisabled())continue;
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", sender.getSenderId());
			map.put("name", sender.getSenderName());
			list.add(map);
		}
		return list;
	}
	
	@DataProvider
	public void loadAssignmentDefs(Page<AssignmentDef> page,Criteria criteria){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		DetachedCriteria dc=this.buildDetachedCriteria(criteria, AssignmentDef.class);
		Property p=Property.forName("companyId");
		dc.add(p.eq(companyId));
		this.pagingQuery(page, dc);
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Collection<TaskAssignment> loadTaskAssignments(String taskName,String processDefinitionId){
		Session session=this.getSessionFactory().openSession();
		try{
			String hql="from "+TaskAssignment.class.getName()+" where taskName=:taskName and processDefinitionId=:processDefinitionId";
			Query query=null;
			if(StringUtils.isNotEmpty(getFixedCompanyId())){
				hql+=" and companyId=:companyId";
				query=session.createQuery(hql);
				query.setString("companyId",getFixedCompanyId());
			}else{
				query=session.createQuery(hql);				
			}
			List<TaskAssignment> result=query.setString("taskName", taskName).setString("processDefinitionId", processDefinitionId).list();
			for(TaskAssignment ta:result){
				AssignmentDef def=(AssignmentDef)session.get(AssignmentDef.class,ta.getAssignmentDefId());
				ta.setAssignmentDefName(def.getName());
			}
			return result;			
		}finally{
			session.flush();
			session.close();
		}
	}
	@DataProvider
	public TaskConfig loadTaskConfig(String taskName,String processDefinitionId){
		String hql="from "+TaskConfig.class.getName()+" where taskName=:taskName and processDefinitionId=:processDefinitionId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("taskName", taskName);
		map.put("processDefinitionId", processDefinitionId);
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			hql+=" and companyId=:companyId";
			map.put("companyId",getFixedCompanyId());			
		}
		List<TaskConfig> result=this.query(hql,map);
		if(result.size()>0){
			return result.get(0);
		}else{
			return new TaskConfig();
		}
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Collection<JobCalendar> loadCalendars(String taskReminderId){
		if(StringUtils.isEmpty(taskReminderId))return null;
		List<JobCalendar> result=new ArrayList<JobCalendar>();
		Session session=this.getSessionFactory().openSession();
		try{
			String hql="from "+ReminderCalendar.class.getName()+" where taskReminderId=:taskReminderId";
			List<ReminderCalendar> list=session.createQuery(hql).setString("taskReminderId", taskReminderId).list();
			for(ReminderCalendar rc:list){
				JobCalendar jc=(JobCalendar)session.get(JobCalendar.class, rc.getJobCalendarId());
				result.add(jc);
			}
		}finally{
			session.flush();
			session.close();
		}
		return result;
	}

	@DataProvider
	public void loadJobCalendars(Page<JobCalendar> page,Criteria criteria){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		DetachedCriteria dc=this.buildDetachedCriteria(criteria, JobCalendar.class);
		Property p=Property.forName("companyId");
		dc.add(p.eq(companyId));
		this.pagingQuery(page, dc);
	}
	
	@DataProvider
	public Collection<Map<String,Object>> loadProcessors(){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		for(String key:processors){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("name",key);
			result.add(map);
		}
		return result;
	}
	@DataResolver
	public void saveConfig(TaskConfig config,Collection<TaskAssignment> assignments,TaskReminder reminder,TaskReminder overdueReminder,Map<String,Object> parameter){
		this.saveConfig(config, assignments, reminder, overdueReminder, null, parameter);
	}
	
	@DataResolver
	public void saveConfig(TaskConfig config,Collection<TaskAssignment> assignments,TaskReminder reminder,TaskReminder overdueReminder,ToolbarConfig toolbarConfig,Map<String,Object> parameter){
		Session session=this.getSessionFactory().openSession();
		try{
			String taskName=(String)parameter.get("taskName");
			String processDefinitionId=(String)parameter.get("processDefinitionId");
			@SuppressWarnings("unchecked")
			Collection<Map<String,Object>> components=(Collection<Map<String,Object>>)parameter.get("components");
			String url=config.getUrl();
			this.saveSecureComponentIds(components, url, processDefinitionId, taskName, session);
			if(StringUtils.isNotEmpty(getFixedCompanyId())){
				reminder.setCompanyId(getFixedCompanyId());
				config.setCompanyId(getFixedCompanyId());
				overdueReminder.setCompanyId(getFixedCompanyId());
				if(toolbarConfig!=null)toolbarConfig.setCompanyId(getFixedCompanyId());
			}
			if(config.getId()==null){
				config.setId(UUID.randomUUID().toString());
				config.setTaskName(taskName);
				config.setProcessDefinitionId(processDefinitionId);
				session.save(config);
			}else{
				session.update(config);
			}
			Query query=null;
			String hql="delete "+TaskAssignment.class.getName()+" where taskName=:taskName and processDefinitionId=:processDefinitionId";
			if(StringUtils.isNotEmpty(getFixedCompanyId())){
				hql+=" and companyId=:companyId";
				query=session.createQuery(hql);
				query.setString("companyId",getFixedCompanyId());
			}else{
				query=session.createQuery(hql);				
			}
			query.setString("taskName",taskName).setString("processDefinitionId",processDefinitionId).executeUpdate();
			if(assignments!=null){
				for(TaskAssignment assign:assignments){
					if(StringUtils.isNotEmpty(getFixedCompanyId())){
						assign.setCompanyId(getFixedCompanyId());
					}
					assign.setId(UUID.randomUUID().toString());
					assign.setTaskName(taskName);
					assign.setProcessDefinitionId(processDefinitionId);
					session.save(assign);
				}
				if(reminder!=null){
					reminder.setCategory(ReminderCategory.TaskCreateReminder);
					saveTaskReminder(reminder, session, taskName,processDefinitionId);
				}
				if(overdueReminder!=null){
					overdueReminder.setCategory(ReminderCategory.TaskOverdueReminder);
					saveTaskReminder(overdueReminder, session, taskName,processDefinitionId);
				}
			}
			if(toolbarConfig!=null){
				toolbarConfig.setTaskName(taskName);
				toolbarConfig.setProcessDefinitionId(processDefinitionId);
				this.saveToolbarConfig(toolbarConfig,session);
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	private void saveSecureComponentIds(Collection<Map<String,Object>> components,String url,String processDefinitionId,String taskName,Session session){
		String hql="delete "+ComponentControl.class.getName()+" where taskName=:taskName and processDefinitionId=:processDefinitionId";
		session.createQuery(hql).setString("taskName", taskName).setString("processDefinitionId", processDefinitionId).executeUpdate();
		for(Map<String,Object> map:components){
			String id=(String)map.get("id");
			String authorityType=(String)map.get("authorityType");
			AuthorityType type=AuthorityType.valueOf(authorityType);
			ComponentControl cc=new ComponentControl();
			cc.setId(UUID.randomUUID().toString());
			cc.setComponentId(id);
			cc.setTaskName(taskName);
			cc.setUrl(url);
			cc.setProcessDefinitionId(processDefinitionId);
			if(type.equals(AuthorityType.read)){
				cc.setControlType(ControlType.ignore);
			}else{
				cc.setControlType(ControlType.readOnly);
			}
			session.save(cc);
		}
	}

	private void saveTaskReminder(TaskReminder reminder, Session session,
			String taskName, String processDefinitionId) {
		String hql;
		String taskReminderId=reminder.getId();
		if(StringUtils.isEmpty(reminder.getId())){
			taskReminderId=UUID.randomUUID().toString();
			reminder.setId(taskReminderId);
			reminder.setTaskName(taskName);
			reminder.setProcessDefinitionId(processDefinitionId);
			String companyId=Configure.getString("bdf2.jbpm4.fixedCompanyId");
			if(StringUtils.isEmpty(companyId)){
				companyId=this.getFixedCompanyId();
			}
			reminder.setCompanyId(companyId);
			session.save(reminder);
		}else{
			session.update(reminder);
		}
		Collection<JobCalendar> calendars=reminder.getCalendars();
		if(calendars!=null){
			hql="delete "+ReminderCalendar.class.getName()+" where taskReminderId=:taskReminderId";
			session.createQuery(hql).setString("taskReminderId",taskReminderId).executeUpdate();
			for(JobCalendar calendar:calendars){
				ReminderCalendar rc=new ReminderCalendar();
				rc.setId(UUID.randomUUID().toString());
				rc.setJobCalendarId(calendar.getId());
				rc.setTaskReminderId(taskReminderId);
				session.save(rc);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public TaskReminder loadTaskReminder(String taskName,String processDefinitionId){
		Session session=this.getSessionFactory().openSession();
		try{
			String hql="from "+TaskReminder.class.getName()+" where taskName=:taskName and processDefinitionId=:processDefinitionId and category=:category";
			Query query=null;
			if(StringUtils.isNotEmpty(getFixedCompanyId())){
				hql+=" and companyId=:companyId";
				query=session.createQuery(hql);
				query.setString("companyId",getFixedCompanyId());
			}else{
				query=session.createQuery(hql);				
			}
			List<TaskReminder> result=query.setString("taskName", taskName).setString("processDefinitionId", processDefinitionId).setParameter("category", ReminderCategory.TaskCreateReminder).list();
			for(TaskReminder ta:result){
				if(StringUtils.isNotEmpty(ta.getMessageTemplateId())){
					MessageTemplate msg=(MessageTemplate)session.get(MessageTemplate.class,ta.getMessageTemplateId());
					ta.setMessageTemplateName(msg.getName());					
				}
			}
			if(result.size()>0){
				return result.get(0);
			}else{
				TaskReminder reminder=new TaskReminder();
				reminder.setReminderType(ReminderType.none);
				return reminder;							
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	@SuppressWarnings("unchecked")
	@DataProvider
	public TaskReminder loadTaskOverdueReminder(String taskName,String processDefinitionId){
		Session session=this.getSessionFactory().openSession();
		try{
			String hql="from "+TaskReminder.class.getName()+" where taskName=:taskName and processDefinitionId=:processDefinitionId and category=:category";
			Query query=null;
			if(StringUtils.isNotEmpty(getFixedCompanyId())){
				hql+=" and companyId=:companyId";
				query=session.createQuery(hql);
				query.setString("companyId",getFixedCompanyId());
			}else{
				query=session.createQuery(hql);				
			}
			List<TaskReminder> result=query.setString("taskName", taskName).setString("processDefinitionId", processDefinitionId).setParameter("category", ReminderCategory.TaskOverdueReminder).list();
			for(TaskReminder ta:result){
				if(StringUtils.isNotEmpty(ta.getMessageTemplateId())){
					MessageTemplate msg=(MessageTemplate)session.get(MessageTemplate.class,ta.getMessageTemplateId());
					ta.setMessageTemplateName(msg.getName());					
				}
			}
			if(result.size()>0){
				return result.get(0);
			}else{
				TaskReminder reminder=new TaskReminder();
				reminder.setReminderType(ReminderType.none);
				reminder.setOverdueMethod(OverdueMethod.SendMessage);
				return reminder;							
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@DataProvider
	public void loadMessageTemplates(Page<MessageTemplate> page,Criteria criteria){
		messageTemplateService.loadMessageTemplates(page, criteria,IJbpm4MessageVariableRegister.TYPE);
	}
	
	public void initProcessImage(HtmlContainer htmlContainer) throws Exception {
		String processDefinitionId = DoradoContext.getCurrent().getRequest()
				.getParameter("processDefinitionId");
		HttpServletRequest request=DoradoContext.getCurrent().getRequest();
		String contextPath=request.getContextPath();
		String imageURL =contextPath.endsWith("/")?"":contextPath+"/"+GenerateProcessImage.URL + "?processDefinitionId=" + processDefinitionId;
		List<Map<String, Object>> labelIconList = new ArrayList<Map<String, Object>>();
		ProcessDefinitionImpl pd = (ProcessDefinitionImpl) bpmService.findProcessDefinitionById(processDefinitionId);
		if(pd==null){
			throw new IllegalArgumentException("Process definition ["+processDefinitionId+"] was not found!");
		}
		List<? extends Activity> activityList = pd.getActivities();
		int relativeTop = -5;
		for (Activity activity : activityList) {
			ActivityImpl ac = (ActivityImpl) activity;
			boolean configAble=false;
			boolean isTask=false;
			// 检查节点类型
			if (ac.getType().equals("task")) {
				isTask=true;
				TaskDefinitionImpl taskDefinition=((TaskActivity)ac.getActivityBehaviour()).getTaskDefinition();
				if (taskDefinition.getAssignmentHandlerReference() != null) {
					ObjectDescriptor od=(ObjectDescriptor)taskDefinition.getAssignmentHandlerReference().getDescriptor();
					String assignValue = od.getClassName();
					if(StringUtils.isNotEmpty(assignValue) && assignValue.equals(GenericTaskAssignmentHandler.class.getName())){
						configAble=true;
					}else{
						configAble=false;						
					}
				}
			}
			if (isTask) {
				ActivityCoordinates activityAC = bpmService.getRepositoryService().getActivityCoordinates(processDefinitionId, ac.getName());
				int width = activityAC.getWidth();
				int height = activityAC.getHeight();
				int left = activityAC.getX();
				int top = activityAC.getY();
				String functionScript = buildFunctionScript(processDefinitionId, ac.getName(),configAble);
				relativeTop = this.addConfigureLabel(labelIconList, "配置", left, top, width, height,functionScript, relativeTop);
			}
		}
		String buffer = "<div id=\"_configProcessNodeDiv\" style=\"z-index:300;position:relative;background:url("+ imageURL + ") no-repeat\">";
		buffer += buildDivStyle(labelIconList);
		buffer += "</div>";
		htmlContainer.setContent(buffer);
		String script="$(\"<img/>\").attr(\"src\",\""+imageURL+"\").load(function(){" +
				"$(\"#_configProcessNodeDiv\").css({width:(this.width+10)+\"px\",height:(this.height+10)+\"px\"});" +
				"});";
		ClientEvent event=new DefaultClientEvent(script);
		htmlContainer.addClientEventListener("onReady", event);
	}

	private String buildFunctionScript(String processDefinitionId, String activityName,boolean configAble) {
		return "configTask('" + processDefinitionId + "','" + activityName + "',"+configAble+")";
	}

	private int addConfigureLabel(List<Map<String, Object>> labelIconList, String text,
			int left, int top, int width, int height, String functionScript, int globalTop) {
		Map<String, Object> style = new HashMap<String, Object>();
		style.put("z-index", "115");
		style.put("position", "relative");
		style.put("left", left + "px");
		top = top - globalTop-8;
		globalTop += height;
		style.put("top", top + "px");
		style.put("width", width + "px");
		style.put("height", height + "px");
		style.put("cursor", "pointer");
		style.put("color", "blue");
		style.put("text-align", "center");
		style.put("onclick", functionScript);
		style.put("content", "<a>" + text + "</a>");
		labelIconList.add(style);
		return globalTop;
	}

	private String buildDivStyle(List<Map<String, Object>> labelIconList) {
		StringBuilder div = new StringBuilder(2000);
		for (Map<String, Object> style : labelIconList) {
			div.append("<div style=\"");
			String contentValue = (String) style.get("content");
			style.remove("content");

			for (Entry<String, Object> entry : style.entrySet()) {
				div.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
			}
			div.append("\"");
			if (style.get("onclick") != null) {
				div.append(" onclick=\"").append(style.get("onclick")).append("\"");
			}
			div.append(">").append(contentValue).append("</div>");
		}
		return div.toString();
	}

	public void afterPropertiesSet() throws Exception {
		builders=this.getApplicationContext().getBeansOfType(IControlBuilder.class).values();
		senders=this.getApplicationContext().getBeansOfType(IMessageSender.class).values();
		processors=this.getApplicationContext().getBeansOfType(ITaskOverdueProcessor.class).keySet();
		Collection<IToolbarContentProvider> providers=this.getApplicationContext().getBeansOfType(IToolbarContentProvider.class).values();
		toolbarContentProviders=new ArrayList<Map<String,Object>>();
		for(IToolbarContentProvider p:providers){
			if(!p.isDisabled()){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("key", p.key());
				map.put("desc", p.desc());
				toolbarContentProviders.add(map);
			}
		}
	}
}
