package com.bstek.bdf2.authoritydelegation.view.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.authoritydelegation.service.IAuthoritydelegationService;
import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author lucas.yue@bstek.com
 * 
 *         2013-4-20 下午11:06:39
 */
@Component("bdf2.groupSelectAd")
public class GroupSelect extends CoreHibernateDao {
	@DataProvider
	public void loadGroups(Page<Group> page, Criteria criteria)
			throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new RuntimeException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		if (user.isAdministrator()) {
			String hql = "from " + Group.class.getName() + " g where ";
			ParseResult result = this.parseCriteria(criteria, true, "g");
			Map<String, Object> map = null;
			if (result != null) {
				hql += result.getAssemblySql().toString()
						+ " and g.companyId=:companyId";
				map = result.getValueMap();
			} else {
				hql += " g.companyId=:companyId";
				map = new HashMap<String, Object>();
			}
			map.put("companyId",companyId);
			this.pagingQuery(page, hql + " order by g.createDate desc",
					"select count(*) " + hql, map);
		} else {
			IAuthoritydelegationService ads = ContextHolder
					.getBean(IAuthoritydelegationService.BEAN_ID_);
			Page<Group> page1 = new Page<Group>(65535, 1);
			ads.findControllableGroups(page1, user.getUsername(), null);
			Collection<Group> gList = page1.getEntities();
			List<String> gIdList = new ArrayList<String>();
			for (Group g : gList) {
				gIdList.add(g.getId());
			}
			if (gIdList.size() > 0) {
				String hql = "from " + Group.class.getName() + " g where ";
				ParseResult result = this.parseCriteria(criteria, true, "g");
				Map<String, Object> map = null;
				if (result != null) {
					hql += result.getAssemblySql().toString()
							+ " and g.companyId=:companyId";
					map = result.getValueMap();
				} else {
					hql += " g.companyId=:companyId";
					map = new HashMap<String, Object>();
				}
				map.put("companyId",companyId);
				hql += " and g.id in(:gIdList)";
				map.put("gIdList", gIdList);
				this.pagingQuery(page, hql + " order by g.createDate desc",
						"select count(*) " + hql, map);
			}
		}
	}
}
