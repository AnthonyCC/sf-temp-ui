/******************************************************************************
$Id : ObjectMap.java 9/8/2014 1:21:28 PM
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

package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cbf.harness.ResourcePaths;
import cbf.utils.ExcelAccess;
import cbf.utils.LogUtils;

public class ObjectMap {

	public List<Map> uiMap;
	
	public ObjectMap() {
		
		// TODO Auto-generated constructor stub
		uiMap = readLocators();
	}

	/**
	 * Reads locators and returns List of it
	 * 
	 * @return List of locators
	 */
	public List<Map> readLocators() {
		List<Map> locators = new ArrayList<Map>();
		try {
		/*	ExcelAccess.accessSheet(ResourcePaths.singleton.getSuiteResource("Plan/AppDriver/OR", "uiMap.xls"),"locators",
					new ExcelAccess.RowArrayBuilder(locators));*/
			
		ExcelAccess.accessLocatorSheet(ResourcePaths.singleton.getSuiteResource("Plan/AppDriver/OR", "uiMap.xls"),
					new ExcelAccess.RowArrayBuilder(locators));
	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return locators;
		
	}
	
	/**
	 * Returns the Locator for the given ElementName
	 * 
	 * @param sElementName
	 *            name of element
	 * @return Locator String
	 */
	public String getLocator(String sElementName) {
		Map result = null;
		String sLocator = null,sParentLocator = null,parentTemp= null;
		try {
			for (Map map : uiMap)
			{
				String temp = (String) map.get("elementName");
				if (sElementName.matches(temp) || sElementName.equals(temp)) 
				{
					result = map;
					break;
				}
			}
			parentTemp = (String) result.get("parentName");
			if(parentTemp!=null && !parentTemp.equals("")){
				
				for(Map map1 : uiMap)
				{
					String elementName = (String) map1.get("elementName");
					String Locator = (String) map1.get("locator");
					if((parentTemp.matches(elementName)|| parentTemp.equals(elementName)) && (Locator  != null))
					{
						sParentLocator = Locator;
						break;
					}
				}
				sLocator = sParentLocator + (String) result.get("locator");
			}
			else
			{
				sLocator = (String) result.get("locator");
			}
		}

		catch (RuntimeException e) 
		{
			logger.trace("Error: Trying to retrieve the Locator of Unknown Element \" "+ sElementName + " \"", e);
		}
		return sLocator;
	}

	
	
	/**
	 * Creates a sub-set of Mapping Element List only for Specified elements
	 * 
	 * @param columnMaps
	 *            from Mapping file
	 * @param selectedElements
	 *            of selected elements
	 * @return sub-set of Mapping Elements
	 */
	public List<Map> getSelectedMappingElements(List<Map> columnMaps,
			List<String> selectedElements) {
		List<Map> reDefinedMaps = new ArrayList<Map>();
		try {
			for (String element : selectedElements) {
				for (int i = 0; i < columnMaps.size(); i++) {
					Map temp = columnMaps.get(i);
					if (((String) temp.get("elementName")).matches(element)) {
						reDefinedMaps.add(temp);
					}
				}
			}
		} catch (Exception e) {
			logger
			.trace("Error : Caused while creating sub-set of Mapping Elements");
		}
		return reDefinedMaps;
	}
	
	
	
	/**
	 * Gets the list of locators that belongs to the specified page
	 * 
	 * @param sPageName
	 *            name of Pagee
	 * @return List of page locators
	 */
	public List<Map> getPageLocators(String sPageName) {
		Map result;
		List<Map> resultMap = new ArrayList();
		String sElement = "";
		try {
			for (Map map : uiMap) {
				sElement = ((String) map.get("elementName"));
				if (sElement.contains(".")) {
					String temp = sElement.substring(0, sElement.indexOf("."));
					if (sPageName.matches(temp)) {
						result = map;
						resultMap.add(result);
					}
				}
			}
		} catch (Exception e) {
			logger
			.trace("Error: Caused while searching the locators of page \" "
					+ sPageName + " \"", e);
		}
		return resultMap;
	}
	


	/**
	 * Retrieves the Element Map of the specified UI element
	 * 
	 * @param sElementName
	 *            name of Element
	 */
	public Map getElementMap(String sElementName) {
		Map locatorMap = null;
		try {
			for (Map map : uiMap) {
				String sTemp = (String) map.get("elementName");
				if (sElementName.matches(sTemp)) {
					locatorMap = map;
					break;
				}
			}
			if (locatorMap == null) {
				logger
				.trace("Error : Unknown Element \"" + sElementName
						+ "\"");

			}
		} catch (Exception e) {
			logger
			.trace("Error : Caused while trying to retrieve the map for Unknown Element \""
					+ sElementName + "\"");
		}
		return locatorMap;
	}
	
	/**
	 * Gets the dynamic locators for the specified page
	 * 
	 * @param sPageName
	 *            Name whose elements has to be filtered
	 * @return List of UI Element map
	 */
	public List<Map> getDynamicLocators(String sPageName) {
		List<Map> resultMap = new ArrayList();
		Map result;
		try {
			for (Map map : uiMap) {
				String temp = (String) map.get("page");
				if (sPageName.matches(temp)
						&& ((String) map.get("elementName")).startsWith("_")) {
					result = map;
					resultMap.add(result);
				}
			}
		} catch (Exception e) {
			logger
			.trace("Error: Caused while searching the dynamic locators of page \" "
					+ sPageName + " \"");
		}
		return resultMap;
	}	

	/**
	 * Overriding toString() method and returns ObjectMap format string
	 */
	public String toString() {
		return "ObjectMap()";
	}
	private String sDynamicValue1 = "_x";
	private String sDynamicValue2 = "_y";
	LogUtils logger =  new LogUtils(this);
}
