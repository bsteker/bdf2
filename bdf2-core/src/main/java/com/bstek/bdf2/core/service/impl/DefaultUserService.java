package com.bstek.bdf2.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.DefaultUser;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;

/**
 * @since 2013-1-18
 * @author Jacky.gao
 */
public class DefaultUserService extends CoreJdbcDao implements IUserService {
	private PasswordEncoder passwordEncoder;
	private static final ResourceManager resourceManager = ResourceManagerUtils
			.get(DefaultUserService.class);

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		String sql = "SELECT U.USERNAME_,U.CNAME_,U.ENAME_,U.ADMINISTRATOR_,U.BIRTHDAY_,U.COMPANY_ID_,U.EMAIL_,U.ENABLED_,U.MALE_,U.MOBILE_,U.PASSWORD_,U.SALT_,U.ADDRESS_ FROM BDF2_USER U WHERE U.USERNAME_=?";
		List<IUser> users = this.getJdbcTemplate().query(sql,
				new Object[] { username }, new DefaultUserRowMapper());
		if (users.size() == 0) {
			throw new UsernameNotFoundException("User " + username
					+ " is not exist");
		} else {
			DefaultUser user = (DefaultUser) users.get(0);
			return user;
		}
	}

	public void loadPageUsers(Page<IUser> page, String companyId,
			Criteria criteria) {
		SingleValueFilterCriterion roleCriterion = getRoleFilterCriterion(criteria);
		if (roleCriterion!=null){
			criteria.getCriterions().remove(roleCriterion);
		}
		SingleValueFilterCriterion groupCriterion = getGroupFilterCriterion(criteria);
		if (groupCriterion!=null){
			criteria.getCriterions().remove(groupCriterion);
		}
		String sql = "SELECT x.USERNAME_,x.CNAME_,x.ENAME_,x.ADMINISTRATOR_,x.BIRTHDAY_,x.COMPANY_ID_,x.EMAIL_,x.ENABLED_,x.MALE_,x.MOBILE_,x.PASSWORD_,x.SALT_,x.ADDRESS_ FROM BDF2_USER x WHERE ";
		ParseResult result = this.parseCriteria(criteria, false, "x");
		Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
		if (result != null) {
			sql += result.getAssemblySql().toString() + " and ";
			valueMap = result.getValueMap();
		}
		if (roleCriterion != null) {
			String roleId = (String)roleCriterion.getValue();
			if (roleId.startsWith("!")){
				sql += " x.USERNAME_ not in (SELECT USERNAME_ FROM BDF2_ROLE_MEMBER WHERE ROLE_ID_ = ?) and ";
				valueMap.put("roleId", roleId.substring(1));
			}else{
				sql += " x.USERNAME_ in (SELECT USERNAME_ FROM BDF2_ROLE_MEMBER WHERE ROLE_ID_ = ?) and ";
				valueMap.put("roleId", roleId);
			}
		}
		if (groupCriterion != null) {
			String groupId = (String)groupCriterion.getValue();
			if (groupId.startsWith("!")){
				sql += " x.USERNAME_ not in (SELECT USERNAME_ FROM BDF2_GROUP_MEMBER WHERE GROUP_ID_ = ?) and ";
				valueMap.put("groupId", groupId.substring(1));
			}else{
				sql += " x.USERNAME_ in (SELECT USERNAME_ FROM BDF2_GROUP_MEMBER WHERE GROUP_ID_ = ?) and ";
				valueMap.put("groupId", groupId);
			}
		}
		sql += " x.COMPANY_ID_=?";
		valueMap.put("companyId", companyId);
		this.pagingQuery(page, sql, valueMap.values().toArray(),
				new DefaultUserRowMapper());
	}
	
	private SingleValueFilterCriterion getRoleFilterCriterion(Criteria criteria){
		if (criteria==null || criteria.getCriterions()==null) return null;
		
		SingleValueFilterCriterion fc = null;
		for(Criterion c : criteria.getCriterions()){
			if (c instanceof SingleValueFilterCriterion){
				SingleValueFilterCriterion sfc = (SingleValueFilterCriterion) c;
				if ("roleId".equals(sfc.getProperty())) {
					fc = sfc;
					break;
				}
			}
		}
		return fc;
	}

	private SingleValueFilterCriterion getGroupFilterCriterion(Criteria criteria){
		if (criteria==null || criteria.getCriterions()==null) return null;
		
		SingleValueFilterCriterion fc = null;
		for(Criterion c : criteria.getCriterions()){
			if (c instanceof SingleValueFilterCriterion){
				SingleValueFilterCriterion sfc = (SingleValueFilterCriterion) c;
				if ("groupId".equals(sfc.getProperty())) {
					fc = sfc;
					break;
				}
			}
		}
		return fc;
	}
	
	public Collection<IUser> loadUsersByDeptId(String deptId) {
		String sql = "SELECT x.USERNAME_,x.CNAME_,x.ENAME_,x.ADMINISTRATOR_,x.BIRTHDAY_,x.COMPANY_ID_,x.EMAIL_,x.ENABLED_,x.MALE_,x.MOBILE_,x.PASSWORD_,x.SALT_,x.ADDRESS_ FROM BDF2_USER_DEPT UD INNER JOIN BDF2_USER x ON UD.USERNAME_=x.USERNAME_ WHERE UD.DEPT_ID_=?";
		return this.getJdbcTemplate().query(sql, new Object[] { deptId },
				new DefaultUserRowMapper());
	}

	public void changePassword(String username, String newPassword) {
		String sql = "UPDATE BDF2_USER SET PASSWORD_=?,SALT_=? WHERE USERNAME_=?";
		int salt = RandomUtils.nextInt(1000);
		this.getJdbcTemplate().update(
				sql,
				new Object[] {
						passwordEncoder.encodePassword(newPassword, salt),
						String.valueOf(salt), username });
	}

	public String checkPassword(String username, String password) {
		DefaultUser user = (DefaultUser) ContextHolder.getLoginUser();
		String salt = user.getSalt();
		if (!passwordEncoder
				.isPasswordValid(user.getPassword(), password, salt)) {
			return resourceManager.getString("bdf2.core/passwordIncorrect");
		} else {
			return null;
		}
	}

	public IUser newUserInstance(String username) {
		return new DefaultUser(username);
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void registerAdministrator(String username, String cname,
			String ename, String password, String email, String mobile,
			String companyId) {
		String sql = "INSERT INTO BDF2_USER(USERNAME_,CNAME_,ENAME_,PASSWORD_,SALT_,ENABLED_,ADMINISTRATOR_,EMAIL_,MOBILE_,COMPANY_ID_,MALE_) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		int salt = RandomUtils.nextInt(1000);
		password = passwordEncoder.encodePassword(password, salt);
		this.getJdbcTemplate().update(
				sql,
				new Object[] { username, cname, ename, password,
						String.valueOf(salt), true, true, email, mobile,
						companyId, true });
	}
}

class DefaultUserRowMapper implements RowMapper<IUser> {

	public IUser mapRow(ResultSet rs, int rowNum) throws SQLException {
		DefaultUser user = new DefaultUser();
		user.setUsername(rs.getString("USERNAME_"));
		user.setCname(rs.getString("CNAME_"));
		user.setEname(rs.getString("ENAME_"));
		user.setAdministrator(rs.getBoolean("ADMINISTRATOR_"));
		user.setBirthday(rs.getDate("BIRTHDAY_"));
		user.setCompanyId(rs.getString("COMPANY_ID_"));
		user.setEmail(rs.getString("EMAIL_"));
		user.setEnabled(rs.getBoolean("ENABLED_"));
		user.setMale(rs.getBoolean("MALE_"));
		user.setMobile(rs.getString("MOBILE_"));
		user.setPassword(rs.getString("PASSWORD_"));
		user.setSalt(rs.getString("SALT_"));
		user.setAddress(rs.getString("ADDRESS_"));
		return user;
	}

}
