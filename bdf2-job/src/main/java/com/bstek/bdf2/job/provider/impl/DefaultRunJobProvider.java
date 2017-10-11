package com.bstek.bdf2.job.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.bdf2.job.model.JobDefinition;
import com.bstek.bdf2.job.model.JobState;
import com.bstek.bdf2.job.provider.IRunJobProvider;
import com.bstek.bdf2.job.provider.RunJob;
import com.bstek.bdf2.job.service.IJobDefinitionService;

/**
 * @author Jacky.gao
 * @since 2013-3-29
 */
public class DefaultRunJobProvider implements IRunJobProvider {
	private IJobDefinitionService jobDefinitionService;
	private final Log logger=LogFactory.getLog(getClass());
	public Collection<RunJob> getRunJobs() {		
		List<RunJob> jobs=new ArrayList<RunJob>();
		List<JobDefinition> jobDefinitions=jobDefinitionService.loadJobs(JobState.run,true);
		logger.info("There are "+jobDefinitions.size()+" jobs to run for startup");
		for(JobDefinition jobDef:jobDefinitions){
			RunJob job=new RunJob();
			job.setJobDefinition(jobDef);
			jobs.add(job);
		}
		return jobs;
	}
	public IJobDefinitionService getJobDefinitionService() {
		return jobDefinitionService;
	}
	public void setJobDefinitionService(IJobDefinitionService jobDefinitionService) {
		this.jobDefinitionService = jobDefinitionService;
	}
}
