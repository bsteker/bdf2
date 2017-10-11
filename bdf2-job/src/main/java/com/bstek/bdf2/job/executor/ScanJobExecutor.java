package com.bstek.bdf2.job.executor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bstek.bdf2.job.model.JobDefinition;
import com.bstek.bdf2.job.model.JobState;
import com.bstek.bdf2.job.service.IJobDefinitionService;
import com.bstek.bdf2.job.service.IJobService;

/**
 * @author Jacky.gao
 * @since 2013-3-12
 */
/**
 * @author Jacky.gao
 * @since 2013-3-12
 */
public class ScanJobExecutor implements Job {
	protected final Log logger = LogFactory.getLog(getClass());
	public static final String JOB_ID="background_scan_job_definition";
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScanJobExecutorDetail jobDetail=(ScanJobExecutorDetail)context.getJobDetail();
		Session session=jobDetail.getSessionFactory().openSession();
		boolean rollback=false;
		Transaction trans=session.getTransaction();
		trans.begin();
		try{
			IJobDefinitionService jobDefinitionService=jobDetail.getJobDefinitionService();
			List<JobDefinition> jobDefinitions=jobDefinitionService.loadJobs(JobState.running,true);
			logger.info("There are "+jobDefinitions.size()+" jobs to run");
			IJobService jobService=jobDetail.getJobService();
			for(JobDefinition jobDef:jobDefinitions){
				jobService.addJobToScheduler(jobDef);
				jobDef.setState(JobState.run);
				session.update(jobDef);
			}
			jobDefinitions=jobDefinitionService.loadJobs(JobState.stopping,false);
			logger.info("There are "+jobDefinitions.size()+" jobs to stop");
			for(JobDefinition jobDef:jobDefinitions){
				jobService.removeJobFromScheduler(jobDef.getId(), jobDef.getCompanyId());
				jobDef.setState(JobState.stop);
				session.update(jobDef);
			}
		}catch(Exception ex){
			trans.rollback();
			rollback=true;
			ex.printStackTrace();
		}finally{
			if(!rollback){
				trans.commit();			
			}
			session.flush();
			session.close();
		}
	}

}
