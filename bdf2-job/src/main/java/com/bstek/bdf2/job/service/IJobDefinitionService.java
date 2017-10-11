package com.bstek.bdf2.job.service;

import java.util.List;

import com.bstek.bdf2.job.model.JobDefinition;
import com.bstek.bdf2.job.model.JobState;

public interface IJobDefinitionService {
	public static final String BEAN_ID="bdf2.job.jobDefinitionService";
	public static final String JOB_DEFINITION_ID="_job_definition_id_";
	List<JobDefinition> loadJobs(JobState state,boolean fillCalendar);
	JobDefinition getJobDefinition(String jobDefId);
}
