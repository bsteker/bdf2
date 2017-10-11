package com.bstek.bdf2.core.view.builder;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.core.resource.LocaleResolver;
import com.bstek.dorado.view.widget.datacontrol.DataPilot;

@Component
public class DataPilotBuilder implements IControlBuilder {
	@Autowired
	@Qualifier("dorado.localeResolver")
	private LocaleResolver localeResolver;
	public void build(Object control, ViewComponent parentViewComponent) {
		Locale locale=null;
		try {
			locale = localeResolver.resolveLocale();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		String language=locale!=null?locale.getLanguage():"zh";
		DataPilot dataPilot=(DataPilot)control;
		String codes=dataPilot.getItemCodes();
		if(codes==null){
			return;
		}
		for(String code:codes.split(",")){
			String id=null;
			if(code.equals("+")){
				if(language.equals("zh")){
					id="添加";
				}else{
					id="Add";					
				}
			}else if(code.equals("-")){
				if(language.equals("zh")){
					id="删除";
				}else{
					id="Del";					
				}
			}
			if(StringUtils.isNotEmpty(id)){
				ViewComponent component=new ViewComponent();
				component.setId(id);
				component.setIcon(">dorado/res/"+DataPilot.class.getName().replaceAll("\\.", "/")+".png");
				component.setName(DataPilot.class.getSimpleName());
				parentViewComponent.addChildren(component);
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof DataPilot;
	}
}
