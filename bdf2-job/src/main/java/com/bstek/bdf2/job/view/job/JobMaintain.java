package com.bstek.bdf2.job.view.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.job.JobHibernateDao;
import com.bstek.bdf2.job.model.JobCalendar;
import com.bstek.bdf2.job.model.JobCalendarRelation;
import com.bstek.bdf2.job.model.JobDefinition;
import com.bstek.bdf2.job.model.JobHistory;
import com.bstek.bdf2.job.model.JobParameter;
import com.bstek.bdf2.job.model.JobState;
import com.bstek.bdf2.job.service.IJobDataService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-3-10
 */
@Component("bdf2.jobMaintain")
public class JobMaintain extends JobHibernateDao implements InitializingBean{
	private IJobDataService dataService;
	private Collection<String> jobBeanIds;
	@DataProvider
	public Collection<JobParameter> loadJobParameters(String jobId) throws Exception{
		String hql="from "+JobParameter.class.getName()+" where jobId=:jobId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("jobId", jobId);
		return this.query(hql,paramMap);
	}
	
	@DataResolver
	public void saveJobParameters(Collection<JobParameter> parameters){
		Session session=this.getSessionFactory().openSession();
		try{
			for(JobParameter p:parameters){
				EntityState state=EntityUtils.getState(p);
				if(state.equals(EntityState.NEW)){
					p.setId(UUID.randomUUID().toString());
					session.save(p);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(p);
				}
				if(state.equals(EntityState.DELETED)){
					session.delete(p);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@DataProvider
	public void loadJobs(Page<JobDefinition> page,Criteria criteria) throws Exception{
		String hql="from "+JobDefinition.class.getName()+" j where j.companyId=:companyId";
		String companyId=dataService.getCompanyId();
		ParseResult result=this.parseCriteria(criteria, true, "j");
		Map<String,Object> paramMap=null;
		if(result!=null){
			paramMap=result.getValueMap();
			hql+=" and "+result.getAssemblySql().toString();
		}else{
			paramMap=new HashMap<String,Object>();
		}
		paramMap.put("companyId", companyId);
		this.pagingQuery(page,hql,"select count(*) "+hql, paramMap);
	}
	@DataProvider
	public Collection<JobCalendar> loadAllCalendars(){
		String hql="from "+JobCalendar.class.getName()+" where companyId=:companyId";
		String companyId=dataService.getCompanyId();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("companyId", companyId);
		return this.query(hql,paramMap);
	}
	@DataProvider
	public Collection<JobCalendar> loadCalendars(String jobId){
		String hql="from "+JobCalendarRelation.class.getName()+" j where j.jobId=:jobId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("jobId",jobId);
		List<JobCalendar> result=new ArrayList<JobCalendar>();
		List<JobCalendarRelation> relations=this.query(hql, map);
		Session session=this.getSessionFactory().openSession();
		try{
			for(JobCalendarRelation r:relations){
				result.add((JobCalendar)session.get(JobCalendar.class, r.getCalendarId()));
			}			
		}finally{
			session.flush();
			session.close();
		}
		return result;
	}
	
	@DataProvider
	public void loadJobHistories(Page<JobDefinition> page,Criteria criteria,String jobDefId) throws Exception{
		String hql="from "+JobHistory.class.getName()+" j where j.jobId=:jobId";
		ParseResult result=this.parseCriteria(criteria, true, "j");
		Map<String,Object> paramMap=null;
		if(result!=null){
			paramMap=result.getValueMap();
			hql+=" and "+result.getAssemblySql().toString();
		}else{
			paramMap=new HashMap<String,Object>();
		}
		paramMap.put("jobId", jobDefId);
		this.pagingQuery(page,hql+" order by j.startDate desc","select count(*) "+hql, paramMap);
	}
	
	@DataProvider
	public Collection<JobInfo> loadJobInfos() throws Exception{
		List<JobInfo> infos=new ArrayList<JobInfo>();
		for(String beanId:jobBeanIds){
			JobInfo info=new JobInfo();
			info.setBeanId(beanId);
			infos.add(info);
		}
		return infos;
	}
	
	@DataProvider
	public Collection<CronDate> parseCronExpression(String cron) throws Exception{
		CronExpression expr=new CronExpression(cron);
		List<CronDate> dates=new ArrayList<CronDate>();
		Date startDate=new Date();
		for(int i=0;i<50;i++){
			startDate=expr.getNextValidTimeAfter(startDate);
			CronDate cd=new CronDate();
			cd.setDate(startDate);
			dates.add(cd);
		}
		return dates;
	}
	
	@Expose
	public void stopJob(String jobDefinitionId){
		Session session=this.getSessionFactory().openSession();
		try{
			JobDefinition job=(JobDefinition)session.get(JobDefinition.class, jobDefinitionId);
			job.setState(JobState.stopping);
			session.update(job);
		}finally{
			session.flush();
			session.close();
		}
	}

	@Expose
	public void runJob(String jobDefinitionId){
		Session session=this.getSessionFactory().openSession();
		try{
			JobDefinition job=(JobDefinition)session.get(JobDefinition.class, jobDefinitionId);
			job.setState(JobState.running);				
			session.update(job);
		}finally{
			session.flush();
			session.close();
		}
	}	
	
	
	@DataResolver
	public void saveJobs(Collection<JobDefinition> jobs) throws Exception{
		Session session=this.getSessionFactory().openSession();
		try{
			for(JobDefinition job:jobs){
				EntityState state=EntityUtils.getState(job);
				if(state.equals(EntityState.NEW)){
					job.setId(UUID.randomUUID().toString());
					job.setState(JobState.ready);
					job.setCompanyId(dataService.getCompanyId());
					session.save(job);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(job);
				}
				if(state.equals(EntityState.DELETED)){
					String hql="delete "+JobHistory.class.getName()+" where jobId=:jobId";
					session.createQuery(hql).setString("jobId",job.getId()).executeUpdate();
					hql="delete "+JobCalendarRelation.class.getName()+" where jobId=:jobId";
					session.createQuery(hql).setString("jobId", job.getId()).executeUpdate();
					hql="delete "+JobParameter.class.getName()+" where jobId=:jobId";
					session.createQuery(hql).setString("jobId", job.getId()).executeUpdate();
					session.delete(job);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public void saveJobCalendars(String calendarId,String jobId,String operation){
		Session session=this.getSessionFactory().openSession();
		try{
			if(operation.equals("add")){
				JobCalendarRelation relation=new JobCalendarRelation();
				relation.setId(UUID.randomUUID().toString());
				relation.setCalendarId(calendarId);
				relation.setJobId(jobId);
				session.save(relation);
			}else{
				String hql="delete "+JobCalendarRelation.class.getName()+" where calendarId=:calendarId and jobId=:jobId";
				session.createQuery(hql).setString("calendarId", calendarId).setString("jobId", jobId).executeUpdate();
			}			
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public void deleteJobHistory(Collection<String> historyIds){
		Session session=this.getSessionFactory().openSession();
		try{		
			for(String historyId:historyIds){
				String hql="delete "+JobHistory.class.getName()+" where id=:id";
				session.createQuery(hql).setString("id", historyId).executeUpdate();
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		Map<String,IJobDataService> map=this.getApplicationContext().getBeansOfType(IJobDataService.class);
		if(map.isEmpty()){
			throw new RuntimeException("Job module need a ["+IJobDataService.class.getName()+"] interface implementation");
		}
		dataService=map.values().iterator().next();
		jobBeanIds=this.getApplicationContext().getBeansOfType(Job.class).keySet();
	}
}
