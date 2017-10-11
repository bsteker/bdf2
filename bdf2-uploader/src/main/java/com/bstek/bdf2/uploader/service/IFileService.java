package com.bstek.bdf2.uploader.service;

import java.io.InputStream;

import com.bstek.bdf2.uploader.model.UploadDefinition;

/**
 * @author Jacky.gao
 * @since 2013-5-12
 */
public interface IFileService {
	public static final String BEAN_ID="bdf2.uploader.fileService";
	UploadDefinition getUploadDefinition(String id);
	InputStream getFile(UploadDefinition definition);
	InputStream getFile(String id);
	void deleteUploadDefinition(String id);
}
