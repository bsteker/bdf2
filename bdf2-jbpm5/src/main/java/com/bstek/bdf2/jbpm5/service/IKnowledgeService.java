package com.bstek.bdf2.jbpm5.service;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * @author Jacky.gao
 * @since 2013-4-9
 */
public interface IKnowledgeService {
	public static final String BEAN_ID="bdf2.jbpm5.bpmService";
	StatefulKnowledgeSession createSession();
	StatefulKnowledgeSession getSession(int sessionId);
	KnowledgeBase getKnowledgeBase();
}
