package com.bstek.bdf2.rapido.bsh;

import java.util.HashMap;
import java.util.Map;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.web.DoradoContext;

public class ContextVariableRegister implements VariableRegister {

	public Map<String, VariableInfo> register(){
		Map<String, VariableInfo> map=new HashMap<String,VariableInfo>();
		VariableInfo doradoContext=new VariableInfo();
		doradoContext.setName("doradoContext");
		doradoContext.setDesc("Dorado7提供的DoradoContext对象实例");
		doradoContext.setVariableExecutor(new VariableExecutor(){
			public Object execute() {
				return DoradoContext.getCurrent();
			}
		});
		
		VariableInfo applicationContext=new VariableInfo();
		applicationContext.setName("applicationContext");
		applicationContext.setDesc("Spring提供的ApplicationContext对象实例");
		applicationContext.setVariableExecutor(new VariableExecutor(){
			public Object execute() {
				return ContextHolder.getApplicationContext();
			}
		});
		
		VariableInfo request=new VariableInfo();
		request.setName("request");
		request.setDesc("HttpServletRequest对象实例");
		request.setVariableExecutor(new VariableExecutor(){
			public Object execute() {
				return ContextHolder.getRequest();
			}
		});
		
		VariableInfo response=new VariableInfo();
		response.setName("response");
		response.setDesc("HttpServletResponse对象实例");
		response.setVariableExecutor(new VariableExecutor(){
			public Object execute() {
				return ContextHolder.getResponse();
			}
		});
		
		VariableInfo session=new VariableInfo();
		session.setName("session");
		session.setDesc("当前用户会话的Session对象实例");
		session.setVariableExecutor(new VariableExecutor(){
			public Object execute() {
				return ContextHolder.getRequest().getSession();
			}
		});
		
		map.put("doradoContext", doradoContext);
		map.put("applicationContext", applicationContext);
		map.put("request", request);
		map.put("response", response);
		map.put("session", session);
		return map;
	}

}
