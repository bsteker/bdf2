package com.bstek.bdf2.jbpm5.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-3-25
 */
@Entity
@Table(name="JBPM5_TASK_CONFIG")
@SequenceGenerator(name="taskConfigSeqId",sequenceName="JBPM5_TASK_CONFIG_ID_SEQ")
public class TaskConfig {
	@Id
	@Column(name="ID_")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="taskConfigSeqId")
	private long id;
	@Column(name="PROCESS_ID_")
	private String processId;
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	@Column(name="URL_",length=120)
	private String url;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
