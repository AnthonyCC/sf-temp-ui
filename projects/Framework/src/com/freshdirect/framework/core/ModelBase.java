/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import org.apache.commons.lang.SerializationUtils;

/** a helper class the provides the basic functionality of all model objects
 *
 * @version $Revision$
 * @author $Author$
 */ 
abstract class ModelBase implements ModelI {
    
    /** the primary key for the entity this model represents
     */    
    private PrimaryKey pk;
    
    /**
     * Default constructor.
     */
    public ModelBase() {
        super();
    }
    
    /** gets the primary key associated with a model
     * @return the primary key of the entity represented by this model
     */    
    public PrimaryKey getPK() {
        return this.pk;
    }
    
    /** sets the primary key for a model
     * @param pk the primary key to associate with this model
     */    
    public void setPK(PrimaryKey pk) {
        this.pk = pk;
    }
    
    /** if a model is not yet associated with an entity it is anonymous, that is, it has no identity
     * @return true if the object has no ID assigned yet.
     */
    public boolean isAnonymous() {
    	return (this.getPK()==null || "".equals(this.getPK()));
    }

	/**
     * Combination hashcode of class name and ID.
     * @return the hash for this object
     */
	public int hashCode() {
		return
			(this.getPK()==null) ?
			super.hashCode() :
			getClass().getName().hashCode() ^ this.getPK().hashCode();
	}
	
	public boolean equals(Object o) {
		if (this==o) {
			return true;
		}
		if (o instanceof ModelI) {
			PrimaryKey opk = ((ModelI)o).getPK();
			return opk==null || this.getPK()==null ? false : this.getPK().equals(opk);
		}
		return false;
	}

	public ModelI deepCopy() {
		return (ModelI) SerializationUtils.clone(this);
	}

    /** gets the string equivalent of this model
     * @return the string equivalent
     */    
    public String toString() {
    	return "Model[" + this.getClass().getName() + ":" + this.getPK() + "]";
    }
     
}
