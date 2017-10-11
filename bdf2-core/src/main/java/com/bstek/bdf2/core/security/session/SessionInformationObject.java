package com.bstek.bdf2.core.security.session;

import java.util.Date;

import org.springframework.security.core.session.SessionInformation;

/**
 * @author Jacky.gao
 * @since 2013年12月19日
 */
public class SessionInformationObject extends SessionInformation {
	private static final long serialVersionUID = 1678981718286334928L;
	private boolean kickAway=false;//用于在只允许一个用户登录的情况下，当前是否为被踢出的用户
	public SessionInformationObject(Object principal, String sessionId, Date lastRequest){
		super(principal,sessionId,lastRequest);
	}
	public boolean isKickAway() {
		return kickAway;
	}
	public void setKickAway(boolean kickAway) {
		this.kickAway = kickAway;
	}
}
