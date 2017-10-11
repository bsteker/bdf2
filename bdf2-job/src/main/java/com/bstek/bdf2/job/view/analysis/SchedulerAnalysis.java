package com.bstek.bdf2.job.view.analysis;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.job.executor.ScanJobExecutorDetail;
import com.bstek.bdf2.job.executor.SpringBeanJobExecutorDetail;
import com.bstek.bdf2.job.service.ISchedulerService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;

/**
 * @author Jacky.gao
 * @since 2013-5-8
 */
@Component("bdf2.schedulerAnalysis")
public class SchedulerAnalysis {
	@Autowired
	@Qualifier(ISchedulerService.BEAN_ID)
	private ISchedulerService schedulerService;
	@DataProvider
	public SchedulerInfo loadScheduerInfo() throws Exception{
		SchedulerInfo info=new SchedulerInfo();
		info.setRunJob(schedulerService.isRunJobInCurrentInstance());
		Scheduler scheduler=schedulerService.getCurrentScheduler();
		if(scheduler!=null){
			if(scheduler.isStarted()){
				info.setState(SchedulerState.started);
			}else if(scheduler.isShutdown()){
				info.setState(SchedulerState.shutdown);
			}else if(scheduler.isInStandbyMode()){
				info.setState(SchedulerState.standby);
			}
			info.setJobInstanceName(schedulerService.getJobInstanceName());
			List<JobInfo> jobs=new ArrayList<JobInfo>();
			for (String groupName : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					JobInfo jobInfo=new JobInfo();
					JobDetail jobDetail=scheduler.getJobDetail(jobKey);
					if(jobDetail instanceof SpringBeanJobExecutorDetail){
						SpringBeanJobExecutorDetail springBeanDetail=(SpringBeanJobExecutorDetail)jobDetail;
						jobInfo.setTargetJobInfo("SpringBean："+springBeanDetail.getTargetJobBeanId());
						jobInfo.setType(JobType.definition);
						
					}else if(jobDetail instanceof ScanJobExecutorDetail){
						jobInfo.setType(JobType.scan);
						jobInfo.setTargetJobInfo(jobDetail.getJobClass().getName());
					}
					Trigger trigger=scheduler.getTriggersOfJob(jobKey).get(0);
					jobInfo.setName(jobDetail.getKey().getName());
					jobInfo.setGroup(jobDetail.getKey().getGroup());
					jobInfo.setNextFireDate(trigger.getNextFireTime());
					jobInfo.setPreviousFireDate(trigger.getPreviousFireTime());
					if(trigger instanceof CronTriggerImpl){
						CronTriggerImpl cron=(CronTriggerImpl)trigger;
						jobInfo.setTriggerInfo("Cron表达式类型触发器["+cron.getCronExpression()+"]");
					}else{
						jobInfo.setTriggerInfo("触发器类型:"+trigger.getClass());
					}
					jobs.add(jobInfo);
					
				}
				info.setJobs(jobs);
			}
		}
		return info;
	}
	
	@Expose
	public String resetScheduler() throws Exception{
		if(schedulerService.isRunJobInCurrentInstance()){
			schedulerService.resetScheduer();
			return null;
		}else{
			return "JOB调试服务重启失败，当前实例不允许运行JOB!";
		}
	}
}
