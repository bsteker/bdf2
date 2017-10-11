package com.bstek.bdf2.core.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky
 *
 */
@Entity
@Table(name="BDF2_MESSAGE")
public class Message  implements java.io.Serializable{
	private static final long serialVersionUID = -6276286440146124045L;

	@Id
	@Column(name="ID_",length=60)
	private String id;
	
	@Column(name="TITLE_",length=60,nullable=false)
	private String title;
	
	@Column(name="CONTENT_",length=1000,nullable=false)
	private String content;
	
	@Column(name="SEND_DATE_")
	private Date sendDate;
	
	@Column(name="SENDER_",length=60,nullable=false)
	private String sender;
	
	@Column(name="RECEIVER_",length=60,nullable=false)
	private String receiver;
	
	//是否已读
	@Column(name="READ_",nullable=false)
	private boolean read;
	
	//是否为回复的消息
	@Column(name="REPLY_")
	private boolean reply;

	//消息分类标签
	@Column(name="TAGS_",length=100)
	private String tags;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isReply() {
		return reply;
	}

	public void setReply(boolean reply) {
		this.reply = reply;
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
