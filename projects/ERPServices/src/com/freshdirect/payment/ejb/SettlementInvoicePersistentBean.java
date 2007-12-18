package com.freshdirect.payment.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.model.ErpSettlementInvoiceModel;

public class SettlementInvoicePersistentBean extends DependentPersistentBeanSupport {
	
	private Date invoiceDate;
	private double invoiceAmount;
	private String invoiceNumber;
	private String description;
	
	/** 
	 * Creates new SettlementInvoicePersistentBean
	 */
    public SettlementInvoicePersistentBean() {
        super();
    }
    
    /** 
     * Creates new SettlementInvoicePersistentBean
     */
    public SettlementInvoicePersistentBean(PrimaryKey pk, Date invoiceDate, double invoiceAmount, String invoiceNumber, String description) {
        super(pk);
        this.invoiceDate = invoiceDate;
        this.invoiceAmount = invoiceAmount;
        this.invoiceNumber = invoiceNumber;
        this.description = description;
    }
    
    /**
     * Load constructor.
     */
    public SettlementInvoicePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
        this();
        this.setPK(pk);
        load(conn);
    }
    
    /**
     * Copy constructor, from model.
     *
     * @param ErpSummaryDetailModel to copy from
     */
    public SettlementInvoicePersistentBean(ErpSettlementInvoiceModel model) {
        super();
        this.setFromModel(model);
    }
    
    /** 
     * Get model representing the state of this object
     * 
     * @throws RemoteException in case there is any problem
     * @return Model representing state of this object
     */
    public ModelI getModel(){
        ErpSettlementInvoiceModel model = new ErpSettlementInvoiceModel(this.invoiceDate, this.invoiceAmount, this.invoiceNumber, this.description);
        super.decorateModel(model);
        return model;
    }
    
    /** 
     * set properties of this object from this model
     * 
     * @param model the model to copy
     * @throws RemoteException in case any problem in accessing remote object
     */
    public void setFromModel(ModelI model){
        ErpSettlementInvoiceModel m = (ErpSettlementInvoiceModel)model;
        this.invoiceDate = m.getInvoiceDate();
        this.invoiceAmount = m.getInvoiceAmount();
        this.invoiceNumber = m.getInvoiceNumber();
        this.description = m.getDescription();
        
    }
    
    /**
     * Find SettlementInvoicePersistentBean objects for a given parent.
     *
     * @param SQLConnection connection
     * @param parentPK primary key of parent
     * @return a List of SettlementInvoicePersistentBean objects (empty if found none).
     * @throws SQLException if any problems occur talking to the database
     */
    public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
        java.util.List beans = new java.util.LinkedList();
        PreparedStatement ps = conn.prepareStatement("SELECT ID, SETTLEMENT_ID, INVOICE_DATE, INVOICE_AMOUNT, INVOICE_NUMBER, DESCRIPTION FROM CUST.SETTLEMENT_INVOICE WHERE SETTLEMENT_ID = ?");
        ps.setString(1, parentPK.getId());
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String id = rs.getString("ID");
            Date invoiceDate = rs.getDate("INVOICE_DATE");
            double invoiceAmount = rs.getDouble("INVOICE_AMOUNT");
            String invoiceNumber = rs.getString("INVOICE_NUMBER");
            String description = rs.getString("DESCRIPTION");
            SettlementInvoicePersistentBean bean =	new SettlementInvoicePersistentBean( new PrimaryKey(id), invoiceDate, invoiceAmount, invoiceNumber, description);
            bean.setParentPK(parentPK);
            beans.add(bean);
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        return beans;
    }
    
    /** 
     * Writes a new object to the persistent store for the first time
     * 
     * @param conn a SQLConnection used to write this object
     * @throws SQLException if any problems encountered while writing this object
     * @return the unique identity of this new object
     */
    public PrimaryKey create(Connection conn) throws SQLException {
    	String id = this.getNextId(conn, "CUST");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SETTLEMENT_INVOICE (ID, SETTLEMENT_ID, INVOICE_DATE, INVOICE_AMOUNT, INVOICE_NUMBER, DESCRIPTION) values (?,?,?,?,?,?)" );
        
        ps.setString(1, id);
        ps.setString(2, this.getParentPK().getId());
        ps.setDate(3, new java.sql.Date(this.invoiceDate.getTime()));
        ps.setDouble(4, this.invoiceAmount);
        ps.setString(5, this.invoiceNumber);
        ps.setString(6, this.description);
        
	    try {
            ps.executeUpdate();
            this.setPK(new PrimaryKey(id));
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            ps.close();
            ps = null;
        }
        this.unsetModified();
        return this.getPK();
    }
    
    /** 
     * Load this object from persistent store
     * 
     * @param conn a SQLConnection to used to load this object
     * @throws SQLException any problems entountered while loading this object
     */
    
    public void load(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT INVOICE_DATE, INVOICE_AMOUNT, INVOICE_NUMBER, DESCRIPTION FROM CUST.SETTLEMENT_INVOICE WHERE ID = ?");
        
        ps.setString(1, this.getPK().getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	this.invoiceDate = rs.getDate("INVOICE_DATE");
        	this.invoiceAmount = rs.getDouble("INVOICE_AMOUNT");
        	this.invoiceNumber = rs.getString("INVOICE_NUMBER");
        	this.description = rs.getString("DESCRIPTION");
            
        } else {
            throw new SQLException("No such SettlementSummaryBean PK: " + this.getPK() );
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        this.unsetModified();
    }
    
    public final void store(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Object cannot be modified");
    }

	public final void remove(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Object cannot be modified");
	}
	
	/**
     * Overriden isModified, always false, as this is a read-only object.
     */
	public final boolean isModified() {
		return false;
	}

}
