package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_action_def_parameter:动作参数关系表
 */
@Entity
@Table(name = "bdf_r_action_def_parameter")
public class BdfRActionDefParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * ACTION_DEF_ID_:所属动作ID
	 */
	public String actionDefId;

	/**
	 * PARAMETER_ID_:所属参数ID
	 */
	public String parameterId;

	public BdfRActionDefParameter() {
		super();
	}

	public BdfRActionDefParameter(String id, String actionDefId,
			String parameterId) {
		super();
		this.id = id;
		this.actionDefId = actionDefId;
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

	public void setActionDefId(String actionDefId) {
		this.actionDefId = actionDefId;
	}

	@Column(name = "ACTION_DEF_ID_", length = 50)
	public String getActionDefId() {
		return actionDefId;
	}

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}

	@Column(name = "PARAMETER_ID_", length = 50)
	public String getParameterId() {
		return parameterId;
	}

	public String toString() {
		return "BdfRActionDefParameter [id=" + id + ",actionDefId="
				+ actionDefId + ",parameterId=" + parameterId + "]";
	}

}
