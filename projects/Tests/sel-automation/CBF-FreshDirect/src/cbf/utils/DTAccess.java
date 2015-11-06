/******************************************************************************
$Id : DTAccess.java 9/8/2014 1:22:08 PM
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

import cbf.utils.DataRow;
import cbf.utils.ExcelAccess;
import cbf.utils.LogUtils;
import cbf.utils.ExcelAccess.MapReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Implements functionalities like readrows to read all rows from component sheet, read selected rows etc.
 * 
 */
public class DTAccess {
	private LogUtils logger=new LogUtils(this);
	private String filePath;

	/**
	 * 
	 * Interface to select row
	 *
	 */
	public interface RowSelector {
		/**
		 * Returns true/false depending on row value
		 * @param row Map of row values
		 * @param rowIx index of row
		 * @return row exists or not
		 */
		public boolean select(Map row, int rowIx);
	}

	/**
	 * 
	 * Implements RowSelector interface and selects row by ID
	 *
	 */
	public static class RowSelectorByRowId implements RowSelector {
		private String rowId;

		/**
		 * Constructor to initialize rowId
		 * @param rowId contains rowId value
		 */
		public RowSelectorByRowId(String rowId) {
			this.rowId = rowId;
		}

		/**
		 * Returns true/false depending on row value
		 * @param row Map of row values
		 * @param rowIx index of row
		 * @return row exists or not
		 */
		public boolean select(Map row, int rowIx) {
			if (rowId.equals("")) {
				return true;
			} else {
				return rowId.equals((String) row.get("_rowId"));
			}
		}
	}

	/**
	 * Constructor to initialize excelFilePath variable
	 * @param excelFilePath path of excel file
	 */
	public DTAccess(String excelFilePath) {
		this.filePath = excelFilePath;	//FIXME: check for file existence/readability; logger.handleError
	}

	/**
	 * Returns List containing excel sheet rows
	 * @param sheetName name of excel sheet
	 * @return List of rows 
	 */
	public List<Map> readSheet(String sheetName) {
		return readSelectedRows(sheetName, "");
	}

	//FIXME:catch exception and call handleError

	/**
	 * Reads selected rows and returns list
	 * @param sheetName name of excel sheet
	 * @param selector object of RowSeletor
	 * @return list of selected rows
	 */
	public List<Map> readSelectedRows(String sheetName,
			final RowSelector selector) throws BiffException, IOException {
		// List<Map> rows = new ArrayList<Map>();
		class MyHandler extends MapReader {
			public Map outrow;
			List<Map> rows = new ArrayList();

			@Override
			public boolean handleRow(DataRow row, int rowIx) {
				if (selector == null || selector.select(row, rowIx)) {
					rows.add(row);
				}
				return true;
			}
		}

		MyHandler mh = new MyHandler();
		ExcelAccess.accessSheet(filePath, sheetName, mh);

		return mh.rows;
	}

	// convenience
	/**
	 * Reads selected rows and returns list
	 * @param sheetName name of excel sheet
	 * @param rowId index of row
	 * @return list of selected rows
	 */
	public List<Map> readSelectedRows(String sheetName, String rowId) {
		RowSelector selector = new RowSelectorByRowId(rowId);
		List<Map> selectedRows = null;
		try {
			selectedRows = readSelectedRows(sheetName, selector);
		} catch (BiffException e) {
			logger.handleError("Exception caught : "+e.getMessage());
		} catch (IOException e) {
			logger.handleError("Exception caught : "+e.getMessage());
		}
		return selectedRows;
	}

	/**
	 * Checks existence of excel sheet
	 * @param sheetName name of excel sheet
	 * @return excel sheet exists or not
	 */
	public boolean isSheetExists(String sheetName) {
		boolean result = false;
		
		try {
			Workbook objWorkbook;
			Sheet objWorksheet;
			int blnSheetExists;
			Workbook workbook = Workbook.getWorkbook(new File(filePath));
			String sheets[] = workbook.getSheetNames();
			for (String sheet : sheets) {
				if (sheet.equals(sheetName)) {
					result = true;
					return result;
				}
			}
		} catch (IOException e) {
			logger.handleError("Exception caught :" +e.getMessage());
		} catch (BiffException e) {
			logger.handleError("Exception caught :" +e.getMessage());
		}
		return result;
	}


	public String toString() {
		return  "Accessing {" + filePath + "}";
	}
}
