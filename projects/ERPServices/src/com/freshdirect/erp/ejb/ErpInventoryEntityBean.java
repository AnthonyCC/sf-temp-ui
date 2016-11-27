/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.EntityBeanI;
import com.freshdirect.framework.core.EntityBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpInventory entity bean implementation.
 *
 * @version    $Revision$
 * @author     $Author$
 *
 * @ejbHome <{com.freshdirect.erp.ejb.ErpInventoryHome}>
 * @ejbRemote <{com.freshdirect.erp.ejb.ErpInventoryEB}>
 * @ejbPrimaryKey <{com.freshdirect.framework.core.PrimaryKey}>
 * @stereotype fd-entity
 */
public class ErpInventoryEntityBean extends EntityBeanSupport implements EntityBeanI {

	/** SAP unique material number */
	private String sapId;

	/** Date of last inventory update */
	private Date lastUpdated;

	/**
	 * Collection of dependent ErpInventoryEntry persistent beans.
	 *
	 * @link aggregationByValue
	 * @directed
	 * @associates <{com.freshdirect.erp.ejb.ErpInventoryEntryPersistentBean}>
	 */
	private EntryList entries;

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.erp.ejb.ErpInventoryHome";
	}

	public void initialize() {
		this.sapId = null;
		this.lastUpdated = null;
		this.entries = new EntryList();
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpInventoryModel object.
	 */
	public ModelI getModel() {
		ErpInventoryModel model = new ErpInventoryModel(this.sapId, this.lastUpdated, this.entries.getModelList());
		super.decorateModel(model);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		// copy properties from model
		ErpInventoryModel m = (ErpInventoryModel) model;
		this.sapId = m.getSapId();
		this.lastUpdated = m.getLastUpdated();
		this.setEntries(m.getEntries());

		this.setModified();
	}

	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT COUNT(*) FROM ERPS.INVENTORY where MATERIAL_SAP_ID=?");
			ps.setString(1, pk.getId());
			rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					throw new ObjectNotFoundException("Unable to find ErpInventory with PK " + pk);
				}
				return new PrimaryKey(pk.getId());
			} else {
				throw new FinderException("Empty resultset");
			}
		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle2) {
				// eat it
			}
		}
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		this.setPK(new PrimaryKey(this.sapId));

		// persist self
		PreparedStatement ps = conn.prepareStatement("INSERT INTO ERPS.INVENTORY (MATERIAL_SAP_ID, DATE_MODIFIED) VALUES (?,?)");
		ps.setString(1, this.sapId);
		ps.setTimestamp(2, new Timestamp(this.lastUpdated.getTime()));
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Failed to insert row");
		}
		ps.close();

		// create children
		this.entries.setParentPK(this.getPK());
		this.entries.create(conn);

		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		// load self
		PreparedStatement ps = conn.prepareStatement("SELECT DATE_MODIFIED FROM ERPS.INVENTORY WHERE MATERIAL_SAP_ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.lastUpdated = rs.getTimestamp(1);
		}
		rs.close();
		ps.close();
		this.sapId = this.getPK().getId();

		// load children
		this.entries.setParentPK(this.getPK());
		this.entries.load(conn);

	}

	public void store(Connection conn) throws SQLException {
		// store self
		if (this.isModified()) {
			PreparedStatement ps = conn.prepareStatement("UPDATE ERPS.INVENTORY SET DATE_MODIFIED=? WHERE MATERIAL_SAP_ID=?");
			ps.setTimestamp(1, new Timestamp(this.lastUpdated.getTime()));
			ps.setString(2, this.sapId);
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Failed to update row");
			}
		}
		// store children
		if (this.entries.isModified()) {
			this.entries.store(conn);
		}
	}

	public void remove(Connection conn) throws SQLException {

		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM ERPS.INVENTORY WHERE MATERIAL_SAP_ID=?");
		ps.setString(1, this.getPK().getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Failed to delete row");
		}
		ps.close();
	}

	/**
	 * Overriden isModified.
	 */
	public boolean isModified() {
		// check children too
		return super.isModified() || this.entries.isModified();
	}

	/**
	 * Get all inventory entries.
	 *
	 * @return collection of ErpInventoryEntryModel objects
	 */
	public List getEntries() {
		// copy into models
		return this.entries.getModelList();
	}

	/**
	 * Set inventory entries. Overwrites existing collection only if
	 * the specified dateCreated is newer than what's already stored.
	 *
	 * @param dateCreated date the entries were queried on
	 * @param collection collection of ErpInventoryEntryModel objects
	 *
	 * @return true if the entires were stored
	 */
	public boolean setEntries(Date dateCreated, Collection collection) {
		if (this.lastUpdated.before(dateCreated)) {
			this.setEntries(collection);
			this.lastUpdated = dateCreated;
			this.setModified();
			return true;
		}
		return false;
	}

	protected void setEntries(Collection collection) {
		// create persistent bean collection
		java.util.List persistentBeans = new java.util.LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpInventoryEntryModel model = (ErpInventoryEntryModel) i.next();
			persistentBeans.add(new ErpInventoryEntryPersistentBean(model));
		}
		// set it
		this.entries.set(persistentBeans);
	}

	/**
	 * Inner class for the list of dependent ErpInventoryEntry persistent beans.
	 */
	private class EntryList extends DependentPersistentBeanList {

		public EntryList() {
			super(getPK());
		}

		public EntryList(Connection conn) throws SQLException {
			this();
			load(conn);
		}

		public void load(Connection conn) throws SQLException {
			this.set(ErpInventoryEntryPersistentBean.findByParent(conn, getPK()));
		}

	}
}