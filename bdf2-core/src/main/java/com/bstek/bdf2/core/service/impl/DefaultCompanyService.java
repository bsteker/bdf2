package com.bstek.bdf2.core.service.impl;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.service.ICompanyService;


/**
 * @author Jacky.gao
 * @since 2013-2-25
 */
public class DefaultCompanyService extends CoreJdbcDao implements ICompanyService {

	public void registerCompany(String id, String name, String desc) {
		String sql="INSERT INTO BDF2_COMPANY(ID_,NAME_,DESC_) VALUES(?,?,?)";
		this.getJdbcTemplate().update(sql, new Object[]{id,name,desc});
	}
}
