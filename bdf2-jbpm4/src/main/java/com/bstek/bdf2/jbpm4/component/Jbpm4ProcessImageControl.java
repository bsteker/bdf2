package com.bstek.bdf2.jbpm4.component;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;

@Widget(name = "Jbpm4ProcessImage",dependsPackage = "Jbpm4Process",category="BDF2")
@ClientObject(prototype = "dorado.widget.Jbpm4ProcessImage", shortTypeName = "Jbpm4ProcessImage")
@XmlNode(nodeName="Jbpm4ProcessImage")
public class Jbpm4ProcessImageControl extends Control {
	private String executionId;
	private String taskId;
	public Jbpm4ProcessImageControl() {
    }

    @ClientProperty()
    public String getExecutionId() {
    	return executionId;
    }
    
    public void setExecutionId(String executionId) {
    	this.executionId = executionId;
    }
	
	@ClientProperty()
	@IdeProperty(highlight=1)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
