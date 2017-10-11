package com.bstek.bdf.plugins.pojo2datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.pojo2datatype.generator.DataType;
import com.bstek.bdf.plugins.pojo2datatype.utils.ProjectHelper;

/**
 * 创建新的Model文件向导页面
 * @author Jake.Wang@bstek.com
 * @since Dec 19, 2012
 * 
 */
public class ModelFilePage extends WizardNewFileCreationPage {
	private static final String ContentTypeID_XML = "org.eclipse.core.runtime.xml";
	private String defaultName = "NewModel"; //$NON-NLS-1$
	private static String defaultFileExtension = ".model.xml"; //$NON-NLS-1$
	private static String[] filterExtensions = { "*.model.xml" }; //$NON-NLS-1$
	private List<String> fValidExtensions = null;

	private Composite composite;
	private Composite newComposite;
	private Composite selectComposite;

	private String[] creationMethodRadioButtonLabels;
	private Button[] creationMethodRadioButtons;
	
	private Button[] interpretCustomTypeButtons;
	private String[] interpretCustomTypeButtonLabels;

	private Text location;
	private String filePath;

	private String[] errorMessage = new String[2];

	public ModelFilePage(IStructuredSelection selection) {
		super("modelFile", selection); //$NON-NLS-1$
		setTitle("Model File");
		setDescription("Model文件创建");

		creationMethodRadioButtonLabels = new String[] { "创建", "添加到已有文件" };
		creationMethodRadioButtons = new Button[creationMethodRadioButtonLabels.length];
		
		interpretCustomTypeButtonLabels = new String[] { "是", "否" };
		interpretCustomTypeButtons = new Button[interpretCustomTypeButtonLabels.length];
	}

	public void createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());

		Label label = setCreationMethod();
		createNewModelFile(parent, label);
		selectExistingModelFile(label);
	}

	private Label setCreationMethod() {
		
		Composite interpretCustomTypeButtonGroup = new Composite(composite, SWT.NONE);
		
		Label interpretCustomTypeLabel = new Label(interpretCustomTypeButtonGroup, SWT.NONE);
		interpretCustomTypeLabel.setText("  是否将类中用户自定义类型字段转为对应DataType中的属性字段： ");
		interpretCustomTypeButtonGroup.setLayout(new RowLayout());
		
		interpretCustomTypeButtons[0] = new Button(interpretCustomTypeButtonGroup, SWT.RADIO);
		interpretCustomTypeButtons[0].setSelection(true);
		interpretCustomTypeButtons[0].setText(interpretCustomTypeButtonLabels[0]);
		interpretCustomTypeButtons[0].addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(interpretCustomTypeButtons[0].getSelection()){
					DataType.isCustomClassTypeFieldInterpreted = true;
				} else {
					DataType.isCustomClassTypeFieldInterpreted = false;
				}
			}
		});
		DataType.isCustomClassTypeFieldInterpreted = true;
		interpretCustomTypeButtons[1] = new Button(interpretCustomTypeButtonGroup, SWT.RADIO);
		interpretCustomTypeButtons[1].setText(interpretCustomTypeButtonLabels[1]);
		interpretCustomTypeButtons[1].addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(interpretCustomTypeButtons[1].getSelection()){
					DataType.isCustomClassTypeFieldInterpreted = false;
				} else {
					DataType.isCustomClassTypeFieldInterpreted = true;
				}
			}
		});
		interpretCustomTypeButtonGroup.setVisible(true);
		
		Label creationDescLabel = new Label(composite, SWT.NONE);
		creationDescLabel.setText("Model文件生成方式： ");
		FormData data = new FormData();
		data.top = new FormAttachment(interpretCustomTypeButtonGroup, 5);
		data.left = new FormAttachment(0, 10);
		creationDescLabel.setLayoutData(data);
		
		creationMethodRadioButtons[0] = new Button(composite, SWT.RADIO);
		creationMethodRadioButtons[0].setText(creationMethodRadioButtonLabels[0]);
		creationMethodRadioButtons[0].setSelection(true);
		data = new FormData();
		data.top = new FormAttachment(interpretCustomTypeButtonGroup, 5);
		data.left = new FormAttachment(creationDescLabel, 10);
		creationMethodRadioButtons[0].setLayoutData(data);

		creationMethodRadioButtons[1] = new Button(composite, SWT.RADIO);
		creationMethodRadioButtons[1].setText(creationMethodRadioButtonLabels[1]);
		creationMethodRadioButtons[1].setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(interpretCustomTypeButtonGroup, 5);
		data.left = new FormAttachment(creationMethodRadioButtons[0], 10);
		creationMethodRadioButtons[1].setLayoutData(data);

		creationMethodRadioButtons[0].addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (creationMethodRadioButtons[0].getSelection()) {
					newComposite.setVisible(true);
				} else {
					newComposite.setVisible(false);
				}
				setErrorMessage();
				composite.layout();
				setPageComplete(validatePage());
			}
		});
		creationMethodRadioButtons[1].addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (creationMethodRadioButtons[1].getSelection()) {
					selectComposite.setVisible(true);
				} else {
					selectComposite.setVisible(false);
				}
				setErrorMessage();
				composite.layout();
				updatePageComplete();
			}
		});
		
		return creationDescLabel;
	}

	private void setErrorMessage() {
		setMessage(null);
		if (newComposite.isVisible()) {
			errorMessage[1] = getErrorMessage();
			setErrorMessage(errorMessage[0]);
		} 
		if(selectComposite.isVisible()) {
			errorMessage[0] = getErrorMessage();
			setErrorMessage(errorMessage[1]);
		}
	}

	private void createNewModelFile(Composite parent, Label label) {
		FormData data;
		newComposite = new Composite(composite, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(label, 10);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(100, -10);
		newComposite.setLayoutData(data);
		newComposite.setLayout(parent.getLayout());
		super.createControl(newComposite);
		this.setFileName(computeDefaultFileName());

		setPageComplete(validatePage());
	}

	private void selectExistingModelFile(Label label) {
		FormData data;
		selectComposite = new Composite(composite, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(label, 10);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		selectComposite.setLayoutData(data);
		selectComposite.setLayout(new FormLayout());

		Label selectLabel = new Label(selectComposite, SWT.NONE);
		selectLabel.setText("位置: ");
		data = new FormData();
		data.top = new FormAttachment(0, 10);
		data.left = new FormAttachment(0, 10);
		selectLabel.setLayoutData(data);

		location = new Text(selectComposite, SWT.BORDER);
		location.setEditable(false);
		data = new FormData();
		data.top = new FormAttachment(0, 10);
		data.left = new FormAttachment(selectLabel, 10);
		data.right = new FormAttachment(100, -100);
		location.setLayoutData(data);
		location.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updatePageComplete();
			}
		});

		final Button button = new Button(selectComposite, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -20);
		button.setText("Browse...");
		button.setLayoutData(data);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				browseForSaveLocation();
			}
		});

		selectComposite.setVisible(false);
	}

	protected String computeDefaultFileName() {
		int count = 0;
		String fileName = defaultName + defaultFileExtension;
		IPath containerFullPath = getContainerFullPath();
		if (containerFullPath != null) {
			while (true) {
				IPath path = containerFullPath.append(fileName);
				if (ResourcesPlugin.getWorkspace().getRoot().exists(path)) {
					count++;
					fileName = defaultName + count + defaultFileExtension;
				} else {
					break;
				}
			}
		}
		return fileName;
	}

	@Override
	protected boolean validatePage() {
		if (!newComposite.isVisible()) {
			return false;
		}
		String fullFileName = getFileName();
		if (fullFileName.trim().equals(defaultFileExtension)) {
			setErrorMessage("File name must be specified!");
			return false;
		}
		if (!extensionValidForContentType(fullFileName)) {
			setErrorMessage(NLS.bind("File name extension must be '.xml'!",
					getValidExtensions().toString()));
			return false;
		}
		// no fileExtension, let's check for this file with default file
		// extension

		fullFileName += defaultFileExtension;
		if ((getContainerFullPath() != null)
				&& (getContainerFullPath().isEmpty() == false)
				&& (getFileName().compareTo("") != 0)) //$NON-NLS-1$
		{

			Path fullPath = new Path(getContainerFullPath().toString() + '/'
					+ fullFileName);
			IResource resource = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(fullPath);
			if (resource != null) {
				setErrorMessage("This model file already exists!");
				return false;
			}
		}

		// check for file should be case insensitive
		String sameName = existsFileAnyCase(fullFileName);
		if (sameName != null) {
			// ISSUE: is qualitifedFileName not needed, or is it supposed
			// to be used in error message?
			// String qualifiedFileName = getContainerFullPath().toString() +
			// '/' + fullFileName;
			setErrorMessage("This model file already exists!" + " " + sameName); //$NON-NLS-1$
			return false;
		}
		return super.validatePage();
	}

	// returns true if file of specified name exists in any case for
	// selected container

	protected String existsFileAnyCase(String fileName) {
		if ((getContainerFullPath() != null)
				&& (getContainerFullPath().isEmpty() == false)
				&& (fileName.compareTo("") != 0)) //$NON-NLS-1$
		{
			// look through all resources at the specified container -
			// compare in upper case

			IResource parent = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(getContainerFullPath());
			if (parent instanceof IContainer) {
				IContainer container = (IContainer) parent;
				try {
					IResource[] members = container.members();
					String enteredFileUpper = fileName.toUpperCase();
					for (int i = 0; i < members.length; i++) {
						String resourceUpperName = members[i].getName()
								.toUpperCase();
						if (resourceUpperName.equals(enteredFileUpper)) {
							return members[i].getName();
						}
					}
				} catch (CoreException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return null;

	}

	/**
	 * Get list of valid extensions for XML Content type
	 * 
	 * @return List
	 */
	List<String> getValidExtensions() {
		if (fValidExtensions == null) {
			IContentType type = Platform.getContentTypeManager()
					.getContentType(ContentTypeID_XML);
			fValidExtensions = new ArrayList<String>(Arrays.asList(type
					.getFileSpecs(IContentType.FILE_EXTENSION_SPEC)));
		}
		return fValidExtensions;

	}

	/**
	 * Verifies if fileName is valid name for content type. Takes base content
	 * type into consideration.
	 * 
	 * @param fileName
	 * @return true if extension is valid for this content type
	 */
	boolean extensionValidForContentType(String fileName) {
		boolean valid = false;
		IContentType type = Platform.getContentTypeManager().getContentType(
				ContentTypeID_XML);
		// there is currently an extension
		if (fileName.lastIndexOf('.') != -1) {
			// check what content types are associated with current extension
			IContentType[] types = Platform.getContentTypeManager()
					.findContentTypesFor(fileName);
			int i = 0;
			while ((i < types.length) && !valid) {
				valid = types[i].isKindOf(type);
				++i;
			}
		} else {
			valid = true; // no extension so valid
		}
		return valid;
	}

	/**
	 * 获取新建Model文件的绝对路径
	 * @return 新建Model文件的绝对路径
	 */
	public String getNewFilePath() {
		String projectPath = ProjectHelper.getCurrentProject().getLocation()
				.toString();
		String workspacePath = projectPath.substring(0,
				projectPath.lastIndexOf('/'));
		return workspacePath + getContainerFullPath().toString() + '/'
				+ getFileName();
	}

	/**
	 * 获取要追加Model文件的绝对路径
	 * @return 追加Model文件的绝对路径
	 */
	public String getFilePath() {
		return filePath;
	}
	
	public boolean isCurrentPage() {
		return super.isCurrentPage();
	}

	private void browseForSaveLocation() {
		filePath = showFileDialog();
		if (filePath != null) {
			location.setText(filePath);
		}
	}

	private void updatePageComplete() {
		if (!selectComposite.isVisible()) {
			setPageComplete(false);
			return;
		}
		setPageComplete(false);

		IPath sourceLoc = getLocation();
		if (sourceLoc == null || !sourceLoc.toFile().exists()) {
			setMessage(null);
			setErrorMessage("请选择一个已存在的Model文件！");
			return;
		}
		setPageComplete(true);
		setMessage(null);
		setErrorMessage(null);
	}

	public IPath getLocation() {
		String text = location.getText().trim();
		if (text.length() == 0)
			return null;
		IPath path = new Path(text);
		if (!path.isAbsolute())
			path = ResourcesPlugin.getWorkspace().getRoot().getLocation()
					.append(path);
		return path;
	}

	public String showFileDialog() {
		FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
		fileDialog
				.setFilterPath(ProjectHelper.getCurrentProject() == null ? null
						: ProjectHelper.getCurrentProject().getLocation()
								.toString());
		fileDialog.setFilterExtensions(ModelFilePage.filterExtensions);
		return fileDialog.open();
	}
	
	/**
	 * 是否创建新的Model文件
	 * @return 新建返回true,否则返回false
	 */
	public boolean isNew(){
		if(newComposite.isVisible()){
			return true;
		}else{
			return false;
		}
	}
}