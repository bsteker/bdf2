package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_action_def:目前只支持两种类型的动作：ajaxAction及updateAction，分别对应d7中的两种类型的action
 */
@Entity
@Table(name = "bdf_r_action_def")
public class BdfRActionDef implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:动作名称
	 */
	public String name;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * TYPE_:两种类型A(ajax)及U(update)
	 */
	public String type;

	/**
	 * SCRIPT_:动作脚本
	 */
	public String script;

	/**
	 * ENTITY_ID_:动作涉及到的实体对象
	 */
	public String entityId;

	/**
	 * ASYNC_:是否采用异步执行
	 */
	public String async;

	/**
	 * CONFIRM_MESSAGE_:执行前确认消息
	 */
	public String confirmMessage;

	/**
	 * SUCCESS_MESSAGE_:执行成功后消息
	 */
	public String successMessage;

	/**
	 * BEFORE_EXECUTE_SCRIPT_:执行动作前的事件脚本
	 */
	public String beforeExecuteScript;

	/**
	 * ON_SUCCESS_SCRIPT_:执行动作成功后的事件脚本
	 */
	public String onSuccessScript;

	/**
	 * PACKAGE_ID_:所在包
	 */
	public String packageId;

	public BdfRActionDef() {
		super();
	}

	public BdfRActionDef(String id, String name, String desc, String type,
			String script, String entityId, String async,
			String confirmMessage, String successMessage,
			String beforeExecuteScript, String onSuccessScript, String packageId) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.script = script;
		this.entityId = entityId;
		this.async = async;
		this.confirmMessage = confirmMessage;
		this.successMessage = successMessage;
		this.beforeExecuteScript = beforeExecuteScript;
		this.onSuccessScript = onSuccessScript;
		this.packageId = packageId;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "NAME_", length = 50)
	public String getName() {
		return name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "TYPE_", length = 10)
	public String getType() {
		return type;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Column(name = "SCRIPT_", length = 1000)
	public String getScript() {
		return script;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Column(name = "ENTITY_ID_", length = 50)
	public String getEntityId() {
		return entityId;
	}

	public void setAsync(String async) {
		this.async = async;
	}

	@Column(name = "ASYNC_", length = 1)
	public String getAsync() {
		return async;
	}

	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	@Column(name = "CONFIRM_MESSAGE_", length = 100)
	public String getConfirmMessage() {
		return confirmMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	@Column(name = "SUCCESS_MESSAGE_", length = 100)
	public String getSuccessMessage() {
		return successMessage;
	}

	public void setBeforeExecuteScript(String beforeExecuteScript) {
		this.beforeExecuteScript = beforeExecuteScript;
	}

	@Column(name = "BEFORE_EXECUTE_SCRIPT_", length = 1000)
	public String getBeforeExecuteScript() {
		return beforeExecuteScript;
	}

	public void setOnSuccessScript(String onSuccessScript) {
		this.onSuccessScript = onSuccessScript;
	}

	@Column(name = "ON_SUCCESS_SCRIPT_", length = 1000)
	public String getOnSuccessScript() {
		return onSuccessScript;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Column(name = "PACKAGE_ID_", length = 50)
	public String getPackageId() {
		return packageId;
	}

	public String toString() {
		return "BdfRActionDef [id=" + id + ",name=" + name + ",desc=" + desc
				+ ",type=" + type + ",script=" + script + ",entityId="
				+ entityId + ",async=" + async + ",confirmMessage="
				+ confirmMessage + ",successMessage=" + successMessage
				+ ",beforeExecuteScript=" + beforeExecuteScript
				+ ",onSuccessScript=" + onSuccessScript + ",packageId="
				+ packageId + "]";
	}

}
