package com.bstek.bdf2.core.view.frame.main.register.message;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Session;
import org.springframework.stereotype.Component;


import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Message;
import com.bstek.dorado.annotation.DataResolver;

/**
 * @author Jacky.gao
 * @since 2013-2-5
 */
@Component("bdf2.sendMessage")
public class SendMessage extends CoreHibernateDao{
	@DataResolver
	
	public void send(Message message) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first");
		}
		String receiver=message.getReceiver();
		String[] receivers=receiver.split(",");
		Session session=this.getSessionFactory().openSession();
		try{
			for(int i=0;i<receivers.length;i++){
				Message msg=new Message();
				msg.setId(UUID.randomUUID().toString());
				msg.setSendDate(new Date());
				msg.setSender(user.getUsername());
				msg.setReply(false);
				msg.setRead(false);
				msg.setContent(message.getContent());
				msg.setTitle(message.getTitle());
				msg.setReceiver(receivers[i]);
				session.save(msg);
			}
		}finally{
			session.flush();
			session.close();
		}
	}
}
