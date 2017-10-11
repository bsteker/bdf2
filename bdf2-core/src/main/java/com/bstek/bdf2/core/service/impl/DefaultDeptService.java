package com.bstek.bdf2.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.model.DefaultDept;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.MemberType;

/**
 * @since 2013-1-24
 * @author Jacky.gao
 */
public class DefaultDeptService extends CoreJdbcDao implements IDeptService {
	public IDept newDeptInstance(String deptId) {
		return new DefaultDept(deptId);
	}
	public List<IDept> loadUserDepts(String username) {
		String sql="SELECT D.ID_,D.NAME_,D.DESC_,D.COMPANY_ID_,D.PARENT_ID_ FROM BDF2_USER_DEPT UD INNER JOIN BDF2_DEPT D ON UD.DEPT_ID_=D.ID_ WHERE UD.USERNAME_=?";
		List<IDept> depts=this.getJdbcTemplate().query(sql,new Object[]{username},new DefaultDeptRowMapper());
		for(IDept dept:depts){
			this.buildParentDept(dept);
		}
		return depts;
	}
	
	public void deleteUserDept(String username){
		String sql="DELETE FROM BDF2_USER_DEPT WHERE USERNAME_=?";
		this.getJdbcTemplate().update(sql,new Object[]{username});
	}
	
	public IDept loadDeptById(String deptId) {
		String sql="SELECT D.ID_,D.NAME_,D.DESC_,D.COMPANY_ID_,D.PARENT_ID_ FROM BDF2_DEPT D WHERE D.ID_=?";
		List<IDept> depts=this.getJdbcTemplate().query(sql,new Object[]{deptId},new DefaultDeptRowMapper());
		if(depts.size()==0){
			return null;			
		}else{
			return depts.get(0);
		}
	}
	private void buildParentDept(IDept dept){
		DefaultDept defaultDept=(DefaultDept)dept;
		if(StringUtils.isNotEmpty(defaultDept.getParentId())){
			IDept parentDept=this.loadDeptById(defaultDept.getParentId());
			defaultDept.setParent(parentDept);
			this.buildParentDept(parentDept);
		}
	}
	public List<IDept> loadDeptsByParentId(String parentId,String companyId) {
		String sql="SELECT D.ID_,D.NAME_,D.DESC_,D.COMPANY_ID_,D.PARENT_ID_ FROM BDF2_DEPT D WHERE D.COMPANY_ID_=?";
		if(parentId==null){
			sql+=" AND D.PARENT_ID_ IS NULL";
			return this.getJdbcTemplate().query(sql,new Object[]{companyId},new DefaultDeptRowMapper());
		}else{
			sql+=" AND D.PARENT_ID_=?";			
			return this.getJdbcTemplate().query(sql, new Object[]{companyId,parentId},new DefaultDeptRowMapper());
		}
	}
}

class DefaultDeptRowMapper implements RowMapper<IDept>{
	public IDept mapRow(ResultSet rs, int rowNum) throws SQLException {
		DefaultDept d=new DefaultDept();
		d.setId(rs.getString("ID_"));
		d.setName(rs.getString("NAME_"));
		d.setDesc(rs.getString("DESC_"));
		d.setCompanyId(rs.getString("COMPANY_ID_"));
		d.setParentId(rs.getString("PARENT_ID_"));
		return d;
	}
}
