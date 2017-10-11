package com.bstek.bdf.plugins.pojo2datatype.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

/**
 * 获取Project的辅助类
 * @author Jake.Wang@bstek.com
 * @since Dec 21, 2012
 *
 */
public class ProjectHelper {
	public static IJavaProject getCurrentJavaProject() {
		IProject project = getCurrentProject();
		if(project != null){
			try {
				if (project.hasNature(JavaCore.NATURE_ID)) {
					return JavaCore.create(project);
				}
			} catch (CoreException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public static IProject getCurrentProject() {
		ISelectionService selectionService =   
	            Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();  
	        ISelection selection = selectionService.getSelection();  
	  
	        IProject project = null;  
	        if(selection instanceof IStructuredSelection) {  
	            Object element = ((IStructuredSelection)selection).getFirstElement();  
	            if (element instanceof IResource) {  
	                project= ((IResource)element).getProject();  
	            } else if (element instanceof PackageFragmentRootContainer) {  
	                IJavaProject jProject =   
	                    ((PackageFragmentRootContainer)element).getJavaProject();  
	                project = jProject.getProject();  
	            } else if (element instanceof IJavaElement) {  
	                IJavaProject jProject= ((IJavaElement)element).getJavaProject();  
	                project = jProject.getProject();  
	            }  
	        }   
	        return project;  
	}
	
	public static String getCurrentClassPath(){
		try {
			return JavaRuntime.computeDefaultRuntimeClassPath(getCurrentJavaProject())[0];
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return ".";
	}
	public static IStructuredSelection getCurrentSelection() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window
					.getSelectionService().getSelection();
			return selection;
		}
		return null;
	}
}
