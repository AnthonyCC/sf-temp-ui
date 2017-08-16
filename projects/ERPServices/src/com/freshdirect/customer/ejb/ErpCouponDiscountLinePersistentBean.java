package com.freshdirect.customer.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponOfferType;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DaoUtil;


public class ErpCouponDiscountLinePersistentBean extends ErpReadOnlyPersistentBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7840808060972124136L;
	private ErpCouponDiscountLineModel model;
	
	public ErpCouponDiscountLinePersistentBean() {
		super();
		this.model = new ErpCouponDiscountLineModel();
	}

	/** Load constructor. */
	public ErpCouponDiscountLinePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		this.load(conn);
	}

	private ErpCouponDiscountLinePersistentBean(PrimaryKey pk, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(rs);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpCouponLineModel to copy from
	 */
	public ErpCouponDiscountLinePersistentBean(ErpCouponDiscountLineModel model) {
		this();
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 * @return ErpCouponLineModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpCouponDiscountLineModel)model;
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}

	/**
	 * Find ErpOrderLinePersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpOrderLinePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;	
		List lst = new LinkedList();
		
		try {
			ps = conn.prepareStatement("SELECT ID, ORDERLINE_ID, COUPON_ID, VERSION, DISC_AMT, DISC_TYPE, REQUIRED_QTY, COUPON_DESC FROM CUST.COUPONLINE WHERE ORDERLINE_ID=? ORDER BY ID");
			ps.setString(1, parentPK.getId());
			rs = ps.executeQuery();
			while (rs.next()) {
				ErpCouponDiscountLinePersistentBean bean = new ErpCouponDiscountLinePersistentBean(new PrimaryKey(rs.getString("ID")), rs);
				bean.setParentPK(parentPK);
				lst.add(bean);
			}
		} finally {
			DaoUtil.close(rs);
			DaoUtil.close(ps);
		}
		
		return lst;
	}
	
	private final static String INSERT_QUERY =
		"INSERT INTO CUST.COUPONLINE (ID, ORDERLINE_ID, COUPON_ID, VERSION, DISC_AMT, DISC_TYPE, REQUIRED_QTY, COUPON_DESC)"
		+ " values (?,?,?,?,?,?,?,?)";

	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setString(3, this.model.getCouponId());
		ps.setInt(4, this.model.getVersion());
		ps.setBigDecimal(5, new BigDecimal(String.valueOf(this.model.getDiscountAmt())));
		ps.setInt(6, null !=this.model.getDiscountType()?Integer.parseInt(this.model.getDiscountType().getName()):null);
		ps.setInt(7, this.model.getRequiredQuantity());
		ps.setString(8, this.model.getCouponDesc());	
		try {
			if (ps.executeUpdate() != 1) {
					throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
		}
		
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT COUPON_ID, ORDERLINE_ID, VERSION, DISC_AMT,DISC_TYPE, REQUIRED_QTY, COUPON_DESC FROM CUST.COUPONLINE WHERE ID=?");
			ps.setString(1, this.getPK().getId());
			rs = ps.executeQuery();
			if (rs.next()) {
				this.loadFromResultSet(rs);
			} else {
				throw new SQLException("No such ErpCouponLine PK: " + this.getPK()); 
			}
//			rs.close();
//			ps.close();
		} catch (Exception e) {
			
		} finally {
			if(null != rs){
				try {
					rs.close();
				} catch (Exception e) {
					
				}
			}
			
			if(null != ps){
				try {
					ps.close ();
				} catch (Exception e) {
					
				}
			}
		}
	}

	private void loadFromResultSet(ResultSet rs) throws SQLException {
		this.model.setCouponId(rs.getString("COUPON_ID"));
		this.model.setOrderLineId(rs.getString("ORDERLINE_ID"));
		this.model.setCouponDesc(rs.getString("COUPON_DESC"));
		this.model.setVersion(rs.getInt("VERSION"));
		this.model.setDiscountAmt(rs.getDouble("DISC_AMT"));
		this.model.setDiscountType(EnumCouponOfferType.getEnum(""+rs.getInt("DISC_TYPE")));
		this.model.setRequiredQuantity(rs.getInt("REQUIRED_QTY"));
		
		this.unsetModified();
	}
}
