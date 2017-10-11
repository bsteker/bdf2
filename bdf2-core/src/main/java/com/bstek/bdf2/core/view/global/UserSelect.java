package com.bstek.bdf2.core.view.global;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;

/**
 * @author Jacky.gao
 * @since 2013-2-5
 */
public class UserSelect extends CoreJdbcDao{
	private IUserService userService;
	
	@DataProvider
	public void loadUsers(Page<IUser> page,Criteria criteria) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		userService.loadPageUsers(page,companyId, criteria);
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
