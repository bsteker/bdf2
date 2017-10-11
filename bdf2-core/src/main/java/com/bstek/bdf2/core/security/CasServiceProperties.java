package com.bstek.bdf2.core.security;

import org.springframework.security.cas.ServiceProperties;

public class CasServiceProperties extends ServiceProperties {

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		String url=this.getService();
		if(url.endsWith("/")){
			this.setService(url+"cas_security_check_");			
		}else{
			this.setService(url+"/cas_security_check_");						
		}
	}
}
