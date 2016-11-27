package com.freshdirect.framework.core;

/**
 * an abstract class to extend when creating a new model class
 * 
 * @version $Revision$
 * @author $Author$
 */

public abstract class ModelSupport extends ModelBase {

	private static final long	serialVersionUID	= -7178043825081956157L;

	/**
	 * default constructor
	 */
	public ModelSupport() {
		super();
	}

	/**
	 * @return The ID of the PrimaryKey if there is one, null otherwise
	 */
	public String getId() {
		return getPK() == null ? null : getPK().getId();
	}

	/**
	 * Sets a new PrimaryKey with the given ID.
	 * @param id ID of the new PK
	 */
	public void setId( String id ) {
		this.setPK( new PrimaryKey( id ) );
	}

}