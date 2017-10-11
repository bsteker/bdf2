package com.bstek.bdf2.webservice.sync.cache;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.core.aop.IMethodInterceptor;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.webservice.client.WebServiceClient;
import com.bstek.bdf2.webservice.jaxb.IWebservice;
import com.bstek.bdf2.webservice.rpc.data.DataRequest;
import com.bstek.bdf2.webservice.rpc.data.DataResponse;

/**
 * @author Jacky.gao
 * @since 2013年7月12日
 */
public class SyncRefreshSecurityCache implements IMethodInterceptor,InitializingBean{
	private static final String C1="com.bstek.bdf2.core.view.role.component.RoleComponentMaintain";
	private static final String C2="com.bstek.bdf2.core.view.role.RoleMaintain";
	private static final String C3="com.bstek.bdf2.core.view.role.url.RoleUrlMaintain";
	private static final String C4="com.bstek.bdf2.authoritydelegation.view.role.url.RoleUrlMaintain";
	private static final String M1="refreshUrlSecurityMetadata";
	private static final String M2="refreshAllSecurityMetadata";
	private static final String M3="refreshComponentSecurityMetadata";
	
	private String remoteServerUrls;
	private String remoteServerUsernamePassword;
	private String username;
	private String password;
	public boolean support(Class<?> objectClass, Method method) {
		if(objectClass==null)return false;
		if(ContextHolder.getRequest()==null)return false;
		Object loginWay=ContextHolder.getHttpSession().getAttribute(ContextHolder.USER_LOGIN_WAY_KEY);
		if(loginWay!=null && loginWay.equals(IWebservice.WS_LOGIN_WAY.toString()))return false;
		if(StringUtils.isEmpty(remoteServerUrls))return false;
		String className=objectClass.getName();
		if(className.equals(C1) || className.equals(C2) || className.equals(C3) || className.equals(C4)){
			String methodName=method.getName();
			if(methodName.equals(M1) || methodName.equals(M2) || methodName.equals(M3)){
				return true;
			}
		}
		return false;
	}

	public void doBefore(Class<?> objectClass, Method method, Object[] arguments)
			throws Exception {
		//do nothing
	}

	public void doAfter(Class<?> objectClass, Method method,Object[] arguments, Object returnValue) throws Exception {
		DataRequest req=new DataRequest();
		req.setBeanId("bdf2.roleMaintain");
		req.setMethodName(method.getName());
		String suffix="dorado/webservice/SpringBeanRPC";
		if(StringUtils.isEmpty(remoteServerUsernamePassword)){
			IUser user=ContextHolder.getLoginUser();
			this.username=user.getUsername();
			this.password=user.getPassword();
		}
		for(String url:remoteServerUrls.split(",")){
			if(url.endsWith("/")){
				url=url+suffix;
			}else{
				url=url+"/"+suffix;				
			}
			WebServiceClient client=new WebServiceClient(url);
			client.setUsernameToken(username, password, true);
			client.setMarshallerClasses(new Class<?>[]{DataRequest.class,DataResponse.class});
			DataResponse res=(DataResponse)client.sendAndReceive(req);
			if(!res.isSuccessful()){
				throw new RuntimeException(res.getReturnValue());
			}
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isEmpty(remoteServerUsernamePassword)){
			remoteServerUsernamePassword=System.getProperty("bdf2.syncCacheRemoteServerUsernamePassword");
		}
		if(StringUtils.isNotEmpty(remoteServerUsernamePassword)){
			String[] account=remoteServerUsernamePassword.split(",");
			if(account.length!=2){
				throw new IllegalArgumentException("Username and password must be separated by commas");
			}
			this.username=account[0];
			this.password=account[1];
		}
		if(StringUtils.isEmpty(remoteServerUrls)){
			remoteServerUrls=System.getProperty("bdf2.syncCacheremoteServerUrls");
		}
	}

	public void setRemoteServerUrls(String remoteServerUrls) {
		this.remoteServerUrls = remoteServerUrls;
	}

	public void setRemoteServerUsernamePassword(String remoteServerUsernamePassword) {
		this.remoteServerUsernamePassword = remoteServerUsernamePassword;
	}
}
