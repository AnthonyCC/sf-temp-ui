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

import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * ErpCharacteristic persistent bean.
 * Parent must have a VersionedPrimaryKey.
 * 
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpCharacteristicPersistentBean extends ErpPersistentBeanSupport {

	/** Characteristic name */
	private String name;

	/**
	 * Collection of dependent ErpCharacteristicValue persistent beans.
	 *
	 * @link aggregationByValue
	 * @directed
	 * @associates <{ErpCharacteristicValuePersistentBean}>
	 */
	private CharacteristicValueList characteristicValues = new CharacteristicValueList();

	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpCharacteristicModel to copy from
	 */
	public ErpCharacteristicPersistentBean(ErpCharacteristicModel model) {
		super();
		this.setFromModel(model);
	}

	/**
	 * Quick load constructor, load() not called, everything comes from the parameters.
	 *
	 * @param pk versioned primary key
	 * @param name characteristic name
	 */
	public ErpCharacteristicPersistentBean(VersionedPrimaryKey pk, String name) {
		super(pk);
		this.name = name;
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpCharacteristicModel object.
	 */
	public ModelI getModel() {
		ErpCharacteristicModel model = new ErpCharacteristicModel(this.name, this.getCharacteristicValues());
		super.decorateModel(model);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		ErpCharacteristicModel m = (ErpCharacteristicModel) model;
		this.name = m.getName();
		this.setCharacteristicValues(m.getCharacteristicValues());
	}

	/**
	 * Find ErpCharacteristicPersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpCharacteristicPersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, VersionedPrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();

		PreparedStatement ps = conn.prepareStatement("select id, name from erps.characteristic where class_id = ?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String id = rs.getString(1);
			String name = rs.getString(2);
			ErpCharacteristicPersistentBean bean =
				new ErpCharacteristicPersistentBean(new VersionedPrimaryKey(id, parentPK.getVersion()), name);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();

		// load children
		for (Iterator i = lst.iterator(); i.hasNext();) {
			((ErpCharacteristicPersistentBean) i.next()).loadChildren(conn);
		}

		return lst;
	}

	/**
	 * Load characteristic values for this bean.
	 *
	 * @param conn database connection
	 * 
	 * @throws SQLException if database error occurs
	 */
	protected void loadChildren(Connection conn) throws SQLException {
		this.characteristicValues.setParentPK(this.getPK());
		this.characteristicValues.load(conn);
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "ERPS");
		int version = ((VersionedPrimaryKey) this.getParentPK()).getVersion();
		PreparedStatement ps = conn.prepareStatement("insert into erps.characteristic (id, class_id, version, name) values (?, ?, ?, ?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setInt(3, version);
		ps.setString(4, this.name);
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}
		ps.close();

		this.setPK(new VersionedPrimaryKey(id, version));

		// create children
		this.characteristicValues.setParentPK(this.getPK());
		this.characteristicValues.create(conn);

		//this.unsetModified();
		return this.getPK();
	}

	/**
	 * Get characteristic values.
	 *
	 * @return collection of ErpCharacteristicValueModel objects
	 */
	public List getCharacteristicValues() {
		// copy into models
		return this.characteristicValues.getModelList();
	}

	/**
	 * Protected setter for characteristic values. Overwrites existing collection.
	 *
	 * @param collection collection of ErpCharacteristicValueModel objects
	 */
	protected void setCharacteristicValues(Collection collection) {
		// create persistent bean collection
		List persistentBeans = new LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpCharacteristicValueModel model = (ErpCharacteristicValueModel) i.next();
			persistentBeans.add(new ErpCharacteristicValuePersistentBean(model));
		}
		// set it
		this.characteristicValues.set(persistentBeans);
	}

	/**
	 * Inner class for the list of dependent ErpCharacteristicValue persistent beans.
	 */
	private class CharacteristicValueList extends DependentPersistentBeanList {

		public CharacteristicValueList() {
			super(getPK());
		}

		public void load(Connection conn) throws SQLException {
			this.set(ErpCharacteristicValuePersistentBean.findByParent(conn, (VersionedPrimaryKey) getPK()));
		}

	}

}
