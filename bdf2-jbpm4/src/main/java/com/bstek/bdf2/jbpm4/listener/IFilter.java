package com.bstek.bdf2.jbpm4.listener;

import java.util.Collection;

import com.bstek.bdf2.jbpm4.model.ComponentControl;

/**
 * @author Jacky.gao
 * @since 2013-6-28
 */
public interface IFilter {
	void filter(Object component,Collection<ComponentControl> controls);
	boolean support(Object component);
}
