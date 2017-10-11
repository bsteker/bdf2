package com.bstek.bdf2.job.daemon.detection.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;

import com.bstek.bdf2.job.service.ISchedulerService;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
public class DaemonDetectionJob implements Job {
	private static final Log log=LogFactory.getLog(DaemonDetectionJob.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
		DetectionJobDetail jobDetail=(DetectionJobDetail)context.getJobDetail();
		try {
			ISchedulerService service=jobDetail.getSchedulerService();
			if(!service.isRunJobInCurrentInstance()){
				log.warn("Current instance not allowed run scheduler!");
				return;
			}
			Scheduler scheduler=service.getCurrentScheduler();
			if(scheduler==null || scheduler.isShutdown()){
				System.out.println("Current instance scheduler was shutdown,start reset...");
				service.resetScheduer();
				System.out.println("Reset successful...");
			}
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
