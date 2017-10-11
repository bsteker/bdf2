package com.bstek.bdf2.job.service;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;

import com.bstek.bdf2.job.model.JobCalendar;
import com.bstek.bdf2.job.model.JobDefinition;

/**
 * @author Jacky.gao
 * @since 2013-3-9
 */
public interface IJobService {
	public static final String BEAN_ID="bdf2.job.jobService";
	void addJobToScheduler(JobDefinition job) throws SchedulerException;
	void addJobToScheduler(JobDefinition job,JobDetailImpl jobDetail) throws SchedulerException;
	void removeJobFromScheduler(String jobDefId,String companyId) throws SchedulerException;
	JobDetail getJobFromScheduler(String jobDefId,String companyId) throws SchedulerException;
	void resumeJob(String jobDefId,String companyId) throws SchedulerException;
	void pauseJob(String jobDefId,String companyId) throws SchedulerException;
	void updateTrigger(String jobDefId,String companyId,String cronExpression) throws SchedulerException;
	void updateCalendar(String jobDefId,String companyId,List<JobCalendar> calendars) throws SchedulerException;
}
