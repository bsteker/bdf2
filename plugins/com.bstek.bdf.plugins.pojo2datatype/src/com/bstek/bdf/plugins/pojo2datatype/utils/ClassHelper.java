package com.bstek.bdf.plugins.pojo2datatype.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * 用来加载eclipse workspace里面java项目的class辅助classloader
 * Class Helper to load class of java project.
 * @author Jake.Wang@bstek.com
 * @since Dec 18, 2012
 *
 */
public final class ClassHelper {

    private static final String PROTOCAL_PREFIX = "file:///";

    /**
     * get the <code>ClassLoader</code> of java project specified.
     *
     * @param project <code>IJavaProject</code>
     * @return <code>ClassLoader</code> of java project
     * @throws CoreException
     * @throws MalformedURLException
     */
    public static ClassLoader getProjectClassLoader(IJavaProject project)
            throws CoreException,MalformedURLException {

        //compute the project classpaths
        //REVIEW: Are the classpaths returned by computeDefaultRuntimeClassPath enough to load class?
        String[] classPaths = JavaRuntime.computeDefaultRuntimeClassPath(project);

        URL[] urls = new URL[classPaths.length];
        for (int i = 0; i < classPaths.length; i++) {
        	urls[i] = new URL(PROTOCAL_PREFIX + computeForURLClassLoader(classPaths[i]));
        }
        return new URLClassLoader(urls);
    }
    
    /**
     * get the <code>ClassLoader</code> of current java project.
     *
     * @return <code>ClassLoader</code> of java project
     * @throws CoreException
     * @throws MalformedURLException
     */
    public static ClassLoader getCurrentProjectClassLoader()
            throws CoreException,MalformedURLException {

        //compute the project classpaths
        //REVIEW: Are the classpaths returned by computeDefaultRuntimeClassPath enough to load class?
        String[] classPaths = JavaRuntime.computeDefaultRuntimeClassPath(ProjectHelper.getCurrentJavaProject());

        URL[] urls = new URL[classPaths.length];
        for (int i = 0; i < classPaths.length; i++) {
        	urls[i] = new URL(PROTOCAL_PREFIX + computeForURLClassLoader(classPaths[i]));
        }
        return new URLClassLoader(urls);
    }

    /**
     * load <code>Class</code> in java project
     *
     * @param project <code>IJavaProject</code>
     * @param className name of class to load
     * @return <code>Class</code>
     * @throws ClassNotFoundException
     * @throws CoreException
     * @throws MalformedURLException
     */
    public static Class<?> loadClass(IJavaProject project, String className)
            throws CoreException, ClassNotFoundException,MalformedURLException {
        ClassLoader loader = getProjectClassLoader(project);
        Class<?> clazz = loader.loadClass(className);
        loader = null;
        return clazz;
    }
    
    /**
     * load <code>Class</code> in current java project
     *
     * @param className name of class to load
     * @return <code>Class</code>
     * @throws ClassNotFoundException
     * @throws CoreException
     * @throws MalformedURLException
     */
    public static Class<?> loadClass(String className)
            throws CoreException, ClassNotFoundException,MalformedURLException {
        ClassLoader loader = getCurrentProjectClassLoader();
        Class<?> clazz = loader.loadClass(className);
        loader = null;
        return clazz;
    }


    /**
     * transform the <code>IType</code> to <code>Class</code>
     *
     * @param type <code>IType</code>
     * @return <code>Class</code>
     * @throws ClassNotFoundException
     * @throws MalformedURLException
     */
    public static Class<?> typeToClass(IType type) throws CoreException,
            ClassNotFoundException,MalformedURLException {
        try {
            if (null != type && (type.isClass() || type.isInterface())) {
                String className = type.getFullyQualifiedName('$');
                return loadClass(type.getJavaProject(), className);
            }
        } catch (JavaModelException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * because of URLClassLoader assumes any URL not ends with '/' to refer to a
     * jar file. so we need to append '/' to classpath if it is a folder but not
     * ends with '/'.
     *
     * @param classpath
     * @return
     */
    private static String computeForURLClassLoader(String classpath) {
        if (!classpath.endsWith("/")) {
            File file = new File(classpath);
            if (file.exists() && file.isDirectory()) {
                classpath = classpath.concat("/");
            }
        }
        return classpath;
    }
}