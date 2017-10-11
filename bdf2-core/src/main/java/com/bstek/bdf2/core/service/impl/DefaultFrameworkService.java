package com.bstek.bdf2.core.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.DefaultUser;
import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IFrameworkService;
import com.bstek.bdf2.core.service.IGroupService;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.dorado.core.Configure;
import com.google.code.kaptcha.Constants;

public class DefaultFrameworkService implements IFrameworkService {
	public static final String BEAN_ID="bdf2.frameworkService";
	private IDeptService deptService;
	private IPositionService positionService;
	private IGroupService groupService;
	private PasswordEncoder passwordEncoder;
	public void authenticate(IUser user,UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		this.preChecks(authentication);
		DefaultUser defaultUser=(DefaultUser)user;
		String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.isPasswordValid(user.getPassword(), presentedPassword,defaultUser.getSalt())) {
            throw new BadCredentialsException("The password is invalid");
        }else{
        	defaultUser.setDepts(deptService.loadUserDepts(user.getUsername()));
        	defaultUser.setPositions(positionService.loadUserPositions(user.getUsername()));
        	defaultUser.setGroups(groupService.loadUserGroups(user.getUsername()));
        }
	}
	private void preChecks(UsernamePasswordAuthenticationToken authentication)throws AuthenticationException{
		boolean useCaptcha=Configure.getBoolean("bdf2.useCaptchaForLogin");
		if(useCaptcha){
			String key=ContextHolder.getRequest().getParameter("captcha_");
			if(StringUtils.isNotEmpty(key)){
				String sessionkey=(String)ContextHolder.getHttpSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
				if(sessionkey==null){
					throw new BadCredentialsException("验证码过期");
				}else if(!sessionkey.equals(key)){
					throw new BadCredentialsException("验证码不正确");					
				}
			}else{
				throw new BadCredentialsException("验证码不能为空");					
			}
		}
		if (authentication.getPrincipal() == null) {
			throw new BadCredentialsException("Username can not be null");
		}
		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException("password can not be null");
		}
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	public void setDeptService(IDeptService deptService) {
		this.deptService = deptService;
	}
	public void setPositionService(IPositionService positionService) {
		this.positionService = positionService;
	}
	public void setGroupService(IGroupService groupService) {
		this.groupService = groupService;
	}
}
