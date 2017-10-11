package com.bstek.bdf2.profile.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bstek.bdf2.profile.model.AssignTarget;
import com.bstek.bdf2.profile.model.UrlDefinition;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * 使用profile模块必须实现的接口
 * @author Jacky.gao
 * @since 2013-2-26
 */
public interface IProfileDataService {
	/**
	 * 根据给出的父ID，返回其下所有子的URL，实现URL树构建
	 * @param companyId 所在公司(或其它)的ID
	 * @param parentId 父URL ID
	 * @return  返回指定父ID的URL集合
	 */
	List<UrlDefinition> loadUrls(String companyId,String parentId);
	/**
	 * 分页加载分配资源的目标对象
	 * @param page Dorado7分页对象
	 * @param criteria 过滤查询对象
	 */
	void loadAssignTargets(Page<AssignTarget> page,Criteria criteria);
	
	/**
	 * 返回加载个性化信息需要使用的分配资源目标对象的ID，比如username,companyid之
	 * @return 一个字符串
	 */
	String getAssignTargetId(HttpServletRequest request);
}
