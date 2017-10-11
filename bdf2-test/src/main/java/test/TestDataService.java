package test;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bstek.bdf2.profile.model.AssignTarget;
import com.bstek.bdf2.profile.model.UrlDefinition;
import com.bstek.bdf2.profile.service.IProfileDataService;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

public class TestDataService implements IProfileDataService {

	public void loadAssignTargets(Page<AssignTarget> page, Criteria criteria) {
		AssignTarget target=new AssignTarget();
		target.setId("bstek");
		target.setName("test target");
		target.setDesc("test target desc");
		AssignTarget target1=new AssignTarget();
		target1.setId("bstek1");
		target1.setName("test target1");
		target1.setDesc("test target1 desc");
		List<AssignTarget> targets=new ArrayList<AssignTarget>();
		targets.add(target);
		targets.add(target1);
		page.setEntities(targets);

	}

	public String getAssignTargetId(HttpServletRequest request) {
		return "bstek";
	}

	public List<UrlDefinition> loadUrls(String companyId, String parentId) {
		UrlDefinition urlDef=new UrlDefinition();
		urlDef.setId("test");
		urlDef.setName("测试URl");
		urlDef.setUrl("test.Test1Page.d");
		List<UrlDefinition> urls=new ArrayList<UrlDefinition>();
		urls.add(urlDef);
		return urls;
	}

}
