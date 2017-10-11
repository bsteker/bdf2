package com.bstek.bdf2.jbpm4.job.impl;

import com.bstek.bdf2.jbpm4.job.ITaskOverdueProcessor;

/**
 * @author Jacky.gao
 * @since 2013-4-26
 */
public class DonothingTaskOverdueProcessor implements ITaskOverdueProcessor {

	public void process(String taskId) {
		System.out.println("Task id["+taskId+"] was out of date!");
	}
}
