/**
 * 
 */
/**
 * @author lucas.yue@bstek.com
 *
 * 2013-4-17 下午11:08:24
 */
package com.bstek.bdf2.authoritydelegation.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bstek.bdf2.authoritydelegation.AuthoritydelegationHibernateDao;
import com.bstek.bdf2.authoritydelegation.model.ResourceAllocation;
import com.bstek.bdf2.authoritydelegation.service.IAuthoritydelegationService;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Service(IAuthoritydelegationService.BEAN_ID_)
public class AuthoritydelegationServiceImpl extends
		AuthoritydelegationHibernateDao implements IAuthoritydelegationService {

	public Collection<Url> findControllableUrls(String owner) {
		Page<Url> page = new Page<Url>(65535, 1);
		findControllableResources(ResourceAllocation.ResourceType.U, page,
				owner, null);
		return page.getEntities();
	}

	public Collection<Url> findControllableUrls(String parentId, String owner) {
		Collection<Url> urls = this.findControllableUrls(owner);
		if (StringUtils.isNotEmpty(parentId)) {
			Collection<Url> children = new ArrayList<Url>();
			for (Url url : urls) {
				if (parentId.equals(url.getParentId())) {
					children.add(url);
				}
			}
			return children;
		} else {
			Collection<Url> topUrl = new ArrayList<Url>();
			for (Url url : urls) {
				if (!StringUtils.isNotEmpty(url.getParentId())) {
					topUrl.add(url);
				}
			}
			return topUrl;
		}
	}

	public void findControllableUsers(Page<UserDetails> page, String owner,
			Criteria criteria) {
		findControllableResources(ResourceAllocation.ResourceType.E, page,
				owner, criteria);
	}

	public void findControllableDepts(Page<IDept> page, String owner,
			Criteria criteria) {
		findControllableResources(ResourceAllocation.ResourceType.D, page,
				owner, criteria);
	}

	public Collection<IDept> findControllableDepts(String parentId, String owner) {
		Page<IDept> page = new Page<IDept>(65535, 1);
		findControllableResources(ResourceAllocation.ResourceType.D, page,
				owner, null);
		Collection<IDept> deptList = page.getEntities();
		Map<String, IDept> deptMap = new HashMap<String, IDept>();
		for (IDept d : deptList) {
			deptMap.put(d.getId(), d);
		}
		if (deptList.size() > 0) {
			if (!StringUtils.isNotEmpty(parentId)) {
				List<IDept> topDept = new ArrayList<IDept>();
				for (IDept d : deptList) {
					if (!StringUtils.isNotEmpty(d.getParentId())) {
						topDept.add(d);
					} else if (deptMap.get(d.getParentId()) == null) {
						topDept.add(d);// 只下发了子部门
					}
				}
				return topDept;
			} else {
				List<IDept> children = new ArrayList<IDept>();
				for (IDept d : deptList) {
					if (parentId.equals(d.getParent())) {
						children.add(d);
					}
				}
				return children;
			}
		}
		return page.getEntities();
	}

	public void findControllableGroups(Page<Group> page, String owner,
			Criteria criteria) {
		findControllableResources(ResourceAllocation.ResourceType.G, page,
				owner, criteria);
	}

	public void findControllablePositions(Page<IPosition> page, String owner,
			Criteria criteria) {
		findControllableResources(ResourceAllocation.ResourceType.P, page,
				owner, criteria);
	}

	@SuppressWarnings("unchecked")
	private <T> void findControllableResources(
			ResourceAllocation.ResourceType resourceType, Page<T> page,
			String owner, Criteria criteria) {
		StringBuffer queryString = new StringBuffer(
				"select resourceId from "
						+ ResourceAllocation.class.getName()
						+ " where resourceOwner=:resourceOwner and resourceType=:resourceType");
		StringBuffer countQueryString = new StringBuffer(
				"select count(*) from "
						+ ResourceAllocation.class.getName()
						+ " where resourceOwner=:resourceOwner and resourceType=:resourceType");
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		Page<String> page1 = new Page<String>(page.getPageSize(),
				page.getPageNo());
		parametersMap.put("resourceOwner", owner);
		if (ResourceAllocation.ResourceType.E.equals(resourceType)) {
			parametersMap.put("resourceType",
					ResourceAllocation.ResourceType.E.name());
			try {
				this.pagingQuery(page1, queryString.toString(),
						countQueryString.toString(), parametersMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<UserDetails> userList = new ArrayList<UserDetails>();
			IUserService userService = ContextHolder
					.getBean(IUserService.BEAN_ID);
			for (String user : page1.getEntities()) {
				userList.add(userService.loadUserByUsername(user));
			}
			page.setEntities((Collection<T>) userList);
			page.setEntityCount(page1.getEntityCount());
		} else if (ResourceAllocation.ResourceType.P.equals(resourceType)) {
			parametersMap.put("resourceType",
					ResourceAllocation.ResourceType.P.name());
			try {
				this.pagingQuery(page1, queryString.toString(),
						countQueryString.toString(), parametersMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Collection<IPosition> pList = new ArrayList<IPosition>();
			IPositionService positionService = ContextHolder
					.getBean(IPositionService.BEAN_ID);
			for (String positionId : page1.getEntities()) {
				IPosition p = positionService.loadPositionById(positionId);
				pList.add(p);
			}
			page.setEntities((Collection<T>) pList);
			page.setEntityCount(page1.getEntityCount());
		} else if (ResourceAllocation.ResourceType.U.equals(resourceType)) {
			parametersMap.put("resourceType",
					ResourceAllocation.ResourceType.U.name());
			List<String> urlIdList = this.query(queryString.toString(),
					parametersMap);
			if (urlIdList.size() > 0) {
				String queryUrl = "from " + Url.class.getName()
						+ " where id in(:idList) order by order asc";
				String queryCountUrl = "select count(*) from "
						+ Url.class.getName() + " where id in(:idList)";
				try {
					Map<String, Object> urlIdMap = new HashMap<String, Object>();
					urlIdMap.put("idList", urlIdList);
					this.pagingQuery(page, queryUrl, queryCountUrl, urlIdMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (ResourceAllocation.ResourceType.G.equals(resourceType)) {
			parametersMap.put("resourceType",
					ResourceAllocation.ResourceType.G.name());
			List<String> groupIdList = this.query(queryString.toString(),
					parametersMap);
			if (groupIdList.size() > 0) {
				String queryUrl = "from " + Group.class.getName()
						+ " where id in(:idList)";
				String queryCountUrl = "select count(*) from "
						+ Group.class.getName() + " where id in(:idList)";
				try {
					Map<String, Object> groupIdParamsMap = new HashMap<String, Object>();
					groupIdParamsMap.put("idList", groupIdList);
					this.pagingQuery(page, queryUrl, queryCountUrl,
							groupIdParamsMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (ResourceAllocation.ResourceType.D.equals(resourceType)) {
			parametersMap.put("resourceType",
					ResourceAllocation.ResourceType.D.name());
			try {
				this.pagingQuery(page1, queryString.toString(),
						countQueryString.toString(), parametersMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<IDept> deptList = new ArrayList<IDept>();
			IDeptService deptService = ContextHolder
					.getBean(IDeptService.BEAN_ID);
			for (String deptId : page1.getEntities()) {
				IDept dept = deptService.loadDeptById(deptId);
				deptList.add(dept);
			}
			page.setEntities((Collection<T>) deptList);
			page.setEntityCount(page1.getEntityCount());
		}
	}

}