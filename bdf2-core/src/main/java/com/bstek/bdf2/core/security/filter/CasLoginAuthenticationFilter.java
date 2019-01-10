package com.bstek.bdf2.core.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.DefaultUser;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IGroupService;
import com.bstek.bdf2.core.service.IPositionService;

/**
 * @author Jacky.gao
 * @since 2013-5-23
 */
public class CasLoginAuthenticationFilter extends CasAuthenticationFilter {
	private IDeptService deptService;
	private IPositionService positionService;
	private IGroupService groupService;
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request,
			final HttpServletResponse response) throws AuthenticationException,
			IOException {
				Authentication authResult = super.attemptAuthentication(request, response);
				HttpSession session=request.getSession();
				IUser loginUser=(IUser)authResult.getPrincipal();
				if(loginUser instanceof DefaultUser){
					DefaultUser u=(DefaultUser)loginUser;
					u.setDepts(deptService.loadUserDepts(u.getUsername()));
					u.setPositions(positionService.loadUserPositions(u.getUsername()));
					u.setGroups(groupService.loadUserGroups(u.getUsername()));
				}
				session.setAttribute(ContextHolder.LOGIN_USER_SESSION_KEY, loginUser);
				session.setAttribute(ContextHolder.USER_LOGIN_WAY_KEY, "cas");
				return authResult;
	}
	public IDeptService getDeptService() {
		return deptService;
	}
	public void setDeptService(IDeptService deptService) {
		this.deptService = deptService;
	}
	public IPositionService getPositionService() {
		return positionService;
	}
	public void setPositionService(IPositionService positionService) {
		this.positionService = positionService;
	}
	public IGroupService getGroupService() {
		return groupService;
	}
	public void setGroupService(IGroupService groupService) {
		this.groupService = groupService;
	}
}
