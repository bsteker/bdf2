package com.bstek.bdf2.jasperreports.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-5-15
 */
public class JasperreportsDisplayController extends AbstractResolver {

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest req,HttpServletResponse res) throws Exception {
		String reportFile=req.getParameter(IExporter.REPORT_FILE_PARAMETER_KEY);
		if(StringUtils.isEmpty(reportFile)){
			throw new RuntimeException("Report file can not be null!");
		}
		String fileSource=req.getParameter(IExporter.FILE_SOURCE_PARAMETER_KEY);
		if(StringUtils.isEmpty(reportFile)){
			throw new RuntimeException("File source can not be null!");
		}
		String dt=req.getParameter(IExporter.DATASOURCE_TYPE_PARAMETER_KEY);
		String dataSourceProvider=req.getParameter(IExporter.DATASOURCE_PROVIDER_PARAMETER_KEY);
		String reportParameters=req.getParameter(IExporter.REPORT_PARAMETERS_PARAMETER_KEY);
		String cache=req.getParameter(IExporter.CACHE_PARAMETER_KEY);
		String exportFileType=req.getParameter(IExporter.EXPORT_FILE_TYPE_PARAMETER_KEY);
		StringBuffer sb=new StringBuffer();
		String url=buildUrl(req)+req.getContextPath();
		if(url.endsWith("/")){
			sb.append(url+"dorado/bdf2/jasperreports/report.exporter?");			
		}else{
			sb.append(url+"/dorado/bdf2/jasperreports/report.exporter?");						
		}
		sb.append(IExporter.REPORT_FILE_PARAMETER_KEY+"="+reportFile);
		sb.append("&"+IExporter.FILE_SOURCE_PARAMETER_KEY+"="+fileSource);
		if(dt!=null){
			sb.append("&"+IExporter.DATASOURCE_TYPE_PARAMETER_KEY+"="+dt);			
		}
		if(dataSourceProvider!=null){
			sb.append("&"+IExporter.DATASOURCE_PROVIDER_PARAMETER_KEY+"="+dataSourceProvider);			
		}
		if(reportParameters!=null){
			sb.append("&"+IExporter.REPORT_PARAMETERS_PARAMETER_KEY+"="+reportParameters);			
		}
		if(cache!=null){
			sb.append("&"+IExporter.CACHE_PARAMETER_KEY+"="+cache);			
		}
		if(exportFileType!=null){
			sb.append("&"+IExporter.EXPORT_FILE_TYPE_PARAMETER_KEY+"="+exportFileType);			
		}
		String targetUrl=sb.toString();
		res.setContentType("text/html; charset=utf-8");
		String baseUrl=req.getContextPath()+"/dorado/res/dorado/resources/jasperreportsviewer/";
		PrintWriter pw=res.getWriter();
		pw.write(this.buildContent(baseUrl, targetUrl));
		pw.flush();
		pw.close();
		return null;
	}
	private String buildContent(String baseUrl,String targetUrl){
		StringBuffer content=new StringBuffer();
		content.append("<html>");
		content.append("\r");
		content.append("<head>");
		content.append("\r");
		content.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		content.append("\r");
		content.append("<script type=\"text/javascript\" src=\""+baseUrl+"swfobject.js\"></script>");
		content.append("\r");
		content.append("<script type=\"text/javascript\">");
		content.append("\r");
		content.append("var swfVersionStr = \"11.1.0\";");
		content.append("\r");
		content.append("var xiSwfUrlStr = \""+baseUrl+"playerProductInstall.swf\";");
		content.append("\r");
		content.append("var flashvars = {jrpxml:escape(\""+targetUrl+"\")};");
		content.append("\r");
		content.append("var params = {};");
		content.append("\r");
		content.append("params.quality = \"high\";");
		content.append("\r");
		content.append("params.bgcolor = \"#ffffff\";");
		content.append("\r");
		content.append("params.allowScriptAccess = \"always\";");
		content.append("\r");
		content.append("params.allowfullscreen = \"true\";");
		content.append("\r");
		content.append("var attributes = {};");
		content.append("\r");
		content.append("attributes.id = \"JasperreportsViewer\";");
		content.append("\r");
		content.append("attributes.name = \"JasperreportsViewer\";");
		content.append("\r");
		content.append("attributes.align = \"middle\";");
		content.append("\r");
		content.append("swfobject.embedSWF(\""+baseUrl+"JasperreportsViewer.swf\", \"flashContent\",\"100%\", \"100%\",swfVersionStr, xiSwfUrlStr,flashvars, params, attributes);");
		content.append("\r");
		content.append("swfobject.createCSS(\"#flashContent\", \"display:block;text-align:left;\");");
		content.append("\r");
		content.append("</script>");
		content.append("\r");
		content.append("</head>");
		content.append("\r");
		content.append("<body>");
		content.append("\r");
		content.append("<div id=\"flashContent\">");
		content.append("\r");
		content.append("<script type=\"text/javascript\">");
		content.append("\r");
		content.append("var pageHost = ((document.location.protocol == \"https:\") ? \"https://\" : \"http://\");");
		content.append("\r");
		content.append("document.write(\"<a href='http://www.adobe.com/go/getflashplayer'><img src='\" + pageHost + \"www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>\" );");
		content.append("\r");
		content.append("</script>");
		content.append("\r");
		content.append("</div>");
		content.append("\r");
		content.append("<noscript>");
		content.append("\r");
		content.append("<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" width=\"100%\" height=\"100%\" id=\"JasperreportsViewer\">");
		content.append("\r");
		content.append("<param name=\"movie\" value=\""+baseUrl+"JasperreportsViewer.swf\" />");
		content.append("\r");
		content.append("<param name=\"quality\" value=\"high\" />");
		content.append("\r");
		content.append("<param name=\"bgcolor\" value=\"#ffffff\" />");
		content.append("\r");
		content.append("<param name=\"allowScriptAccess\" value=\"always\" />");
		content.append("\r");
		content.append("<param name=\"allowFullScreen\" value=\"true\" />");
		content.append("\r");
		content.append("<!--[if !IE]>-->");
		content.append("\r");
		content.append("<object type=\"application/x-shockwave-flash\" data=\""+baseUrl+"JasperreportsViewer.swf\" width=\"100%\" height=\"100%\">");
		content.append("\r");
		content.append("<param name=\"quality\" value=\"high\" />");
		content.append("\r");
		content.append("<param name=\"bgcolor\" value=\"#ffffff\" />");
		content.append("\r");
		content.append("<param name=\"allowScriptAccess\" value=\"always\" />");
		content.append("\r");
		content.append("<param name=\"allowFullScreen\" value=\"true\" />");
		content.append("\r");
		content.append("<!--<![endif]-->");
		content.append("\r");
		content.append("<!--[if gte IE 6]>-->");
		content.append("\r");
		content.append("Either scripts and active content are not permitted to run or Adobe Flash Player version 11.1.0 or greater is not installed.");
		content.append("\r");
		content.append("<br>");
		content.append("\r");
		content.append("<a href=\"http://www.adobe.com/go/getflashplayer\">");
		content.append("\r");
		content.append("<img src=\"http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif\" alt=\"Get Adobe Flash Player\" />");
		content.append("\r");
		content.append("</a>");
		content.append("\r");
		content.append("<!--[if !IE]>-->");
		content.append("\r");
		content.append("</object>");
		content.append("\r");
		content.append(" <!--<![endif]-->");
		content.append("\r");
		content.append("</object>");
		content.append("\r");
		content.append("</noscript>");
		content.append("\r");
		content.append("</body>");
		content.append("\r");
		content.append("</html>");
		return content.toString();
	}
	
	private String buildUrl(HttpServletRequest req){
		String scheme = req.getScheme().toLowerCase();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(req.getServerName());

        // Only add port if not default
        int serverPort=req.getServerPort();
        if ("http".equals(scheme)) {
            if (serverPort != 80) {
                url.append(":").append(serverPort);
            }
        } else if ("https".equals(scheme)) {
            if (serverPort != 443) {
                url.append(":").append(serverPort);
            }
        }
        return url.toString();
	}
}
