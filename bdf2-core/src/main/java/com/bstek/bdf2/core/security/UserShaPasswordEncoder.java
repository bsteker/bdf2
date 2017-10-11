package com.bstek.bdf2.core.security;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * @since 2013-1-31
 * @author Jacky.gao
 */
public class UserShaPasswordEncoder extends ShaPasswordEncoder {
	public static final String BEAN_ID="bdf2.passwordEncoder";

	public UserShaPasswordEncoder() {
		super();
	}

	public UserShaPasswordEncoder(int strength) {
		super(strength);
	}
	
}
