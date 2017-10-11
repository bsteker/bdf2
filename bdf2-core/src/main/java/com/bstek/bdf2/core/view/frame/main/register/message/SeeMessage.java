package com.bstek.bdf2.core.view.frame.main.register.message;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Message;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-2-5
 */
@Component("bdf2.seeMessage")
public class SeeMessage extends CoreHibernateDao {
	@DataProvider
	public void loadSendMessages(Page<Message> page, Criteria criteria) throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new RuntimeException("Please login first");
		}
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("messageSender", user.getUsername());
		loadMessages(page, criteria, valueMap);
	}

	@DataProvider
	public void loadReceiveMessages(Page<Message> page, Criteria criteria) throws Exception {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new RuntimeException("Please login first");
		}
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("messageReceiver", user.getUsername());
		loadMessages(page, criteria, valueMap);
	}

	@DataResolver
	public void updateMessage(Collection<Message> messages) throws Exception {
		Session session = this.getSessionFactory().openSession();
		try {
			for (Message message : messages) {
				EntityState entityState = EntityUtils.getState(message);
				if (EntityState.MODIFIED.equals(entityState)) {
					session.update(message);
				} else if (EntityState.DELETED.equals(entityState)) {
					session.delete(message);
				}
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	private void loadMessages(Page<Message> page, Criteria criteria, Map<String, String> params) throws Exception {
		String baseHql = "from " + Message.class.getName() + " x where 1=1";
		ParseResult result = this.parseCriteria(criteria, true, "x");
		Map<String, Object> valueMap = null;
		String countHql = "select count(*) ";
		if (result != null) {
			StringBuffer sb = result.getAssemblySql();
			valueMap = result.getValueMap();
			baseHql += " and " + sb.toString();
		} else {
			valueMap = new HashMap<String, Object>();
		}
		String messageReceiver = params.get("messageReceiver");
		String messageSender = params.get("messageSender");
		if (StringUtils.hasText(messageReceiver)) {
			valueMap.put("messageReceiver", messageReceiver);
			baseHql += " and x.receiver=:messageReceiver";
		}
		if (StringUtils.hasText(messageSender)) {
			valueMap.put("messageSender", messageSender);
			baseHql += " and x.sender=:messageSender";
		}
		countHql += baseHql;
		baseHql += " order by x.read asc,x.sendDate desc";
		pagingQuery(page, baseHql, countHql, valueMap);
	}
}
