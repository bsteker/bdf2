package com.bstek.bdf2.jasperreports.view.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.orm.DataSourceRepository;
import com.bstek.bdf2.jasperreports.ReportHibernateDao;
import com.bstek.bdf2.jasperreports.model.ReportDefinition;
import com.bstek.bdf2.jasperreports.model.ReportParameter;
import com.bstek.bdf2.jasperreports.model.ReportResource;
import com.bstek.bdf2.jasperreports.service.IReportDataProvider;
import com.bstek.bdf2.uploader.model.UploadDefinition;
import com.bstek.bdf2.uploader.service.IFileService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-5-11
 */
@Component("bdf2.reportDefinition")
public class ReportMaintain extends ReportHibernateDao{
	@DataProvider
	public void loadDefinitions(Page<ReportDefinition> page,Criteria criteria){
		DetachedCriteria dc=this.buildDetachedCriteria(criteria, ReportDefinition.class);
		dc.addOrder(Order.desc("createDate"));
		this.pagingQuery(page, dc);
		IFileService fileService=ContextHolder.getBean(IFileService.BEAN_ID);
		for(ReportDefinition report:page.getEntities()){
			UploadDefinition uploadDefinition=fileService.getUploadDefinition(report.getReportFile());
			if(uploadDefinition!=null){
				report.setReportFileName(uploadDefinition.getFileName());
			}
		}
	}
	
	@DataProvider
	public Collection<Map<String,Object>> loadDataSources(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		DataSourceRepository repository=ContextHolder.getApplicationContext().getBean(DataSourceRepository.class);
		for(String name:repository.getDataSources().keySet()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("name",name);
			list.add(map);
		}
		return list;
	}
	@DataProvider
	public Collection<Map<String,Object>> loadDataSourceProviders(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for(String name:ContextHolder.getApplicationContext().getBeansOfType(IReportDataProvider.class).keySet()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("name",name);
			list.add(map);
		}
		return list;
	}
	
	@DataProvider
	public Collection<ReportResource> loadResources(String definitionId){
		String hql="from "+ReportResource.class.getName()+" where reportDefinitionId=:definitionId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("definitionId", definitionId);
		Collection<ReportResource> resources=this.query(hql, map);
		IFileService fileService=ContextHolder.getBean(IFileService.BEAN_ID);
		for(ReportResource res:resources){
			String resFile=res.getResourceFile();
			if(StringUtils.hasText(resFile)){
				UploadDefinition uploadDefinition=fileService.getUploadDefinition(resFile);
				if(uploadDefinition!=null){
					res.setResourceFileName(uploadDefinition.getFileName());
				}
			}
		}
		return resources;
	}
	@DataProvider
	public Collection<ReportParameter> loadParameters(String definitionId){
		String hql="from "+ReportParameter.class.getName()+" where reportDefinitionId=:definitionId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("definitionId", definitionId);
		return this.query(hql, map);
	}
	
	@DataResolver
	public void saveParameters(Collection<ReportParameter> parameters){
		Session session=this.getSessionFactory().openSession();
		try{
			for(ReportParameter param:parameters){
				EntityState state=EntityUtils.getState(param);
				if(state.equals(EntityState.NEW)){
					param.setId(UUID.randomUUID().toString());
					session.save(param);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(param);
				}
				if(state.equals(EntityState.DELETED)){
					session.delete(param);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	@DataResolver
	public void saveResources(Collection<ReportResource> resources){
		Session session=this.getSessionFactory().openSession();
		IFileService fileService=ContextHolder.getBean(IFileService.BEAN_ID);
		try{
			for(ReportResource res:resources){
				EntityState state=EntityUtils.getState(res);
				if(state.equals(EntityState.NEW)){
					res.setId(UUID.randomUUID().toString());
					session.save(res);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(res);
				}
				if(state.equals(EntityState.DELETED)){
					String resFile=res.getResourceFile();
					if(StringUtils.hasText(resFile)){
						fileService.deleteUploadDefinition(resFile);
					}
					session.delete(res);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public String checkDefinitionExist(String id){
		String hql="select count(*) from "+ReportDefinition.class.getName()+" where id=:id";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		int count=this.queryForInt(hql, map);
		if(count>0){
			return "ID已存在";
		}else{
			return null;
		}
	}
	
	@DataResolver
	public void saveDefinitions(Collection<ReportDefinition> definitions){
		Session session=this.getSessionFactory().openSession();
		try{
			IFileService fileService=ContextHolder.getBean(IFileService.BEAN_ID);
			for(ReportDefinition definition:definitions){
				EntityState state=EntityUtils.getState(definition);
				if(state.equals(EntityState.NEW)){
					definition.setCreateDate(new Date());
					session.save(definition);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(definition);
				}
				if(state.equals(EntityState.DELETED)){
					fileService.deleteUploadDefinition(definition.getReportFile());
					String hql="delete "+ReportParameter.class.getName()+" where reportDefinitionId=:definitionId";
					session.createQuery(hql).setString("definitionId", definition.getId()).executeUpdate();
					hql="delete "+ReportResource.class.getName()+" where reportDefinitionId=:definitionId";
					session.createQuery(hql).setString("definitionId", definition.getId()).executeUpdate();
					session.delete(definition);
				}
				if(definition.getParameters()!=null){
					this.saveParameters(definition.getParameters());
				}
				if(definition.getResources()!=null){
					this.saveResources(definition.getResources());
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
}
