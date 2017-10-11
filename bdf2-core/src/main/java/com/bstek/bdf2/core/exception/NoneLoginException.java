package com.bstek.bdf2.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.security.session.SessionInformationObject;
import com.bstek.bdf2.core.security.session.SessionStateConstants;

/**
 * @since 2013-1-27
 * @author Jacky.gao
 */
public class NoneLoginException extends AuthenticationException {
	private static final long serialVersionUID = -7160962899377931433L;
	private boolean sessionKickAway;
	public NoneLoginException(String msg) {
		super(msg);
		HttpServletRequest request=ContextHolder.getRequest();
		if(request==null){
			return;
		}
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		String state=(String)session.getAttribute(SessionStateConstants.SESSION_STATE);
		if(state==null){
			SessionRegistry sessionRegistry=ContextHolder.getBean("bdf2.sessionRegistry");
			SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
			if(info==null){
				return;
			}
			if(info instanceof SessionInformationObject){
				SessionInformationObject obj=(SessionInformationObject)info;
				if(obj.isKickAway()){
					session.setAttribute(SessionStateConstants.SESSION_STATE, SessionStateConstants.KICKAWAY);
					this.sessionKickAway=true;
				}
			}else if(info.isExpired()){
				session.setAttribute(SessionStateConstants.SESSION_STATE, SessionStateConstants.EXPIRED);
			}
		}else if(state.equals(SessionStateConstants.KICKAWAY)){
			this.sessionKickAway=true;
		}
	}
	public boolean isSessionKickAway() {
		return sessionKickAway;
	}
}
