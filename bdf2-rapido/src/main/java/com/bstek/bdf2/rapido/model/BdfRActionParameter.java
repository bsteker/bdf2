package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_action_parameter:具体动作参数关系表
 */
@Entity
@Table(name = "bdf_r_action_parameter")
public class BdfRActionParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * ACTION_ID_:具体动作ID
	 */
	public String actionId;

	/**
	 * PARAMETER_ID_:参数ID
	 */
	public String parameterId;

	public BdfRActionParameter() {
		super();
	}

	public BdfRActionParameter(String id, String actionId, String parameterId) {
		super();
		this.id = id;
		this.actionId = actionId;
		this.parameterId = parameterId;
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

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}

	@Column(name = "PARAMETER_ID_", length = 50)
	public String getParameterId() {
		return parameterId;
	}

	public String toString() {
		return "BdfRActionParameter [id=" + id + ",actionId=" + actionId
				+ ",parameterId=" + parameterId + "]";
	}

}
