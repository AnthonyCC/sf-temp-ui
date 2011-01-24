/**
 * @author ekracoff
 * Created on Mar 29, 2005*/

package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.freshdirect.crm.CrmStatus;
import com.freshdirect.framework.core.PrimaryKey;


public class CrmStatusDAO {
	
	private final static String INSERT = "insert into cust.CRM_SESSION_STATUS (agent_id, sale_id, case_id, customer_id) values(?,?,?,?)";
	public void create(Connection conn, CrmStatus status) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(INSERT);
		ps.setString(1, status.getAgentId());//status.getAgentPK().getId());
		ps.setString(2, status.getSaleId());
		ps.setString(3, status.getCaseId());
		ps.setString(4, status.getErpCustomerId());
		ps.execute();
	}
	
	private final static String RETRIEVE = "select agent_id, sale_id, case_id, customer_id from cust.CRM_SESSION_STATUS where agent_id = ?";
	public CrmStatus retrieve(Connection conn, PrimaryKey agentPK) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(RETRIEVE);
		ps.setString(1, agentPK.getId());
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			CrmStatus status = new CrmStatus(new PrimaryKey(rs.getString("AGENT_ID")));
			status.setSaleId(rs.getString("SALE_ID"));
			status.setCaseId(rs.getString("CASE_ID"));
			status.setErpCustomerId(rs.getString("CUSTOMER_ID"));
			return status;
		}
		
		return null;
	}
	
	public CrmStatus retrieve(Connection conn, String agentId) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(RETRIEVE);
		ps.setString(1, agentId);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			CrmStatus status = new CrmStatus(rs.getString("AGENT_ID"));
			status.setSaleId(rs.getString("SALE_ID"));
			status.setCaseId(rs.getString("CASE_ID"));
			status.setErpCustomerId(rs.getString("CUSTOMER_ID"));
			return status;
		}
		
		return null;
	}
	
	private final static String UPDATE = "update cust.CRM_SESSION_STATUS set sale_id = ?, case_id = ?, customer_id = ? where agent_id = ?";
	public void update(Connection conn, CrmStatus status) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(UPDATE);
		ps.setString(1, status.getSaleId());
		ps.setString(2, status.getCaseId());
		ps.setString(3, status.getErpCustomerId());
		ps.setString(4, status.getAgentId());//status.getAgentPK().getId());
		ps.executeUpdate();
	}

}
