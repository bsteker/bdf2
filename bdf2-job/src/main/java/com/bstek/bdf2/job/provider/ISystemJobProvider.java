package com.bstek.bdf2.job.provider;

import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public interface ISystemJobProvider {
	public static final String GROUP_ID="background_system";
	JobDetail getJobDetail();
	Trigger getTrigger();
}
