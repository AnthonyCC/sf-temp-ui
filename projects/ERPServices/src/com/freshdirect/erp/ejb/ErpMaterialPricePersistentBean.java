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
import java.util.Collections;
import java.util.List;

import com.freshdirect.erp.PricingFactory;
import com.freshdirect.erp.model.EnumPriceType;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * ErpMaterialPrice persistent bean.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpMaterialPricePersistentBean extends ErpPersistentBeanSupport {

	/** SAP unique ID */
	private String sapId;

	/** Price in USD per pricing unit */
	private double price;

	/** Pricing unit of measure */
	private String pricingUnit;

	/** Scale quantity */
	private double scaleQuantity;

	/** Scale unit of measure */
	private String scaleUnit;
	
	/** Zone Id of Price */
	private String sapZoneId;

	/** Price Type */
	private double promoPrice;
	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpMaterialPriceModel to copy from
	 */
	public ErpMaterialPricePersistentBean(ErpMaterialPriceModel model) {
		super();
		this.setFromModel(model);
	}

	/**
	 * Quick load constructor, load() not called, everything comes from the parameters.
	 *
	 * @param pk versioned primary key
	 * @param sapId SAP unique ID
	 * @param price Price in USD per pricing unit
	 * @param pricingUnit Pricing unit of measure
	 * @param scaleQuantity Scale quantity
	 * @param scaleUnit Scale unit of measure
	 */
	public ErpMaterialPricePersistentBean(
		VersionedPrimaryKey pk,
		String sapId,
		double price,
		String pricingUnit,
		double scaleQuantity,
		String scaleUnit,
		String sapZoneId,
		double promoPrice) {
		super(pk);
		this.sapId = sapId;
		this.price = price;
		this.pricingUnit = pricingUnit;
		this.scaleQuantity = scaleQuantity;
		this.scaleUnit = scaleUnit;
		this.sapZoneId = sapZoneId;
		this.promoPrice = promoPrice;
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpMaterialPriceModel object.
	 */
	public ModelI getModel() {
		ErpMaterialPriceModel model =
			new ErpMaterialPriceModel(this.sapId, this.price, this.pricingUnit, this.scaleQuantity, this.scaleUnit, this.sapZoneId, this.promoPrice);
		super.decorateModel(model);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		ErpMaterialPriceModel m = (ErpMaterialPriceModel) model;
		this.sapId = m.getSapId();
		this.price = m.getPrice();
		this.pricingUnit = m.getPricingUnit();
		this.scaleQuantity = m.getScaleQuantity();
		this.scaleUnit = m.getScaleUnit();
		this.sapZoneId = m.getSapZoneId();
		this.promoPrice = m.getPromoPrice();
	}

	/**
	 * Find ErpMaterialPricePersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpMaterialPricePersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, VersionedPrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("select id,sap_id,price,pricing_unit,scale_quantity,scale_unit,sap_zone_id,promo_price from erps.materialprice where mat_id = ?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			ErpMaterialPricePersistentBean bean =
				new ErpMaterialPricePersistentBean(
					new VersionedPrimaryKey(rs.getString(1), parentPK.getVersion()),
					rs.getString(2),
					rs.getDouble(3),
					rs.getString(4),
					rs.getDouble(5),
					rs.getString(6),
					rs.getString(7),
					rs.getDouble(8));
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
		PreparedStatement ps = conn.prepareStatement("insert into erps.materialprice (id, mat_id, version, sap_id, price, pricing_unit, scale_quantity, scale_unit, sap_zone_id, promo_price) values (?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setInt(3, version);
		ps.setString(4, this.sapId);
		ps.setDouble(5, this.price);
		ps.setString(6, this.pricingUnit);
		ps.setDouble(7, this.scaleQuantity);
		ps.setString(8, this.scaleUnit);
		ps.setString(9, this.sapZoneId);
		ps.setDouble(10, this.promoPrice);
		if (ps.executeUpdate() != 1) {
			throw new SQLException("No database rows created!");
		}
		ps.close();

		this.setPK(new VersionedPrimaryKey(id, version));

		return this.getPK();
	}

}
