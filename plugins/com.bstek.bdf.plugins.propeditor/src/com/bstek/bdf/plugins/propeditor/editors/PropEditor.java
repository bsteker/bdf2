/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.bstek.bdf.plugins.propeditor.Activator;
import com.bstek.bdf.plugins.propeditor.dialog.property.PropertyBrowser;
import com.bstek.bdf.plugins.propeditor.dialog.update.UpdateDialog;

public class PropEditor extends MultiPageEditorPart implements IResourceChangeListener {

	public static final String xmlFileName = ".xml.propdesc";

	private TextEditor editor;

	public PropEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	private PropertiesOutlinePage outlinePage;

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IContentOutlinePage.class)) {
			if (outlinePage == null)
				outlinePage = new PropertiesOutlinePage(this, inputPropertyFile, editor);
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}

	private TableViewer tableViewer;
	private KeyFilter keyFilter;
	Button buttonAdd;
	Button buttonDel;
	ToolItem toolItemAdd;
	ToolItem toolItemDel;
	ToolItem toolItemProperty;

	Image getImage(String name) {
		return Activator.getImageDescriptor(name).createImage();
	}

	void createPagePropGrid() {
		Composite mainComposite = new Composite(getContainer(), SWT.None);

		GridLayout layout = new GridLayout();

		layout.numColumns = 1;
		mainComposite.setLayout(layout);

		Listener mouseListener = new TableButtonListener();

		ToolBar toolbar = new ToolBar(mainComposite, SWT.FLAT | SWT.RIGHT);
		GridData toolbarGridData = new GridData();
		toolbar.setLayoutData(toolbarGridData);

		toolItemAdd = new ToolItem(toolbar, SWT.PUSH);
		toolItemAdd.setToolTipText("添加");
		toolItemAdd.setText("添加");
		toolItemAdd.addListener(SWT.Selection, mouseListener);
		toolItemAdd.setImage(getImage("icons/add.png"));

		toolItemDel = new ToolItem(toolbar, SWT.PUSH);
		toolItemDel.setToolTipText("删除");
		toolItemDel.setText("删除");
		toolItemDel.addListener(SWT.Selection, mouseListener);
		toolItemDel.setImage(getImage("icons/delete.png"));

		toolItemProperty = new ToolItem(toolbar, SWT.PUSH);
		toolItemProperty.setToolTipText("插入BDF系统属性");
		toolItemProperty.setText("插入BDF系统属性");
		toolItemProperty.addListener(SWT.Selection, mouseListener);
		toolItemProperty.setImage(getImage("icons/add_page.png"));

		Composite filterComposite = new Composite(mainComposite, SWT.None);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		GridData gridDataSearchLabel = new GridData();
		gridDataSearchLabel.minimumWidth = 30;

		GridData gridDataSearchText = new GridData(GridData.FILL_BOTH);
		gridDataSearchText.minimumWidth = 300;

		filterComposite.setLayout(gridLayout);
		CLabel label = new CLabel(filterComposite, SWT.None);
		label.setText("搜索: ");
		label.setImage(getImage("icons/search.png"));
		label.setLayoutData(gridDataSearchLabel);

		final Text searchText = new Text(filterComposite, SWT.BORDER | SWT.SEARCH);
		searchText.setFocus();
		searchText.setLayoutData(gridDataSearchText);
		searchText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				keyFilter.setSearchText(searchText.getText());
				tableViewer.refresh();
			}
		});

		Composite tableComposite = new Composite(mainComposite, SWT.None);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		tableComposite.setLayoutData(gridData);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);
		keyFilter = new KeyFilter();
		tableViewer.addFilter(keyFilter);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);

		TableColumn keyColumn = new TableColumn(table, SWT.NONE);
		keyColumn.setText("Key");
		tableColumnLayout.setColumnData(keyColumn, new ColumnWeightData(50));
		TableViewerColumn keyTableViewerColumn = new TableViewerColumn(tableViewer, keyColumn);
		keyTableViewerColumn.setEditingSupport(createEditingSupport(0));

		TableColumn valueColumn = new TableColumn(table, SWT.NONE);
		valueColumn.setText("Value");
		tableColumnLayout.setColumnData(valueColumn, new ColumnWeightData(50));
		TableViewerColumn valueTableViewerColumn = new TableViewerColumn(tableViewer, valueColumn);
		valueTableViewerColumn.setEditingSupport(createEditingSupport(1));

		int index = addPage(mainComposite);
		setPageText(index, "Properties");
	}

	private EditingSupport createEditingSupport(final int index) {
		return new EditingSupport(tableViewer) {
			TextCellEditor editor = null;

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (editor == null) {
					editor = new TextCellEditor(tableViewer.getTable());
				}
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected Object getValue(Object element) {
				return propsLabelProvider.getColumnText(element, index);
			}

			@Override
			protected void setValue(Object element, Object value) {
				String text = ((String) value).trim();
				if (element instanceof PropertyElement) {
					switch (index) {
					case 0:
						((PropertyElement) element).setKey(text);
						break;
					case 1:
						((PropertyElement) element).setValue(text);
					default:
						break;
					}
				}
			}
		};
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	void createPageSource() {
		try {
			editor = new TextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, "Source");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPagePropGrid();
		createPageSource();
		initTableContent();
		updateTitle();
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	public void doSaveAs() {
		if (getActivePage() == 0 && isPageModified)
			updateTextEditorFromTree();
		isPageModified = false;
		editor.doSaveAs();
		setInput(editor.getEditorInput());
		updateTitle();
	}

	void updateTitle() {
		IEditorInput input = getEditorInput();
		setPartName(input.getName());
		setTitleToolTip(input.getToolTipText());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		super.init(site, editorInput);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		switch (newPageIndex) {
		case 0:
			if (isDirty())
				updatePropsFromTextEditor();
			break;
		case 1:
			if (isPageModified)
				updateTextEditorFromTree();
			break;
		}
		isPageModified = false;
		super.pageChange(newPageIndex);
	}

	void updateTextEditorFromTree() {
		editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(inputPropertyFile.asText());
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput()).getFile().getProject()
								.equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	private PropsContentProvider propsContentProvider;
	private PropertiesEditorLabelProvider propsLabelProvider;
	private PropertyFile inputPropertyFile;

	void initTableContent() {
		propsContentProvider = new PropsContentProvider();
		tableViewer.setContentProvider(propsContentProvider);
		propsLabelProvider = new PropertiesEditorLabelProvider();
		tableViewer.setLabelProvider(propsLabelProvider);

		inputPropertyFile = new PropertyFile(editor.getDocumentProvider().getDocument(editor.getEditorInput()));
		inputPropertyFile.addPropertyFileListener(propertyFileListener);
		tableViewer.setInput(inputPropertyFile);

		getSite().setSelectionProvider(tableViewer);

		tableViewer.getTable().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				updatePropsFromTextEditor();
			}
		});
	}

	private boolean isPageModified;

	public void propsModified() {
		boolean wasDirty = isDirty();
		isPageModified = true;
		if (!wasDirty)
			firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	protected void handlePropertyChange(int propertyId) {
		if (propertyId == IEditorPart.PROP_DIRTY) {
			isPageModified = isDirty();
		}
		super.handlePropertyChange(propertyId);
	}

	public boolean isDirty() {
		return isPageModified || super.isDirty();
	}

	void updatePropsFromTextEditor() {
		inputPropertyFile.removePropertyFileListener(propertyFileListener);
		inputPropertyFile = new PropertyFile(editor.getDocumentProvider().getDocument(editor.getEditorInput()));
		tableViewer.setInput(inputPropertyFile);
		inputPropertyFile.addPropertyFileListener(propertyFileListener);
	}

	public void doSave(IProgressMonitor monitor) {
		if (getActivePage() == 0 && isPageModified)
			updateTextEditorFromTree();
		isPageModified = false;
		editor.doSave(monitor);
	}

	private final PropertyFileListener propertyFileListener = new PropertyFileListener() {
		private void updateElement(PropertyEntry element) {
			tableViewer.refresh(element);
			propsModified();
		}

		@Override
		public void keyChanged(PropertyEntry element) {
			updateElement(element);
		}

		@Override
		public void valueChanged(PropertyEntry element) {
			updateElement(element);
		}

		@Override
		public void entryAdded(PropertyEntry element) {
			tableViewer.add(element);
			propsModified();
			highLightProperty((PropertyElement) element);
		}

		@Override
		public void entryRemoved(PropertyEntry element) {
			tableViewer.remove(element);
			propsModified();
		}
	};

	public void highLightProperty(PropertyElement propertyElement) {
		editor.setHighlightRange(propertyElement.getLineOffset(), 0, true);
		StructuredSelection ss = new StructuredSelection(tableViewer.getElementAt(propertyElement.getIndex()));
		tableViewer.setSelection(ss, true);
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public PropertyFile getInput() {
		return inputPropertyFile;
	}

	public TextEditor getEditor() {
		return editor;
	}

	private PropertyBrowser propertyBrowser;

	final PropEditor me = this;

	class TableButtonListener implements Listener {

		private void doAdd() {
			inputPropertyFile.addPropertyFileListener(propertyFileListener);
			PropertyElement propertyElement = new PropertyElement(inputPropertyFile, "", "");
			inputPropertyFile.addPropertyElement(propertyElement);
			highLightProperty(propertyElement);
		}

		private void doDel() {
			StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
			PropertyElement propertyElement = (PropertyElement) selection.getFirstElement();
			inputPropertyFile.removePropertyElement(propertyElement);
		}

		@Override
		public void handleEvent(Event e) {
			if (e.widget == toolItemAdd) {
				doAdd();
			} else if (e.widget == toolItemDel) {
				doDel();
			} else if (e.widget == toolItemProperty) {
				if (propertyBrowser == null) {
					if (!checkXmlExist()) {
						try {
							updatePropertyBrowser();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					propertyBrowser = new PropertyBrowser(getContainer().getShell(), me, getXmlFilePath());
				}
				propertyBrowser.open();
			}

		}
	}

	private boolean checkXmlExist() {
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		File file = new File(getXmlFilePath());
		boolean fileExist = false;
		BufferedReader br = null;
		if (file.exists()) {
			try {
				br = new BufferedReader(new FileReader(file));
				if (xmlHeader.equalsIgnoreCase(br.readLine())) {
					fileExist = true;
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fileExist;
	}

	public String getXmlFilePath() {
		((IFileEditorInput) getEditorInput()).getFile().getProject().getName();
		return ((IFileEditorInput) getEditorInput()).getFile().getProject().getLocation().toString()
				+ File.separatorChar + xmlFileName;
	}

	public static final String xmlController = "bdf.popertiesLoderController.downXmlPropdesc.c";

	public void updatePropertyBrowser() throws Exception {

		UpdateDialog updateDialog = new UpdateDialog(getContainer().getShell(), getXmlFilePath(),
				((IFileEditorInput) getEditorInput()).getFile().getProject().getName());
		updateDialog.open();
	}

	public void updateXmlFile(String xmlPath) throws Exception {
		if (xmlPath != null && xmlPath.length() > 0) {
			if (!xmlPath.endsWith(".c") && !xmlPath.endsWith(".action")) {
				if (xmlPath.charAt(xmlPath.length() - 1) != '/') {
					xmlPath += "/";
				}
				xmlPath += xmlController;
			}
		}
		BufferedReader br = null;
		try {
			URL url = new URL(xmlPath);
			URLConnection connection = url.openConnection();

			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			br.close();

			PrintWriter pw = new PrintWriter(getXmlFilePath());
			pw.write(sb.toString());
			pw.close();
		} catch (Exception e) {
			MessageDialog.openInformation(getContainer().getShell(), "连接失败", "服务器连接失败,请核查服务器地址,端口号及URL");
			throw e;
		} finally {

		}

	}

	class FilterListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {

		}

	}

	class KeyFilter extends ViewerFilter {
		private String searchString;

		public void setSearchText(String s) {
			this.searchString = ".*" + s + ".*";
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (searchString == null || searchString.length() == 0) {
				return true;
			}

			if (element instanceof PropertyElement) {
				PropertyElement p = (PropertyElement) element;
				if (p.getKey().toLowerCase().matches(searchString.toLowerCase())) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}

	}

	public PropertyFile getInputPropertyFile() {
		return inputPropertyFile;
	}

	public void setInputPropertyFile(PropertyFile inputPropertyFile) {
		this.inputPropertyFile = inputPropertyFile;
	}
}
