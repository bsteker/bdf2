package com.bstek.bdf2.jbpm4.view.todo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.jbpm4.model.TodoTask;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-3-27
 */
@Component("bdf2.jbpm4.todoTask")
public class TodoTaskMaintain {
	@Autowired
	@Qualifier(IBpmService.BEAN_ID)
	private IBpmService bpmService;
	@DataProvider
	public void loadPersonalTodoTasks(Page<TodoTask> page){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		bpmService.findPersonalTodoTasks(page,user.getUsername());
	}
	@DataProvider
	public void loadGroupTodoTasks(Page<TodoTask> page){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		bpmService.findGroupTodoTasks(page,user.getUsername());
	}
	
	@Expose
	public Map<String,Object> loadTodoTaskCount(){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		return bpmService.findTodoTaskCount(user.getUsername());
	}
}
