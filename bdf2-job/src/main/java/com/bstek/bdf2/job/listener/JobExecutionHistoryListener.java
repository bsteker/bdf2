package com.bstek.bdf2.job.listener;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Session;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bstek.bdf2.job.executor.SpringBeanJobExecutorDetail;
import com.bstek.bdf2.job.model.JobHistory;
import com.bstek.bdf2.job.service.IJobDefinitionService;

/**
 * @author Jacky.gao
 * @since 2013-3-10
 */
public class JobExecutionHistoryListener extends JobExecutionListener {
	private static final String START_DATE_KEY = "SYSTEM_STARTDATE_KEY";
	public String getName() {
		return "JobExecutionHistoryListener";
	}

	public void jobToBeExecuted(JobExecutionContext context) {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap map = jobDetail.getJobDataMap();
		map.put(START_DATE_KEY, new Date());
	}

	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		JobDetail jobDetail = context.getJobDetail();
		if (!(jobDetail instanceof SpringBeanJobExecutorDetail)) {
			return;
		}
		JobDataMap map = jobDetail.getJobDataMap();
		if (!map.containsKey(IJobDefinitionService.JOB_DEFINITION_ID)) {
			return;
		}
		Date end = new Date();
		String exception = getExceptionStackMessage(jobException);
		JobHistory history = new JobHistory();
		history.setSuccessful(exception == null ? true : false);
		if (exception != null) {
			history.setExceptionMessage(exception.length() > 1500 ? exception
					.substring(0, 1500) : exception);
		}
		history.setEndDate(end);
		history.setStartDate((Date)map.get(START_DATE_KEY));
		history.setId(UUID.randomUUID().toString());
		history.setJobId(map.getString(IJobDefinitionService.JOB_DEFINITION_ID));
		Session session = getSessionFactory().openSession();
		try {
			session.save(history);
		} finally {
			session.flush();
			session.close();
		}
	}

	public void jobExecutionVetoed(JobExecutionContext context) {

	}
}
