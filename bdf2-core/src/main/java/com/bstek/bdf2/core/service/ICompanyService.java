package com.bstek.bdf2.core.service;

/**
 * @author Jacky.gao
 * @since 2013-2-25
 */
public interface ICompanyService {
	public static final String BEAN_ID="bdf2.companyService";
	void registerCompany(String id,String name,String desc);
}
