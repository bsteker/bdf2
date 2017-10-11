/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.action;

import java.util.Collection;
import java.util.Map;

import com.bstek.bdf2.rapido.domain.Parameter;

/**
 * 用于定义前后台交互时要执行的具体动作,<br>
 * 动作可以累加，也就是说可以一次性执行多个当前接口的实现类,<br>
 * 用户可以通过实现该接口，在execute方法中添加具体的需要执行的具体逻辑代码，然后将实现类配置到spring当中即可
 * @author jacky.gao@bstek.com
 * @since 2012-7-5
 */
public interface IAction {
	/**
	 * 返回当前动作的名称
	 * @return 返回动作名称
	 */
	String getName();
	/**
	 * 具体要执行的动作代码，这里的参数只有一个Map，<br>
	 * 在这个Map当中包含了当前业务数据的ID(key为businessId)以及所有在动作定义时定义的参数
	 * @param map 外部传入的参数
	 */
	Map<String,Object> execute(Map<String,Object> map);
	
	/**
	 * 执行这个实现时需要哪些参数
	 * @return 返回一个Parameter对象实例集合
	 */
	Collection<Parameter> requiredParameters();
}
