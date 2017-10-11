package com.bstek.bdf2.jbpm4.listener.impl;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.dorado.view.widget.Container;

/**
 * @author Jacky.gao
 * @since 2013-7-1
 */
@Component("com.bstek.bdf2.jbpm4.listener.impl.ContainerFilter")
public class ContainerFilter extends AbstractFilter {

	public void filter(Object component, Collection<ComponentControl> controls) {
		Container container=(Container)component;
		String id=container.getId();
		if(StringUtils.isNotEmpty(id) && this.match(controls, component, id)!=null){
			container.setIgnored(true);
		}
	}

	public boolean support(Object component) {
		return component instanceof Container;
	}

}
