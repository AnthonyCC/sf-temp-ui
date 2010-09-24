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
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpChargeLine persistent bean.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpChargeLinePersistentBean extends ErpReadOnlyPersistentBean {

	private ErpChargeLineModel model;
	
	/** Default constructor. */
	public ErpChargeLinePersistentBean() {
		super();
		this.model = new ErpChargeLineModel();
	}

	/** Load constructor. */
	public ErpChargeLinePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpChargeLineModel to copy from
	 */
	public ErpChargeLinePersistentBean(ErpChargeLineModel model) {
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
	 * @return ErpChargeLineModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI m) {
		this.model = (ErpChargeLineModel) m;
	}

	/**
	 * Find ErpChargeLinePersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpChargeLinePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.CHARGELINE WHERE SALESACTION_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpChargeLinePersistentBean bean = new ErpChargeLinePersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.CHARGELINE (ID, SALESACTION_ID, AMOUNT, REASON_CODE, TYPE, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN, TAX_RATE) values (?,?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setBigDecimal(3, new BigDecimal(this.model.getAmount()));
		ps.setString(4, this.model.getReasonCode());
		ps.setString(5, this.model.getType().getCode());
		Discount d = this.model.getDiscount();
		if (d != null) {
			ps.setInt(6, d.getDiscountType().getId());
			ps.setBigDecimal(7, new BigDecimal(d.getAmount()));
			ps.setString(8, d.getPromotionCode());
		} else {
			ps.setNull(6, Types.INTEGER);
			ps.setNull(7, Types.DOUBLE);
			ps.setNull(8, Types.VARCHAR);
		}
		ps.setBigDecimal(9, new BigDecimal(this.model.getTaxRate()));
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
		PreparedStatement ps = conn.prepareStatement("SELECT AMOUNT, REASON_CODE, TYPE, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN, TAX_RATE FROM CUST.CHARGELINE WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setAmount(rs.getDouble("AMOUNT"));
			this.model.setReasonCode(rs.getString("REASON_CODE"));
			this.model.setType(EnumChargeType.getEnum(rs.getString("TYPE")));
			this.model.setTaxRate(rs.getDouble("TAX_RATE"));

			int promoType = rs.getInt("PROMOTION_TYPE");
			if (!rs.wasNull()) {
				EnumDiscountType promotionType = EnumDiscountType.getPromotionType(promoType);
				if (promotionType==null) {
					throw new SQLException("Promotion code is wrong or does not exist");
				}
				this.model.setDiscount(new Discount(rs.getString("PROMOTION_CAMPAIGN"), promotionType, rs.getDouble("PROMOTION_AMT")));
			}

		} else {
			throw new SQLException("No such ErpChargeLineLine PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;

		this.unsetModified();
	}

}
