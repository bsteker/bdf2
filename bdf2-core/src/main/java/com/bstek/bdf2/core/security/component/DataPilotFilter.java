package com.bstek.bdf2.core.security.component;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.datacontrol.DataPilot;

/**
 * 实现对DataPilot权限过滤
 * 
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class DataPilotFilter implements IComponentFilter {

	public void filter(String url, Component component, UserAuthentication authentication) throws Exception {
		DataPilot dataPilot = (DataPilot) component;
		String id = dataPilot.getId();
		boolean authority = true;
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(authentication, AuthorityType.read, url, id);
		}
		if (!authority) {
			dataPilot.setIgnored(true);
			return;
		}
		setSubControlAuth(url, dataPilot, authentication);
	}

	private void setSubControlAuth(String url, DataPilot pilot, UserAuthentication authentication) throws Exception {
		String itemCodes = pilot.getItemCodes();
		if (itemCodes == null) {
			return;
		}
		if (itemCodes.contains("+")) {
			boolean addCaptionCNPermission_read = SecurityUtils.checkComponent(authentication, AuthorityType.read, url,
					"添加");
			boolean addCaptionENPermission_read = SecurityUtils.checkComponent(authentication, AuthorityType.read, url,
					"Insert");
			if (addCaptionCNPermission_read && addCaptionENPermission_read) {
			} else {
				setSubControlNoAuth("+", pilot);
			}
		}
		if (itemCodes.contains("-")) {
			boolean delCaptionCNPermission_read = SecurityUtils.checkComponent(authentication, AuthorityType.read, url,
					"删除");
			boolean delCaptionENPermission_read = SecurityUtils.checkComponent(authentication, AuthorityType.read, url,
					"Delete");
			if (delCaptionCNPermission_read && delCaptionENPermission_read) {
			} else {
				setSubControlNoAuth("-", pilot);
			}
		}
		if (itemCodes.contains("x")) {
			boolean cancelCaptionCNPermission_read = SecurityUtils.checkComponent(authentication, AuthorityType.read,
					url, "取消");
			boolean cancelCaptionENPermission_read = SecurityUtils.checkComponent(authentication, AuthorityType.read,
					url, "Cancel");
			if (cancelCaptionCNPermission_read && cancelCaptionENPermission_read) {
			} else {
				setSubControlNoAuth("x", pilot);
			}
		}
	}

	private void setSubControlNoAuth(String code, DataPilot pilot) {
		String temp1 = "," + code;
		String temp2 = code + ",";
		String itemCodes = pilot.getItemCodes();
		if (itemCodes.contains(temp1)) {
			String newItemCodes = itemCodes.replace(temp1, "");
			pilot.setItemCodes(newItemCodes);
		} else if (itemCodes.contains(temp2)) {
			String newItemCodes = itemCodes.replace(temp2, "");
			pilot.setItemCodes(newItemCodes);
		}
	}

	public boolean support(Component component) {
		return component instanceof DataPilot;
	}
}
