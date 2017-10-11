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
@XmlRootElement(name="DataResponse",namespace=Namespace.NS)
@XmlAccessorType(XmlAccessType.FIELD)
public class DataResponse {
	@XmlElement(namespace=Namespace.NS)
	private boolean successful;
	@XmlElement(namespace=Namespace.NS)
	private String returnValue;

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
}
