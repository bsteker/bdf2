/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard.pages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaConventionsUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.exporter.ExportConstants;

@SuppressWarnings("restriction")
public class ExportToJavaBeanWizardPage extends WizardPage {
	private IPackageFragmentRoot fCurrRoot;
	private IPackageFragment fCurrPackage;
	private Text sourceFolderText;
	private Text packageText;
	private Button buttonPropertyDef;
	private Button buttonHibernateAnnotation;

	

	public ExportToJavaBeanWizardPage(String pageName) {
		super(pageName);
		setTitle("导出JavaBean文件");
		setDescription("导出JavaBean文件");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		createExportControl(container);
		createExportConfigControl(container);

		setControl(container);
		setPageComplete(validatePage());
	}

	private void createExportControl(Composite container) {
		GridData gd = null;
		Label sourceFolderLabel = new Label(container, SWT.LEFT);
		sourceFolderLabel.setText("Source Folder:");
		gd = new GridData();
		gd.horizontalSpan = 1;
		sourceFolderLabel.setLayoutData(gd);

		sourceFolderText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		sourceFolderText.setLayoutData(gd);
		sourceFolderText.addListener(SWT.Modify, listener);

		Button buttonPackageName = new Button(container, SWT.PUSH);
		buttonPackageName.setText("浏览...");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.widthHint = 80;
		buttonPackageName.setLayoutData(gd);
		buttonPackageName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fCurrRoot = chooseContainer();
				String str = (fCurrRoot == null) ? "" : fCurrRoot.getPath().makeRelative().toString();
				sourceFolderText.setText(str);
			}
		});

		Label packageLabel = new Label(container, SWT.LEFT);
		packageLabel.setText("Package：");
		gd = new GridData();
		gd.horizontalSpan = 1;
		packageLabel.setLayoutData(gd);

		packageText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		packageText.setLayoutData(gd);
		packageText.addListener(SWT.Modify, listener);

		Button buttonPackage = new Button(container, SWT.PUSH);
		buttonPackage.setText("浏览...");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.widthHint = 80;
		buttonPackage.setLayoutData(gd);
		buttonPackage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fCurrPackage = choosePackage();
				String str = (fCurrPackage == null) ? "" : fCurrPackage.getElementName();
				packageText.setText(str);
			}
		});
	}

	private void createExportConfigControl(Composite container) {
		Group group = new Group(container, SWT.NONE);
		GridData gd = new GridData();
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		group.setLayoutData(gd);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 20;
		group.setLayout(layout);
		group.setText("导出设置");

		buttonPropertyDef = new Button(group, SWT.CHECK);
		buttonPropertyDef.setText("生成Dorado PropertyDef Annotation");
		buttonHibernateAnnotation = new Button(group, SWT.CHECK);
		buttonHibernateAnnotation.setText("生成Hiberante Annotation");

		boolean doradoPropertyDef = Activator.getDefault().getPreferenceStore().getBoolean(ExportConstants.DORADO_PROPERTY_DEF);
		boolean hiberanteAnnotation = Activator.getDefault().getPreferenceStore().getBoolean(ExportConstants.HIBERNATE_ANNOTATION);
		buttonPropertyDef.setSelection(doradoPropertyDef);
		buttonHibernateAnnotation.setSelection(hiberanteAnnotation);

	}

	private Listener listener = new Listener() {
		public void handleEvent(Event event) {
			validatePage();
		}
	};

	public boolean finish() {
		return true;
	}

	protected boolean validatePage() {
		if (getSourceFolderText().getText().length() == 0) {
			setErrorMessage("Source Folder 不能为空！");
			setPageComplete(false);
			return false;
		}
		IStatus sta = containerChanged();
		if (sta.getSeverity() == IStatus.ERROR) {
			setErrorMessage(Messages.format(NewWizardMessages.NewTypeWizardPage_error_InvalidPackageName, sta.getMessage()));
			setPageComplete(false);
			return false;
		}

		sta = packageChanged();
		if (sta.getSeverity() == IStatus.ERROR) {
			setErrorMessage(Messages.format(NewWizardMessages.NewTypeWizardPage_error_InvalidPackageName, sta.getMessage()));
			setPageComplete(false);
			return false;
		}

		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	protected IPackageFragmentRoot chooseContainer() {
		Class<?>[] acceptedClasses = new Class[] { IPackageFragmentRoot.class, IJavaProject.class };
		TypedElementSelectionValidator validator = new TypedElementSelectionValidator(acceptedClasses, false) {
			@Override
			public boolean isSelectedValid(Object element) {
				try {
					if (element instanceof IJavaProject) {
						IJavaProject jproject = (IJavaProject) element;
						IPath path = jproject.getProject().getFullPath();
						return (jproject.findPackageFragmentRoot(path) != null);
					} else if (element instanceof IPackageFragmentRoot) {
						return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
					}
					return true;
				} catch (JavaModelException e) {
					JavaPlugin.log(e.getStatus()); // just log, no UI in
													// validation
				}
				return false;
			}
		};

		acceptedClasses = new Class[] { IJavaModel.class, IPackageFragmentRoot.class, IJavaProject.class };
		ViewerFilter filter = new TypedViewerFilter(acceptedClasses) {
			@Override
			public boolean select(Viewer viewer, Object parent, Object element) {
				if (element instanceof IPackageFragmentRoot) {
					try {
						return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
					} catch (JavaModelException e) {
						JavaPlugin.log(e.getStatus()); // just log, no UI in
														// validation
						return false;
					}
				}
				return super.select(viewer, parent, element);
			}
		};

		StandardJavaElementContentProvider provider = new StandardJavaElementContentProvider();
		ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), labelProvider, provider);
		dialog.setValidator(validator);
		dialog.setComparator(new JavaElementComparator());
		dialog.setTitle(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_title);
		dialog.setMessage(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_description);
		dialog.addFilter(filter);
		dialog.setInput(JavaCore.create(getWorkspaceRoot()));
		dialog.setHelpAvailable(false);

		if (dialog.open() == Window.OK) {
			Object element = dialog.getFirstResult();
			if (element instanceof IJavaProject) {
				IJavaProject jproject = (IJavaProject) element;
				return jproject.getPackageFragmentRoot(jproject.getProject());
			} else if (element instanceof IPackageFragmentRoot) {
				return (IPackageFragmentRoot) element;
			}
			return null;
		}
		return null;
	}

	protected IStatus containerChanged() {
		StatusInfo status = new StatusInfo();
		String str = getSourceFolderText().getText();
		if (str.length() == 0) {
			status.setError(NewWizardMessages.NewContainerWizardPage_error_EnterContainerName);
			return status;
		}
		IPath path = new Path(str);
		IResource res = getWorkspaceRoot().findMember(path);
		if (res != null) {
			int resType = res.getType();
			if (resType == IResource.PROJECT || resType == IResource.FOLDER) {
				IProject proj = res.getProject();
				if (!proj.isOpen()) {
					status.setError(Messages.format(NewWizardMessages.NewContainerWizardPage_error_ProjectClosed,
							BasicElementLabels.getPathLabel(proj.getFullPath(), false)));
					return status;
				}
				IJavaProject jproject = JavaCore.create(proj);
				fCurrRoot = jproject.getPackageFragmentRoot(res);
				if (res.exists()) {
					if (res.isVirtual()) {
						status.setError(NewWizardMessages.NewContainerWizardPage_error_FolderIsVirtual);
						return status;
					}
					try {
						if (!proj.hasNature(JavaCore.NATURE_ID)) {
							if (resType == IResource.PROJECT) {
								status.setError(NewWizardMessages.NewContainerWizardPage_warning_NotAJavaProject);
							} else {
								status.setWarning(NewWizardMessages.NewContainerWizardPage_warning_NotInAJavaProject);
							}
							return status;
						}
						if (fCurrRoot.isArchive()) {
							status.setError(Messages.format(NewWizardMessages.NewContainerWizardPage_error_ContainerIsBinary,
									BasicElementLabels.getPathLabel(path, false)));
							return status;
						}
						if (fCurrRoot.getKind() == IPackageFragmentRoot.K_BINARY) {
							status.setWarning(Messages.format(NewWizardMessages.NewContainerWizardPage_warning_inside_classfolder,
									BasicElementLabels.getPathLabel(path, false)));
						} else if (!jproject.isOnClasspath(fCurrRoot)) {
							status.setWarning(Messages.format(NewWizardMessages.NewContainerWizardPage_warning_NotOnClassPath,
									BasicElementLabels.getPathLabel(path, false)));
						}
					} catch (JavaModelException e) {
						status.setWarning(NewWizardMessages.NewContainerWizardPage_warning_NotOnClassPath);
					} catch (CoreException e) {
						status.setWarning(NewWizardMessages.NewContainerWizardPage_warning_NotAJavaProject);
					}
				}
				return status;
			} else {
				status.setError(Messages.format(NewWizardMessages.NewContainerWizardPage_error_NotAFolder,
						BasicElementLabels.getPathLabel(path, false)));
				return status;
			}
		} else {
			status.setError(Messages.format(NewWizardMessages.NewContainerWizardPage_error_ContainerDoesNotExist,
					BasicElementLabels.getPathLabel(path, false)));
			return status;
		}
	}

	protected IStatus packageChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = getPackageFragmentRoot();
		IJavaProject project = root != null ? root.getJavaProject() : null;

		String packName = getPackageText().getText();
		if (packName.length() > 0) {
			IStatus val = validatePackageName(packName, project);
			if (val.getSeverity() == IStatus.ERROR) {
				status.setError(Messages.format(NewWizardMessages.NewTypeWizardPage_error_InvalidPackageName, val.getMessage()));
				return status;
			} else if (val.getSeverity() == IStatus.WARNING) {
				status.setWarning(Messages.format(NewWizardMessages.NewTypeWizardPage_warning_DiscouragedPackageName, val.getMessage()));
				// continue
			}
		} else {
			status.setWarning(NewWizardMessages.NewTypeWizardPage_warning_DefaultPackageDiscouraged);
		}

		if (project != null) {
			if (project.exists() && packName.length() > 0) {
				try {
					IPath rootPath = root.getPath();
					IPath outputPath = project.getOutputLocation();
					if (rootPath.isPrefixOf(outputPath) && !rootPath.equals(outputPath)) {
						// if the bin folder is inside of our root, don't allow
						// to name a package
						// like the bin folder
						IPath packagePath = rootPath.append(packName.replace('.', '/'));
						if (outputPath.isPrefixOf(packagePath)) {
							status.setError(NewWizardMessages.NewTypeWizardPage_error_ClashOutputLocation);
							return status;
						}
					}
				} catch (JavaModelException e) {
					JavaPlugin.log(e);
					// let pass
				}
			}

			fCurrPackage = root.getPackageFragment(packName);
			IResource resource = fCurrPackage.getResource();
			if (resource != null) {
				if (resource.isVirtual()) {
					status.setError(NewWizardMessages.NewTypeWizardPage_error_PackageIsVirtual);
					return status;
				}
				if (!ResourcesPlugin.getWorkspace().validateFiltered(resource).isOK()) {
					status.setError(NewWizardMessages.NewTypeWizardPage_error_PackageNameFiltered);
					return status;
				}
			}
		} else {
			status.setError("");
		}
		return status;
	}

	private static IStatus validatePackageName(String text, IJavaProject project) {
		if (project == null || !project.exists()) {
			return JavaConventions.validatePackageName(text, JavaCore.VERSION_1_3, JavaCore.VERSION_1_3);
		}
		return JavaConventionsUtil.validatePackageName(text, project);
	}

	protected IPackageFragment choosePackage() {
		IPackageFragmentRoot froot = getPackageFragmentRoot();
		IJavaElement[] packages = null;
		try {
			if (froot != null && froot.exists()) {
				packages = froot.getChildren();
			}
		} catch (JavaModelException e) {
			JavaPlugin.log(e);
		}
		if (packages == null) {
			packages = new IJavaElement[0];
		}

		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new JavaElementLabelProvider(
				JavaElementLabelProvider.SHOW_DEFAULT));
		dialog.setIgnoreCase(false);
		dialog.setTitle(NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_title);
		dialog.setMessage(NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_description);
		dialog.setEmptyListMessage(NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_empty);
		dialog.setElements(packages);
		dialog.setHelpAvailable(false);
		if (dialog.open() == Window.OK) {
			return (IPackageFragment) dialog.getFirstResult();
		}
		return null;
	}

	public IPackageFragmentRoot getPackageFragmentRoot() {
		return fCurrRoot;
	}

	public IPackageFragment getPackageFragment() {
		return fCurrPackage;
	}

	public IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public Text getSourceFolderText() {
		return sourceFolderText;
	}

	public Text getPackageText() {
		return packageText;
	}

	public void setSourceFolderText(Text sourceFolderText) {
		this.sourceFolderText = sourceFolderText;
	}

	public void setPackageText(Text packageText) {
		this.packageText = packageText;
	}

	public Button getButtonPropertyDef() {
		return buttonPropertyDef;
	}

	public Button getButtonHibernateAnotation() {
		return buttonHibernateAnnotation;
	}

	public void setButtonPropertyDef(Button buttonPropertyDef) {
		this.buttonPropertyDef = buttonPropertyDef;
	}

	public void setButtonHibernateAnotation(Button buttonHibernateAnotation) {
		this.buttonHibernateAnnotation = buttonHibernateAnotation;
	}

}
