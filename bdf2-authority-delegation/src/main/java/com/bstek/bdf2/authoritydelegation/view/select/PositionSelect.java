package com.bstek.bdf2.authoritydelegation.view.select;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.authoritydelegation.AuthoritydelegationHibernateDao;
import com.bstek.bdf2.authoritydelegation.service.IAuthoritydelegationService;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author lucas.yue@bstek.com
 * 
 *         2013-4-20 下午11:25:53
 */
@Component("bdf2.positionSelectAd")
public class PositionSelect extends AuthoritydelegationHibernateDao {
	private IPositionService positionService;

	@DataProvider
	public void loadPositions(Page<IPosition> page, Criteria criteria)
			throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new RuntimeException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		if (user.isAdministrator()) {
			positionService = ContextHolder.getBean(IPositionService.BEAN_ID);
			positionService.loadPagePositions(page, companyId,
					criteria);
		} else {
			IAuthoritydelegationService ad = ContextHolder
					.getBean(IAuthoritydelegationService.BEAN_ID_);
			ad.findControllablePositions(page, user.getUsername(), criteria);
		}
	}

}
