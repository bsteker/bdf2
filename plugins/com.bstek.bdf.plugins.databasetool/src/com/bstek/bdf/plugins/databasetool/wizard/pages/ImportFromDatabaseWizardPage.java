package com.bstek.bdf.plugins.databasetool.wizard.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.wizard.ImportFromDatabaseWizard;
import com.bstek.bdf.plugins.databasetool.wizard.pages.provider.TableTreeNode;
import com.bstek.bdf.plugins.databasetool.wizard.pages.provider.TableTreeNodeContentProvider;
import com.bstek.bdf.plugins.databasetool.wizard.pages.provider.TableTreeNodeFactory;
import com.bstek.bdf.plugins.databasetool.wizard.pages.provider.TableTreeNodeLabelProvider;

public class ImportFromDatabaseWizardPage extends WizardPage {
	private CheckboxTreeViewer checkboxTreeViewer;
	private DbDriverMetaData dbDriverMetaData;
	private List<String> checkedTables = new ArrayList<String>();

	public ImportFromDatabaseWizardPage(String pageName) {
		super(pageName);
		setTitle("选择数据库表");
		setDescription("选择要导入的数据库表");
	}

	@Override
	public void createControl(Composite parent) {
		setPageComplete(false);
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
		createTreeViewer(container);
		initTreeViewerData();
		addListeners();
		setControl(container);

	}

	private void createTreeViewer(Composite container) {
		checkboxTreeViewer = new CheckboxTreeViewer(container, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		checkboxTreeViewer.getTree().setLayoutData(gd);
		checkboxTreeViewer.setLabelProvider(new TableTreeNodeLabelProvider());
		checkboxTreeViewer.setContentProvider(new TableTreeNodeContentProvider());

	}

	private void initTreeViewerData() {
		if (this.getWizard() instanceof ImportFromDatabaseWizard) {
			ImportFromDatabaseWizard wizard = (ImportFromDatabaseWizard) this.getWizard();
			dbDriverMetaData = wizard.getDbConectionWizardPage().getDbDriverMetaData();
		}
		if (dbDriverMetaData.getDbType() == null) {
			setErrorMessage("数据库连接不可以用！");
			return;
		}
		setErrorMessage(null);
		setMessage("正在加载数据库表，请耐心等待....");
		Job job = new Job("加载数据库表") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask("加载数据库表....", IProgressMonitor.UNKNOWN);
					getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							TableTreeNode[] treeDatas = null;
							try {
								treeDatas = TableTreeNodeFactory.getInstence().getTreeDatas(dbDriverMetaData);
								setErrorMessage(null);
								setMessage(getDescription());
								setPageComplete(true);
							} catch (Exception e) {
								setErrorMessage("数据库连接异常："+e.getMessage());
								setPageComplete(false);
								e.printStackTrace();
							}
							checkboxTreeViewer.setInput(treeDatas);
						}
					});
					monitor.done();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	private void addListeners() {
		checkboxTreeViewer.addCheckStateListener(new TreeViewerCheckStateListener());
	}

	private class TreeViewerCheckStateListener implements ICheckStateListener {
		@Override
		public void checkStateChanged(CheckStateChangedEvent event) {
			checkboxTreeViewerCheckStateChanged(event);
		}
	}

	private void checkboxTreeViewerCheckStateChanged(CheckStateChangedEvent event) {
		CheckboxTreeViewer viewer = (CheckboxTreeViewer) event.getSource();
		TableTreeNode node = (TableTreeNode) event.getElement();
		if (event.getChecked()) {
			viewer.setSubtreeChecked(node, true);
		} else {
			viewer.setSubtreeChecked(node, false);
			if (node.getParent() != null) {
				viewer.setChecked(node.getParent(), false);
			}
		}
		Object[] elements = viewer.getCheckedElements();
		setPageComplete(elements.length > 0 ? true : false);

		checkedTables.clear();
		TableTreeNode tableTreeNode;
		for (Object obj : elements) {
			tableTreeNode = (TableTreeNode) obj;
			if (tableTreeNode.isTable) {
				checkedTables.add(tableTreeNode.getName());
			} else {
				dbDriverMetaData.setDbSchema(tableTreeNode.getName());
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		setControl(null);
	}

	public DbDriverMetaData getDbDriverMetaData() {
		return dbDriverMetaData;
	}

	public void setDbDriverMetaData(DbDriverMetaData dbDriverMetaData) {
		this.dbDriverMetaData = dbDriverMetaData;
	}

	public List<String> getCheckedTables() {
		return checkedTables;
	}

}
