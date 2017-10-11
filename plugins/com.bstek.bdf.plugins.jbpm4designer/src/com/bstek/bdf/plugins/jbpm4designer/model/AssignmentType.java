/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;
/**
 * @author Jacky
 */
public enum AssignmentType {
	none,assignee,candidategroups{
		@Override
		public String toString() {
			return "candidate-groups";
		}
		
	},candidateusers{
		@Override
		public String toString() {
			return "candidate-users";
		}		
	}
	,swimlane,assignmenthandler{
		@Override
		public String toString() {
			return "assignment-handler";
		}
	};
}
