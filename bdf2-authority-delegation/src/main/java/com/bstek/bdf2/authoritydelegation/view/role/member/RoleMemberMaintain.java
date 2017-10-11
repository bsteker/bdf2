package com.bstek.bdf2.authoritydelegation.view.role.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.authoritydelegation.model.ResourceAllocation;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.RoleMember;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.bdf2.core.view.role.RoleMaintain;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;

/**
 * @author lucas.yue@bstek.com
 * 
 *         2013-4-20 下午11:31:52
 */
@Component("bdf2.roleMemberMaintainAd")
public class RoleMemberMaintain extends RoleMaintain {
	private IDeptService deptService;

	private IPositionService positionService;

	private IUserService userService;

	@DataProvider
	public void loadMembers(Page<RoleMember> page, Map<String, Object> parameter)
			throws Exception {
		IUser user = getLoginUser();
		String hql = "from " + RoleMember.class.getName()
				+ " rm where rm.roleId=:roleId and";
		Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
		String type = (String) parameter.get("type");
		if (type.equals("user")) {
			hql += " rm.username is not null and rm.username in ";
			valueMap.put("resourceType", ResourceAllocation.ResourceType.E.name());
		} else if (type.equals("dept")) {
			hql += " rm.deptId is not null and rm.deptId in ";
			valueMap.put("resourceType", ResourceAllocation.ResourceType.D.name());
		} else if (type.equals("position")) {
			hql += " rm.positionId is not null and rm.positionId in ";
			valueMap.put("resourceType", ResourceAllocation.ResourceType.P.name());
		} else if (type.equals("group")) {
			hql += " rm.group is not null and rm.group in ";
			valueMap.put("resourceType", ResourceAllocation.ResourceType.G.name());
		}
		String scopeHql = "(select resourceId from "
				+ ResourceAllocation.class.getName()
				+ " where resourceOwner=:resourceOwner and resourceType=:resourceType)";
		hql += scopeHql;
		String countHql = "select count(*) " + hql;
		hql += " order by rm.createDate desc";
		valueMap.put("roleId", parameter.get("roleId"));
		valueMap.put("resourceOwner", user.getUsername());
		this.pagingQuery(page, hql, countHql, valueMap);
		for (RoleMember member : page.getEntities()) {
			if (StringUtils.isNotEmpty(member.getUsername())) {
				userService = ContextHolder.getBean(IUserService.BEAN_ID);
				member.setUser((IUser) userService.loadUserByUsername(member
						.getUsername()));
			}
			if (StringUtils.isNotEmpty(member.getDeptId())) {
				deptService = ContextHolder.getBean(IDeptService.BEAN_ID);
				member.setDept(deptService.loadDeptById(member.getDeptId()));
			}
			if (StringUtils.isNotEmpty(member.getPositionId())) {
				positionService = ContextHolder
						.getBean(IPositionService.BEAN_ID);
				member.setPosition(positionService.loadPositionById(member
						.getPositionId()));
			}
		}
	}

	@Expose
	public void changeGranted(Collection<Map<String, Object>> members)
			throws Exception {
		getLoginUser();
		Session session = this.getSessionFactory().openSession();
		try {
			for (Map<String, Object> map : members) {
				RoleMember rm = (RoleMember) session.load(RoleMember.class,
						(String) map.get("memberId"));
				rm.setGranted((Boolean) map.get("granted"));
				session.update(rm);
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	@Expose
	public void deleteRoleMember(String memberId) throws Exception {
		getLoginUser();
		Session session = this.getSessionFactory().openSession();
		try {
			RoleMember rm = new RoleMember();
			rm.setId(memberId);
			session.delete(rm);
		} finally {
			session.flush();
			session.close();
		}
	}

	@Expose
	public String insertRoleMember(Collection<String> ids, String type,
			String roleId) throws Exception {
		getLoginUser();
		String error = null;
		List<RoleMember> members = new ArrayList<RoleMember>();
		for (String id : ids) {
			RoleMember roleMember = new RoleMember();
			roleMember.setId(UUID.randomUUID().toString());
			roleMember.setRoleId(roleId);
			roleMember.setGranted(true);
			roleMember.setCreateDate(new Date());
			int count = 0;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleId", roleId);
			if (type.equals("user")) {
				map.put("username", id);
				count = this
						.queryForInt(
								"select count(*) from "
										+ RoleMember.class.getName()
										+ " rm where rm.roleId=:roleId and rm.username=:username",
								map);
				roleMember.setUsername(id);
			} else if (type.equals("dept")) {
				map.put("deptId", id);
				count = this.queryForInt("select count(*) from "
						+ RoleMember.class.getName()
						+ " rm where rm.roleId=:roleId and rm.deptId=:deptId",
						map);
				roleMember.setDeptId(id);
			} else if (type.equals("position")) {
				map.put("positionId", id);
				count = this
						.queryForInt(
								"select count(*) from "
										+ RoleMember.class.getName()
										+ " rm where rm.roleId=:roleId and rm.positionId=:positionId",
								map);
				roleMember.setPositionId(id);
			} else if (type.equals("group")) {
				map.put("groupId", id);
				count = this
						.queryForInt(
								"select count(*) from "
										+ RoleMember.class.getName()
										+ " rm where rm.roleId=:roleId and rm.group.id=:groupId",
								map);
				Group group = new Group();
				group.setId(id);
				roleMember.setGroup(group);
			}
			if (count > 0) {
				error = id;
				break;
			} else {
				members.add(roleMember);
			}
		}
		if (error == null) {
			Session session = this.getSessionFactory().openSession();
			try {
				for (RoleMember roleMember : members) {
					session.save(roleMember);
				}
			} finally {
				session.flush();
				session.close();
			}
		}
		return error;
	}

	public IDeptService getDeptService() {
		return deptService;
	}

	public void setDeptService(IDeptService deptService) {
		this.deptService = deptService;
	}

	public IPositionService getPositionService() {
		return positionService;
	}

	public void setPositionService(IPositionService positionService) {
		this.positionService = positionService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	private IUser getLoginUser() {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new NoneLoginException("please login first");
		}
		return user;
	}
}
