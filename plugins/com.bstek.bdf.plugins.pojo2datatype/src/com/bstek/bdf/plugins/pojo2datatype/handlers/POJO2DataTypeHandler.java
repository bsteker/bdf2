package com.bstek.bdf.plugins.pojo2datatype.handlers;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bstek.bdf.plugins.pojo2datatype.POJO2DataTypeWizard;

/**
 * 右键菜单启动向导命令处理器
 * @author Jake.Wang@bstek.com
 * @since Dec 25, 2012
 *
 */
public class POJO2DataTypeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		POJO2DataTypeWizard wizard = new POJO2DataTypeWizard();
		wizard.init(window.getWorkbench(), 
				selection instanceof IStructuredSelection
				? (IStructuredSelection) selection : StructuredSelection.EMPTY);
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
		return null;
	}
}
