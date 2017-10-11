package com.bstek.bdf2.core.message.impl;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.dom4j.IllegalAddException;
import org.hibernate.Session;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.message.IMessageSender;
import com.bstek.bdf2.core.message.MessagePacket;
import com.bstek.bdf2.core.model.Message;
import com.bstek.bdf2.core.orm.hibernate.HibernateDao;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public class InternalMessageSender extends HibernateDao implements IMessageSender {
	public static final String BEAN_ID="bdf2.internalMessageSender";
	private boolean diableInternalMessageSender;
	public void send(MessagePacket message) {
		Collection<IUser> toUsers=message.getTo();
		if(toUsers==null || toUsers.size()==0){
			throw new IllegalAddException("You must define at least one message recipient!");
		}
		Collection<IUser> ccUsers=message.getCc();
		if(ccUsers!=null && ccUsers.size()>0){
			toUsers.addAll(ccUsers);
		}
		Session session=this.getSessionFactory().openSession();
		try{
			for(IUser user:toUsers){
				Message msg=new Message();
				msg.setId(UUID.randomUUID().toString());
				msg.setSendDate(new Date());
				msg.setSender(message.getSender().getUsername());
				msg.setReply(false);
				msg.setRead(false);
				msg.setContent(message.getContent());
				msg.setTitle(message.getTitle());
				msg.setReceiver(user.getUsername());
				session.save(msg);
			}
		}finally{
			session.flush();
			session.close();
		}
	}

	public String getSenderId() {
		return "internal-message";
	}

	public String getSenderName() {
		return "站内消息";
	}
	public boolean isDisabled(){
		return diableInternalMessageSender;
	}
	public void setDiableInternalMessageSender(boolean diableInternalMessageSender) {
		this.diableInternalMessageSender = diableInternalMessageSender;
	}
}
