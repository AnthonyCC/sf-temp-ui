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
import com.freshdirect.payment.model.ErpSettlementTransactionModel;
import com.freshdirect.payment.model.ErpSummaryDetailModel;

public class SettlementSummaryPersistentBean extends DependentPersistentBeanSupport {
	
	private SummaryDetailList summaryDetails = new SummaryDetailList();
	private SettlementInvoiceList invoices = new SettlementInvoiceList();
	private SettlementTransactionList settlementTrxns = new SettlementTransactionList();
	
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
	
	private String affiliateAccountId = null;
	private long totalGrossCredit = 0;
	private long totalGrossDebit = 0;
	private long totalTransactionFeeCredit = 0;
	private long totalTransactionFeeDebit = 0;
	private String settlementSource = null;
	
	private String isLocked = "";
	private String status = "";
	
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
        
        this.isLocked = isLocked;
    }
    
    
    /** 
     * Creates new SettlementSummaryPersistentBean
     */
    public SettlementSummaryPersistentBean(PrimaryKey pk, Date processPeriodStart, Date processPeriodEnd, Date batchDate, String batchNumber, Date processDate, Date depositDate, 
    									   double netSalesAmount, int numberOfAdjustments, double adjustmentAmount, Date settlementFileDate, 
    									   String affiliateAccountId, long totalGrossCredit, long totalGrossDebit, long totalTransactionFeeCredit, long totalTransactionFeeDebit,
    									   String settlementSource) {
        this(pk, processPeriodStart, processPeriodEnd, batchDate, batchNumber, processDate, depositDate, 
				   netSalesAmount, numberOfAdjustments, adjustmentAmount, settlementFileDate);
        this.affiliateAccountId = affiliateAccountId;
        this.totalGrossCredit = totalGrossCredit;
        this.totalGrossDebit = totalGrossDebit;
        this.totalTransactionFeeCredit = totalTransactionFeeCredit;
        this.totalTransactionFeeDebit = totalTransactionFeeDebit;
        this.settlementSource = settlementSource;
    }
    
    /** 
     * Creates new SettlementSummaryPersistentBean
     */
    public SettlementSummaryPersistentBean(PrimaryKey pk, Date processPeriodStart, Date processPeriodEnd, Date batchDate, String batchNumber, Date processDate, Date depositDate, 
    									   double netSalesAmount, int numberOfAdjustments, double adjustmentAmount, Date settlementFileDate, 
    									   String affiliateAccountId, long totalGrossCredit, long totalGrossDebit, long totalTransactionFeeCredit, long totalTransactionFeeDebit,
    									   String settlementSource, String isLocked, String status) {
        this(pk, processPeriodStart, processPeriodEnd, batchDate, batchNumber, processDate, depositDate, 
				   netSalesAmount, numberOfAdjustments, adjustmentAmount, settlementFileDate, affiliateAccountId,
				   totalGrossCredit, totalGrossDebit, totalTransactionFeeCredit, totalTransactionFeeDebit,
				   settlementSource);
        this.isLocked = isLocked;
        this.status = status;
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
        
        //PayPal related fields
        this.affiliateAccountId = m.getAffiliateAccountId();
        this.totalGrossCredit = m.getTotalGrossCredit();
        this.totalGrossDebit = m.getTotalGrossDebit();
        this.totalTransactionFeeCredit = m.getTotalTransactionFeeCredit();
        this.totalTransactionFeeDebit = m.getTotalTransactionFeeDebit();
        this.settlementSource = m.getSettlementSource();
        
        this.isLocked = m.getIsLocked();
        this.status = m.getStatus();
        
        //PayPal related fields, which can be empty
        for(Iterator i = m.getSummaryDetails().iterator(); i.hasNext(); ){
        	SettlementDetailPersistentBean bean = new SettlementDetailPersistentBean((ErpSummaryDetailModel)i.next());
        	this.summaryDetails.add(bean);
        }
        
        for(Iterator i = m.getInvoices().iterator(); i.hasNext(); ){
        	SettlementInvoicePersistentBean bean = new SettlementInvoicePersistentBean((ErpSettlementInvoiceModel)i.next());
        	this.invoices.add(bean);
        }

        if (this.settlementSource != null && settlementSource.equals("PP")) {
            for(Iterator i = m.getSettlementTrxns().iterator(); i.hasNext(); ){
            	SettlementTransactionPersistentBean bean = new SettlementTransactionPersistentBean(
            														(ErpSettlementTransactionModel)i.next());
            	this.settlementTrxns.add(bean);
            }
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
        
        PreparedStatement ps = conn.prepareStatement("SELECT ID, PROCESS_PERIOD_START, PROCESS_PERIOD_END, BATCH_DATE, BATCH_NUMBER, PROCESS_DATE," +
        		" DEPOSIT_DATE, NET_SALES_AMOUNT, NUM_ADJUSTMENTS, ADJUSTMENT_AMOUNT, SETTLEMENT_FILE_DATE, " +
        		"AFFILIATE_ACCOUNT_ID, TOTAL_GROSS_CREDIT, TOTAL_GROSS_DEBIT, TOTAL_TRANSACTION_FEE_CREDIT, TOTAL_TRANSACTION_FEE_DEBIT, SETTLEMENT_SOURCE" +
        		" FROM CUST.SETTLEMENT WHERE ID = ?");
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
            
        	String affiliateAccountId = rs.getString("AFFILIATE_ACCOUNT_ID");
        	long totalGrossCredit = rs.getLong("TOTAL_GROSS_CREDIT");
        	long totalGrossDebit = rs.getLong("TOTAL_GROSS_DEBIT");
        	long totalTransactionFeeCredit = rs.getLong("TOTAL_TRANSACTION_FEE_CREDIT");
        	long totalTransactionFeeDebit = rs.getLong("TOTAL_TRANSACTION_FEE_DEBIT");
        	String settlementSource = rs.getString("SETTLEMENT_SOURCE");
            			
            bean =	new SettlementSummaryPersistentBean( new PrimaryKey(id), processPeriodStart, processPeriodEnd, batchDate, batchNumber, processDate, depositDate, netSalesAmount, numberOfAdjustments, adjustmentAmount, settlementFileDate,
            		affiliateAccountId, totalGrossCredit, totalGrossDebit, totalTransactionFeeCredit, totalTransactionFeeDebit,
					   settlementSource);
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
        PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SETTLEMENT (ID, PROCESS_PERIOD_START," +
        		" PROCESS_PERIOD_END, BATCH_DATE, BATCH_NUMBER, PROCESS_DATE, DEPOSIT_DATE, NET_SALES_AMOUNT," +
        		" NUM_ADJUSTMENTS, ADJUSTMENT_AMOUNT, SETTLEMENT_FILE_DATE," +
        		" AFFILIATE_ACCOUNT_ID, TOTAL_GROSS_CREDIT, TOTAL_GROSS_DEBIT, TOTAL_TRANS_FEE_CREDIT, " +
        		"TOTAL_TRANS_FEE_DEBIT, SETTLEMENT_SOURCE, CREATED_TIME_DATE, IS_LOCKED, ALL_RECORDS_PROCESSED)" +
        		" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );
        
        ps.setString(1, id);
        ps.setDate(2, new java.sql.Date(this.processPeriodStart.getTime()));
        ps.setDate(3, new java.sql.Date(this.processPeriodEnd.getTime()));
        ps.setDate(4, new java.sql.Date(this.batchDate.getTime()));
        ps.setString(5, this.batchNumber);
        ps.setDate(6, new java.sql.Date(this.processDate.getTime()));
        ps.setDate(7, new java.sql.Date(this.depositDate.getTime()));
        //ps.setDouble(8, this.netSalesAmount);
        ps.setBigDecimal(8, new java.math.BigDecimal(this.netSalesAmount));
        ps.setInt(9, this.numberOfAdjustments);
        ps.setBigDecimal(10, new java.math.BigDecimal(this.adjustmentAmount));
        ps.setDate(11, new java.sql.Date(this.settlementFileDate.getTime()));
        
        //PayPal related fields
        ps.setString(12, this.affiliateAccountId);
        ps.setLong(13, this.totalGrossCredit);
        ps.setLong(14, this.totalGrossDebit);
        ps.setLong(15, this.totalTransactionFeeCredit);
        ps.setLong(16, this.totalTransactionFeeDebit);
        ps.setString(17, this.settlementSource);
        ps.setTimestamp(18, new java.sql.Timestamp(new Date().getTime()));
        ps.setString(19, this.isLocked);
        ps.setString(20, this.status);
        
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
		
		if (this.settlementSource != null && settlementSource.equals("PP")) { //TODO change to appropriate Enum
			this.settlementTrxns.setParentPK(this.getPK());
			this.settlementTrxns.create(conn);
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
        PreparedStatement ps = conn.prepareStatement("SELECT ID, PROCESS_PERIOD_START, PROCESS_PERIOD_END, BATCH_DATE," +
        		" BATCH_NUMBER, PROCESS_DATE, DEPOSIT_DATE, NET_SALES_AMOUNT, NUM_ADJUSTMENTS, ADJUSTMENT_AMOUNT, SETTLEMENT_FILE_DATE " +
        		"AFFILIATE_ACCOUNT_ID, TOTAL_GROSS_CREDIT, TOTAL_GROSS_DEBIT, TOTAL_TRANSACTION_FEE_CREDIT, TOTAL_TRANSACTION_FEE_DEBIT, SETTLEMENT_SOURCE, " +
        		"ALL_RECORDS_PROCESSED" +
        		" FROM CUST.SETTLEMENT WHERE ID = ?");
        
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
            
        	this.affiliateAccountId = rs.getString("AFFILIATE_ACCOUNT_ID");
        	this.totalGrossCredit = rs.getLong("TOTAL_GROSS_CREDIT");
        	this.totalGrossDebit = rs.getLong("TOTAL_GROSS_DEBIT");
        	this.totalTransactionFeeCredit = rs.getLong("TOTAL_TRANSACTION_FEE_CREDIT");
        	this.totalTransactionFeeDebit = rs.getLong("TOTAL_TRANSACTION_FEE_DEBIT");
        	this.settlementSource = rs.getString("SETTLEMENT_SOURCE");
        	this.status = rs.getString("ALL_RECORDS_PROCESSED");
            
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
		
		if (settlementSource != null && settlementSource.equals("PP")) //TODO change to appropriate Enum
			settlementTrxns.load(conn);
        
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
	
	/**
	 * Inner class for the list of Settlement Invoices.
	 */
	private static class SettlementTransactionList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set( SettlementTransactionPersistentBean.findByParent(conn, SettlementTransactionList.this.getParentPK()) );
		}	
	}
}
