package com.bstek.bdf2.core.context;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.dorado.core.Configure;


public class ContextHolder implements ApplicationContextAware{
	public static final String USER_LOGIN_WAY_KEY="USER_LOGIN_WAY_KEY_";
	public static final String LOGIN_USER_SESSION_KEY="BDF_LOGIN_USER_";
	/**
	 * 保存HttpServletResponse的线程局部变量.
	 */
	private static final ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();
	/**
	 * 保存HttpServletRequest的线程局部变量.
	 */
	private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
	
	/**
	 * 用于在当前线程中存放当前线程操作采用的数据源名称
	 */
	private static final ThreadLocal<String> currentDataSourceNameThreadLocal = new ThreadLocal<String>();
	
	/**
	 * 存储当前线程当中开启的Hibernate Session，以当前session所在的dataSource name为key，在纯种结束时将所有未close的session执行close操作
	 */
	private static final ThreadLocal<Map<String,Session>> hibernateSessionMapThreadLocal = new ThreadLocal<Map<String,Session>>();
	
	private static ApplicationContext applicationContext;
	
	private static String BDF_TEMP_DIR;
	
	public static String getBdfTempFileStorePath(){
		return BDF_TEMP_DIR;
	}
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId){
		return (T)applicationContext.getBean(beanId);
	}
	
	public void setApplicationContext(ApplicationContext context){
		Assert.notNull(context, "ApplicationContext can not be null");
		applicationContext=(ApplicationContext)context;
		String tmpdir=Configure.getString("bdf2.systemTempDir");
		if(StringUtils.isNotEmpty(tmpdir)){
			BDF_TEMP_DIR=tmpdir;
			File file=new File(BDF_TEMP_DIR);
			if(!file.exists() && applicationContext instanceof org.springframework.web.context.WebApplicationContext){
				BDF_TEMP_DIR=((org.springframework.web.context.WebApplicationContext)applicationContext).getServletContext().getRealPath(BDF_TEMP_DIR);
			}
			BDF_TEMP_DIR=BDF_TEMP_DIR.replaceAll("\\\\", "/");
			if(!BDF_TEMP_DIR.endsWith("/")){
				BDF_TEMP_DIR+="/";
			}
		}else{
			BDF_TEMP_DIR=System.getProperty("java.io.tmpdir");
			BDF_TEMP_DIR=BDF_TEMP_DIR.replaceAll("\\\\", "/");
			if(!BDF_TEMP_DIR.endsWith("/")){
				BDF_TEMP_DIR+="/bdftemp/";
			}else{
				BDF_TEMP_DIR+="bdftemp/";				
			}
		}
		File f=new File(BDF_TEMP_DIR);
		if(!f.exists()){
			f.mkdirs();
		}
	}
	
	public static void removeCurrentDataSourceName(){
		currentDataSourceNameThreadLocal.remove();
	}
	public static final void setCurrentDataSourceName(String dataSourceName) {
		currentDataSourceNameThreadLocal.set(dataSourceName);
	}
	public static final String getCurrentDataSourceName() {
		return currentDataSourceNameThreadLocal.get();
	}
	public static HttpServletRequest getRequest() {
		return requestThreadLocal.get();
	}
	public static HttpServletResponse getResponse() {
		return responseThreadLocal.get();
	}
	public static Map<String,Session> getHibernateSessionMap(){
		return hibernateSessionMapThreadLocal.get();
	}
		
	public static HttpSession getHttpSession(){
		HttpServletRequest req=getRequest();
		if(req!=null){
			return req.getSession();
		}else{
			return null;		
		}
	}
	
	public static IUser getLoginUser(){
		HttpSession session=getHttpSession();
		if(session==null){
			return null;
		}
		return (IUser)session.getAttribute(LOGIN_USER_SESSION_KEY);
	}
	
	public static String getLoginUserName(){
		IUser user=getLoginUser();
		if(user!=null){
			return user.getUsername();
		}
		return null;
	}
	public static void setHttpRequestResponseHolder(HttpServletRequest request,HttpServletResponse response) {
		hibernateSessionMapThreadLocal.set(new Hashtable<String,Session>());
		responseThreadLocal.set(response);
		requestThreadLocal.set(request);
	}
	public static void clean(){
		if (null!=hibernateSessionMapThreadLocal.get()){
			for(Session session:hibernateSessionMapThreadLocal.get().values()){
				if(session.isOpen()){
					session.flush();
					session.close();				
				}
			}
		}
		hibernateSessionMapThreadLocal.remove();
		responseThreadLocal.remove();
		requestThreadLocal.remove();
		currentDataSourceNameThreadLocal.remove();
	}
}
