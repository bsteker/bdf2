package com.bstek.bdf2.job.executor;

import org.quartz.impl.JobDetailImpl;
import org.springframework.context.ApplicationContext;

public class SpringBeanJobExecutorDetail extends JobDetailImpl {
	private static final long serialVersionUID = 1760997423381930643L;
	private ApplicationContext applicationContext;
	private String targetJobBeanId;
	public SpringBeanJobExecutorDetail(ApplicationContext applicationContext,String targetJobBeanId){
		this.applicationContext=applicationContext;
		this.targetJobBeanId=targetJobBeanId;
	}
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	public String getTargetJobBeanId() {
		return targetJobBeanId;
	}
	public void setTargetJobBeanId(String targetJobBeanId) {
		this.targetJobBeanId = targetJobBeanId;
	}
}
