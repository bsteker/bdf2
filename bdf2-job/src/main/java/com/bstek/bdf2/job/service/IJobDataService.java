package com.bstek.bdf2.job.service;

import java.util.List;

import com.bstek.bdf2.job.model.JobDefinition;

/**
 * @author Jacky.gao
 * @since 2013-3-10
 */
public interface IJobDataService {
	String getCompanyId();
	List<JobDefinition> filterJobs(List<JobDefinition> jobs);
}
