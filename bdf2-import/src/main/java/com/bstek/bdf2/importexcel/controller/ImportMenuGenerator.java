package com.bstek.bdf2.importexcel.controller;

import java.util.UUID;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IMenuGenerator;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Component
public class ImportMenuGenerator extends HibernateDao implements IMenuGenerator {

	public void generate(Session session, String rootId) {
		Url secondUrl = this.createUrl("Excel导入", "dorado/res/com/bstek/bdf2/importexcel/view/icons/page_excel.png", rootId, 5, null);
		session.save(secondUrl);
		Url childUrl = this.createUrl("导入模板定义", "url(skin>common/icons.gif) -102px -21px", secondUrl.getId(), 1, "bdf2.importexcel.view.ExcelMaintain.d");
		session.save(childUrl);
		childUrl = this.createUrl("导入演示", "dorado/res/com/bstek/bdf2/importexcel/view/icons/page_excel.png", secondUrl.getId(), 2, "bdf2.importexcel.view.ImportExcel.d");
		session.save(childUrl);
	}

	private Url createUrl(String name, String icon, String parentId, int order, String targetUrl) {
		boolean noCore=Configure.getBoolean("bdf2.noCore",false);
		String companyId=null;
		if(noCore){
			companyId = "bstek";
		}else{
			companyId = ContextHolder.getLoginUser().getCompanyId();
		}
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
