package com.bstek.bdf2.uploader.processor.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import com.bstek.bdf2.uploader.model.UploadDefinition;
import com.bstek.bdf2.uploader.processor.IFileProcessor;

/**
 * @author Jacky.gao
 * @since 2013-5-1
 */
public class LocalDirectoryFileProcessor implements IFileProcessor,ApplicationContextAware{
	private Log log=LogFactory.getLog(LocalDirectoryFileProcessor.class);
	private boolean disabled;
	private String localDirectory="uploadfiles";
	private String storageMethod="month";//有三个可选值：day,month,year
	private String fileNameStorageMethod="uuid";//有三个可选值：uuid,realName,hybrid
	public void saveFile(UploadDefinition uploadDefinition, InputStream inputStream) {
		String storagePath=getFullStoragePath(uploadDefinition);
		File file=new File(storagePath);
		if(!file.exists()){
			file.mkdirs();
		}
		String targetFile=storagePath+getFileName(uploadDefinition);
		System.out.println(targetFile);
		FileOutputStream outputStream=null;
		try {
			outputStream=new FileOutputStream(targetFile);
			IOUtils.copy(inputStream, outputStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			try {
				outputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public InputStream loadFile(UploadDefinition uploadDefinition) {
		String fullFileName=getFullStoragePath(uploadDefinition)+getFileName(uploadDefinition);
		try {
			FileInputStream fin=new FileInputStream(fullFileName);
			return fin;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteFile(UploadDefinition uploadDefinition) {
		String fullFileName=getFullStoragePath(uploadDefinition)+getFileName(uploadDefinition);
		File file=new File(fullFileName);
		if(file.exists()){
			file.delete();
		}else{
			log.warn("File ["+fullFileName+" is not exist!]");
		}
	}

	public String key() {
		return "LocalDirectory";
	}

	private String getFullStoragePath(UploadDefinition uploadDefinition){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(uploadDefinition.getCreateDate());
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		String storagePath=localDirectory;
		if(storageMethod.equals("day")){
			storagePath+="/"+year+"/"+month+"/"+day+"/";
		}
		if(storageMethod.equals("month")){
			storagePath+="/"+year+"/"+month+"/";
		}
		if(storageMethod.equals("year")){
			storagePath+="/"+year+"/";
		}
		return storagePath;
	}
	
	private String getFileName(UploadDefinition uploadDefinition){
		String fileName=null;
		if(fileNameStorageMethod.equals("hybrid")){
			fileName=uploadDefinition.getId()+"-"+uploadDefinition.getFileName();
		}
		if(fileNameStorageMethod.equals("uuid")){
			fileName=uploadDefinition.getId();
		}
		if(fileNameStorageMethod.equals("realName")){
			fileName=uploadDefinition.getFileName();
		}
		return fileName;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(!storageMethod.equals("day") && !storageMethod.equals("month") && !storageMethod.equals("year")){
			throw new IllegalArgumentException("[storageMethod] property in "+LocalDirectoryFileProcessor.class.getName()+" can only use these three values:day/month/year");
		}
		File file=new File(localDirectory);
		if(!file.exists()){
			WebApplicationContext wac=(WebApplicationContext)applicationContext;
			if(localDirectory.startsWith("/")){
				localDirectory="/WEB-INF"+localDirectory;				
			}else{
				localDirectory="/WEB-INF/"+localDirectory;								
			}
			String realPath=wac.getServletContext().getRealPath(localDirectory);
			try{
				File f=new File(realPath);
				if(!f.exists()){
					f.mkdirs();
				}
			}catch(Exception ex){
				throw new IllegalArgumentException("The directory [+localDirectory+] is not exist!");
			}
			localDirectory=realPath;
		}
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void setLocalDirectory(String localDirectory) {
		this.localDirectory = localDirectory;
	}

	public void setStorageMethod(String storageMethod) {
		this.storageMethod = storageMethod;
	}

	public void setFileNameStorageMethod(String fileNameStorageMethod) {
		this.fileNameStorageMethod = fileNameStorageMethod;
	}

}
