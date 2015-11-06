package CBF.Test.utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CBF.utils.Configuration;
import CBF.utils.LogUtils;

public class TestConfig {

	private static Configuration CONFIG;
	private LogUtils logger = new LogUtils(this);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "config/config.xml";
		TestConfig conf = new TestConfig(fileName);
		conf.printValues();
	}

	public TestConfig(String fileName) {
		try {
			CONFIG = Configuration.getInstance();
			setFileName(fileName);
		} catch (FileNotFoundException e) {
			logger.handleError("File not exist ", fileName, e);
		}
	}

	private void traverseObject(Object object) {
		try {
			String str = (String) object;
			System.out.println(str);
		} catch (ClassCastException e) {
			try {
				Map<String, Object> map = (Map) object;
				for (String str : map.keySet()) {
					System.out.println(str + "=>" + map.get(str));
				}
			} catch (ClassCastException c) {
				ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) object;
				for (Object obj : list) {
					traverseObject(obj);
				}
			}
		}
	}

	private void printValues() {
		System.out.print("For key :TestCaseAccess value :");
		traverseObject(CONFIG.get("TestCaseAccess"));

		Map<String,Object> map = (Map<String,Object>) CONFIG.get("DataAccess");
		String dataAccess = (String) map.get("type");
		map=(Map<String,Object>) CONFIG.get("TestCaseAccess");
		String testCaseAccess = (String) map.get("type");
		System.out.println(dataAccess+" "+testCaseAccess);
		
		map=(Map<String, Object>) CONFIG.get("TestCaseAccess");
		map=(Map<String, Object>) map.get("parameters");
		System.out.println(map);
		String serverURL = (String) map.get("url");
		String dbName = (String) map.get("dbname");
		String request = serverURL + "/" + dbName;
		
		String userName= (String) map.get("username");
		String password = (String) map.get("password");
		String userCredentials=userName+":"+password;		
		System.out.println(request+" "+userCredentials);
		
		ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) CONFIG.get("ReporterSelection");
		String reporterSelection ="";
		String[] reportsSelected = null;int i=0;
		for (Object obj : list) {
			map=(Map<String, Object>) obj;
			if(i==0){
				reporterSelection=(String) map.get("type");
				i++;
			}
			else
			reporterSelection=reporterSelection+","+(String) map.get("type");
		}
		System.out.println(reporterSelection);
		reportsSelected = reporterSelection.split(",");
		System.out.println(reportsSelected.length);
		for(String selReport : reportsSelected){
			System.out.println(selReport);
		}

		System.out.print("\nFor key :ReporterSelection value :");
		traverseObject(CONFIG.get("ReporterSelection"));
		System.out.print("\nFor key :WorkFolder value :");
		traverseObject(CONFIG.get("WorkFolder"));
	}
}
