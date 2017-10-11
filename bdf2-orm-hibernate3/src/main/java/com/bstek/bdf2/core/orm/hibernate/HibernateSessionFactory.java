package com.bstek.bdf2.core.orm.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import com.bstek.bdf2.core.orm.AnnotationPackages;
import com.bstek.bdf2.core.orm.DataSourceRepository;

/**
 * 扩展自AnnotationSessionFactoryBean，实现对Hibernate配置文件的动态加载与解析
 * 
 * @since 2013-1-17
 * @author Jacky.gao
 */
public class HibernateSessionFactory extends AnnotationSessionFactoryBean implements
		ApplicationContextAware {
	public static final String BEAN_ID = "bdf2.sessionFactory";
	private String dataSourceName;
	private ApplicationContext applicationContext;
	private boolean asDefault = false;
	private List<String> scanPackages=new ArrayList<String>();
	private List<String> mappingResourceList=new ArrayList<String>();

	@Override
	public void setPackagesToScan(String[] packagesToScan) {
		for(String pkg:packagesToScan){
			scanPackages.add(pkg);
		}
	}
	
	@Override
	public void setMappingResources(String[] mappingResources) {
		for(String res:mappingResources){
			this.mappingResourceList.add(res);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		DataSourceRepository dataSourceRepository = applicationContext
				.getBean(DataSourceRepository.class);
		if (StringUtils.isNotEmpty(this.dataSourceName)) {
			Map<String, DataSource> dsMap = dataSourceRepository.getDataSources();
			for (String name : dsMap.keySet()) {
				if (name.equals(this.dataSourceName)) {
					this.setDataSource(dsMap.get(name));
					break;
				}
			}
			if (this.dataSourceName.equals(dataSourceRepository.getDefaultDataSourceName())) {
				this.setAsDefault(true);
			}
		}
		for (AnnotationPackages packages : applicationContext.getBeansOfType(
				AnnotationPackages.class).values()) {
			if (StringUtils.isNotEmpty(packages.getDataSourceRegisterName())) {
				if (this.dataSourceName.equals(packages.getDataSourceRegisterName())) {
					scanPackages.addAll(packages.getScanPackages());
				}
			} else if (this.asDefault) {
				scanPackages.addAll(packages.getScanPackages());
			}
		}
		if(scanPackages.size()>0){
			String[] tmp = new String[scanPackages.size()];
			scanPackages.toArray(tmp);
			super.setPackagesToScan(tmp);			
		}
		Map<String,MappingResources> mappingMap=applicationContext.getBeansOfType(MappingResources.class);
		for(MappingResources res:mappingMap.values()){
			String name=res.getDataSourceRegisterName();
			if(StringUtils.isNotEmpty(name)){
				if(this.dataSourceName.equals(name)){
					this.mappingResourceList.addAll(res.getResources());
				}
			}else if(this.asDefault){
				this.mappingResourceList.addAll(res.getResources());
			}
		}
		if(mappingResourceList.size()>0){
			String[] resources=new String[mappingResourceList.size()];
			mappingResourceList.toArray(resources);
			super.setMappingResources(resources);
		}
		super.afterPropertiesSet();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public boolean isAsDefault() {
		return asDefault;
	}

	public void setAsDefault(boolean asDefault) {
		this.asDefault = asDefault;
	}
}
