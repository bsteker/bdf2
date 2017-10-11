package com.bstek.bdf2.uploader.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-5-1
 */
@Entity
@Table(name="BDF2_UPLOAD_DEFINITION")
public class UploadDefinition  implements java.io.Serializable{
	private static final long serialVersionUID = -2654867904591057398L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="FILE_NAME_",length=60)
	private String fileName;
	@Column(name="SIZE_")
	private long size;
	@Column(name="CREATE_DATE_")
	private Date createDate;
	@Column(name="CREATE_USER_",length=60)
	private String createUser;
	@Column(name="UPLOAD_PROCESSOR_KEY_",length=60)
	private String uploadProcessorKey;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUploadProcessorKey() {
		return uploadProcessorKey;
	}
	public void setUploadProcessorKey(String uploadProcessorKey) {
		this.uploadProcessorKey = uploadProcessorKey;
	}
}
