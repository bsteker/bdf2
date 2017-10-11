/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.dialog.property;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import com.bstek.bdf.plugins.propeditor.editors.PropEditor;
import com.bstek.bdf.plugins.propeditor.editors.PropertyElement;
import com.bstek.bdf.plugins.propeditor.editors.PropertyFile;
import com.bstek.bdf.plugins.propeditor.xml.XmlReader;
import com.bstek.bdf.plugins.propeditor.xml.domain.PropertyCategory;
import com.bstek.bdf.plugins.propeditor.xml.domain.PropertyComment;

public class PropertyBrowser extends Dialog {
	public PropertyBrowser(Shell parentShell, PropEditor propEditor, String path) {
		super(parentShell);
		this.propEditor = propEditor;
		this.propertyFile = propEditor.getInputPropertyFile();
		this.path = path;
	}

	private PropEditor propEditor;
	private PropertyFile propertyFile;
	private String path;

	protected PropertyBrowser(Shell parentShell) {
		super(parentShell);
	}

	private FilteredTree filteredTree = null;
	private TreeViewer treeViewer = null;

	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("浏览支持的properties");
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite childComposite = new Composite(composite, SWT.NONE);
		GridData gridLayout = new GridData(GridData.FILL_BOTH);
		gridLayout.grabExcessVerticalSpace = true;
		gridLayout.grabExcessHorizontalSpace = true;
		gridLayout.minimumHeight = 300;
		gridLayout.minimumWidth = 500;
		childComposite.setLayoutData(gridLayout);
		childComposite.setLayout(new FillLayout());

		PatternFilter patternFilter = new PatternFilter();
		filteredTree = new FilteredTree(childComposite, SWT.None, patternFilter, true);
		filteredTree.setLayoutData(null);
		// treeViewer = new TreeViewer(childComposite);
		treeViewer = filteredTree.getViewer();
		treeViewer.setContentProvider(new PropertyContentProvider());
		treeViewer.setLabelProvider(new PropertyLabelProvider());

		try {
			treeViewer.setInput(XmlReader.loadXmlReader(path));
		} catch (Exception e1) {
			System.out.println("file not found");
		}

		Tree tree = treeViewer.getTree();

		GridData textLayoutData = new GridData(SWT.BORDER | GridData.FILL_BOTH);
		textLayoutData.minimumHeight = 150;
		textLayoutData.minimumWidth = 500;
		textLayoutData.grabExcessVerticalSpace = true;
		textLayoutData.grabExcessHorizontalSpace = true;
		Composite detailComposite = new Composite(composite, SWT.NONE);
		detailComposite.setLayoutData(textLayoutData);

		detailComposite.setLayout(new FillLayout());

		final StyledText text = new StyledText(detailComposite, SWT.BORDER | SWT.MULTI);
		text.setEditable(false);
		text.setWordWrap(true);

		tree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof TreeItem) {
					Object obj = e.item.getData();
					if (obj instanceof PropertyCategory) {
						text.setText("");
					} else if (obj instanceof PropertyComment) {
						text.setText("当前值: " + ((PropertyComment) obj).getValue() + "\n说明：\n\t"
								+ ((PropertyComment) obj).getComment());
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		return composite;
	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			okPressed();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			cancelPressed();
		} else if (UPDATE_ID == buttonId) {

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					try {
						propEditor.updatePropertyBrowser();
						treeViewer.setInput(XmlReader.loadXmlReader(path));
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					} finally {
						treeViewer.refresh();
					}
				}
			});
		}
	}

	public int UPDATE_ID = 101;

	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, "插入", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
		createButton(parent, UPDATE_ID, "更新", false);
	}

	public static void main(String[] arg) {
		Display display = new Display();
		Shell shell = new Shell(display);
		PropertyBrowser propertyBrowser = new PropertyBrowser(shell);
		propertyBrowser.open();
	}

	protected void okPressed() {
		IStructuredSelection iSelection = (IStructuredSelection) treeViewer.getSelection();
		PropertyComment propertyComment = (PropertyComment) iSelection.getFirstElement();
		setReturnCode(OK);
		String comments = propertyComment.getComment();
		if (comments != null) {
			StringBuilder rebuildComment = new StringBuilder();
			String[] commentsArray = comments.split("\n");
			for (String cmt : commentsArray) {
				rebuildComment.append("#").append(cmt).append("\n");
			}
			comments = rebuildComment.toString();
		}
		propertyFile.addPropertyElement(new PropertyElement(propertyFile, propertyComment.getKey(), propertyComment
				.getValue(), comments));
		close();
	}

	protected boolean isResizable() {
		return true;
	}

	public PropertyFile getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(PropertyFile propertyFile) {
		this.propertyFile = propertyFile;
	}
}
