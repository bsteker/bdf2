package com.bstek.bdf2.core.orm;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bstek.bdf2.core.context.ContextHolder;

/**
 * 扫描系统添加所有DataSource
 * @author jacky.gao@bstek.com
 * @since 2.0
 */
public class DataSourceRepository implements InitializingBean,ApplicationContextAware{
	private String defaultDataSourceName;
	private ApplicationContext applicationContext;
	private Map<String,JdbcTemplate> jdbcTemplateMap=new HashMap<String,JdbcTemplate>();
	private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
	private JdbcTemplate defaultJdbcTemplate;
	
	/**
	 * 从数据源仓库中取得接下来线程要用到的数据源.
	 * 
	 * @return 真实的数据源
	 */
	public DataSource getRealDataSource() {
		String currentDataSourceName = ContextHolder.getCurrentDataSourceName();
		// 如果明确指定了当前线程使用的数据源的名称,则使用它
		// 这通常是在调用getJdbcTemplate("dataSourceName")等函数时指定的
		if (StringUtils.isNotEmpty(currentDataSourceName)) {
			return dataSources.get(currentDataSourceName);
		}
		// 如果当前线程中没有明确指定使用的数据源名称,使用默认
		if (StringUtils.isNotEmpty(this.defaultDataSourceName)) {
			return dataSources.get(this.defaultDataSourceName);
		}
		// 如果当前线程中没有明确指定使用的数据源名称,且没有默认名称,若仅有一个数据源,使用它
		if (1 == dataSources.size()) {
			return dataSources.values().iterator().next();
		}
		// 如果系统中多个数据源,且未明确指定当前线程使用哪个数据源,同时没有指定默认值,抛出异常
		throw new IllegalStateException("当前系统配置了多个数据源,您必须指定其一为默认.");
	}
	public void afterPropertiesSet() throws Exception {
		Map<String, DataSourceRegister> dataSourceRegisters = applicationContext.getBeansOfType(DataSourceRegister.class);
		for (DataSourceRegister dataSourceRegister : dataSourceRegisters.values()) {
			dataSources.put(dataSourceRegister.getName(), dataSourceRegister.getDataSource());
			JdbcDaoSupport jdbcDaoSupport = new JdbcDaoSupport(){};
			jdbcDaoSupport.setDataSource(dataSourceRegister.getDataSource());
			jdbcTemplateMap.put(dataSourceRegister.getName(),jdbcDaoSupport.getJdbcTemplate());
			if(dataSourceRegister.isAsDefault()){
				defaultDataSourceName=dataSourceRegister.getName();
				this.defaultJdbcTemplate=jdbcDaoSupport.getJdbcTemplate();
			}
		}
	}
	
	public Map<String, JdbcTemplate> getJdbcTemplateMap() {
		return jdbcTemplateMap;
	}
	
	public NamedParameterJdbcTemplate getDefaultNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(this.getDefaultJdbcTemplate());
	}
	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(String dataSourceName) {
		if(this.jdbcTemplateMap.containsKey(dataSourceName)){
			return new NamedParameterJdbcTemplate(jdbcTemplateMap.get(dataSourceName));
		}else{
			return getDefaultNamedParameterJdbcTemplate();			
		}
	}
	
	public JdbcTemplate getDefaultJdbcTemplate() {
		return defaultJdbcTemplate;
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}
	public String getDefaultDataSourceName() {
		return defaultDataSourceName;
	}
	public Map<String, DataSource> getDataSources() {
		return dataSources;
	}
}
