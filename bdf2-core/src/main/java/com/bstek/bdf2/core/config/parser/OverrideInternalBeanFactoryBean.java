package com.bstek.bdf2.core.config.parser;

import org.springframework.beans.factory.FactoryBean;

/**
 * @since 2013-1-29
 * @author Jacky.gao
 */
public class OverrideInternalBeanFactoryBean implements FactoryBean<Object> {
	private Object referenceBean;
	private Class<?> targetBeanClass;
	private String referenceBeanId;
	public Object getObject() throws Exception {
		if(!targetBeanClass.isAssignableFrom(referenceBean.getClass())){
			throw new RuntimeException("Bean "+referenceBeanId+" must be implement "+targetBeanClass.getName());
		}
		return this.referenceBean;
	}

	public Class<?> getObjectType() {
		return Object.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public Object getReferenceBean() {
		return referenceBean;
	}

	public void setReferenceBean(Object referenceBean) {
		this.referenceBean = referenceBean;
	}

	public Class<?> getTargetBeanClass() {
		return targetBeanClass;
	}

	public void setTargetBeanClass(Class<?> targetBeanClass) {
		this.targetBeanClass = targetBeanClass;
	}

	public String getReferenceBeanId() {
		return referenceBeanId;
	}

	public void setReferenceBeanId(String referenceBeanId) {
		this.referenceBeanId = referenceBeanId;
	}
}
