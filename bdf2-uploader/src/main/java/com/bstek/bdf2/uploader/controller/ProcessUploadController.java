package com.bstek.bdf2.uploader.controller;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.uploader.UploaderHibernateDao;
import com.bstek.bdf2.uploader.model.UploadDefinition;
import com.bstek.bdf2.uploader.processor.IFileProcessor;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-5-3
 */
public class ProcessUploadController extends AbstractResolver implements InitializingBean {
	private Log log = LogFactory.getLog(ProcessUploadController.class);
	private Collection<IFileProcessor> processors;
	private UploaderHibernateDao hibernateDao;
	private String defaultUploadProcessor;
	private String FILE_FIELD_NAME = "targetFile";
	private String UPLOAD_PROCESSER_FIELD_NAME = "_uploadProcessor";
	private String UPLOAD_ALLOW_FILE_TYPES = "_allowFileTypes";
	private String UPLOAD_ALLOW_MAX_FILE_SIZE = "_allowMaxFileSize";
	private String allowFileTypes;
	private long allowMaxFileSize;

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String errorMsg = null;
		boolean fail = false;
		UploadDefinition uploadDefinition = null;
		Session session = null;
		InputStream inputstream = null;
		String fileName = null;
		long fileSize = 0;
		String uploadProcessor = null;
		String allowFileTypesParameter = null;
		String allowMaxFileSizeParameter = null;
		try {
			session = hibernateDao.getSessionFactory().openSession();
			if (req instanceof MultipartRequest) {
				DefaultMultipartHttpServletRequest request = (DefaultMultipartHttpServletRequest) req;
				request.setCharacterEncoding("utf-8");
				MultipartFile file = request.getFile(FILE_FIELD_NAME);
				inputstream = file.getInputStream();
				fileName = file.getOriginalFilename();
				fileName = new String(fileName.getBytes("iso-8859-1"), "utf-8");
				fileSize = file.getSize();
				Map<String, String[]> map = request.getParameterMap();
				String[] uploadProcessors = map.get(UPLOAD_PROCESSER_FIELD_NAME);
				if (uploadProcessors != null && uploadProcessors.length > 0) {
					uploadProcessor = uploadProcessors[0];
				}
				String[] types = map.get(UPLOAD_ALLOW_FILE_TYPES);
				if (types != null && types.length > 0) {
					allowFileTypesParameter = types[0];
				}

				String[] size = map.get(UPLOAD_ALLOW_MAX_FILE_SIZE);
				if (size != null && size.length > 0) {
					allowMaxFileSizeParameter = size[0];
				}
			} else {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletContext servletContext = req.getSession().getServletContext();
				File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
				factory.setRepository(repository);
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("utf-8");
				List<FileItem> items = upload.parseRequest(req);
				if (items != null) {
					for (FileItem item : items) {
						String fieldName = item.getFieldName();
						if (fieldName.equals(FILE_FIELD_NAME)) {
							inputstream = item.getInputStream();
							fileName = item.getName();
							fileSize = item.getSize();
						} else if (fieldName.equals(UPLOAD_PROCESSER_FIELD_NAME)) {
							uploadProcessor = item.getString();
						} else if (fieldName.equals(UPLOAD_ALLOW_FILE_TYPES)) {
							allowFileTypesParameter = item.getString();
						} else if (fieldName.equals(UPLOAD_ALLOW_MAX_FILE_SIZE)) {
							allowMaxFileSizeParameter = item.getString();
						} else {
							ContextHolder.getRequest().setAttribute(fieldName, item.getString());
						}
					}
				}
			}
			if (StringUtils.isEmpty(uploadProcessor)) {
				uploadProcessor = defaultUploadProcessor;
			}
			IFileProcessor p = null;
			for (IFileProcessor processor : processors) {
				if (processor.key().equals(uploadProcessor)) {
					if (processor.isDisabled()) {
						throw new RuntimeException("上传文件处理器 [" + uploadProcessor + "] 已被禁用!");
					} else {
						p = processor;
						break;
					}
				}
			}
			if (p == null) {
				throw new RuntimeException("上传文件处理器 [" + uploadProcessor + "] 没找到!");
			}
			uploadDefinition = new UploadDefinition();
			uploadDefinition.setId(UUID.randomUUID().toString());
			uploadDefinition.setCreateDate(new Date());
			IUser user = ContextHolder.getLoginUser();
			if (user != null) {
				uploadDefinition.setCreateUser(user.getUsername());
			}
			uploadDefinition.setUploadProcessorKey(uploadProcessor);
			checkFileType(allowFileTypesParameter, fileName);
			checkFileSize(allowMaxFileSizeParameter, fileSize);
			uploadDefinition.setFileName(fileName);
			uploadDefinition.setSize(fileSize);
			p.saveFile(uploadDefinition, inputstream);
			session.save(uploadDefinition);
			inputstream.close();
		} catch (Exception ex) {
			errorMsg = ex.getMessage();
			fail = true;
			log.warn("File upload fail", ex);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		UploadResult result = new UploadResult();
		if (fail) {
			result.setResult("fail");
			result.setErrorMessage(errorMsg);
		} else {
			result.setResult("success");
			result.setFilename(uploadDefinition.getFileName());
			result.setId(uploadDefinition.getId());
		}
		res.setContentType("text/html; charset=UTF-8");
		res.setCharacterEncoding("UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		OutputStream out = res.getOutputStream();
		try {
			mapper.writeValue(out, result);
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	private void checkFileSize(String allowMaxFileSizeParameter, long currentFileSize) {
		long size = allowMaxFileSize;
		if (StringUtils.isNotEmpty(allowMaxFileSizeParameter)) {
			size = Long.valueOf(allowMaxFileSizeParameter);
		}
		if (size > 0) {
			if (currentFileSize > size) {
				throw new IllegalArgumentException("当前允许上传的文件最大尺寸为 [" + size + "byte],而当前文件尺寸为 [" + currentFileSize
						+ "]byte,已超过最大尺寸限制!");
			}
		}
	}

	private void checkFileType(String allowFileTypesParameter, String fileName) {
		String types = allowFileTypes;
		if (StringUtils.isNotEmpty(allowFileTypesParameter)) {
			types = allowFileTypesParameter;
		}
		if (StringUtils.isNotEmpty(types)) {
			int pos = fileName.lastIndexOf(".");
			if (pos < 0) {
				throw new IllegalArgumentException("上传文件的文件名 [" + fileName + "]不合法!");
			}
			boolean allow = false;
			String fileType = fileName.substring(pos + 1, fileName.length());
			for (String type : types.split(",")) {
				if (type.equalsIgnoreCase(fileType)) {
					allow = true;
					break;
				}
			}
			if (!allow) {
				throw new IllegalArgumentException("上传文件只能是 [" + types + "]类型,当前文件是 [" + fileType + "]类型!");
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		if (this.getApplicationContext().getBeansOfType(IFileProcessor.class).size() > 0) {
			processors = this.getApplicationContext().getBeansOfType(IFileProcessor.class).values();
		} else {
			ApplicationContext ac = this.getApplicationContext().getParent();
			if (ac != null) {
				processors = ac.getBeansOfType(IFileProcessor.class).values();
			}
		}
	}

	public String getAllowFileTypes() {
		return allowFileTypes;
	}

	public void setAllowFileTypes(String allowFileTypes) {
		this.allowFileTypes = allowFileTypes;
	}

	public long getAllowMaxFileSize() {
		return allowMaxFileSize;
	}

	public void setAllowMaxFileSize(long allowMaxFileSize) {
		this.allowMaxFileSize = allowMaxFileSize;
	}

	public void setDefaultUploadProcessor(String defaultUploadProcessor) {
		this.defaultUploadProcessor = defaultUploadProcessor;
	}

	public void setHibernateDao(UploaderHibernateDao hibernateDao) {
		this.hibernateDao = hibernateDao;
	}

	class UploadResult {
		private String result;
		private String errorMessage;
		private String filename;
		private String id;

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}
}
