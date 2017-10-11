package com.bstek.bdf2.core.view.dept;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.DefaultDept;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IGroupService;
import com.bstek.bdf2.core.service.IRoleService;
import com.bstek.bdf2.core.service.MemberType;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;

/**
 * @since 2013-1-29
 * @author Jacky.gao
 */
public class DeptMaintain extends CoreHibernateDao {
	private IDeptService deptService;
	private IRoleService roleService;
	private IGroupService groupService;
	@DataProvider
	public List<IDept> loadDepts(String parentId) throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		return deptService.loadDeptsByParentId(parentId, companyId);
	}

	@DataResolver
	public void saveDepts(Collection<DefaultDept> depts) {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		Session session = this.getSessionFactory().openSession();
		try{
			for (DefaultDept defaultDept : depts) {
				EntityState state = EntityUtils.getState(defaultDept);
				
				if (state.equals(EntityState.NEW)) {
					defaultDept.setCompanyId(companyId);
					defaultDept.setCreateDate(new Date());
					session.save(defaultDept);
				}
				if (state.equals(EntityState.MODIFIED) || state.equals(EntityState.MOVED)) {
					session.update(defaultDept);
				}
				
				if (defaultDept.getChildren() != null) {
					DefaultDept[] defaultDepts = new DefaultDept[defaultDept.getChildren().size()];
					saveDepts(Arrays.asList(defaultDept.getChildren().toArray(defaultDepts)));
				}
				
				if (state.equals(EntityState.DELETED)) {
					if (countChildren(defaultDept.getId()) == 0) {
						roleService.deleteRoleMemeber(defaultDept.getId(), MemberType.Dept);
						groupService.deleteGroupMemeber(defaultDept.getId(), MemberType.Dept);
						session.delete(defaultDept);
					} else {
						throw new RuntimeException("请先删除子部门");
					}
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}

	@Expose
	public String uniqueCheck(String id){
		String hql = "select count(*) from " + DefaultDept.class.getName() + " d where d.id = :id";
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("id", id);
		if(this.queryForInt(hql, parameterMap)>0){
			return "部门ID已存在！";
		}else{
			return null;
		}
	}
	
	@Expose
	public int countChildren(String parentId) {
		String hql = "select count(*) from " + DefaultDept.class.getName() + " d where d.parentId = :parentId";
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parentId", parentId);
		return this.queryForInt(hql, parameterMap);
	}

	public IDeptService getDeptService() {
		return deptService;
	}

	public void setDeptService(IDeptService deptService) {
		this.deptService = deptService;
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
}
