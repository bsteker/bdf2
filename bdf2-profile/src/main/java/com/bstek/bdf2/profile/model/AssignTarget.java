package com.bstek.bdf2.profile.model;

/**
 * @author Jacky.gao
 * @since 2013-2-26
 */
public class AssignTarget  implements java.io.Serializable{
	private static final long serialVersionUID = 4403803394542164953L;
	private String id;
	private String name;
	private String desc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
