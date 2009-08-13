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

import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * ErpSalesUnit persistent bean.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpSalesUnitPersistentBean extends ErpPersistentBeanSupport {

	/** Alternative unit of measure */
	private String alternativeUnit;

	/** Base unit of measure */
	private String baseUnit;

	/** Numerator */
	private int numerator;

	/** Denominator */
	private int denominator;

	/** Sales unit description */
	private String description;

	/** [APPDEV-209]Display Indicator - Only for display or not.*/
	private boolean displayInd;
	
	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpSalesUnitModel to copy from
	 */
	public ErpSalesUnitPersistentBean(ErpSalesUnitModel model) {
		super();
		this.setFromModel(model);
	}

	/**
	 * Quick load constructor, load() not called, everything comes from the parameters.
	 *
	 */
	public ErpSalesUnitPersistentBean(
		VersionedPrimaryKey pk,
		String alternativeUnit,
		String baseUnit,
		int numerator,
		int denominator,
		String description,
		boolean displayInd) {
		super(pk);
		this.alternativeUnit = alternativeUnit;
		this.baseUnit = baseUnit;
		this.numerator = numerator;
		this.denominator = denominator;
		this.description = description;
		this.displayInd = displayInd;
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpSalesUnitModel object.
	 */
	public ModelI getModel() {
		ErpSalesUnitModel model =
			new ErpSalesUnitModel(this.alternativeUnit, this.baseUnit, this.numerator, this.denominator, this.description, this.displayInd);
		super.decorateModel(model);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		ErpSalesUnitModel m = (ErpSalesUnitModel) model;
		this.alternativeUnit = m.getAlternativeUnit();
		this.baseUnit = m.getBaseUnit();
		this.numerator = m.getNumerator();
		this.denominator = m.getDenominator();
		this.description = m.getDescription();
		this.displayInd = m.isDisplayInd();
	}

	/**
	 * Find ErpSalesUnitPersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpSalesUnitPersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, VersionedPrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("select id, alternative_unit, base_unit, numerator, denominator, description, display_ind from erps.salesunit where mat_id = ? and upper(display_ind) <>'Y'" );
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			ErpSalesUnitPersistentBean bean =
				new ErpSalesUnitPersistentBean(
					new VersionedPrimaryKey(rs.getString(1), parentPK.getVersion()),
					rs.getString(2),
					rs.getString(3),
					rs.getInt(4),
					rs.getInt(5),
					rs.getString(6),
					"Y".equalsIgnoreCase(rs.getString(7)));
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
		PreparedStatement ps = conn.prepareStatement("insert into erps.salesunit (id, mat_id, version, alternative_unit, base_unit, numerator, denominator, description, display_ind) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setInt(3, version);
		ps.setString(4, this.alternativeUnit);
		ps.setString(5, this.baseUnit);
		ps.setInt(6, this.numerator);
		ps.setInt(7, this.denominator);
		ps.setString(8, this.description);
		ps.setString(9, this.displayInd?"Y":"N");
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("No database rows created!");
		}
		ps.close();

		this.setPK(new VersionedPrimaryKey(id, version));

		return this.getPK();
	}

	
	/**
	 * Find ErpSalesUnitPersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpSalesUnitPersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParentForDisplay(Connection conn, VersionedPrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("select id, alternative_unit, base_unit, numerator, denominator, description, display_ind from erps.salesunit where mat_id = ? and upper(display_ind) ='Y'" );
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			ErpSalesUnitPersistentBean bean =
				new ErpSalesUnitPersistentBean(
					new VersionedPrimaryKey(rs.getString(1), parentPK.getVersion()),
					rs.getString(2),
					rs.getString(3),
					rs.getInt(4),
					rs.getInt(5),
					rs.getString(6),
					"Y".equalsIgnoreCase(rs.getString(7)));
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}
	
}
