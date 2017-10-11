package com.bstek.bdf2.core.business;

import org.hibernate.Session;

/**
 * @author Jacky.gao
 * @since 2013-3-20
 */
public interface IMenuGenerator {
	void generate(Session session,String parentMenuId);
}
