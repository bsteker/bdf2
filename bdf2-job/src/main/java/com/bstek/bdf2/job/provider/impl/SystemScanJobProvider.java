package com.bstek.bdf2.job.provider.impl;

import java.text.ParseException;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.bstek.bdf2.job.JobHibernateDao;
import com.bstek.bdf2.job.executor.ScanJobExecutor;
import com.bstek.bdf2.job.executor.ScanJobExecutorDetail;
import com.bstek.bdf2.job.provider.ISystemJobProvider;

/**
 * @author Jacky.gao
 * @since 2013-3-29
 */
public class SystemScanJobProvider extends JobHibernateDao implements ISystemJobProvider {
	private String scanJobCronExpression;
	public JobDetail getJobDetail() {
		ScanJobExecutorDetail jobDetail=(ScanJobExecutorDetail)this.getApplicationContext().getBean(ScanJobExecutorDetail.BEAN_ID);
		jobDetail.setSessionFactory(this.getSessionFactory());
		jobDetail.setKey(new JobKey(ScanJobExecutor.JOB_ID,GROUP_ID));
		jobDetail.setJobClass(ScanJobExecutor.class);
		return jobDetail;
	}
	public Trigger getTrigger() {
		CronTriggerImpl trigger=new CronTriggerImpl();
		trigger.setName("trigger"+ScanJobExecutor.JOB_ID);
		try {
			trigger.setCronExpression(scanJobCronExpression);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return trigger;
	}
	public String getScanJobCronExpression() {
		return scanJobCronExpression;
	}
	public void setScanJobCronExpression(String scanJobCronExpression) {
		this.scanJobCronExpression = scanJobCronExpression;
	}
}
