package com.bstek.bdf2.core.aop;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.SerializationUtils;

import com.bstek.bdf2.core.cache.RefreshCacheRegister;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.web.DoradoContext;

public class RefreshCacheMethodInterceptor implements IMethodInterceptor, InitializingBean, ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(RefreshCacheMethodInterceptor.class);

	private String refreshMethodInterceptors;
	private String apps;

	private List<String> appList = new ArrayList<String>();
	private List<String> methodInterceptorList = new ArrayList<String>();
	private ConcurrentHashMap<String, String> parameterCache = new ConcurrentHashMap<String, String>();

	private ApplicationContext applicationContext;

	public boolean support(Class<?> objectClass, Method method) {
		if (ContextHolder.getLoginUser() == null || appList.size() == 0) {
			return false;
		}
		String key = objectClass.getName() + "#" + method.getName();
		String value = parameterCache.get(key);
		if (StringUtils.isNotEmpty(value)) {
			return true;
		} else {
			for (String property : methodInterceptorList) {
				String[] propertyArray = property.split("#");
				if (propertyArray.length == 2) {
					Object object = applicationContext.getBean(propertyArray[0]);
					String tempKey = AopUtils.getTargetClass(object).getName() + "#" + propertyArray[1];
					if (key.equals(tempKey)) {
						parameterCache.put(key, property);
						return true;
					}
				}
			}
		}
		return false;
	}

	public void doBefore(Class<?> objectClass, Method method, Object[] arguments) throws Exception {
	}

	public void doAfter(Class<?> objectClass, Method method, Object[] arguments, Object returnValue) throws Exception {
		String key = objectClass.getName() + "#" + method.getName();
		HttpServletRequest request = DoradoContext.getCurrent().getRequest();
		String currentAppUrl = request.getScheme() + "://" + InetAddress.getLocalHost().getHostAddress() + ":" + request.getServerPort() + request.getContextPath();
		for (String appUrl : appList) {
			if (appUrl.startsWith(currentAppUrl)) {
				continue;
			}
			String urlPath = appUrl.endsWith("/") ? appUrl.substring(0, appUrl.length() - 1) : appUrl;
			String[] splitValueArray = parameterCache.get(key).split("#");
			urlPath += "/system.refreshCache.action" + "?beanId=" + splitValueArray[0] + "&methodName=" + splitValueArray[1];

			InputStream inputStream = null;
			ByteArrayOutputStream outputStream = null;
			try {
				if (arguments != null) {
					String parameter = new String(Base64.encodeBase64(SerializationUtils.serialize(arguments), false, true));
					urlPath += "&parameter=" + parameter;
				}
				URL url = new URL(urlPath);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setUseCaches(false);
				inputStream = urlConnection.getInputStream();
				outputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int n = 0;
				while (-1 != (n = inputStream.read(buffer))) {
					outputStream.write(buffer, 0, n);
				}
				log.info("request url " + urlPath + " success, result " + outputStream.toString());
			} catch (Exception e) {
				log.error("request url " + urlPath + " error," + e.getMessage());
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}

	}

	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isNotEmpty(apps)) {
			appList = Arrays.asList(apps.split(","));
		}
		if (StringUtils.isNotEmpty(refreshMethodInterceptors)) {
			for (String temp : refreshMethodInterceptors.split(",")) {
				methodInterceptorList.add(temp);
			}
		}
		Collection<RefreshCacheRegister> refreshCacheRegisters = applicationContext.getBeansOfType(RefreshCacheRegister.class).values();
		for (RefreshCacheRegister refreshCacheRegister : refreshCacheRegisters) {
			List<String> beanMethodNames = refreshCacheRegister.getBeanMethodNames();
			if (beanMethodNames != null) {
				for (String name : beanMethodNames) {
					methodInterceptorList.add(name);
				}
			}
		}
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
	}

	public void setRefreshMethodInterceptors(String refreshMethodInterceptors) {
		this.refreshMethodInterceptors = refreshMethodInterceptors;
	}

	public void setApps(String apps) {
		this.apps = apps;
	}

}
