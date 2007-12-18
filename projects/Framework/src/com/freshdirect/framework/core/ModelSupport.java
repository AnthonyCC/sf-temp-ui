/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

/**
 * an abstract class to extend when creating a new model class
 * 
 * @version $Revision$
 * @author $Author$
 */
public abstract class ModelSupport extends ModelBase {

	/**
	 * default constructor
	 */
	public ModelSupport() {
		super();
	}

	public String getId() {
		return getPK() == null ? null : getPK().getId();
	}

	public void setId(String id) {
		this.setPK(new PrimaryKey(id));
	}

}