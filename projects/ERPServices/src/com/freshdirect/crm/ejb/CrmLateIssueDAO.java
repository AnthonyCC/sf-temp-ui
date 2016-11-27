/*
 * Created on May 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.ObjectNotFoundException;

import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.StringUtil;


public class CrmLateIssueDAO {

	//private static Category LOGGER = LoggerFactory.getInstance(CrmLateIssueDAO.class);

	public static PrimaryKey findById(Connection conn, String id) throws SQLException, ObjectNotFoundException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.LATEISSUE WHERE ID = ?");
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new ObjectNotFoundException("Unable to find Late Issue  with Id " + id);
		}
		String IssueId = rs.getString("ID");
		rs.close();
		ps.close();
		return new PrimaryKey(IssueId);
	}
	
	public static CrmLateIssueModel getLateIssueModelById(Connection conn, String id) throws SQLException, ObjectNotFoundException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.LATEISSUE WHERE ID = ?");
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			 PrimaryKey pk = new PrimaryKey(rs.getString("id"));
			 CrmLateIssueModel lateIssue = new CrmLateIssueModel(pk);
			 populateModel(rs,lateIssue);
			 lateIssue.setStopsAndOrders(loadStopsAndOrders(conn,pk.getId()));
			 rs.close();
			 ps.close();
			return lateIssue;
		} else {
			ps.close();
			throw new ObjectNotFoundException("Unable to find Late Issue with Id " + id);
		}
		
	}

	
	public static Collection<CrmLateIssueModel> findByDate(Connection conn, Date dlvDate) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.LATEISSUE WHERE trunc(DELIVERY_DATE) = ? order by reported_at desc");
		ps.setDate(1, new java.sql.Date(dlvDate.getTime()));
		Collection<CrmLateIssueModel> issues= runQueryAndGather(conn,ps);
		ps.close();
		return issues.size()>0 ? issues : Collections.<CrmLateIssueModel>emptyList();
	}

	
	public static Collection<CrmLateIssueModel> findByRouteAndDate(Connection conn,String route,Date dlvDate) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.LATEISSUE WHERE DELIVERY_DATE = ? and route =? order by reported_at desc");
		ps.setDate(1, new java.sql.Date(dlvDate.getTime()));
		ps.setString(2,route);
		Collection<CrmLateIssueModel> issues= runQueryAndGather(conn,ps);
		ps.close();
		return issues.size()>0 ? issues : Collections.<CrmLateIssueModel>emptyList();
	}

	
	private static Collection<CrmLateIssueModel> runQueryAndGather(Connection conn,PreparedStatement ps) throws SQLException {
		List<CrmLateIssueModel> issues = new ArrayList<CrmLateIssueModel>();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			 PrimaryKey pk = new PrimaryKey(rs.getString("id"));
			 CrmLateIssueModel lateIssue = new CrmLateIssueModel(pk);
			 populateModel(rs,lateIssue);
			 lateIssue.setStopsAndOrders(loadStopsAndOrders(conn,pk.getId()));
			 issues.add(lateIssue);
		}
		rs.close();
		return issues;
	}
	
	private final static String getLateIssuesForOrderSql = "select * from cust.lateissue "
		+ " where id in " 
		+" (select lateissue_id  from cust.lateissue_orders where sale_id = ?)"
		+" order by reported_at desc";

	public static CrmLateIssueModel getRecentLateIssueForOrder(Connection conn,String orderId)throws SQLException {
		PreparedStatement ps = conn.prepareStatement(getLateIssuesForOrderSql);
		ps.setString(1, orderId);
		ResultSet rs = ps.executeQuery();
		CrmLateIssueModel lateIssue =null; 
		if (rs.next()) {
			 PrimaryKey pk = new PrimaryKey(rs.getString("id"));
			 lateIssue = new CrmLateIssueModel(pk);
			 populateModel(rs,lateIssue);
			 lateIssue.setStopsAndOrders(loadStopsAndOrders(conn,pk.getId()));
		}
		rs.close();
		ps.close();
		return lateIssue;
	}
	
	
	public static PrimaryKey createLateIssue(Connection conn,  ModelI model) throws SQLException {
		CrmLateIssueModel lateIssue = (CrmLateIssueModel) model;
		lateIssue.expandActualStops(false);
		String id = SequenceGenerator.getNextId(conn, "CUST");
		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO CUST.LATEISSUE(ID, ROUTE, STOPSTEXT, DELIVERY_DATE, AGENT_USER_ID, REPORTED_AT, REPORTED_BY,DELAY_MINUTES,DELIVERY_WINDOW,COMMENTS,ACTUAL_STOPSTEXT,ACTUAL_STOPSCOUNT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, lateIssue.getRoute());
		ps.setString(3,lateIssue.getReportedStopsText());
		ps.setDate(4, new java.sql.Date(lateIssue.getDeliveryDate().getTime())	);
		ps.setString(5,lateIssue.getAgentUserId());
		ps.setTimestamp(6, new java.sql.Timestamp(lateIssue.getReportedAt().getTime()));
		ps.setString(7, lateIssue.getReportedBy());
		ps.setInt(8, lateIssue.getDelayMinutes());
		ps.setString(9, lateIssue.getDelivery_window());
		ps.setString(10, lateIssue.getComments());
		ps.setString(11,lateIssue.getActualStopsText());
		ps.setInt(12,lateIssue.getActualStopCount());
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("LateIssue row not created for: Route:"+lateIssue.getRoute());
		}
		ps.close();
		//insert the stop order numbers
		lateIssue.setPK(new PrimaryKey(id));
		lateIssue.expandReportedStops(false);
		createLateIssueOrders(conn, lateIssue);
		return new PrimaryKey(id);
	}

	private static void createLateIssueOrders(Connection conn, CrmLateIssueModel lateIssue) throws SQLException {
		
		setSaleIdForStops(conn, lateIssue);
		
		for (Iterator itrEntries =lateIssue.getStopsAndOrders().entrySet().iterator(); itrEntries.hasNext(); ){
			Map.Entry mapEntry = (Map.Entry)itrEntries.next();
			PreparedStatement ps2 = conn.prepareStatement("insert into cust.lateissue_orders columns(lateissue_id,stop_number,sale_id) values(?,?,?)");
			ps2.setString(1,lateIssue.getPK().getId());
			ps2.setString(2,StringUtil.leftPad(""+(Integer)mapEntry.getKey(),5,'0'));
			ps2.setString(3,(String)mapEntry.getValue());
			if (ps2.executeUpdate()!=1) {
				ps2.close();
				throw new SQLException("LateIssue_Order row not created for: route: "+lateIssue.getRoute()+" Stop:"+mapEntry.getKey());
			}
		}
			
	}
	
	private final static String saleIdForStopsSql =  " select /*+ USE_NL (sa sale) */ sale_id,stop_sequence as stopNumber "
	 +" from  cust.salesaction sa ,cust.sale "  
	 +"where requested_date = ? "
	 +" and action_type in ('CRO','MOD') "
	 +" and action_date = (select max(action_date) from cust.salesaction "
	 +"       where action_type in ('CRO','MOD')"
	 +"		and sale_id = sa.sale_id) "
	 +" and sa.sale_id = sale.id   "
	 +" and sale.type ='REG' "
     +" 	and truck_number = ?";

		
	private static void setSaleIdForStops(Connection conn, CrmLateIssueModel lateIssue) throws SQLException {
		StringBuffer sqlStmt = new StringBuffer(saleIdForStopsSql);
		StringBuffer stopNumbers = new StringBuffer();
		if (lateIssue.getStopsAndOrders().size() > 0) {
			for (Iterator itr = lateIssue.getStopsAndOrders().keySet().iterator(); itr.hasNext();) {
				if (stopNumbers.length() > 1) stopNumbers.append(",");
				stopNumbers.append("'").append(StringUtil.leftPad(""+(Integer) itr.next(),5,'0')).append("'");
			}
			sqlStmt.append(" and stop_sequence in (").append(stopNumbers).append(")"); 
			PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
			ps.setDate(1,new java.sql.Date(lateIssue.getDeliveryDate().getTime()));
			ps.setString(2, StringUtil.leftPad(lateIssue.getRoute(),6,'0'));
			ResultSet rs = ps.executeQuery();
			 
			while (rs.next()){
				String saleId = rs.getString("sale_id");
				int stopNumber = Integer.parseInt(rs.getString("stopNumber"));
				lateIssue.setStopAndOrder(new Integer(stopNumber),saleId);
			}
			rs.close();
			ps.close();
		}
	}
	

	public static void updateLateIssue(Connection conn, ModelI model) throws SQLException {
		CrmLateIssueModel lateIssue = (CrmLateIssueModel) model;
		lateIssue.expandActualStops(false);
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.LATEISSUE SET ROUTE=?,StopsText=?, DELIVERY_DATE=?, AGENT_USER_ID=?, REPORTED_AT=?, REPORTED_BY=?,DELAY_MINUTES=?,DELIVERY_WINDOW=?,COMMENTS=? , ACTUAL_STOPSTEXT=?,ACTUAL_STOPSCOUNT=? WHERE ID=? ");
		ps.setString(1, lateIssue.getRoute());
		ps.setString(2,lateIssue.getReportedStopsText());
		ps.setDate(3, new java.sql.Date(lateIssue.getDeliveryDate().getTime())	);
		ps.setString(4,lateIssue.getAgentUserId());
		ps.setTimestamp(5, new java.sql.Timestamp(lateIssue.getReportedAt().getTime()));
		ps.setString(6, lateIssue.getReportedBy());
		ps.setInt(7, lateIssue.getDelayMinutes());
		ps.setString(8, lateIssue.getDelivery_window());
		ps.setString(9,lateIssue.getComments());
		ps.setString(10,lateIssue.getActualStopsText());
		ps.setInt(11,lateIssue.getActualStopCount());
		ps.setString(12, lateIssue.getPK().getId());

		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("LateIssue row not updated for: ID:"+lateIssue.getPK().getId());
		}
		ps.close();
		lateIssue.expandReportedStops(false);
		removeLateIssueOrders(conn,lateIssue.getPK().getId());
		if (!lateIssue.getStopsAndOrders().equals(Collections.EMPTY_MAP)) {
			createLateIssueOrders(conn, lateIssue);
		}
	}

	private static  TreeMap loadStopsAndOrders(Connection conn, String lateIssueId) throws SQLException {
		TreeMap stopsAndOrders = new TreeMap();
		PreparedStatement ps = conn.prepareStatement("select stop_number,sale_id from CUST.LATEISSUE_ORDERS WHERE LATEISSUE_ID = ? order by stop_number");
		ps.setString(1,lateIssueId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			stopsAndOrders.put(new Integer(rs.getString("stop_number")), rs.getString("sale_id"));
		}
		rs.close();
		ps.close();
		return stopsAndOrders;
	}
	
	
	private static CrmLateIssueModel populateModel(ResultSet rs, CrmLateIssueModel lateIssue) throws SQLException  {
		lateIssue.setRoute(rs.getString("Route"));
		lateIssue.setDeliveryDate(rs.getDate("delivery_date"));
		lateIssue.setReportedStopsText(rs.getString("stopsText"));
		lateIssue.setAgentUserId(rs.getString("agent_user_id"));
		lateIssue.setReportedAt(rs.getTimestamp("reported_at"));
		lateIssue.setReportedBy(rs.getString("reported_by"));
		lateIssue.setDelayMinutes(rs.getInt("delay_minutes"));
		lateIssue.setDelivery_window(rs.getString("delivery_window"));
		lateIssue.setComments(rs.getString("comments"));
		lateIssue.setActualStopsText(rs.getString("Actual_stopSText"));
		lateIssue.setActualStopCount(rs.getInt("Actual_StopSCount"));
		return lateIssue;
	}
	
	private static void removeLateIssueOrders(Connection conn, String id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.LATEISSUE_ORDERS WHERE LATEISSUE_ID=?");
		ps.setString(1,id);
		ps.executeUpdate();
		ps.close();
	}
	
}
