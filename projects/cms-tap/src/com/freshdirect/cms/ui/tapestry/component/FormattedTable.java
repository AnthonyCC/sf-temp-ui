/*
 * Created on Feb 28, 2005
 */
package com.freshdirect.cms.ui.tapestry.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.meta.AttributeDef;

/**
 * Applies certain formatting/logic based on naming convention.
 * 
 * <ul>
 * <li><code>CLASS$</code> - hide column, available on Row as getMetaClass</li>
 * <li><code>GROUP$</code> - hide column, available on Row as getMetaGroup</li>
 * <li>suffix <code>_KEY$</code> - convert to ContentKey. Column value has to be a String, "ContentType:ID"</li>
 * <li>suffix <code>_ATTRIBUTE$</code> - lookup attribute value. Column value has to be a String, "ContentType:ID:attributeName"</li>
 * </ul>
 *   
 * @author vszathmary
 */
public class FormattedTable implements ITable {

	private final static int COLUMNTYPE_PLAIN = 0;
	private final static int COLUMNTYPE_META = 1;
	private final static int COLUMNTYPE_KEY = 2;
	private final static int COLUMNTYPE_ATTRIBUTE = 3;

	private final static String SUFFIX_KEY = "_KEY$";

	private final static String SUFFIX_ATTRIBUTE = "_ATTRIBUTE$";

	private final static String COLUMN_CLASS = "CLASS$";

	private final static Set META_COLUMNS = new HashSet();
	static {
		META_COLUMNS.add(TableUtil.COLUMN_GROUP);
		META_COLUMNS.add(COLUMN_CLASS);
	}

	private final ITable table;

	/** Map of String -> Integer */
	private final Map metaColumnsByName = new HashMap();

	private final int[] columnType;

	public FormattedTable(ITable table) {
		this.table = table;

		AttributeDefI[] defs = table.getColumnDefinitions();
		columnType = new int[defs.length];
		for (int i = 0; i < defs.length; i++) {
			String name = defs[i].getName();
			columnType[i] = COLUMNTYPE_PLAIN;

			if (META_COLUMNS.contains(name)) {
				metaColumnsByName.put(name, new Integer(i));
				columnType[i] = COLUMNTYPE_META;

			} else if (EnumAttributeType.STRING.equals(defs[i].getAttributeType())) {
				if (name.endsWith(SUFFIX_KEY)) {
					columnType[i] = COLUMNTYPE_KEY;

				} else if (name.endsWith(SUFFIX_ATTRIBUTE)) {
					columnType[i] = COLUMNTYPE_ATTRIBUTE;
				}
			}
		}
	}

	public AttributeDefI[] getColumnDefinitions() {
		AttributeDefI[] origDefs = table.getColumnDefinitions();
		AttributeDefI[] defs = new AttributeDefI[origDefs.length - metaColumnsByName.size()];
		int pos = 0;
		for (int i = 0; i < origDefs.length; i++) {
			switch (columnType[i]) {
				case COLUMNTYPE_META :
					continue;
				case COLUMNTYPE_KEY :
					String label = stripSuffix(origDefs[i].getLabel(), SUFFIX_KEY);
					defs[pos] = new AttributeDef(EnumAttributeType.RELATIONSHIP, origDefs[i].getName(), label);
					break;
				case COLUMNTYPE_ATTRIBUTE :
					label = stripSuffix(origDefs[i].getLabel(), SUFFIX_ATTRIBUTE);
					defs[pos] = new AttributeDef(EnumAttributeType.STRING, origDefs[i].getName(), label);
					break;
				default :
					defs[pos] = origDefs[i];

			}
			pos++;
		}
		return defs;
	}

	private static String stripSuffix(String label, String suffix) {
		return label.substring(0, label.length() - suffix.length());
	}

	public List getRows() {
		List rows = table.getRows();
		List l = new ArrayList(rows.size());
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Row row = (Row) i.next();
			l.add(new FormattedRow(row));
		}
		return l;
	}

	public class FormattedRow implements Row {

		private final Row row;

		public FormattedRow(Row row) {
			this.row = row;
		}

		public Object[] getValues() {
			Object[] origValues = row.getValues();
			Object[] values = new Object[origValues.length - metaColumnsByName.size()];
			int pos = 0;
			for (int i = 0; i < origValues.length; i++) {
				Object value = origValues[i];
				switch (columnType[i]) {
					case COLUMNTYPE_META :
						continue;
					case COLUMNTYPE_KEY :
						if (value != null) {
							values[pos] = ContentKey.decode((String) value);
						}
						break;
					case COLUMNTYPE_ATTRIBUTE :
						if (value != null) {
							String[] tokens = StringUtils.split((String) value, ":");
							if (tokens.length == 3) {
								ContentKey key = new ContentKey(ContentType.get(tokens[0]), tokens[1]);
								ContentNodeI node = key.lookupContentNode();
								if (node != null) {
									AttributeI attribute = node.getAttribute(tokens[2]);
									if (attribute != null && attribute.getValue() != null) {
										values[pos] = String.valueOf(String.valueOf(attribute.getValue()));
									}
								}
							}
						}
						break;
					default :
						values[pos] = value;
				}
				pos++;
			}
			return values;
		}

		private String getMeta(String name) {
			Integer idx = (Integer) metaColumnsByName.get(name);
			if (idx == null) {
				return null;
			}
			Object value = row.getValues()[idx.intValue()];
			return value == null ? null : String.valueOf(value);
		}

		public String getMetaGroup() {
			return getMeta(TableUtil.COLUMN_GROUP);
		}

		public String getMetaClass() {
			return getMeta(COLUMN_CLASS);
		}
	}

}