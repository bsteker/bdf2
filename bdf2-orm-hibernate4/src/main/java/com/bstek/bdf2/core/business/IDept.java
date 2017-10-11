package com.bstek.bdf2.core.business;

import java.util.List;

/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
public interface IDept extends ICompany{
	String getId();
	String getName();
	String getParentId();
	IDept getParent();
	List<IUser> getUsers();
}
