package com.bstek.bdf2.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.Role;
import com.bstek.bdf2.core.model.RoleMember;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.bdf2.core.service.IRoleService;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.bdf2.core.service.MemberType;

/**
 * @since 2013-1-24
 * @author Jacky.gao
 */
public class DefaultRoleService extends CoreJdbcDao implements IRoleService {
	private IUserService userService;
	private IDeptService deptService;
	private IPositionService positionService;
	
	public List<Role> loadAllRoles() {
		String sql = "SELECT R.ID_,R.COMPANY_ID_,R.ENABLED_ FROM BDF2_ROLE R";
		if(StringUtils.isNotEmpty(this.getFixedCompanyId())){
			sql+=" where R.COMPANY_ID_=?";
			return this.getJdbcTemplate().query(sql,new Object[]{this.getFixedCompanyId()},new RoleRowMapper());
		}else{
			return this.getJdbcTemplate().query(sql, new RoleRowMapper());
		}
	}

	class RoleRowMapper implements RowMapper<Role>{
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role=new Role();
			role.setId(rs.getString("ID_"));
			role.setCompanyId(rs.getString("COMPANY_ID_"));
			if(rs.getObject("ENABLED_")!=null){
				role.setEnabled(rs.getBoolean("ENABLED_"));
			}
			return role;
		}
	}
	
	public List<RoleMember> loadRoleMemberByRoleId(String roleId) {
		String sql="SELECT M.ID_,M.USERNAME_,M.DEPT_ID_,M.POSITION_ID_,M.GROUP_ID_,M.GRANTED_ FROM BDF2_ROLE_MEMBER M WHERE M.ROLE_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{roleId},new RowMapper<RoleMember>(){
			public RoleMember mapRow(ResultSet rs, int rowNum) throws SQLException {
				RoleMember member=new RoleMember();
				member.setId(rs.getString("ID_"));
				member.setGranted(rs.getBoolean("GRANTED_"));
				String username=rs.getString("USERNAME_");
				if(StringUtils.isNotEmpty(username)){
					member.setUser(userService.newUserInstance(username));					
				}
				String deptId=rs.getString("DEPT_ID_");
				if(StringUtils.isNotEmpty(deptId)){
					member.setDept(deptService.newDeptInstance(deptId));					
				}
				String positionId=rs.getString("POSITION_ID_");
				if(StringUtils.isNotEmpty(positionId)){
					member.setPosition(positionService.newPositionInstance(positionId));					
				}
				String groupId=rs.getString("GROUP_ID_");
				if(StringUtils.isNotEmpty(groupId)){
					member.setGroup(buildGroup(groupId));					
				}
				return member;
			}
		});
	}
	
	private Group buildGroup(String groupId){
		Group g=new Group(groupId);
		String sql="SELECT DEPT_ID_,POSITION_ID_,USERNAME_ FROM BDF2_GROUP_MEMBER WHERE GROUP_ID_=?";
		List<Map<String,Object>> list=this.getJdbcTemplate().queryForList(sql,new Object[]{groupId});
		List<IDept> depts=new ArrayList<IDept>();
		List<IPosition> positions=new ArrayList<IPosition>();
		List<IUser> users=new ArrayList<IUser>();
		for(Map<String,Object> map:list){
			String deptId=(String)map.get("DEPT_ID_");
			String positionId=(String)map.get("POSITION_ID_");
			String username=(String)map.get("USERNAME_");
			if(StringUtils.isNotEmpty(deptId)){
				depts.add(deptService.newDeptInstance(deptId));
			}
			if(StringUtils.isNotEmpty(positionId)){
				positions.add(positionService.newPositionInstance(positionId));
			}
			if(StringUtils.isNotEmpty(username)){
				users.add(userService.newUserInstance(username));					
			}
		}
		if(depts.size()>0){
			g.setDepts(depts);
		}
		if(positions.size()>0){
			g.setPositions(positions);
		}
		if(users.size()>0){
			g.setUsers(users);
		}
		return g;
	}
	public void deleteRoleMemeber(String memeberId, MemberType type) {
		String sql="DELETE FROM BDF2_ROLE_MEMBER WHERE ";
		if(type.equals(MemberType.User)){
			sql+=" USERNAME_=?";
		}else if(type.equals(MemberType.Dept)){
			sql+=" DEPT_ID_=?";
		}else if(type.equals(MemberType.Position)){
			sql+=" POSITION_ID_=?";
		}else if(type.equals(MemberType.Group)){
			sql+=" GROUP_ID_=?";
		}else{
			throw new IllegalArgumentException("Unsupport MemberType ["+type+"]");
		}
		this.getJdbcTemplate().update(sql,new Object[]{memeberId});
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setDeptService(IDeptService deptService) {
		this.deptService = deptService;
	}


	public void setPositionService(IPositionService positionService) {
		this.positionService = positionService;
	}

}
