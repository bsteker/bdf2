package com.bstek.bdf2.jbpm5.model;

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


@Entity(name="JBPM5_TASK_GROUP_CANDIDATE")
@SequenceGenerator(name="candidateSeqId",sequenceName="JBPM5_GROUP_CANDIDATE_ID_SEQ",allocationSize=1)
public class TaskGroupCandidate {
	@Id
	@Column(name="ID_")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="candidateSeqId")
	private long id;
	
	@Column(name="GROUP_ID_")
	private String groupId;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="TASK_ID_")
	private Task task;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
}
