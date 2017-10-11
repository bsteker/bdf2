package com.bstek.bdf2.webservice.rpc.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bstek.bdf2.webservice.rpc.Namespace;

/**
 * @author Jacky.gao
 * @since 2013年7月12日
 */
@XmlRootElement(name="DataRequest",namespace=Namespace.NS)
@XmlAccessorType(XmlAccessType.FIELD)
public class DataRequest {
	@XmlElement(namespace=Namespace.NS)
	private String beanId;
	@XmlElement(namespace=Namespace.NS)
	private String methodName;
	@XmlElement(namespace=Namespace.NS)
	private String[] methodArgements;
	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String[] getMethodArgements() {
		return methodArgements;
	}
	public void setMethodArgements(String[] methodArgements) {
		this.methodArgements = methodArgements;
	}
}
