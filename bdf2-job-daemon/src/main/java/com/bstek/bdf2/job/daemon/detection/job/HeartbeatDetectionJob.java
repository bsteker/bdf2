package com.bstek.bdf2.job.daemon.detection.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bstek.bdf2.job.daemon.model.Heartbeat;
import com.bstek.bdf2.job.service.ISchedulerService;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
public class HeartbeatDetectionJob implements Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		DetectionJobDetail jobDetail=(DetectionJobDetail)context.getJobDetail();
		Session session=jobDetail.getSessionFactory().openSession();
		try {
			String currentInstanceName=jobDetail.getCurrentInstanceName();
			Operation operation=detection(session,jobDetail.getJobInstanceNames(),currentInstanceName);
			if(operation.equals(Operation.reset)){
				ISchedulerService service=jobDetail.getSchedulerService();
				System.out.println("Current instance scheduler starting...");
				service.resetScheduer();
				System.out.println("Start successful...");				
			}
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}finally{
			session.flush();
			session.close();
		}
	}
	
	/**
	 * 当实例列表中只有一个，且是当前实例时就重启
	 * @param session Hibernate Session对象
	 * @param instanceNames 排队等待的实例名列表，如InsA,InsB,InsC,InsD
	 * @param currentInstanceName 当前服务器实例名
	 */
	@SuppressWarnings("unchecked")
	private Operation detection(Session session,String[] clusterJobInstanceNames,String currentInstanceName) {
		Query query=session.createQuery("from "+Heartbeat.class.getName()+" b where b.applicationName=:applicationName order by b.date desc");
		List<Heartbeat> heartbeats=query.setString("applicationName",Configure.getString(ISchedulerService.JOB_APPLICATION_NAME)).setMaxResults(1).list();
		int currentPos=this.getPosition(clusterJobInstanceNames, currentInstanceName)+1;
		if(heartbeats.size()>0){
			Date now=new Date();
			Heartbeat heartbeat=heartbeats.get(0);
			Date beatDate=heartbeat.getDate();
			Calendar beatCalendar=Calendar.getInstance();
			beatCalendar.setTime(beatDate);
			String beatInstanceName=heartbeat.getInstanceName();
			int secondUnit=40;
			int beatPos=this.getPosition(clusterJobInstanceNames, beatInstanceName)+1;
			if(!currentInstanceName.equals(beatInstanceName)){
				int currentSecond=currentPos*secondUnit;
				if(currentPos>beatPos){
					beatCalendar.add(Calendar.SECOND,currentSecond);
				}else if(currentPos<beatPos){
					currentSecond=(currentPos+(clusterJobInstanceNames.length-beatPos))*secondUnit;
					beatCalendar.add(Calendar.SECOND,currentSecond);
				}
			}else{
				beatCalendar.add(Calendar.SECOND,secondUnit*clusterJobInstanceNames.length);				
			}
			if(now.compareTo(beatCalendar.getTime())>0){
				//当前时间大于心跳时间+currentSecond,说明当前运行JOB的实例挂了
				return Operation.reset;
			}
		}else{
			if(currentPos==1)return Operation.reset;
		}
		return Operation.donothing;
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
	enum Operation{
		reset,donothing
	}
}
