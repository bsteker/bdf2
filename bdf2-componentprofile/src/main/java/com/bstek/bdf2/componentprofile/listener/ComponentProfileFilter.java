/*
 * @author Bing
 * @since 2013-03-05
 */
package com.bstek.bdf2.componentprofile.listener;

import java.util.Collection;

import com.bstek.bdf2.componentprofile.model.ComponentConfig;
import com.bstek.bdf2.componentprofile.model.ComponentConfigMember;

/**
 * @author Bing
 * 
 * @param <T>
 */
public abstract class ComponentProfileFilter<T> {

	public void configure(T component, ComponentConfig config) throws Exception {
		this.rebuildComponentConfig(component, config);
		Collection<ComponentConfigMember> members = config.getComponentConfigMembers();
		if (members != null) {
			this.rebuildComponentConfigMember(component, members);
			config.setComponentConfigMembers(members);
		}
	}

	protected abstract void rebuildComponentConfig(T component, ComponentConfig config);

	protected abstract void rebuildComponentConfigMember(T component, Collection<ComponentConfigMember> members);

}
