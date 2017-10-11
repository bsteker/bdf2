/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.builder;

import org.dom4j.Element;

import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.dorado.idesupport.model.RuleSet;

public interface IBuilder {
	Element build(PageInfo page,RuleSet ruleSet) throws Exception;
}
