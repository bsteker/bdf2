package com.bstek.bdf2.job.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.job.JobHibernateDao;
import com.bstek.bdf2.job.model.JobCalendar;
import com.bstek.bdf2.job.model.JobCalendarDate;
import com.bstek.bdf2.job.model.JobCalendarRelation;
import com.bstek.bdf2.job.model.JobDefinition;
import com.bstek.bdf2.job.model.JobParameter;
import com.bstek.bdf2.job.model.JobState;
import com.bstek.bdf2.job.service.IJobDataService;
import com.bstek.bdf2.job.service.IJobDefinitionService;

/**
 * @author Jacky.gao
 * @since 2013-3-9
 */
public class JobDefinitionServiceImpl extends JobHibernateDao implements
		IJobDefinitionService, InitializingBean {
	private IJobDataService dataService;

	public JobDefinition getJobDefinition(String jobDefId) {
		DetachedCriteria dc = DetachedCriteria.forClass(JobDefinition.class, "m");
		dc.add(Restrictions.eq("id", jobDefId));
		@SuppressWarnings("unchecked")
		Collection<JobDefinition> list = (Collection<JobDefinition>) this.query(dc);
		if (list.isEmpty()) {
			return null;
		} else {
			return list.iterator().next();
		}
	}
	
	public List<JobDefinition> loadJobs(JobState state, boolean fillCalendar) {
		String hql = "from " + JobDefinition.class.getName()
				+ " where state=:state";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", state);
		if (StringUtils.isNotEmpty(getFixedCompanyId())) {
			hql += " and companyId=:companyId";
			map.put("companyId", getFixedCompanyId());
		}
		List<JobDefinition> jobDefinitions = this.query(hql, map);
		if (fillCalendar) {
			for (JobDefinition jobDef : jobDefinitions) {
				jobDef.setCalendars(this.loadJobCalendar(jobDef.getId()));
				jobDef.setParameters(this.loadJobParameters(jobDef.getId()));
			}
		}
		jobDefinitions = dataService.filterJobs(jobDefinitions);
		return jobDefinitions;
	}

	private List<JobCalendar> loadJobCalendar(String jobDefId) {
		String hql = "from " + JobCalendarRelation.class.getName()
				+ " where jobId=:jobId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobDefId);
		List<JobCalendarRelation> relations = this.query(hql, map);
		List<JobCalendar> result = new ArrayList<JobCalendar>();
		for (JobCalendarRelation relation : relations) {
			hql = "from " + JobCalendar.class.getName() + " where id=:id";
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", relation.getCalendarId());
			List<JobCalendar> calendars = this.query(hql, m);
			for (JobCalendar jc : calendars) {
				hql = "from " + JobCalendarDate.class.getName()
						+ " where calendarId=:id";
				List<JobCalendarDate> dates = this.query(hql, m);
				jc.setCalendarDates(dates);
			}
			result.addAll(calendars);
		}
		return result;
	}

	private List<JobParameter> loadJobParameters(String jobDefId) {
		String hql = "from " + JobParameter.class.getName()
				+ " where jobId=:jobId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobDefId);
		return this.query(hql, map);
	}

	public void afterPropertiesSet() throws Exception {
		Map<String, IJobDataService> map = this.getApplicationContext()
				.getBeansOfType(IJobDataService.class);
		if (map.isEmpty()) {
			throw new RuntimeException("Job module need a ["
					+ IJobDataService.class.getName()
					+ "] interface implementation");
		}
		dataService = map.values().iterator().next();
	}

}
