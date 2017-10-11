package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_action_def_relation:动作定义关系表
 */
@Entity
@Table(name = "bdf_r_action_def_relation")
public class BdfRActionDefRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * ACTION_ID_:动作ID
	 */
	public String actionId;

	/**
	 * ACTION_DEF_ID_:动作定义ID
	 */
	public String actionDefId;

	public BdfRActionDefRelation() {
		super();
	}

	public BdfRActionDefRelation(String id, String actionId, String actionDefId) {
		super();
		this.id = id;
		this.actionId = actionId;
		this.actionDefId = actionDefId;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	public String getId() {
		return id;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	@Column(name = "ACTION_ID_", length = 50)
	public String getActionId() {
		return actionId;
	}

	public void setActionDefId(String actionDefId) {
		this.actionDefId = actionDefId;
	}

	@Column(name = "ACTION_DEF_ID_", length = 50)
	public String getActionDefId() {
		return actionDefId;
	}

	public String toString() {
		return "BdfRActionDefRelation [id=" + id + ",actionId=" + actionId
				+ ",actionDefId=" + actionDefId + "]";
	}

}
