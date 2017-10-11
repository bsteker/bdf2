package com.bstek.bdf2.core.security.decision;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.security.attribute.AttributeType;
import com.bstek.bdf2.core.security.attribute.SecurityConfigAttribute;

/**
 * @since 2013-1-25
 * @author Jacky.gao
 */
public class UserAccessDecisionVoter extends AbstractAccessDecisionVoter {
	
	@Override
	protected AttributeType getAttributeType() {
		return AttributeType.user;
	}

	public int vote(Authentication authentication, Object object,Collection<ConfigAttribute> attributes) {
		IUser loginUser=(IUser)authentication.getPrincipal();
		for(ConfigAttribute attribute:attributes){
			SecurityConfigAttribute configAttribute=(SecurityConfigAttribute)attribute;
			boolean granted=configAttribute.isGranted();
			if(!configAttribute.getAttributeType().equals(AttributeType.user)){
				continue;
			}
			IUser user=(IUser)configAttribute.getMember();
			if(loginUser.getUsername().equals(user.getUsername())){
				if(granted){
					return ACCESS_GRANTED;
				}else{
					return ACCESS_DENIED;					
				}
			}
		}
		return ACCESS_ABSTAIN;
	}
}
