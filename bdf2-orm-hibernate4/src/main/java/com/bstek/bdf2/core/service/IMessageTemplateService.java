package com.bstek.bdf2.core.service;

import com.bstek.bdf2.core.model.MessageTemplate;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-3-25
 */
public interface IMessageTemplateService {
	public static final String BEAN_ID="bdf2.messageTemplateService";
	void loadMessageTemplates(Page<MessageTemplate> page,Criteria criteria,String type);
}
