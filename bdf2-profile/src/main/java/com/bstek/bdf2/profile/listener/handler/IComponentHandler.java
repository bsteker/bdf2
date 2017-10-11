package com.bstek.bdf2.profile.listener.handler;

import java.util.Map;

import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public interface IComponentHandler {
	void handle(IComponentService componentService,Object control,String assignTargetId,Map<String,ComponentInfo> mapInCache);
	boolean support(Object control);
}
