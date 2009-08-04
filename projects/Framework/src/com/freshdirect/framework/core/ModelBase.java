/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

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
        // do a serialization / de-serialization cycle as a trick
        // against explicit deep cloning

        // serialization
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        ObjectOutputStream    oas  = null;
        try {
            oas = new ObjectOutputStream(baos);
            oas.writeObject(this);
            oas.close();
        } catch (IOException e) {
            return null;
        }

        // de-serialization
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream    oin = null;
        try {
            oin = new ObjectInputStream(bais);
            return (ModelI) oin.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
	}

    /** gets the string equivalent of this model
     * @return the string equivalent
     */    
    public String toString() {
    	return "Model[" + this.getClass().getName() + ":" + this.getPK() + "]";
    }
     
}
