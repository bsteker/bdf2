package com.bstek.bdf2.core.view.frame.main.register.logininfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;
import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.form.Label;

/**
 * @author Jacky.gao
 * @since 2013-2-6
 */
@Component
public class ShowLoginInfoShortcutRegister implements IFrameShortcutActionRegister {
	@Value("${bdf2.disableShowLoginInfoShortcutRegister}")
	private boolean disabled;
	private static final ResourceManager resourceManager = ResourceManagerUtils
			.get(ShowLoginInfoShortcutRegister.class);

	public void registerToFrameTop(Container container) {
	}

	public void registerToStatusBar(Container container) {
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String name=user.getCname();
		if(StringUtils.isEmpty(name)){
			name=user.getEname();
		}
		String info=user.getUsername();
		if(StringUtils.isNotEmpty(name)){
			info=name+"("+info+")";
		}
		
		Label loginUserInfoLabel=new Label();
		loginUserInfoLabel.addClientEventListener("onRefreshDom", new DefaultClientEvent("arg.dom.style.lineHeight=arg.dom.style.height;"));
		loginUserInfoLabel.setText(resourceManager.getString("bdf2.core/loginUserInfoLabel") + "："+info);
		container.addChild(loginUserInfoLabel);
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public int order() {
		return 10;
	}

}
