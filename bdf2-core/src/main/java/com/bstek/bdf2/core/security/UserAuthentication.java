package com.bstek.bdf2.core.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.bstek.bdf2.core.business.IUser;

/**
 * @since 2013-1-29
 * @author Jacky.gao
 */
public class UserAuthentication implements Authentication {
	private static final long serialVersionUID = 7275039159608358294L;
	private IUser user;
	private boolean authenticated;
	public UserAuthentication(IUser user){
		this.user=user;
	}
	public String getName() {
		return user.getUsername();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles();
	}

	public Object getCredentials() {
		return user.getAuthorities();
	}

	public Object getDetails() {
		return user;
	}

	public Object getPrincipal() {
		return user;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean isAuthenticated)throws IllegalArgumentException {
		this.authenticated=isAuthenticated;
	}
	
	public String toString(){
		return "user:"+getName() + ", authenticated:"+authenticated;
	}
}
