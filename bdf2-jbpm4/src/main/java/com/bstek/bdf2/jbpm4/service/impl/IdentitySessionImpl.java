package com.bstek.bdf2.jbpm4.service.impl;

import java.util.Collections;
import java.util.List;

import org.jbpm.api.identity.Group;
import org.jbpm.api.identity.User;
import org.jbpm.pvm.internal.identity.impl.UserImpl;
import org.jbpm.pvm.internal.identity.spi.IdentitySession;

/**
 * @author Jacky.gao
 * @since 2013-3-27
 */
public class IdentitySessionImpl implements IdentitySession {

	public String createUser(String userId, String givenName,
			String familyName, String businessEmail) {
		return null;
	}

	public User findUserById(String userId) {
		return new UserImpl(userId,userId,userId);
	}

	public List<User> findUsersById(String... userIds) {
		return null;
	}

	public List<User> findUsers() {
		return null;
	}

	public void deleteUser(String userId) {

	}

	public String createGroup(String groupName, String groupType,
			String parentGroupId) {
		return null;
	}

	public List<User> findUsersByGroup(String groupId) {
		return null;
	}

	public Group findGroupById(String groupId) {
		return null;
	}

	public List<Group> findGroupsByUserAndGroupType(String userId,
			String groupType) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Group> findGroupsByUser(String userId) {
		return Collections.EMPTY_LIST;
	}

	public void deleteGroup(String groupId) {

	}

	public void createMembership(String userId, String groupId, String role) {

	}

	public void deleteMembership(String userId, String groupId, String role) {
	}

}
