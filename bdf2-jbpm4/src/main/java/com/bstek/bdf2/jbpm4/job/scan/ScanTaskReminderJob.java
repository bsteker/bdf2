package com.bstek.bdf2.jbpm4.job.scan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;

import com.bstek.bdf2.core.orm.IDao;
import com.bstek.bdf2.jbpm4.job.reminder.TaskReminderJob;
import com.bstek.bdf2.jbpm4.model.OverdueMethod;
import com.bstek.bdf2.jbpm4.model.ReminderCalendar;
import com.bstek.bdf2.jbpm4.model.ReminderJob;
import com.bstek.bdf2.jbpm4.model.ReminderJobState;
import com.bstek.bdf2.job.model.JobCalendar;
import com.bstek.bdf2.job.model.JobDefinition;
import com.bstek.bdf2.job.model.JobParameter;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public class ScanTaskReminderJob implements Job {
	protected final Log logger = LogFactory.getLog(getClass());
	public static final String JOB_ID="background_scan_task_reminder_job";
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScanTaskReminderJobDetail jobDetail=(ScanTaskReminderJobDetail)context.getJobDetail();
		Session session=jobDetail.getSessionFactory().openSession();
		boolean rollback=false;
		Transaction trans=session.beginTransaction();
		trans.begin();
		try{
			Collection<ReminderJob> runningJobs=this.loadReminderJobs(session, ReminderJobState.running);
			logger.info("There are "+runningJobs.size()+" task reminder jobs to run");
			for(ReminderJob job:runningJobs){
				JobDefinition jobDef=buildJobDefinition(job,session);
				JobDetailImpl jd=new JobDetailImpl();
				jd.setJobClass(TaskReminderJob.class);
				if(job.getOverdueDays()>0){
					JobDataMap jobDataMap=new JobDataMap();
					jobDataMap.put("overdueDays",job.getOverdueDays());
					jobDataMap.put("createDate",job.getCreateDate());
					if(job.getOverdueMethod().equals(OverdueMethod.SendMessage)){
						jobDataMap.put("overdueMethodSendMessage",true);
					}else{
						jobDataMap.put("overdueMethodSendMessage",false);	
						jobDataMap.put("overdueCustomBeanProcessor",job.getOverdueCustomBeanProcessor());
						jobDataMap.put("reminderJobId",job.getId());
					}
					jd.setJobDataMap(jobDataMap);					
				}
				jobDetail.getJobService().addJobToScheduler(jobDef,jd);
				job.setState(ReminderJobState.run);
				session.save(job);
			}
			Collection<ReminderJob> deletedJobs=this.loadReminderJobs(session, ReminderJobState.deleted);
			logger.info("There are "+deletedJobs.size()+" task reminder jobs to remove");
			for(ReminderJob job:deletedJobs){
				jobDetail.getJobService().removeJobFromScheduler(job.getId(),"internal_system");
				session.delete(job);
			}
		}catch(Exception ex){
			trans.rollback();
			rollback=true;
			ex.printStackTrace();
		}finally{
			if(!rollback){
				trans.commit();			
			}
			session.flush();
			session.close();
		}
	}
	
	private JobDefinition buildJobDefinition(ReminderJob job,Session session){
		JobDefinition jobDef=new JobDefinition();
		jobDef.setId(job.getId());
		jobDef.setParameters(buildJobParameters(job));
		jobDef.setCronExpression(job.getCronExpression());
		jobDef.setCalendars(buildJobCalendars(job,session));
		jobDef.setCompanyId("internal_system");
		jobDef.setDesc("流程实例["+job.getExecutionId()+"]中["+job.getTaskName()+"]节点任务提醒");
		jobDef.setName("["+job.getTaskName()+"]节点任务提醒");
		return jobDef;
	}
	
	@SuppressWarnings("unchecked")
	private List<JobCalendar> buildJobCalendars(ReminderJob job,Session session){
		List<JobCalendar> result=new ArrayList<JobCalendar>();
		String hql="from "+ReminderCalendar.class.getName()+" where taskReminderId=:taskReminderId";
		List<ReminderCalendar> list=session.createQuery(hql).setString("taskReminderId",job.getTaskReminderId()).list();
		for(ReminderCalendar rc:list){
			result.add((JobCalendar)session.get(JobCalendar.class, rc.getJobCalendarId()));
		}
		return result;
	}
	
	private List<JobParameter> buildJobParameters(ReminderJob job){
		List<JobParameter> result=new ArrayList<JobParameter>();
		JobParameter p=new JobParameter();
		p.setName("taskId");
		p.setValue(job.getTaskId());
		result.add(p);
		p=new JobParameter();
		p.setName("messageTemplateId");
		p.setValue(job.getMessageTemplateId());
		result.add(p);
		p=new JobParameter();
		p.setName("principal");
		p.setValue(job.getPrincipal());
		result.add(p);
		p=new JobParameter();
		p.setName("messageSenders");
		p.setValue(job.getMessageSenders());
		result.add(p);
		return result;
	}
	@SuppressWarnings("unchecked")
	private Collection<ReminderJob> loadReminderJobs(Session session,ReminderJobState state){
		String hql="from "+ReminderJob.class.getName()+" where state=:state";
		String companyId=Configure.getString(IDao.FIXED_COMPANY_ID);
		Query query=null;
		if(StringUtils.isNotEmpty(companyId)){
			hql+=" and companyId=:companyId";
			query=session.createQuery(hql);
			query.setString("companyId",companyId);		
		}else{
			query=session.createQuery(hql);			
		}
		query.setParameter("state",state);
		return query.list();
	}
}
