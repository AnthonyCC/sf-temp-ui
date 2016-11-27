/******************************************************************************
$Id : ExcelAccess.java 9/8/2014 1:22:41 PM
Copyright 2014-2016 IGATE GROUP OF COMPANIES. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF IGATE GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND IGATE GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
 ******************************************************************************/

package cbf.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 
 * Handles all the function related to Excel Data sheet like reading
 * sheet,reading row,etc..
 * 
 */
public class ExcelAccess {
	private ExcelAccess() {
	}

	/**
	 * 
	 * Handles row data
	 * 
	 */
	public interface RowHandler {

		/**
		 * Accesses row and returns boolean result
		 * 
		 * @param rowAccess
		 *            object of RowAccess
		 * @param rowIx
		 *            index of row to be accessed
		 * @return boolean result
		 */
		boolean handleRow(RowAccess rowAccess, int rowIx);
	}

	/**
	 * 
	 * Accesses row of sheet and sets respective values
	 * 
	 */
	public interface RowAccess {
		String[] get();

		void set(String[] values);

		void set(int colIx, String value);
	};

	/**
	 * Accesses the particular sheet of workbook
	 * 
	 * @param fileName
	 *            name of workbook
	 * @param sheetName
	 *            name of excel sheet
	 * @param rowHandler
	 *            object of RowHandler
	 * @return number of rows
	 */
	public static int accessSheet(String fileName, String sheetName,
			RowHandler rowHandler) throws BiffException, IOException {
		
		Workbook workbook = Workbook.getWorkbook(new File(fileName));
		Sheet sheet = workbook.getSheet(sheetName);
		return access(sheet, rowHandler);
		
	}
	
	/**
	 * Accesses the particular sheet of uiMap(locators) workbook
	 * 
	 * @param fileName
	 *            name of workbook
	 * @param rowHandler
	 *            object of RowHandler
	 * @return number of Locators
	 */
	
	public static int accessLocatorSheet(String fileName,
			RowHandler rowHandler) throws BiffException, IOException {
		
		Workbook workbook = Workbook.getWorkbook(new File(fileName));	

		int sheetNo = workbook.getNumberOfSheets();
		int numberofLoc = 0;
		for(int shNo=0; shNo<sheetNo; shNo++)
		{
			Sheet sheet = workbook.getSheet(shNo);
			numberofLoc = numberofLoc+access(sheet, rowHandler);
		}

		return  numberofLoc;

	}

	private static class SimpleRowAccess implements RowAccess {
		final Cell[] cells;

		SimpleRowAccess(Cell[] cells) {
			this.cells = cells;
		}

		public String[] get() {
			String[] colValues = new String[cells.length];
			for (int j = 0; j < cells.length; j++) {
				colValues[j] = cells[j].getContents().trim();
			}
			return colValues;
		}

		public void set(String[] values) {
			for (int i = 0; i < cells.length; ++i) {
				if (i >= values.length)
					break;
			}
		}

		public void set(int colIx, String value) {
			// cells[colIx].setContents(value);
		}
	}

	/**
	 * Accesses sheet and returns the row count
	 * 
	 * @param sheet
	 *            name of sheet
	 * @param rowHandler
	 *            object of RowHandler
	 * @return count of rows
	 */
	public static int access(Sheet sheet, RowHandler rowHandler) {
		int count = 0;
		for (int i = 0; i < sheet.getRows(); i++) {
			Cell cells[] = sheet.getRow(i);
			CellView c = sheet.getRowView(i);

			boolean rc = rowHandler.handleRow(new SimpleRowAccess(cells), i);
			if (!rc)
				break;
			++count;
		}
		return count;
	}

	/**
	 * 
	 * Sets and gets the value of rows
	 * 
	 */
	public interface NamedRowAccess {
		/**
		 * Gets Row and returns it
		 * 
		 * @return object of DataRow
		 */
		public DataRow get();

		/**
		 * Sets value of row
		 * 
		 * @param namedValues
		 *            object of DataRow
		 */
		public void set(DataRow namedValues);

		/**
		 * Overloaded method that sets value of particular column
		 * 
		 * @param colName
		 *            name of field
		 * @param value
		 *            value of that field
		 */
		public void set(String colName, String value);
	}

	/**
	 * 
	 * Implementing RowHandler interface and handles row data
	 * 
	 */
	public static abstract class NamedRowHandler implements RowHandler {
		private String[] colNames;
		private Map<String, Integer> colName2IxMap = new HashMap<String, Integer>();

		private final class Access implements NamedRowAccess {
			private RowAccess rowAccess;

			private Access(RowAccess rowAccess) {
				this.rowAccess = rowAccess;
			}

			public DataRow get() {
				String[] values = rowAccess.get();
				HashMap namedValues = new HashMap();
				for (int ix = 0; ix < values.length; ix++) {
					if (ix >= colNames.length) {
						break;
					}
					String colName = colNames[ix];
					namedValues.put(colName, values[ix]);
				}

				return new DataRow(namedValues);
			}

			public void set(DataRow namedValues) {
				String[] values = new String[colNames.length];
				/*
				 * for (<key, value> : namedValues) { set(key, value); }
				 */
			}

			public void set(String colName, String value) {
				int ix = colName2IxMap.get(colName);
				/*
				 * if (ix == null) throw error; rowAccess.set(ix, value);
				 */
			}
		}

		/**
		 * Abstract method of RowHandler
		 * 
		 * @param namedRowAccess
		 *            object of NamedRowAccess
		 * @param rowIx
		 *            index of row
		 * @return boolean result
		 */
		public abstract boolean handleRow(NamedRowAccess namedRowAccess,
				int rowIx);

		/**
		 * Handles row and returns boolean result
		 * 
		 * @param rowAccess
		 *            object of RowAccess
		 * @param rowIx
		 *            index of row
		 * @return boolean result
		 */
		final public boolean handleRow(RowAccess rowAccess, int rowIx) {
			String[] colValues = rowAccess.get();
			if (rowIx == 0) { // header row
				setColNames(colValues);
				return true;
			}
			return handleRow(new Access(rowAccess), rowIx - 1);
		}

		private void setColNames(String[] colNames) {
			this.colNames = colNames;
			for (int ix = 0; ix < colNames.length; ix++)
				colName2IxMap.put(colNames[ix], ix);
		}
	}

	/**
	 * 
	 * Extending NamedRowHandler class and handles row data
	 * 
	 */
	public static abstract class MapReader extends NamedRowHandler {
		/**
		 * Abstract method of NamedRowHandler class
		 * 
		 * @param row
		 *            object of DataRow
		 * @param rowIx
		 *            index of row
		 * @return boolean result
		 */
		public abstract boolean handleRow(DataRow row, int rowIx);

		/**
		 * Overloaded method of NamedRowHandler class
		 * 
		 * @param namedRowAccess
		 *            object of NamedRowAccess
		 * @param rowIx
		 *            index of row
		 * @return boolean result
		 */
		final public boolean handleRow(NamedRowAccess namedRowAccess, int rowIx) {
			return handleRow((DataRow) namedRowAccess.get(), rowIx);
		}
	}

	/**
	 * 
	 * Extends MapHeader class and defines its abstract method
	 * 
	 */
	public static class RowArrayBuilder extends MapReader {
		private List<Map> rows;

		/**
		 * Constructor to initialize List of rows
		 * 
		 * @param rows
		 *            List containing row data in Map
		 */
		public RowArrayBuilder(List<Map> rows) {
			this.rows = rows;
		}

		/**
		 * Abstract method of MapHeader class that is handling row data
		 * 
		 * @param row
		 *            object of DataRow
		 * @param rowIx
		 *            index of row
		 * @return boolean result
		 */
		public boolean handleRow(DataRow row, int rowIx) {
			rows.add(row);
			return true;
		}
	}
}
