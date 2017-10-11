package com.bstek.bdf2.jbpm4.job.scan;

import org.hibernate.SessionFactory;
import org.quartz.impl.JobDetailImpl;

import com.bstek.bdf2.job.service.IJobService;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public class ScanTaskReminderJobDetail extends JobDetailImpl {
	private static final long serialVersionUID = 4490026952521823606L;
	private SessionFactory sessionFactory;
	private IJobService jobService;
	public ScanTaskReminderJobDetail(SessionFactory sessionFactory,
			IJobService jobService) {
		super();
		this.sessionFactory = sessionFactory;
		this.jobService = jobService;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public IJobService getJobService() {
		return jobService;
	}
	public void setJobService(IJobService jobService) {
		this.jobService = jobService;
	}
	
}
