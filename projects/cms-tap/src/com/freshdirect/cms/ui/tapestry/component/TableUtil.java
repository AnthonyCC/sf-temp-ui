/*
 * Created on Feb 25, 2005
 */
package com.freshdirect.cms.ui.tapestry.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ITable;

/**
 * @author vszathmary
 */
public class TableUtil {

	public final static String COLUMN_GROUP = "GROUP$";

	public static Map getTableGroups(ITable table) {
		AttributeDefI[] colDefs = table.getColumnDefinitions();
		int groupCol = -1;
		for (int i = 0; i < colDefs.length; i++) {
			if (COLUMN_GROUP.equals(colDefs[i].getName())) {
				groupCol = i;
				break;
			}
		}

		Map groups = new LinkedHashMap();

		if (groupCol == -1) {
			groups.put("", new FormattedTable(table));

		} else {
			for (Iterator i = table.getRows().iterator(); i.hasNext();) {
				ITable.Row row = (ITable.Row) i.next();

				String group = String.valueOf(row.getValues()[groupCol]);
				ITable groupTable = (ITable) groups.get(group);
				if (groupTable == null) {
					groupTable = new FormattedTable(new GroupTable(table, groupCol, group));
					groups.put(group, groupTable);
				}
			}
		}

		return groups;
	}

	private static class GroupTable implements ITable {

		private final ITable table;
		private final int groupColumn;
		private final String group;

		public GroupTable(ITable table, int groupColumn, String group) {
			this.table = table;
			this.groupColumn = groupColumn;
			this.group = group;
		}

		public AttributeDefI[] getColumnDefinitions() {
			return table.getColumnDefinitions();
		}

		public List getRows() {
			List l = new ArrayList();
			for (Iterator i = table.getRows().iterator(); i.hasNext();) {
				Row row = (Row) i.next();
				String rowGroup = String.valueOf(row.getValues()[groupColumn]);
				if (group.equals(rowGroup)) {
					l.add(row);
				}
			}
			return l;
		}
	}

}