/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.common;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.idesupport.RuleSetBuilder;
import com.bstek.dorado.idesupport.model.RuleSet;

public class RuleSetHelper{
	private RuleSet ruleSet;
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
		String targetUrl=ContextHolder.getRequest().getRequestURL().toString();
		String uri=ContextHolder.getRequest().getRequestURI();
		int pos=targetUrl.indexOf(uri);
		String contextPath=ContextHolder.getRequest().getContextPath();
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
