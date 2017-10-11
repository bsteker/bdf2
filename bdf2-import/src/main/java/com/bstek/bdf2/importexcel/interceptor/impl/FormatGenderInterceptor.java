package com.bstek.bdf2.importexcel.interceptor.impl;

import org.springframework.stereotype.Service;


@Service("bdf2.formatGenderInterceptor")
public class FormatGenderInterceptor extends RequiredInterceptor {
	public Object execute(Object cellValue) throws Exception {
		super.execute(cellValue);
		if ("男".equals(cellValue)) {
			return "Y";
		} else {
			return "N";
		}
	}

	public String getName() {
		return "格式化性别[男=Y,女=N]";
	}

}
