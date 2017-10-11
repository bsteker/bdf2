package com.bstek.bdf2.uploader.component;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Jacky.gao
 * @since 2013-5-3
 */
@Widget(name = "RichUploader",dependsPackage = "RichUploader",category="BDF2")
@ClientObject(prototype = "dorado.widget.RichUploader", shortTypeName = "RichUploader")
@XmlNode(nodeName="RichUploader")
@ClientEvents({
	@ClientEvent(name="onSelect"),
	@ClientEvent(name="beforeSubmit"),
	@ClientEvent(name="onSuccess"),
	@ClientEvent(name="onFail"),
})
public class RichUploaderControl extends Control{
	private String processor;
	private String caption;
	private boolean autoSubmit=true;
	private boolean disabled;
	private String allowFileTypes;
	private long allowMaxFileSize;
	@ClientProperty
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}

	@ClientProperty
	@IdeProperty(highlight=1)
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	@ClientProperty(escapeValue="true")
	public boolean isAutoSubmit() {
		return autoSubmit;
	}
	public void setAutoSubmit(boolean autoSubmit) {
		this.autoSubmit = autoSubmit;
	}
	@ClientProperty(escapeValue="true")
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	@ClientProperty
	public String getAllowFileTypes() {
		return allowFileTypes;
	}
	public void setAllowFileTypes(String allowFileTypes) {
		this.allowFileTypes = allowFileTypes;
	}
	@ClientProperty
	public long getAllowMaxFileSize() {
		return allowMaxFileSize;
	}
	public void setAllowMaxFileSize(long allowMaxFileSize) {
		this.allowMaxFileSize = allowMaxFileSize;
	}
}
