package com.bstek.bdf2.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.model.ComponentDefinition;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.model.UrlComponent;
import com.bstek.bdf2.core.service.IUrlService;

public class DefaultUrlService extends CoreJdbcDao implements IUrlService{
	public List<Url> loadUrlsByRoleId(String roleId) {
		String sql="SELECT U.ID_,U.URL_,U.COMPANY_ID_,U.NAME_ FROM BDF2_ROLE_RESOURCE R INNER JOIN BDF2_URL U ON R.URL_ID_=U.ID_ WHERE ROLE_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{roleId},new RowMapper<Url>(){
			public Url mapRow(ResultSet rs, int rowNum) throws SQLException {
				Url url=new Url();
				url.setId(rs.getString("ID_"));
				url.setUrl(rs.getString("URL_"));
				url.setName(rs.getString("NAME_"));
				url.setCompanyId(rs.getString("COMPANY_ID_"));
				return url;
			}
		});
	}

	public List<UrlComponent> loadComponentUrlsByRoleId(final String roleId) {
		String sql="SELECT U.ID_,U.URL_,U.COMPANY_ID_,C.COMPONENT_ID_,UC.AUTHORITY_TYPE_ " +
				"FROM BDF2_URL_COMPONENT UC LEFT JOIN BDF2_URL U ON UC.URL_ID_=U.ID_ LEFT JOIN BDF2_COMPONENT C ON UC.COMPONENT_ID_=C.ID_ WHERE UC.ROLE_ID_=?";
		return this.getJdbcTemplate().query(sql, new Object[]{roleId},new RowMapper<UrlComponent>(){

			public UrlComponent mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				UrlComponent uc=new UrlComponent();
				uc.setRoleId(roleId);
				Url url=new Url();
				url.setId(rs.getString("ID_"));
				url.setUrl(rs.getString("URL_"));
				uc.setUrl(url);
				String type=rs.getString("AUTHORITY_TYPE_");
				uc.setAuthorityType(type!=null?AuthorityType.valueOf(type):AuthorityType.read);
				ComponentDefinition component=new ComponentDefinition();
				component.setComponentId(rs.getString("COMPONENT_ID_"));
				uc.setComponent(component);
				return uc;
			}
			
		});
	}
}
