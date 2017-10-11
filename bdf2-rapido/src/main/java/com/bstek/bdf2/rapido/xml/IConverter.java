/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.xml;

import org.dom4j.Element;

import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.dorado.idesupport.model.RuleSet;

/**
 * 用于view.xml页面生成时，输出组件对应的xml信息
 * @author jacky.gao@bstek.com
 * @since 2012-8-25
 */
public interface IConverter{
	/**
	 * 根据给定的组件信息生成组件对应的XML
	 * @param component 组件信息
	 * @param ruleSet 组件对应的Dorado7的规则信息
	 * @param rootElement view.xml文档的根
	 * @return 返回组件的Dom4j的Element对象
	 * @throws Exception
	 */
	Element convert(ComponentInfo component,RuleSet ruleSet,Element rootElement) throws Exception;
	/**
	 * 当前Converter是否支持给定的组件
	 * @param component 给定要判断的组件
	 * @return 是否支持
	 */
	boolean support(ComponentInfo component);
}
