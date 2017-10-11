package com.bstek.bdf2.core.security.metadata;

/**
 * 设置那些不需要登录就可以访问的URL(可匿名访问的URL)，URL在设置的时候可以采用通配符模式,<br>
 * 比如/*.js，这就表示根目录下所有的js文件将不需要通过登录就可以访问，<br>
 * 同时即使在登录情况下，这些js文件在加载时也不会进行权限判断
 * @since 2013-1-26
 * @author Jacky.gao
 */
public class AnonymousUrl {
	private String urlPattern;

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
}
