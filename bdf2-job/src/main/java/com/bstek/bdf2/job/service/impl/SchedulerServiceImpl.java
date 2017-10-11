package com.bstek.bdf2.job.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.bstek.bdf2.job.JobHibernateDao;
import com.bstek.bdf2.job.listener.JobExecutionListener;
import com.bstek.bdf2.job.service.ISchedulerService;
import com.bstek.bdf2.job.service.Initializer;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-3-8
 */
public class SchedulerServiceImpl extends JobHibernateDao implements ISchedulerService,DisposableBean{
	private boolean runJobInCurrentInstance;
	private Scheduler scheduler;
	private int threadCount=10;
	private Resource configLocation;
	private String makeSchedulerThreadDaemon;
	public Scheduler retrieveScheduler() throws SchedulerException{
		if(scheduler==null){
			scheduler=newScheduler();
		}
		return scheduler;
	}
	public Scheduler getCurrentScheduler() throws SchedulerException{
		return scheduler;
	}
	
	protected Scheduler newScheduler()throws SchedulerException{
		StdSchedulerFactory factory=new StdSchedulerFactory();
		Properties mergedProps = new Properties();
		mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
		mergedProps.setProperty("org.quartz.scheduler.instanceName", "BDF2Scheduler");
		mergedProps.setProperty("org.quartz.scheduler.instanceId", "CoreBDF2Scheduler");
		mergedProps.setProperty("org.quartz.scheduler.makeSchedulerThreadDaemon", makeSchedulerThreadDaemon);
		mergedProps.setProperty("org.quartz.threadPool.threadCount", Integer.toString(threadCount));
		if (this.configLocation != null) {
			try {
				PropertiesLoaderUtils.fillProperties(mergedProps, this.configLocation);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		factory.initialize(mergedProps);
		Scheduler scheduler=factory.getScheduler();
		Collection<JobExecutionListener> jobListeners = this.getApplicationContext().getBeansOfType(JobExecutionListener.class).values();
		for(JobExecutionListener jobListener:jobListeners){
			jobListener.setSessionFactory(getSessionFactory());
			scheduler.getListenerManager().addJobListener(jobListener);
		}
		return scheduler;
	}
	
	public void destroy() throws SchedulerException {
		if(scheduler!=null && !scheduler.isShutdown()){
			System.out.println("Shutting down Quartz Scheduler");
			scheduler.shutdown(false);						
		}
	}
	
	public boolean isRunJobInCurrentInstance() {
		return runJobInCurrentInstance;
	}
	public void setRunJobInCurrentInstance(boolean runJobInCurrentInstance) {
		this.runJobInCurrentInstance = runJobInCurrentInstance;
	}
	
	public String getJobApplicationName(){
		return Configure.getString("bdf2.jobApplicationName");
	}
	
	public void resetScheduer() throws Exception {
		if(scheduler!=null && !scheduler.isShutdown()){
			scheduler.shutdown(false);
		}
		this.scheduler=this.newScheduler();
		Initializer initializer=(Initializer)this.getApplicationContext().getBean(Initializer.BEAN_ID);
		initializer.initRunJobsForStartup();
		initializer.initSystemJobs();
		if(this.scheduler.isInStandbyMode()){
			this.scheduler.start();
		}
	}
	
	public String getJobInstanceName(){
		String jobApplicationName=getJobApplicationName();
		if(StringUtils.isEmpty(jobApplicationName)){
			return System.getProperty(JOB_INSTANCE_NAME);			
		}else{
			return System.getProperty(jobApplicationName+"."+JOB_INSTANCE_NAME);						
		}
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}
	public String getMakeSchedulerThreadDaemon() {
		return makeSchedulerThreadDaemon;
	}
	public void setMakeSchedulerThreadDaemon(String makeSchedulerThreadDaemon) {
		this.makeSchedulerThreadDaemon = makeSchedulerThreadDaemon;
	}
}
