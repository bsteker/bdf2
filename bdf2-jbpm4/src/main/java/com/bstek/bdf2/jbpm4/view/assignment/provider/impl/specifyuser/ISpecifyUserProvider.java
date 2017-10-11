package com.bstek.bdf2.jbpm4.view.assignment.provider.impl.specifyuser;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-3-23
 */
public interface ISpecifyUserProvider {
	void loadUsers(Page<IUser> page,Criteria criteria);
}
