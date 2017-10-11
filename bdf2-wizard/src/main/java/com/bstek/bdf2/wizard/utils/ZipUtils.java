package com.bstek.bdf2.wizard.utils;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class ZipUtils {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String zipFileName = "d:/a.zip";
		String sourceDir = "D:/work/bdf2/workspace/bdf2-wizard";
		generateZipFile(zipFileName, sourceDir);
	}

	public static void generateZipFile(String zipFile, String sourceDir) {
		Project project = new Project();
		Zip zip = new Zip();
		zip.setProject(project);
		zip.setDestFile(new File(zipFile));
		FileSet fileSet = new FileSet();
		fileSet.setProject(project);
		fileSet.setDir(new File(sourceDir));
		zip.addFileset(fileSet);
		zip.execute();
	}

}
