/******************************************************************************
$Id : DbAccess.java 10/21/2014 4:07:28 PM
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

package cbf.dataAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cbf.engine.DataAccess;
import cbf.utils.DBUtils;
import cbf.utils.LogUtils;

/**
 * Implements DataAccess and provides module data
 * 
 */
public class DbAccess implements DataAccess {

	/**
	 * Constructor to initialize parameters
	 * 
	 * @param params
	 *            map containing parameters
	 */
	public DbAccess(Map params) {
		this.params = params;
	}

	/**
	 * Returns row data for the selected rows
	 * 
	 * @param componentCode
	 *            contains componentCode value
	 * @param rowSelector
	 *            contains row selection value
	 * @return list of Map
	 */
	public List<Map> selectRows(String componentCode, String rowSelector) {

		logger.trace("SelectRows(" + componentCode + "-" + rowSelector + ")");
		List<Map> list = new ArrayList<Map>();
		dbFileName = (String) params.get("dbname");

		dbUtils = new DBUtils(dbFileName);
		boolean dbExists = dbUtils.checkExists(dbFileName);
		if (dbExists) {
			list = null;
			ArrayList<Object> param = new ArrayList<Object>();
			list = dbUtils.runQuery(dbFileName, "Select * from " + moduleCode
					+ "__" + componentCode + " where [_rowId]='" + rowSelector
					+ "';", param);
		} else {
			logger.error("DB does not Exists");
		}
		dbUtils.disconnect();
		return list;
	}

	/**
	 * Reads all rows from the component sheet and returns list
	 * 
	 * @param componentCode
	 *            contains componentCode
	 * @return list of Map
	 */
	public List<Map> readRows(String componentCode) {
		return null;
	}

	/**
	 * Sets the value of module code parameter
	 * 
	 * @param moduleCode
	 *            value of module
	 */
	public void getModuleAccess(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	
	/**
	 * Returns DbAccess format string
	 */
	public String toString() {
		return "DbAccess("+params+")";
	}

	private LogUtils logger = new LogUtils(this);
	private String moduleCode;
	private String dbFileName;
	private DBUtils dbUtils;
	private Map params;

}
