package com.bstek.bdf2.jbpm5.resource;

import org.drools.builder.ResourceType;
import org.drools.io.Resource;

public interface IKnowledgeResource {
	Resource getResource();
	ResourceType getResourceType();
}
