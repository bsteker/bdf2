package com.bstek.bdf2.core.security.decision;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.security.attribute.AttributeType;

/**
 * 1.首先判断当前用户所在所有部门是否有在授权的部门范围内,如果在就明确返回允许或拒绝访问,<br>
 * 2.如果都不在那么再找当前登录用户所在部门的父部门有没有权限，如果没有，那么就拒绝访问
 * @since 2013-1-25
 * @author Jacky.gao
 */
public class DeptAccessDecisionVoter extends AbstractAccessDecisionVoter {

	@Override
	protected AttributeType getAttributeType() {
		return AttributeType.dept;
	}

	public int vote(Authentication authentication, Object object,Collection<ConfigAttribute> attributes) {
		List<IDept> loginUserDepts=((IUser)authentication.getPrincipal()).getDepts();
		if(loginUserDepts==null || loginUserDepts.size()==0){
			return ACCESS_ABSTAIN;
		}
		return decitionDepts(attributes,loginUserDepts);
	}
}
