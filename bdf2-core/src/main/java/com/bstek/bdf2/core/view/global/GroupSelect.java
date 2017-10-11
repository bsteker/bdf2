package com.bstek.bdf2.core.view.global;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
/**
 * @author Jacky.gao
 * @since 2013-2-17
 */
@Component("bdf2.groupSelect")
public class GroupSelect extends CoreHibernateDao{
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
		ParseResult result=this.parseCriteria(criteria, true, "g");
		String hql="from "+Group.class.getName()+" g where ";
		Map<String,Object> map=null;
		if(result!=null){
			hql+=result.getAssemblySql().toString()+" and g.companyId=:companyId";
			map=result.getValueMap();
		}else{
			hql+=" g.companyId=:companyId";
			map=new HashMap<String,Object>();
		}
		map.put("companyId",companyId);
		this.pagingQuery(page, hql+" order by g.createDate desc","select count(*) "+hql, map);
	}
}
