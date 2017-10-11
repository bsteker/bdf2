package com.bstek.bdf2.dbconsole.controller;

import java.util.UUID;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;

import com.bstek.bdf2.core.business.IMenuGenerator;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.orm.jdbc.JdbcDao;

@Controller
public class DbConsoleMenuGenerator extends JdbcDao implements IMenuGenerator {
	public void generate(Session session, String rootId) {
		Url secondUrl = this.createUrl("数据库浏览器", "dorado/res/com/bstek/bdf2/dbconsole/view/icons/server_database.png", rootId, 8, null);
		session.save(secondUrl);
		Url childUrl = this.createUrl("数据库浏览器", "dorado/res/com/bstek/bdf2/dbconsole/view/icons/database_refresh.png", secondUrl.getId(), 1, "bdf2.dbconsole.view.DbConsoleMaintain.d");
		session.save(childUrl);
	}

	private Url createUrl(String name, String icon, String parentId, int order, String targetUrl) {
		String companyId = ContextHolder.getLoginUser().getCompanyId();
		Url url = new Url();
		url.setId(UUID.randomUUID().toString());
		url.setName(name);
		url.setIcon(icon);
		url.setUrl(targetUrl);
		url.setParentId(parentId);
		url.setCompanyId(companyId);
		url.setForNavigation(true);
		url.setOrder(order);
		return url;
	}

}
