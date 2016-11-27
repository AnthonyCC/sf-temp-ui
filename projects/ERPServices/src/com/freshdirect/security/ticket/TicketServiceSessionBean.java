package com.freshdirect.security.ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPSessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class TicketServiceSessionBean extends ERPSessionBeanSupport {
	
	private static final long serialVersionUID = 5281303678829934188L;

	private static final Logger LOGGER = LoggerFactory.getInstance(TicketServiceSessionBean.class);

	public Ticket createTicket(Ticket ticket) {
		Connection conn = null;

		try {
			conn = getConnection();
			Ticket existing = retrieveTicket(ticket.getKey());
			if (existing == null) {
				// insert
				PreparedStatement ps = conn.prepareStatement("INSERT INTO cust.securityticket "
						+ "(key1, owner, purpose, expiration, used, created) VALUES (?, ?, ?, ?, ?, SYSDATE)");
				ps.setString(1, ticket.getKey());
				ps.setString(2, ticket.getOwner());
				ps.setString(3, ticket.getPurpose());
				ps.setTimestamp(4, new Timestamp(ticket.getExpiration().getTime()));
				if (ticket.isUsed())
					ps.setTimestamp(5, new Timestamp(new Date().getTime()));
				else
					ps.setNull(5, Types.TIMESTAMP);
				try {
					ps.executeUpdate();
				} catch (SQLException e) {
					// integrity violation check
					if (e.getSQLState() != null && e.getSQLState().equals("23000"))
						ticket = null;
					else
						throw e;
				}
				ps.close();
				ps = null;
			} else {
				ticket = null;
			}
			return ticket;
		} catch (SQLException e) {
			LOGGER.error("query of security tickets failed : " + e.getMessage(), e);
			throw new EJBException(e);
		} finally {
			close(conn);
		}
	}

	public Ticket updateTicket(Ticket ticket) {
		Connection conn = null;

		try {
			conn = getConnection();
			Ticket existing = retrieveTicket(ticket.getKey());
			if (existing != null) {
				// update
				// only 'used' attribute can be updated
				PreparedStatement ps = conn.prepareStatement("UPDATE cust.securityticket SET used = ? WHERE key1 = ?");
				if (ticket.isUsed())
					ps.setTimestamp(1, new Timestamp(new Date().getTime()));
				else
					ps.setNull(1, Types.TIMESTAMP);
				ps.setString(2, ticket.getKey());
				ps.executeUpdate();
				ps.close();
				ps = null;
			}
			return ticket;
		} catch (SQLException e) {
			LOGGER.error("query of security tickets failed : " + e.getMessage(), e);
			throw new EJBException(e);
		} finally {
			close(conn);
		}
	}

	public Ticket retrieveTicket(String key) {
		Connection conn = null;

		try {
			conn = getConnection();
			return retrieveTicket(key, conn);
		} catch (SQLException e) {
			LOGGER.error("query of security tickets failed : " + e.getMessage(), e);
			throw new EJBException(e);
		} finally {
			close(conn);
		}
	}

	private Ticket retrieveTicket(String key, Connection conn) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("SELECT key1, owner, purpose, expiration, used FROM cust.securityticket WHERE key1 = ?");
		ps.setString(1, key);
		ResultSet rs = ps.executeQuery();

		Ticket ticket = null;
		if (rs.next()) {
			key = rs.getString(1);
			String owner = rs.getString(2);
			String purpose = rs.getString(3);
			Date expiration = new Date(rs.getTimestamp(4).getTime());
			boolean used = rs.getTimestamp(5) != null;
			ticket = new Ticket(key, owner, purpose, expiration, used);
		}

		rs.close();
		rs = null;

		ps.close();
		ps = null;

		return ticket;
	}
}
