package com.bstek.bdf2.wizard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.dgc.VMID;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.StringUtils;

import com.bstek.dorado.core.Configure;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public class WizardFileUtils {
	/**
	 * 复制文件
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(sourceFile);
			output = new FileOutputStream(targetFile);
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	/**
	 * 复制文件夹
	 * 
	 * @param sourceDir
	 *            源文件夹
	 * @param targetDir
	 *            目标文件夹
	 * @throws IOException
	 */
	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
		File targetFile = new File(targetDir);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		}
		File sourceFile = new File(sourceDir);
		if (!sourceFile.exists()) {
			return;
		}
		File[] file = sourceFile.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				copyFile(file[i], new File(targetFile.getAbsolutePath() + File.separator + file[i].getName()));
			}
			if (file[i].isDirectory() && !file[i].getName().endsWith(".svn")) {
				copyDirectiory(sourceDir + File.separator + file[i].getName(), targetDir + File.separator + file[i].getName());
			}
		}
	}

	/**
	 * 生成唯一的文件id
	 * 
	 * @return 文件id
	 */
	public static String generateFileVMID() {
		String id = new VMID().toString();
		return id.replaceAll(":", "");
	}

	/**
	 * 把文件压缩成zip文件
	 * 
	 * @param zipFileName
	 *            zip文件路径名称
	 * @param sourceDir
	 *            源文件目录
	 * @throws Exception
	 */
	public static void generateZipFile(String zipFileName, String sourceDir) throws Exception {
		ZipOutputStream zipOutputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(zipFileName);
			zipOutputStream = new ZipOutputStream(fileOutputStream);
			File file = new File(sourceDir);
			generateZipFile(zipOutputStream, file, null);
		} finally {
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			fileOutputStream.close();
		}
	}

	private static void generateZipFile(ZipOutputStream out, File file, String path) throws Exception {
		String name = path == null ? file.getName() : path + File.separator + file.getName();
		if (file.isFile()) {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(name));
				IOUtils.copy(fileInputStream, out);
			} finally {
				IOUtils.closeQuietly(fileInputStream);
			}
		}
		if (file.isDirectory() && !file.getName().endsWith(".svn")) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				out.putNextEntry(new ZipEntry(name + File.separator));
			} else {
				for (int i = 0; i < files.length; i++) {
					generateZipFile(out, files[i], name);
				}
			}
		}

	}

	/**
	 * 获取存放临时文件目录
	 * 
	 * @return 临时文件目录
	 */
	public static String getTempFilePath() {
		String fileLocation = Configure.getString("bdf2.wizard.location");
		if (StringUtils.hasText(fileLocation)) {
			return fileLocation.endsWith("/") ? fileLocation : fileLocation + "/";
		} else {
			return System.getProperty("java.io.tmpdir") + "/";
		}
	}

	public static File getTempFile(String id, String name) {
		if (StringUtils.hasText(id) && StringUtils.hasText(name)) {
			return new File(getTempFilePath() + id + File.separator + name);
		}
		return null;
	}

	public static File getTempFile(String id) {
		if (StringUtils.hasText(id)) {
			return new File(getTempFilePath() + id);
		}
		return null;
	}

}
