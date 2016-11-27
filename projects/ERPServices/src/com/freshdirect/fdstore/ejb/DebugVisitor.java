/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.ejb;

import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;

/**
 * Simple visitor for getting the string representation of an ErpModel tree.
 * The visitor is not thread safe. It can be easily recycled by calling clear().
 *
 * @version $Revision$
 * @author $Author$
 */
public class DebugVisitor implements ErpVisitorI {

	private StringBuffer buf = new StringBuffer();
	private int level = 0;

	/**
	 * Default constructor.
	 */
	public DebugVisitor() {
	}
	
	/**
	 * Visiting constructor: visits the specified model.
	 */
	public DebugVisitor(ErpModelSupport model) {
		model.accept(this);
	}

	/**
	 * Callback method, called by the model object when visitor enters.
	 * Useful for tracking context in a stack.
	 *
	 * @param model the model object the visitor entered
	 */
	public void pushModel(ErpModelSupport model) {
		level++;
		for (int i=0; i<level-1; i++) {
			buf.append("  ");
		}
		buf.append( model.toString() );
		buf.append( "\n" );
	}
	
	/**
	 * Callback method, called by a model object when it finished visiting it's children.
	 */
	public void popModel() {
		level--;
	}
	
	/**
	 * Clear the contents of the visitor.
	 */
	public void clear() {
		this.buf=new StringBuffer();
		this.level=0;
	}
	
	/**
	 * Get the contents of the visitor.
	 */
	public String toString() {
		return buf.toString();
	}
	
}