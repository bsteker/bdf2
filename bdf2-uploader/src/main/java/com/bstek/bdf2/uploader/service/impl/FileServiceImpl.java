package com.bstek.bdf2.uploader.service.impl;

import java.io.InputStream;
import java.util.Collection;

import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.uploader.UploaderHibernateDao;
import com.bstek.bdf2.uploader.model.UploadDefinition;
import com.bstek.bdf2.uploader.processor.IFileProcessor;
import com.bstek.bdf2.uploader.service.IFileService;

/**
 * @author Jacky.gao
 * @since 2013-5-12
 */
public class FileServiceImpl extends UploaderHibernateDao implements IFileService,InitializingBean{
	private Collection<IFileProcessor> processors;
	public UploadDefinition getUploadDefinition(String id) {
		Session session=this.getSessionFactory().openSession();
		try{
			return (UploadDefinition)session.get(UploadDefinition.class, id);
		}finally{
			session.close();
		}
	}

	public InputStream getFile(UploadDefinition definition) {
		if(StringUtils.isEmpty(definition.getUploadProcessorKey())){
			throw new RuntimeException("Upload definition ["+definition.getId()+"] has not set processor!");
		}
		String key=definition.getUploadProcessorKey();
		IFileProcessor targetProcessor=null;
		for(IFileProcessor processor:processors){
			if(processor.key().equals(key)){
				targetProcessor=processor;
				break;
			}
		}
		if(targetProcessor==null){
			throw new RuntimeException("The processor ["+key+"] is not exist!");
		}
		InputStream in = targetProcessor.loadFile(definition);
		return in;
	}
	

	public InputStream getFile(String id) {
		Session session=this.getSessionFactory().openSession();
		try{
			UploadDefinition definition= (UploadDefinition)session.get(UploadDefinition.class, id);
			return this.getFile(definition);
		}finally{
			session.close();
		}
	}

	public void deleteUploadDefinition(String id) {
		Session session=this.getSessionFactory().openSession();
		try{
			UploadDefinition definition=(UploadDefinition)session.get(UploadDefinition.class, id);
			if(definition==null){
				throw new RuntimeException("Upload definition ["+id+"] is not exist!");
			}
			if(StringUtils.isEmpty(definition.getUploadProcessorKey())){
				throw new RuntimeException("Upload definition ["+definition.getId()+"] has not set processor!");
			}
			String key=definition.getUploadProcessorKey();
			IFileProcessor targetProcessor=null;
			for(IFileProcessor processor:processors){
				if(processor.key().equals(key)){
					targetProcessor=processor;
					break;
				}
			}
			if(targetProcessor==null){
				throw new RuntimeException("The processor ["+key+"] is not exist!");
			}
			targetProcessor.deleteFile(definition);
			session.delete(definition);
		}finally{
			session.flush();
			session.close();
		}
	}

	public void afterPropertiesSet() throws Exception {
		processors=this.getApplicationContext().getBeansOfType(IFileProcessor.class).values();
	}
}
