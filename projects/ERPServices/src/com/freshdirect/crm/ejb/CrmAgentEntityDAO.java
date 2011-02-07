package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.ObjectNotFoundException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.framework.core.EntityDAOI;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author knadeem
 */

public class CrmAgentEntityDAO implements EntityDAOI {

	public PrimaryKey findByPrimaryKey(Connection conn, PrimaryKey pk) throws SQLException, ObjectNotFoundException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.AGENT WHERE ID = ?");
		ps.setString(1, pk.getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new ObjectNotFoundException("Unable to find ErpAgent with PK " + pk);
		}
		String id = rs.getString("ID");
		rs.close();
		ps.close();
		return new PrimaryKey(id);
	}

	public PrimaryKey findUserByIdAndPassword(Connection conn, String userId, String password) throws SQLException, ObjectNotFoundException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.AGENT WHERE USER_ID = ? AND PASSWORD = ?");
		ps.setString(1, userId);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String id = rs.getString("ID");
			rs.close();
			ps.close();
			return new PrimaryKey(id);
		}
		rs.close();
		ps.close();
		throw new ObjectNotFoundException("No Agent Found for USER_ID: " + userId);
	}
	
	public Collection<PrimaryKey> findAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.AGENT WHERE LDAP_ID IS NOT NULL");
		ResultSet rs = ps.executeQuery();
		List<PrimaryKey> pks = new ArrayList<PrimaryKey>();
		while(rs.next()){
			pks.add(new PrimaryKey(rs.getString("ID")));
		}
		return pks;
	}

	public void create(Connection conn, PrimaryKey pk, ModelI model) throws SQLException {
		CrmAgentModel agent = (CrmAgentModel) model;
		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO CUST.AGENT(ID, USER_ID, PASSWORD, FIRST_NAME, LAST_NAME, ACTIVE, ROLE, CREATED, MASQUERADE_ALLOWED,LDAP_ID) VALUES(?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, pk.getId());
		ps.setString(2, agent.getUserId());
		ps.setString(3, agent.getPassword());
		ps.setString(4, agent.getFirstName());
		ps.setString(5, agent.getLastName());
		ps.setString(6, agent.isActive() ? "X" : "");
		ps.setString(7, agent.getRole().getCode());
		ps.setTimestamp(8, new java.sql.Timestamp(new Date().getTime()));
		ps.setString(9, agent.isMasqueradeAllowed() ? "Y" : "N");
		ps.setString(10,agent.getLdapId());

		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not created");
		}
		ps.close();
		this.insertAgentQueues(conn, pk, agent.getAgentQueues());
	}

	public ModelI load(Connection conn, PrimaryKey pk) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement("SELECT USER_ID, PASSWORD, FIRST_NAME, LAST_NAME, ACTIVE, ROLE, CREATED, MASQUERADE_ALLOWED, LDAP_ID FROM CUST.AGENT WHERE ID = ?");
		ps.setString(1, pk.getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new SQLException("No Agent for PK: " + pk);
		}
		CrmAgentModel agent = new CrmAgentModel(pk);
		agent.setUserId(rs.getString("USER_ID"));
		agent.setPassword(rs.getString("PASSWORD"));
		agent.setFirstName(rs.getString("FIRST_NAME"));
		agent.setLastName(rs.getString("LAST_NAME"));
		agent.setActive("X".equalsIgnoreCase(rs.getString("ACTIVE")) ? true : false);
		agent.setRole(CrmAgentRole.getEnum(rs.getString("ROLE")));
		agent.setCreateDate(rs.getTimestamp("CREATED"));
		agent.setMasqueradeAllowed( "Y".equalsIgnoreCase( rs.getString("MASQUERADE_ALLOWED") ) );
		agent.setLdapId(rs.getString("LDAP_ID"));

		rs.close();
		ps.close();
		
		agent.setAgentQueues(this.selectAgentQueues(conn, pk));

		return agent;
	}

	public void store(Connection conn, ModelI model) throws SQLException {
		CrmAgentModel agent = (CrmAgentModel) model;
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.AGENT SET USER_ID=?, PASSWORD=?, FIRST_NAME=?, LAST_NAME=?, ACTIVE=?, ROLE=?, MASQUERADE_ALLOWED=? WHERE ID=?");
		ps.setString(1, agent.getUserId());
		ps.setString(2, agent.getPassword());
		ps.setString(3, agent.getFirstName());
		ps.setString(4, agent.getLastName());
		ps.setString(5, agent.isActive() ? "X" : "");
		ps.setString(6, agent.getRole().getCode());
		ps.setString(7, agent.isMasqueradeAllowed() ? "Y" : "N");
		ps.setString(8, agent.getPK().getId());

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated for PK:" + agent.getPK());
		}
		ps.close();
		this.deleteAgentQueues(conn, agent.getPK());
		this.insertAgentQueues(conn, agent.getPK(), agent.getAgentQueues());
	}

	public void remove(Connection conn, PrimaryKey pk) throws SQLException {
		//delete queues
		this.deleteAgentQueues(conn, pk);
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.AGENT WHERE ID = ?");
		ps.setString(1, pk.getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();
	}

	private void deleteAgentQueues(Connection conn, PrimaryKey agentPk) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.AGENT_QUEUE WHERE AGENT_ID=?");
		ps.setString(1, agentPk.getId());
		ps.executeUpdate();
		ps.close();
	}
	
	private  void insertAgentQueues(Connection conn, PrimaryKey agentPk, List<CrmCaseQueue> queues) throws SQLException {
		if (queues.isEmpty()) {
			return;
		}

		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.AGENT_QUEUE(AGENT_ID, CASE_QUEUE) VALUES (?,?)");
		for ( CrmCaseQueue queue : queues ) {
			ps.setString(1, agentPk.getId());
			ps.setString(2, queue.getCode());
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}
	
	private List<CrmCaseQueue> selectAgentQueues(Connection conn, PrimaryKey agentPk) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CASE_QUEUE FROM CUST.AGENT_QUEUE WHERE AGENT_ID=? ORDER BY CASE_QUEUE");
		ps.setString(1, agentPk.getId());

		List<CrmCaseQueue> queues = new ArrayList<CrmCaseQueue>();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			queues.add(CrmCaseQueue.getEnum(rs.getString("CASE_QUEUE")));
		}

		rs.close();
		ps.close();

		return queues;
	}
	
	public PrimaryKey findAgentByLdapId(Connection conn, String agentLdapId) throws SQLException, ObjectNotFoundException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.AGENT WHERE LDAP_ID = ?");
		ps.setString(1, agentLdapId);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String id = rs.getString("ID");
			rs.close();
			ps.close();
			return new PrimaryKey(id);
		}
		rs.close();
		ps.close();
		throw new ObjectNotFoundException("No Agent Found for LDAP_ID: " + agentLdapId);
	}
}
