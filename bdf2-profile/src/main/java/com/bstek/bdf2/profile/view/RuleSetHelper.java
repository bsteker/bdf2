package com.bstek.bdf2.profile.view;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.dorado.idesupport.RuleSetBuilder;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.web.DoradoContext;

@Component("bdf2.profile.ruleSetHelper")
public class RuleSetHelper{
	public static final String BEAN_ID="bdf2.profile.ruleSetHelper";
	private RuleSet ruleSet;
	@Autowired
	@Qualifier("dorado.idesupport.ruleSetBuilder")
	private RuleSetBuilder ruleSetBuilder;
	public RuleSet getRuleSet() {
		synchronized (this) {
			if(ruleSet==null){
				try {
					this.initRuleSet();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return ruleSet;
	}
	public void initRuleSet() throws Exception{
		InputStream fin = null;
		String targetUrl=DoradoContext.getCurrent().getRequest().getRequestURL().toString();
		String uri=DoradoContext.getCurrent().getRequest().getRequestURI();
		int pos=targetUrl.indexOf(uri);
		String contextPath=DoradoContext.getCurrent().getRequest().getContextPath();
		if(StringUtils.isNotEmpty(contextPath)){
			targetUrl=targetUrl.substring(0,pos)+contextPath+"/dorado/ide/config-rules.xml";			
		}else{
			targetUrl=targetUrl.substring(0,pos)+"/dorado/ide/config-rules.xml";						
		}
		URL url=new URL(targetUrl);
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		try{
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(10000);
			httpConnection.setRequestMethod("GET");
			fin=httpConnection.getInputStream();
			ruleSet = ruleSetBuilder.buildRuleSet(fin);
		}finally{
			if(fin!=null)fin.close();	
			if(httpConnection!=null)httpConnection.disconnect();
		}
	}
	
	public RuleSetBuilder getRuleSetBuilder() {
		return ruleSetBuilder;
	}
	public void setRuleSetBuilder(RuleSetBuilder ruleSetBuilder) {
		this.ruleSetBuilder = ruleSetBuilder;
	}
}
