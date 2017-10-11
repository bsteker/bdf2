package com.bstek.bdf2.core.view.global;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-2-17
 */
public class PositionSelect extends CoreJdbcDao{
	private IPositionService positionService;
	@DataProvider
	public void loadPositions(Page<IPosition> page,Criteria criteria) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}

		positionService.loadPagePositions(page,companyId, criteria);
	}
	
	public void loadUserNotAssignPositions(Page<IPosition> page, String username, Criteria criteria) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}

		positionService.loadUserNotAssignPositions(page,companyId, username, criteria);
	}
	
	@DataProvider
	public Collection<IPosition> loadPositionByUsername(String username) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}

		return positionService.loadUserPositions(username);
	}
	
	public IPositionService getPositionService() {
		return positionService;
	}
	public void setPositionService(IPositionService positionService) {
		this.positionService = positionService;
	}
	
}
