package com.bstek.bdf2.job.daemon.heart.job;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bstek.bdf2.core.orm.AbstractDao;
import com.bstek.bdf2.job.daemon.model.Heartbeat;
import com.bstek.bdf2.job.service.ISchedulerService;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-5-7
 */
public class HeartJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		HeartJobDetail detail=(HeartJobDetail)context.getJobDetail();
		String instanceName=detail.getCurrentInstanceName();
		Session session=detail.getSessionFactory().openSession();
		try{
			String applicationName=Configure.getString(ISchedulerService.JOB_APPLICATION_NAME);
			String hql="from "+Heartbeat.class.getName()+" b where b.instanceName=:instanceName and b.applicationName=:applicationName";
			Query query=session.createQuery(hql).setString("instanceName",instanceName).setString("applicationName",applicationName);
			@SuppressWarnings("unchecked")
			List<Heartbeat> beats=query.list();
			Date now=new Date();
			Heartbeat beat=null;
			if(beats.size()>0){
				beat=beats.get(0);
			}else{
				beat=new Heartbeat();
				beat.setId(UUID.randomUUID().toString());
				beat.setApplicationName(applicationName);
			}
			beat.setDate(now);
			beat.setInstanceName(instanceName);
			session.saveOrUpdate(beat);
		}catch(Exception ex){
			throw new JobExecutionException(ex);
		}finally{
			session.flush();
			session.close();
		}
	}
	protected String getFixedCompanyId(){
		return Configure.getString(AbstractDao.FIXED_COMPANY_ID);
	}
}
