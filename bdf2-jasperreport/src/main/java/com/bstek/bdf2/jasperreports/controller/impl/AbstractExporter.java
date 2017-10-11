package com.bstek.bdf2.jasperreports.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.orm.DataSourceRepository;
import com.bstek.bdf2.jasperreports.controller.IExporter;
import com.bstek.bdf2.jasperreports.model.DataSourceType;
import com.bstek.bdf2.jasperreports.model.ReportDefinition;
import com.bstek.bdf2.jasperreports.model.ReportParameter;
import com.bstek.bdf2.jasperreports.model.ReportResource;
import com.bstek.bdf2.jasperreports.service.IReportDataProvider;
import com.bstek.bdf2.jasperreports.service.IReportDefinitionService;
import com.bstek.bdf2.uploader.model.UploadDefinition;
import com.bstek.bdf2.uploader.service.IFileService;

/**
 * @author Jacky.gao
 * @since 2013-5-11
 */
public abstract class AbstractExporter implements IExporter {
	protected JasperPrint getJasperPrint(HttpServletRequest req) throws Exception{
		String tmpDir=null;
		String reportFile=req.getParameter(REPORT_FILE_PARAMETER_KEY);
		if(StringUtils.isEmpty(reportFile)){
			throw new RuntimeException("Report file can not be null!");
		}
		String fileSource=req.getParameter(FILE_SOURCE_PARAMETER_KEY);
		if(StringUtils.isEmpty(reportFile)){
			throw new RuntimeException("File source can not be null!");
		}
		FileSource fs=FileSource.valueOf(fileSource);
		String dt=req.getParameter(DATASOURCE_TYPE_PARAMETER_KEY);
		DataSourceType dataSourceType=(dt!=null)?DataSourceType.valueOf(dt):null;
		String dataSourceProvider=req.getParameter(DATASOURCE_PROVIDER_PARAMETER_KEY);
		InputStream inputStream=null;
		Connection connection=null;
		JRDataSource jrDataSource=null;
		try{
			Map<String,Object> parameters=new HashMap<String,Object>();
			String reportParameters=req.getParameter(REPORT_PARAMETERS_PARAMETER_KEY);
			if(StringUtils.isNotEmpty(reportParameters)){
				reportParameters=URLDecoder.decode(reportParameters,"utf-8");
				for(String p:reportParameters.split(";")){
					String[] param=p.split("=====");
					if(param.length==2){
						parameters.put(param[0],param[1]);
					}
				}
			}
			if(fs.equals(FileSource.file)){
				File f=new File(reportFile);
				if(!f.exists()){
					String realReportFilePath=req.getSession().getServletContext().getRealPath(reportFile);
					if(realReportFilePath==null){
						throw new IllegalArgumentException("Report file ["+reportFile+"] is not exist!");						
					}
					f=new File(realReportFilePath);
					if(!f.exists()){
						throw new IllegalArgumentException("Report file ["+reportFile+"] is not exist!");
					}			
				}
				inputStream=new FileInputStream(f);
			}else if(fs.equals(FileSource.uploadedFile)){
				IReportDefinitionService reportDefinitionService=ContextHolder.getBean(IReportDefinitionService.BEAN_ID);
				ReportDefinition definition=reportDefinitionService.loadReportDefinition(reportFile);
				if(definition==null){
					throw new IllegalArgumentException("Uploaded file ["+reportFile+"] is not exist!");
				}
				dataSourceType=definition.getDataSourceType();
				dataSourceProvider=definition.getDataSource();
				for(ReportParameter reportParameter:reportDefinitionService.loadReportParameter(definition.getId())){
					parameters.put(reportParameter.getName(),reportParameter.getValue());
				}
				IFileService fileService=ContextHolder.getBean(IFileService.BEAN_ID);
				tmpDir=ContextHolder.getBdfTempFileStorePath()+"jasperreports/"+definition.getName()+"/";
				File f=new File(tmpDir);
				if(!f.exists()){
					f.mkdirs();
				}
				for(ReportResource res:reportDefinitionService.loadReportResouces(definition.getId())){
					String name=res.getName();
					UploadDefinition uploadDefinition=fileService.getUploadDefinition(res.getResourceFile());
					String tmpResourceFile=tmpDir+uploadDefinition.getFileName();
					InputStream fin=null;
					FileOutputStream fout=null;
					try{
						fin=fileService.getFile(uploadDefinition);
						fout=new FileOutputStream(tmpResourceFile);
						IOUtils.copy(fin, fout);
					}finally{
						IOUtils.closeQuietly(fout);
						IOUtils.closeQuietly(fin);
					}
					parameters.put(name, tmpResourceFile);
				}
				inputStream=fileService.getFile(definition.getReportFile());
			}else{
				throw new IllegalArgumentException("Unsupport current report file type ["+fileSource+"]");
			}
			if(dataSourceType.equals(DataSourceType.Jdbc)){
				connection=this.getConnection(dataSourceProvider);
			}else if(dataSourceType.equals(DataSourceType.JavaBean)){
				jrDataSource=new JRBeanArrayDataSource(this.getReportDataProvider(dataSourceProvider).getData().toArray());
			}else if(dataSourceType.equals(DataSourceType.Map)){
				jrDataSource=new JRMapArrayDataSource(this.getReportDataProvider(dataSourceProvider).getData().toArray());				
			}else{
				throw new IllegalArgumentException("Unsupport current data source type ["+dataSourceType+"]");
			}
			JasperPrint jasperPrint = null;
			String cache=req.getParameter(CACHE_PARAMETER_KEY);
			if(cache!=null && cache.equals("true")){
				parameters.put(JRParameter.REPORT_VIRTUALIZER, new JRFileVirtualizer(10,ContextHolder.getBdfTempFileStorePath()));				
			}
			if(connection!=null){
				jasperPrint = JasperFillManager.fillReport(inputStream, parameters,connection);				
			}else if(jrDataSource!=null){
				jasperPrint = JasperFillManager.fillReport(inputStream, parameters,jrDataSource);								
			}else{
				throw new IllegalArgumentException("You need define at least one report data source!");
			}
			return jasperPrint;
		}finally{
			if(inputStream!=null){
				inputStream.close();
			}
			if(connection!=null){
				connection.close();
			}
			if(tmpDir!=null){
				File f=new File(tmpDir);
				if(f.exists()){
					for(File childFile:f.listFiles()){
						childFile.deleteOnExit();
					}
					f.delete();
				}
			}
		}
	}
	
	protected String getExportFileName(HttpServletRequest req) throws UnsupportedEncodingException{
		String fileName=req.getParameter("exportFileName");
		if(StringUtils.isEmpty(fileName)){
			return "report";
		}else{
			return URLDecoder.decode(fileName,"utf-8");
		}
	}
	
	private IReportDataProvider getReportDataProvider(String beanId){
		return ContextHolder.getBean(beanId);
	}
	
	private Connection getConnection(String dataSourceName){
		Collection<DataSourceRepository> res=ContextHolder.getApplicationContext().getBeansOfType(DataSourceRepository.class).values();
		if(res.size()==0){
			throw new RuntimeException("You need config at least one dataSourceRegister when use bdf2-jasperreports module!");
		}
		DataSourceRepository dsRepository=res.iterator().next();
		if(StringUtils.isEmpty(dataSourceName)){
			dataSourceName=dsRepository.getDefaultDataSourceName();
		}
		if(!dsRepository.getDataSources().containsKey(dataSourceName)){
			throw new RuntimeException("Data source ["+dataSourceName+" is not exist!]");
		}
		try {
			return dsRepository.getDataSources().get(dataSourceName).getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
