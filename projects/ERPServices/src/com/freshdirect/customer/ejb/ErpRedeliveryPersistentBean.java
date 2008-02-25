/**
 * 
 * ErpRedeliveryPersistentBean.java
 * Created Nov 19, 2002
 */
package com.freshdirect.customer.ejb;

/**
 *
 *  @author knadeem
 */
import java.sql.*;
import java.util.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.customer.*;

public class ErpRedeliveryPersistentBean extends ErpTransactionPersistentBean {
	
	private ErpRedeliveryModel model;
	
	/**
	 * Constructor for ErpRedeliveryPersistentBean.
	 */
	public ErpRedeliveryPersistentBean() {
		super();
		this.model = new ErpRedeliveryModel();
	}
	
	public ErpRedeliveryPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}

	/**
	 * Constructor for ErpRedeliveryPersistentBean.
	 * @param pk
	 * @param conn
	 * @throws SQLException
	 */
	public ErpRedeliveryPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
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
	public ErpRedeliveryPersistentBean(PrimaryKey pk, Connection conn, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(conn, rs);
	}
	
	/**
	 * Constructor for ErpRedeliveryPersistentBean.
	 * @param model
	 */
	public ErpRedeliveryPersistentBean(ErpRedeliveryModel model) {
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
		this.model = (ErpRedeliveryModel)m;	
	}
	
	/**
	 * Find ErpRedeliveryPersistentBean objects for a given parent.
	 * 
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpCreateOrderPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.REDELIVERY.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpRedeliveryPersistentBean bean = new ErpRedeliveryPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		ps.setString(4, EnumTransactionType.REDELIVERY.getCode());
		ps.setDouble(5, this.model.getAmount());
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
		
		// create delivery info
		ErpDeliveryInfoPersistentBean diPB = new ErpDeliveryInfoPersistentBean(this.model.getDeliveryInfo());
		diPB.setParentPK(this.getPK());
		diPB.create( conn );
		
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
		
		ErpDeliveryInfoPersistentBean diPB = new ErpDeliveryInfoPersistentBean();
		diPB.setParentPK(this.getPK());
		diPB.load(conn);
		this.model.setDeliveryInfo((ErpDeliveryInfoModel)diPB.getModel());
		
		this.unsetModified();
		
	}

}
