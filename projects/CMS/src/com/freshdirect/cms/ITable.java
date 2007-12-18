/*
 * Created on Feb 24, 2005
 */
package com.freshdirect.cms;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a columnar table with type definitions for the columns,
 * used by attribute values of {@link com.freshdirect.cms.EnumAttributeType#TABLE}
 */
public interface ITable extends Serializable {

	/**
	 * Get type definition of columns.
	 * 
	 * @return attribute definition of columns (never null)
	 */
	public AttributeDefI[] getColumnDefinitions();

	/**
	 * Get row data.
	 * 
	 * @return List of ITable.Row
	 */
	public List getRows();

	/**
	 * Interface for an individual row in the table.
	 */
	public static interface Row extends Serializable {

		/**
		 * Get column values. Number of columns returned must be consistent
		 * with {@link ITable#getColumnDefinitions()}.
		 * 
		 * @return column values of the row (never null)
		 */
		public Object[] getValues();
	}

}