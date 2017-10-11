package com.bstek.bdf2.uploader.controller;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.uploader.model.UploadDefinition;
import com.bstek.bdf2.uploader.service.IFileService;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
public class ProcessDownloadController extends AbstractResolver{
	private IFileService fileService;
	private static final String ID_KEY="id";
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest req,HttpServletResponse res) throws Exception {
		String id = req.getParameter(ID_KEY);
		if (!StringUtils.hasText(id)) {
			id = (String) req.getAttribute(ID_KEY);
		}
		if(StringUtils.isEmpty(id)){
			throw new RuntimeException("Upload definition id can not be null!");
		}
		UploadDefinition uploadDef=fileService.getUploadDefinition(id);
		if(StringUtils.isEmpty(uploadDef.getUploadProcessorKey())){
			throw new RuntimeException("Upload definition ["+id+"] has not set processor!");
		}
		String fileName = uploadDef.getFileName();
		fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
		res.setContentType("application/octet-stream");
		res.setHeader("Connection", "close");// 表示不能用浏览器直接打开
		res.setHeader("Accept-Ranges", "bytes");// 告诉客户端允许断点续传多线程连接下载
		long p = 0;
		if (req.getHeader("Range") != null) {// 客户端请求的下载的文件块的开始字节
			res.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			p = Long.parseLong(req.getHeader("Range").replaceAll("bytes=", "").replaceAll("-", ""));
		}
		res.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		InputStream in = fileService.getFile(uploadDef);
		BufferedInputStream bin = new BufferedInputStream(in);
		bin.skip(p);
		long fileTotalLong = uploadDef.getSize();
		// 下载的文件(或块)长度响应的格式是Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
		res.setHeader("Content-Length", new Long(fileTotalLong - p).toString());
		if (p != 0) {
			// 如果不是从最开始下载,那么设置响应格式Content-Range: bytes [文件块的开始字节]-[文件的总大小
			// -1]/[文件的总大小]
			res.setHeader("Content-Range", "bytes " + new Long(p).toString() + "-"
					+ (fileTotalLong - 1) + "/" + fileTotalLong);
		}
		ServletOutputStream out = res.getOutputStream();
		try {
			IOUtils.copy(bin, out);
		} finally {
			IOUtils.closeQuietly(in);
		}
		out.flush();
		out.close();
		return null;
	}
	
	public IFileService getFileService() {
		return fileService;
	}
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}
}
