package com.bstek.bdf2.job.executor;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Jacky.gao
 * @since 2013-3-8
 */
public class SpringBeanJobExecutor implements Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SpringBeanJobExecutorDetail jobDetail=(SpringBeanJobExecutorDetail)context.getJobDetail();
		Job job=(Job)jobDetail.getApplicationContext().getBean(jobDetail.getTargetJobBeanId());
		job.execute(context);
	}
}
