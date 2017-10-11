package com.bstek.bdf.plugins.databasetool.wizard.pages;

import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;
import com.bstek.bdf.plugins.databasetool.wizard.ExportToDatabaseWizard;

public class ExportToDatabaseWizardPage extends WizardPage {

	private boolean deleleTable = false;
	private Group ruleGroup;

	public ExportToDatabaseWizardPage(String pageName) {
		super(pageName);
		setTitle("同步模型到数据库");
		setDescription("设置同步规则");
	}

	@Override
	public void createControl(Composite parent) {
		setPageComplete(false);
		checkConnection();
		ruleGroup = new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		ruleGroup.setLayout(layout);
		ruleGroup.setText("同步规则");
		ruleGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		final Button deleteButton = new Button(ruleGroup, SWT.CHECK);
		deleteButton.setText("删除已经存在模型中的表");
		deleteButton.setEnabled(false);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (deleteButton.getSelection()) {
					setDeleleTable(true);
				} else {
					setDeleleTable(false);
				}
			}

		});
		setControl(ruleGroup);

	}

	private void setRuleGroupEnable(boolean enabled) {
		if (ruleGroup != null) {
			Control[] controls = ruleGroup.getChildren();
			for (Control c : controls) {
				c.setEnabled(enabled);
			}
		}
	}

	private void checkConnection() {
		if (this.getWizard() instanceof ExportToDatabaseWizard) {
			ExportToDatabaseWizard wizard = (ExportToDatabaseWizard) this.getWizard();
			final DbDriverMetaData dbDriverMetaData = wizard.getDbConectionWizardPage().getDbDriverMetaData();
			if (dbDriverMetaData.getDbType() == null) {
				setErrorMessage("数据库连接不可以用！");
				return;
			}
			setErrorMessage(null);
			setMessage("正在检查数据库连接，请耐心等待....");
			Job job = new Job("检查数据库连接") {
				protected IStatus run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("检查数据库连接....", IProgressMonitor.UNKNOWN);
						getShell().getDisplay().asyncExec(new Runnable() {
							public void run() {
								Connection conn = null;
								try {
									conn = DbJdbcUtils.getConnection(dbDriverMetaData.getDbType(), dbDriverMetaData.getUrl(),
											dbDriverMetaData.getUsername(), dbDriverMetaData.getPassword());
									setErrorMessage(null);
									setMessage(getDescription());
									setRuleGroupEnable(true);
									setPageComplete(true);
								} catch (Exception e) {
									setErrorMessage("数据库连接异常：" + e.getMessage());
									setRuleGroupEnable(false);
									setPageComplete(false);
									e.printStackTrace();
								} finally {
									DbJdbcUtils.closeConnection(conn);
								}
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

	}

	@Override
	public void dispose() {
		super.dispose();
		setControl(null);
	}

	public boolean isDeleleTable() {
		return deleleTable;
	}

	public void setDeleleTable(boolean deleleTable) {
		this.deleleTable = deleleTable;
	}

}
