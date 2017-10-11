package com.bstek.bdf2.core.model;

/**
 * @author Jacky.gao
 * @since 2013-3-24
 */
public class MessageVariable  implements java.io.Serializable{
	private static final long serialVersionUID = 5488029673860165021L;
	private String name;
	private String desc;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
