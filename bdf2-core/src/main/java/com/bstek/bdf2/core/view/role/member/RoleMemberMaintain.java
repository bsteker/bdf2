package com.bstek.bdf2.core.view.role.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.RoleMember;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.bdf2.core.view.role.RoleMaintain;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;

public class RoleMemberMaintain extends RoleMaintain{
	private IDeptService deptService;
	
	private IPositionService positionService;
	
	private IUserService userService;
	@DataProvider
	public void loadMembers(Page<RoleMember> page,Map<String,Object> parameter) throws Exception{
		String hql="from "+RoleMember.class.getName()+" rm where rm.roleId=:roleId and";
		Map<String,Object> valueMap=new LinkedHashMap<String,Object>();
		String type=(String)parameter.get("type");
		if(type.equals("user")){
			hql+=" rm.username is not null";
		}else if(type.equals("dept")){
			hql+=" rm.deptId is not null";
		}else if(type.equals("position")){
			hql+=" rm.positionId is not null";
		}else if(type.equals("group")){
			hql+=" rm.group is not null";
		}
		String countHql="select count(*) "+hql;
		hql+=" order by rm.createDate desc";
		valueMap.put("roleId",parameter.get("roleId"));		
		this.pagingQuery(page,hql,countHql,valueMap);
		for(RoleMember member:page.getEntities()){
			if(StringUtils.isNotEmpty(member.getUsername())){
				member.setUser((IUser)userService.loadUserByUsername(member.getUsername()));
			}
			if(StringUtils.isNotEmpty(member.getDeptId())){
				member.setDept(deptService.loadDeptById(member.getDeptId()));
			}
			if(StringUtils.isNotEmpty(member.getPositionId())){
				member.setPosition(positionService.loadPositionById(member.getPositionId()));
			}
		}
	}
	
	@Expose
	public void changeGranted(Collection<Map<String,Object>> members) throws Exception{
		Session session=this.getSessionFactory().openSession();
		try{
			for(Map<String,Object> map:members){
				RoleMember rm=(RoleMember)session.load(RoleMember.class,(String)map.get("memberId"));
				rm.setGranted((Boolean)map.get("granted"));
				session.update(rm);
			}			
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public void deleteRoleMember(String memberId) throws Exception{
		RoleMember rm=new RoleMember();
		rm.setId(memberId);
		Session session=this.getSessionFactory().openSession();
		try{
			session.delete(rm);
		}finally{
			session.flush();
			session.close();
		}
	}
	@Expose
	public String insertRoleMember(Collection<String> ids,String type,String roleId)throws Exception{
		Session session=this.getSessionFactory().openSession();
		try{
			String error=null;
			String sql = "delete from " + RoleMember.class.getName()+" rm where rm.roleId=?";  
		    Query query = session.createQuery(sql);  
		    query.setString(0, roleId);  
		    query.executeUpdate();  
		    
			List<RoleMember> members=new ArrayList<RoleMember>();
			for(String id:ids){
				RoleMember roleMember=new RoleMember();
				roleMember.setId(UUID.randomUUID().toString());
				roleMember.setRoleId(roleId);
				roleMember.setGranted(true);
				roleMember.setCreateDate(new Date());
				int count=0;
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("roleId",roleId);
				if(type.equals("user")){
					map.put("username",id);
					count=this.queryForInt("select count(*) from "+RoleMember.class.getName()+" rm where rm.roleId=:roleId and rm.username=:username",map);
					roleMember.setUsername(id);
				}else if(type.equals("dept")){
					map.put("deptId",id);
					count=this.queryForInt("select count(*) from "+RoleMember.class.getName()+" rm where rm.roleId=:roleId and rm.deptId=:deptId",map);
					roleMember.setDeptId(id);
				}else if(type.equals("position")){
					map.put("positionId",id);
					count=this.queryForInt("select count(*) from "+RoleMember.class.getName()+" rm where rm.roleId=:roleId and rm.positionId=:positionId",map);
					roleMember.setPositionId(id);
				}else if(type.equals("group")){
					map.put("groupId",id);
					count=this.queryForInt("select count(*) from "+RoleMember.class.getName()+" rm where rm.roleId=:roleId and rm.group.id=:groupId",map);
					Group group=new Group();
					group.setId(id);
					roleMember.setGroup(group);
				}
				if(count>0){
					error=id;
					//break;
				}else{
					members.add(roleMember);
				}
			}
//			if(error==null){
				for(RoleMember roleMember:members){
					session.save(roleMember);								
				}
//			}
			return null;
		}finally{
			session.flush();
			session.close();
		}
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
}
