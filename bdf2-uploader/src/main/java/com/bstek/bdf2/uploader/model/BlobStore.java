package com.bstek.bdf2.uploader.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-5-20
 */
@Entity
@Table(name="BDF2_BLOB_STORE")
public class BlobStore {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Lob
	@Column(name="CONTENT_")
	@Basic(fetch=FetchType.LAZY)
	private byte[] content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
}
