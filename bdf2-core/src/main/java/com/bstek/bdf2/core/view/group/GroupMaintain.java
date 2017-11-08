package com.bstek.bdf2.core.view.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.GroupMember;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IGroupService;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.bdf2.core.service.IRoleService;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.bdf2.core.service.MemberType;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
public class GroupMaintain extends CoreHibernateDao {
	private IUserService userService;
	private IDeptService deptService;
	private IPositionService positionService;
	private IRoleService roleService;
	private IGroupService groupService;
	@DataProvider
	public void loadGroups(Page<Group> page,Criteria criteria) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		String hql="from "+Group.class.getName()+" g where ";
		ParseResult result=this.parseCriteria(criteria,true,"g");
		Map<String,Object> map=null;
		if(result!=null){
			hql+=result.getAssemblySql().toString()+" and g.companyId=:companyId";
			map=result.getValueMap();
		}else{
			hql+="g.companyId=:companyId";
			map=new HashMap<String,Object>();
		}
		String countHql="select count(*) "+hql;
		hql+=" order by g.createDate desc";
		map.put("companyId",companyId);
		this.pagingQuery(page, hql,countHql, map);
	}
	
	@DataProvider
	public void loadGroupMembers(Page<GroupMember> page,String groupId,String type) throws Exception{
		String hql="from "+GroupMember.class.getName()+" gm where gm.groupId=:groupId";
		if(type.equals("user")){
			hql+=" and gm.username is not null";
		}else if(type.equals("dept")){
			hql+=" and gm.deptId is not null";
		}else if(type.equals("position")){
			hql+=" and gm.positionId is not null";
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("groupId", groupId);
		String countHql="select count(*) "+hql;
		this.pagingQuery(page, hql,countHql, map);
		for(GroupMember gm:page.getEntities()){
			if(StringUtils.isNotEmpty(gm.getUsername())){
				gm.setUser((IUser)userService.loadUserByUsername(gm.getUsername()));
			}
			if(StringUtils.isNotEmpty(gm.getDeptId())){
				gm.setDept(deptService.loadDeptById(gm.getDeptId()));
			}
			if(StringUtils.isNotEmpty(gm.getPositionId())){
				gm.setPosition(positionService.loadPositionById(gm.getPositionId()));
			}
		}
	}
	
	@DataResolver
	public void saveGroups(Collection<Group> groups) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		Session session=this.getSessionFactory().openSession();
		try{
			for(Group g:groups){
				EntityState state=EntityUtils.getState(g);
				if(state.equals(EntityState.NEW)){
					g.setId(UUID.randomUUID().toString());
					g.setCompanyId(companyId);
					g.setCreateDate(new Date());
					session.save(g);
				}else if(state.equals(EntityState.MODIFIED)){
					session.update(g);
				}else if(state.equals(EntityState.DELETED)){
					roleService.deleteRoleMemeber(g.getId(), MemberType.Group);
					groupService.deleteGroupMemeber(g.getId(), MemberType.Group);
					session.delete(g);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public void deleteMember(String memberId) throws Exception{
		GroupMember gm=new GroupMember();
		gm.setId(memberId);
		Session session=this.getSessionFactory().openSession();
		try{
			session.delete(gm);			
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public String insertGroupMembers(Collection<String> ids,String groupId,String type) throws Exception{
		String error=null;
		List<GroupMember> members=new ArrayList<GroupMember>();
		for(String id:ids){
			GroupMember gm=new GroupMember();
			gm.setId(UUID.randomUUID().toString());
			gm.setGroupId(groupId);
			String hql="select count(*) from "+GroupMember.class.getName()+" gm where gm.groupId=:groupId and ";
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("groupId", groupId);
			if(type.equals("user")){
				hql+="gm.username=:username";
				gm.setUsername(id);
				map.put("username",id);
			}else if(type.equals("dept")){
				hql+="gm.deptId=:deptId";
				map.put("deptId",id);
				gm.setDeptId(id);
			}else if(type.equals("position")){
				hql+="gm.positionId=:positionId";
				map.put("positionId",id);
				gm.setPositionId(id);
			}
			
			if(this.queryForInt(hql, map)==0){
				//error=id;
				//break;
				members.add(gm);
			}
		}
		if(error==null){
			Session session=this.getSessionFactory().openSession();
			try{
				for(GroupMember gm:members){
					session.save(gm);
				}
			}finally{
				session.flush();
				session.close();
			}
		}
		return error;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
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
