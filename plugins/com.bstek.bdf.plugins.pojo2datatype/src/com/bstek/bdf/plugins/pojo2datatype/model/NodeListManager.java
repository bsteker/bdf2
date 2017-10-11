package com.bstek.bdf.plugins.pojo2datatype.model;

import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import com.bstek.bdf.plugins.pojo2datatype.utils.ClassHelper;

/**
 * 节点列表维护类
 * @author Jake.Wang@bstek.com
 * @since Dec 22, 2012
 *
 */
public class NodeListManager{
	private List<Node> selectedNodeList = new ArrayList<Node>();
	private List<Node> nodeList = new ArrayList<Node>();
	
	/**
	 * 将包对应的Java类添加进树形选择控件对应的资源中
	 * @param jProject 当前JavaProject
	 * @param packageName 包名
	 * @param fileNames  包对应的文件名
	 * @return 添加成功返回 true,如果该包已添加返回false
	 */
	public boolean  addNodesFromPackage(IJavaProject jProject, String packageName, String[] fileNames){
		if (exists(packageName)) {
			return false;
		}
		Node rootNode = null;
		for (String name : fileNames) {
			try {
				if (name.endsWith(".class") || name.endsWith(".java")) {
					if (isValidClass(getSpecifiedClass(jProject,
							packageName, name))) {
						rootNode = initializeRootNode(packageName,
								nodeList, rootNode);
						rootNode.addChild(new Node(name.substring(0,
								name.indexOf('.')), false));
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		return true;
	}
	
	/**
	 * 初始化根节点
	 * @param packageName
	 * @param nodeList
	 * @param rootNode
	 * @return
	 */
	private Node initializeRootNode(String packageName, List<Node> nodeList,
			Node rootNode) {
		if (rootNode == null) {
			rootNode = new Node(packageName, true);
			nodeList.add(rootNode);
		}
		return rootNode;
	}
	
	private Class<?> getSpecifiedClass(IJavaProject jProject,
			String packageName, String name) throws MalformedURLException,
			ClassNotFoundException, CoreException {
		return Class.forName(
				packageName + "." + name.substring(0, name.indexOf('.')),
				false, ClassHelper.getProjectClassLoader(jProject));
	}

	/**
	 * 判断指定的类是不是符合条件
	 * @param clazz 指定的类
	 * @return 如果指定的类为接口或抽象类，则返回false。否则返回true。
	 */
	private boolean isValidClass(Class<?> clazz) {
		return !(clazz.isInterface() || Modifier.isAbstract(clazz
				.getModifiers()));
	}
	
	public boolean exists(String packageName) {
		for (Node node : nodeList) {
			if (node.getName().equals(packageName) && node.isPackage()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean removePackageNode(String packageName) {
		for (Node node : nodeList) {
			if (node.getName().equals(packageName) && node.isPackage()) {
				nodeList.remove(node);
				node.dispose();
				return true;
			}
		}
		return false;
	}
	
	public void clearSelectedNodeList(){
		for(Node node : selectedNodeList){
			node.dispose();
		}
		selectedNodeList.clear();
	}
	
	public void addSelectedNode(Node node){
		selectedNodeList.add(node);
	}
	public List<Node> getSelectedNodeList() {
		return selectedNodeList;
	}
	
	public List<Node> getNodeList() {
		return nodeList;
	}
	
	public void dispose(){
		for(Node node : selectedNodeList){
			node.dispose();
		}
		
		for(Node node : nodeList){
			node.dispose();
		}
	}
}
