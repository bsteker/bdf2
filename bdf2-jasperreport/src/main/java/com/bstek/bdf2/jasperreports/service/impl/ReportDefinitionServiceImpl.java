package com.bstek.bdf2.jasperreports.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import com.bstek.bdf2.jasperreports.ReportHibernateDao;
import com.bstek.bdf2.jasperreports.model.ReportDefinition;
import com.bstek.bdf2.jasperreports.model.ReportParameter;
import com.bstek.bdf2.jasperreports.model.ReportResource;
import com.bstek.bdf2.jasperreports.service.IReportDefinitionService;

/**
 * @author Jacky.gao
 * @since 2013-5-12
 */
public class ReportDefinitionServiceImpl extends ReportHibernateDao implements IReportDefinitionService{

	public ReportDefinition loadReportDefinition(String id) {
		Session session=this.getSessionFactory().openSession();
		try{
			return (ReportDefinition)session.get(ReportDefinition.class, id);
		}finally{
			session.close();
		}
	}

	public Collection<ReportParameter> loadReportParameter(String reportDefinitionId) {
		String hql="from "+ReportParameter.class.getName()+" where reportDefinitionId=:reportDefinitionId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("reportDefinitionId",reportDefinitionId);
		return this.query(hql, map);
	}

	public Collection<ReportResource> loadReportResouces(String reportDefinitionId) {
		String hql="from "+ReportResource.class.getName()+" where reportDefinitionId=:reportDefinitionId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("reportDefinitionId",reportDefinitionId);
		return this.query(hql, map);
	}

	public Collection<ReportDefinition> loadSubReportDefinitions(String reportDefinitionId) {
		String hql="from "+ReportDefinition.class.getName()+" where parentId=:reportDefinitionId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("reportDefinitionId",reportDefinitionId);
		return this.query(hql, map);
	}
}
