package com.bstek.bdf2.core.service;

import java.util.List;

import com.bstek.bdf2.core.model.Role;
import com.bstek.bdf2.core.model.RoleMember;

/**
 * @author Jacky.gao
 * @since 2013-2-25
 */
public interface IRoleService {
	List<Role> loadAllRoles();
	List<RoleMember> loadRoleMemberByRoleId(String roleId);
	void deleteRoleMemeber(String memeberId,MemberType type);
}
