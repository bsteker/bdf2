package com.bstek.bdf2.core.security.session;

import java.util.List;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

public class ConcurrentSessionControlStrategyImpl extends ConcurrentSessionControlStrategy {
	public ConcurrentSessionControlStrategyImpl(SessionRegistry sessionRegistry) {
		super(sessionRegistry);
	}
	@Override
    protected void allowableSessionsExceeded(List<SessionInformation> sessions, int allowableSessions,
            SessionRegistry registry) throws SessionAuthenticationException {
        SessionInformation leastRecentlyUsed = null;
        for (SessionInformation session : sessions) {
            if ((leastRecentlyUsed == null)
                    || session.getLastRequest().before(leastRecentlyUsed.getLastRequest())) {
                leastRecentlyUsed = session;
            }
        }
        if(leastRecentlyUsed instanceof SessionInformationObject){
        	SessionInformationObject sessionObject=(SessionInformationObject)leastRecentlyUsed;
        	sessionObject.setKickAway(true);
        }
        leastRecentlyUsed.expireNow();
    }
}
