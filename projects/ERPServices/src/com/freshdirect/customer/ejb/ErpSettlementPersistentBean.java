/*
 * ErpSettlementPersistentBean.java
 *
 * Created on April 23, 2002, 3:00 PM
 */

package com.freshdirect.customer.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.sql.*;
import java.util.List;

import com.freshdirect.customer.*;
import com.freshdirect.framework.core.*;

public class ErpSettlementPersistentBean extends ErpAbstractSettlementPersistentBean {
	
	/** 
	 * Creates new ErpSettlementPersistentBean 
	 */
    public ErpSettlementPersistentBean() {
		super();
		this.model = new ErpSettlementModel();
    }
    
    public ErpSettlementPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** 
	 * Load constructor. 
	 */
	public ErpSettlementPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpSettlementModel to copy from
	 */
	public ErpSettlementPersistentBean(ErpSettlementModel model) {
		this();
		this.setFromModel(model);
	}
		
	/**
	 * Find ErpSettlementPersistentBean objects for a given parent.
	 * 
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpSettlementPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ? ");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.SETTLEMENT.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpSettlementPersistentBean bean = new ErpSettlementPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}
		
	protected String getTransactionType() {
		return EnumTransactionType.SETTLEMENT.getCode();
	}
	
	protected ErpPaymentModel createModel() {
		return new ErpSettlementModel();

	}	
}
