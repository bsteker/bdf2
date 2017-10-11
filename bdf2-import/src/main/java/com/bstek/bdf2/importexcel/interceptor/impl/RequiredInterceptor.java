package com.bstek.bdf2.importexcel.interceptor.impl;

import org.springframework.stereotype.Service;

import com.bstek.bdf2.importexcel.exception.InterceptorException;
import com.bstek.bdf2.importexcel.interceptor.ICellDataInterceptor;

@Service("bdf2.requiredInterceptor")
public class RequiredInterceptor implements ICellDataInterceptor {
	public Object execute(Object cellValue) throws Exception {
		if (cellValue == null) {
			throw new InterceptorException("当前单元格内容不能为空！");
		}
		return cellValue;
	}

	public String getName() {
		return "格式化单元格不能为空";
	}

}
