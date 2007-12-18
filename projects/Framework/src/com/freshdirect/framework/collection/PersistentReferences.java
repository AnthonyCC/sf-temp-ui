/*
 * $Workfile: PersistentReferences.java$
 *
 * $Date: 8/14/2001 5:42:43 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.collection;

import java.sql.*;
import java.util.List;
import java.util.LinkedList;

import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.PersistentI;

/**
 * Represents a simple n-to-n relationship, persisted to a table.
 *
 * @version    $Revision: 8$
 * @author     $Author: Mike Rose$
 */
public class PersistentReferences implements PersistentI {
    
	private String tableName;
	private String parentFieldName;
	private String refFieldName;
	
	private String parentId = null;
	private String[] refs = null;
	
	private boolean modified=false;
	
	
	/**
	 * PersistentReferences constructor.
	 *
	 * @param tableName table to persist into
	 * @param parentFieldName database field name for parent ID (eg. "PARENT_ID")
	 * @param refFieldName field name for referenced IDs (eg. "CHILD_ID")
	 */
	public PersistentReferences(String tableName, String parentFieldName, String refFieldName) {
		this.tableName = tableName;
		this.parentFieldName = parentFieldName;
		this.refFieldName = refFieldName;
	}

	/**
	 * PersistentReferences constructor with parent ID.
	 *
	 * @param tableName table to persist into
	 * @param parentFieldName database field name for parent ID (eg. "PARENT_ID")
	 * @param refFieldName field name for referenced IDs (eg. "CHILD_ID")
	 * @param parentId ID of parent
	 */
	public PersistentReferences(String tableName, String parentFieldName, String refFieldName, String parentId) {
		this(tableName, parentFieldName, refFieldName);
		this.setParentId(parentId);
	}

	public void setParentId(String parentId) {
		if (this.parentId!=null) {
			throw new IllegalStateException("ParentID already set");
		}
		this.parentId = parentId;
	}
	
	public void setRefs(String[] ids) {
		if (ids==null) {
			throw new NullPointerException("ID array cannot be null");
		}
		String[] copy = new String[ids.length];
		System.arraycopy(ids, 0, copy, 0, copy.length);
		this.refs = copy;
		this.modified=true;
	}
			
	public String[] getRefs() {
		String[] copy = new String[this.refs.length];
		System.arraycopy(this.refs, 0, copy, 0, copy.length);
		return copy;
	}
    
    public String getTableName() {
        return this.tableName;
    }
    
    public String getParentFieldName() {
        return this.parentFieldName;
    }
    
    public String getRefFieldName() {
        return this.refFieldName;
    }
    
    public String getParentId() {
        return this.parentId;
    }
	
	public boolean isModified() {
		return this.modified;
	}
	
	public PrimaryKey create(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO "+tableName+"("+parentFieldName+","+refFieldName+") VALUES(?,?)");
		ps.setString(1, parentId);
		for (int i=0; i<this.refs.length; i++) {
			ps.setString(2, this.refs[i]);
			ps.executeUpdate();
		}
		ps.close();
		this.modified=false;
		return null;
	}
	
	public void load(Connection conn) throws SQLException {
		List lst = new LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT "+refFieldName+" FROM "+tableName+" WHERE "+parentFieldName+"=?");
		ps.setString(1, parentId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			lst.add(rs.getString(1));
		}
		rs.close();
		ps.close();
		this.refs = (String[])lst.toArray(new String[0]);
	}
	
	public void store(Connection conn) throws SQLException {
		if (this.modified) {
			this.remove(conn);
			this.create(conn);
		}
	}
	
	public void remove(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM "+tableName+" WHERE "+parentFieldName+"=?");
		ps.setString(1, parentId);
		ps.executeUpdate();
		ps.close();
	}
	
}
