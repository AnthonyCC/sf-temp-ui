/*
 * Created on Feb 8, 2005
 */
package com.freshdirect.cms.application.service.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.Factory;
import org.apache.log4j.Category;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.application.service.ContentDecoratorI;
import com.freshdirect.cms.meta.AttributeDef;
import com.freshdirect.cms.node.AttributeMappedNode;
import com.freshdirect.framework.util.HashMessageFormat;
import com.freshdirect.framework.util.LazyInstanceProxy;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * {@link com.freshdirect.cms.application.service.ContentDecoratorI}
 * implementation that exposes the results of queries contained in
 * content nodes of type <code>CmsQuery</code> or <code>CmsReport</code>
 * where the <code>language</code> attribute is unset or is "SQL".
 * <p>
 * After decoration, the <code>results</code> attribute will contain
 * the lazy-instantiated outcome of the query stored in the <code>script</code>
 * attribute. In the case of a <code>CmsQuery</code>, the result is a relationship,
 * for <code>CmsReport</code>s, the result is an {@link com.freshdirect.cms.ITable}.
 * <p>
 * It also performs very simple string substitution for parameter bindings,
 * obtained from the <code>parameters</code> attribute.
 * 
 * @see com.freshdirect.cms.node.AttributeMappedNode
 */
public class DbQueryDecorator implements ContentDecoratorI, Serializable {

	private final static Category LOGGER = LoggerFactory.getInstance(DbQueryDecorator.class);
	
	private final static String MAP_SEPARATOR = "|";

	private final DataSource dataSource;

	public DbQueryDecorator(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ContentNodeI decorateNode(ContentNodeI node) {
		ContentType type = node.getKey().getType();
		if (!(type.equals(CmsQueryTypes.QUERY) || type.equals(CmsQueryTypes.REPORT))) {
			return null;
		}

		String language = (String) node.getAttribute("language").getValue();
		if (!(language == null || "SQL".equals(language))) {
			return null;
		}

		String query = (String) node.getAttribute("script").getValue();
		if (query == null) {
			LOGGER.warn("No script for " + node);
			return null;
		}

		Object results;
		if (type.equals(CmsQueryTypes.REPORT)) {

			String paramString = (String) node.getAttribute("parameters").getValue();
			Map parameters = paramString == null ? null : AttributeMappedNode.stringToMap(paramString, MAP_SEPARATOR);
			results = LazyInstanceProxy.newInstance(ITable.class, new ReportFactory(query, parameters));

		} else {
			// CmsQueryTypes.QUERY
			results = LazyInstanceProxy.newInstance(List.class, new QueryFactory(query));
		}

		ContentNodeI clone = node.copy();
		clone.getAttribute("results").setValue(results);

		if (type.equals(CmsQueryTypes.REPORT)) {

			LOGGER.debug("decorateNode() " + node.getKey());
			String[] params = HashMessageFormat.getMessageParams(query);
			Map paramDefs = new HashMap(params.length);
			for (int i = 0; i < params.length; i++) {
				String key = params[i];
				paramDefs.put(key, new AttributeDef(EnumAttributeType.STRING, "param_" + key, "Parameter " + key));
			}
			return new AttributeMappedNode(clone, "parameters", paramDefs, MAP_SEPARATOR);

		}
		return clone;

	}

	private final class ReportFactory implements Factory, Serializable {
		private final String query;
		private final Map parameters;

		private ReportFactory(String query, Map parameters) {
			this.query = query;
			this.parameters = parameters;
		}

		public Object create() {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();

				String q = query;
				if (parameters != null) {
					q = HashMessageFormat.format(query, parameters);
				}

				LOGGER.debug("ReportFactory.create() -> " + q);

				PreparedStatement ps = conn.prepareStatement(q);
				ResultSet rs = ps.executeQuery();
				ITable table = DbTable.createFromResultSet(rs);

				rs.close();
				ps.close();

				return table;

			} catch (SQLException e) {
				e.printStackTrace();
				return new ExceptionTable(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new CmsRuntimeException(e);
					}
				}
			}
		}
	}

	private class QueryFactory implements Factory, Serializable {
		private final String query;

		private QueryFactory(String query) {
			this.query = query;
		}

		public Object create() {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);

				List l = new ArrayList();

				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ContentKey k = ContentKey.decode(rs.getString(1));
					l.add(k);
				}
				rs.close();
				ps.close();

				return l;

			} catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new CmsRuntimeException(e);
					}
				}
			}
		}
	}

}