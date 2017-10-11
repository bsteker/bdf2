package com.bstek.bdf2.authoritydelegation.view.allocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.authoritydelegation.AuthoritydelegationHibernateDao;
import com.bstek.bdf2.authoritydelegation.model.ResourceAllocation;
import com.bstek.bdf2.authoritydelegation.model.ResourceOwner;
import com.bstek.bdf2.authoritydelegation.service.IAuthoritydelegationService;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.Url;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.entity.FilterType;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Component("bdf2.allocationMaintain")
public class AllocationMaintain extends AuthoritydelegationHibernateDao {
	@SuppressWarnings("unchecked")
	@DataProvider
	public Collection<ResourceOwner> findResourceOwners() {
		checkIsAdmin();
		// 查询分层管理员
		IUser loginUser = this.getLoginUser();
		String hql = "from " + ResourceOwner.class.getName()
				+ " where companyId=:companyId";
		return this.getSession().createQuery(hql)
				.setParameter("companyId", loginUser.getCompanyId()).list();
	}

	@DataResolver
	public void saveResourceOwners(Collection<ResourceOwner> resourceOwners) {
		checkIsAdmin();
		// 保存分层管理员
		Session session = this.getSessionFactory().openSession();
		try {
			for (Iterator<ResourceOwner> resOwnerIt = EntityUtils.getIterator(
					resourceOwners, FilterType.ALL, ResourceOwner.class); resOwnerIt
					.hasNext();) {
				ResourceOwner resOwner = resOwnerIt.next();
				EntityState resOwnerState = EntityUtils.getState(resOwner);
				if (EntityState.NEW.equals(resOwnerState)) {
					resOwner.setCreateDate(new Date());
					resOwner.setCreateUser(this.getLoginUser().getUsername());
					resOwner.setCompanyId(this.getLoginUser().getCompanyId());
					session.save(resOwner);
				} else if (EntityState.DELETED.equals(resOwnerState)) {
					this.getSession()
							.createQuery(
									"delete "
											+ ResourceAllocation.class
													.getName()
											+ " where resourceOwner=:resourceOwner")
							.setParameter("resourceOwner",
									resOwner.getUsername()).executeUpdate();
					session.delete(resOwner);
				}
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	private void checkIsAdmin() {
		if (!this.getLoginUser().isAdministrator()) {
			throw new RuntimeException("只有超级管理员才能进行下发资源管理");
		}
	}

	@Expose
	public List<String> saveAllocationResources(String owner, String resType,
			String addOrDel, Collection<String> resList) {
		checkIsAdmin();
		// 保存下发可管理的资源
		if (ResourceAllocation.ResourceType.contain(resType)) {
			Session session = this.getSessionFactory().openSession();
			try {
				if ("Add".equals(addOrDel)) {
					if (ResourceAllocation.ResourceType.U.name()
							.equals(resType)) {
						this.getSession()
								.createQuery(
										"delete "
												+ ResourceAllocation.class
														.getName()
												+ " where resourceOwner=:resourceOwner and resourceType=:resourceType")
								.setParameter("resourceOwner", owner)
								.setParameter("resourceType", resType)
								.executeUpdate();
					}
					List<String> exsitResIdList = this
							.getExsitResourceAllocations(session, owner,
									resType, resList);
					if (exsitResIdList.size() > 0) {
						return exsitResIdList;
					}
					ResourceAllocation resA = null;
					Date nowDate = new Date();
					String loginUser = this.getLoginUser().getUsername();
					for (String res : resList) {
						resA = new ResourceAllocation();
						resA.setCreateDate(nowDate);
						resA.setCreateUser(loginUser);
						resA.setId(UUID.randomUUID().toString());
						resA.setResourceId(res);
						resA.setResourceOwner(owner);
						resA.setResourceType(resType);
						session.save(resA);
					}
				} else if ("Del".equals(addOrDel)) {
					for (String res : resList) {
						ResourceAllocation resObj = (ResourceAllocation) this
								.getSession()
								.createCriteria(ResourceAllocation.class)
								.add(Restrictions.eq("resourceOwner", owner))
								.add(Restrictions.eq("resourceType", resType))
								.add(Restrictions.eq("resourceId", res))
								.uniqueResult();
						session.delete(resObj);
					}
				}
			} finally {
				session.flush();
				session.close();
			}
		}
		return null;
	}

	@DataProvider
	public void findAllocationUsers(Page<UserDetails> page, String owner,
			Criteria criteria) {
		checkIsAdmin();
		this.getAuthoritydelegationService().findControllableUsers(page, owner,
				criteria);
	}

	@DataProvider
	public void findAllocationDepts(Page<IDept> page, String owner,
			Criteria criteria) {
		checkIsAdmin();
		this.getAuthoritydelegationService().findControllableDepts(page, owner,
				criteria);
	}

	@DataProvider
	public void findAllocationPositions(Page<IPosition> page, String owner,
			Criteria criteria) {
		checkIsAdmin();
		this.getAuthoritydelegationService().findControllablePositions(page,
				owner, criteria);
	}

	@DataProvider
	public void findAllocationGroups(Page<Group> page, String owner,
			Criteria criteria) {
		checkIsAdmin();
		this.getAuthoritydelegationService().findControllableGroups(page,
				owner, criteria);
	}

	@SuppressWarnings("unchecked")
	@DataProvider
	public Collection<Url> findAllUrls(String username, String parentId) {
		checkIsAdmin();
		IUser user = this.getLoginUser();
		Collection<Url> conUrls = this.getAuthoritydelegationService()
				.findControllableUrls(username);
		Map<String, Url> conUrlMap = new HashMap<String, Url>();
		for (Url u : conUrls) {
			conUrlMap.put(u.getId(), u);
		}
		Collection<Url> allUrls = null;
		if (StringUtils.isNotEmpty(parentId)) {
			allUrls = this.getSession().createCriteria(Url.class)
					.add(Restrictions.eq("companyId", user.getCompanyId()))
					.add(Restrictions.eq("parentId", parentId))
					.addOrder(Order.asc("order")).list();
			for (Url u : allUrls) {
				if (conUrlMap.containsKey(u.getId())) {
					u.setUse(true);
				}
			}
			if (allUrls.size() == 0) {
				allUrls = null;
			}
		} else {
			allUrls = this.getSession().createCriteria(Url.class)
					.add(Restrictions.eq("companyId", user.getCompanyId()))
					.add(Restrictions.isNull("parentId"))
					.addOrder(Order.asc("order")).list();
			for (Url u : allUrls) {
				if (conUrlMap.containsKey(u.getId())) {
					u.setUse(true);
				}
			}
		}
		return allUrls;
	}

	private IUser getLoginUser() {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new NoneLoginException("please login first");
		}
		return user;
	}

	public IAuthoritydelegationService getAuthoritydelegationService() {
		return ContextHolder.getBean(IAuthoritydelegationService.BEAN_ID_);
	}

	@SuppressWarnings("unchecked")
	private List<String> getExsitResourceAllocations(Session session,
			String owner, String resType, Collection<String> resList) {
		List<String> existIdList = new ArrayList<String>();
		if (resList != null && resList.size() > 0) {
			Collection<ResourceAllocation> list = this.getSession()
					.createCriteria(ResourceAllocation.class)
					.add(Restrictions.eq("resourceOwner", owner))
					.add(Restrictions.eq("resourceType", resType))
					.add(Restrictions.in("resourceId", resList)).list();
			for (ResourceAllocation res : list) {
				existIdList.add(res.getResourceId());
			}
		}
		return existIdList;
	}
}
