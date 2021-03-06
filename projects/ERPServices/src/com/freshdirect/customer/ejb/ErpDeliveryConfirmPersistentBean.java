package com.freshdirect.customer.ejb;

import java.sql.*;
import java.util.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.customer.*;

public class ErpDeliveryConfirmPersistentBean extends ErpTransactionPersistentBean {
	
	private ErpDeliveryConfirmModel model;
	
	/**
	 * Constructor for ErpRedeliveryPersistentBean.
	 */
	public ErpDeliveryConfirmPersistentBean() {
		super();
		this.model = new ErpDeliveryConfirmModel();
	}
	
	public ErpDeliveryConfirmPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}

	/**
	 * Constructor for ErpRedeliveryPersistentBean.
	 * @param pk
	 * @param conn
	 * @throws SQLException
	 */
	public ErpDeliveryConfirmPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Constructor for ErpRedeliveryPersistentBean.
	 * @param pk
	 * @param conn
	 * @param rs
	 * @throws SQLException
	 */
	public ErpDeliveryConfirmPersistentBean(PrimaryKey pk, Connection conn, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(conn, rs);
	}
	
	/**
	 * Constructor for ErpRedeliveryPersistentBean.
	 * @param model
	 */
	public ErpDeliveryConfirmPersistentBean(ErpDeliveryConfirmModel model) {
		this();
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
	 * @return ErpRedeliveryModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}
	
	/**
	 * set state of the bean from given model
	 * 
	 * @param ModelI model to copy properties from
	 */
	
	public void setFromModel(ModelI m) {
		this.model = (ErpDeliveryConfirmModel)m;	
	}
	
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.DELIVERY_CONFIRM.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpDeliveryConfirmPersistentBean bean = new ErpDeliveryConfirmPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, CUSTOMER_ID) values (?,?,?,?,?,?,?)");
		
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setTimestamp(3, new java.sql.Timestamp(this.model.getTransactionDate().getTime()));
		ps.setString(4, EnumTransactionType.DELIVERY_CONFIRM.getCode());
		//ps.setDouble(5, this.model.getAmount());
		ps.setBigDecimal(5, new java.math.BigDecimal(this.model.getAmount()));
		ps.setString(6, this.model.getTransactionSource().getCode()); 
		ps.setString(7, this.model.getCustomerId());
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
		//Coupon related.
		if(null !=this.model.getCouponTransModel()){
			ErpCouponTransactionPersistentBean ctPB = new ErpCouponTransactionPersistentBean(this.model.getCouponTransModel());
			ctPB.setParentPK(this.getPK());
			ctPB.create( conn );
		}

		this.unsetModified();
		return this.getPK();
	}
	
	public void load(Connection conn) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, SOURCE FROM CUST.SALESACTION WHERE ID = ?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(conn, rs);
		} else {
			throw new SQLException("No such ErpInvoice PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		
	}
	
	private void loadFromResultSet(Connection conn, ResultSet rs) throws SQLException {
		
		this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
		this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
		
		this.unsetModified();
		
	}

}
