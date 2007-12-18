package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;

public class ActivityDAO implements java.io.Serializable {

	public void logActivity(Connection conn, ErpActivityRecord rec) throws SQLException {
		StringBuffer q = new StringBuffer();
		q.append(
			"INSERT INTO CUST.ACTIVITY_LOG (ACTIVITY_ID, CUSTOMER_ID, NOTE, TIMESTAMP, SOURCE, INITIATOR, DLV_PASS_ID, SALE_ID, REASON) VALUES (?,?,?,?,?,?,?,?,?)");

		PreparedStatement ps = conn.prepareStatement(q.toString());
		ps.setString(1, rec.getActivityType().getCode());
		ps.setString(2, rec.getCustomerId());
		ps.setString(3, rec.getNote());
		ps.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
		ps.setString(5, rec.getSource().getCode());
		ps.setString(6, rec.getInitiator());
		if(rec.getDeliveryPassId() != null){
			ps.setString(7, rec.getDeliveryPassId());
		}else{
			ps.setNull(7, Types.VARCHAR);
		}
		if(rec.getChangeOrderId() != null){
			ps.setString(8, rec.getChangeOrderId());
		}else{
			ps.setNull(8, Types.VARCHAR);
		}
		if(rec.getReason() != null){
			ps.setString(9, rec.getReason());
		}else{
			ps.setNull(9, Types.VARCHAR);
		}
		
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
		}
		return;
	}

	/**
	 * @return Collection of <code>ErpActivityRecord</code> objects
	 */
	public Collection getActivityByTemplate(Connection conn, ErpActivityRecord template) throws SQLException {
		CriteriaBuilder builder = new CriteriaBuilder();
		builder.addString("CUSTOMER_ID", template.getCustomerId());
		builder.addString("ACTIVITY_ID", template.getActivityType() == null ? null : template.getActivityType().getCode());
		builder.addString("SOURCE", template.getSource() == null ? null : template.getSource().getCode());
		builder.addString("INITIATOR", template.getInitiator());
		//Criteria specific to delivery pass changes.
		builder.addString("DLV_PASS_ID", template.getDeliveryPassId() == null ? null : template.getDeliveryPassId()); 
		builder.addString("SALE_ID", template.getChangeOrderId() == null ? null : template.getChangeOrderId());
		if (template.getDate() != null) {
			builder.addSql("TIMESTAMP > ?", new Object[] { new Timestamp(template.getDate().getTime())});
		}

		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.ACTIVITY_LOG WHERE " + builder.getCriteria());
		Object[] par = builder.getParams();
		for (int i = 0; i < par.length; i++) {
			ps.setObject(i + 1, par[i]);
		}
		ResultSet rs = ps.executeQuery();
		List l = new ArrayList();

		while (rs.next()) {
			l.add(this.loadFromResultSet(rs));
		}

		rs.close();
		ps.close();

		return l;
	}

	private ErpActivityRecord loadFromResultSet(ResultSet rs) throws SQLException {
		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setCustomerId(rs.getString("CUSTOMER_ID"));
		rec.setActivityType(EnumAccountActivityType.getActivityType(rs.getString("ACTIVITY_ID")));
		rec.setNote(rs.getString("NOTE"));
		rec.setDate(new java.util.Date(rs.getTimestamp("TIMESTAMP").getTime()));
		rec.setSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
		rec.setInitiator(rs.getString("INITIATOR"));
		//Columns specific to delivery pass changes.
		rec.setDeliveryPassId(rs.getString("DLV_PASS_ID"));
		rec.setChangeOrderId(rs.getString("SALE_ID"));
		rec.setReason(rs.getString("REASON"));
		return rec;
	}

}