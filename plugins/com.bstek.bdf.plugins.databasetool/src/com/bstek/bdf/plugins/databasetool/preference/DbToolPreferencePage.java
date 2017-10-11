package com.bstek.bdf.plugins.databasetool.preference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.wizard.pages.dialog.DbDriverLocationDialog;

public class DbToolPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private TableViewer tableViewer;
	private Button buttonSetJarLoaction;
	private List<DbDriverMetaData> dbDriverMetaDataList;
	private DbDriverMetaData currentDbDriverMetaData;

	@Override
	public void init(IWorkbench workbench) {
		initDbDriverMetaData();
	}

	@Override
	public boolean performOk() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		for (DbDriverMetaData dbDriverMetaData : dbDriverMetaDataList) {
			store.setValue(dbDriverMetaData.getDbType(), dbDriverMetaData.getDriverLocation());
		}
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		for (DbDriverMetaData dbDriverMetaData : dbDriverMetaDataList) {
			dbDriverMetaData.setDriverLocation("");
			store.setValue(dbDriverMetaData.getDbType(), "");
		}
		tableViewer.refresh();
		super.performDefaults();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 10;
		createTable(composite);
		createButton(composite);
		return composite;
	}

	private void createTable(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		gd.heightHint = 200;
		tableViewer.getTable().setLayoutData(gd);

		String[] columnIndex = new String[] { "数据库名称", "Jdbc驱动类", "Jdbc驱动位置" };
		Integer[] columnWidth = new Integer[] { 80, 220, 400 };
		Integer[] columnAlign = new Integer[] { SWT.LEFT, SWT.LEFT, SWT.LEFT };
		for (int i = 0; i < columnIndex.length; i++) {
			TableViewerColumn choice = new TableViewerColumn(tableViewer, SWT.BORDER);
			choice.getColumn().setText(columnIndex[i]);
			choice.getColumn().setWidth(columnWidth[i]);
			choice.getColumn().setAlignment(columnAlign[i]);
		}
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TableColumnLabelProvider());
		tableViewer.setInput(dbDriverMetaDataList);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(final SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					buttonSetJarLoaction.setEnabled(true);
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					currentDbDriverMetaData = (DbDriverMetaData) selection.getFirstElement();
				}
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openDbDriverLocationDialog();
			}
		});
	}

	private void createButton(Composite parent) {
		buttonSetJarLoaction = new Button(parent, SWT.PUSH);
		buttonSetJarLoaction.setText("设置Jdbc驱动位置");
		buttonSetJarLoaction.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openDbDriverLocationDialog();
			}
		});
		buttonSetJarLoaction.setEnabled(false);
	}

	private void openDbDriverLocationDialog() {
		if (currentDbDriverMetaData != null) {
			DbDriverLocationDialog dialog = new DbDriverLocationDialog(getShell(), currentDbDriverMetaData);
			if (dialog.open() == Dialog.OK) {
				currentDbDriverMetaData.setDriverLocation(dialog.getDriverLocation());
				tableViewer.refresh();
			}
		}
	}

	private void initDbDriverMetaData() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		dbDriverMetaDataList = new ArrayList<DbDriverMetaData>();
		List<DbDialect> dialects = DbDialectManager.getDialects();
		DbDriverMetaData dbDriverMetaData = null;
		for (DbDialect dbDialect : dialects) {
			dbDriverMetaData = new DbDriverMetaData();
			dbDriverMetaData.setDbNmae(dbDialect.getDbType());
			dbDriverMetaData.setDriverClassName(dbDialect.getJdbcDriverName());
			dbDriverMetaData.setDriverLocation(store.getString(dbDialect.getDbType()));
			dbDriverMetaDataList.add(dbDriverMetaData);
		}
	}

	private class TableColumnLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int index) {
			DbDriverMetaData dbDriverMetaData = (DbDriverMetaData) element;
			switch (index) {
			case 0:
				return dbDriverMetaData.getDbType();
			case 1:
				return dbDriverMetaData.getDriverClassName();
			case 2:
				return dbDriverMetaData.getDriverLocation();
			default:
				break;
			}
			return "";
		}
	}

}
