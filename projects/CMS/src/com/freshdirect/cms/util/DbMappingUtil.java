package com.freshdirect.cms.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.meta.AttributeDef;

/**
 * Utility methods that help mapping JDBC meta-data to CMS attributes.
 */
public class DbMappingUtil {

	private final static Map TYPES = new HashMap();
	static {
		TYPES.put("java.lang.String", EnumAttributeType.STRING);
		TYPES.put("java.lang.Integer", EnumAttributeType.INTEGER);
		TYPES.put("java.lang.Double", EnumAttributeType.DOUBLE);
		TYPES.put("java.lang.Boolean", EnumAttributeType.BOOLEAN);
		TYPES.put("java.sql.Date", EnumAttributeType.DATE);

		TYPES.put("java.sql.Timestamp", EnumAttributeType.STRING);
	}

	private DbMappingUtil() {
	}
	
	/**
	 * Discover {@link AttributeDefI}s based on JDBC meta-data. 
	 * 
	 * @param meta result set meta-data
	 * @return attribute definitions (one for each column)
	 * @throws SQLException
	 */
	public static AttributeDefI[] getDefinitions(ResultSetMetaData meta) throws SQLException {
		int cols = meta.getColumnCount();
		AttributeDefI[] columnDefinitions = new AttributeDefI[cols];
		for (int i = 0; i < cols; i++) {
			columnDefinitions[i] = DbMappingUtil.getDefinition(meta, i + 1);
		}
		return columnDefinitions;
	}

	private static AttributeDefI getDefinition(ResultSetMetaData meta, int col) throws SQLException {
		String className = meta.getColumnClassName(col);
		EnumAttributeType attrType = (EnumAttributeType) TYPES.get(className);
		if ("java.math.BigDecimal".equals(className)) {
			attrType = meta.getScale(col) == 0 ? EnumAttributeType.INTEGER : EnumAttributeType.DOUBLE;
		}
		if (attrType == null) {
			throw new CmsRuntimeException("Unsupported SQL type " + className + " in column " + col);
		}

		String label = formatLabel(meta.getColumnLabel(col));
		return new AttributeDef(attrType, meta.getColumnName(col), label, false, false, true, EnumCardinality.ONE);
	}

	private static String formatLabel(String label) {
		return StringUtils.capitaliseAllWords(label.replace('_', ' ').toLowerCase());
	}

}
