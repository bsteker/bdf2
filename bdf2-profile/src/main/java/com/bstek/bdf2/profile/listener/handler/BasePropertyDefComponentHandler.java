package com.bstek.bdf2.profile.listener.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.profile.listener.handler.validator.IValidatorDefinition;
import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.model.ValidatorDef;
import com.bstek.bdf2.profile.model.ValidatorEvent;
import com.bstek.bdf2.profile.model.ValidatorProperty;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.bdf2.profile.service.IValidatorService;
import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.data.type.validator.Validator;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
@Component
public class BasePropertyDefComponentHandler extends AbstractComponentHandler implements ApplicationContextAware {
	private Collection<IValidatorDefinition> validatorDefinitions;
	@Autowired
	@Qualifier(IValidatorService.BEAN_ID)
	private IValidatorService validatorService;
	public void handle(IComponentService componentService, Object control,
			String assignTargetId,Map<String,ComponentInfo> mapInCache) {
		BasePropertyDef propertyDef=this.getControl(control);
		String controlId=propertyDef.getName();
		String componentId=setControlPropertiesAndEvents(componentService,assignTargetId,propertyDef,controlId,"PropertyDef",mapInCache);
		if(componentId==null)return;
		List<ValidatorDef> validatorDefs=validatorService.loadValidators(componentId);
		if(validatorDefs==null || validatorDefs.size()==0)return;
		List<Validator> validators=propertyDef.getValidators();
		if(validators==null){
			validators=new ArrayList<Validator>();
			propertyDef.setValidators(validators);
		}
		for(ValidatorDef validatorDef:validatorDefs){
			String type=validatorDef.getType();
			Validator targetValidator=null;
			for(Validator validator:validators){
				if(validator.getClass().getSimpleName().equals(type)){
					targetValidator=validator;
					break;
				}
			}
			if(targetValidator==null){
				targetValidator = buildValidator(type);
			}
			if(targetValidator==null)continue;
			validators.add(targetValidator);
			for(ValidatorProperty vp:validatorDef.getProperties()){
				String pname=vp.getName();
				String pvalue=vp.getValue();
				if(StringUtils.isNotEmpty(pname) && StringUtils.isNotEmpty(pvalue)){
					try {
						BeanUtils.setProperty(targetValidator, pname, pvalue);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			if(!(targetValidator instanceof ClientEventSupported))return;
			ClientEventSupported eventPropertyDef=(ClientEventSupported)targetValidator;
			for(ValidatorEvent event:validatorDef.getEvents()){
				String eventName=event.getName();
				String script=event.getContent();
				if(StringUtils.isNotEmpty(eventName) && StringUtils.isNotEmpty(script)){
					eventPropertyDef.addClientEventListener(eventName, new DefaultClientEvent(script));
				}
			}
		}
	}

	private Validator buildValidator(String type) {
		Validator targetValidator=null;
		for(IValidatorDefinition v:validatorDefinitions){
			if(v.validatorClassSimpleName().equals(type)){
				targetValidator=v.getInstance();
				break;
			}
		}
		return targetValidator;
	}

	public boolean support(Object control) {
		return control instanceof BasePropertyDef;
	}
	public void setValidatorService(IValidatorService validatorService) {
		this.validatorService = validatorService;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		validatorDefinitions=applicationContext.getBeansOfType(IValidatorDefinition.class).values();
	}
}
