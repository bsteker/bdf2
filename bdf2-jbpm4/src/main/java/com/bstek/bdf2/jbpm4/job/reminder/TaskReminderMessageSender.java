package com.bstek.bdf2.jbpm4.job.reminder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.jbpm.api.task.Task;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.message.IMessageSender;
import com.bstek.bdf2.core.message.MessagePacket;
import com.bstek.bdf2.core.model.MessageTemplate;
import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.context.IJbpm4MessageVariableRegister;
import com.bstek.bdf2.jbpm4.service.IBpmService;

/**
 * @author Jacky.gao
 * @since 2013-3-29
 */
public class TaskReminderMessageSender extends Jbpm4HibernateDao implements InitializingBean{
	public static final String BEAN_ID="bdf2.jbpm4.taskReminderMessageSender";
	private Collection<IMessageSender> senders;
	private IBpmService bpmService;
	private UserDetailsService userDetailsService;
	public void sendReminderMessage(String taskId,String principal,String messageTemplateId,String[] senderIds){
		Session session=this.getSessionFactory().openSession();
		try{
			Task task=bpmService.findTaskById(taskId);
			if(task==null){
				return;
			}
			if(StringUtils.isEmpty(messageTemplateId)){
				throw new RuntimeException("Task ["+task.getName()+"] reminder was not define message template");
			}
			MessageTemplate messageTemplate=(MessageTemplate)session.get(MessageTemplate.class, messageTemplateId);
			if(messageTemplate==null){
				throw new RuntimeException("Task ["+task.getName()+"] reminder define message template ["+messageTemplateId+"] was not exist");
			}
			String title=messageTemplate.getName();
			List<String> titleVariables=this.getVariables(title);
			Map<String,String> variableMap=null;
			if(titleVariables.size()>0){
				variableMap=this.getVariableMap(task, principal);
				for(String key:titleVariables){
					key=key.substring(2,key.length()-1);
					String value=variableMap.get(key);
					if(StringUtils.isNotEmpty(value)){
						title=title.replaceAll("#\\{"+key+"\\}",value);
					}
				}
			}
			String content=messageTemplate.getContent();
			List<String> contentVariables=this.getVariables(content);
			if(contentVariables.size()>0){
				if(variableMap==null){
					variableMap=this.getVariableMap(task, principal);
				}
				for(String key:contentVariables){
					key=key.substring(2,key.length()-1);
					String value=variableMap.get(key);
					if(StringUtils.isNotEmpty(value)){
						content=content.replaceAll("#\\{"+key+"\\}",value);
					}
				}
			}
			MessagePacket message=new MessagePacket();
			message.setTitle(title);
			message.setContent(content);
			IUser user=(IUser)userDetailsService.loadUserByUsername(principal);
			List<IUser> toUsers=new ArrayList<IUser>();
			toUsers.add(user);
			message.setTo(toUsers);
			for(String senderId:senderIds){
				for(IMessageSender sender:senders){
					if(senderId.equals(sender.getSenderId())){
						sender.send(message);
					}
				}
			}
		}finally{
			session.flush();
			session.close();
		}		
	}
	
	private List<String> getVariables(String str){
		List<String> result=new ArrayList<String>();
		String regex="#\\{[^\\s\u4e00-\u9fa5][0-9a-zA-Z]([^\\s\u4e00-\u9fa5]*[0-9a-zA-Z])?\\}";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(str);
		while(matcher.find()){
			result.add(matcher.group());
		}
		return result;
	}
	
	public void afterPropertiesSet() throws Exception {
		Collection<UserDetailsService> userDetailsServices=this.getApplicationContext().getBeansOfType(UserDetailsService.class).values();
		if(userDetailsServices.size()==0){
			throw new RuntimeException("You must implementation["+UserDetailsService.class.getName()+"] interface and config it in spring context when use bdf2-jbpm4 module");
		}
		userDetailsService=userDetailsServices.iterator().next();
		senders=this.getApplicationContext().getBeansOfType(IMessageSender.class).values();
	}
	private Map<String,String> getVariableMap(Task task,String assignee){
		Map<String,String> map=new HashMap<String,String>();
		for(IJbpm4MessageVariableRegister reg:ContextHolder.getApplicationContext().getBeansOfType(IJbpm4MessageVariableRegister.class).values()){
			map.putAll(reg.fetchMessages(task, assignee));
		}
		return map;
	}
	public IBpmService getBpmService() {
		return bpmService;
	}
	public void setBpmService(IBpmService bpmService) {
		this.bpmService = bpmService;
	}
}
