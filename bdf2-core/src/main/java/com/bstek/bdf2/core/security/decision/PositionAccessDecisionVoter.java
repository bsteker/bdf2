package com.bstek.bdf2.core.security.decision;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.security.attribute.AttributeType;
import com.bstek.bdf2.core.security.attribute.SecurityConfigAttribute;

/**
 * @since 2013-1-25
 * @author Jacky.gao
 */
public class PositionAccessDecisionVoter extends AbstractAccessDecisionVoter {

	@Override
	protected AttributeType getAttributeType() {
		return AttributeType.position;
	}

	public int vote(Authentication authentication, Object object,Collection<ConfigAttribute> attributes) {
		List<IPosition> loginUserPositions=((IUser)authentication.getPrincipal()).getPositions();
		for(ConfigAttribute attribute:attributes){
			SecurityConfigAttribute configAttribute=(SecurityConfigAttribute)attribute;
			if(!configAttribute.getAttributeType().equals(AttributeType.position)){
				continue;
			}
			IPosition position=(IPosition)configAttribute.getMember();
			for(IPosition userPosition:loginUserPositions){
				if(position.getId().equals(userPosition.getId())){
					if(configAttribute.isGranted()){
						return ACCESS_GRANTED;
					}else{
						return ACCESS_DENIED;
					}
				}
			}
		}
		return ACCESS_ABSTAIN;
	}
}
