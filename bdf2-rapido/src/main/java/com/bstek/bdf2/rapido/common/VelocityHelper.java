/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.common;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;

public class VelocityHelper implements InitializingBean{
	private VelocityEngine velocityEngine;
	
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	public void afterPropertiesSet() throws Exception {
		velocityEngine=new VelocityEngine();
		Properties p=new Properties();
		p.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
		velocityEngine.init(p);
	}
}
