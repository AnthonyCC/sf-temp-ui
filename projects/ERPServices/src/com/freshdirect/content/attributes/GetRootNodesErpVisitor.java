/*
 * $Workfile: GetRootNodesErpVisitor.java$
 *
 * $Date: 8/21/2001 4:29:28 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

import java.util.LinkedList;
import com.freshdirect.erp.*;
import com.freshdirect.erp.model.ErpProductModel;

/**
 * ErpVisitor to get a list of root-level node IDs (ErpModel objects that implement both EntityModelI and DurableModelI).
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public class GetRootNodesErpVisitor implements ErpVisitorI {

	private LinkedList rootIds;

	/**
	 * Default constructor
	 */
	public GetRootNodesErpVisitor() {
		this.clear();
	}

	/**
	 * Visiting constructor.
	 *
	 * @param product ErpProduct to visit
	 */
	public GetRootNodesErpVisitor(ErpProductModel product) {
		this.clear();
		product.accept(this);
	}

	/**
	 * Callback method, called by the model object when visitor enters.
	 * Useful for tracking context in a stack.
	 *
	 * @param model the model object the visitor entered
	 */
	public void pushModel(ErpModelSupport model){
		if ((model instanceof EntityModelI) && (model instanceof DurableModelI)) {
			this.rootIds.add( ((DurableModelI)model).getDurableId() );
		}
	}

	/**
	 * Callback method, called by a model object when it finished visiting it's children.
	 */
	public void popModel(){
		// intentionally blank :)
	}

	public String[] getRootIds() {
		return (String[]) this.rootIds.toArray( new String[0] );
	}

	public void clear() {
		this.rootIds = new LinkedList();
	}
}
