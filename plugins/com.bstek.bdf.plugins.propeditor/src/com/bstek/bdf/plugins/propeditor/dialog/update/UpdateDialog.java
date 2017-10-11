package com.bstek.bdf.plugins.propeditor.dialog.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.bstek.bdf.plugins.propeditor.Activator;
import com.bstek.bdf.plugins.propeditor.preferences.PreferenceConstants;
import com.bstek.bdf.plugins.propeditor.preferences.UrlListEditor;

public class UpdateDialog extends TitleAreaDialog {
	private Shell shell;
	private String xmlFilePath;
	private Combo combo;
	private String projectName;

	public UpdateDialog(Shell parentShell, String xmlPath, String projectName) {
		super(parentShell);
		this.shell = parentShell;
		this.xmlFilePath = xmlPath;
		this.projectName = projectName;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("属性文件更新");
		this.shell = shell;
	}

	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		container.setLayout(layout);
		// container.setLayoutData(new GridData(GridData.FILL_BOTH));

		// TitleArea中的Title
		setTitle("属性文件更新");

		// TitleArea中的Message
		setMessage("输入正确的url地址，以更新文件。\n可提示的属性数量会根据当前项目存在的jar包，对已有属性增加或者删除！");

		Label label = new Label(container, SWT.NONE);
		label.setText("项目URL: ");
		combo = new Combo(container, SWT.DROP_DOWN);
		String[] items = new String[getUrlMap().size()];
		getUrlMap().values().toArray(items);
		combo.setItems(items);
		String url = getPreferedUrl(projectName);
		combo.setText(url);
		combo.setLayoutData(new RowData(400, 30));

		return area;
	}

	private Map<String, String> urlMap = null;
	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

	private String getPreferedUrl(String projectName) {
		String list = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.URL_LIST);
		String preferedUrl = null;
		if (list != null) {
			String[] urls = list.split(UrlListEditor.LISTDELIMITER);
			for (String url : urls) {

				String[] tokens = url.split(UrlListEditor.PROJECTDELIMITER);
				if (getUrlMap().containsKey(tokens[0])) {
					preferedUrl = getUrlMap().get(tokens[0]);
					break;
				}
			}
		}

		if (preferedUrl == null || preferedUrl.length() == 0) {

			int version = Integer.parseInt(preferenceStore.getString(PreferenceConstants.BDF_VERSION));
			preferedUrl = "http://" + preferenceStore.getString(PreferenceConstants.HOST) + ":"
					+ preferenceStore.getString(PreferenceConstants.PORT) + "/" + projectName + "/"
					+ UrlListEditor.VERSION_URL[version];
		}
		return preferedUrl;
	}

	protected void okPressed() {
		if (updateXmlFile(xmlFilePath)) {
			saveUrlList();
			super.okPressed();
		}
	}

	private void saveUrlList() {
		StringBuilder sb = new StringBuilder();
		combo.add(combo.getText(), 0);
		urlMap.put(projectName, combo.getText());

		for (Entry<String, String> urlEntry : urlMap.entrySet()) {
			sb.append(urlEntry.getKey()).append(UrlListEditor.PROJECTDELIMITER).append(urlEntry.getValue())
					.append(UrlListEditor.LISTDELIMITER);
		}
		preferenceStore.setValue(PreferenceConstants.URL_LIST, sb.toString());
	}

	private boolean updateXmlFile(String xmlFilePath) {
		String xmlUrl = combo.getText();
		if (xmlUrl == null || xmlUrl.length() == 0) {
			return false;
		}
		BufferedReader br = null;
		try {
			URL url = new URL(xmlUrl);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(1000);
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			if (checkXmlFileHeader(line)) {
				sb.append(line);
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();

				PrintWriter pw = new PrintWriter(xmlFilePath);
				pw.write(sb.toString());
				pw.close();
				return true;
			} else {
				MessageDialog.openInformation(shell, "连接成功,但获取信息失败", "请核查URL");
			}
		} catch (Exception e) {
			MessageDialog.openInformation(shell, "连接失败", "服务器连接失败,请核查服务器地址,端口号");
		} finally {
		}
		return false;
	}

	private boolean checkXmlFileHeader(String line) {
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		if (xmlHeader.equalsIgnoreCase(line)) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] arg) {
		Display display = new Display();
		Shell shell = new Shell(display);
		UpdateDialog dialog = new UpdateDialog(shell, "", "");
		dialog.open();
	}

	public Map<String, String> getUrlMap() {
		if (urlMap == null) {
			urlMap = new HashMap<String, String>();
			String list = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.URL_LIST);
			if (list != null) {
				String[] urls = list.split(UrlListEditor.LISTDELIMITER);
				for (String url : urls) {
					String[] tokens = url.split(UrlListEditor.PROJECTDELIMITER);
					if (tokens.length == 2) {
						urlMap.put(tokens[0], tokens[1]);
					}
				}
			}
		}
		return urlMap;
	}

	public void setUrlMap(Map<String, String> urlMap) {
		this.urlMap = urlMap;
	}

}
