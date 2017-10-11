package com.bstek.bdf2.profile.listener.handler.validator;

import org.springframework.stereotype.Component;

import com.bstek.dorado.data.type.validator.Validator;
import com.bstek.dorado.view.type.property.validator.LengthValidator;
/**
 * @author Jacky.gao
 * @since 2013-3-4
 */
@Component
public class LengthValidatorDefinition implements IValidatorDefinition{

	public String validatorClassSimpleName() {
		return LengthValidator.class.getSimpleName();
	}

	public Validator getInstance() {
		return new LengthValidator();
	}

}
