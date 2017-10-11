package com.bstek.bdf2.core.view;

import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.view.config.xml.ViewXmlConstants;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.service.DataServiceProcessorSupport.ParsedDataObjectName;
import com.bstek.dorado.web.DoradoContext;

/**
 * Dorado7 ViewManager工具类，利用该类可快速 取到ViewConfig、DataProvider之类Dorado7服务端对象
 * @since 2013-1-30
 * @author Jacky.gao
 */
public class ViewManagerHelper {
	public static final String BEAN_ID="bdf2.viewManagerHelper";
	protected static final String PRIVATE_VIEW_OBJECT_PREFIX = ViewXmlConstants.PATH_VIEW_SHORT_NAME + Constants.PRIVATE_DATA_OBJECT_SUBFIX;
	private ViewConfigManager viewConfigManager;
	private DataProviderManager dataProviderManager;

	public ViewConfig getViewConfig(DoradoContext context, String viewName)
			throws Exception {
		ViewConfig viewConfig = (ViewConfig) context.getAttribute(viewName);
		if (viewConfig == null) {
			viewConfig = viewConfigManager.getViewConfig(viewName);
			context.setAttribute(viewName, viewConfig);
		}
		return viewConfig;
	}

	public DataProvider getDataProvider(String dataProviderId)
			throws Exception {
		DataProvider dataProvider;
		// 判断是否View中的私有DataProvider
		if (dataProviderId.startsWith(PRIVATE_VIEW_OBJECT_PREFIX)) {
			ParsedDataObjectName parsedName = new ParsedDataObjectName(
					dataProviderId);
			ViewConfig viewConfig = getViewConfig(DoradoContext.getCurrent(),
					parsedName.getViewName());
			dataProvider = viewConfig.getDataProvider(parsedName
					.getDataObject());
		} else {
			dataProvider = dataProviderManager.getDataProvider(dataProviderId);
		}
		return dataProvider;
	}

	public ViewConfigManager getViewConfigManager() {
		return viewConfigManager;
	}

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public DataProviderManager getDataProviderManager() {
		return dataProviderManager;
	}

	public void setDataProviderManager(DataProviderManager dataProviderManager) {
		this.dataProviderManager = dataProviderManager;
	}
}
