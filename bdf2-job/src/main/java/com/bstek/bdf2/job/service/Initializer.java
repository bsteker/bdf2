package com.bstek.bdf2.job.service;

import java.util.Collection;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.job.provider.IRunJobProvider;
import com.bstek.bdf2.job.provider.ISystemJobProvider;
import com.bstek.bdf2.job.provider.RunJob;

/**
 * @author Jacky.gao
 * @since 2013-3-30
 */
public class Initializer implements ApplicationContextAware,InitializingBean{
	public static final String BEAN_ID="bdf2.job.initializer";
	private ISchedulerService schedulerService;
	private IJobService jobService;
	private Collection<ISystemJobProvider> systemJobProviders;
	private Collection<IRunJobProvider> runJobProviders;
	public void initRunJobsForStartup() throws SchedulerException{
		for(IRunJobProvider p:runJobProviders){
			for(RunJob runJob:p.getRunJobs()){
				if(runJob.getJobDefinition()!=null){
					if(runJob.getJobDetail()!=null){
						jobService.addJobToScheduler(runJob.getJobDefinition(),runJob.getJobDetail());															
					}else{
						jobService.addJobToScheduler(runJob.getJobDefinition());																					
					}
				}
			}
		}
	}
	public void initSystemJobs() throws Exception{
		Scheduler scheduler=schedulerService.retrieveScheduler();
		for(ISystemJobProvider provider:systemJobProviders){
			scheduler.scheduleJob(provider.getJobDetail(), provider.getTrigger());			
		}			
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		systemJobProviders=applicationContext.getBeansOfType(ISystemJobProvider.class).values();
		runJobProviders=applicationContext.getBeansOfType(IRunJobProvider.class).values();
	}
	
	public void startJobInstance() throws Exception{
		if(schedulerService.isRunJobInCurrentInstance()){
			initRunJobsForStartup();
			initSystemJobs();
			Scheduler scheduler=schedulerService.retrieveScheduler();
			if(scheduler.isInStandbyMode()){
				System.out.println("Start scheduler ["+scheduler.getSchedulerName()+"]...");
				scheduler.start();
			}
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		if(schedulerService.isRunJobInCurrentInstance()){
			startJobInstance();
		}
	}
	public ISchedulerService getSchedulerService() {
		return schedulerService;
	}
	public void setSchedulerService(ISchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	public IJobService getJobService() {
		return jobService;
	}
	public void setJobService(IJobService jobService) {
		this.jobService = jobService;
	}
}
