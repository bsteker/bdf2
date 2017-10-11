package com.bstek.bdf2.webservice.client;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.ws.security.WSConstants;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;


/**
 * 客户端调用WebService的辅助类<br>
 * 该类利用Spring-WS提供的WebServiceTemplate功能，可以快速实现对WebService的调用<br>
 * 既可以快速调用不需要认证的WebService，也可以快速调用符合WS-Security规范的需要认证或需要认证加密的WebService<br>
 * 同时利用JAXB2，可以实现XML与Java对象的互转换，从而实现WebService的快速调用目的
 * @author Jacky.gao
 * @since 2013-3-7
 */
class WebServiceClientTools extends WebServiceGatewaySupport{
	private HttpComponentsMessageSender sender;
	/**
	 * 设置需要进行Marshaller或Unmarshaller的Java类
	 * @param clazzs 用于进行Marshaller或Unmarshaller操作的Java类数组
	 */
	public void setMarshallerUnmarshallerClass(Class<?>[] clazzs){
		Jaxb2Marshaller marshaller=new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(clazzs);
		this.setMarshaller(marshaller);
		this.setUnmarshaller(marshaller);
	}


	/**
	 * 采用marshal方式将Java对象类型负载交给目标WebService，同时将目标WebService的响应（XML）转换成Java对象<br>
	 * 在调用该方法前需要调用setMarshallerUnmarshallerClass方法设置相关的Marshaller或Unmarshaller操作的Java类
	 * @param requestPayload 要交给目标WebService调用的Java对象
	 * @return 目标WebService返回的结果对象
	 */
	public Object marshalSendAndReceive(Object requestPayload){
		AxiomSoapMessageFactory factory=new AxiomSoapMessageFactory();
		factory.setAttachmentCaching(true);
		factory.setPayloadCaching(false);
		factory.setAttachmentCacheThreshold(1024);
		this.getWebServiceTemplate().setMessageFactory(factory);
		try {
			this.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this.getWebServiceTemplate().marshalSendAndReceive(requestPayload);
	}
	
	/**
	 * 要调用的目标WebService采用了基于标准的WS-Security认证时，调用该方法实现向SOAP消息当中添加UsernameToken，<br>
	 * 同时通过最后一个属性可以设置是否对提供的用户密码进行加密（当WebService服务器需要加密这里就需要设置为true,否则要设置为false）
	 * @param username 用于构建UsernameToken的用户名
	 * @param password 用于构建UsernameToken的用户密码
	 * @param encryptPassword 是否对用户密码进行加密
	 */
	public void setUsernameToken(String username,String password,boolean encryptPassword){
		boolean flag=false;
		ClientInterceptor[] interceptors=this.getInterceptors();
		if(interceptors!=null){
			for(ClientInterceptor clientInterceptor:this.getInterceptors()){
				if(clientInterceptor instanceof Wss4jSecurityInterceptor){
					buildWss4jSecurityInterceptor(clientInterceptor, username, password, encryptPassword);
					flag=true;
					break;
				}
			}
		}
		if(!flag){
			Wss4jSecurityInterceptor wss4jInterceptor=new Wss4jSecurityInterceptor();
			this.setInterceptors(new ClientInterceptor[]{buildWss4jSecurityInterceptor(wss4jInterceptor, username, password, encryptPassword)});
		}
	}
	
	public void setHttpAuthenticationCredentials(String username,String password){
		sender=new HttpComponentsMessageSender();
		UsernamePasswordCredentials credentials=new UsernamePasswordCredentials(username,password);
		sender.setCredentials(credentials);
		try {
			sender.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.getWebServiceTemplate().setMessageSender(sender);
	}
	
	/**
	 * 对提供的Wss4jSecurityInterceptor对象进行重新构建，添加UsernameToken动作，同时添加对应的用户名、密码以及是否对密码进行加密
	 * @param interceptor Wss4jSecurityInterceptor对象
	 * @param username 用户名
	 * @param password 密码
	 * @param encryptPassword 是否对密码进行加密
	 * @return 构建后的Wss4jSecurityInterceptor对象
	 */
	private ClientInterceptor buildWss4jSecurityInterceptor(ClientInterceptor interceptor,String username,String password,boolean encryptPassword){
		Wss4jSecurityInterceptor wss4jInterceptor=(Wss4jSecurityInterceptor)interceptor;
		wss4jInterceptor.setSecurementActions("UsernameToken");
		wss4jInterceptor.setSecurementUsername(username);
		wss4jInterceptor.setSecurementPassword(password);
		this.getWebServiceTemplate().getInterceptors();
		if(encryptPassword){
			wss4jInterceptor.setSecurementPasswordType(WSConstants.PW_DIGEST);			
		}else{
			wss4jInterceptor.setSecurementPasswordType(WSConstants.PW_TEXT);			
		}
		return wss4jInterceptor;
	}
}
