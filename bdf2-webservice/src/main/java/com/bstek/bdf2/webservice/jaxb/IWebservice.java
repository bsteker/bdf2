package com.bstek.bdf2.webservice.jaxb;

/**
 * @author Jacky.gao
 * @since 2013-3-6
 */
public interface IWebservice {
	public static final String WS_LOGIN_WAY="loginFromWS";
	Class<?>[] bindClasses();
	boolean useSecurity();
}
