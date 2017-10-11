package com.bstek.bdf2.profile.listener.handler.validator;

import com.bstek.dorado.data.type.validator.Validator;

/**
 * @author Jacky.gao
 * @since 2013-3-4
 */
public interface IValidatorDefinition {
	String validatorClassSimpleName();
	Validator getInstance();
}
