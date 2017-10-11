package com.bstek.bdf2.wizard.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bstek.dorado.core.Configure;

@Service(DownloadCounter.BEAN_ID)
public class DownloadCounter implements InitializingBean {

	public static final String BEAN_ID = "bdf2.wizard.downloadCounter";
	public static final String COUNT_PRPERTY_NAME = "downloadCount";
	public static final String FILE_NAME = "bdf2-wizard.properties";

	private AtomicInteger downloadCounter = new AtomicInteger(Integer.parseInt(Configure.getString("bdf2.wizard.initDownloadSize")));
	
	private Properties properties = new Properties();

	public int incrementAndGet() {
		int num = downloadCounter.incrementAndGet();
		this.storeProperties();
		return num;
	}

	public int get() {
		return downloadCounter.get();
	}

	private File getConfigFile() {
		return new File(System.getProperty("user.home"), FILE_NAME);
	}

	private void init() {
		File tempFile = this.getConfigFile();
		if (tempFile.exists()) {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(tempFile);
				properties.load(fileInputStream);
				String downloadCount = properties.getProperty(COUNT_PRPERTY_NAME);
				if (StringUtils.hasText(downloadCount)) {
					downloadCounter = new AtomicInteger(Integer.valueOf(downloadCount));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(fileInputStream);
			}
		}
	}

	private void storeProperties() {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(this.getConfigFile());
			properties.setProperty(COUNT_PRPERTY_NAME, String.valueOf(this.get()));
			properties.store(fileOutputStream, COUNT_PRPERTY_NAME);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fileOutputStream);
		}
	}

	public void afterPropertiesSet() throws Exception {
		this.init();
	}

}
