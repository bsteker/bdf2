package com.bstek.bdf2.core.security.decision;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;

import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.security.attribute.AttributeType;
import com.bstek.bdf2.core.security.attribute.SecurityConfigAttribute;
import com.bstek.dorado.core.Configure;

/**
 * @since 2013-1-25
 * @author Jacky.gao
 */
public abstract class AbstractAccessDecisionVoter implements AccessDecisionVoter<Object> {
	public boolean supports(ConfigAttribute attribute) {
		if(!(attribute instanceof SecurityConfigAttribute)){
			return false;
		}
		SecurityConfigAttribute securityConfigAttribute=(SecurityConfigAttribute)attribute;
		if(securityConfigAttribute.getAttributeType().equals(getAttributeType())){
			return true;
		}else{
			return false;			
		}
	}
	
	/**
	 * 先判断当前给出的部门当中有没有允许或拒绝访问的部门，如果有直接返回，<br>
	 * 如果没有同时又允许继承父部门权限，那么就对给出部门的上级部门进行判断，如果有返回，否则继续以递归形式向上级部门查找
	 * @param attributes 权限资源信息
	 * @param depts 要评测的部门集合
	 * @return 评测的结果
	 */
	protected int decitionDepts(Collection<ConfigAttribute> attributes,List<IDept> depts){
		int voteResult=ACCESS_ABSTAIN;
		for(IDept dept:depts){
			voteResult=voteDept(attributes,dept);
			if(voteResult!=ACCESS_ABSTAIN){
				return voteResult;
			}
		}
		boolean inheriteParentDeptPermission=Configure.getBoolean("bdf2.inheritParentDeptPermission");
		if(!inheriteParentDeptPermission){
			return voteResult;
		}
		for(IDept dept:depts){
			if(dept.getParent()==null){
				continue;
			}
			voteResult=recurVoteParentDept(attributes,dept.getParent());
			if(voteResult!=ACCESS_ABSTAIN){
				break;
			}
		}
		return voteResult;
	}
	
	private int recurVoteParentDept(Collection<ConfigAttribute> attributes,IDept userDept){
		int voteResult=voteDept(attributes,userDept);
		if(voteResult==ACCESS_ABSTAIN && userDept.getParent()!=null){
			voteResult=recurVoteParentDept(attributes,userDept.getParent());
		}
		return voteResult;
	}
	
	private int voteDept(Collection<ConfigAttribute> attributes,IDept userDept){
		for(ConfigAttribute attribute:attributes){
			SecurityConfigAttribute configAttribute=(SecurityConfigAttribute)attribute;
			if(!configAttribute.getAttributeType().equals(AttributeType.dept)){
				continue;
			}
			boolean granted=configAttribute.isGranted();
			IDept dept=(IDept)configAttribute.getMember();
			if(dept.getId().equals(userDept.getId())){
				if(granted){
					return ACCESS_GRANTED;
				}else{
					return ACCESS_DENIED;
				}
			}
		}
		return ACCESS_ABSTAIN;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}
	protected abstract AttributeType getAttributeType();
}
