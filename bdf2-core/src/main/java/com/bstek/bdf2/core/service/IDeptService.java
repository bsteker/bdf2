package com.bstek.bdf2.core.service;

import java.util.List;

import com.bstek.bdf2.core.business.IDept;

/**
 * @since 2013-1-24
 * @author Jacky.gao
 */
public interface IDeptService {
	public static final String BEAN_ID="bdf2.deptService";
	IDept newDeptInstance(String deptId);
	List<IDept> loadUserDepts(String username);
	IDept loadDeptById(String deptId);
	List<IDept> loadDeptsByParentId(String parentId,String companyId);
	void deleteUserDept(String username);
}
