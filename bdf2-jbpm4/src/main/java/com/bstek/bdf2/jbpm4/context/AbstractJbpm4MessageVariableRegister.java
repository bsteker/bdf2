package com.bstek.bdf2.jbpm4.context;


/**
 * @author Jacky.gao
 * @since 2013-3-25
 */
public abstract class AbstractJbpm4MessageVariableRegister implements IJbpm4MessageVariableRegister {
	public String getTypeId() {
		return TYPE;
	}
	public String getTypeName() {
		return "jBPM4消息模版";
	}
}
