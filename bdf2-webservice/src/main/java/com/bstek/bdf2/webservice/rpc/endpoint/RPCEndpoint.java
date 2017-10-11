package com.bstek.bdf2.webservice.rpc.endpoint;

import java.lang.reflect.Method;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.webservice.jaxb.IWebservice;
import com.bstek.bdf2.webservice.rpc.Namespace;
import com.bstek.bdf2.webservice.rpc.data.DataRequest;
import com.bstek.bdf2.webservice.rpc.data.DataResponse;

/**
 * @author Jacky.gao
 * @since 2013年7月12日
 */
@Endpoint
public class RPCEndpoint implements IWebservice{
	@PayloadRoot(localPart="DataRequest",namespace=Namespace.NS)
	public @ResponsePayload DataResponse service(@RequestPayload DataRequest request){
		String beanId=request.getBeanId();
		String methodName=request.getMethodName();
		String[] args=request.getMethodArgements();
		DataResponse response=new DataResponse();
		try{
			
			Object targetBean=ContextHolder.getBean(beanId);
			Method method=null;
			if(args==null || args.length==0){
				method=targetBean.getClass().getMethod(methodName);
			}else{
				Class<?>[] classes=new Class<?>[args.length];
				for(int i=0;i<args.length;i++){
					classes[i]=String.class;
				}
				method=targetBean.getClass().getMethod(methodName, classes);				
			}
			Object returnValue=null;
			if(args!=null && args.length>0){
				Object[] objs=new Object[args.length];
				for(int i=0;i<args.length;i++){
					objs[i]=args[i];
				}
				returnValue=method.invoke(targetBean, objs);				
			}else{
				returnValue=method.invoke(targetBean, new Object[]{});								
			}
			if(returnValue!=null){
				response.setReturnValue(returnValue.toString());				
			}
			response.setSuccessful(true);
		}catch(Exception ex){
			response.setReturnValue(ex.getMessage());
			response.setSuccessful(false);
		}
		return response;
	}

	public Class<?>[] bindClasses() {
		return new Class<?>[]{DataResponse.class,DataRequest.class};
	}

	public boolean useSecurity() {
		return true;
	}
}
