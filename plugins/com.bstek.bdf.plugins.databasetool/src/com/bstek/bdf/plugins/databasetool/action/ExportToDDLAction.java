/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.action;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.DbToolGefEditor;
import com.bstek.bdf.plugins.databasetool.exporter.ExportUtils;

public class ExportToDDLAction extends BaseSelectionAction {

	private static final String[] EXTENSIONS = new String[] { "*.sql" };

	public ExportToDDLAction(IWorkbenchPart part) {
		super(part);
	}

	protected void init() {
		super.init();
		setText("Export DDL");
		setId(ActionIdConstants.EXPORT_DDL_ACTION_ID);
		setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_DDL));
	}

	protected boolean calculateEnabled() {
		if (getWorkbenchPart() instanceof GraphicalEditor) {
			return true;
		}
		return false;
	}

	public void run() {
		FileDialog dialog = new FileDialog(getWorkbenchPart().getSite().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(getText());
		dialog.setOverwrite(true);
		dialog.setFileName("NewFile");
		final String savePath = dialog.open();
		if (savePath != null) {
			final DbToolGefEditor editor = (DbToolGefEditor) getWorkbenchPart().getAdapter(GraphicalEditor.class);
			IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("正在导出DDL文件...", IProgressMonitor.UNKNOWN);
					try {
						ExportUtils.exportDDL(editor.getModel(), savePath);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					monitor.done();
				}
			};
			try {
				new ProgressMonitorDialog(getWorkbenchPart().getSite().getShell()).run(true, true, runnableWithProgress);
				okMessage("生成DDL语句成功!");
			} catch (InvocationTargetException e1) {
				errorMessage("生成DDL语句失败!\n" + e1.getTargetException().getMessage());
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				errorMessage("生成DDL语句失败!\n" + e1.getMessage());
				e1.printStackTrace();
			}

		}
	}

}