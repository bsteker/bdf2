/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.outline;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.IPageSite;

import com.bstek.bdf.plugins.databasetool.DbToolGefEditor;
import com.bstek.bdf.plugins.databasetool.part.factory.DbToolTreeEditPartFactory;

public class DbToolGefEditorOutlinePage extends ContentOutlinePage implements IAdaptable {
	private DbToolGefEditor dbToolGefEditor;
	private TreeViewer treeViewer;
	private SashForm control;

	public DbToolGefEditorOutlinePage(EditPartViewer viewer, DbToolGefEditor dbToolGefEditor) {
		super(viewer);
		this.treeViewer = (TreeViewer) viewer;
		this.dbToolGefEditor = dbToolGefEditor;
	}

	public void createControl(Composite parent) {
		control = new SashForm(parent, SWT.VERTICAL | SWT.FLAT);
		createTableTreeViewer(control);
		createEyeViewControl(control);
		control.setWeights(new int[] { 60, 40 });
	}

	public void init(IPageSite pageSite) {
		super.init(pageSite);
	}

	private void createTableTreeViewer(Composite form) {
		treeViewer.createControl(form);
		treeViewer.setEditDomain(dbToolGefEditor.getEditDomain());
		treeViewer.setEditPartFactory(new DbToolTreeEditPartFactory());
		treeViewer.setContents(dbToolGefEditor.getModel());
		dbToolGefEditor.getSelectionSynchronizer().addViewer(treeViewer);

	}

	public void createEyeViewControl(Composite parent) {
		Canvas canvas = new Canvas(parent, SWT.NONE);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		LightweightSystem liSystem = new LightweightSystem(canvas);
		ScalableFreeformRootEditPart rootEditPart = ((ScalableFreeformRootEditPart) dbToolGefEditor.getGraphicalViewer().getRootEditPart());
		ScrollableThumbnail thumbnail = new ScrollableThumbnail((Viewport) rootEditPart.getFigure());
		thumbnail.setSource(rootEditPart.getLayer(LayerConstants.SCALABLE_LAYERS));
		liSystem.setContents(thumbnail);
	}

	public void dispose() {
		dbToolGefEditor.getSelectionSynchronizer().removeViewer(getViewer());
		super.dispose();
	}

	@Override
	public Control getControl() {
		return control;
	}

	@Override
	public void setFocus() {
		control.setFocus();
	}

	public DbToolGefEditor getDbToolGefEditor() {
		return dbToolGefEditor;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

}
