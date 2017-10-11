package com.bstek.bdf2.authoritydelegation.service;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;

import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.Url;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author lucas.yue@bstek.com
 * 
 *         2013-4-17 下午11:09:51
 */
public interface IAuthoritydelegationService {
	public final static String BEAN_ID_ = "bdf2.authoritydelegationService";

	/**
	 * 查询下放给用户的可管理的URL资源，超级管理员Or分层管理员
	 */
	public Collection<Url> findControllableUrls(String owner);

	/**
	 * 查询下放给用户的可管理的URL资源，超级管理员Or分层管理员
	 */
	public Collection<Url> findControllableUrls(String parentId, String owner);

	/**
	 * 分页查询下放给用户的可管理的用户资源，超级管理员Or分层管理员
	 */
	public void findControllableUsers(Page<UserDetails> page, String owner,
			Criteria criteria);

	/**
	 * 查询下放给用户的可管理的部门资源，超级管理员Or分层管理员
	 */
	public void findControllableDepts(Page<IDept> page, String owner,
			Criteria criteria);

	/**
	 * 查询下放给用户的可管理的部门资源，超级管理员Or分层管理员
	 * 
	 * @param parentId
	 * @param owner
	 * @return
	 */
	public Collection<IDept> findControllableDepts(String parentId, String owner);

	/**
	 * 查询下放给用户的可管理的群组资源，超级管理员Or分层管理员
	 */
	public void findControllableGroups(Page<Group> page, String owner,
			Criteria criteria);

	/**
	 * 分页查询下放给用户的可管理的职位资源，超级管理员Or分层管理员
	 */
	public void findControllablePositions(Page<IPosition> page, String owner,
			Criteria criteria);
}
