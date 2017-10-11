package com.bstek.bdf2.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.service.IGroupService;
import com.bstek.bdf2.core.service.MemberType;

/**
 * @since 2013-1-30
 * @author Jacky.gao
 */
public class DefaultGroupService extends CoreJdbcDao implements IGroupService {

	public List<Group> loadUserGroups(String username) {
		String sql="SELECT G.ID_,G.NAME_,G.DESC_,G.COMPANY_ID_ FROM BDF2_GROUP_MEMBER GM INNER JOIN BDF2_GROUP G ON GM.GROUP_ID_ = G.ID_ WHERE GM.USERNAME_=?";
		return this.getJdbcTemplate().query(sql, new Object[]{username},new RowMapper<Group>(){
			public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
				Group g=new Group();
				g.setCompanyId(rs.getString("COMPANY_ID_"));
				g.setId(rs.getString("ID_"));
				g.setName(rs.getString("NAME_"));
				g.setDesc(rs.getString("DESC_"));
				return g;
			}
		});
	}
	
	public void deleteGroupMemeber(String memeberId, MemberType type) {
		String sql="DELETE FROM BDF2_GROUP_MEMBER WHERE ";
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
}
