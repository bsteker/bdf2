package com.bstek.bdf2.rapido.common;


/**
 * 注册Velocity上下文变量，以供Rapido动态SQL拼装时使用
 * @author jacky.gao@bstek.com
 * @since 2012-10-22
 */
public interface VelocityVariableRegister {
	/**
	 * @return 返回变量名
	 */
	String getVariableName();
	/**
	 * @return 返回变量描述
	 */
	String getVariableDesc();
	/**
	 * @return 返回具体变量对象
	 */
	Object getVariable();
}
