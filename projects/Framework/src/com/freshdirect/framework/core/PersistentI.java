/*
 * $Workfile: PersistentI.java$
 *
 * $Date: 8/1/2001 11:20:26 AM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.framework.core;

import java.sql.SQLException;
import java.sql.Connection;

/** 
 *
 * @version     $Revision: 2$
 * @author      $Author: Viktor Szathmary$
 */ 
public interface PersistentI {
    
    /** indicates that the object's state has been changed since it was last
     * read from the persistent store and should be saved again
     * @return true if the object needs to be saved
     */    
    public boolean isModified();
    
    /** writes a new object to the persistent store for the first time
     * @param conn a SQLConnection to use to write this object to the
     * persistent store
     * @throws SQLException any problems encountered while writing this object
     * to the persistent store
     * @return the unique identity of this new object in the
     * persistent store
     */    
    public PrimaryKey create(Connection conn) throws SQLException;
    
    /** reads an object from the persistent store
     * @param conn a SQLConnection to use when reading this object
     * from the persistent store
     * @throws SQLException any problems entountered while reading this object
     * from the persistent store
     */    
    public void load(Connection conn) throws SQLException;
    
    /** saves this object's state to the persistent store
     * @param conn a SQLConnection to use when saving this object
     * to the persistent store
     * @throws SQLException any problems entountered while saving this object
     * from the persistent store
     */    
    public void store(Connection conn) throws SQLException;
    
    /** removes this object from the persistent store
     * @param conn a SQLConnection to use when removing this object
     * from the persistent store
     * @throws SQLException any problems entountered while removing this object
     * from the persistent store
     */    
    public void remove(Connection conn) throws SQLException;

}

