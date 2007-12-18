/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Used to provide a unique identity for all persistent objects.
 *
 * @version $Revision$
 * @author $Author$
 */ 
public class PrimaryKey implements Externalizable {
    
    /** The internal identifier */    
	private String id;
	
    /**
     * Default constructor.
     */    
	public PrimaryKey() {
		super();
		this.setId("");
	}
	
    /**
     * Creates a primary key object with an identifer. This identifier
     * must be unique.
     *
     * @param id the identifier unique to this primary key
     */    
	public PrimaryKey(String id) {
		super();
		this.setId(id);
	}
	
    /**
     * Copy constructor
     *
     * @param pk the primary key to copy
     */    
	public PrimaryKey(PrimaryKey pk) {
		super();
		this.setId( pk.getId() );
	}
	
    /**
     * Gets the unique identifer this primary key represents.
     *
     * @return the unique identifier
     */    
	public String getId() {
		return this.id;
	}
	
    /** sets the unique identity of a primary key
     * @param id the unique identifer
     */    
	protected void setId(String id) {
		if (id==null) {
			throw new NullPointerException("PK.ID cannot be null");
		}
		this.id = id;
	}
	
    /**
     * Returns a hash of this primary key's identity to be used
     * in ordering primary keys.
     *
     * @return the key's hash
     */    
	public int hashCode() {
		return this.id.hashCode();
	}
	
    /**
     * Tests if two primary keys have the same identity.
     *
     * @param o another object to test for equality
     *
     * @return true if the other primary key represents the same
     * object as this one
     */    
	public boolean equals(Object o) {
		if (o==this) {
			return true;
		}
		if (o instanceof PrimaryKey) {
			// this.getClass().equals(o.getClass()) might be better,
			// but we'll have globally unique IDs anyway...
			PrimaryKey other = (PrimaryKey) o;
			return this.id.equals( other.getId() );
		}
		return false;
	}
	
    /**
     * Gets the string equivalent of this object.
     *
     * @return the string equivalent
     */    
	public String toString() {
		return "PrimaryKey[id " + this.id + "]";
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(id);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.id = in.readUTF();
	}

}
