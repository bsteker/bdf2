/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.key;

import java.util.Map;

/**
 * 定义主键生成器
 * @author jacky.gao@bstek.com
 * @since 2012-7-16
 */
public interface IGenerator<T> {
	/**
	 * 用于显示的描述
	 * @return 返回描述信息
	 */
	String desc();
	/**
	 * 产生一个唯一的用于填充主键的ID值,唯一的参数是一个Map，其中一定包含的参数有如下三个:<br>
	 * 1.entityTableName:当前操作的表名，<br>
	 * 2.entityFields：当前表对应实体定义时包含的字段，<br>
	 * 3.entityTablePrimaryKeys：当前表在定义实体时设置的主键名，多个主键字段名之间采用逗号分隔(,)，<br>
	 * 以上三个在Map中包含的参数，可以在生成唯一ID时可以选择性的使用，<br>
	 * 除以上三个参数包，其中还包含所有在客户端动态添加进来的参数
	 * @param map 数据提交中所带的各种参数
	 * @return 返回的的UUID值
	 */
	T execute(Map<String,Object> map);
}
