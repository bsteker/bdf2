/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard.pages;

import java.sql.Connection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.wizard.ExportToDatabaseWizard;
import com.bstek.bdf.plugins.databasetool.wizard.ImportFromDatabaseWizard;

public class DbConectionWizardPage extends WizardPage {
	private Text dbTypeText;
	private Text driverClassNameText;
	private Text serverNameText;
	private Text serverPortText;
	private Text dataBaseNameText;
	private Text usernameText;
	private Text passwordText;
	private Text urlText;
	private Button buttonTestConnection;
	private Schema schema;
	private DbDialect dbDialect;
	private DbDriverMetaData dbDriverMetaData = new DbDriverMetaData();

	public DbConectionWizardPage(String pageName) {
		super(pageName);
		setTitle("数据库连接配置");
		setDescription("配置一个Jdbc连接");

	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		createDbConnectionControl(container);
		initDbConnectionControl();
		addListeners();
		setControl(container);
		setPageComplete(validatePage());
	}

	private void createDbConnectionControl(Composite container) {
		this.dbTypeText = createFieldControl(container, "数据库名称:");
		this.driverClassNameText = createFieldControl(container, "Jdbc驱动类:");
		this.serverNameText = createFieldControl(container, "服务器地址:");
		this.serverPortText = createFieldControl(container, "端口号:");
		this.dataBaseNameText = createFieldControl(container, "数据库名称:");
		this.usernameText = createFieldControl(container, "用户名:");
		this.passwordText = createFieldControl(container, "密码:", true);
		this.urlText = createFieldControl(container, "URL:", false, 1);
		this.buttonTestConnection = this.createTestConnectionControl(container);

	}

	private Button createTestConnectionControl(Composite container) {
		Button button = new Button(container, SWT.PUSH);
		button.setText("测试连接");
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		gd.widthHint = 80;
		button.setLayoutData(gd);
		return button;
	}

	private String testConnection(String url, String username, String password) {
		Connection con = null;
		try {
			con = DbJdbcUtils.getConnection(schema.getCurrentDbType(), url, username, password);
		} catch (Exception e) {
			return e.getMessage();
		} finally {
			DbJdbcUtils.closeConnection(con);
		}
		return null;
	}

	private void initDbConnectionControl() {
		if (this.getWizard() instanceof ExportToDatabaseWizard) {
			ExportToDatabaseWizard wizard = (ExportToDatabaseWizard) this.getWizard();
			schema = wizard.getSchema();
		} else if (this.getWizard() instanceof ImportFromDatabaseWizard) {
			ImportFromDatabaseWizard wizard = (ImportFromDatabaseWizard) this.getWizard();
			schema = wizard.getSchema();
		} else {
			return;
		}
		dbDialect = DbDialectManager.getDbDialect(schema.getCurrentDbType());
		dbTypeText.setText(schema.getCurrentDbType());
		dbTypeText.setEnabled(false);

		driverClassNameText.setText(dbDialect.getJdbcDriverName());
		driverClassNameText.setEnabled(false);

		serverNameText.setText("localhost");
		serverNameText.selectAll();
		serverNameText.setFocus();

		serverPortText.setText(String.valueOf(dbDialect.getDefaultPort()));

		urlText.setText(dbDialect.getUrl("localhost", dbDialect.getDefaultPort(), ""));

	}

	private void addListeners() {
		serverNameText.addListener(SWT.Modify, modifyTextlistener);
		serverPortText.addListener(SWT.Modify, modifyTextlistener);
		dataBaseNameText.addListener(SWT.Modify, modifyTextlistener);
		usernameText.addListener(SWT.Modify, modifyTextlistener);
		passwordText.addListener(SWT.Modify, modifyTextlistener);
		urlText.addListener(SWT.Modify, modifyTextlistener);
		serverPortText.addVerifyListener(new VerifyNumberListener());
		buttonTestConnection.addSelectionListener(new TestConnectionButtonClickListener());
	}

	private Text createFieldControl(Composite parent, String labelName) {
		return createFieldControl(parent, labelName, false);
	}

	private Text createFieldControl(Composite parent, String labelName, boolean password) {
		return createFieldControl(parent, labelName, password, 2);
	}

	private Text createFieldControl(Composite parent, String labelName, boolean password, int textHorizontalSpan) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText(labelName);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		label.setLayoutData(gd);
		int stype = SWT.BORDER | SWT.SINGLE;
		if (password) {
			stype = stype | SWT.PASSWORD;
		}
		Text textControl = new Text(parent, stype);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = textHorizontalSpan;
		textControl.setLayoutData(gd);
		return textControl;
	}

	private class VerifyNumberListener implements VerifyListener {
		public void verifyText(VerifyEvent event) {
			if (event.text != null && event.text.length() > 0) {
				event.doit = Character.isDigit(event.text.charAt(0));
			}
		}
	}

	private class TestConnectionButtonClickListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			String url = urlText.getText();
			String username = usernameText.getText();
			String password = passwordText.getText();
			String testResult = testConnection(url, username, password);
			if (testResult == null) {
				MessageDialog.openInformation(getShell(), "提示", "测试连接成功！");
			} else {
				MessageDialog.openError(getShell(), "错误", "测试连接失败!\n" + testResult);
			}
		}
	}

	private Listener modifyTextlistener = new Listener() {
		public void handleEvent(Event event) {
			dbDriverMetaData.setDbType(dbTypeText.getText());
			dbDriverMetaData.setUsername(usernameText.getText());
			dbDriverMetaData.setPassword(passwordText.getText());

			if (!event.widget.equals(urlText)) {
				urlText.setText(dbDialect.getUrl(serverNameText.getText(), Integer.valueOf(serverPortText.getText()), dataBaseNameText.getText()));
			}
			dbDriverMetaData.setUrl(urlText.getText());
		}
	};

	@Override
	public IWizardPage getNextPage() {
		IWizardPage page = super.getNextPage();
		if (page != null && page.getControl() != null) {
			page.dispose();
		}
		return super.getNextPage();
	}

	public boolean finish() {
		return true;
	}

	protected boolean validatePage() {
		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	public DbDriverMetaData getDbDriverMetaData() {
		return dbDriverMetaData;
	}

}
