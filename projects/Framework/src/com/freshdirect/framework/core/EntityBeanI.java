/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import javax.ejb.*;

/** the interface to be implemented by all entity bean implementation classes
 *
 * @version $Revision$
 * @author $Author$
 */ 
public interface EntityBeanI extends PersistentBeanI, EntityBean {

    /** cleans all of the member variables of an entity to prevent any dirty data
     * from appearing after a bean instance has been passivated and re-activated from the pool
     */    
	public void initialize();

}

