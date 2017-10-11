package com.bstek.bdf2.core.service;

import java.util.List;

import com.bstek.bdf2.core.business.IPosition;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @since 2013-1-24
 * @author Jacky.gao
 */
public interface IPositionService {
	public static final String BEAN_ID = "bdf2.positionService";

	IPosition newPositionInstance(String positionId);

	List<IPosition> loadUserPositions(String username);
	void loadUserNotAssignPositions(Page<IPosition> page, String companyId, String username,Criteria criteria);

	IPosition loadPositionById(String positionId);

	/**
	 * 分页加载岗位数据
	 * 
	 * @param page
	 *            Dorado7分页对象，其中包含pageNo,pageSize，分页后的数据也填充到这个page对象当中，该参数不可为空
	 * @param companyId
	 *            要加载哪个companyId下的岗位信息，该参数不可为空
	 * @param criteria
	 *            Dorado7条件对象，可从中取到相应的条件值，该参数可为空
	 */
	void loadPagePositions(Page<IPosition> page, String companyId,
			Criteria criteria);

	/**
	 * 删除用户岗位关系表中岗位记录
	 * 
	 * @param positionId
	 */
	void deleteUserPosition(String positionId);
	
	/**
	 * 删除用户岗位关系表中用户记录
	 * 
	 * @param userName
	 */
	void deleteUserPositionByUser(String userName);
}
