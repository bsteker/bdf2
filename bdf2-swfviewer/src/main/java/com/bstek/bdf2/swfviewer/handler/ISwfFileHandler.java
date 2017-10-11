package com.bstek.bdf2.swfviewer.handler;

import java.io.File;
import java.util.Map;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public interface ISwfFileHandler {

	public String getHandlerName();

	public String getHandlerDesc();

	public File execute(Map<String, Object> parameter) throws Exception;
}
