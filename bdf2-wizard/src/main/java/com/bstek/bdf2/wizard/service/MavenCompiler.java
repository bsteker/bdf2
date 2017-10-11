package com.bstek.bdf2.wizard.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.launcher.CommandLauncher;
import org.apache.commons.exec.launcher.CommandLauncherFactory;
import org.springframework.stereotype.Service;

import com.bstek.dorado.core.Configure;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Service(MavenCompiler.BEAN_ID)
public class MavenCompiler {
	public static final String BEAN_ID = "bdf2.wizard.mavenCompiler";

	private String mavenHome = Configure.getString("bdf2.wizard.mavenHome");
	private CommandLauncher launcher = CommandLauncherFactory.createVMLauncher();

	public String getMavenHome() {
		return mavenHome;
	}

	public void setMavenHome(String mavenHome) {
		this.mavenHome = mavenHome;
	}

	public void execute(String pomPath) throws Exception {
		Process process = null;
		if (this.isWindows()) {
			String command = mavenHome + "/bin/mvn.bat -U -f=" + pomPath + " compile ";
			process = launcher.exec(CommandLine.parse(command), null);
		} else {
			String binDir = System.getProperty("M2_HOME") + "/bin/";
			process = launcher.exec(CommandLine.parse(binDir + "mvn -U -f=" + pomPath + " compile "), null);
		}
		new DoOutput(process.getInputStream()).start();
		new DoOutput(process.getErrorStream()).start();
		process.waitFor();
	}

	private boolean isWindows() {
		String p = System.getProperty("os.name");
		return p.toLowerCase().indexOf("windows") >= 0 ? true : false;
	}

	private class DoOutput extends Thread {
		public InputStream is;

		public DoOutput(InputStream is) {
			this.is = is;
		}

		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
			try {
				@SuppressWarnings("unused")
				String str = null;
				while ((str = br.readLine()) != null) {
					 //System.out.println(str);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
}
