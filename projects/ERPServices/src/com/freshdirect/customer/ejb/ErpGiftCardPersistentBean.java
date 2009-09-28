package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.giftcard.ErpGiftCardTransModel;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpPayment persistent bean.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public abstract class ErpGiftCardPersistentBean extends ErpTransactionPersistentBean {

	
	public ErpGiftCardPersistentBean() {
		super();
	}
	
	public PrimaryKey create(Connection conn, ErpGiftCardTransModel model) throws SQLException {		
		String id = this.getNextId(conn, "CUST");
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_TYPE, ACTION_DATE, SOURCE, AMOUNT, TAX, CUSTOMER_ID) values (?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setString(3, model.getTransactionType().getCode());
		ps.setTimestamp(4, new java.sql.Timestamp(model.getTransactionDate().getTime()));
		if(model.getTransactionSource()!=null)
		   ps.setString(5, model.getTransactionSource().getCode());
		else
			ps.setNull(5, Types.NULL);
		ps.setDouble(6, model.getAmount());
		ps.setDouble(7, model.getTax());
		ps.setString(8, model.getCustomerId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}
		this.setPK(new PrimaryKey(id));
        ps.close();        
		return this.getPK();
	}

	public void load(Connection conn, ErpGiftCardTransModel model) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, SOURCE, AMOUNT, TAX FROM CUST.SALESACTION WHERE ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
			model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
			model.setAmount(rs.getDouble("AMOUNT"));
			model.setTax(rs.getDouble("TAX"));
		} else {
			throw new SQLException("No such ErpPayment PK: " + this.getPK());
		}		
		rs.close();
		ps.close();
	}		
	
}
