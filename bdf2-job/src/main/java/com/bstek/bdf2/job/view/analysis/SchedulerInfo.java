package com.bstek.bdf2.job.view.analysis;

import java.util.List;

public class SchedulerInfo {
	private String jobInstanceName;
	private boolean runJob;
	private SchedulerState state;
	private List<JobInfo> jobs;
	public String getJobInstanceName() {
		return jobInstanceName;
	}
	public void setJobInstanceName(String jobInstanceName) {
		this.jobInstanceName = jobInstanceName;
	}
	public boolean isRunJob() {
		return runJob;
	}
	public void setRunJob(boolean runJob) {
		this.runJob = runJob;
	}
	public SchedulerState getState() {
		return state;
	}
	public void setState(SchedulerState state) {
		this.state = state;
	}
	public List<JobInfo> getJobs() {
		return jobs;
	}
	public void setJobs(List<JobInfo> jobs) {
		this.jobs = jobs;
	}
}
