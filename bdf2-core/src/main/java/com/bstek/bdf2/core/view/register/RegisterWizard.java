package com.bstek.bdf2.core.view.register;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bstek.bdf2.core.CoreJdbcDao;
import com.bstek.bdf2.core.service.ICompanyService;
import com.bstek.bdf2.core.service.IUserService;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.Configure;

/**
 * @since 2013-1-31
 * @author Jacky.gao
 */
public class RegisterWizard extends CoreJdbcDao{
	private IUserService userService;
	
	private ICompanyService companyService;
	
	public void onInit(){
		boolean disabledRegister=Configure.getBoolean("bdf2.disabledSystemRegister");
		if(disabledRegister){
			throw new RuntimeException("The current page has been disabled");
		}
	}
	
	@DataResolver
	public void registerCompanyAndUser(Map<String,Object> company,Map<String,Object> user) throws Exception{
		this.companyService.registerCompany((String)company.get("id"), (String)company.get("name"),(String)company.get("desc"));
		this.userService.registerAdministrator((String)user.get("username"), (String)user.get("cname"), (String)user.get("ename"), (String)user.get("password"), (String)user.get("email"),(String)user.get("mobile"),(String)company.get("id"));
	}
	
	@Expose
	public String checkCompanyExist(String id){
		String result=null;
		String sql="SELECT COUNT(*) FROM BDF2_COMPANY WHERE ID_=?";
		int count=this.getJdbcTemplate().queryForObject(sql,Integer.class,new Object[]{id});
		if(count>0){
			result="公司ID已存在";
		}
		return result;
	}
	
	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public ICompanyService getCompanyService() {
		return companyService;
	}

	public void setCompanyService(ICompanyService companyService) {
		this.companyService = companyService;
	}

	@Expose
	public String checkUserExist(String username){
		String result=null;
		try{
			UserDetails user=userService.loadUserByUsername(username);
			if(user!=null){
				result="用户已存在！";
			}
		}catch(UsernameNotFoundException ex){
		}
		return result;
	}
}
