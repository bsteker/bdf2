package com.bstek.bdf2.jbpm4.designer.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.designer.model.DesignerProcess;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-6-25
 */
public class ProcessListController extends AbstractResolver {
	private Jbpm4HibernateDao hibernateDao;
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		String hql="from "+DesignerProcess.class.getName()+" order by createDate desc";
		List<DesignerProcess> processes=hibernateDao.query(hql);
		int i=0;
		for(DesignerProcess p:processes){
			i++;
			sb.append("{");
			sb.append("\"id\":\""+p.getId()+"\",");
			sb.append("\"name\":\""+p.getName()+"\",");
			sb.append("\"version\":\""+p.getVersion()+"\",");
			sb.append("\"lobId\":\""+p.getLobId()+"\",");
			sb.append("\"deploymentId\":\""+p.getDeploymentId()+"\",");
			sb.append("\"createDate\":\""+sd.format(p.getCreateDate())+"\"");
			sb.append("}");
			if(i<processes.size()){
				sb.append(",");
			}
		}
		sb.append("]");
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter writer=response.getWriter();
        writer.write(sb.toString());
        writer.flush();
        writer.close();
		return null;
	}
	public Jbpm4HibernateDao getHibernateDao() {
		return hibernateDao;
	}
	public void setHibernateDao(Jbpm4HibernateDao hibernateDao) {
		this.hibernateDao = hibernateDao;
	}
}
