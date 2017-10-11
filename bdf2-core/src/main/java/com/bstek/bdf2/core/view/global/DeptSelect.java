package com.bstek.bdf2.core.view.global;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.dorado.annotation.DataProvider;

public class DeptSelect extends CoreJdbcDao{
	private IDeptService deptService;
	
	@DataProvider
	public List<IDept> loadDepts(String parentId) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		return deptService.loadDeptsByParentId(parentId,companyId);
	}

	public IDeptService getDeptService() {
		return deptService;
	}

	public void setDeptService(IDeptService deptService) {
		this.deptService = deptService;
	}
	
}
