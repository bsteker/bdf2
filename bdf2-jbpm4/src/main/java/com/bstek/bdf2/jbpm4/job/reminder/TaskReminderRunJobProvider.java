package com.bstek.bdf2.jbpm4.job.reminder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.quartz.impl.JobDetailImpl;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.ReminderCalendar;
import com.bstek.bdf2.jbpm4.model.ReminderJob;
import com.bstek.bdf2.jbpm4.model.ReminderJobState;
import com.bstek.bdf2.job.model.JobCalendar;
import com.bstek.bdf2.job.model.JobDefinition;
import com.bstek.bdf2.job.model.JobParameter;
import com.bstek.bdf2.job.provider.IRunJobProvider;
import com.bstek.bdf2.job.provider.RunJob;
import com.bstek.dorado.core.Configure;

public class TaskReminderRunJobProvider extends Jbpm4HibernateDao implements IRunJobProvider {
	public Collection<RunJob> getRunJobs() {
		List<RunJob> result=new ArrayList<RunJob>();
		Session session=this.getSessionFactory().openSession();
		try{
			Collection<ReminderJob> runningJobs=this.loadReminderJobs(session);
			for(ReminderJob job:runningJobs){
				JobDetailImpl jobDetail=new JobDetailImpl();
				JobDefinition jobDef=buildJobDefinition(job,session);
				jobDetail.setJobClass(TaskReminderJob.class);
				RunJob runJob=new RunJob();
				runJob.setJobDefinition(jobDef);
				runJob.setJobDetail(jobDetail);
				result.add(runJob);
			}
		}finally{
			session.flush();
			session.close();
		}
		return result;
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
	private Collection<ReminderJob> loadReminderJobs(Session session){
		String hql="from "+ReminderJob.class.getName()+" where state=:state";
		String fixedCompanyId=Configure.getString("bdf2.jbpm4.fixedCompanyId");
		if(StringUtils.isEmpty(fixedCompanyId)){
			fixedCompanyId=this.getFixedCompanyId();
		}
		if(StringUtils.isNotEmpty(fixedCompanyId)){
			hql+=" and companyId=:companyId";
			return session.createQuery(hql).setParameter("state",ReminderJobState.run).setParameter("companyId",fixedCompanyId).list();
		}
		return session.createQuery(hql).setParameter("state",ReminderJobState.run).list();
	}
}
