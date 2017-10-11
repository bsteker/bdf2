package com.bstek.bdf2.core.security.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.security.attribute.AttributeType;
import com.bstek.bdf2.core.security.attribute.SecurityConfigAttribute;

/**
 * @since 2013-1-25
 * @author Jacky.gao
 */
public class GroupAccessDecisionVoter extends AbstractAccessDecisionVoter {
	@Override
	protected AttributeType getAttributeType() {
		return AttributeType.group;
	}

	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		IUser loginUser=(IUser)authentication.getPrincipal();
		int voteResult=voteGroups(loginUser,attributes,GroupVoteType.user);
		if(voteResult!=ACCESS_ABSTAIN){
			return voteResult;
		}
		voteResult=voteGroups(loginUser,attributes,GroupVoteType.dept);
		if(voteResult!=ACCESS_ABSTAIN){
			return voteResult;
		}
		voteResult=voteGroups(loginUser,attributes,GroupVoteType.position);
		if(voteResult!=ACCESS_ABSTAIN){
			return voteResult;
		}
		voteResult=voteGroups(loginUser,attributes,GroupVoteType.group);
		if(voteResult!=ACCESS_ABSTAIN){
			return voteResult;
		}
		return ACCESS_ABSTAIN;
	}
	private int voteGroups(IUser loginUser,Collection<ConfigAttribute> attributes,GroupVoteType type){
		for(ConfigAttribute attribute:attributes){
			SecurityConfigAttribute configAttribute=(SecurityConfigAttribute)attribute;
			AttributeType attributeType=configAttribute.getAttributeType();
			if(!attributeType.equals(AttributeType.group)){
				continue;
			}
			Group group=(Group)configAttribute.getMember();
			if(type.equals(GroupVoteType.group) && loginUser.getGroups()!=null){
				for(Group g:loginUser.getGroups()){
					if(group.getId().equals(g.getId())){
						if(configAttribute.isGranted()){
							return ACCESS_GRANTED;
						}else{
							return ACCESS_DENIED;
						}
					}
				}
			}else if(type.equals(GroupVoteType.user)){
				for(IUser user:group.getUsers()){
					if(user.getUsername().equals(loginUser.getUsername())){
						if(configAttribute.isGranted()){
							return ACCESS_GRANTED;
						}else{
							return ACCESS_DENIED;
						}
					}
				}
			}else if(type.equals(GroupVoteType.dept) && loginUser.getDepts()!=null){
				return decitionDepts(buildDeptSecurityConfigAttributes(group.getDepts(),configAttribute.isGranted()), loginUser.getDepts());
			}else if(type.equals(GroupVoteType.position) && loginUser.getPositions()!=null){
				for(IPosition p:group.getPositions()){
					for(IPosition userPosition:loginUser.getPositions()){
						if(p.getId().equals(userPosition.getId())){
							if(configAttribute.isGranted()){
								return ACCESS_GRANTED;
							}else{
								return ACCESS_DENIED;
							}
						}
					}
				}
			}
		}
		return ACCESS_ABSTAIN;
	}
	
	private List<ConfigAttribute> buildDeptSecurityConfigAttributes(List<IDept> depts,boolean granted){
		List<ConfigAttribute> list=new ArrayList<ConfigAttribute>();
		for(IDept dept:depts){
			SecurityConfigAttribute attr=new SecurityConfigAttribute(AttributeType.dept,granted,dept.getCompanyId());
			attr.setMember(dept);
			list.add(attr);
		}
		return list;
	}
}
enum GroupVoteType{
	group,user,dept,position
}