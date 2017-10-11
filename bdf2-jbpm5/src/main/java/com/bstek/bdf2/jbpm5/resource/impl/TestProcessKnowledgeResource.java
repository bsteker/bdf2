package com.bstek.bdf2.jbpm5.resource.impl;

import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;

import com.bstek.bdf2.jbpm5.resource.IKnowledgeResource;

public class TestProcessKnowledgeResource implements IKnowledgeResource {

	public Resource getResource() {
		return ResourceFactory.newClassPathResource("bpmn/test.bpmn");
	}

	public ResourceType getResourceType() {
		return ResourceType.BPMN2;
	}
}
