package com.freshdirect.rules;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.thoughtworks.xstream.XStream;

public class RulesDAO {

	private final static Category LOGGER = LoggerFactory.getInstance(RulesDAO.class);

	private final XStream xstream = new XStream();

	public RulesDAO(RulesConfig config) {
		List l = new ArrayList();
		l.addAll(config.getConditionTypes());
		l.addAll(config.getOutcomeTypes());
		for (Iterator i = l.iterator(); i.hasNext();) {
			ClassDescriptor desc = (ClassDescriptor) i.next();
			xstream.alias(desc.getXmlTag(), desc.getTargetClass());
		}
	}

	private static final String LOAD_QUERY = "select id, name, start_date, end_date, priority, conditions, outcome, subsystem "
		+ "from cust.rules where subsystem = ? ";

	public Map loadRules(Connection conn, String subsystem) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_QUERY);
		ps.setString(1, subsystem);
		ResultSet rs = ps.executeQuery();
		Map m = new TreeMap();
		while (rs.next()) {
			Rule r = loadFromResultSet(rs);
			m.put(r.getId(), r);
		}

		rs.close();
		ps.close();

		return m;
	}

	private static final String GET_QUERY = "select id, name, start_date, end_date, priority, conditions, outcome, subsystem "
		+ "from cust.rules where id = ? ";

	public Rule getRule(Connection conn, String id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(GET_QUERY);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		try {
			if (rs.next()) {
				Rule r = loadFromResultSet(rs);
				return r;
			} else {
				throw new SQLException("Could not find a rule for id: " + id);
			}
		} finally {
			rs.close();
			ps.close();
		}
	}

	private Rule loadFromResultSet(ResultSet rs) throws SQLException {
		Rule r = new Rule();
		r.setId(rs.getString("ID"));
		r.setName(rs.getString("NAME"));
		r.setStartDate(new java.util.Date(rs.getTimestamp("START_DATE").getTime()));
		r.setEndDate(new java.util.Date(rs.getTimestamp("END_DATE").getTime()));
		r.setPriority(rs.getInt("PRIORITY"));
		r.setSubsystem(rs.getString("SUBSYSTEM"));
		String conditions = "<conditions>" + NVL.apply(rs.getString("CONDITIONS"), "").trim() + "</conditions>";
		String outcome = NVL.apply(rs.getString("OUTCOME"), "").trim();

		List l = new ArrayList();

		try {
			ObjectInputStream in = xstream.createObjectInputStream(new StringReader(conditions));
			while (true) {
				try {
					l.add(in.readObject());
				} catch (EOFException e) {
					break;
				}
			}

		} catch (ClassNotFoundException e) {
			throw new SQLException("Bad data in Conditions: " + conditions);
		} catch (IOException e) {
			throw new SQLException("Bad data in Conditions: " + conditions);
		}

		if (!"".equals(outcome)) {
			Object o = xstream.fromXML(outcome);
			r.setOutcome(o);
		}

		r.setConditions(l);

		return r;
	}

	private static final String ADD_QUERY = "insert into cust.rules (id, name, start_date, end_date, priority, conditions, outcome, subsystem) "
		+ "values(?, ?, ?, ?, ?, ?, ?, ?) ";

	public void addRule(Connection conn, Rule rule) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(ADD_QUERY);
		String id = SequenceGenerator.getNextId(conn, "CUST");
		ps.setString(1, id);
		ps.setString(2, rule.getName());
		ps.setTimestamp(3, new Timestamp(rule.getStartDate().getTime()));
		ps.setTimestamp(4, new Timestamp(rule.getEndDate().getTime()));
		ps.setInt(5, rule.getPriority());
		ps.setString(6, getConditionsXML(rule.getConditions()).trim());
		ps.setString(7, xstream.toXML(rule.getOutcome()).trim());
		ps.setString(8, rule.getSubsystem());
		ps.executeUpdate();

		ps.close();
	}

	private static final String UPDATE_QUERY = "update cust.rules set name=?, start_date=?, end_date=?, priority=?, conditions=?, outcome=?, subsystem=? "
		+ "where id = ? ";

	public void storeRule(Connection conn, Rule rule) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(UPDATE_QUERY);
		ps.setString(1, rule.getName());
		ps.setTimestamp(2, new Timestamp(rule.getStartDate().getTime()));
		ps.setTimestamp(3, new Timestamp(rule.getEndDate().getTime()));
		ps.setInt(4, rule.getPriority());
		ps.setString(5, getConditionsXML(rule.getConditions()).trim());
		ps.setString(6, xstream.toXML(rule.getOutcome()).trim());
		ps.setString(7, rule.getSubsystem());
		ps.setString(8, rule.getId());

		try {
			if (1 != ps.executeUpdate()) {
				throw new SQLException("Cannot find rule to update for id: " + rule.getId());
			}
		} finally {
			ps.close();
		}

	}

	public void updateRule(Connection conn, Rule rule) throws SQLException {
		if (rule.getId() == null) {
			addRule(conn, rule);
		} else {
			storeRule(conn, rule);
		}
	}

	private static final String DELETE_QUERY = "delete from cust.rules where id = ? ";

	public void deleteRule(Connection conn, String ruleId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
		ps.setString(1, ruleId);
		try {
			if (1 != ps.executeUpdate()) {
				throw new SQLException("Cannot find rule to delete for id: " + ruleId);
			}
		} finally {
			ps.close();
		}
	}

	private String getConditionsXML(List conditions) {
		return xstream.toXML(conditions).replaceAll("\\<list>|</list>|\n|\t", "");
	}

}