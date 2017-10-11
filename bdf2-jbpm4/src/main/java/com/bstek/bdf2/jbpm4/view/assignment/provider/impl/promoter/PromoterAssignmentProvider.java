package com.bstek.bdf2.jbpm4.view.assignment.provider.impl.promoter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.model.OpenExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.model.AssignmentInfo;
import com.bstek.bdf2.jbpm4.model.PrincipalDef;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.jbpm4.view.assignment.provider.IAssignmentProvider;

/**
 * 流程发起人任务分配方式
 * @author Jacky.gao
 * @since 2013-3-23
 */
@Component
public class PromoterAssignmentProvider implements IAssignmentProvider {
	@Value("${bdf2.jbpm4.disablePromoterAssignment}")
	private boolean disabled;
	@Autowired
	@Qualifier(IBpmService.BEAN_ID)
	private IBpmService bmpService;
	public String getUrl() {
		return null;
	}

	public String getTypeId() {
		return "promoter";
	}

	public String getTypeName() {
		return "流程实例发起人";
	}

	public Collection<AssignmentInfo> getAssignmentInfos(String assignmentDefId) {
		List<AssignmentInfo> info=new ArrayList<AssignmentInfo>();
		AssignmentInfo a=new AssignmentInfo();
		a.setName("流程实例发起人");
		a.setValue("从流程实例中取名为["+IBpmService.PROCESS_INSTANCE_PROMOTER+"]的变量值,这个变量值表示流程实例发起人用户名");
		info.add(a);
		return info;
	}

	public Collection<PrincipalDef> getTaskPrincipals(String assignmentDefId,OpenExecution execution) {
		List<PrincipalDef> usernames=new ArrayList<PrincipalDef>();
		String username=(String)execution.getVariable(IBpmService.PROCESS_INSTANCE_PROMOTER);
		if(StringUtils.isEmpty(username)){
			throw new RuntimeException("Variable not found ["+IBpmService.PROCESS_INSTANCE_PROMOTER+"] in current process instance");
		}
		PrincipalDef p=new PrincipalDef();
		p.setId(username);
		usernames.add(p);
		return usernames;
	}
	
	public void deleteAssignmentInfos(String assignmentDefId){
		//do nothing
	}
	
	public boolean isDisabled(){
		return disabled;
	}

}
