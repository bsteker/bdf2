package com.bstek.bdf2.job.listener;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.hibernate.SessionFactory;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public abstract class JobExecutionListener implements JobListener {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	protected String getExceptionStackMessage(JobExecutionException jobException) {
		if (jobException != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			jobException.printStackTrace(pw);
			return sw.toString();
		} else {
			return null;
		}
	}


}
