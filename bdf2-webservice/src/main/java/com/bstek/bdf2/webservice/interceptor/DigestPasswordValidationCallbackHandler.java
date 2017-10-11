package com.bstek.bdf2.webservice.interceptor;

import java.io.IOException;

import javax.security.auth.callback.UnsupportedCallbackException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.ws.soap.security.callback.CleanupCallback;
import org.springframework.ws.soap.security.support.SpringSecurityUtils;
import org.springframework.ws.soap.security.wss4j.callback.AbstractWsPasswordCallbackHandler;
import org.springframework.ws.soap.security.wss4j.callback.UsernameTokenPrincipalCallback;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.webservice.jaxb.IWebservice;

/**
 * @author Jacky.gao
 * @since 2013-3-6
 */
public class DigestPasswordValidationCallbackHandler extends
		AbstractWsPasswordCallbackHandler implements ApplicationContextAware {
	private UserCache userCache = new NullUserCache();
	private UserDetailsService userDetailsService;
	private String userServiceBean;
	private String userCacheBean;

	@Override
	protected void handleUsernameToken(WSPasswordCallback callback)
			throws IOException, UnsupportedCallbackException {
		String identifier = callback.getIdentifier();
		UserDetails user = loadUserDetails(identifier);
		if (user != null) {
			SpringSecurityUtils.checkUserValidity(user);
			callback.setPassword(user.getPassword());
		}
	}

	@Override
	protected void handleUsernameTokenPrincipal(UsernameTokenPrincipalCallback callback) throws IOException,
			UnsupportedCallbackException {
		UserDetails user = loadUserDetails(callback.getPrincipal().getName());
		WSUsernameTokenPrincipal principal = callback.getPrincipal();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				principal, principal.getPassword(), user.getAuthorities());
		if (logger.isDebugEnabled()) {
			logger.debug("Authentication success: " + authRequest.toString());
		}
		SecurityContextHolder.getContext().setAuthentication(authRequest);
		if (user instanceof IUser) {
			HttpSession session=ContextHolder.getHttpSession();
			session.setAttribute(ContextHolder.LOGIN_USER_SESSION_KEY, user);
			session.setAttribute(ContextHolder.USER_LOGIN_WAY_KEY,IWebservice.WS_LOGIN_WAY);
		}
	}

	@Override
	protected void handleCleanup(CleanupCallback callback) throws IOException,
			UnsupportedCallbackException {
		SecurityContextHolder.clearContext();
	}

	private UserDetails loadUserDetails(String username) throws DataAccessException {
		UserDetails user = userCache.getUserFromCache(username);
		if (user == null) {
			try {
				user = userDetailsService.loadUserByUsername(username);
			} catch (UsernameNotFoundException notFound) {
				if (logger.isDebugEnabled()) {
					logger.debug("Username '" + username + "' not found");
				}
				return null;
			}
			userCache.putUserInCache(user);
		}
		return user;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (StringUtils.isEmpty(userCacheBean)) {
			if (applicationContext.containsBean(userServiceBean)) {
				userDetailsService = (UserDetailsService) applicationContext
						.getBean(userServiceBean);
			} else {
				throw new RuntimeException("Can not found spring bean["
						+ userServiceBean + "] implements "
						+ UserDetailsService.class.getName() + " interface!");
			}
		} else {
			if (applicationContext.containsBean(userCacheBean)) {
				userCache = (UserCache) applicationContext
						.getBean(userServiceBean);
			} else {
				throw new RuntimeException("Can not found spring bean["
						+ userCacheBean + "] implements "
						+ UserCache.class.getName() + " interface!");
			}

		}
	}

	public void setUserServiceBean(String userServiceBean) {
		this.userServiceBean = userServiceBean;
	}

	public void setUserCacheBean(String userCacheBean) {
		this.userCacheBean = userCacheBean;
	}
}
