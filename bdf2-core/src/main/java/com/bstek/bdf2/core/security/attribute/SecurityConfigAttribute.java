package com.bstek.bdf2.core.security.attribute;

import org.springframework.security.access.ConfigAttribute;

import com.bstek.bdf2.core.business.ICompany;
/**
 * @since 2013-1-23
 * @author Jacky.gao
 */
public class SecurityConfigAttribute implements ConfigAttribute,ICompany {
	private static final long serialVersionUID = 6738019563942071383L;
	private boolean granted;
	private Object member;
	private String companyId;
	private String attribute;
	private AttributeType attributeType;
	public SecurityConfigAttribute(AttributeType attributeType,boolean granted,String companyId){
		this.companyId=companyId;
		this.granted=granted;
		this.attributeType=attributeType;
	}
	
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public boolean isGranted() {
		return granted;
	}

	public Object getMember() {
		return member;
	}

	public void setMember(Object member) {
		this.member = member;
	}

	public String getCompanyId() {
		return companyId;
	}

	public AttributeType getAttributeType() {
		return attributeType;
	}
	
	public String toString(){
		return "member:"+member;
	}
}
