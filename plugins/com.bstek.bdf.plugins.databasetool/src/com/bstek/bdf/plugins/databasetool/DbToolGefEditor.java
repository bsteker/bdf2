/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.databasetool.action.ExportToDDLAction;
import com.bstek.bdf.plugins.databasetool.action.ExportToDatabaseAction;
import com.bstek.bdf.plugins.databasetool.action.ExportToImageAction;
import com.bstek.bdf.plugins.databasetool.action.ExportToJavaBeanAction;
import com.bstek.bdf.plugins.databasetool.action.ImportFromDatabaseAction;
import com.bstek.bdf.plugins.databasetool.action.ModelModifyAction;
import com.bstek.bdf.plugins.databasetool.action.TableRelationModifyAction;
import com.bstek.bdf.plugins.databasetool.action.TransformSchemaDbTypeAction;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.persistence.PersistenceHelper;
import com.bstek.bdf.plugins.databasetool.outline.DbToolGefEditorOutlinePage;
import com.bstek.bdf.plugins.databasetool.palettle.DbToolGefEditorPaletteFactory;
import com.bstek.bdf.plugins.databasetool.part.factory.DbToolEditPartFactory;

public class DbToolGefEditor extends GraphicalEditorWithFlyoutPalette implements ITabbedPropertySheetPageContributor {
	public static Color GRID_COLOR = new Color(Display.getCurrent(), 220, 220, 255);
	public static int PALETTE_WIDTH = 130;
	public static boolean PALETTE_USE_ICON16 = true;
	public static int WEST = 8;
	private ScalableFreeformRootEditPart rootEditPart;
	private Schema schema;

	public DbToolGefEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return DbToolGefEditorPaletteFactory.createPalette();
	}

	public void doSave(IProgressMonitor monitor) {
		try {
			InputStream stream = PersistenceHelper.convertObjectToXml(schema);
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(stream, true, false, monitor);
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IFile file = ((IFileEditorInput) input).getFile();
		InputStream is = null;
		try {
			setPartName(file.getName());
			is = file.getContents(true);
			schema = new Schema();
			PersistenceHelper.convertXmlToObject(is, schema);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getCommandStack().addCommandStackListener(commandStackListener);
		getGraphicalViewer().setEditPartFactory(new DbToolEditPartFactory());
		rootEditPart = new ScalableFreeformRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditPart);
		registerZoomAction();
		reigsterToggleGridAction();
		registerContextMenuProvider();
		registerKeyHandler();
		configInitData();
	}

	private void registerZoomAction() {
		List<String> zoomLevels = new ArrayList<String>(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		rootEditPart.getZoomManager().setZoomLevelContributions(zoomLevels);
		IAction zoomIn = new ZoomInAction(rootEditPart.getZoomManager());
		IAction zoomOut = new ZoomOutAction(rootEditPart.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		addKeyHandler(zoomIn);
		addKeyHandler(zoomOut);
	}

	private void reigsterToggleGridAction() {
		IAction action = new ToggleGridAction(getGraphicalViewer());
		getActionRegistry().registerAction(action);
	}

	private void registerContextMenuProvider() {
		ContextMenuProvider provider = new DbToolGefEditorContextMenuProvider(getGraphicalViewer(), getActionRegistry());
		getGraphicalViewer().setContextMenu(provider);
	}

	private void registerKeyHandler() {
		GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(getGraphicalViewer());
		KeyHandler parentKeyHandler = graphicalViewerKeyHandler.setParent(getCommonKeyHandler());
		getGraphicalViewer().setKeyHandler(parentKeyHandler);
	}

	private void configInitData() {
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		IFigure gridLayer = rootEditPart.getLayer(LayerConstants.GRID_LAYER);
		gridLayer.setForegroundColor(GRID_COLOR);
		getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL), MouseWheelZoomHandler.SINGLETON);
	}

	private CommandStackListener commandStackListener = new CommandStackListener() {
		public void commandStackChanged(EventObject event) {
			firePropertyChange(PROP_DIRTY);
		}
	};

	public void firePropertyChangeDirty() {
		firePropertyChange(PROP_DIRTY);
	}

	public KeyHandler getCommonKeyHandler() {
		KeyHandler sharedKeyHandler = new KeyHandler();
		sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		sharedKeyHandler.put(KeyStroke.getReleased((char)1, 97, SWT.CTRL), getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId()));
		return sharedKeyHandler;
	}

	private void addKeyHandler(IAction action) {
		IHandlerService service = (IHandlerService) getSite().getService(IHandlerService.class);
		service.activateHandler(action.getActionDefinitionId(), new ActionHandler(action));

	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		if (!schema.validateDbType()) {
			MessageDialog.openError(getSite().getShell(), "错误提示", "解析数据库方言失败！");
		}
		getGraphicalViewer().setContents(schema);
		getGraphicalViewer().addDropTargetListener(createTransferDropTargetListener());
	}

	@Override
	protected FlyoutPreferences getPalettePreferences() {
		FlyoutPreferences p = super.getPalettePreferences();
		p.setDockLocation(WEST);
		p.setPaletteWidth(PALETTE_WIDTH);
		return p;
	}

	protected PaletteViewerProvider createPaletteViewerProvider() {
		PaletteViewerProvider paletteViewerProvider = new PaletteViewerProvider(getEditDomain()) {
			public PaletteViewer createPaletteViewer(Composite parent) {
				PaletteViewer paletteViewer = super.createPaletteViewer(parent);
				paletteViewer.getPaletteViewerPreferences().setUseLargeIcons(PaletteViewerPreferences.LAYOUT_LIST, !PALETTE_USE_ICON16);
				return paletteViewer;
			}

			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
		return paletteViewerProvider;
	}

	private TransferDropTargetListener createTransferDropTargetListener() {
		return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
			protected CreationFactory getFactory(Object template) {
				return new SimpleFactory((Class<?>) template);
			}
		};
	}

	public void dispose() {
		super.dispose();
		getCommandStack().removeCommandStackListener(commandStackListener);
	}

	public boolean isDirty() {
		return getCommandStack().isDirty();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class type) {
		if (type == IContentOutlinePage.class) {
			return new DbToolGefEditorOutlinePage(new TreeViewer(), this);
		} else if (type == IPropertySheetPage.class) {
			return new TabbedPropertySheetPage(this);
		} else if (type == ZoomManager.class) {
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());
		}
		return super.getAdapter(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();

		IAction modelModifyAction = new ModelModifyAction(this);
		IAction exportToImageAction = new ExportToImageAction(this);
		IAction exportToDDLAction = new ExportToDDLAction(this);
		IAction exportToJavaBeanAction = new ExportToJavaBeanAction(this);
		IAction exportToDatabaseAction = new ExportToDatabaseAction(this);
		IAction importFromDatabaseAction = new ImportFromDatabaseAction(this);
		IAction transformSchemaDbTypeAction = new TransformSchemaDbTypeAction(this);
		IAction tableRelationModifyAction = new TableRelationModifyAction(this);

		registry.registerAction(modelModifyAction);
		registry.registerAction(exportToImageAction);
		registry.registerAction(exportToDDLAction);
		registry.registerAction(exportToJavaBeanAction);
		registry.registerAction(exportToDatabaseAction);
		registry.registerAction(importFromDatabaseAction);
		registry.registerAction(transformSchemaDbTypeAction);
		registry.registerAction(tableRelationModifyAction);

		getSelectionActions().add(modelModifyAction.getId());
		getSelectionActions().add(exportToImageAction.getId());
		getSelectionActions().add(exportToDDLAction.getId());
		getSelectionActions().add(exportToJavaBeanAction.getId());
		getSelectionActions().add(exportToDatabaseAction.getId());
		getSelectionActions().add(importFromDatabaseAction.getId());
		getSelectionActions().add(transformSchemaDbTypeAction.getId());
		getSelectionActions().add(tableRelationModifyAction.getId());
	}

	public Schema getModel() {
		return schema;
	}

	public void setModel(Schema schema) {
		this.schema = schema;
	}

	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	public SelectionSynchronizer getSelectionSynchronizer() {
		return super.getSelectionSynchronizer();
	}

	public ActionRegistry getActionRegistry() {
		return super.getActionRegistry();
	}

	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}

	@Override
	public String getContributorId() {
		return getSite().getId();
	}
	
	/*protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
	keyHandler = new KeyHandler();
	keyHandler.put( KeyStroke.getPressed(SWT.DEL, 127, 0),getActionRegistry().getAction(ActionFactory.DELETE.getId()));
	keyHandler.put( KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0),getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
	keyHandler.put( KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0),getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
	keyHandler.put(KeyStroke.getPressed(SWT.F2, 0),getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
	keyHandler.put(KeyStroke.getReleased((char) 1, 97, SWT.CTRL),getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId()));
	keyHandler.put(KeyStroke.getReleased((char) 0x03, 99, SWT.CTRL),getActionRegistry().getAction(ActionFactory.COPY.getId())); 
	//复制 Ctrl+C
	keyHandler.put(KeyStroke.getReleased((char) 0x16, 118, SWT.CTRL),getActionRegistry().getAction(ActionFactory.PASTE.getId())); 
	//黏贴 Ctrl+Vkey
	Handler.put(KeyStroke.getReleased((char) 24, (int)'x', SWT.CTRL),getActionRegistry().getAction(ActionFactory.CUT.getId()));
	//剪切 Ctrl+X... ...
	viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(keyHandler));}
	}
*/
}
