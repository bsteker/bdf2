package com.bstek.bdf2.job.daemon.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.job.daemon.detection.InstanceDetection;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-5-8
 */
public class DaemonStatusController extends AbstractResolver implements InitializingBean{
	private InstanceDetection instanceDetection;
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest req,HttpServletResponse res) throws Exception {
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Scheduler scheduler=instanceDetection.getScheduler();
		StringBuffer sb=new StringBuffer();
		sb.append("<strong>Status</strong>:");
		if(scheduler.isInStandbyMode()){
			sb.append("Standby");
		}
		if(scheduler.isShutdown()){
			sb.append("Shutdown");
		}
		if(scheduler.isStarted()){
			sb.append("Started");
		}
		sb.append("<br>");
		for (String groupName : scheduler.getJobGroupNames()) {
		     for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
		    	 JobDetail jobDetail=scheduler.getJobDetail(jobKey);
		    	 sb.append("<strong>JobClass:</strong>");
		    	 sb.append(jobDetail.getJobClass().getName());
		    	 
		    	 Trigger trigger=scheduler.getTriggersOfJob(jobDetail.getKey()).get(0);
		    	 sb.append("<br><strong>PreviousFireDate:</strong>");
		    	 if(trigger.getPreviousFireTime()!=null){
		    		 sb.append(sd.format(trigger.getPreviousFireTime()));		    		 
		    	 }
		    	 sb.append("<br><strong>NextFireDate:</strong>");
		    	 if(sd.format(trigger.getNextFireTime())!=null){
		    		 sb.append(sd.format(trigger.getNextFireTime()));		    		 
		    	 }
		    	 if(trigger instanceof CronTriggerImpl){
		    		 CronTriggerImpl cron=(CronTriggerImpl)trigger;
		    		 sb.append("<br><strong>CronExpression:</strong>");
		    		 sb.append(cron.getCronExpression());
		    	 }
		    	 
		     }
		}
		res.setContentType("text/html; charset=utf-8");
		PrintWriter pw=res.getWriter();
		pw.write(sb.toString());
		pw.flush();
		pw.close();
		return null;
	}
	public void afterPropertiesSet() throws Exception {
		ApplicationContext context=this.getApplicationContext().getParent();
		if(context!=null){
			Collection<InstanceDetection> coll=context.getBeansOfType(InstanceDetection.class).values();
			if(coll.size()>0){
				instanceDetection=coll.iterator().next();
			}
		}
	}
}
