package com.bstek.bdf2.core.business;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.Role;

/**
 * @since 2013-1-24
 * @author Jacky.gao
 */
public interface IUser extends UserDetails,ICompany{
	String getCname();
	String getEname();
	boolean isAdministrator();
	String getMobile();
	String getEmail();
	List<IDept> getDepts();
	List<IPosition> getPositions();
	List<Role> getRoles();
	List<Group> getGroups();
	Map<String,Object> getMetadata();
}
