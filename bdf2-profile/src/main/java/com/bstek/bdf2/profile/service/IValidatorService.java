package com.bstek.bdf2.profile.service;

import java.util.List;

import com.bstek.bdf2.profile.model.ValidatorDef;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public interface IValidatorService {
	public static final String BEAN_ID="bdf2.profile.validatorService";
	List<ValidatorDef> loadValidators(String componentId);
	void initValidatorsToCache();
}
