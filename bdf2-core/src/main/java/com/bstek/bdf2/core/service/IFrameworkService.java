package com.bstek.bdf2.core.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import com.bstek.bdf2.core.business.IUser;

/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
public interface IFrameworkService {
	public static final String BEAN_ID="bdf2.frameworkService";
	void authenticate(IUser user,UsernamePasswordAuthenticationToken authentication) throws AuthenticationException;
}
