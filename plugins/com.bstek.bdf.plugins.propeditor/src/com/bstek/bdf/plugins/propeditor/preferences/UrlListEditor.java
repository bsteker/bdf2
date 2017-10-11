package com.bstek.bdf.plugins.propeditor.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class UrlListEditor extends ListEditor {
	public static final String LISTDELIMITER = ";;";
	public static final String PROJECTDELIMITER = "---";
	public static final String[] VERSION_URL = new String[] { "bdf.popertiesLoderController.downXmlPropdesc.c",
			"properties.list.action" };

	public UrlListEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected String createList(String[] list) {
		StringBuilder builder = new StringBuilder();
		String dm = "";
		for (String str : list) {
			builder.append(dm).append(str);
			dm = LISTDELIMITER;
		}
		return builder.toString();
	}

	@Override
	protected String getNewInputObject() {
		InputDialog dialog = new InputDialog(Display.getCurrent().getActiveShell(), "添加新的URL", "URL地址:", null, null);
		dialog.create();
		dialog.open();
		String text = dialog.getValue();
		return text;
	}

	@Override
	protected String[] parseString(String str) {
		return str.split(LISTDELIMITER);
	}

}
