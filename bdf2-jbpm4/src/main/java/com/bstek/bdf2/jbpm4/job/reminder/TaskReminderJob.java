package com.bstek.bdf2.jbpm4.job.reminder;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.jbpm4.job.ITaskOverdueProcessor;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public class TaskReminderJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map=context.getJobDetail().getJobDataMap();
		if(map.containsKey("overdueDays")){
			int overdueDays=map.getInt("overdueDays");
			Date createDate=(Date)map.get("createDate");
			if(defaultCalculateOverdueDays(overdueDays,createDate)){
				if(map.getBoolean("overdueMethodSendMessage")){
					executeRemind(map);											
				}else{
					String overdueCustomBeanProcessor=map.getString("overdueCustomBeanProcessor");
					ITaskOverdueProcessor processor=ContextHolder.getBean(overdueCustomBeanProcessor);
					processor.process(map.getString("taskId"));
					String reminderJobId=map.getString("reminderJobId");
					CancelReminderJobBean cancelReminderJobBean=ContextHolder.getBean(CancelReminderJobBean.BEAN_ID);
					cancelReminderJobBean.cancelReminderJob(reminderJobId);
				}
			}
		}else{
			executeRemind(map);			
		}
	}

	private void executeRemind(JobDataMap map) {
		String taskId=map.getString("taskId");
		String messageSenders=map.getString("messageSenders");
		String principal=map.getString("principal");
		String messageTemplateId=map.getString("messageTemplateId");
		TaskReminderMessageSender sender=ContextHolder.getBean(TaskReminderMessageSender.BEAN_ID);
		sender.sendReminderMessage(taskId, principal, messageTemplateId, messageSenders.split(","));
	}
	
	private boolean defaultCalculateOverdueDays(int overdueDays,Date createDate){
		Map<String,ICalculateOverdueTaskReminder> map=ContextHolder.getApplicationContext().getBeansOfType(ICalculateOverdueTaskReminder.class);
		if(map.size()>0){
			return map.values().iterator().next().calculateOverdue(overdueDays, createDate);
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(createDate);
		calendar.add(Calendar.DAY_OF_MONTH, overdueDays);
		Date overdueDate=calendar.getTime();
		Date now=new Date();
		int result=overdueDate.compareTo(now);
		if(result==0 || result<0){
			return true;
		}else{
			return false;
		}
	}
}
