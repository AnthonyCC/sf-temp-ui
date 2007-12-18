/*
 * Created on Feb 24, 2005
 */
package com.freshdirect.ocf.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ekracoff
 */
public class DbOcfTable implements OcfTableI {

	private final Column[] columns;
	private final List rows;

	public DbOcfTable(Column[] columns, List rows) {
		this.columns = columns;
		this.rows = rows;
	}

	public Column[] getColumns() {
		return columns;
	}

	public List getRows() {
		return rows;
	}

	public static DbOcfTable createFromResultSet(ResultSet rs) throws SQLException {

		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();

		Column[] columns = new Column[cols];
		for (int i = 0; i < cols; i++) {
			columns[i] = new ColumnImpl(meta.getColumnName(i + 1), meta.getColumnType(i + 1));
		}

		List rows = new ArrayList();
		while (rs.next()) {
			Object[] row = new Object[cols];
			for (int i = 0; i < cols; i++) {
				row[i] = coerce(columns[i].getDataType(), rs.getObject(i + 1));
			}
			rows.add(new RowImpl(row));
		}

		return new DbOcfTable(columns, rows);
	}

	private static Object coerce(int type, Object value) {
		if (Types.INTEGER == type) {
			if (value instanceof BigDecimal) {
				return new Integer(((BigDecimal) value).intValue());
			}
		}
		if (Types.VARCHAR == type) {
			return value == null ? null : value.toString();
		}
		return value;
	}

	private static class ColumnImpl implements Column {
		private String name;
		private int dataType;

		public ColumnImpl(String name, int type) {
			this.name = name;
			this.dataType = type;
		}

		public String getName() {
			return name;
		}

		public int getDataType() {
			return dataType;
		}

	}

	private static class RowImpl implements Row {
		private final Object[] values;

		public RowImpl(Object[] values) {
			this.values = values;
		}

		public Object[] getValues() {
			return values;
		}
	}

	public int getColumnPosByName(String name) {
		Column[] cols = getColumns();
		for (int x = 0; x < cols.length; x++) {
			if (name.equals(cols[x].getName())) {
				return x;
			}
		}
		return -1;
	}

	public List getValuesByColumn(String name) {
		int col = getColumnPosByName(name);
		if (col > -1) {
			return getValuesByColumn(col);
		}
		return new ArrayList();
	}
	
	public List getValuesByColumn(int col){
		List vals = new ArrayList();
		for (Iterator i = getRows().iterator(); i.hasNext();) {
			Row r = (Row) i.next();
			Object value = r.getValues()[col];
			vals.add(value);
		}
		return vals;
	}
	
	public void toFile(String absoluteFilename, String delimiter) throws IOException{
		File file = new File(absoluteFilename);
		
		BufferedWriter os = new BufferedWriter(new FileWriter(file));
		for(Iterator i = this.getRows().iterator(); i.hasNext();){
			Row row = (Row) i.next();
			String line = createLine(row.getValues(), delimiter);
			os.write(line);
		}
		os.flush();
		os.close();
	}
	
	private String createLine(Object[] values, String delimiter){
		StringBuffer line = new StringBuffer();
		for(int x = 0; x < values.length; x++){
			String val = (String) values[x];
			line.append(val);
			
			if(x+1 != values.length){
				line.append(delimiter);
			}
		}
		line.append("\n");
		return line.toString();
	}

}