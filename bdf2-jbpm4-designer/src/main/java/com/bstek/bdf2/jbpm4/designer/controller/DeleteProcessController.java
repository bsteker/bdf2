package com.bstek.bdf2.jbpm4.designer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.designer.model.DesignerProcess;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.uploader.service.ILobStoreService;
import com.bstek.dorado.web.resolver.AbstractResolver;


/**
 * @author Jacky.gao
 * @since 2013-6-28
 */
public class DeleteProcessController extends AbstractResolver {
	private Jbpm4HibernateDao hibernateDao;
	private IBpmService bpmService;
	private ILobStoreService lobStoreService;
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lobId=request.getParameter("lobId");
		String deploymentId=request.getParameter("deploymentId");
		String id=request.getParameter("id");
		DesignerProcess process=new DesignerProcess();
		process.setId(id);
		Session session=hibernateDao.getSessionFactory().openSession();
		try{
			session.delete(process);
			lobStoreService.deleteString(lobId);
			bpmService.deleteDeployment(deploymentId);
		}finally{
			session.flush();
			session.close();
		}
		return null;
	}
	public ILobStoreService getLobStoreService() {
		return lobStoreService;
	}
	public void setLobStoreService(ILobStoreService lobStoreService) {
		this.lobStoreService = lobStoreService;
	}
	public Jbpm4HibernateDao getHibernateDao() {
		return hibernateDao;
	}
	public void setHibernateDao(Jbpm4HibernateDao hibernateDao) {
		this.hibernateDao = hibernateDao;
	}
	public IBpmService getBpmService() {
		return bpmService;
	}
	public void setBpmService(IBpmService bpmService) {
		this.bpmService = bpmService;
	}
}
