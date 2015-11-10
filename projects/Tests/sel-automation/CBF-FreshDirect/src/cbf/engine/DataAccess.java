/******************************************************************************
$Id : DataAccess.java 9/8/2014 1:21:28 PM
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

package cbf.engine;

import java.util.List;
import java.util.Map;

/**
 * 
 * Interface for accessing component data
 * 
 */
public interface DataAccess {
	
	/**
	 * Sets the value of module code parameter
	 * @param moduleCode value of module
	 */
	public void getModuleAccess(String moduleCode);
	
	/**
	 * Returns row data for the selected rows
	 * 
	 * @param componentCode
	 *            contains componentCode value
	 * @param rowSelector
	 *            contains row selection value
	 * @return list of Map
	 */
	public List<Map> selectRows(String componentCode, String rowSelector);
	
	/**
	 * Reads all rows from the component sheet and returns list
	 * 
	 * @param componentCode
	 *            contains componentCode
	 * @return list of Map
	 */
	public List<Map> readRows(String componentCode);
}
