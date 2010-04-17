package com.freshdirect.framework.core;

import java.io.Serializable;

public interface ModelI extends Serializable, IdentifiedI {    

    /** gets the primary key of the entity this model is associated with
     * @return the primary key for this object
     */
	public PrimaryKey getPK();

    /** a model is anonymous if it is not yet associated with a persistent entity
     * @return true if the object has no PK assigned yet.
     */
	public boolean isAnonymous();
	
	public ModelI deepCopy();

}