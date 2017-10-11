package com.bstek.bdf2.job.daemon.heart.job;

import org.hibernate.SessionFactory;
import org.quartz.impl.JobDetailImpl;

/**
 * @author Jacky.gao
 * @since 2013-5-7
 */
public class HeartJobDetail extends JobDetailImpl {
	private static final long serialVersionUID = 1075359553053951760L;
	private SessionFactory sessionFactory;
	private String currentInstanceName;
	public HeartJobDetail(SessionFactory sessionFactory,
			String currentInstanceName) {
		this.sessionFactory = sessionFactory;
		this.currentInstanceName = currentInstanceName;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public String getCurrentInstanceName() {
		return currentInstanceName;
	}
	public void setCurrentInstanceName(String currentInstanceName) {
		this.currentInstanceName = currentInstanceName;
	}
	
}
