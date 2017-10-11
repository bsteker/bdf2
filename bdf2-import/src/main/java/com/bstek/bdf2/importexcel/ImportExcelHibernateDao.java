package com.bstek.bdf2.importexcel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.provider.Page;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 * 
 */
public class ImportExcelHibernateDao extends HibernateDao {
	public static final String dataSourceRegisterName = "bdf2.import.dataSourceRegisterName";

	public void save(Object entity) {
		Session session=this.getSessionFactory(dataSourceRegisterName).openSession();
		try{
			session.saveOrUpdate(entity);
		}finally{
			session.flush();
			session.close();
		}
	}

	public void update(Object entity) {
		this.save(entity);
	}

	public void delete(Object entity) {
		Session session=this.getSessionFactory(dataSourceRegisterName).openSession();
		try{
			session.delete(entity);
		}finally{
			session.flush();
			session.close();
		}
	}

	public void findPageByCriteria(final DetachedCriteria detachedCriteria, final Page<?> page) throws Exception {
		pagingQuery(page, detachedCriteria);
	}

	@SuppressWarnings("rawtypes")
	public List findByCriteria(final DetachedCriteria detachedCriteria) throws Exception {
		Session session=this.getSessionFactory(dataSourceRegisterName).openSession();
		try{
			org.hibernate.Criteria criteria = detachedCriteria.getExecutableCriteria(session);
			return criteria.list();
		}finally{
			session.flush();
			session.close();
		}
	}

	@Override
	protected String getModuleFixDataSourceName() {
		String name = Configure.getString(dataSourceRegisterName);
		if (StringUtils.isNotEmpty(name)) {
			return name;
		} else {
			return null;
		}
	}

}
