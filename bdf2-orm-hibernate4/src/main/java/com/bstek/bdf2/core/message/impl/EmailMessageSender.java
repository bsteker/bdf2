package com.bstek.bdf2.core.message.impl;

import com.bstek.bdf2.core.message.EmailSender;
import com.bstek.bdf2.core.message.IMessageSender;
import com.bstek.bdf2.core.message.MessagePacket;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public class EmailMessageSender implements IMessageSender {
	private EmailSender emailSender;
	private boolean diableEmailMessageSender;
	public void send(MessagePacket message) {
		try {
			emailSender.sendMail(message, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public EmailSender getEmailSender() {
		return emailSender;
	}
	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}
	public String getSenderId() {
		return "email";
	}
	public String getSenderName() {
		return "发送邮件";
	}
	public boolean isDisabled(){
		return diableEmailMessageSender;
	}
	public void setDiableEmailMessageSender(boolean diableEmailMessageSender) {
		this.diableEmailMessageSender = diableEmailMessageSender;
	}
}
