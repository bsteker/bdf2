package com.bstek.bdf2.importexcel.context;

import java.util.Map;

import com.bstek.bdf2.core.context.ContextHolder;

public class ImportContext {
	public static Map<String, Object> getParameters(){
		return (Map<String, Object>)ContextHolder.getRequest().getAttribute(ImportContext.class.getName());
	}
	
	public static void setParameters(Map<String, Object> parameters){
		ContextHolder.getRequest().setAttribute(ImportContext.class.getName(), parameters);
	}
}
