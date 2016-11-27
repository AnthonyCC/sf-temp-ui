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
import java.util.Date;
import java.util.List;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.QuickDateFormat;

/**
 * ErpInventoryEntry persistent bean.
 *
 * @version	$Revision$
 * @author	 $Author$
 * @stereotype fd-persistent
 */
public class ErpInventoryEntryPersistentBean extends DependentPersistentBeanSupport {

	/** Inventory entry date */
	private Date startDate;

	/** Inventory level quantity in base UOM */
	private double quantity;

	/**
	 * inventory plantId 
	 */
	private String plantId;

	/**
	 * Load constructor.
	 */
	public ErpInventoryEntryPersistentBean(PrimaryKey pk, Date startDate, double quantity, String plantId) {
		super();
		this.setPK(pk);
		this.startDate = startDate;
		this.quantity = quantity;
		this.plantId = plantId;
	}

	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpInventoryEntryModel to copy from
	 */
	public ErpInventoryEntryPersistentBean(ErpInventoryEntryModel model) {
		super();
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpInventoryEntryModel object.
	 */
	public ModelI getModel() {
		ErpInventoryEntryModel model = new ErpInventoryEntryModel(
				this.startDate,
				this.quantity,
				this.plantId
		);
		super.decorateModel(model);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		ErpInventoryEntryModel m = (ErpInventoryEntryModel)model;
		this.startDate = m.getStartDate();
		this.quantity = m.getQuantity();
		this.plantId = m.getPlantId();
	}


	/**
	 * Find ErpInventoryEntryPersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpInventoryEntryPersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT START_DATE, QUANTITY, PLANT_ID FROM ERPS.INVENTORY_ENTRY WHERE MATERIAL_SAP_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Date stDate = rs.getDate(1);
			ErpInventoryEntryPersistentBean bean =	new ErpInventoryEntryPersistentBean(
				new PrimaryKey( QuickDateFormat.SHORT_DATE_FORMATTER.format(stDate) ),
				stDate, rs.getDouble(2), rs.getString(3));
			bean.setParentPK(parentPK);
            lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}


	public PrimaryKey create(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(
			"INSERT INTO ERPS.INVENTORY_ENTRY (MATERIAL_SAP_ID, START_DATE, QUANTITY, PLANT_ID) values (?,?,?,?)" );
		ps.setString(1, this.getParentPK().getId());
		ps.setDate(2, new java.sql.Date(this.startDate.getTime()) );
		ps.setDouble(3, this.quantity );
		ps.setString(4, this.plantId);
		try {
			ps.executeUpdate();
			this.setPK( new PrimaryKey( QuickDateFormat.SHORT_DATE_FORMATTER.format(this.startDate) ) );
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
		}
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Should not be called, load is performed in finder");
	}

	public void store(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE ERPS.INVENTORY_ENTRY SET QUANTITY=? WHERE MATERIAL_SAP_ID = ? AND START_DATE = ? AND PLANT_ID = ?");
		ps.setDouble(1, this.quantity );
		ps.setString(2, this.getParentPK().getId());
		ps.setDate(3, new java.sql.Date(this.startDate.getTime()));
		ps.setString(4, this.plantId);
		ps.executeUpdate();
		ps.close();
		this.unsetModified();
	}

	public void remove(Connection conn) throws SQLException {
		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM ERPS.INVENTORY_ENTRY WHERE MATERIAL_SAP_ID = ? AND START_DATE = ? AND PLANT_ID = ?");
		ps.setString(1, this.getParentPK().getId());
		ps.setDate(2, new java.sql.Date(this.startDate.getTime()) );
		ps.setString(3, this.plantId);
		ps.executeUpdate();
		ps.close();
		this.setPK(null); // make it anonymous
	}

}
