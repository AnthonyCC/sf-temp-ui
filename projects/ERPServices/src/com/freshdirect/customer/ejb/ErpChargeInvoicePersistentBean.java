/*
 * Created on Apr 5, 2005
 *
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;


import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAbstractInvoiceModel;
import com.freshdirect.customer.ErpChargeInvoiceModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author jng
 *
 */
public class ErpChargeInvoicePersistentBean extends ErpTransactionPersistentBean {
	
	protected ErpChargeInvoiceModel model;
	
	/**
	 * Constructor for ErpChargeInvoicePersistentBean.
	 */
	public ErpChargeInvoicePersistentBean() {
		super();
		this.model = new ErpChargeInvoiceModel();
	}
	
	public ErpChargeInvoicePersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}

	/**
	 * Constructor for ErpChargeInvoicePersistentBean.
	 * @param pk
	 * @param conn
	 * @throws SQLException
	 */
	public ErpChargeInvoicePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		this.load(conn);
	}

	/**
	 * Constructor for ErpChargeInvoicePersistentBean.
	 * @param pk
	 * @param conn
	 * @param rs
	 * @throws SQLException
	 */
	public ErpChargeInvoicePersistentBean(PrimaryKey pk, Connection conn, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(conn, rs);
	}

	/**
	 * Constructor for ErpChargeInvoicePersistentBean.
	 * @param model
	 */
	public ErpChargeInvoicePersistentBean(ErpChargeInvoiceModel model) {
		this();
		this.setFromModel(model);
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}
	
	public ModelI getModel() {
		return this.model.deepCopy();
	}
	
	public void setFromModel(ModelI m) {
		this.model = (ErpChargeInvoiceModel)m;
	}
	
	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, INVOICE_NUMBER, TAX, SUB_TOTAL, CUSTOMER_ID) values (?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setTimestamp(3, new java.sql.Timestamp(this.model.getTransactionDate().getTime()));
		ps.setString(4, this.getTransactionType().getCode());
		ps.setDouble(5, this.model.getAmount());
		ps.setString(6, this.model.getTransactionSource().getCode());
		ps.setString(7, this.model.getInvoiceNumber());
		ps.setDouble(8, this.model.getTax());
		ps.setDouble(9, this.model.getSubTotal()); 
		ps.setString(10, this.model.getCustomerId());
		
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
				
		ChargeList cList = this.getChargeLinePBList();
		cList.create(conn);
		
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, INVOICE_NUMBER, TAX, SUB_TOTAL FROM CUST.SALESACTION WHERE ID=?");
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
		this.model.setInvoiceNumber(rs.getString("INVOICE_NUMBER"));
		this.model.setTax(rs.getDouble("TAX"));
		this.model.setSubTotal(rs.getDouble("SUB_TOTAL"));
						
		ChargeList cList = new ChargeList();
		cList.setParentPK(this.getPK());
		cList.load(conn);
		this.model.setCharges(cList.getModelList());
		
		this.unsetModified();
		
	}
	

	/**
	 * Find ErpInvoicePersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpInvoicePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID=? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.INVOICE_CHARGE.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpChargeInvoicePersistentBean bean = new ErpChargeInvoicePersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}
	
	protected InvoiceLineList getInvoiceLinePBList(){
		InvoiceLineList lst = new InvoiceLineList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getInvoiceLines().iterator(); i.hasNext(); ){
			lst.add(new ErpInvoiceLinePersistentBean((ErpInvoiceLineModel)i.next()));
		}
		return lst;
	}
	
	protected ChargeList getChargeLinePBList(){
		ChargeList lst = new ChargeList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getCharges().iterator(); i.hasNext(); ){
			lst.add(new ErpChargeLinePersistentBean((ErpChargeLineModel)i.next()));
		}
		return lst;
	}
	
	/**
	 * Template method to create a concrete instance of ErpAbstractOrderModel.
	 */
	protected ErpAbstractInvoiceModel createModel(){
		return new ErpChargeInvoiceModel();
	}

	/**
	 * Template method to get the tx type.
	 */
	protected EnumTransactionType getTransactionType(){
		return EnumTransactionType.INVOICE_CHARGE;
	}
		
	private static class InvoiceLineList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpInvoiceLinePersistentBean.findByParent(conn, (PrimaryKey)InvoiceLineList.this.getParentPK()));
	    }
	}

	private static class ChargeList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpChargeLinePersistentBean.findByParent(conn, (PrimaryKey) ChargeList.this.getParentPK()));
		}
	}

}
