/*
 * ErpCancelOrderPersistentBean.java
 * Date: 07/01/2002 05:40 PM
 */

package com.freshdirect.customer.ejb;

/**
 * 
 * @author knadeem
 */
import java.sql.*;
import java.util.*;

import com.freshdirect.customer.*;
import com.freshdirect.framework.core.*;

public class ErpCancelOrderPersistentBean extends ErpTransactionPersistentBean {
	
	private ErpCancelOrderModel model;
	
	/** Default constructor. */
	public ErpCancelOrderPersistentBean() {
		super();
		this.model = new ErpCancelOrderModel();
	}
	
	public ErpCancelOrderPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}

	/** Load constructor. */
	public ErpCancelOrderPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/** Load constructor. */
	public ErpCancelOrderPersistentBean(PrimaryKey pk, Connection conn, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(conn, rs);
	}


	/**
	 * Copy constructor, from model.
	 * @param bean ErpCancelOrderModel to copy from
	 */
	public ErpCancelOrderPersistentBean(ErpCancelOrderModel model) {
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
	 * @return ErpCancelOrderModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpCancelOrderModel)model;		
	}
	
	/**
	 * Find ErpCancelOrderPersistentBean objects for a given parent.
	 * 
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpCreateOrderPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID=? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.CANCEL_ORDER.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpCancelOrderPersistentBean bean = new ErpCancelOrderPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		PreparedStatement ps = null;
		
		try{
			ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE,INITIATOR) values (?,?,?,?,?,?,?)");
			ps.setString(1, id);
			ps.setString(2, this.getParentPK().getId());
			ps.setTimestamp(3, new java.sql.Timestamp(this.model.getTransactionDate().getTime()));
			ps.setString(4, EnumTransactionType.CANCEL_ORDER.getCode());
			ps.setDouble(5, this.model.getAmount());
			ps.setString(6, this.model.getTransactionSource().getCode());
			ps.setString(7,this.model.getTransactionInitiator());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
			
		}catch(SQLException se){
			throw se;
		}finally{
			if(ps != null){
				ps.close();
				ps = null;
			}
		}
		
		return this.getPK();
	}
	
	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, initiator FROM CUST.SALESACTION WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(conn, rs);
		} else {
			throw new SQLException("No such ErpCancelOrder PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
	}
	
	private void loadFromResultSet(Connection conn, ResultSet rs) throws SQLException {
		
		this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
		this.model.setAmount(rs.getDouble("AMOUNT"));
		this.model.setTransactionInitiator(rs.getString("INITIATOR"));
		this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));

		this.unsetModified();
	}

}
