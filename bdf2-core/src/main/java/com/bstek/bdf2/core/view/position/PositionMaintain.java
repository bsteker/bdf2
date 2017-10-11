package com.bstek.bdf2.core.view.position;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.DefaultPosition;
import com.bstek.bdf2.core.service.IGroupService;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.bdf2.core.service.IRoleService;
import com.bstek.bdf2.core.service.MemberType;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

public class PositionMaintain extends CoreHibernateDao {
	private IPositionService positionService;
	private IRoleService roleService;
	private IGroupService groupService;

	@DataProvider
	public void loadPostions(Page<IPosition> page, Criteria criteria) throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new NoneLoginException("Please login first");
		}
		String companyId = user.getCompanyId();
		if (StringUtils.isNotEmpty(getFixedCompanyId())) {
			companyId = getFixedCompanyId();
		}
		positionService.loadPagePositions(page, companyId, criteria);
	}

	@DataProvider
	public Collection<IPosition> loadPositionByUsername(String username) {
		return positionService.loadUserPositions(username);
	}

	@DataResolver
	public void savePostions(final Collection<DefaultPosition> positions) {
		Session session = this.getSessionFactory().openSession();
		try {
			IUser user = ContextHolder.getLoginUser();
			if (user == null) {
				throw new NoneLoginException("Please login first");
			}
			String companyId = user.getCompanyId();
			if (StringUtils.isNotEmpty(getFixedCompanyId())) {
				companyId = getFixedCompanyId();
			}
			for (DefaultPosition position : positions) {
				EntityState state = EntityUtils.getState(position);
				if (state.equals(EntityState.NEW)) {
					position.setCompanyId(companyId);
					position.setCreateDate(new Date());
					session.save(position);
				} else if (state.equals(EntityState.MODIFIED)) {
					session.update(position);
				} else if (state.equals(EntityState.DELETED)) {
					roleService.deleteRoleMemeber(position.getId(), MemberType.Position);
					groupService.deleteGroupMemeber(position.getId(), MemberType.Position);
					positionService.deleteUserPosition(position.getId());
					session.delete(position);
				}
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	@Expose
	public String uniqueCheck(String id) {
		String hql = "select count(*) from " + DefaultPosition.class.getName() + " d where d.id = :id";
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("id", id);
		if (this.queryForInt(hql, parameterMap) > 0) {
			return "岗位ID已存在！";
		} else {
			return null;
		}
	}

	public IRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public IGroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(IGroupService groupService) {
		this.groupService = groupService;
	}

	public IPositionService getPositionService() {
		return positionService;
	}

	public void setPositionService(IPositionService positionService) {
		this.positionService = positionService;
	}
}
