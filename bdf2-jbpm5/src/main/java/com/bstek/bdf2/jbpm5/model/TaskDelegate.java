package com.bstek.bdf2.jbpm5.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-4-10
 */
@Entity
@Table(name="JBPM5_TASK_DELEGATE")
@SequenceGenerator(name="taskDelegateSeqId",sequenceName="JBPM5_TASK_DELEGATE_ID_SEQ",allocationSize=1)
public class TaskDelegate {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="taskDelegateSeqId")
	@Column(name="ID_")
	private long id;
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER,targetEntity=Task.class)
	@JoinColumn(name="TASK_ID_")
	private Task task;
	@Column(name="TASK_OWNER_",length=60)
	private String taskOwner;
	@Column(name="DELEGATOR_",length=60)
	private String delegator;
	@Column(name="CREATE_DATE_")
	private Date createDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public String getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}
	public String getDelegator() {
		return delegator;
	}
	public void setDelegator(String delegator) {
		this.delegator = delegator;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
