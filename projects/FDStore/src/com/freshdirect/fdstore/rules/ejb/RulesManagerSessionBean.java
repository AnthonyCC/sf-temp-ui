package com.freshdirect.fdstore.rules.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RulesConfig;
import com.freshdirect.rules.RulesManagerDAO;

public class RulesManagerSessionBean extends SessionBeanSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1314489328918173170L;
	private static Category LOGGER = LoggerFactory.getInstance(RulesManagerSessionBean.class);
	
	public Map<String, Rule> getRules(RulesConfig config) throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			RulesManagerDAO dao = new RulesManagerDAO(config);
			conn = getConnection();
			return dao.loadRules(conn, config.getSubsystem());
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}
	
	public Rule getRule(RulesConfig config, String ruleId) throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			RulesManagerDAO dao = new RulesManagerDAO(config);
			conn = getConnection();
			return dao.getRule(conn, config.getSubsystem());
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}
	public void deleteRule(RulesConfig config, String ruleId) throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			RulesManagerDAO dao = new RulesManagerDAO(config);
			conn = getConnection();
			dao.deleteRule(conn, config.getSubsystem());
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}
	public void storeRule(RulesConfig config, Rule rule) throws FDResourceException, RemoteException
	{
		Connection conn = null;
		try {
			RulesManagerDAO dao = new RulesManagerDAO(config);
			conn = getConnection();
			dao.storeRule(conn, rule);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while getting the Dlv Restriction.");
			throw new FDResourceException(e, "Could not get the Dlv Restriction due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after getting the Dlv Restriction.", e);
				}
			}
		}
	}


	
}


	