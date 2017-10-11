package com.bstek.bdf2.jbpm4.job.reminder;

import org.hibernate.Session;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.ReminderJob;
import com.bstek.bdf2.jbpm4.model.ReminderJobState;

public class CancelReminderJobBean extends Jbpm4HibernateDao{
	public static final String BEAN_ID="bdf2.jbpm4.cancelReminderJobBean";
	public void cancelReminderJob(String reminderJobId){
		Session session=this.getSessionFactory().openSession();
		try{
			ReminderJob job=(ReminderJob)session.get(ReminderJob.class, reminderJobId);
			job.setState(ReminderJobState.deleted);
			session.update(job);
		}finally{
			session.flush();
			session.close();
		}
	}
}
