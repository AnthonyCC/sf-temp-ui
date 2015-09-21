package ui;

import java.util.List;
import java.util.Map;

import org.sikuli.script.Screen;

import cbf.utils.LogUtils;

public class SikuliUIDriver extends cbf.engine.BaseAppDriver {
	
	private List<Map> sxuiMap;
	private String sDynamicValue1 = "_x";
	//private String sDynamicValue2 = "_y";
	private cbf.utils.LogUtils LOGGER =  new LogUtils(this);
	//private SikuliObjectMap sikuliObjMap = new SikuliObjectMap();
	public static  TableHandler tableObj;
	


	/**
	 * @param sikuli
	 */
	public SikuliUIDriver(Map parameters){
		
		this.sikuli = (Screen) parameters.get("sikuli");
		objMap = new ObjectMap();
		//sxuiMap = sikuliObjMap.sxuiMap;
		sxuiMap = objMap.uiMap;
		tableObj = new TableHandler();
	}
	
	/**
	 * Default constructor
	 */
	public SikuliUIDriver(){
		//System.out.println("****************"+uiMap);
	}
	
	public void click(String sxElementName){
		//System.out.println("*****************SIKULI.... CLICK......");
		//LOGGER.trace("*****************SIKULI.... CLICK......");
		Map result = null;
		try{
			result = objMap.getElementMap(sxElementName);
			
			if (result != null) {
				String sLocator = (String) result.get("locator");
				String sType = (String) result.get("type");
				String sOtherInfo = "";
				if(sType.equalsIgnoreCase("image")){
					sikuli.click(sLocator);
				}else{
					LOGGER.error("'"+sxElementName+"' is not a Sikuli element.");
				}
			}
		}catch (NullPointerException e) {
			LOGGER.trace("Error caused By \"" + sxElementName + "\" "
					+ e.getMessage());
		} catch (Exception e) {
			LOGGER.trace("Error caused By \"" + sxElementName + "\" "
					+ e.getMessage());
		}
	}
	
	public void doubleClick(String sxElementName){
		Map result = null;
		try{
			result = objMap.getElementMap(sxElementName);
			if (result != null) {
				String sLocator = (String) result.get("locator");
				String sType = (String) result.get("type");
				String sOtherInfo = "";
				if(sType.equalsIgnoreCase("image")){
					sikuli.doubleClick(sLocator);
				}else{
					LOGGER.error("'"+sxElementName+"' is not a Sikuli element.");
				}
			}
		}catch (NullPointerException e) {
			LOGGER.trace("Error caused By \"" + sxElementName + "\" "
					+ e.getMessage());
		} catch (Exception e) {
			LOGGER.trace("Error caused By \"" + sxElementName + "\" "
					+ e.getMessage());
		}
	}
	
	public void rightClick(String sxElementName){
		Map result = null;
		try{
			result = objMap.getElementMap(sxElementName);
			if (result != null) {
				String sLocator = (String) result.get("locator");
				String sType = (String) result.get("type");
				String sOtherInfo = "";
				if(sType.equalsIgnoreCase("image")){
					sikuli.rightClick(sLocator);
				}else{
					LOGGER.error("'"+sxElementName+"' is not a Sikuli element.");
				}
			}
		}catch (NullPointerException e) {
			LOGGER.trace("Error caused By \"" + sxElementName + "\" "
					+ e.getMessage());
		} catch (Exception e) {
			LOGGER.trace("Error caused By \"" + sxElementName + "\" "
					+ e.getMessage());
		}
	}
	
	public void type(String sxElementName, String sValue){
		Map result = null;
		try{
			result = objMap.getElementMap(sxElementName);
			if (result != null) {
				String sLocator = (String) result.get("locator");
				String sType = (String) result.get("type");
				String sOtherInfo = "";
				if(sType.equalsIgnoreCase("image")){
					sikuli.type(sLocator, sValue);
				}else{
					LOGGER.error("'"+sxElementName+"' is not a Sikuli element.");
				}
			}
		}catch (NullPointerException e) {
			LOGGER.trace("Error caused By \"" + sxElementName + "\" "
					+ e.getMessage());
		} catch (Exception e) {
			LOGGER.trace("Error caused By \"" + sxElementName + "\" "
					+ e.getMessage());
		}
	}

	public class TableHandler
	{
	/**
	 * For getting total number of columns in table
	 * @param sElementName
	 *          Element name for table locator - uiMap             
	 * @return int - Number of columns 
	 */
	
	public int getColumnCount(String sElementName)
	{
		int col_count=0;
	    Map result = null;
		result = objMap.getElementMap(sElementName);
		if (result != null) 
		{
			String sLocator = (String) result.get("locator");
			String sType = (String) result.get("type");
		}
		return col_count;
	}
	
	
	
}
}
