/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.key.impl;

import java.util.Map;
import java.util.UUID;

import com.bstek.bdf2.rapido.key.IGenerator;

/**
 * 生成UUID字符串作为主键
 * @author jacky.gao@bstek.com
 * @since 2012-7-16
 */
public class UUIDGenerator implements IGenerator<String>{

	public String desc() {
		return "UUID生成器";
	}

	public String execute(Map<String, Object> map) {
		return UUID.randomUUID().toString();
	}
}
