package com.bstek.bdf2.jbpm5.resource;

import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.beans.factory.FactoryBean;

public class StatefulKnowledgeSessionFactoryBean implements FactoryBean<StatefulKnowledgeSession> {

	public StatefulKnowledgeSession getObject() throws Exception {
		return null;
	}

	public Class<StatefulKnowledgeSession> getObjectType() {
		return StatefulKnowledgeSession.class;
	}

	public boolean isSingleton() {
		return true;
	}
}
