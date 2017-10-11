package com.bstek.bdf2.jbpm4.job.scan;

import java.text.ParseException;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.job.provider.ISystemJobProvider;
import com.bstek.bdf2.job.service.IJobService;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public class ScanTaskReminderJobProvider extends Jbpm4HibernateDao implements ISystemJobProvider {
	private IJobService jobService;
	private String scanTaskReminderJobCronExpression;
	public JobDetail getJobDetail() {
		ScanTaskReminderJobDetail jobDetail=new ScanTaskReminderJobDetail(this.getSessionFactory(),jobService);
		jobDetail.setKey(new JobKey(ScanTaskReminderJob.JOB_ID,GROUP_ID));
		jobDetail.setJobClass(ScanTaskReminderJob.class);
		return jobDetail;
	}
	public Trigger getTrigger() {
		CronTriggerImpl trigger=new CronTriggerImpl();
		trigger.setName("trigger"+ScanTaskReminderJob.JOB_ID);
		try {
			trigger.setCronExpression(scanTaskReminderJobCronExpression);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return trigger;
	}
	public IJobService getJobService() {
		return jobService;
	}
	public void setJobService(IJobService jobService) {
		this.jobService = jobService;
	}
	public String getScanTaskReminderJobCronExpression() {
		return scanTaskReminderJobCronExpression;
	}
	public void setScanTaskReminderJobCronExpression(
			String scanTaskReminderJobCronExpression) {
		this.scanTaskReminderJobCronExpression = scanTaskReminderJobCronExpression;
	}
}
