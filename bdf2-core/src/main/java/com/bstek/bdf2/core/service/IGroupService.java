package com.bstek.bdf2.core.service;

import java.util.List;

import com.bstek.bdf2.core.model.Group;

/**
 * @since 2013-1-29
 * @author Jacky.gao
 */
public interface IGroupService {
	public static final String BEAN_ID="bdf2.groupService";
	List<Group> loadUserGroups(String username);
	void deleteGroupMemeber(String memeberId,MemberType type);
}