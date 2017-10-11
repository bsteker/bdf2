package com.bstek.bdf.plugins.pojo2datatype;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bstek.bdf.plugins.pojo2datatype.generator.ModelBuilder;
import com.bstek.bdf.plugins.pojo2datatype.model.Node;
import com.bstek.bdf.plugins.pojo2datatype.utils.ProjectHelper;

/**
 * 将Java类转换为Model文件向导
 * @author Jake.Wang@bstek.com
 * @since Dec 18, 2012
 * 
 */
public class POJO2DataTypeWizard extends Wizard implements INewWizard {
	private SelectClassPage selectClassPage;
	private ModelFilePage modelFilePage;

	public POJO2DataTypeWizard(){
		setWindowTitle("POJO2DataType Wizard");
		selectClassPage = new SelectClassPage();
		modelFilePage = new ModelFilePage(ProjectHelper.getCurrentSelection());
	}
	@Override
	public void addPages() {
		addPage(selectClassPage);
		addPage(modelFilePage);
	}

	@Override
	public boolean canFinish() {
		if (modelFilePage.isCurrentPage() && modelFilePage.isPageComplete()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean performFinish() {
		List<Node> nodes = getSelectedClasses();
		if (modelFilePage.isNew()) {
			new ModelBuilder().build(nodes, getNewFilePath(), true);
		} else {
			new ModelBuilder().build(nodes, getExistingFilePath(), false);
		}
		refreshProject();
		return true;
	}

	private void refreshProject() {
		try {
			ProjectHelper.getCurrentProject().refreshLocal(
					IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private List<Node> getSelectedClasses() {
		return selectClassPage.getSelectedClasses();
	}

	private String getNewFilePath() {
		return modelFilePage.getNewFilePath();
	}

	private String getExistingFilePath() {
		return modelFilePage.getFilePath();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if(selection.isEmpty()){
			// do nothing
		}else{
			selectClassPage.init(selection);
		}
	}
}
