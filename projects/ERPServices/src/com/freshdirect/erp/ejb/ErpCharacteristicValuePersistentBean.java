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
import java.util.List;

import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * ErpCharacteristicValue persistent bean.
 * Parent must have a VersionedPrimaryKey.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpCharacteristicValuePersistentBean extends ErpPersistentBeanSupport {

	/** Characteristic value name */
	private String name;

	/** Characteristic value description */
	private String description;

	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpCharacteristicValueModel to copy from
	 */
	public ErpCharacteristicValuePersistentBean(ErpCharacteristicValueModel model) {
		super();
		this.setFromModel(model);
	}

	/**
	 * Quick load constructor, load() not called, everything comes from the parameters.
	 *
	 * @param pk versioned primary key
	 * @param name characteristic value name
	 * @param description char value description
	 */
	public ErpCharacteristicValuePersistentBean(VersionedPrimaryKey pk, String name, String description) {
		super(pk);
		this.name = name;
		this.description = description;
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpCharacteristicValueModel object.
	 */
	public ModelI getModel() {
		ErpCharacteristicValueModel model = new ErpCharacteristicValueModel(this.name, this.description);
		super.decorateModel(model);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		ErpCharacteristicValueModel m = (ErpCharacteristicValueModel) model;
		this.name = m.getName();
		this.description = m.getDescription();
	}

	/**
	 * Find ErpCharacteristicValuePersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpCharacteristicValuePersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, VersionedPrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();

		PreparedStatement ps = conn.prepareStatement("select id, name, description from erps.charvalue where char_id = ?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			ErpCharacteristicValuePersistentBean bean =
				new ErpCharacteristicValuePersistentBean(
					new VersionedPrimaryKey(rs.getString(1), parentPK.getVersion()),
					rs.getString(2),
					rs.getString(3));
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "ERPS");
		int version = ((VersionedPrimaryKey) this.getParentPK()).getVersion();

		PreparedStatement ps = conn.prepareStatement("insert into erps.charvalue (id, char_id, version, name, description) values (?, ?, ?, ?, ?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setInt(3, version);
		ps.setString(4, this.name);
		ps.setString(5, this.description);

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}
		ps.close();

		this.setPK(new VersionedPrimaryKey(id, version));

		return this.getPK();
	}

}
