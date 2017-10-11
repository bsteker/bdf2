package com.bstek.bdf2.core.orm.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.bdf2.core.orm.AbstractDao;
import com.bstek.bdf2.core.orm.DataSourceRepository;
import com.bstek.bdf2.core.orm.jdbc.dialect.IDialect;
import com.bstek.dorado.data.provider.Page;

/**
 * @since 2013-1-17
 * @author Jacky.gao
 */
public abstract class JdbcDao extends AbstractDao implements ApplicationContextAware{
	private ApplicationContext applicationContext;
	private DataSourceRepository dataSourceRepository;
	private Collection<IDialect> dialects;
	
	protected void pagingQuery(Page<?> page,String sql,String dataSourceName,RowMapper<?> mapper,Map<String,Object> parameters){
		this.pagingQuery(page, sql, dataSourceName,parameters, mapper);
	}
	protected void pagingQuery(Page<?> page,String sql,String dataSourceName,Map<String,Object> parameters){
		this.pagingQuery(page, sql, dataSourceName,parameters,null);
	}
	protected void pagingQuery(Page<?> page,String sql,Map<String,Object> parameters){
		this.pagingQuery(page, sql, null,parameters,null);
	}
	protected void pagingQuery(Page<?> page,String sql,RowMapper<?> mapper,Map<String,Object> parameters){
		this.pagingQuery(page, sql,null,parameters, mapper);
	}

	protected void pagingQuery(Page<?> page,String sql,String dataSourceName,RowMapper<?> mapper){
		pagingQuery(page,sql,null,dataSourceName,mapper);
	}
	protected void pagingQuery(Page<?> page,String sql,RowMapper<?> mapper){
		String currentDatasourceName=this.getDataSourceName(null);
		pagingQuery(page,sql,null,currentDatasourceName,mapper);
	}
	protected void pagingQuery(Page<?> page,String sql,Object parameters[],RowMapper<?> mapper){
		String currentDatasourceName=this.getDataSourceName(null);
		pagingQuery(page,sql,parameters,currentDatasourceName,mapper);
	}
	
	
	protected void pagingQuery(Page<?> page,String sql,String dataSourceName){
		pagingQuery(page,sql,null,dataSourceName,null);
	}
	protected void pagingQuery(Page<?> page,String sql){
		String currentDatasourceName=this.getDataSourceName(null);
		pagingQuery(page,sql,null,currentDatasourceName,null);
	}
	protected void pagingQuery(Page<?> page,String sql,Object parameters[]){
		String currentDatasourceName=this.getDataSourceName(null);
		pagingQuery(page,sql,parameters,currentDatasourceName,null);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void pagingQuery(Page<?> page,String sql,Object parameters[],String dataSourceName,RowMapper<?> mapper){
		JdbcTemplate jdbcTemplate=null;
		if(dataSourceName==null){
			jdbcTemplate=this.getJdbcTemplate();			
		}else{
			jdbcTemplate=this.getJdbcTemplate(dataSourceName);			
		}
		IDialect dialect = getDialect(jdbcTemplate);
		if (dialect == null){
			throw new RuntimeException("无法找到与当前数据连接匹配的数据库方言类");
		}
		String querySql = dialect.getPaginationSql(sql, page.getPageNo(), page.getPageSize());
		String countSql="select count(*) from ("+sql+") sub_table_alias_";
		if(parameters != null){
			if(mapper == null){
				page.setEntities((List)jdbcTemplate.queryForList(querySql,parameters));
			}else{
				page.setEntities((List)jdbcTemplate.query(querySql,parameters,mapper));
			}
			page.setEntityCount(jdbcTemplate.queryForObject(countSql,parameters, Integer.class));								
		}else{
			if(mapper == null){
				page.setEntities((List)jdbcTemplate.queryForList(querySql));				
			}else{
				page.setEntities((List)jdbcTemplate.query(querySql,mapper));								
			}
			page.setEntityCount(jdbcTemplate.queryForObject(countSql, Integer.class));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void pagingQuery(Page<?> page,String sql,String dataSourceName,Map<String,Object> parameters,RowMapper<?> mapper){
		NamedParameterJdbcTemplate namedjdbcTemplate=this.getNamedParameterJdbcTemplate(dataSourceName);			
		String querySql=this.getDialect(this.getJdbcTemplate(dataSourceName)).getPaginationSql(sql, page.getPageNo(), page.getPageSize());
		String countSql="select count(*) from ("+sql+") sub_table_alias_";
		if(parameters!=null){
			if(mapper==null){
				page.setEntities((List)namedjdbcTemplate.queryForList(querySql,parameters));
			}else{
				page.setEntities((List)namedjdbcTemplate.query(querySql,parameters,mapper));
			}
			page.setEntityCount(namedjdbcTemplate.queryForObject(countSql,parameters, Integer.class));								
		}else{
			JdbcTemplate jdbcTemplate=this.getJdbcTemplate(dataSourceName);
			if(mapper==null){
				page.setEntities((List)jdbcTemplate.queryForList(querySql));				
			}else{
				page.setEntities((List)jdbcTemplate.query(querySql,mapper));								
			}
			page.setEntityCount(jdbcTemplate.queryForObject(countSql, Integer.class));
		}
	}
	
	public JdbcTemplate getJdbcTemplate(){
		String currentDataSourceName=this.getDataSourceName(null);
		if(currentDataSourceName==null){
			return this.dataSourceRepository.getDefaultJdbcTemplate();			
		}else{
			Map<String,JdbcTemplate> map=this.dataSourceRepository.getJdbcTemplateMap();
			if(map.containsKey(currentDataSourceName)){
				return map.get(currentDataSourceName);
			}else{
				return this.dataSourceRepository.getDefaultJdbcTemplate();
			}
		}
	}
	public JdbcTemplate getJdbcTemplate(String dataSourceName){
		if(dataSourceName==null){
			return this.dataSourceRepository.getDefaultJdbcTemplate();			
		}else{
			Map<String,JdbcTemplate> map=this.dataSourceRepository.getJdbcTemplateMap();
			if(map.containsKey(dataSourceName)){
				return map.get(dataSourceName);
			}else{
				return this.dataSourceRepository.getDefaultJdbcTemplate();
			}
		}
	}
	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
		String currentDataSourceName=this.getDataSourceName(null);
		return this.getNamedParameterJdbcTemplate(currentDataSourceName);
	}
	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(String dataSourceName){
		if(StringUtils.isNotEmpty(dataSourceName)){
			return this.dataSourceRepository.getNamedParameterJdbcTemplate(dataSourceName);
		}else{
			return this.dataSourceRepository.getDefaultNamedParameterJdbcTemplate();
		}
	}
	
	protected IDialect getDialect(JdbcTemplate jdbcTemplate){
		return jdbcTemplate.execute(new ConnectionCallback<IDialect>(){
			public IDialect doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				IDialect result=null;
				for(IDialect dialect:dialects){
					if(dialect.support(connection)){
						result=dialect;
						break;
					}
				}
				return result;
			}
		});
	}
	
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
		Collection<DataSourceRepository> dataSourceRepositoryCollection=applicationContext.getBeansOfType(DataSourceRepository.class).values();
		if(dataSourceRepositoryCollection.size()>0){
			this.dataSourceRepository=dataSourceRepositoryCollection.iterator().next();			
		}else if(applicationContext.getParent()!=null){
			dataSourceRepositoryCollection=applicationContext.getParent().getBeansOfType(DataSourceRepository.class).values();
			if(dataSourceRepositoryCollection.size()>0){
				this.dataSourceRepository=dataSourceRepositoryCollection.iterator().next();		
			}
		}
		this.dialects=applicationContext.getBeansOfType(IDialect.class).values();
	}
}
