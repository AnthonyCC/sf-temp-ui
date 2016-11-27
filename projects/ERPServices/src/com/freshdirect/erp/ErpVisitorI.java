/*
 * $Workfile: ErpVisitorI.java$
 *
 * $Date: 8/17/2001 12:51:36 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp;

/**
 * Visitor interface for ERP model objects.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public interface ErpVisitorI {
	
	/**
	 * Callback method, called by the model object when visitor enters.
	 * Useful for tracking context in a stack.
	 *
	 * @param model the model object the visitor entered
	 */
	public void pushModel(ErpModelSupport model);
	
	/**
	 * Callback method, called by a model object when it finished visiting it's children.
	 */
	public void popModel();

}