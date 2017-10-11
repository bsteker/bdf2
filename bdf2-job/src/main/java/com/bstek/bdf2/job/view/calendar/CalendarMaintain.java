package com.bstek.bdf2.job.view.calendar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.job.JobHibernateDao;
import com.bstek.bdf2.job.model.JobCalendar;
import com.bstek.bdf2.job.model.JobCalendarDate;
import com.bstek.bdf2.job.model.JobCalendarRelation;
import com.bstek.bdf2.job.service.IJobDataService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;

/**
 * @author Jacky.gao
 * @since 2013-3-12
 */
@Component("bdf2.calendarMaintain")
public class CalendarMaintain extends JobHibernateDao implements InitializingBean{
	private IJobDataService dataService;
	@DataProvider
	public Collection<JobCalendar> loadCalendars(){
		String hql="from "+JobCalendar.class.getName()+" where companyId=:companyId";
		String companyId=dataService.getCompanyId();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("companyId", companyId);
		return this.query(hql,paramMap);
	}
	@DataProvider
	public Collection<JobCalendar> loadCalendarDates(String calendarId){
		String hql="from "+JobCalendarDate.class.getName()+" where calendarId=:calendarId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("calendarId", calendarId);
		return this.query(hql,paramMap);
	}
	
	@DataResolver
	public void saveCalendars(Collection<JobCalendar> calendars){
		SessionFactory sessionFactory=this.getSessionFactory();
		Session session=sessionFactory.openSession();
		try{
			for(JobCalendar c:calendars){
				EntityState state=EntityUtils.getState(c);
				if(state.equals(EntityState.NEW)){
					c.setId(UUID.randomUUID().toString());
					c.setCompanyId(dataService.getCompanyId());
					session.save(c);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(c);
				}
				if(state.equals(EntityState.DELETED)){
					String hql="select count(*) from "+JobCalendarRelation.class.getName()+" where calendarId=:calendarId";
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("calendarId", c.getId());
					int count=this.queryForInt(hql, map);
					if(count>0){
						throw new RuntimeException("当前日期有Job正在使用，不能被删除");
					}
					hql="delete "+JobCalendarDate.class.getName()+" where calendarId=:calendarId";
					session.createQuery(hql).setString("calendarId", c.getId()).executeUpdate();
					session.delete(c);
				}
				if(c.getCalendarDates()!=null){
					this.saveCalendarDates(c.getCalendarDates(), session);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	private void saveCalendarDates(Collection<JobCalendarDate> dates,Session session){
		for(JobCalendarDate c:dates){
			EntityState state=EntityUtils.getState(c);
			if(state.equals(EntityState.NEW)){
				c.setId(UUID.randomUUID().toString());
				session.save(c);
			}
			if(state.equals(EntityState.MODIFIED)){
				session.update(c);
			}
			if(state.equals(EntityState.DELETED)){
				session.delete(c);
			}
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		Map<String,IJobDataService> map=this.getApplicationContext().getBeansOfType(IJobDataService.class);
		if(map.isEmpty()){
			throw new RuntimeException("Job module need a ["+IJobDataService.class.getName()+"] interface implementation");
		}
		dataService=map.values().iterator().next();
	}
}
