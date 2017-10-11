package com.bstek.bdf2.authoritydelegation.view.select;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.authoritydelegation.AuthoritydelegationHibernateDao;
import com.bstek.bdf2.authoritydelegation.service.IAuthoritydelegationService;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.dorado.annotation.DataProvider;

/**
 * @author lucas.yue@bstek.com
 * 
 *         2013-4-20 下午11:26:24
 */
@Component("bdf2.deptSelectAd")
public class DeptSelect extends AuthoritydelegationHibernateDao {
	private IDeptService deptService;

	@DataProvider
	public Collection<IDept> loadDepts(String parentId) throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new RuntimeException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		if (user.isAdministrator()) {
			deptService = ContextHolder.getBean(IDeptService.BEAN_ID);
			return deptService.loadDeptsByParentId(parentId,
					companyId);
		} else {
			IAuthoritydelegationService ad = ContextHolder
					.getBean(IAuthoritydelegationService.BEAN_ID_);
			return ad.findControllableDepts(parentId, user.getUsername());
		}
	}

}
