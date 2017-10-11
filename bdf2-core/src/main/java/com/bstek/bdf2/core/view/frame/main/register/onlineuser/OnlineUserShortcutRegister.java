package com.bstek.bdf2.core.view.frame.main.register.onlineuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.SubViewHolder;
import com.bstek.dorado.view.widget.form.Label;

/**
 * @author Jacky.gao
 * @since 2013年12月17日
 */
@Component("bdf2.onlineUserShortcutRegister")
public class OnlineUserShortcutRegister implements IFrameShortcutActionRegister{
	@Value("${bdf2.disabledOnlineUserShortcutRegister}")
	private boolean disabledOnlineUserShortcutRegister;
	@Value("${bdf2.showOnlineUserCount}")
	private boolean showOnlineUserCount;
	private static final ResourceManager resourceManager = ResourceManagerUtils
			.get(OnlineUserShortcutRegister.class);
	@Autowired
	@Qualifier("bdf2.sessionRegistry")
	private SessionRegistry sessionRegistry;
	
	@Expose
	public int getLoginuserCount(){
		return sessionRegistry.getAllPrincipals().size();
	}
	
	@DataProvider
	public List<Map<String,Object>> loadAllLoginUsers(){
		String currentCompany = ContextHolder.getLoginUser().getCompanyId();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		int i = 0;
		List<Object> users=sessionRegistry.getAllPrincipals();
		for(Object obj:users){
			IUser user=(IUser)obj;
			if (user.getCompanyId().equals(currentCompany)){
				i++;
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("username", user.getUsername());
				map.put("cname", user.getCname());
				list.add(map);
			}
			if (i >= 500) break;
		}
		return list;
	}
	
	
	public void registerToStatusBar(Container container) {
		SubViewHolder subviewHolder=new SubViewHolder();
		subviewHolder.setSubView("bdf2.core.view.frame.main.register.onlineuser.OnlineUser");
		container.addChild(subviewHolder);
		
		Label onlineUserCountLabel=new Label();
		onlineUserCountLabel.setId("onlineUserCountLabel");
		Map<String,Object> style=new HashMap<String,Object>();
		style.put("cursor", "pointer");
		style.put("padding-left", "5px");
		onlineUserCountLabel.setStyle(style);
		onlineUserCountLabel.addClientEventListener("onRefreshDom", new DefaultClientEvent("arg.dom.style.lineHeight=arg.dom.style.height;"));
		onlineUserCountLabel.addClientEventListener("onDoubleClick", new DefaultClientEvent("window.showOnlineUserDialog()"));
		if (showOnlineUserCount){
			onlineUserCountLabel.setTip(resourceManager.getString("bdf2.core/loginUserCountLabelTip"));
			onlineUserCountLabel.setText(resourceManager.getString("bdf2.core/loginUserCountLabel", sessionRegistry.getAllPrincipals().size()));
		}else{
			onlineUserCountLabel.setText(resourceManager.getString("bdf2.core/loginUserCountLabelTip"));
		}
		container.addChild(onlineUserCountLabel);
	}
	public void registerToFrameTop(Container container) {
		
	}
	public int order() {
		return 20;
	}
	public boolean isDisabled() {
		return disabledOnlineUserShortcutRegister;
	}
}
