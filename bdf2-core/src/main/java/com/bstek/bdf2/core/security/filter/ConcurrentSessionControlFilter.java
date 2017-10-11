package com.bstek.bdf2.core.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import com.bstek.bdf2.core.security.session.SessionInformationObject;
import com.bstek.bdf2.core.security.session.SessionStateConstants;

/**
 * @author Jacky.gao
 * @since 2013年12月19日
 */
public class ConcurrentSessionControlFilter extends ConcurrentSessionFilter {
	private String sessionKickAwayUrl;
	public ConcurrentSessionControlFilter(SessionRegistry sessionRegistry, String expiredUrl,String sessionKickAwayUrl) {
		super(sessionRegistry,expiredUrl);
		this.sessionKickAwayUrl=sessionKickAwayUrl;
	}
	protected String determineExpiredUrl(HttpServletRequest request, SessionInformation info) {
		HttpSession session=request.getSession();
		if(info instanceof SessionInformationObject){
			SessionInformationObject sessionObject=(SessionInformationObject)info;
			if(sessionObject.isKickAway()){
				session.setAttribute(SessionStateConstants.SESSION_STATE, SessionStateConstants.KICKAWAY);
				return sessionKickAwayUrl;
			}
		}
		session.setAttribute(SessionStateConstants.SESSION_STATE, SessionStateConstants.EXPIRED);
        return super.determineExpiredUrl(request, info);
    }
}
