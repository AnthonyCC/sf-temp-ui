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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PayloadI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedEntityBeanSupport;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * ErpClass entity bean implementation.
 *
 * @version    $Revision$
 * @author     $Author$
 *
 * @ejbHome <{ErpClassHome}>
 * @ejbRemote <{ErpClassEB}>
 * @ejbPrimaryKey <{VersionedPrimaryKey}>
 * @stereotype fd-entity
 */
public class ErpClassEntityBean extends VersionedEntityBeanSupport {

	/** SAP unique ID */
	private String sapId;

	/**
	 * Collection of dependent ErpCharacteristic persistent beans.
	 *
	 * @link aggregationByValue
	 * @directed
	 * @associates <{ErpCharacteristicPersistentBean}>
	 */
	private CharacteristicList characteristics;

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.erp.ejb.ErpClassHome";
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpClassModel object.
	 */
	public ModelI getModel() {
		ErpClassModel model = new ErpClassModel(this.sapId, this.getCharacteristics());
		super.decorateModel(model);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		// copy properties from model
		ErpClassModel m = (ErpClassModel) model;
		this.sapId = m.getSapId();
		this.setCharacteristicsFromModel(m.getCharacteristics());
	}

	/**
	 * Create with version
	 */
	public VersionedPrimaryKey create(Connection conn, int version) throws SQLException {
		String id = this.getNextId(conn, "ERPS");
		PreparedStatement ps = conn.prepareStatement("insert into erps.class (id, version, sap_id) values (?, ?, ?)");
		ps.setString(1, id);
		ps.setInt(2, version);
		ps.setString(3, this.sapId);
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("No database rows created!");
		}

		ps.close();

		this.setPK(new VersionedPrimaryKey(id, version));

		// create children
		this.characteristics.setParentPK(this.getPK());
		this.characteristics.create(conn);

		return (VersionedPrimaryKey) this.getPK();
	}

	/**
	 * Set from payload.
	 *
	 * @param payload payload object
	 */
	public void setFromPayload(PayloadI payload) {
		ErpClassPayload p = (ErpClassPayload) payload;
		this.sapId = p.sapId;
		this.characteristics = p.characteristics;
	}

	/**
	 * Load the row with specified PK into a payload object.
	 *
	 * @param conn database connection
	 * @param pk primary key
	 *
	 * @return payload object, null if row was not found
	 *
	 * @throws SQLException if a database error occured
	 */
	public PayloadI loadRowPayload(Connection conn, PrimaryKey pk) throws SQLException {

		PreparedStatement ps = conn.prepareStatement("select sap_id from erps.class where id = ?");
		ps.setString(1, pk.getId());
		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {
			return null;
		}

		ErpClassPayload p = new ErpClassPayload();
		p.sapId = rs.getString(1);

		rs.close();
		ps.close();

		// load children
		p.characteristics.setParentPK(pk);
		p.characteristics.load(conn);

		return p;
	}

	public Collection ejbFindAllClasses() throws FinderException {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select max(to_number(id)), max(version), sap_id from erps.class group by sap_id order by sap_id");
			ResultSet rs = ps.executeQuery();

			LinkedList results = new LinkedList();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2), null);
				results.add(vpk);
			}

			rs.close();
			ps.close();

			return results;

		} catch (SQLException sqle) {
			throw new FinderException("Unable to find Classes : " + sqle.getMessage());
		} finally {
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new EJBException(e);
				}
			}
		}

	}

	public VersionedPrimaryKey ejbFindBySapId(String sapId) throws FinderException {

		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, version, sap_id from erps.class where sap_id=? and version=(select max(version) from erps.class where sap_id=?)");
			ps.setString(1, sapId);
			ps.setString(2, sapId);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find a Class with an SAP ID = " + sapId);
			}

			VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2), null);

			rs.close();
			ps.close();

			return vpk;

		} catch (SQLException sqle) {
			throw new EJBException("Unable to find a Class by its SAP ID : " + sqle.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				throw new EJBException(sqle);
			}
		}

	}

	public void remove(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - store() should not be called, business object unmutable.");
	}

	public void initialize() {
		this.sapId = null;
		this.characteristics = new CharacteristicList();
	}

	/**
	 * Get characteristics.
	 *
	 * @return collection of ErpCharacteristicModel objects
	 */
	public List getCharacteristics() {
		// copy into models
		return this.characteristics.getModelList();
	}

	/**
	 * Protected setter for characteristics. Overwrites existing collection.
	 *
	 * @param collection collection of ErpCharacteristicModel objects
	 */
	protected void setCharacteristicsFromModel(Collection collection) {
		// create persistent bean collection
		java.util.List persistentBeans = new java.util.LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpCharacteristicModel model = (ErpCharacteristicModel) i.next();
			persistentBeans.add(new ErpCharacteristicPersistentBean(model));
		}
		// set it
		this.characteristics.set(persistentBeans);
	}

	/**
	 * Inner class for the list of dependent ErpCharacteristic persistent beans.
	 */
	private static class CharacteristicList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(
				ErpCharacteristicPersistentBean.findByParent(conn, (VersionedPrimaryKey) CharacteristicList.this.getParentPK()));
		}
	}

	/**
	 * Payload class used in the fat PK
	 */
	private static class ErpClassPayload implements PayloadI {
		String sapId;
		CharacteristicList characteristics = new CharacteristicList();
	}

}
