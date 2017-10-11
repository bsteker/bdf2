package com.bstek.bdf.plugins.pojo2datatype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.PackageSelectionDialog;
import org.eclipse.jdt.internal.ui.search.JavaSearchScopeFactory;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import com.bstek.bdf.plugins.pojo2datatype.model.Node;
import com.bstek.bdf.plugins.pojo2datatype.model.NodeListManager;
import com.bstek.bdf.plugins.pojo2datatype.providers.ClassListLabelProvider;
import com.bstek.bdf.plugins.pojo2datatype.providers.ClassTreeContentProvider;
import com.bstek.bdf.plugins.pojo2datatype.utils.ProjectHelper;

/**
 * 选择Java类向导页面
 * @author Jake.Wang@bstek.com
 * @since Dec 18, 2012
 * 
 */
public class SelectClassPage extends WizardPage {
	private NodeListManager nodeListManager;
	private TreeViewer treeViewer;

	protected SelectClassPage() {
		super("selectClasses");
		setTitle("选择Class");
		setDescription("选择将要生成Model文件的Class.");
		nodeListManager = new NodeListManager();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final FormLayout formLayout = new FormLayout();
		container.setLayout(formLayout);
		setControl(container);

		treeViewer = new TreeViewer(container, SWT.BORDER | SWT.CHECK
				| SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI);

		FormData formData;
		Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText("删除包");
		formData = new FormData();
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(100, -5);
		removeButton.setLayoutData(formData);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSelectedPackage(e, treeViewer);
			}
		});

		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(removeButton, -5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		treeViewer.getTree().setLayoutData(formData);
		treeViewer.setContentProvider(new ClassTreeContentProvider());
		treeViewer.setLabelProvider(new ClassListLabelProvider());
		treeViewer.getTree().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setCheckedItem(e);
			}
		});
		setTreeViewer(treeViewer);

		Button addPackageButton = new Button(container, SWT.PUSH);
		addPackageButton.setText("添加包");
		formData = new FormData();
		formData.right = new FormAttachment(removeButton, -5);
		formData.bottom = new FormAttachment(100, -5);
		addPackageButton.setLayoutData(formData);
		addPackageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addPackage(treeViewer);
			}
		});

		setPageComplete(false);
	}

	private void addPackage(TreeViewer treeViewer) {
		IJavaProject jProject = ProjectHelper.getCurrentJavaProject();
		if (jProject == null) {
			alertAndClose();
			return;
		}
		PackageSelectionDialog dialog = createPackageSelectionDialog(jProject);
		if (dialog.open() == IDialogConstants.OK_ID) {
			String packageName = getPackageName(dialog);
			if (nodeListManager.addNodesFromPackage(jProject, packageName,
					getFile(jProject, packageName).list())) {
				setTreeViewer(treeViewer);
				setPageComplete(false);
			}
		}
	}

	/**
	 * 设置类选择树对应的数据，并将所有节点展开
	 * @param treeViewer
	 */
	private void setTreeViewer(final TreeViewer treeViewer) {
		treeViewer.setInput(nodeListManager.getNodeList().toArray());
		treeViewer.expandAll();
	}

	/**
	 * 当用户通过右键菜单启动向导时，初始化类选择列表
	 * @param selection 当前选择项
	 */
	public void init(IStructuredSelection selection) {
		IJavaProject jProject = ProjectHelper.getCurrentJavaProject();
		if (jProject == null) {
			alertAndClose();
			return;
		}
		
		Map<String, List<String>> packageClassMap = new HashMap<String, List<String>>();
		setPackageClassMap(selection, packageClassMap);
		
		for(String packageName : packageClassMap.keySet()){
			List<String> classes = packageClassMap.get(packageName);
			if(classes.isEmpty()){
				nodeListManager.addNodesFromPackage(jProject, packageName, getFile(jProject, packageName).list());
			}else{
				nodeListManager.addNodesFromPackage(jProject, packageName, classes.toArray(new String[classes.size()]));
			}
		}
	}

	/**
	 * 设置用户选择的包和类的对应关系
	 * @param selection 用户选择
	 * @param packageClassMap 包和类的对应Map
	 */
	private void setPackageClassMap(IStructuredSelection selection,
			Map<String, List<String>> packageClassMap) {
		@SuppressWarnings("rawtypes")
		Iterator iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (element instanceof IPackageFragment) {
				String packageName = ((IPackageFragment) element)
						.getElementName();
				List<String> classes = packageClassMap.get(packageName);
				if (classes == null) {
					packageClassMap.put(packageName, new ArrayList<String>());
				}
			} else if (element instanceof CompilationUnit) {
				CompilationUnit unit = ((CompilationUnit) element);
				String packageName = unit.getParent().getElementName();
				String className = unit.getElementName();
				List<String> classes = packageClassMap.get(packageName);
				if (classes == null) {
					classes = new ArrayList<String>();
				}
				classes.add(className);
				packageClassMap.put(packageName, classes);
			}
		}
	}

	private PackageSelectionDialog createPackageSelectionDialog(
			IJavaProject jProject) {
		PackageSelectionDialog dialog = new PackageSelectionDialog(
				JavaPlugin.getActiveWorkbenchShell(), PlatformUI.getWorkbench()
						.getProgressService(),
				PackageSelectionDialog.F_HIDE_EMPTY_INNER,
				JavaSearchScopeFactory.getInstance()
						.createJavaProjectSearchScope(jProject, false));
		dialog.setTitle("选择当前工程中的包");
		return dialog;
	}

	private void alertAndClose() {
		alert("请选择一个Java工程!");
		getShell().close();
	}

	private String getPackageName(PackageSelectionDialog dialog) {
		PackageFragment packageFragment = (PackageFragment) dialog
				.getFirstResult();
		return packageFragment.getElementName();
	}

	private java.io.File getFile(IJavaProject jProject, String packageName) {
		String path = getAbsolutePath(jProject, packageName);
		return new java.io.File(path);
	}

	private String getAbsolutePath(IJavaProject jProject, String packageName) {
		try {
			return JavaRuntime.computeDefaultRuntimeClassPath(jProject)[0]
					+ (Path.SEPARATOR + packageName.replace(".", "/") + Path.SEPARATOR);
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void setCheckedItem(SelectionEvent e) {
		if (e.detail == SWT.CHECK) {
			TreeItem selectedTreeItem = (TreeItem) e.item;
			// 如果有父节点，选择父节点
			selectAllParentItems(selectedTreeItem);
			// 如果有子节点，全选子节点
			selectAllSubItems(selectedTreeItem);

			updatePageComplete((Tree) e.getSource());
		}
	}

	// 迭代选择下级目录的多选框
	private void selectAllSubItems(TreeItem selectedTreeItem) {
		TreeItem[] selection = selectedTreeItem.getItems();
		for (int i = 0; i < selection.length; i++) {
			selection[i].setChecked(selectedTreeItem.getChecked());
			selectAllSubItems(selection[i]);
		}
	}

	private void selectAllParentItems(TreeItem selectedTreeItem) {
		TreeItem parentTreeItem = selectedTreeItem.getParentItem();
		// 当有一个未被选中的时候，取消父节点的选择,当全部选中，则父节点选中
		if (parentTreeItem != null) {
			TreeItem[] parentSubItems = parentTreeItem.getItems();
			boolean allChecked = true;
			boolean isGrayed = false;
			for (int i = 0; i < parentSubItems.length; i++) {
				allChecked = parentSubItems[i].getChecked();
				if (!allChecked) {
					break;
				}
			}
			for (int i = 0; i < parentSubItems.length; i++) {
				if (parentSubItems[i].getChecked()) {
					isGrayed = true;
					break;
				}
			}

			if (isGrayed && allChecked) {
				parentTreeItem.setChecked(allChecked);
				parentTreeItem.setGrayed(!isGrayed);
			}
			if (!allChecked) {
				parentTreeItem.setChecked(isGrayed);
				parentTreeItem.setGrayed(isGrayed);
			}
			selectAllParentItems(parentTreeItem);
		}
	}

	private void updatePageComplete(Tree tree) {
		TreeItem[] rootItems = tree.getItems();
		nodeListManager.clearSelectedNodeList();
		getSelectedItems(rootItems, nodeListManager.getSelectedNodeList());
		if (nodeListManager.getSelectedNodeList().size() > 0) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}

	private void getSelectedItems(TreeItem[] items, List<Node> selectedItems) {
		if (items == null) {
			return;
		}
		for (TreeItem item : items) {
			Node parent = new Node(item.getText(), true);
			TreeItem[] children = item.getItems();
			if (children != null) {
				boolean selected = false;
				for (TreeItem child : children) {
					if (child.getChecked()) {
						parent.addChild(new Node(child.getText(), false));
						selected = true;
					}
				}
				if (selected) {
					nodeListManager.addSelectedNode(parent);
				}
			}
		}
	}

	private void removeSelectedPackage(SelectionEvent e, TreeViewer treeViewer) {
		Tree tree = getTree(e);
		if (tree == null) {
			return;
		}
		TreeItem[] selection = tree.getSelection();
		if (selection != null && selection.length > 0) {
			for (TreeItem item : selection) {
				TreeItem parent = item.getParentItem();
				if (parent == null) {
					removePackage(item);
					updatePageComplete(tree);
				} else {
					alert("请选择要删除的包!");
				}
			}
		} else {
			alert("请选择要删除的包!");
		}
	}

	private Tree getTree(SelectionEvent e) {
		Control[] controls = ((Button) e.getSource()).getParent().getChildren();
		for (Object control : controls) {
			if (control instanceof Tree) {
				return (Tree) control;
			}
		}
		return null;
	}

	private void removePackage(TreeItem item) {
		nodeListManager.removePackageNode(item.getText());
		dispose(item);
	}

	private void dispose(TreeItem item) {
		if (item != null) {
			if (item.getItems() != null) {
				for (TreeItem child : item.getItems()) {
					dispose(child);
				}
			}
		}
		item.getImage().dispose();
		item.dispose();
	}

	public List<Node> getSelectedClasses() {
		return nodeListManager.getSelectedNodeList();
	}

	private void alert(String info) {
		MessageBox messageBox = new MessageBox(
				JavaPlugin.getActiveWorkbenchShell());
		messageBox.setText("提示");
		messageBox.setMessage(info);
		messageBox.open();
	}

	@Override
	public void dispose() {
		super.dispose();
		nodeListManager.dispose();
		for (TreeItem item : treeViewer.getTree().getItems()) {
			dispose(item);
		}
	}
}
