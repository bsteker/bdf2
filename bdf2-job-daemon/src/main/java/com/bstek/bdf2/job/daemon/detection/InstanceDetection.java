package com.bstek.bdf2.job.daemon.detection;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.simpl.SimpleThreadPool;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.job.JobHibernateDao;
import com.bstek.bdf2.job.daemon.detection.job.DaemonDetectionJob;
import com.bstek.bdf2.job.daemon.detection.job.DetectionJobDetail;
import com.bstek.bdf2.job.daemon.detection.job.HeartbeatDetectionJob;
import com.bstek.bdf2.job.daemon.model.Heartbeat;
import com.bstek.bdf2.job.service.ISchedulerService;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
public class InstanceDetection extends JobHibernateDao implements InitializingBean{
	private String detectionMode;
	private static final String instanceNamesKey="bdf2.jobDaemon.instanceNames";
	private Scheduler scheduler;
	private ISchedulerService schedulerService;
	private Job detectionJob;
	private JobDetailImpl jobDetail;
	private CronTriggerImpl trigger;
	private String detectionCron="0/30 * * * * ?";
	private void initDetectionScheduler() throws Exception{
		StdSchedulerFactory factory=new StdSchedulerFactory();
		Properties mergedProps = new Properties();
		mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
		mergedProps.setProperty("org.quartz.scheduler.instanceName", "BDF2HeartbeatScheduler");
		mergedProps.setProperty("org.quartz.scheduler.instanceId", "HeartbeatDetectionScheduler");
		mergedProps.setProperty("org.quartz.threadPool.threadCount","1");
		factory.initialize(mergedProps);
		scheduler=factory.getScheduler();
	}
	private void startDaemonJob() throws Exception{
		System.out.println("Start daemon scheduler...");
		initJobDetail();
		iniTtrigger();
		if(this.isDaemon()){
			detectionJob=new DaemonDetectionJob();
		}else if(this.isHeartbeat()){
			detectionJob=new HeartbeatDetectionJob();
		}else{
			throw new RuntimeException("Not support the detectionMode property["+detectionMode+"],the detectionMode property can only set to daemon or heartbeat");
		}
		jobDetail.setJobClass(detectionJob.getClass());
		initDetectionScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
		System.out.println("Daemon scheduler started...");		
	}
	
	private void initJobDetail(){
		String currentInstanceName=schedulerService.getJobInstanceName();
		if(StringUtils.isEmpty(currentInstanceName) && isHeartbeat()){
			throw new RuntimeException("Use [bdf2-job-daemon] module you need define a system property["+schedulerService.getJobApplicationName()+"."+ISchedulerService.JOB_INSTANCE_NAME+"] ");
		}
		String clusterJobInstanceNames[]=getClusterJobInstanceNames();
		jobDetail=new DetectionJobDetail(this.getSessionFactory(),currentInstanceName,clusterJobInstanceNames,schedulerService);
		jobDetail.setKey(new JobKey("DaemonJobDetail"));
		jobDetail.setName("DaemonDetectionJobDetail");
	}
	
	private String[] getClusterJobInstanceNames(){
		String names[]=null;
		String instanceNames=Configure.getString(instanceNamesKey);
		if(StringUtils.isEmpty(instanceNames) && isHeartbeat()){
			throw new RuntimeException("Use [bdf2-job-daemon] module you need define a named ["+instanceNamesKey+"] property!");
		}
		if(isHeartbeat()){
			names=instanceNames.split(",");			
		}
		return names;
	}
	
	private void iniTtrigger(){
		trigger=new CronTriggerImpl();
		trigger.setName("HeartbeatTrigger");
		trigger.setKey(new TriggerKey("HeartbeatTrigger"));
		try {
			trigger.setCronExpression(detectionCron);
		} catch (ParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
	}
	
	public boolean isRunBusinessJob(){
		boolean runJobInCurrentInstance=false;
		String appName=schedulerService.getJobApplicationName();
		String jobApplicationNames=System.getProperty("jobApplicationNames");
		if(StringUtils.isNotEmpty(jobApplicationNames)){
			if(StringUtils.isNotEmpty(appName)){
				for(String name:jobApplicationNames.split(",")){
					if(name.equals(appName)){
						runJobInCurrentInstance=true;	
						break;
					}
				}						
			}
		}
		return runJobInCurrentInstance;
	}
	
	private void runBusinessJobInCurrentInstance() throws Exception{
		if(!isRunBusinessJob()){
			return;
		}
		Scheduler scheduler=schedulerService.getCurrentScheduler();
		if(scheduler!=null && scheduler.isStarted()){
			return;
		}
		String currentInstanceName=schedulerService.getJobInstanceName();
		String clusterJobInstanceNames[]=getClusterJobInstanceNames();
		Session session=this.getSessionFactory().openSession();
		boolean runScheduler=true;
		try{
			Query query=session.createQuery("from "+Heartbeat.class.getName()+" b where b.applicationName=:applicationName order by b.date desc");
			@SuppressWarnings("unchecked")
			List<Heartbeat> heartbeats=query.setString("applicationName",schedulerService.getJobApplicationName()).setMaxResults(1).list();
			int currentPos=this.getPosition(clusterJobInstanceNames, currentInstanceName)+1;
			int secondUnit=40;
			if(heartbeats.size()>0){
				Date now=new Date();
				Heartbeat heartbeat=heartbeats.get(0);
				Date beatDate=heartbeat.getDate();
				Calendar beatCalendar=Calendar.getInstance();
				beatCalendar.setTime(beatDate);
				String beatInstanceName=heartbeat.getInstanceName();
				if(!currentInstanceName.equals(beatInstanceName)){
					int currentSecond=currentPos*secondUnit;
					int beatPos=this.getPosition(clusterJobInstanceNames, beatInstanceName)+1;
					if(currentPos>beatPos){
						beatCalendar.add(Calendar.SECOND,currentSecond);
					}else if(currentPos<beatPos){
						currentSecond=(currentPos+(clusterJobInstanceNames.length-beatPos))*secondUnit;
						beatCalendar.add(Calendar.SECOND,currentSecond);
					}
				}else{
					beatCalendar.add(Calendar.SECOND,secondUnit*clusterJobInstanceNames.length);				
				}
				if(now.compareTo(beatCalendar.getTime())<0){
					//当前时间小于心跳时间+currentSecond,说明当前运行JOB的实例运行中
					runScheduler=false;
				}
			}else{
				if(currentPos>1){
					//如果当前实例不处于运动集群JOB的第一位，那么就不运行JOB
					runScheduler=false;					
				}
			}
		}finally{
			session.close();
		}
		if(runScheduler){
			schedulerService.resetScheduer();				
		}
	}
	
	private int getPosition(String[] instanceNames,String instanceName){
		int pos=0;
		for(int i=0;i<instanceNames.length;i++){
			String name=instanceNames[i];
			if(name.equals(instanceName)){
				pos=i;
			}
		}
		return pos;
	}
	
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isEmpty(schedulerService.getJobInstanceName())){
			throw new RuntimeException("You need config a named ["+ISchedulerService.JOB_INSTANCE_NAME+"] system property when use bdf2-job-daemon module!");
		}
		runBusinessJobInCurrentInstance();
		startDaemonJob();
	}
	private boolean isHeartbeat(){
		return detectionMode.equals("heartbeat");
	}
	private boolean isDaemon(){
		return detectionMode.equals("daemon");
	}
	public void setSchedulerService(ISchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	public void setDetectionMode(String detectionMode) {
		this.detectionMode = detectionMode;
	}
	public void setDetectionCron(String detectionCron) {
		this.detectionCron = detectionCron;
	}
	public Scheduler getScheduler() {
		return scheduler;
	}
}
