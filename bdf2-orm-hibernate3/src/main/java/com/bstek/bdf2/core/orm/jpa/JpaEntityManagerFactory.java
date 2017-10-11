package com.bstek.bdf2.core.orm.jpa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.bstek.bdf2.core.orm.AnnotationPackages;
import com.bstek.bdf2.core.orm.DataSourceRegister;

/**
 * @since 2013-1-17
 * @author Jacky.gao
 */
public class JpaEntityManagerFactory extends LocalContainerEntityManagerFactoryBean implements ApplicationContextAware{
	private String dataSourceName;
	private List<String> scanPackages;
	private boolean asDefault;
	@Override
	public void setPackagesToScan(String... packagesToScan) {
		for(int i=0;i<packagesToScan.length;i++){
			scanPackages.add(packagesToScan[i]);			
		}
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
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		for (DataSourceRegister dataSourceRegister : applicationContext.getBeansOfType(DataSourceRegister.class).values()) {
			if(dataSourceRegister.getName().equals(dataSourceName)){
				this.setDataSource(dataSourceRegister.getDataSource());
				if(dataSourceRegister.isAsDefault()){
					this.asDefault=true;
				}
			}
		}
		
		List<String> packageNames=new ArrayList<String>();
		for(AnnotationPackages packages:applicationContext.getBeansOfType(AnnotationPackages.class).values()){
			packageNames.addAll(packages.getScanPackages());
		}
		if(scanPackages!=null){
			packageNames.addAll(scanPackages);			
		}
		if(packageNames.size()>0){
			String[] tmp=new String[packageNames.size()];
			packageNames.toArray(tmp);
			super.setPackagesToScan(tmp);
		}
	}
}
