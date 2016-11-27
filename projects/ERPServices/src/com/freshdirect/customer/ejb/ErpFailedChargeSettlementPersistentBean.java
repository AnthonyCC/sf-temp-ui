/*
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpFailedChargeSettlementModel;
import com.freshdirect.customer.ErpPaymentModel;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author jng
 *
 */
public class ErpFailedChargeSettlementPersistentBean extends ErpAbstractSettlementPersistentBean {
	
	public ErpFailedChargeSettlementPersistentBean() {
		super();
		this.model = new ErpFailedChargeSettlementModel();
	}
	
    public ErpFailedChargeSettlementPersistentBean(PrimaryKey pk){
		this();
    	super.setPK(pk);
    }
	
	/** 
	 * Load constructor. 
	 */
	public ErpFailedChargeSettlementPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}


	public ErpFailedChargeSettlementPersistentBean(ErpFailedChargeSettlementModel model) {
		this();
		this.setFromModel(model);
	}

	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID=? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.SETTLEMENT_CHARGE_FAILED.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpFailedChargeSettlementPersistentBean bean = new ErpFailedChargeSettlementPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		return EnumTransactionType.SETTLEMENT_CHARGE_FAILED.getCode();
	}
	
	protected ErpPaymentModel createModel() {
		return new ErpFailedChargeSettlementModel();
	}
	
}
