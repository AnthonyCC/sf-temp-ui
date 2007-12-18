/**
 * @author ekracoff
 * Created on Apr 21, 2005*/

package com.freshdirect.ocf.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


public interface OcfTableI extends Serializable {

	public Column[] getColumns();

	/**
	 * @return List of ITable.Row
	 */
	public List getRows();
	
	public int getColumnPosByName(String name);
	
	public List getValuesByColumn(String name);
	
	public List getValuesByColumn(int col);
	
	public void toFile(String absoluteFileName, String delimiter) throws IOException;

	public static interface Row extends Serializable {
		public Object[] getValues();
	}
	
	public static interface Column extends Serializable{
		public String getName();
		public int getDataType();
	}

}