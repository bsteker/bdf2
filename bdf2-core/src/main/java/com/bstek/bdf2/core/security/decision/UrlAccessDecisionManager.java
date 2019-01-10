package com.bstek.bdf2.core.security.decision;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

import com.bstek.bdf2.core.business.IUser;

/**
 * 1.首先判断用户有没有权限，如果能在用户级别明确得到允许或拒绝的权限就不再向后计算,<br>
 * 2.如果用户层面没有得到具体答案再计算当前登录用户的岗位有没有权限,<br>
 * 3.岗位层面如果没有答案再向下到登录用户所在的部门（可能有多个），在部门计算时如直接部门未得到答案，同时又允许继承父部门权限，那么将向父部门寻找答案，<br>
 * 4.如果部门上也没有得到答案就只能向用户隶属的群组寻找答案了，因为群组当中可能包含用户、部门、群组，所以还会继续类似上述查询过程
 * @since 2013-1-25
 * @author Jacky.gao
 */
public class UrlAccessDecisionManager extends
		AbstractAccessDecisionManager {
	public static final String BEAN_ID="bdf2.accessDecisionManager";
	public UrlAccessDecisionManager(
			List<AccessDecisionVoter<? extends Object>> decisionVoters) {
		super(decisionVoters);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void decide(Authentication authentication, Object object,Collection<ConfigAttribute> configAttributes)throws AccessDeniedException, InsufficientAuthenticationException {
		if((authentication.getPrincipal() instanceof IUser)){
			IUser loginUser=(IUser)authentication.getPrincipal();
			if(loginUser.isAdministrator())return;			
		}
		int result=10;
		for (AccessDecisionVoter voter : getDecisionVoters()) {
			result = voter.vote(authentication, object, configAttributes);
			if(result==AccessDecisionVoter.ACCESS_ABSTAIN){
				continue;
			}
			if(result==AccessDecisionVoter.ACCESS_DENIED){
				throw new AccessDeniedException("Access is denied");
			}
			if(result==AccessDecisionVoter.ACCESS_GRANTED){
				break;
			}
		}
		if(result==AccessDecisionVoter.ACCESS_ABSTAIN && configAttributes.size()>0){
			throw new AccessDeniedException("Access is denied");
		}
	}
}
