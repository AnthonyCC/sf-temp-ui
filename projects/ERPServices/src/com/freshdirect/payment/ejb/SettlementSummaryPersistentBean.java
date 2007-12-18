package com.freshdirect.payment.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.model.ErpSettlementInvoiceModel;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.model.ErpSummaryDetailModel;

public class SettlementSummaryPersistentBean extends DependentPersistentBeanSupport {
	
	private SummaryDetailList summaryDetails = new SummaryDetailList();
	private SettlementInvoiceList invoices = new SettlementInvoiceList();
	
	private Date processPeriodStart;
	private Date processPeriodEnd;
	private Date batchDate;
	private String batchNumber;
	private Date processDate;
	private Date depositDate;
	private double netSalesAmount;
	private int numberOfAdjustments;
	private double adjustmentAmount;
	private Date settlementFileDate;
	
	
	/** 
	 * Creates new SettlementSummaryPersistentBean
	 */
    public SettlementSummaryPersistentBean() {
        super();
    }
    
    /** 
     * Creates new SettlementSummaryPersistentBean
     */
    public SettlementSummaryPersistentBean(PrimaryKey pk, Date processPeriodStart, Date processPeriodEnd, Date batchDate, String batchNumber, Date processDate, Date depositDate, 
    									   double netSalesAmount, int numberOfAdjustments, double adjustmentAmount, Date settlementFileDate) {
        super(pk);
        this.processPeriodStart = processPeriodStart;
        this.processPeriodEnd = processPeriodEnd;
        this.batchDate = batchDate;
        this.batchNumber = batchNumber;
        this.processDate = processDate;
        this.depositDate = depositDate;
        this.netSalesAmount = netSalesAmount;
        this.numberOfAdjustments = numberOfAdjustments;
        this.adjustmentAmount = adjustmentAmount;
        this.settlementFileDate = settlementFileDate;
    }
    
    /**
     * Load constructor.
     */
    public SettlementSummaryPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
        this();
        this.setPK(pk);
        load(conn);
    }
    
    /**
     * Copy constructor, from model.
     *
     * @param ErpSettlementSummaryModel to copy from
     */
    public SettlementSummaryPersistentBean(ErpSettlementSummaryModel model) {
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
        ErpSettlementSummaryModel model = new ErpSettlementSummaryModel(this.processPeriodStart, this.processPeriodEnd, this.batchDate, this.batchNumber, this.processDate, this.depositDate, 
        	this.netSalesAmount, this.numberOfAdjustments, this.adjustmentAmount, this.settlementFileDate);
        model.setSummaryDetails(this.summaryDetails.getModelList());
        model.setInvoices(this.invoices.getModelList());
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
        ErpSettlementSummaryModel m = (ErpSettlementSummaryModel)model;
        this.processPeriodStart = m.getProcessPeriodStart();
        this.processPeriodEnd = m.getProcessPeriodEnd();
        this.batchDate = m.getBatchDate();
        this.batchNumber = m.getBatchNumber();
        this.processDate = m.getProcessDate();
        this.depositDate = m.getDepositDate();
        this.netSalesAmount = m.getNetSaleAmount();
        this.numberOfAdjustments = m.getNumberOfAdjustments();
        this.adjustmentAmount = m.getAdjustmentAmount();
        this.settlementFileDate = m.getSettlementFileDate();
        for(Iterator i = m.getSummaryDetails().iterator(); i.hasNext(); ){
        	SettlementDetailPersistentBean bean = new SettlementDetailPersistentBean((ErpSummaryDetailModel)i.next());
        	this.summaryDetails.add(bean);
        }
        
        for(Iterator i = m.getInvoices().iterator(); i.hasNext(); ){
        	SettlementInvoicePersistentBean bean = new SettlementInvoicePersistentBean((ErpSettlementInvoiceModel)i.next());
        	this.invoices.add(bean);
        }

    }
    
    
    /**
     * Find SettlementSummaryPersistentBean objects for a given parent.
     *
     * @param SQLConnection connection
     * @param parentPK primary key of parent
     * @return a List of SettlementDetailPersistentBean objects (empty if found none).
     * @throws SQLException if any problems occur talking to the database
     */
    public static SettlementSummaryPersistentBean findByPrimaryKey(Connection conn, PrimaryKey pk) throws SQLException {
        SettlementSummaryPersistentBean bean = null;
        
        PreparedStatement ps = conn.prepareStatement("SELECT ID, PROCESS_PERIOD_START, PROCESS_PERIOD_END, BATCH_DATE, BATCH_NUMBER, PROCESS_DATE, DEPOSIT_DATE, NET_SALES_AMOUNT, NUM_ADJUSTMENTS, ADJUSTMENT_AMOUNT, SETTLEMENT_FILE_DATE FROM CUST.SETTLEMENT WHERE ID = ?");
        ps.setString(1, pk.getId());
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String id = rs.getString("ID");
            Date processPeriodStart = rs.getDate("PROCESS_PERIOD_START");
            Date processPeriodEnd = rs.getDate("PROCESS_PERIOD_END");
            Date batchDate = rs.getDate("BATCH_DATE");
            String batchNumber = rs.getString("BATCH_NUMBER");
            Date processDate = rs.getDate("PROCESS_DATE");
            Date depositDate = rs.getDate("DEPOSIT_DATE");
            double netSalesAmount = rs.getDouble("NET_SALES_AMOUNT");
            int numberOfAdjustments = rs.getInt("NUM_ADJUSTMENTS");
            double adjustmentAmount = rs.getDouble("ADJUSTMENT_MOUNT");
            Date settlementFileDate = rs.getDate("SETTLEMENT_FILE_DATE");
            
			
            bean =	new SettlementSummaryPersistentBean( new PrimaryKey(id), processPeriodStart, processPeriodEnd, batchDate, batchNumber, processDate, depositDate, netSalesAmount, numberOfAdjustments, adjustmentAmount, settlementFileDate);
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        return bean;
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
        PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SETTLEMENT (ID, PROCESS_PERIOD_START, PROCESS_PERIOD_END, BATCH_DATE, BATCH_NUMBER, PROCESS_DATE, DEPOSIT_DATE, NET_SALES_AMOUNT, NUM_ADJUSTMENTS, ADJUSTMENT_AMOUNT, SETTLEMENT_FILE_DATE) values (?,?,?,?,?,?,?,?,?,?,?)" );
        
        ps.setString(1, id);
        ps.setDate(2, new java.sql.Date(this.processPeriodStart.getTime()));
        ps.setDate(3, new java.sql.Date(this.processPeriodEnd.getTime()));
        ps.setDate(4, new java.sql.Date(this.batchDate.getTime()));
        ps.setString(5, this.batchNumber);
        ps.setDate(6, new java.sql.Date(this.processDate.getTime()));
        ps.setDate(7, new java.sql.Date(this.depositDate.getTime()));
        ps.setDouble(8, this.netSalesAmount);
        ps.setInt(9, this.numberOfAdjustments);
        ps.setDouble(10, this.adjustmentAmount);
        ps.setDate(11, new java.sql.Date(this.settlementFileDate.getTime()));
        
        
        
	    try {
            ps.executeUpdate();
            this.setPK(new PrimaryKey(id));
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            ps.close();
            ps = null;
        }
        
        this.summaryDetails.setParentPK(this.getPK());
		this.summaryDetails.create( conn );
		
		this.invoices.setParentPK(this.getPK());
		this.invoices.create(conn);
		
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
        PreparedStatement ps = conn.prepareStatement("SELECT ID, PROCESS_PERIOD_START, PROCESS_PERIOD_END, BATCH_DATE, BATCH_NUMBER, PROCESS_DATE, DEPOSIT_DATE, NET_SALES_AMOUNT, NUM_ADJUSTMENTS, ADJUSTMENT_AMOUNT, SETTLEMENT_FILE_DATE FROM CUST.SETTLEMENT WHERE ID = ?");
        
        ps.setString(1, this.getPK().getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	this.processPeriodStart = rs.getDate("PROCESS_PERIOD_START");
        	this.processPeriodEnd = rs.getDate("PROCESS_PERIOD_END");
        	this.batchDate = rs.getDate("BATCH_DATE");
        	this.batchNumber = rs.getString("BATCH_NUMBER");
        	this.processDate = rs.getDate("PROCESS_DATE");
        	this.depositDate = rs.getDate("DEPOSIT_DATE");
        	this.netSalesAmount = rs.getDouble("NET_SALES_AMOUNT");
        	this.numberOfAdjustments = rs.getInt("NUM_ADJUSTMENTS");
        	this.adjustmentAmount = rs.getDouble("ADJUSTMENT_AMOUNT");
        	this.settlementFileDate = rs.getDate("SETTLEMENT_FILE_DATE");
            
        } else {
            throw new SQLException("No such SettlementSummaryBean PK: " + this.getPK() );
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        // load children
		summaryDetails.setParentPK(this.getPK());
		summaryDetails.load(conn);
		
		invoices.setParentPK(this.getPK());
		invoices.load(conn);
        
        this.unsetModified();
    }
	
	/**
     * Overriden isModified, always false, as this is a read-only object.
     */
	public final boolean isModified() {
		return false;
	}

	public final void store(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Object cannot be modified");
    }

	public final void remove(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Object cannot be modified");
	}
	
	
	
	
	/**
	 * Inner class for the list of detail summary records for this settlement summary.
	 */
	private static class SummaryDetailList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set( SettlementDetailPersistentBean.findByParent(conn, SummaryDetailList.this.getParentPK()) );
		}	
	}
	
	/**
	 * Inner class for the list of Settlement Invoices.
	 */
	private static class SettlementInvoiceList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set( SettlementInvoicePersistentBean.findByParent(conn, SettlementInvoiceList.this.getParentPK()) );
		}	
	}

}
