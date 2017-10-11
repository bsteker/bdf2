package com.bstek.bdf2.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.model.DefaultPosition;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

public class DefaultPositionService extends CoreJdbcDao implements
		IPositionService {
	public IPosition newPositionInstance(String positionId) {
		return new DefaultPosition(positionId);
	}

	public List<IPosition> loadUserPositions(String username) {
		String sql = "SELECT P.ID_,P.COMPANY_ID_,P.NAME_,P.DESC_ FROM BDF2_USER_POSITION UP INNER JOIN BDF2_POSITION P ON UP.POSITION_ID_ = P.ID_ WHERE UP.USERNAME_=?";
		return this.getJdbcTemplate().query(sql, new Object[] { username },
				new DefaultPositionRowMapper());
	}

	public IPosition loadPositionById(String positionId) {
		String sql = "SELECT P.ID_,P.COMPANY_ID_,P.NAME_,P.DESC_ FROM BDF2_POSITION P WHERE P.ID_=?";
		List<IPosition> list = this.getJdbcTemplate().query(sql,
				new Object[] { positionId }, new DefaultPositionRowMapper());
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void loadUserNotAssignPositions(Page<IPosition> page, String companyId, String username,
			Criteria criteria) {
		String sql = "SELECT P.ID_,P.COMPANY_ID_,P.NAME_,P.DESC_ FROM BDF2_POSITION P WHERE ";
		String notInSql = "AND P.ID_ not in (SELECT UP.POSITION_ID_ FROM BDF2_USER_POSITION UP WHERE UP.USERNAME_=?) ";
		ParseResult result = this.parseCriteria(criteria, false, "P");
		if (result != null) {
			Map<String, Object> map = result.getValueMap();
			map.put("companyId", companyId);
			map.put("username", username);
			sql += result.getAssemblySql().toString() + " AND P.COMPANY_ID_=? " + notInSql;
			this.pagingQuery(page, sql, map.values().toArray(),
					new DefaultPositionRowMapper());
		} else {
			sql += " P.COMPANY_ID_=? "  + notInSql;
			this.pagingQuery(page, sql, new Object[] { companyId, username },
					new DefaultPositionRowMapper());
		}
	}

	public void loadPagePositions(Page<IPosition> page, String companyId,
			Criteria criteria) {
		String sql = "SELECT P.ID_,P.COMPANY_ID_,P.NAME_,P.DESC_ FROM BDF2_POSITION P WHERE ";
		ParseResult result = this.parseCriteria(criteria, false, "P");
		if (result != null) {
			Map<String, Object> map = result.getValueMap();
			map.put("companyId", companyId);
			sql += result.getAssemblySql().toString() + " AND P.COMPANY_ID_=?";
			this.pagingQuery(page, sql, map.values().toArray(),
					new DefaultPositionRowMapper());
		} else {
			sql += " P.COMPANY_ID_=?";
			this.pagingQuery(page, sql, new Object[] { companyId },
					new DefaultPositionRowMapper());
		}
	}

	public void deleteUserPosition(String positionId) {
		String sql = "DELETE FROM BDF2_USER_POSITION WHERE POSITION_ID_=?";
		this.getJdbcTemplate().update(sql, new Object[] { positionId });
	}
	
	public void deleteUserPositionByUser(String userName){
		String sql = "DELETE FROM BDF2_USER_POSITION WHERE USERNAME_=?";
		this.getJdbcTemplate().update(sql, new Object[] { userName });
	}
}

class DefaultPositionRowMapper implements RowMapper<IPosition> {
	public IPosition mapRow(ResultSet rs, int rowNum) throws SQLException {
		DefaultPosition p = new DefaultPosition();
		p.setId(rs.getString("ID_"));
		p.setCompanyId(rs.getString("COMPANY_ID_"));
		p.setName(rs.getString("NAME_"));
		p.setDesc(rs.getString("DESC_"));
		return p;
	}
}
