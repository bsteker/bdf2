/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.action;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import org.eclipse.jdt.internal.corext.util.Strings;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.DbToolGefEditor;
import com.bstek.bdf.plugins.databasetool.exporter.ExportUtils;
import com.bstek.bdf.plugins.databasetool.exporter.JavaBeanProperty;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.wizard.ExportToJavaBeanWizard;

@SuppressWarnings("restriction")
public class ExportToJavaBeanAction extends BaseSelectionAction {

	private static final boolean FORMAT_CODE = true;

	public ExportToJavaBeanAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	@Override
	protected void init() {
		setId(ActionIdConstants.EXPORT_JAVABEAN_ACTION_ID);
		setText("Export JavaBean");
		setEnabled(false);
		setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_JAVABEAN));

	}

	@Override
	public void run() {
		final ExportToJavaBeanWizard wizard = new ExportToJavaBeanWizard();
		WizardDialog dialog = new WizardDialog(getWorkbenchPart().getSite().getShell(), wizard);
		dialog.setHelpAvailable(false);
		if (dialog.open() == Dialog.OK) {
			IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("正在生成java文件...", IProgressMonitor.UNKNOWN);
					doRun(wizard, monitor);
					monitor.done();
				}
			};
			try {
				new ProgressMonitorDialog(getWorkbenchPart().getSite().getShell()).run(true, true, runnableWithProgress);
			} catch (InvocationTargetException e1) {
				errorMessage(e1.getTargetException().getMessage());
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				errorMessage(e1.getMessage());
				e1.printStackTrace();
			}
				

		}
	}

	private void doRun(ExportToJavaBeanWizard wizard, IProgressMonitor monitor) {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		String packageName = wizard.getPackageName();
		DbToolGefEditor editor = (DbToolGefEditor) getWorkbenchPart().getAdapter(GraphicalEditor.class);
		Schema schema = editor.getModel();
		IPackageFragmentRoot root = wizard.getPage().getPackageFragmentRoot();
		IPackageFragment pack = root.getPackageFragment(packageName);
		try {
			if (!pack.exists()) {
				pack = root.createPackageFragment(pack.getElementName(), true, monitor);
			}
			List<JavaBeanProperty> beans = ExportUtils.exportJavaBeans(packageName, wizard.isDoradoPropertyDef(), wizard.isHiberanteAnnotation(),
					schema);
			for (JavaBeanProperty bean : beans) {
				ICompilationUnit cu = pack.createCompilationUnit(bean.getClassName() + ExportUtils.JAVA_EXTENSION, bean.getContent(), true, monitor);
				if (FORMAT_CODE) {
					cu.becomeWorkingCopy(monitor);
					IBuffer buffer = cu.getBuffer();
					IType type = cu.getTypes()[0];
					ISourceRange sourceRange = type.getSourceRange();
					String originalContent = buffer.getText(sourceRange.getOffset(), sourceRange.getLength());
					String formattedContent = CodeFormatterUtil.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS, originalContent, 0, "\n",
							cu.getJavaProject());
					formattedContent = Strings.trimLeadingTabsAndSpaces(formattedContent);
					buffer.replace(sourceRange.getOffset(), sourceRange.getLength(), formattedContent);
					cu.commitWorkingCopy(true, monitor);
					if (cu != null) {
						cu.discardWorkingCopy();
					}
				}
			}
		} catch (Exception e) {
			errorMessage("生成JavaBean失败!\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected boolean calculateEnabled() {
		if (getWorkbenchPart() instanceof GraphicalEditor) {
			return true;
		}
		return false;
	}

}
