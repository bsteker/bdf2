/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.bsh;

import java.util.Map;

/**
 * 注册BeanShell脚本中要使用的变量信息,实现类需要配置到Spring环境当中，<br>
 * 因此在注册时可使用Spring环境中各种类型的Bean对象
 * @author jacky.gao@bstek.com
 * @since 2012-8-28
 */
public interface VariableRegister {
	/**
	 * 返回注册到BeanShell上下文中的变量集合
	 * @return 返回一个变量集合的Map，Map中key为变量名，Value就是一个VariableInfo对象，用于描述变量信息
	 */
	Map<String,VariableInfo> register();
}
