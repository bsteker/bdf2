package com.bstek.bdf.plugins.databasetool.action;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;

public abstract class BaseSelectionAction extends SelectionAction {

	public BaseSelectionAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	protected boolean calculateEnabled() {
		return false;
	}

	public void errorMessage(String message) {
		MessageDialog.openError(getWorkbenchPart().getSite().getShell(), getText(), message);
	}

	public void okMessage(String message) {
		MessageDialog.openInformation(getWorkbenchPart().getSite().getShell(), getText(), message);
	}

}
