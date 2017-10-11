package com.bstek.bdf2.job.executor;

import org.hibernate.SessionFactory;
import org.quartz.impl.JobDetailImpl;

import com.bstek.bdf2.job.service.IJobDefinitionService;
import com.bstek.bdf2.job.service.IJobService;

/**
 * @author Jacky.gao
 * @since 2013-3-12
 */
public class ScanJobExecutorDetail extends JobDetailImpl {
	public static final String BEAN_ID="bdf2.job.scanJobExecutorDetail";
	private static final long serialVersionUID = 5235476854106861377L;
	private IJobDefinitionService jobDefinitionService;
	private SessionFactory sessionFactory;
	private IJobService jobService;
	
    public ScanJobExecutorDetail() {
        //System.out.println(BEAN_ID);
    }
	public IJobDefinitionService getJobDefinitionService() {
		return jobDefinitionService;
	}
	public void setJobDefinitionService(IJobDefinitionService jobDefinitionService) {
		this.jobDefinitionService = jobDefinitionService;
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
