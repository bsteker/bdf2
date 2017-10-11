/*
 * @author Bing
 * @since 2013-03-05
 */
package com.bstek.bdf2.componentprofile.manager;

import java.util.Collection;

import com.bstek.bdf2.componentprofile.model.ComponentConfig;
import com.bstek.dorado.data.variant.Record;

/**
 * @author Bing
 * @since 2013-3-5
 */
public interface IComponentConfigManager {

	void insertComponentConfigMembers(ComponentConfig componentConfig, Collection<Record> members);

	Collection<ComponentConfig> loadComponentConfigsByName(String profileKey);

	Collection<ComponentConfig> loadComponentConfigs();

	void deleteComponentProfileByControlId(String controlId, String name);

	ComponentConfig loadComponentConfig(String controlId, String name);

	Collection<ComponentConfig> loadComponentConfigsByViewName(String viewName);
}
