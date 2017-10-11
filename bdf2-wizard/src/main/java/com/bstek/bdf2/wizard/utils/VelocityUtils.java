package com.bstek.bdf2.wizard.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public class VelocityUtils {
	public static void velocityEvaluate(VelocityContext context, String vmFilePath, String destFileName) throws Exception {
		StringWriter writer = new StringWriter();
		FileInputStream in = new FileInputStream(vmFilePath);
		FileOutputStream out = new FileOutputStream(destFileName);
		try {
			Velocity.evaluate(context, writer, vmFilePath, new InputStreamReader(in));
			out.write(writer.toString().getBytes());
		} finally {
			out.close();
			in.close();
		}
	}

	public static void velocityEvaluate(VelocityContext context, String vmFilePath, StringWriter writer) throws Exception {
		FileInputStream in = new FileInputStream(vmFilePath);
		try {
			Velocity.evaluate(context, writer, vmFilePath, new InputStreamReader(in));
		} finally {
			in.close();
		}
	}
}
