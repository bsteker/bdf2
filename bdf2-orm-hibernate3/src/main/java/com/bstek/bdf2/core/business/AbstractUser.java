package com.bstek.bdf2.core.business;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;

/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
public abstract class AbstractUser implements IUser{
	private static final long serialVersionUID = 5125145011670416263L;
	@SuppressWarnings("unchecked")
	public final Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles()==null?CollectionUtils.EMPTY_COLLECTION:getRoles();
	}
	public boolean isAccountNonExpired() {
		return true;
	}
	public boolean isAccountNonLocked() {
		return true;
	}
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public int hashCode() {
		if(this.getUsername()!=null){
			return this.getUsername().hashCode();
		}else{
			return -1;
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AbstractUser && (obj.hashCode()==this.hashCode())){
			return true;
		}
		return false;
	}
	
}
