package com.bstek.bdf2.jbpm5.service.impl;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.impl.EnvironmentFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;

import com.bstek.bdf2.jbpm5.BpmJpaDao;
import com.bstek.bdf2.jbpm5.resource.IKnowledgeResource;
import com.bstek.bdf2.jbpm5.service.IKnowledgeService;
import com.bstek.bdf2.jbpm5.task.TaskManager;
import com.bstek.bdf2.jbpm5.workitem.HumanTaskWorkItemHandler;

public class KnowledgeServiceImpl extends BpmJpaDao implements IKnowledgeService,ApplicationContextAware,InitializingBean{
	private Environment environment;
	private PlatformTransactionManager platformTransactionManager;
	private KnowledgeBase knowledgeBase;
	private TaskManager taskManager;
	public synchronized StatefulKnowledgeSession getSession(int sessionId) {
		if(environment==null){
			initEnvironment();
		}
		StatefulKnowledgeSession session=JPAKnowledgeService.loadStatefulKnowledgeSession(sessionId, knowledgeBase, null, environment);
		session.getWorkItemManager().registerWorkItemHandler("Human Task", new HumanTaskWorkItemHandler(session,taskManager));
		new JPAWorkingMemoryDbLogger(session);
		return session;
	}
	public synchronized StatefulKnowledgeSession createSession() {
		if(environment==null){
			initEnvironment();
		}
		StatefulKnowledgeSession session=JPAKnowledgeService.newStatefulKnowledgeSession(knowledgeBase,null, environment);
		session.getWorkItemManager().registerWorkItemHandler("Human Task", new HumanTaskWorkItemHandler(session,taskManager));
		new JPAWorkingMemoryDbLogger(session);
		return session;
	}
	
	public KnowledgeBase getKnowledgeBase(){
		return knowledgeBase;
	}
	
	private void initEnvironment(){
		EntityManagerFactory emf=null;
		Map<String,EntityManagerFactory> map=this.getJpaEntityManagerRepository().getEntityManagerFactoryMap();
		if(map.size()==0){
			throw new RuntimeException("You need config a JPA EntityManagerFactory for jBPM5 module!");
		}
		String dsName=this.getModuleFixDataSourceName();
		if(map.containsKey(dsName)){
			emf=map.get(dsName);
		}else{
			emf=map.values().iterator().next();
		}
		environment = EnvironmentFactory.newEnvironment();
		environment.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
		environment.set(EnvironmentName.TRANSACTION_MANAGER,platformTransactionManager);
	}

	public PlatformTransactionManager getPlatformTransactionManager() {
		return platformTransactionManager;
	}
	public void setPlatformTransactionManager(
			PlatformTransactionManager platformTransactionManager) {
		this.platformTransactionManager = platformTransactionManager;
	}
	
	public TaskManager getTaskManager() {
		return taskManager;
	}
	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}
	public void afterPropertiesSet() throws Exception {
		KnowledgeBuilder kbuilder=KnowledgeBuilderFactory.newKnowledgeBuilder();
		for(IKnowledgeResource resource:this.getApplicationContext().getBeansOfType(IKnowledgeResource.class).values()){
			kbuilder.add(resource.getResource(), resource.getResourceType());
		}
		knowledgeBase = kbuilder.newKnowledgeBase();
	}
}
