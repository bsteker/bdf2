package com.bstek.bdf2.jbpm4.view.todo.frame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.SubViewHolder;

/**
 * @author Jacky.gao
 * @since 2013-3-27
 */
@Component
public class FetchTodoTaskCountShortcutRegister implements
		IFrameShortcutActionRegister {
	@Autowired
	@Qualifier(IBpmService.BEAN_ID)
	private IBpmService bpmService;
	@Value("${bdf2.jbpm4.disableFetchTodoTaskCountShortcutRegister}")
	private boolean disabled;
	public void registerToFrameTop(Container container) {

	}

	public void registerToStatusBar(Container container) {
		SubViewHolder subviewHolder=new SubViewHolder();
		subviewHolder.setSubView("bdf2.jbpm4.view.todo.frame.FetchTodoTaskCount");
		container.addChild(subviewHolder);
	}

	public boolean isDisabled() {
		return disabled;
	}

	public int order() {
		return 1000;
	}
}
