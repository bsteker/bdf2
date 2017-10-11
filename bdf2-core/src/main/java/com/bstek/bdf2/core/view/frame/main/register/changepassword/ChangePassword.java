package com.bstek.bdf2.core.view.frame.main.register.changepassword;

import java.util.Map;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;

/**
 * @author Jacky.gao
 * @since 2013-2-8
 */
public class ChangePassword {
	private IUserService userService;
	@Expose
	public String checkPassword(String password) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		return userService.checkPassword(user.getUsername(), password);
	}
	
	@DataResolver
	public void changePassword(Map<String,Object> data) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		userService.changePassword(user.getUsername(), (String)data.get("newPassword"));
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
}
