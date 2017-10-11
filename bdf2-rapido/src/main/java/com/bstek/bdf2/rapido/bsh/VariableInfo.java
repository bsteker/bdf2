package com.bstek.bdf2.rapido.bsh;

public class VariableInfo {
	private String name;
	private String desc;
	private VariableExecutor variableExecutor;
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
	public VariableExecutor getVariableExecutor() {
		return variableExecutor;
	}
	public void setVariableExecutor(VariableExecutor variableExecutor) {
		this.variableExecutor = variableExecutor;
	}
	
}
