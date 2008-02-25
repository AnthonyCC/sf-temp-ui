/*
 * 
 * ErpReturnOrderPersistentBean.java
 * Date June 06/2002 06:23 PM
 */
package com.freshdirect.customer.ejb;

/**
 * 
 * @author knadeem
 * @version
 */
import java.sql.*;
import java.util.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.framework.collection.*;
import com.freshdirect.customer.*;

public class ErpReturnOrderPersistentBean extends ErpTransactionPersistentBean {
	
	private ErpReturnOrderModel model;
	
	/**
	 * Constructor for ErpReturnOrderPersistentBean.
	 */
	public ErpReturnOrderPersistentBean() {
		super();
		this.model = new ErpReturnOrderModel();
	}
	
	public ErpReturnOrderPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}

	/**
	 * Constructor for ErpReturnOrderPersistentBean.
	 * @param pk
	 * @param conn
	 * @throws SQLException
	 */
	public ErpReturnOrderPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Constructor for ErpReturnOrderPersistentBean.
	 * @param pk
	 * @param conn
	 * @param rs
	 * @throws SQLException
	 */
	public ErpReturnOrderPersistentBean(PrimaryKey pk, Connection conn, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(conn, rs);
	}

	/**
	 * Constructor for ErpReturnOrderPersistentBean.
	 * @param model
	 */
	public ErpReturnOrderPersistentBean(ErpReturnOrderModel model) {
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
	 * @return ErpAbstractOrderModel object.
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
		this.model = (ErpReturnOrderModel)m;
	}
	
	/**
	 * Find ErpReturnOrderPersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpCreateOrderPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID=? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.RETURN_ORDER.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpReturnOrderPersistentBean bean = new ErpReturnOrderPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		
		ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, TAX, SUB_TOTAL, CUSTOMER_ID) values (?,?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setTimestamp(3, new java.sql.Timestamp(this.model.getTransactionDate().getTime()));
		ps.setString(4, EnumTransactionType.RETURN_ORDER.getCode());
		ps.setDouble(5, this.model.getAmount());
		ps.setString(6, this.model.getTransactionSource().getCode());
		ps.setDouble(7, this.model.getTax());
		ps.setDouble(8, this.model.getSubTotal()); 
		ps.setString(9, this.model.getCustomerId());
		
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
		// create children
		ReturnLineList rlList = this.getReturnLinePBList();
		rlList.create(conn);
		
		ChargeList cList = this.getChargePBList();
		cList.create(conn);
				
		this.unsetModified();
		return this.getPK();
	}
	
	public void load(Connection conn) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, TAX, SUB_TOTAL FROM CUST.SALESACTION WHERE ID = ?");
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
		this.model.setAmount(rs.getDouble("AMOUNT"));
		this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
		this.model.setTax(rs.getDouble("TAX"));
		this.model.setSubTotal(rs.getDouble("SUB_TOTAL"));
		
		// load children
		ReturnLineList rlList = new ReturnLineList();
		rlList.setParentPK(this.getPK());
		rlList.load(conn);
		this.model.setInvoiceLines(rlList.getModelList());
		
		ChargeList cList = new ChargeList();
		cList.setParentPK(this.getPK());
		cList.load(conn);
		this.model.setCharges(cList.getModelList());
				
		this.unsetModified();
		
	}

	protected ReturnLineList getReturnLinePBList(){
		ReturnLineList lst = new ReturnLineList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getInvoiceLines().iterator(); i.hasNext(); ){
			lst.add(new ErpReturnLinePersistentBean((ErpReturnLineModel)i.next()));
		}
		return lst;
	}
	
	protected ChargeList getChargePBList() {
		ChargeList lst = new ChargeList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getCharges().iterator(); i.hasNext(); ){
			lst.add(new ErpChargeLinePersistentBean((ErpChargeLineModel)i.next()));
		}
		return lst;
	}
	
	private static class ReturnLineList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpReturnLinePersistentBean.findByParent(conn, (PrimaryKey) ReturnLineList.this.getParentPK()));
	    }
	}
	
	private static class ChargeList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpChargeLinePersistentBean.findByParent(conn, (PrimaryKey) ChargeList.this.getParentPK()));
		}
	}

}
