package com.bstek.bdf2.job.daemon.heart.provider;

import java.text.ParseException;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.bstek.bdf2.job.JobHibernateDao;
import com.bstek.bdf2.job.daemon.heart.job.HeartJob;
import com.bstek.bdf2.job.daemon.heart.job.HeartJobDetail;
import com.bstek.bdf2.job.provider.ISystemJobProvider;
import com.bstek.bdf2.job.service.ISchedulerService;

/**
 * @author Jacky.gao
 * @since 2013-5-7
 */
public class HeartSystemJobProvider extends JobHibernateDao implements ISystemJobProvider {
	private ISchedulerService schedulerService;
	private String heartJobCronExpression="0/30 * * * * ?";
	public JobDetail getJobDetail() {
		HeartJobDetail jobDetail=new HeartJobDetail(this.getSessionFactory(),schedulerService.getJobInstanceName());
		jobDetail.setKey(new JobKey("HeartJob",GROUP_ID));
		jobDetail.setJobClass(HeartJob.class);
		return jobDetail;
	}

	public Trigger getTrigger() {
		CronTriggerImpl trigger=new CronTriggerImpl();
		trigger.setName("HeartJobTrigger");
		try {
			trigger.setCronExpression(heartJobCronExpression);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return trigger;
	}

	public ISchedulerService getSchedulerService() {
		return schedulerService;
	}

	public void setSchedulerService(ISchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
}
