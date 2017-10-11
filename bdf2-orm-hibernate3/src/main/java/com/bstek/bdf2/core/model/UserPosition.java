package com.bstek.bdf2.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @since 2013-1-26
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_USER_POSITION")
public class UserPosition  implements java.io.Serializable{
	private static final long serialVersionUID = -3018697241446594713L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="USERNAME_",length=60,nullable=false)
	private String username;
	@Column(name="POSITION_ID_",length=60,nullable=false)
	private String positionId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
}
