/*
 * This file is part of BDF
 * BDF£¬Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.common;

/**
 * @author Jacky
 */
public class StringHelper {
	public static boolean isEmpty(String str){
		if(str==null || str.trim().length()<1){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isNotEmpty(String str){
		if(str!=null && str.trim().length()>0){
			return true;
		}else{
			return false;
		}
	}
}
