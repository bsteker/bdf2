package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_entity_parameter:实体对象查询条件参数表
 */
@Entity
@Table(name = "bdf_r_entity_parameter")
public class BdfREntityParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * ENTITY_ID_:隶属实体对象ID
	 */
	public String entityId;

	/**
	 * PARAMETER_ID_:参数ID
	 */
	public String parameterId;

	public BdfREntityParameter() {
		super();
	}

	public BdfREntityParameter(String id, String entityId, String parameterId) {
		super();
		this.id = id;
		this.entityId = entityId;
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

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Column(name = "ENTITY_ID_", length = 50)
	public String getEntityId() {
		return entityId;
	}

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}

	@Column(name = "PARAMETER_ID_", length = 50)
	public String getParameterId() {
		return parameterId;
	}

	public String toString() {
		return "BdfREntityParameter [id=" + id + ",entityId=" + entityId
				+ ",parameterId=" + parameterId + "]";
	}

}
