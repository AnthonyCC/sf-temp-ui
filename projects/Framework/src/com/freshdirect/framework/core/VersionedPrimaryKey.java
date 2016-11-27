/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Composite primary key class with version, and extra fat :-).
 * That is, it carries an optional payload for efficient loading.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public class VersionedPrimaryKey extends FatPrimaryKey {

	/** Version number */
	private int version;

	/**
	 * Default constructor.
	 */    
	public VersionedPrimaryKey() {
		this("", -1);
	}
	
	/**
	 * Constructor with ID and version.
	 *
	 * @param id the id for this primary key
	 * @param version entity version number for this PK
	 */
	public VersionedPrimaryKey(String id, int version) {
		super(id);
		this.version=version;
	}
	
	/**
	 * Constructor with ID, version and payload.
	 *
	 * @param id the id for this primary key
	 * @param version entity version number for this PK
	 * @param payload payload object for efficient loading
	 */
	public VersionedPrimaryKey(String id, int version, PayloadI payload) {
		super(id, payload);
		this.version=version;
	}

	/**
	 * Get the entity version for this PK.
	 * 
	 * @return version
	 */
	public int getVersion() {
		return this.version;
	}
	
    /**
     * Returns a hash of this primary key's identity to be used
     * in ordering primary keys.
     *
     * @return the key's hash
     */    
	public int hashCode() {
		return super.hashCode() ^ version;
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
		if (o instanceof VersionedPrimaryKey) {
			VersionedPrimaryKey other = (VersionedPrimaryKey) o;
			return (this.version==other.getVersion()) && this.getId().equals(other.getId());
		}
		return false;
	}
    
    /**
     * Gets the string equivalent of the key.
     *
     * @return the string equivalent
     */    
	public String toString() {
		return "VersionedPrimaryKey[id " + this.getId() + ",version " + this.version + ", payload " + this.getPayload() + "]";
	}
	
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(this.version);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		this.version = in.readInt();
	}

}
