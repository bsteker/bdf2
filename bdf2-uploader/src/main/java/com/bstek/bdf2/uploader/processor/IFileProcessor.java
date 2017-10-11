package com.bstek.bdf2.uploader.processor;

import java.io.InputStream;

import com.bstek.bdf2.uploader.model.UploadDefinition;

/**
 * @author Jacky.gao
 * @since 2013-5-1
 */
public interface IFileProcessor {
	/**
	 * 保存上传的文件
	 * @param uploadDefinition 可以从这个对象中取到上传文件的ID、名称、大小等信息
	 * @param inputStream 上传文件的流对象
	 */
	void saveFile(UploadDefinition uploadDefinition,InputStream inputStream);
	/**
	 * 根据给出的文件上传对象，返回对应的文件流
	 * @param uploadDefinition 文件上传对象
	 * @return 要取回的文件流
	 */
	InputStream loadFile(UploadDefinition uploadDefinition);
	/**
	 * 根据文件上传对象，删除对应的文件
	 * @param uploadDefinition 文件上传对象
	 */
	void deleteFile(UploadDefinition uploadDefinition);
	/**
	 * @return 返回当前处理器的ID
	 */
	String key();
	/**
	 * @return 是否禁用当前处理器
	 */
	boolean isDisabled();
}
