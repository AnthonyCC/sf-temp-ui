package com.freshdirect.payment.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.model.EnumSummaryDetailType;
import com.freshdirect.payment.model.ErpSummaryDetailModel;


public class SettlementDetailPersistentBean extends DependentPersistentBeanSupport {
	
	private EnumSummaryDetailType summaryType = null;
	private int numberOfItems;
	private double netAmount;
	private double interchangeFees;
	private double assessmentFees;
	private double transactionFees;
	
	/** 
	 * Creates new SettlementDetailPersistentBean
	 */
    public SettlementDetailPersistentBean() {
        super();
    }
    
    /** 
     * Creates new SettlementDetailPersistentBean
     */
    public SettlementDetailPersistentBean(PrimaryKey pk, EnumSummaryDetailType summaryType, int numberOfItems, double netAmount, double interchangeFees, double assessmentFees, double transactionFees) {
        super(pk);
        this.summaryType = summaryType;
        this.numberOfItems = numberOfItems;
        this.netAmount = netAmount;
        this.interchangeFees = interchangeFees;
        this.assessmentFees = assessmentFees;
        this.transactionFees = transactionFees;
    }
    
    /**
     * Load constructor.
     */
    public SettlementDetailPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
        this();
        this.setPK(pk);
        load(conn);
    }
    
    /**
     * Copy constructor, from model.
     *
     * @param ErpSummaryDetailModel to copy from
     */
    public SettlementDetailPersistentBean(ErpSummaryDetailModel model) {
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
        ErpSummaryDetailModel model = new ErpSummaryDetailModel(this.summaryType, this.numberOfItems, this.netAmount, this.interchangeFees, this.assessmentFees, this.transactionFees);
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
        ErpSummaryDetailModel m = (ErpSummaryDetailModel)model;
        this.summaryType = m.getSummaryDatailType();
        this.numberOfItems = m.getNumberOfItems();
        this.netAmount = m.getNetAmount();
        this.interchangeFees = m.getInterchangeFees();
        this.assessmentFees = m.getAssessmentFees();
        this.transactionFees = m.getTransactionFees();
    }
    
    
    /**
     * Find SettlementDetailPersistentBean objects for a given parent.
     *
     * @param SQLConnection connection
     * @param parentPK primary key of parent
     * @return a List of SettlementDetailPersistentBean objects (empty if found none).
     * @throws SQLException if any problems occur talking to the database
     */
    public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
        java.util.List beans = new java.util.LinkedList();
        PreparedStatement ps = conn.prepareStatement("SELECT ID, SETTLEMENT_ID, CARD_TYPE, NUM_ITEMS, NET_AMOUNT, INTERCHANGE_FEES, ASSESSMENT_FEES, TRANSACTION_FEES FROM CUST.SETTLEMENT_DETAIL WHERE SETTLEMENT_ID = ?");
        ps.setString(1, parentPK.getId());
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String id = rs.getString("ID");
            EnumSummaryDetailType summaryType = EnumSummaryDetailType.getSummaryDetail(rs.getString("CARD_TYPE"));
			int numberOfItems = rs.getInt("NUM_ITEMS");
			double netAmount = rs.getDouble("NET_AMOUNT");
			double interchangeFees = rs.getDouble("INTERCHANGE_FEES");
			double assessmentFees = rs.getDouble("ASSESSMENT_FEES");
			double transactionFees = rs.getDouble("TRANSACTION_FEES");
            SettlementDetailPersistentBean bean =	new SettlementDetailPersistentBean( new PrimaryKey(id),  summaryType, numberOfItems, netAmount, interchangeFees, assessmentFees, transactionFees);
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
        PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SETTLEMENT_DETAIL (ID, SETTLEMENT_ID, CARD_TYPE, NUM_ITEMS, NET_AMOUNT, INTERCHANGE_FEES, ASSESSMENT_FEES, TRANSACTION_FEES) values (?,?,?,?,?,?,?,?)" );
        
        ps.setString(1, id);
        ps.setString(2, this.getParentPK().getId());
        ps.setString(3, this.summaryType.getCode());
        ps.setInt(4, this.numberOfItems);
        ps.setDouble(5, this.netAmount);
        ps.setDouble(6, this.interchangeFees);
        ps.setDouble(7, this.assessmentFees);
        ps.setDouble(8, this.transactionFees);
        
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
        PreparedStatement ps = conn.prepareStatement("SELECT CARD_TYPE, NUM_ITEMS, NET_AMOUNT, INTERCHANGE_FEES, ASSESSMENT_FEES, TRANSACTION_FEES FROM CUST.SETTLEMENT_DETAIL WHERE ID = ?");
        
        ps.setString(1, this.getPK().getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	
            this.summaryType = EnumSummaryDetailType.getSummaryDetail(rs.getString("CARD_TYPE"));
            this.numberOfItems = rs.getInt("NUM_ITEMS");
            this.netAmount = rs.getDouble("NET_AMOUNT");
            this.interchangeFees = rs.getDouble("INTERCHANGE_FEES");
            this.assessmentFees = rs.getDouble("ASSESSMENT_FEES");
            this.transactionFees = rs.getDouble("TRANSACTION_FEES");
            
        } else {
            throw new SQLException("No such SettlementSummaryBean PK: " + this.getPK() );
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
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

}
