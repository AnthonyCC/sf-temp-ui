/******************************************************************************
$Id : DesignerDeserializer.java 9/8/2014 1:21:28 PM
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

package cbf.testAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cbf.engine.TestCase;
import cbf.engine.TestIteration;
import cbf.engine.TestStep;
import cbf.utils.DataRow;
import cbf.utils.JsonUtils;
import cbf.utils.LogUtils;
import cbf.utils.MongoDBUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * Generates DesignerTestCase
 *
 */
public class DesignerDeserializer {

	/**
	 * Returns TestCase object
	 * 
	 * @param params map containing parameters
	 * 
	 * @param testName
	 *            name of TestCase
	 * @param folderPath
	 *            folder path for test case
	 * @return object of TestCase
	 * @throws Exception
	 */
	public static TestCase deserialize(Map params, String testName,
			String folderPath) {

		return new DesignerDeserializer(params, testName, folderPath)
				.deserialize();
	}

	private DesignerDeserializer(Map params, String testName,
			String folderPath) {
		info.put("params", params);
		info.put("testName", testName);
		info.put("folderPath", folderPath);
	}

	private TestCase deserialize() {
		String jsonString = MongoDBUtils.getInstance().makeMongoConnection(info);
		return getTestCase(jsonString);
	}

	private TestCase getTestCase(String str) {
		str = str.replaceAll("=>", ":");
		str = str.replaceAll("nil", "null");
		
		JSONObject jsonObj=JsonUtils.getInstance().parseStringToJson(str);
		jsonObj=(JSONObject) jsonObj.get("ok");
		final String sName = (String) ((JSONObject) jsonObj
				.get("attributes")).get("name");

		final JSONArray itrCnt = (JSONArray) jsonObj.get("iterations");
		final List<TestIteration> iterations = makeTestIterations(itrCnt);
		return new TestCase() {

			public List<Map> variables() {
				return null;
			}

			public String name() {
				return sName;
			}

			public Map masterReferences() {
				return null;
			}

			public int iterationCount() {
				return itrCnt.size();
			}

			public TestIteration iteration(int iterationIx) {
				return iterations.get(iterationIx);
			}
		};

	}

	private List<TestIteration> makeTestIterations(JSONArray itrCnt) {
		List<TestIteration> iterations = new ArrayList<TestIteration>();
		Iterator itr = itrCnt.iterator();
		while (itr.hasNext()) {
			iterations.add(makeTestIteration((JSONObject) itr.next()));
		}
		return iterations;
	}

	private TestIteration makeTestIteration(JSONObject innerItr) {
		
		final JSONArray stpCnt = (JSONArray) innerItr.get("steps");
		final List<TestStep> steps = makeTestSteps(stpCnt);
		return new TestIteration() {

			public int stepCount() {
				return stpCnt.size();
			}

			public TestStep step(int stepIx) {
				return steps.get(stepIx);
			}

			public Map parameters() {
				return null;
			}
		};
	}

	private List<TestStep> makeTestSteps(JSONArray stpCnt) {
		List<TestStep> steps = new ArrayList<TestStep>();
		Iterator itr = stpCnt.iterator();
		while (itr.hasNext()) {
			steps.add(makeTestStep((JSONObject) itr.next()));
		}
		return steps;
	}

	private TestStep makeTestStep(final JSONObject innerStep) {

//		JSONObject component = (JSONObject) innerStep.get("action");
//		final JSONObject conditional = (JSONObject) (((JSONArray) component
//				.get("parameters")).get(0));
		final HashMap<String, String> param = new HashMap<String, String>();

		JSONObject parameters = (JSONObject) innerStep
				.get("expandedParameters");

		final String moduleCode=(String) innerStep.get("moduleCode");
		final String componentCode=(String) innerStep.get("componentCode");
		if (moduleCode.equals("") && componentCode.equals("")) {
			logger
			.handleError("Blank module and component codes in test file;\n step skipped at: ");
			return null;
		}
		if (parameters != null) {
			java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>() {
			}.getType();
			Gson gson = new Gson();
			Map<String, Object> map = gson.fromJson(parameters.toString(),
					mapType);
			ArrayList<String> keySet = new ArrayList<String>(map.keySet());

			for (String key : keySet) {
				String value = (String) map.get(key);
				if(value!=null)
					param.put(key, value);
				else
					param.put(key, "");
			}
		}

		return new TestStep() {

			public String stepName() {
				return (String) innerStep.get("masterstepIx");
			}

			public String moduleCode() {
				return moduleCode;
			}

			public boolean failTestIfUnexpected() {
				return (Boolean) innerStep.get("failTestIfUnexpected");
			}

			public DataRow componentParameters() {
				return new DataRow(param);
			}

			public String componentOutputValidation() {
				return null;
			}

			public String componentCode() {
				return componentCode;
			}

			public boolean abortTestIfUnexpected() {
				return (Boolean) innerStep.get("abortTestIfUnexpected");
			}
		};
	}

	private LogUtils logger = new LogUtils(this);
	private Map<String, Object> info = new HashMap<String, Object>();
	
	/**
	 * Overloaded toString() method to return DesignerDeserializer format String
	 */
	public String toString() {
		return "DesignerDeserializer()" ;
	}
	
	
}