package com.bstek.bdf2.core.message;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public interface IMessageSender {
	String getSenderId();
	String getSenderName();
	void send(MessagePacket message);
	boolean isDisabled();
}
