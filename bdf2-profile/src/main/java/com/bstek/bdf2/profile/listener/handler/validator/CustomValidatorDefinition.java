package com.bstek.bdf2.profile.listener.handler.validator;

import org.springframework.stereotype.Component;

import com.bstek.dorado.data.type.validator.Validator;
import com.bstek.dorado.view.type.property.validator.CustomValidator;
/**
 * @author Jacky.gao
 * @since 2013-3-4
 */
@Component
public class CustomValidatorDefinition implements IValidatorDefinition{

	public String validatorClassSimpleName() {
		return CustomValidator.class.getSimpleName();
	}

	public Validator getInstance() {
		return new CustomValidator();
	}

}
