package com.bstek.bdf2.jbpm4.context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;
import org.jbpm.pvm.internal.task.TaskImpl;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.orm.IDao;
import com.bstek.bdf2.jbpm4.job.reminder.TaskReminderMessageSender;
import com.bstek.bdf2.jbpm4.model.AssignmentDef;
import com.bstek.bdf2.jbpm4.model.OverdueMethod;
import com.bstek.bdf2.jbpm4.model.PrincipalDef;
import com.bstek.bdf2.jbpm4.model.ReminderJob;
import com.bstek.bdf2.jbpm4.model.ReminderJobState;
import com.bstek.bdf2.jbpm4.model.ReminderType;
import com.bstek.bdf2.jbpm4.model.TaskAssignment;
import com.bstek.bdf2.jbpm4.model.TaskReminder;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.jbpm4.view.assignment.provider.IAssignmentProvider;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-3-25
 */
public class GenericTaskAssignmentHandler implements AssignmentHandler {
	private static final long serialVersionUID = 156631235057155136L;

	
	@SuppressWarnings("unchecked")
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		String hql="from "+TaskAssignment.class.getName()+" where processDefinitionId=:processDefinitionId and taskName=:taskName";
		IBpmService bpmService=ContextHolder.getBean(IBpmService.BEAN_ID);
		String processDefinitionId=execution.getProcessDefinitionId();
		String nodeName=execution.getActivity().getName();
		Session session=bpmService.getSessionFactory().openSession();
		try{
			List<TaskAssignment> list=session.createQuery(hql).setString("processDefinitionId", processDefinitionId).setString("taskName", nodeName).list();
			if(list.size()>0){
				List<String> principals=new ArrayList<String>();
				for(TaskAssignment assignment:list){
					String assignmentDefId=assignment.getAssignmentDefId();
					AssignmentDef def=(AssignmentDef)session.get(AssignmentDef.class, assignmentDefId);
					String type=def.getType();
					for(IAssignmentProvider provider:bpmService.getApplicationContext().getBeansOfType(IAssignmentProvider.class).values()){
						if(provider.getTypeId().equals(type)){
							Collection<PrincipalDef> p=provider.getTaskPrincipals(assignmentDefId, execution);
							if(p!=null && p.size()>0){
								for(PrincipalDef principalDef:p){
									String principal=principalDef.getId();
									if(!principals.contains(principal)){
										principals.add(principal);
									}
								}
							}
						}
					}
				}
				if(principals.size()>0){
					if(principals.size()==1){
						assignable.setAssignee(principals.get(0));
					}else{
						for(String id:principals){
							assignable.addCandidateUser(id);													
						}
					}
					createReminderJob(principals,session,(TaskImpl)assignable,execution.getProcessDefinitionId());
				}else{
					throw new RuntimeException("Task node ["+nodeName+"] in process "+processDefinitionId+" was not found any principal!");					
				}
			}else{
				throw new RuntimeException("Task node ["+nodeName+"] in process "+processDefinitionId+" was not found assignment info!");
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void createReminderJob(List<String> principals,Session session,TaskImpl task,String processDefinitionId){
		String hql="from "+TaskReminder.class.getName()+" where processDefinitionId=:processDefinitionId and taskName=:taskName";
		Query query=null;
		String fixedCompanyId=Configure.getString(IDao.FIXED_COMPANY_ID);
		if(StringUtils.isNotEmpty(fixedCompanyId)){
			hql+=" and companyId=:companyId";
			query=session.createQuery(hql);
			query.setString("companyId",fixedCompanyId);
		}else{
			query=session.createQuery(hql);			
		}
		query.setString("processDefinitionId",processDefinitionId).setString("taskName",task.getName());
		List<TaskReminder> reminders=query.list();
		for(TaskReminder reminder:reminders){
			createReminderJob(principals, session, task, reminder);			
		}
	}

	private void createReminderJob(List<String> principals,
			Session session, TaskImpl task, TaskReminder reminder) {
		if(reminder==null || reminder.getReminderType().equals(ReminderType.none)){
			return;
		}
		String cron = null;
		ReminderType type=reminder.getReminderType();
		if(reminder.getOverdueDays()==0){
			TaskReminderMessageSender sender=ContextHolder.getBean(TaskReminderMessageSender.BEAN_ID);
			for(String principal:principals){
				sender.sendReminderMessage(task.getId(),principal, reminder.getMessageTemplateId(), reminder.getMessageSenders().split(","));
			}
			cron = buildCronExpression(reminder, type,false);
		}
		if(reminder.getOverdueDays()>0){
			if(reminder.getOverdueMethod().equals(OverdueMethod.Custom)){
				cron = buildCronExpression(reminder, type,true);
			}else if(reminder.getOverdueMethod().equals(OverdueMethod.SendMessage)){
				cron = buildCronExpression(reminder, type,false);
			}
		}
		if(cron==null){
			return;
		}
		for(String principal:principals){
			ReminderJob job=new ReminderJob();
			job.setCompanyId(reminder.getCompanyId());
			job.setId(UUID.randomUUID().toString());
			job.setTaskId(task.getId());
			job.setExecutionId(task.getExecutionId());
			job.setTaskName(task.getName());
			job.setTaskReminderId(reminder.getId());
			job.setMessageSenders(reminder.getMessageSenders());
			job.setPrincipal(principal);
			job.setState(ReminderJobState.running);
			job.setMessageTemplateId(reminder.getMessageTemplateId());
			job.setOverdueDays(reminder.getOverdueDays());
			job.setCronExpression(cron);
			job.setCreateDate(new Date());
			job.setOverdueMethod(reminder.getOverdueMethod());
			job.setOverdueCustomBeanProcessor(reminder.getOverdueCustomBeanProcessor());
			session.save(job);			
		}
	}

	private String buildCronExpression(TaskReminder reminder, ReminderType type,boolean custom) {
		String cron=null;
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE,-1);
		int second=calendar.get(Calendar.SECOND);
		int minute=calendar.get(Calendar.MINUTE);
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		if(custom){
			cron="0 0/30 * * * ?";
		}else{
			if(type.equals(ReminderType.hourPeriodic)){
				cron=second+" "+minute+" 0/"+hour+" * * ?";
			}
			if(type.equals(ReminderType.dayPeriodic)){
				cron=second+" "+minute+" "+hour+" 1/"+reminder.getEveryDay()+" * ?";
			}			
		}
		return cron;
	}
}
