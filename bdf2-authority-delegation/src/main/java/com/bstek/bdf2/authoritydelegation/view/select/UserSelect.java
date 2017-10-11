package com.bstek.bdf2.authoritydelegation.view.select;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.authoritydelegation.AuthoritydelegationHibernateDao;
import com.bstek.bdf2.authoritydelegation.service.IAuthoritydelegationService;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author lucas.yue@bstek.com
 * 
 *         2013-4-20 下午11:26:02
 */
@Component("bdf2.userSelectAd")
public class UserSelect extends AuthoritydelegationHibernateDao {
	private IUserService userService;

	@SuppressWarnings("unchecked")
	@DataProvider
	public void loadUsers(Page<IUser> page, Criteria criteria) throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new RuntimeException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		if (user.isAdministrator()) {
			userService = ContextHolder.getBean(IUserService.BEAN_ID);
			userService.loadPageUsers(page,companyId, criteria);
		} else {
			IAuthoritydelegationService ad = ContextHolder
					.getBean(IAuthoritydelegationService.BEAN_ID_);
			Page<UserDetails> page1 = new Page<UserDetails>(page.getPageSize(),
					page.getPageNo());
			ad.findControllableUsers(page1, user.getUsername(), criteria);
			Collection<UserDetails> uList = page1.getEntities();
			if (uList.size() > 0) {
				Collection<?> userList = page1.getEntities();
				page.setEntities((Collection<IUser>) userList);
				page.setEntityCount(page1.getEntityCount());
			}
		}
	}

}
