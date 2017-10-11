package com.bstek.bdf.plugins.databasetool.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public final class ClassUtils {
	private static final String PROTOCAL_PREFIX = "file:///";

	public static ClassLoader getClassLoader(String location) throws MalformedURLException {
		List<URL> urls = new ArrayList<URL>();
		String[] paths = location.split(";");
		File file = null;
		URL url = null;
		for (String path : paths) {
			if (path != null && path.trim().length() > 0) {
				file = new File(path);
				if (file.exists()) {
					url = new URL(PROTOCAL_PREFIX + computeForURLClassLoader(path));
					urls.add(url);
				}
			}
		}
		URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[] {}), ClassUtils.class.getClassLoader());
		return loader;
	}

	public static Class<?> loadJdbcDriverClass(String className, String driverLocation) throws Exception, MalformedURLException {
		ClassLoader loader = getClassLoader(driverLocation);
		return loader.loadClass(className);
	}

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