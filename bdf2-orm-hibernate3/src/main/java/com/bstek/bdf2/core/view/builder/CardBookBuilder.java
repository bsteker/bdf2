package com.bstek.bdf2.core.view.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.base.CardBook;
/**
 * @author William.Jiang
 * @since 2014-7-21
 */
@Component
public class CardBookBuilder extends AbstractControlBuilder {
	public void build(Object control, ViewComponent parentViewComponent) {
		CardBook cardbook = (CardBook)control;
		String id = cardbook.getId();
		ViewComponent component = generateViewComponent(id, CardBook.class);
		component.setIcon(">dorado/res/" + CardBook.class.getName().replaceAll("\\.", "/")+".png");
		component.setName(CardBook.class.getSimpleName());
		if(StringUtils.isEmpty(component.getId())){
			component.setEnabled(false);
		}
		parentViewComponent.addChildren(component);	
		for (Control c: cardbook.getControls()){
			for (IControlBuilder builder : builders) {
				if (builder.support(c)) {
					builder.build(c, component);
					break;
				}
			}
		}
	}
	public boolean support(Object control){
		return (control instanceof CardBook);
	}
}
