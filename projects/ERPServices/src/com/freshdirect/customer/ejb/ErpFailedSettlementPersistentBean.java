/*
 * Created on Mar 28, 2005
 *
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpPaymentModel;
import com.freshdirect.customer.ErpFailedSettlementModel;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author jng
 */
public class ErpFailedSettlementPersistentBean extends ErpSettlementPersistentBean {

	public ErpFailedSettlementPersistentBean() {
		super();
		this.model = new ErpFailedSettlementModel();
	}
	
    public ErpFailedSettlementPersistentBean(PrimaryKey pk){
    	this();
    	super.setPK(pk);
    }
	
	/** 
	 * Load constructor. 
	 */
	public ErpFailedSettlementPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
    	this();
		this.setPK(pk);
		load(conn);		
	}


	public ErpFailedSettlementPersistentBean(ErpFailedSettlementModel model) {
		this();
		this.setFromModel(model);
	}

	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID=? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.SETTLEMENT_FAILED.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpFailedSettlementPersistentBean bean = new ErpFailedSettlementPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		return EnumTransactionType.SETTLEMENT_FAILED.getCode();
	}
	
	protected ErpPaymentModel createModel() {
		return new ErpFailedSettlementModel();
	}
	
}
