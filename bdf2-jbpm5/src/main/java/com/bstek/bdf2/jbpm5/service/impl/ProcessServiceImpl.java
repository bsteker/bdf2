package com.bstek.bdf2.jbpm5.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.jbpm5.service.IKnowledgeService;
import com.bstek.bdf2.jbpm5.service.IProcessService;
import com.bstek.bdf2.jbpm5.service.ProcessConstants;

/**
 * @author Jacky.gao
 * @since 2013-4-14
 */
public class ProcessServiceImpl implements IProcessService, ProcessConstants {
	private IKnowledgeService bpmService;

	public synchronized ProcessInstance startProcess(String processId) {
		StatefulKnowledgeSession ksession = this.bpmService.createSession();
		try {
			Map<String, Object> map = this.initProcessVariables(null);
			ProcessInstance processInstance = ksession.startProcess(processId,map);
			return processInstance;
		} finally {
			ksession.dispose();
		}
	}

	public synchronized ProcessInstance startProcess(String processId,
			Map<String, Object> map) {
		StatefulKnowledgeSession ksession = this.bpmService.createSession();
		try {
			map = this.initProcessVariables(map);
			ProcessInstance processInstance = ksession.startProcess(processId,
					map);
			return processInstance;
		} finally {
			ksession.dispose();
		}
	}

	public synchronized ProcessInstance startProcess(String processId,
			String businessId) {
		StatefulKnowledgeSession ksession = this.bpmService.createSession();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(BUSINESS_ID, businessId);
			map = this.initProcessVariables(map);
			ProcessInstance processInstance = ksession.startProcess(processId,
					map);
			return processInstance;
		} finally {
			ksession.dispose();
		}
	}

	public synchronized void abortProcessInstance(long processInstanceId) {
		StatefulKnowledgeSession ksession = this.bpmService.createSession();
		try {
			ksession.abortProcessInstance(processInstanceId);
		} finally {
			ksession.dispose();
		}
	}

	public synchronized Map<String, Object> getProcessInstanceVariables(
			long processInstanceId) {
		StatefulKnowledgeSession ksession = this.bpmService.createSession();
		try {
			ProcessInstance processInstance = ksession.getProcessInstance(processInstanceId);
			if (processInstance != null) {
				Map<String, Object> variables = ((WorkflowProcessInstanceImpl) processInstance)
						.getVariables();
				if (variables == null) {
					return new HashMap<String, Object>();
				}
				// filter out null values
				Map<String, Object> result = new HashMap<String, Object>();
				for (Map.Entry<String, Object> entry : variables.entrySet()) {
					if (entry.getValue() != null) {
						result.put(entry.getKey(), entry.getValue());
					}
				}
				return result;
			} else {
				throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
			}
		} finally {
			ksession.dispose();
		}
	}

	public void setProcessInstanceVariables(long processInstanceId,
			Map<String, Object> variables) {
		StatefulKnowledgeSession ksession = this.bpmService.createSession();
		try {
			ProcessInstance processInstance = ksession.getProcessInstance(processInstanceId);
			if (processInstance != null) {
                VariableScopeInstance variableScope = (VariableScopeInstance) 
                    ((org.jbpm.process.instance.ProcessInstance) processInstance)
                        .getContextInstance(VariableScope.VARIABLE_SCOPE);
                if (variableScope == null) {
                    throw new IllegalArgumentException("Could not find variable scope for process instance " + processInstanceId);
                }
                for (Map.Entry<String, Object> entry: variables.entrySet()) {
                    variableScope.setVariable(entry.getKey(), entry.getValue());
                }
            } else {
                throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
            }
		} finally {
			ksession.dispose();
		}
	}

	private Map<String, Object> initProcessVariables(Map<String, Object> map) {
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		IUser user = ContextHolder.getLoginUser();
		if (user != null) {
			map.put(PROCESS_PROMOTER, user.getUsername());
		} else {
			map.put(PROCESS_PROMOTER, ANONYMOUS_USER);
		}
		map.put(PROCESS_START_DATE, new Date());
		return map;
	}

	public IKnowledgeService getBpmService() {
		return bpmService;
	}

	public void setBpmService(IKnowledgeService bpmService) {
		this.bpmService = bpmService;
	}
}
