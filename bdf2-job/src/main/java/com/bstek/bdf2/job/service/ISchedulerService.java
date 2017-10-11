package com.bstek.bdf2.job.service;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * @author Jacky.gao
 * @since 2013-3-8
 */
public interface ISchedulerService {
	public static final String BEAN_ID="bdf2.job.schedulerService";
	public static final String JOB_INSTANCE_NAME="jobInstanceName";
	public static final String JOB_APPLICATION_NAME="bdf2.jobApplicationName";
	public static final String DEFAULT_SCHEDULER_GROUP="default_scheduler_group_";
	/**
	 * 获取当前创建的Scheduler，如果没有就创建一个新的
	 * @return Scheduler
	 * @throws SchedulerException
	 */
	Scheduler retrieveScheduler() throws SchedulerException;
	boolean isRunJobInCurrentInstance();
	String getJobInstanceName();
	String getJobApplicationName();
	void resetScheduer() throws Exception;
	/**
	 * 获取当前创建的Scheduler，如果不存在就返回null
	 * @return Scheduler
	 * @throws SchedulerException
	 */
	Scheduler getCurrentScheduler() throws SchedulerException;
}
