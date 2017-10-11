package com.bstek.bdf2.job.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.calendar.HolidayCalendar;
import org.quartz.impl.calendar.MonthlyCalendar;
import org.quartz.impl.calendar.WeeklyCalendar;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.job.executor.SpringBeanJobExecutor;
import com.bstek.bdf2.job.executor.SpringBeanJobExecutorDetail;
import com.bstek.bdf2.job.model.CalendarType;
import com.bstek.bdf2.job.model.JobCalendar;
import com.bstek.bdf2.job.model.JobCalendarDate;
import com.bstek.bdf2.job.model.JobDefinition;
import com.bstek.bdf2.job.model.JobParameter;
import com.bstek.bdf2.job.service.IJobDefinitionService;
import com.bstek.bdf2.job.service.IJobService;
import com.bstek.bdf2.job.service.ISchedulerService;

/**
 * @author Jacky.gao
 * @since 2013-3-8
 */
public class JobServiceImpl implements IJobService,ApplicationContextAware{
	public static final String JOB_PREFIX="job";
	public static final String TRIGGER_PREFIX="trigger";
	public static final String CALENDAR_PREFIX="calendar";
	private ISchedulerService schedulerService;
	private ApplicationContext applicationContext;
	public void addJobToScheduler(JobDefinition jobDef) throws SchedulerException {
		addJobToScheduler(jobDef,null);
	}
	public void addJobToScheduler(JobDefinition jobDef,JobDetailImpl jobDetail) throws SchedulerException {
		String jobDefId=jobDef.getId();
		Scheduler scheduler=schedulerService.retrieveScheduler();
		if(jobDetail==null){
			jobDetail=new SpringBeanJobExecutorDetail(applicationContext,jobDef.getBeanId());	
		}
		if (jobDetail.getJobClass() == null) {
			jobDetail.setJobClass(SpringBeanJobExecutor.class);	
        }
		jobDetail.setKey(new JobKey(JOB_PREFIX+jobDefId,jobDef.getCompanyId()));
		jobDetail.setDescription(jobDef.getName()+","+jobDef.getDesc());
		JobDataMap jobDataMap=jobDetail.getJobDataMap();
		if(jobDataMap==null){
			jobDataMap=new JobDataMap();
		}
		jobDataMap.put(IJobDefinitionService.JOB_DEFINITION_ID, jobDefId);
		for(JobParameter p:jobDef.getParameters()){
			jobDataMap.put(p.getName(), p.getValue());
		}
		jobDetail.setJobDataMap(jobDataMap);
		CronTriggerImpl trigger=new CronTriggerImpl();
		try {
			trigger.setCronExpression(jobDef.getCronExpression());
		} catch (ParseException e1) {
			e1.printStackTrace();
			throw new SchedulerException(e1);
		}
		trigger.setName(TRIGGER_PREFIX+jobDefId);
		trigger.setGroup(jobDef.getCompanyId());
		try {
			trigger.setCronExpression(jobDef.getCronExpression());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SchedulerException(e);
		}
		JobKey jobKey=new JobKey(JOB_PREFIX+jobDefId,jobDef.getCompanyId());
		TriggerKey triggerKey=new TriggerKey(TRIGGER_PREFIX+jobDefId,jobDef.getCompanyId());
		if(scheduler.checkExists(jobKey)){
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			scheduler.deleteJob(jobKey);
		}
		List<JobCalendar> calendars=jobDef.getCalendars();
		if(calendars==null)return;
		Calendar calendar=buildCalendar(calendars);
		String calendarName=CALENDAR_PREFIX+jobDefId;
		if(calendar!=null){
			trigger.setCalendarName(calendarName);
			scheduler.addCalendar(calendarName, calendar, true, true);			
		}
		scheduler.scheduleJob(jobDetail, trigger);
	}
	
	private Calendar buildCalendar(List<JobCalendar> calendars){
		MultipleCalendar mulCalendar=null;
		for(JobCalendar calendarDef:calendars){
			if(calendarDef.getType().equals(CalendarType.holiday)){
				HolidayCalendar calendar=new HolidayCalendar();
				List<JobCalendarDate> dates=calendarDef.getCalendarDates();
				if(dates!=null){
					for(JobCalendarDate d:dates){
						calendar.addExcludedDate(d.getCalendarDate());
					}
				}
				if(mulCalendar==null){
					mulCalendar=new MultipleCalendar();
				}
				mulCalendar.addCalendar(calendar);
			}
			if(calendarDef.getType().equals(CalendarType.annual)){
				AnnualCalendar calendar=new AnnualCalendar();
				List<JobCalendarDate> dates=calendarDef.getCalendarDates();
				if(dates!=null){
					ArrayList<java.util.Calendar> excludedDates=new ArrayList<java.util.Calendar>();
					for(JobCalendarDate d:dates){
						java.util.Calendar c=new GregorianCalendar();
						c.add(java.util.Calendar.MONTH,d.getMonthOfYear());
						c.add(java.util.Calendar.DAY_OF_MONTH,d.getDayOfMonth());
						excludedDates.add(c);
					}
					calendar.setDaysExcluded(excludedDates);
				}
				if(mulCalendar!=null){
					mulCalendar=new MultipleCalendar();
				}
				mulCalendar.addCalendar(calendar);
			}
			if(calendarDef.getType().equals(CalendarType.monthly)){
				MonthlyCalendar calendar=new MonthlyCalendar();
				List<JobCalendarDate> dates=calendarDef.getCalendarDates();
				if(dates!=null){
					for(JobCalendarDate d:dates){
						calendar.setDayExcluded(d.getDayOfMonth(),true);
					}
				}
				if(mulCalendar!=null){
					mulCalendar=new MultipleCalendar();
				}
				mulCalendar.addCalendar(calendar);
			}
			if(calendarDef.getType().equals(CalendarType.weekly)){
				WeeklyCalendar calendar=new WeeklyCalendar();
				List<JobCalendarDate> dates=calendarDef.getCalendarDates();
				if(dates!=null){
					for(JobCalendarDate d:dates){
						calendar.setDayExcluded(d.getDayOfWeek(),true);
					}
				}
				if(mulCalendar!=null){
					mulCalendar=new MultipleCalendar();
				}
				mulCalendar.addCalendar(calendar);
			}
			
		}
		return mulCalendar;			
	}

	public void removeJobFromScheduler(String jobDefId,String companyId) throws SchedulerException {
		Scheduler scheduler=schedulerService.retrieveScheduler();
		JobKey jobKey=new JobKey(JOB_PREFIX+jobDefId,companyId);
		TriggerKey triggerKey=new TriggerKey(TRIGGER_PREFIX+jobDefId,companyId);
		if(scheduler.checkExists(jobKey)){
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			scheduler.deleteJob(jobKey);
		}
	}
	public void resumeJob(String jobDefId,String companyId) throws SchedulerException {
		Scheduler scheduler=schedulerService.retrieveScheduler();
		scheduler.resumeJob(new JobKey(JOB_PREFIX+jobDefId,companyId));
	}
	public void pauseJob(String jobDefId,String companyId) throws SchedulerException {
		Scheduler scheduler=schedulerService.retrieveScheduler();
		scheduler.pauseJob(new JobKey(JOB_PREFIX+jobDefId,companyId));
	}
	public JobDetail getJobFromScheduler(String jobDefId,String companyId) throws SchedulerException {
		Scheduler scheduler=schedulerService.retrieveScheduler();
		return scheduler.getJobDetail(new JobKey(JOB_PREFIX+jobDefId,companyId));
	}
	
	public void updateTrigger(String jobDefId,String companyId,String cronExpression) throws SchedulerException {
		Scheduler scheduler=schedulerService.retrieveScheduler();
		CronTriggerImpl trigger=(CronTriggerImpl)scheduler.getTrigger(new TriggerKey(TRIGGER_PREFIX+jobDefId,companyId));
		try {
			trigger.setCronExpression(cronExpression);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SchedulerException(e);
		}
		scheduler.scheduleJob(trigger);
	}
	
	public void updateCalendar(String jobDefId,String companyId,List<JobCalendar> calendars) throws SchedulerException {
		Calendar calendar=this.buildCalendar(calendars);
		if(calendar==null)return;
		Scheduler scheduler=schedulerService.retrieveScheduler();
		scheduler.addCalendar(CALENDAR_PREFIX+jobDefId, calendar, true, true);
		
	}
	public void setSchedulerService(ISchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

}
