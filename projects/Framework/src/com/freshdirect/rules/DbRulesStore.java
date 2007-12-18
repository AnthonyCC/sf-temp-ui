package com.freshdirect.rules;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class DbRulesStore extends AbstractRuleStore {

	private final DataSource dataSource;
	private final RulesConfig config;
	private final RulesDAO dao;

	public DbRulesStore(DataSource dataSource, String subsystem, List configurations) {
		this.dataSource = dataSource;
		this.config = findConfig(configurations, subsystem);
		this.dao = new RulesDAO(this.config);
	}


	public Map getRules() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return dao.loadRules(conn, config.getSubsystem());
		} catch (SQLException e) {
			throw new RulesRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RulesRuntimeException(e);
			}
		}
	}

	public Rule getRule(String ruleId) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return dao.getRule(conn, ruleId);
		} catch (SQLException e) {
			throw new RulesRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RulesRuntimeException(e);
			}
		}
	}

	public void storeRule(Rule rule) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			dao.updateRule(conn, rule);
			conn.commit();
		} catch (SQLException e) {
			throw new RulesRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e) {
				throw new RulesRuntimeException(e);
			}
		}

	}

	public void deleteRule(String ruleId) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			dao.deleteRule(conn, ruleId);
			conn.commit();
		} catch (SQLException e) {
			throw new RulesRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e) {
				throw new RulesRuntimeException(e);
			}
		}
	}

	public String getSubsystem() {
		return this.config.getSubsystem();
	}

}
