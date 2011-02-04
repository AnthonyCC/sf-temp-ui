package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;

public class ActivityDAO implements java.io.Serializable {
	
	private static final long serialVersionUID = 2593574875770125711L;

	private static final String INSERT = "INSERT INTO CUST.ACTIVITY_LOG (ACTIVITY_ID, CUSTOMER_ID, NOTE, TIMESTAMP, SOURCE, INITIATOR, DLV_PASS_ID, SALE_ID, REASON, STANDINGORDER_ID, MASQUERADE_AGENT) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
	public void logActivity( Connection conn, ErpActivityRecord rec ) throws SQLException {

		PreparedStatement ps = conn.prepareStatement( INSERT );
		ps.setString( 1, rec.getActivityType().getCode() );
		ps.setString( 2, rec.getCustomerId() );
		ps.setString( 3, rec.getNote() );
		ps.setTimestamp( 4, new java.sql.Timestamp( new java.util.Date().getTime() ) );
		ps.setString( 5, rec.getSource().getCode() );
		ps.setString( 6, rec.getInitiator() );
		if ( rec.getDeliveryPassId() != null ) {
			ps.setString( 7, rec.getDeliveryPassId() );
		} else {
			ps.setNull( 7, Types.VARCHAR );
		}
		if ( rec.getChangeOrderId() != null ) {
			ps.setString( 8, rec.getChangeOrderId() );
		} else {
			ps.setNull( 8, Types.VARCHAR );
		}
		if ( rec.getReason() != null ) {
			ps.setString( 9, rec.getReason() );
		} else {
			ps.setNull( 9, Types.VARCHAR );
		}
		if ( rec.getStandingOrderId() != null ) {
			ps.setString( 10, rec.getStandingOrderId() );
		} else {
			ps.setNull( 10, Types.VARCHAR );
		}
		if ( rec.getMasqueradeAgent() != null ) {
			ps.setString( 11, rec.getMasqueradeAgent() );
		} else {
			ps.setNull( 11, Types.VARCHAR );
		}

		try {
			if ( ps.executeUpdate() != 1 ) {
				throw new SQLException( "Row not created" );
			}
		} catch ( SQLException sqle ) {
			throw sqle;
		} finally {
			ps.close();
		}
		return;
	}

	/**
	 * @return Collection of <code>ErpActivityRecord</code> objects
	 */
	public Collection<ErpActivityRecord> getActivityByTemplate(Connection conn, ErpActivityRecord template) throws SQLException {
		
		CriteriaBuilder builder = new CriteriaBuilder();
		builder.addString("CUSTOMER_ID", template.getCustomerId());
//		builder.addString("ACTIVITY_ID", template.getActivityType() == null ? null : template.getActivityType().getCode());
		boolean isCCActivity = EnumAccountActivityType.VIEW_CC.equals(template.getActivityType()) || EnumAccountActivityType.VIEW_ECHECK.equals(template.getActivityType());
		if(isCCActivity){
			builder.addInString("ACTIVITY_ID", new String[]{EnumAccountActivityType.VIEW_CC.getCode(),EnumAccountActivityType.VIEW_ECHECK.getCode()});
		}else{
			builder.addString("ACTIVITY_ID", template.getActivityType() == null ? null : template.getActivityType().getCode());
		}
		builder.addString("SOURCE", template.getSource() == null ? null : template.getSource().getCode());
		builder.addString("INITIATOR", template.getInitiator());
		//Criteria specific to delivery pass changes.
		builder.addString("DLV_PASS_ID", template.getDeliveryPassId() == null ? null : template.getDeliveryPassId()); 
		builder.addString("SALE_ID", template.getChangeOrderId() == null ? null : template.getChangeOrderId());
		if (template.getDate() != null) {
			builder.addSql("TIMESTAMP > ?", new Object[] { new Timestamp(template.getDate().getTime())});
		}else{
			if (template.getFromDate() != null) {
				builder.addSql("TIMESTAMP > ?", new Object[] { new Timestamp(template.getFromDate().getTime())});
			}
			if (template.getToDate() != null) {
				builder.addSql("TIMESTAMP < ?", new Object[] { new Timestamp(template.getToDate().getTime())});
			}
		}
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.ACTIVITY_LOG WHERE " + builder.getCriteria());
		Object[] par = builder.getParams();
		for (int i = 0; i < par.length; i++) {
			ps.setObject(i + 1, par[i]);
		}
		ResultSet rs = ps.executeQuery();
		List<ErpActivityRecord> l = new ArrayList<ErpActivityRecord>();

		while (rs.next()) {
			l.add(this.loadFromResultSet(rs));
		}

		rs.close();
		ps.close();

		return l;
	}
	
	/**
	 * @return Collection of <code>ErpActivityRecord</code> objects
	 */
	public Collection<ErpActivityRecord> getCCActivitiesByTemplate(Connection conn, ErpActivityRecord template) throws SQLException {
		
		CriteriaBuilder builder = new CriteriaBuilder();
		builder.addString("AL.CUSTOMER_ID", template.getCustomerId());
//		builder.addString("ACTIVITY_ID", template.getActivityType() == null ? null : template.getActivityType().getCode());
		boolean isCCActivity = EnumAccountActivityType.VIEW_CC.equals(template.getActivityType()) || EnumAccountActivityType.VIEW_ECHECK.equals(template.getActivityType());
		if(isCCActivity){
			builder.addInString("ACTIVITY_ID", new String[]{EnumAccountActivityType.VIEW_CC.getCode(),EnumAccountActivityType.VIEW_ECHECK.getCode()});
		}else{
			builder.addString("ACTIVITY_ID", template.getActivityType() == null ? null : template.getActivityType().getCode());
		}
		builder.addString("SOURCE", template.getSource() == null ? null : template.getSource().getCode());
		builder.addString("INITIATOR", template.getInitiator());
		//Criteria specific to delivery pass changes.
		builder.addString("DLV_PASS_ID", template.getDeliveryPassId() == null ? null : template.getDeliveryPassId()); 
		builder.addString("SALE_ID", template.getChangeOrderId() == null ? null : template.getChangeOrderId());
		/*if (template.getDate() != null) {
			builder.addSql("TIMESTAMP > ?", new Object[] { new Timestamp(template.getDate().getTime())});
		}*/
		if (template.getFromDate() != null) {
			builder.addSql("TIMESTAMP > ?", new Object[] { new Timestamp(template.getFromDate().getTime())});
		}
		if (template.getToDate() != null) {
			builder.addSql("TIMESTAMP < ?", new Object[] { new Timestamp(template.getToDate().getTime())});
		}
		PreparedStatement ps = conn.prepareStatement("SELECT AL.*,CI.FIRST_NAME,CI.LAST_NAME FROM CUST.ACTIVITY_LOG AL, CUST.CUSTOMERINFO CI WHERE CI.CUSTOMER_ID= AL.CUSTOMER_ID AND " + builder.getCriteria());
		Object[] par = builder.getParams();
		for (int i = 0; i < par.length; i++) {
			ps.setObject(i + 1, par[i]);
		}
		ResultSet rs = ps.executeQuery();
		List<ErpActivityRecord> l = new ArrayList<ErpActivityRecord>();

		while (rs.next()) {
			l.add(this.loadFromResultSet2(rs));
		}

		rs.close();
		ps.close();

		return l;
	}

	private ErpActivityRecord loadFromResultSet( ResultSet rs ) throws SQLException {
		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setCustomerId( rs.getString( "CUSTOMER_ID" ) );
		rec.setActivityType( EnumAccountActivityType.getActivityType( rs.getString( "ACTIVITY_ID" ) ) );
		rec.setNote( rs.getString( "NOTE" ) );
		rec.setDate( new java.util.Date( rs.getTimestamp( "TIMESTAMP" ).getTime() ) );
		rec.setSource( EnumTransactionSource.getTransactionSource( rs.getString( "SOURCE" ) ) );
		rec.setInitiator( rs.getString( "INITIATOR" ) );
		// Columns specific to delivery pass changes.
		rec.setDeliveryPassId( rs.getString( "DLV_PASS_ID" ) );
		rec.setChangeOrderId( rs.getString( "SALE_ID" ) );
		rec.setReason( rs.getString( "REASON" ) );
		rec.setStandingOrderId( rs.getString( "STANDINGORDER_ID" ) );
		rec.setMasqueradeAgent( rs.getString( "MASQUERADE_AGENT" ) );		
		return rec;
	}
	
	private ErpActivityRecord loadFromResultSet2( ResultSet rs ) throws SQLException {
		ErpActivityRecord rec = loadFromResultSet(rs);
		rec.setCustFirstName(rs.getString( "FIRST_NAME" ));
		rec.setCustLastName(rs.getString( "LAST_NAME" ));
		return rec;
	}
	public Map<String, List> getFilterLists(Connection conn,ErpActivityRecord template) throws SQLException{
		CriteriaBuilder builder = new CriteriaBuilder();
		builder.addString("CUSTOMER_ID", template.getCustomerId());
		List<EnumAccountActivityType> activityTypeList = new ArrayList<EnumAccountActivityType>();
		List<EnumTransactionSource> sourceList = new ArrayList<EnumTransactionSource>();
		List<String> initiatorList = new ArrayList<String>();
		String columnName ="";
		List list = null;
		Map<String, List> map = new HashMap<String, List>();
		columnName="activity_id";
		map.put(columnName, getUniqueList(conn, builder, columnName));
		columnName="source";
		map.put(columnName, getUniqueList(conn, builder, columnName));
		columnName="initiator";
		map.put(columnName, getUniqueList(conn, builder, columnName));
		return map;
	}

	private List getUniqueList(Connection conn,
			CriteriaBuilder builder,String columnName) throws SQLException {
		List list = new ArrayList();
		PreparedStatement ps = conn.prepareStatement("select distinct("+columnName+") from cust.activity_log  WHERE " + builder.getCriteria()+" order by "+columnName);
		Object[] par = builder.getParams();
		for (int i = 0; i < par.length; i++) {
			ps.setObject(i + 1, par[i]);
		}
		ResultSet rs = ps.executeQuery();
		
		if("activity_id".equalsIgnoreCase(columnName)){
			while(rs.next()){
				list.add(EnumAccountActivityType.getActivityType(rs.getString("activity_id")));
			}
		}else if("source".equalsIgnoreCase(columnName)){
			while(rs.next()){
				list.add(EnumTransactionSource.getTransactionSource(rs.getString("source")));
			}
		}else{
			while(rs.next()){
				list.add(rs.getString("initiator"));
			}
		}
		
		rs.close();
		ps.close();
		return list;
	}

}