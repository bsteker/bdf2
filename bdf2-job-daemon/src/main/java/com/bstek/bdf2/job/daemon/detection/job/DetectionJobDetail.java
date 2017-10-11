package com.bstek.bdf2.job.daemon.detection.job;

import org.hibernate.SessionFactory;
import org.quartz.impl.JobDetailImpl;

import com.bstek.bdf2.job.service.ISchedulerService;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
public class DetectionJobDetail extends JobDetailImpl {
	private static final long serialVersionUID = 3409374678334137950L;
	private SessionFactory sessionFactory;
	private String currentInstanceName;
	private String[] jobInstanceNames;
	private ISchedulerService schedulerService;
	public DetectionJobDetail(SessionFactory sessionFactory,String currentInstanceName, String[] jobInstanceNames,ISchedulerService schedulerService) {
		this.sessionFactory = sessionFactory;
		this.currentInstanceName = currentInstanceName;
		this.jobInstanceNames = jobInstanceNames;
		this.schedulerService=schedulerService;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public String getCurrentInstanceName() {
		return currentInstanceName;
	}
	public String[] getJobInstanceNames() {
		return jobInstanceNames;
	}
	public ISchedulerService getSchedulerService() {
		return schedulerService;
	}
}
