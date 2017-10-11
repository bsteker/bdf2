package com.bstek.bdf2.core.message;

import java.util.Collection;

import com.bstek.bdf2.core.business.IUser;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public class MessagePacket {
	private String title;
	private String content;
	private Collection<IUser> to;
	private Collection<IUser> cc;
	private IUser sender;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Collection<IUser> getTo() {
		return to;
	}
	public void setTo(Collection<IUser> to) {
		this.to = to;
	}
	public Collection<IUser> getCc() {
		return cc;
	}
	public void setCc(Collection<IUser> cc) {
		this.cc = cc;
	}
	public IUser getSender() {
		return sender;
	}
	public void setSender(IUser sender) {
		this.sender = sender;
	}
}
