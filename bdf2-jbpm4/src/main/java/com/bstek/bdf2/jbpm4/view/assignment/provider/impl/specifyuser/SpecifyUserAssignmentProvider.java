package com.bstek.bdf2.jbpm4.view.assignment.provider.impl.specifyuser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jbpm.api.model.OpenExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.AssignmentInfo;
import com.bstek.bdf2.jbpm4.model.InternalAssignment;
import com.bstek.bdf2.jbpm4.model.PrincipalDef;
import com.bstek.bdf2.jbpm4.view.assignment.provider.IAssignmentProvider;

/**
 * 指定一个用户作为任务处理人
 * @author Jacky.gao
 * @since 2013-3-23
 */
@Component
public class SpecifyUserAssignmentProvider extends Jbpm4HibernateDao implements IAssignmentProvider {
	@Value("${bdf2.jbpm4.disableSpecifyUserAssignment}")
	private boolean disabled;
	public String getUrl() {
		return "bdf2.jbpm4.view.assignment.provider.impl.specifyuser.SpecifyUserProvider";
	}

	public String getTypeId() {
		return "specify_user";
	}

	public String getTypeName() {
		return "指定一个用户";
	}

	public Collection<AssignmentInfo> getAssignmentInfos(String assignmentDefId) {
		String hql="from "+InternalAssignment.class.getName()+" where assignmentDefId=:assignmentDefId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("assignmentDefId", assignmentDefId);
		List<InternalAssignment> internals=this.query(hql, map);
		List<AssignmentInfo> infos=new ArrayList<AssignmentInfo>();
		for(InternalAssignment internal:internals){
			AssignmentInfo info=new AssignmentInfo();
			info.setName("一个用户");
			info.setValue(internal.getName());
			infos.add(info);
		}
		return infos;
	}

	public Collection<PrincipalDef> getTaskPrincipals(String assignmentDefId,OpenExecution execution) {
		String hql="from "+InternalAssignment.class.getName()+" where assignmentDefId=:assignmentDefId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("assignmentDefId", assignmentDefId);
		List<InternalAssignment> internals=this.query(hql, map);
		List<PrincipalDef> result=new ArrayList<PrincipalDef>();
		for(InternalAssignment internal:internals){
			PrincipalDef def=new PrincipalDef();
			def.setId(internal.getValue());
			result.add(def);
		}
		return result;
	}
	
	public void deleteAssignmentInfos(String assignmentDefId){
		String hql="delete "+InternalAssignment.class.getName()+" where assignmentDefId=:assignmentDefId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("assignmentDefId", assignmentDefId);
		Session session=this.getSessionFactory().openSession();
		try{
			session.createQuery(hql).setString("assignmentDefId", assignmentDefId).executeUpdate();
		}finally{
			session.flush();
			session.close();
		}
	}

	public boolean isDisabled() {
		return disabled;
	}
}
