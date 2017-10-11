package com.bstek.bdf2.uploader.processor.impl;

import java.io.InputStream;

import com.bstek.bdf2.uploader.model.UploadDefinition;
import com.bstek.bdf2.uploader.processor.IFileProcessor;
import com.bstek.bdf2.uploader.service.ILobStoreService;

/**
 * @author Jacky.gao
 * @since 2013-5-20
 */
public class DatabaseFileProcessor implements IFileProcessor {
	private boolean disabled;
	private ILobStoreService lobStoreService;
	public void saveFile(UploadDefinition uploadDefinition,InputStream inputStream) {
		try {
			lobStoreService.storeBinaryStream(inputStream, inputStream.available(),uploadDefinition.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream loadFile(UploadDefinition uploadDefinition) {
		try {
			return lobStoreService.getBinaryStream(uploadDefinition.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteFile(UploadDefinition uploadDefinition) {
		try {
			lobStoreService.deleteBinaryStream(uploadDefinition.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String key() {
		return "Database";
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public ILobStoreService getLobStoreService() {
		return lobStoreService;
	}

	public void setLobStoreService(ILobStoreService lobStoreService) {
		this.lobStoreService = lobStoreService;
	}
}
