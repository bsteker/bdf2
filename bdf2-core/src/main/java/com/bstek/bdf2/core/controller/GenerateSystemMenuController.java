package com.bstek.bdf2.core.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IMenuGenerator;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.view.frame.main.MainFrame;

public class GenerateSystemMenuController extends CoreHibernateDao implements IController,InitializingBean {
	private boolean disabled;
	private Collection<IMenuGenerator> menuGenerators;
	public String getUrl() {
		return "/generate.system.menu";
	}

	
	public void execute(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException {
		Session session=this.getSessionFactory().openSession();
		try{
			IUser user=ContextHolder.getLoginUser();
			if(!user.isAdministrator()){
				PrintWriter out=response.getWriter();
				out.println("You are not a system administrator,so you can't do this!");
				out.flush();
				out.close();
				return;
			}
			String companyId=user.getCompanyId();
			if(StringUtils.isNotEmpty(getFixedCompanyId())){
				companyId=getFixedCompanyId();
			}
			String rootId="root-"+companyId;
			this.deleteSystemUrl(rootId, session);
			session.createQuery("delete "+Url.class.getName()+" where id=:id").setString("id",rootId).executeUpdate();
			Url root=new Url();
			root.setId(rootId);
			root.setCompanyId(companyId);
			root.setForNavigation(true);
			root.setName("系统管理");
			root.setIcon("url(skin>common/icons.gif) -102px -101px");
			root.setOrder(1);
			session.save(root);
			for(IMenuGenerator generator:menuGenerators){
				generator.generate(session, rootId);
			}
			PrintWriter pw=response.getWriter();
			try{
				pw.write("<font color='green'>Successful generating system menu</font>");
			}finally{
				pw.flush();
				pw.close();
			}
		}finally{
			session.flush();
			session.close();
		}
		MainFrame frame=ContextHolder.getBean(MainFrame.BEAN_ID);
		frame.cacheNavigatorUrls();
	}
	
	@SuppressWarnings("unchecked")
	private void deleteSystemUrl(String parentId,Session session){
		List<Url> urls=session.createQuery("from "+Url.class.getName()+" where parentId=:parentId").setString("parentId", parentId).list();
		session.createQuery("delete "+Url.class.getName()+" where parentId=:parentId").setString("parentId", parentId).executeUpdate();
		for(Url url:urls){
			int count=0;
			Query countQuery=session.createQuery("select count(*) from "+Url.class.getName()+" where parentId=:parentId").setString("parentId", url.getId());
			Object countObj=countQuery.uniqueResult();
			if(countObj instanceof Long){
				count=((Long)countObj).intValue();
			}else if(countObj instanceof Integer){
				count=((Integer)countObj).intValue();
			}
			if(count>0){
				deleteSystemUrl(url.getId(),session);	
			}
		}
	}

	public boolean anonymousAccess() {
		return false;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled(){
		return disabled;
	}

	public void afterPropertiesSet() throws Exception {
		menuGenerators=this.getApplicationContext().getBeansOfType(IMenuGenerator.class).values();
	}
}
