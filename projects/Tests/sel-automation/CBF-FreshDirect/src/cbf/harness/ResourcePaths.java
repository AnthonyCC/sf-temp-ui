/******************************************************************************
$Id : ResourcePaths.java 9/8/2014 1:21:47 PM
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

package cbf.harness;

/**
 * All paths either are defined here.
 * 
 */
public class ResourcePaths {
	String autoHome;
	String workHome;
	String runHome;

	/**
	 * Returns suites path
	 * 
	 * @param relPath
	 * @param relName
	 * @return
	 */
	public String getSuiteResource(String relPath, String relName) {
		//return autoHome + "/" + relPath + "/" + relName;
		return createPath(autoHome, relPath, relName);
	}

	public String getFrameworkResource(String relPath, String relName) {
		//return workHome + "/" + relPath + "/" + relName;
		return createPath(workHome, relPath, relName);
	}

	public String getRunResource(String relPath, String relName) {
		//return runHome + "/" + relPath + "/" + relName;
		return createPath(runHome, relPath, relName);
	}

	public static ResourcePaths getInstance(String autoHome, String workHome,
			String runHome) {
		if (singleton == null)
			singleton = new ResourcePaths(autoHome, workHome, runHome);
		return singleton;
	}

	private ResourcePaths(String autoHome, String workHome, String runHome) {
		this.autoHome = autoHome;
		this.workHome = workHome;
		this.runHome = runHome;
	}
	
	private String createPath(String home, String relPath, String relName){
		if(!relPath.equals(""))
			home= home+"/"+relPath;
		if(!relName.equals(""))
			home= home+"/"+relName;		
		return home;
	}

	public static ResourcePaths singleton = null;
}
