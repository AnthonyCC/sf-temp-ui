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
 * "Fat" Primary Key class.
 * <P>
 * Can contain an arbitary Object with all the data loaded
 * with the finder query - this is called the payload.
 * <p>
 * Useful for optimizing performance of persistent objects that have
 * very few member variables (one or two besides a primary key).
 * <P>
 * The payload is not serialized with the primary key.
 *
 * @version     $Revision$
 * @author      $Author$
 */ 
public class FatPrimaryKey extends PrimaryKey {

    /** The payload for the object this primary key represents - not serialized */
	private transient PayloadI payload = null;

    /**
     * Default constructor
     */    
	public FatPrimaryKey() {
		super();
	}
	
    /**
     * Constructor that takes an id only.
     *
     * @param id the id for this primary key
     */    
	public FatPrimaryKey(String id) {
		super(id);
	}

    /**
     * Constructor that takes an id and a model.
     *
     * @param id the id for this primary key
     * @param payload the payload object for this primary key
     */    
	public FatPrimaryKey(String id, PayloadI payload ) {
		super(id);
		this.payload = payload;
	}

    /**
     * Get the payload the key is carrying.
     *
     * @return the payload, or null if the payload has been removed (or never been set)
     */    
	public PayloadI getPayload() {
		return this.payload ;
	}

    /**
     * Get the payload the key is carrying, removing it at the same time,
     * thereby reducing the size of the fat key.
     *
     * @return the payload, or null if the payload has been removed (or never been set)
     */    
	public PayloadI grabPayload() {
		PayloadI o = this.payload;
		this.payload = null;
		return o;
	}

    /**
     * Gets the string equivalent of the key.
     *
     * @return the string equivalent
     */    
	public String toString() {
		return "FatPrimaryKey[id " + this.getId() + ", payload " + this.payload + "]";
	}
}
