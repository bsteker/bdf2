package com.bstek.bdf2.core.business;

import java.util.List;

/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
public interface IPosition extends ICompany{
	String getId();
	String getName();
	List<IUser> getUsers();
}
