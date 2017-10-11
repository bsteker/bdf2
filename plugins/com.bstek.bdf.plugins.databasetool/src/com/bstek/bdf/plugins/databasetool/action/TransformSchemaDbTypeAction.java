package com.bstek.bdf.plugins.databasetool.action;

import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.DbToolGefEditor;
import com.bstek.bdf.plugins.databasetool.command.TransformSchemaDbTypeCommand;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.wizard.pages.dialog.TransformSchemaDbTypeDialog;

public class TransformSchemaDbTypeAction extends BaseSelectionAction {

	public TransformSchemaDbTypeAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	protected void init() {
		setId(ActionIdConstants.CHANGE_DATABASE_ACTION_ID);
		setText("Change Database");
		setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_DATABASE_LINK));
	}

	@Override
	public void run() {
		DbToolGefEditor editor = (DbToolGefEditor) getWorkbenchPart().getAdapter(GraphicalEditor.class);
		final Schema schema = editor.getModel();
		final TransformSchemaDbTypeDialog dialog = new TransformSchemaDbTypeDialog(getWorkbenchPart().getSite().getShell(), schema);
		if (dialog.open() == Dialog.OK) {
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					execute(new TransformSchemaDbTypeCommand(schema, dialog.getDestDbType()));
				}
			});
		}
	}

	protected boolean calculateEnabled() {
		if (getWorkbenchPart() instanceof GraphicalEditor) {
			return true;
		}
		return false;
	}

}
