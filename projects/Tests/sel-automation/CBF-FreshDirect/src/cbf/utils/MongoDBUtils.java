/******************************************************************************
$Id : MongoDBUtils.java 9/8/2014 1:21:28 PM
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 
 * Utility to handle MongoDB connection
 *
 */
public class MongoDBUtils {
	
	/**
	 * Returns instance of MongoDBUtils class
	 * 
	 * @return object of MongoDBUtils
	 */
	public static synchronized MongoDBUtils getInstance() {
		if (mongoDBUtils == null) {
			mongoDBUtils = new MongoDBUtils();
		}
		return mongoDBUtils;
	}
	
	/**
	 * Makes connection to MongoDB and returns its response
	 * @param info Map containing parameters for MongoDB request
	 * @return response of connection
	 */
	public String makeMongoConnection(Map info){
		setParameters(info);
		return makeConnection();
	}

	private LogUtils logger = new LogUtils(this);
	private String folderPath;
	private String testName;
	private Map params;
	private String request;
	private String input;
	private String userCredentials;
	
	private static MongoDBUtils mongoDBUtils;
	
	private String makeConnection(){		
		InputStream rstream = null;
		String line =null;

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(request);

		String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
		method.setRequestHeader ("Authorization", basicAuth);
		
		method.setRequestHeader("Content-Type", "application/json");		
		method.setRequestBody(input);
		
		try {
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			rstream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					rstream));
			line = br.readLine();

		} catch (IOException e) {
			logger.handleError("Error reading response ", e);
		}
		return line;
	}
	
	private void setParameters(Map info){
		params=(Map) info.get("params");
		testName=(String) info.get("testName");
		folderPath = (String) info.get("folderPath");

		String serverURL = (String) params.get("url");
		String projectName = (String) params.get("projectname");
		request = serverURL + "/" + projectName;
		
		String userName= (String) params.get("username");
		String password = (String) params.get("password");
		userCredentials=userName+":"+password;
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("folderpath", folderPath);
		args.put("tcName", testName);
		HashMap[] argArray = { (HashMap<String, String>) args };

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("component", "Service");
		params.put("object", "TestCase");
		params.put("method", "get");
		params.put("arguments", argArray);
		
		input = JsonUtils.getInstance().ObjectToJsonString(params);
	}
	
	/**
	 * Overloaded toString() method to return MongoDBUtils format String
	 */
	public String toString() {
		return "MongoDBUtils()" ;
	}

}
