package com.bstek.bdf2.componentprofile.listener;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.bstek.bdf2.componentprofile.listener.impl.AutoFormProfileFilter;
import com.bstek.bdf2.componentprofile.listener.impl.DataGridProfileFilter;
import com.bstek.bdf2.componentprofile.model.ComponentConfig;
import com.bstek.bdf2.componentprofile.service.ComponentProfileService;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.grid.DataGrid;

public class ViewListener extends GenericObjectListener<View> {
	private ComponentProfileService componentProfileService;
	private AutoFormProfileFilter autoFormProfileFilter = new AutoFormProfileFilter();
	private DataGridProfileFilter dataGridProfileFilter = new DataGridProfileFilter();

	@Override
	public boolean beforeInit(View view) throws Exception {
		return true;
	}

	@Override
	public void onInit(View view) throws Exception {
		String viewName = view.getName();

		String profileKey = componentProfileService.getProfileKey();

		Stack<Iterator<ViewElement>> viewElementsIteratorStack = new Stack<Iterator<ViewElement>>();

		Map<String, ComponentConfig> configs = componentProfileService.loadComponentConfigByView(profileKey, viewName);

		Collection<ViewElement> innerElements = view.getInnerElements();
		if (innerElements == null) {
			return;
		}
		Iterator<ViewElement> currentIterator = view.getInnerElements().iterator();
		viewElementsIteratorStack.push(currentIterator);

		while (!viewElementsIteratorStack.isEmpty()) {
			currentIterator = viewElementsIteratorStack.pop();
			while (currentIterator.hasNext()) {
				ViewElement viewElement = currentIterator.next();
				if (viewElement instanceof AutoForm) {
					AutoForm autoForm = (AutoForm) viewElement;
					ComponentConfig config = configs.get(viewName + "." + autoForm.getId());
					if (config != null) {
						autoFormProfileFilter.configure(autoForm, config);
					}
				} else if (viewElement instanceof DataGrid) {
					DataGrid dataGrid = (DataGrid) viewElement;
					ComponentConfig config = configs.get(viewName + "." + dataGrid.getId());
					if (config != null) {
						dataGridProfileFilter.configure(dataGrid, config);
					}
				}
				innerElements = viewElement.getInnerElements();

				if (innerElements != null && innerElements.size() > 0) {
					viewElementsIteratorStack.push(currentIterator);
					currentIterator = innerElements.iterator();
				}
			}
		}
	}

	public ComponentProfileService getComponentProfileService() {
		return componentProfileService;
	}

	public void setComponentProfileService(ComponentProfileService componentProfileService) {
		this.componentProfileService = componentProfileService;
	}

}
