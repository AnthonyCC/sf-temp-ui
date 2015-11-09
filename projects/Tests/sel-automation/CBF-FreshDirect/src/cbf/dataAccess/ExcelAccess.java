/******************************************************************************
$Id : ExcelAccess.java 9/8/2014 1:22:07 PM
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

import java.util.List;
import java.util.Map;

import cbf.engine.DataAccess;
import cbf.harness.Harness;
import cbf.harness.ResourcePaths;
import cbf.utils.DTAccess;
import cbf.utils.FileUtils;
import cbf.utils.LogUtils;
import cbf.utils.Utils;

/**
 * Implements DataAccess and provides module data
 * 
 */
public class ExcelAccess implements DataAccess {

	/**
	 * 
	 * Constructor to initialize parameters
	 * 
	 * @param params
	 *            map containing parameters
	 */
	public ExcelAccess(Map params) {
		this.params = params;
		// this.resourcePaths = Harness.resourcePaths;
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

		List<Map> data = null;

		String filePath = getFilePath(componentCode);
		if (filePath == null) { // changed .equals to ==
			logger.handleError("File path is null for ", componentCode);
		}
		data = new DTAccess(filePath).readSelectedRows(componentCode,
				rowSelector);

		return data;
	}

	/**
	 * Reads all rows from the component sheet and returns list
	 * 
	 * @param componentCode
	 *            contains componentCode
	 * @return list of Map
	 */
	public List<Map> readRows(String componentCode) {
		List<Map> data = null;

		logger.trace("ReadRows(" + componentCode + ")");
		String sFilePath = getFilePath(componentCode);
		if (sFilePath == null) { // changed .equals to ==
			logger.handleError("File path is null for ", componentCode);
		}
		DTAccess dtAccess = new DTAccess(sFilePath);
		if (dtAccess.isSheetExists(componentCode)) {
			data = dtAccess.readSheet(componentCode);
		}
		return data;
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
	 * Returns ExcelAccess format string
	 */
	public String toString() {
		return "ExcelAccess(" + params + ")";
	}

	private String getFilePath(String componentCode) {
		String filePath = null;
		if (Utils.string2Bool((String) ResourcePaths.singleton.getSuiteResource("Plan", ""))) {
			filePath = tryFilePath(moduleCode, componentCode + ".xls");

		}
		if (filePath == null) {
			filePath = tryFilePath("", moduleCode + "Data.xls");
		}
		return filePath;
	}

	private String tryFilePath(String branchPath, String fileName) {
		logger.trace("TryFilePath(" + branchPath + "-" + fileName + ")");
		String filePath = (String) params.get("folderpath");
		if (filePath.equals("")) {
			String folderPath = "Plan/Data";
			if (!(branchPath.equals(""))) {
				folderPath = folderPath + "/" + branchPath;
			}
			// filePath = resourcePaths.makePath(folderPath, fileName);
			filePath = resourcePaths.getSuiteResource(folderPath, fileName);
			if ((filePath == null) || !(FileUtils.fileExists(filePath))) {
				return null;
			}
		}
		logger.trace("TryFilePath(" + branchPath + "-" + fileName + ")="
				+ filePath);
		return filePath;
	}

	private LogUtils logger = new LogUtils(this);
	private ResourcePaths resourcePaths = ResourcePaths.singleton;
	private Map params;
	private String moduleCode;
}
