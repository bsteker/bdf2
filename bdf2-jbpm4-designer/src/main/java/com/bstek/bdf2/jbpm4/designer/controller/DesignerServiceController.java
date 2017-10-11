package com.bstek.bdf2.jbpm4.designer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.Session;
import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.pvm.internal.repository.DeploymentImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.designer.converter.IConverter;
import com.bstek.bdf2.jbpm4.designer.converter.JpdlInfo;
import com.bstek.bdf2.jbpm4.designer.converter.impl.TransitionConverter;
import com.bstek.bdf2.jbpm4.designer.model.DesignerProcess;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.uploader.service.ILobStoreService;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-6-20
 */
public class DesignerServiceController extends AbstractResolver implements InitializingBean{
	private ILobStoreService lobStoreService;
	private Jbpm4HibernateDao hibernateDao;
	private IBpmService bpmService;
	private Collection<IConverter> converters;
	private String DIR="/onlineprocess";
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String xmlData=request.getParameter("xmlData");
		String svgData=request.getParameter("svgData");
		this.saveJpdl(xmlData);
		this.saveImage(svgData);
		String tempDir=ContextHolder.getBdfTempFileStorePath()+DIR;
		File xmlFile=new File(tempDir+"/process.jpdl.xml");
		File imgFile=new File(tempDir+"/process.png");
		String zipFile=tempDir+"/process.zip";
		this.createZipFile(zipFile, new File[]{xmlFile,imgFile});
		FileInputStream fin=new FileInputStream(zipFile);
		NewDeployment deployment=bpmService.deployProcess(fin);
		this.saveProcessDef(deployment, xmlData);
		File tempFile=new File(tempDir);
		for(File f:tempFile.listFiles()){
			f.deleteOnExit();
		}
		return null;
	}
	
	private void saveProcessDef(NewDeployment deployment,String xmlData) throws Exception{
		DeploymentImpl deploy=(DeploymentImpl)deployment;
		ProcessDefinition pd=(ProcessDefinition)deploy.getObjects().values().iterator().next();
		String deploymentId=pd.getDeploymentId();
		String name=pd.getName();
		int version=pd.getVersion();
		DesignerProcess process=new DesignerProcess();
		process.setDeploymentId(deploymentId);
		process.setName(name);
		process.setCreateDate(new Date());
		Session session=hibernateDao.getSessionFactory().openSession();
		try{
			process.setVersion(version);
			String lobId=lobStoreService.storeString(xmlData);
			process.setLobId(lobId);
			process.setId(UUID.randomUUID().toString());
			session.save(process);
		}finally{
			session.flush();
			session.close();
		}
	}
	
	private void createZipFile(String targetOutZipFileName, File[] inputFiles) throws Exception{
		ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(targetOutZipFileName));  
		for(File inputFile:inputFiles){
			zip(zos,inputFile);  				
		}
		zos.closeEntry();
		zos.close();		
    }  
    private void zip(ZipOutputStream zos, File file) throws Exception{
    	byte[] buffer = new byte[1024];
    	zos.putNextEntry(new ZipEntry(file.getName()));
    	FileInputStream in=new FileInputStream(file);  
    	int len;  
    	while((len=in.read(buffer))!=-1){  
    		zos.write(buffer,0,len);
    	}
    	in.close();
    }  
	
	private void saveImage(String svgData) throws Exception{
		PNGTranscoder coder=new PNGTranscoder();
		svgData="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+svgData;
		//ByteArrayInputStream fin=new ByteArrayInputStream(svgData.getBytes());
		StringReader reader=new StringReader(svgData);
		TranscoderInput input=new TranscoderInput(reader);
		String tempDir=ContextHolder.getBdfTempFileStorePath()+DIR;
		File f=new File(tempDir);
		if(!f.exists())f.mkdirs();
		FileOutputStream fout=new FileOutputStream(tempDir+"/process.png");
		TranscoderOutput output=new TranscoderOutput(fout);
		try{
			coder.transcode(input, output);			
		}finally{
			reader.close();
			//fin.close();
			fout.close();			
		}
	}
	
	private void saveJpdl(String xmlData) throws Exception{
		Document doc=DocumentHelper.parseText(xmlData);
		Element root=doc.getRootElement();
		Document document = DocumentHelper.createDocument();
		Element jpdlRootElement=document.addElement("process");
		jpdlRootElement.addAttribute("xmlns", "http://jbpm.org/4.4/jpdl");
		String name=root.attributeValue("name");
		String key=root.attributeValue("key");
		jpdlRootElement.addAttribute("name",name);
		if(StringUtils.isNotEmpty(key)){
			jpdlRootElement.addAttribute("key",key);			
		}
		List<JpdlInfo> transitions=new ArrayList<JpdlInfo>();
		List<JpdlInfo> nodes=new ArrayList<JpdlInfo>();
		for(Object obj:root.elements()){
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			IConverter target=null;
			String shapeId=ele.attributeValue("shapeId");
			for(IConverter converter:converters){
				if(converter.support(shapeId)){
					target=converter;
					break;
				}
			}
			if(target==null){
				continue;
			}
			if(target instanceof TransitionConverter){
				transitions.add(target.toJpdl(ele));
			}else{
				nodes.add(target.toJpdl(ele));
			}
		}
		for(JpdlInfo info:transitions){
			Element ele=info.getElement();
			String fromShapeLabel=ele.attributeValue("fromShapeLabel");
			Element parentElement=null;
			if(StringUtils.isNotEmpty(fromShapeLabel)){
				for(JpdlInfo nodeInfo:nodes){
					if(nodeInfo.getName().equals(fromShapeLabel)){
						parentElement=nodeInfo.getElement();
						break;
					}
				}
				ele.remove(ele.attribute("fromShapeLabel"));
			}
			if(parentElement!=null){
				parentElement.add(ele);
			}
		}
		for(JpdlInfo nodeInfo:nodes){
			jpdlRootElement.add(nodeInfo.getElement());
		}
		String tempDir=ContextHolder.getBdfTempFileStorePath()+DIR;
		File f=new File(tempDir);
		if(!f.exists())f.mkdirs();
		FileOutputStream out=new FileOutputStream(tempDir+"/process.jpdl.xml");
		OutputFormat formate=OutputFormat.createPrettyPrint();
		XMLWriter xmlWriter=new XMLWriter(out,formate);
		try{
			xmlWriter.write(document);		
		}finally{
			xmlWriter.close();
			out.close();
		}
	}
	
	public IBpmService getBpmService() {
		return bpmService;
	}

	public void setBpmService(IBpmService bpmService) {
		this.bpmService = bpmService;
	}

	public Jbpm4HibernateDao getHibernateDao() {
		return hibernateDao;
	}

	public void setHibernateDao(Jbpm4HibernateDao hibernateDao) {
		this.hibernateDao = hibernateDao;
	}

	public ILobStoreService getLobStoreService() {
		return lobStoreService;
	}

	public void setLobStoreService(ILobStoreService lobStoreService) {
		this.lobStoreService = lobStoreService;
	}

	public void afterPropertiesSet() throws Exception {
		converters=this.getApplicationContext().getBeansOfType(IConverter.class).values();
	}
}
