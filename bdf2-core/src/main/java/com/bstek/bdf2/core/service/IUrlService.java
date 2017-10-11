package com.bstek.bdf2.core.service;

import java.util.List;

import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.model.UrlComponent;

public interface IUrlService {
	public static final String BEAN_ID="bdf2.urlService";
	List<Url> loadUrlsByRoleId(String roleId);
	List<UrlComponent> loadComponentUrlsByRoleId(String roleId);
}
