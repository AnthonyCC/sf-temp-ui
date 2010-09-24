/*
 * Created on Jul 12, 2005
 */
package com.freshdirect.customer.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author jng
 */
public class ErpDiscountLinePersistentBean extends ErpReadOnlyPersistentBean {

	private ErpDiscountLineModel model;
	
	/** Default constructor. */
	public ErpDiscountLinePersistentBean() {
		super();
		this.model = new ErpDiscountLineModel();
	}

	/** Load constructor. */
	public ErpDiscountLinePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpDiscountLineModel to copy from
	 */
	public ErpDiscountLinePersistentBean(ErpDiscountLineModel model) {
		super();
		this.setFromModel(model);
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}

	/**
	 * Copy into model.
	 * @return ErpDiscountLineModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI m) {
		this.model = (ErpDiscountLineModel) m;
	}

	/**
	 * Find ErpDiscountLinePersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpDiscountLinePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		// need to have a constant order
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.DISCOUNTLINE WHERE SALESACTION_ID=? ORDER BY ID");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpDiscountLinePersistentBean bean = new ErpDiscountLinePersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.DISCOUNTLINE (ID, SALESACTION_ID, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN) values (?,?,?,?,?)");
		int index = 1;
		ps.setString(index++, id);
		ps.setString(index++, this.getParentPK().getId());
		Discount d = this.model.getDiscount();
		if (d != null) {
			ps.setInt(index++, d.getDiscountType().getId());
			ps.setBigDecimal(index++, new BigDecimal(String.valueOf(d.getAmount())));
			ps.setString(index++, d.getPromotionCode());
		} else {
			ps.setNull(index++, Types.INTEGER);
			ps.setNull(index++, Types.DOUBLE);
			ps.setNull(index++, Types.VARCHAR);
		}
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
			ps = null;
		}
		// create children here

		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN FROM CUST.DISCOUNTLINE WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			int promoType = rs.getInt("PROMOTION_TYPE");
			if (!rs.wasNull()) {
				EnumDiscountType promotionType = EnumDiscountType.getPromotionType(promoType);
				if (promotionType==null) {
					throw new SQLException("Promotion code is wrong or does not exist");
				}
				this.model.setDiscount(new Discount(rs.getString("PROMOTION_CAMPAIGN"), promotionType, rs.getDouble("PROMOTION_AMT")));
			}
		} else {
			throw new SQLException("No such ErpDiscountLine PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;

		this.unsetModified();
	}

}
