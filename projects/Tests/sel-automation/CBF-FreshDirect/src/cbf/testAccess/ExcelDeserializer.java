/******************************************************************************
$Id : ExcelDeserializer.java 9/8/2014 1:22:08 PM
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cbf.engine.DataAccess;
import cbf.engine.TestCase;
import cbf.engine.TestIteration;
import cbf.engine.TestStep;
import cbf.harness.Harness;
import cbf.utils.Configuration;
import cbf.utils.DTAccess;
import cbf.utils.DataRow;
import cbf.utils.FileUtils;
import cbf.utils.LogUtils;
import cbf.utils.StringUtils;
import cbf.utils.Substitutor;
import cbf.utils.Utils;

/**
 * 
 * Extends Utils class and generates ExcelTestCase
 * 
 */
public class ExcelDeserializer extends Utils {

	/**
	 * Returns TestCase object
	 * 
	 * @param dtAccess
	 *            object of DataAccess
	 * @param testName
	 *            name of TestCase
	 * @param serializedFileName
	 *            name of TestCase file
	 * @return object of TestCase
	 */
	public static TestCase deserialize(DataAccess dtAccess, String testName,
			String serializedFileName) {
		if (!FileUtils.fileExists(serializedFileName)) {
			 logger.handleError("File does not exist : ", serializedFileName);
		}
		return new ExcelDeserializer(dtAccess, testName, serializedFileName)
		.deserialize();
	}

	/**
	 * Returns ExcelDeserializer with test name, serialized file name format string
	 */
	public String toString() {
		return "ExcelDeserializer()" + "(" + testName + " - " + sSerializedFileName
		+ ")";
	}

	private ExcelDeserializer(DataAccess dtAccess, String testName,
			String serializedFileName) {
		this.dtAccess = dtAccess;
		this.testName = testName;
		this.sSerializedFileName = serializedFileName;

		// FIXME: Handle
		this.tcDataAccess = new DTAccess(serializedFileName);
	}

	private ExcelDeserializer() {
	}

	private TestCase deserialize() {
		final Map references;
		if (tcDataAccess.isSheetExists(SHN_REFERENCES)) {
			List<Map> refRows = tcDataAccess.readSheet(SHN_REFERENCES);
			references = makeReferences(refRows);
		} else {
			references = null;
		}
		List<Map> vars = new ArrayList<Map>();
		if (tcDataAccess.isSheetExists(SHN_VARIABLES)) {
			vars = tcDataAccess.readSheet(SHN_VARIABLES);
		}
		if (references != null && !(vars.isEmpty())) {
			for (Map variable : vars) {
				if (references.containsKey(variable.get("name"))) {
					logger.handleError("Clash in variable and reference names:"
							+ variable.get("name"));
				}
			}
		}
		final List<Map> variables = vars;
		// fetch iteration rows
		List<Map> iterRows = null;
		if (tcDataAccess.isSheetExists(SHN_ITERATIONS)) {
			iterRows = tcDataAccess.readSheet(SHN_ITERATIONS);

			iterRows = substitute(iterRows, references);
						int rowSize = iterRows.size();
			int count=0;
			for (Map iterRow : iterRows)
			{
			boolean isSelected = true;
			if (!iterRow.isEmpty()) {
				isSelected = s2B((String) GCONFIG.get("EnableIterationSelection"), true);
				if (isSelected) {
					isSelected = s2B((String) iterRow.get("_selectYN"), true);
				}
				if(!isSelected)
				{
					count++;
				}
			}	
			}

			if(count == rowSize)
			{
				iterRows = new ArrayList<Map>();
				iterRows.add(new HashMap());
			}

//			if (iterRows.size() == 0) {
//				logger.handleError("Should have at least 1 iteration, if the "
//						+ SHN_ITERATIONS + " sheet is used");
//				return null;
//			}

		} else {
			iterRows = new ArrayList<Map>();
			iterRows.add(new HashMap());
		}

		List<Map> stepRows = tcDataAccess.readSheet(SHN_STEPS);
		final String sName = this.testName;
		stepRows = substitute(stepRows, references);

		final List<TestIteration> iterations = makeIterations(iterRows,
				references, stepRows);
		if (iterations.size() == 0) {
			logger.handleError("No iterations are selected from the "
					+ SHN_ITERATIONS + " sheet");
			return null;
		}

		return new TestCase() {
			public TestIteration iteration(int iterationIx) {
				return iterations.get(iterationIx);
			}

			public int iterationCount() {
				return iterations.size();
			}

			public Map masterReferences() {
				return references;
			}

			public String name() {
				return sName;
			}

			public List<Map> variables() {
				return variables;
			}

		};
	}

	private List<TestIteration> makeIterations(List<Map> iterRows,
			Map references, List<Map> stepRows) {
		List<TestIteration> iterations = new ArrayList();
		boolean isSelected;
		for (Map iterRow : iterRows) {
			isSelected = true;
			if (!iterRow.isEmpty()) {
				isSelected = s2B((String) GCONFIG.get("EnableIterationSelection"), true);
				if (isSelected) {
					// ** isSelected = s2B((String) iterRow.get("_selectYN"),
					// true);
					isSelected = s2B((String) iterRow.get("_selectYN"), true);

				}
			}
			if (isSelected) {
				TestIteration iteration = makeIteration(iterRow, references,
						stepRows);
				iterations.add(iteration);
			}
		}
		return iterations;
	}

	private TestIteration makeIteration(final Map iterRow, Map references,
			List<Map> stepRows) {
		Map iterParamsMap = new HashMap();
		if (iterRow != null) {

			iterParamsMap.put("ITERATION", iterRow);
		}
		List<Map> iterStepRows = substitute(stepRows, iterParamsMap);
		final List<TestStep> steps = new ArrayList<TestStep>();
		int stepIx = 0;
		for (Map stepRow : iterStepRows) {
			boolean isSelected = true;
			if (Utils.string2Bool((String) GCONFIG.get("EnableStepSelection"))) {

				isSelected = s2B((String) stepRow.get("_selectYN"), true);
			}
			TestStep oneStep = null;
			if (isSelected) {
				oneStep = makeTestStep(stepRow, stepIx, iterParamsMap,
						references);
			}
			if (oneStep == null) {
				logger.trace("Step row skipped:"
						+ StringUtils.mapToString(stepRow).replace(",", ";") + ":#" + stepIx);
			} else {
				steps.add(oneStep);
				logger.trace("Step added:" + StringUtils.mapToString(stepRow).replace(",", ";")
						+ ":#" + stepIx);
			}
		}
		return new TestIteration() {
			public Map parameters() {
				return iterRow;
			}

			public TestStep step(int stepIx) {
				return steps.get(stepIx);
			}

			public int stepCount() {
				return steps.size();
			}
		};

	}

	private boolean s2B(String s, boolean defb) {
		if (s == null || "".equals(s))
			return defb;
		return Utils.string2Bool(s);
	}

	private TestStep makeTestStep(Map stepRow, int iCount, Map iterParamsMap,
			Map references) {
		final String stepName, moduleCode, componentCode, componentOutputValidation;
		final DataRow componentParameters;
		Map paramSpec = null;
		if (Utils.string2Bool((String) GCONFIG.get("EnableStepSelection"))) {

			boolean isExpected;
			isExpected = s2B((String) stepRow.get("_selectYN"), true);
			if (!isExpected)
				logger.trace("Test Step Skipped!!");
			else {


				moduleCode = (String) stepRow.get("moduleCode");
				componentCode = (String) stepRow.get("componentCode");
				if (moduleCode.equals("") && componentCode.equals("")) {
					logger
					.handleError("Blank module and component codes in test file;\n step skipped at: "
							+ (iCount + 1));
					return null;
				}
				String name = (String) stepRow.get("stepName");
				if (name == null || name == "") {
					name = componentCode;
				}
				stepName = name;
				componentParameters = (DataRow) unionOfMaps(new Map[] {
						resolveOfflineRowId(moduleCode, componentCode, stepRow
								.get("offlineRowId") == null ? ""
										: (String) stepRow.get("offlineRowId")),
										resolveInlineRowId(moduleCode, componentCode, stepRow
												.get("inlineRowId") == null ? ""
														: (String) stepRow.get("inlineRowId"),
														iterParamsMap, references),
														resolveInlineParamValues(
																stepRow.get("parameters") == null ? ""
																		: (String) stepRow.get("parameters"),
																		iterParamsMap, references) });
				final boolean bFailTest = Boolean.parseBoolean((String) stepRow
						.get("failTestIfUnexpected"));

				final boolean bAbortTest = Boolean
				.parseBoolean((String) stepRow
						.get("abortTestIfUnexpected"));

				componentOutputValidation = (String) stepRow
				.get("componentOutputValidation");

				return new TestStep() {
					public boolean abortTestIfUnexpected() {
						return bAbortTest;
					}

					public String componentCode() {
						return componentCode;
					}

					public String componentOutputValidation() {
						return componentOutputValidation;
					}

					public DataRow componentParameters() {
						return componentParameters;
					}

					public boolean failTestIfUnexpected() {
						return bFailTest;
					}

					public String moduleCode() {
						return moduleCode;
					}

					public String stepName() {
						return stepName;
					}

				};
			}
		}
		// }
		return null;
	}

	private String checkRows(List<?> rows) {
		if (rows == null) {
			return "Table not found";
		}
		switch (rows.size()) {
		case 0:
			return "No rows found";
		case 1:
			return "";
		default:
			return "Multiple rows found " + rows.size();
		}
	}

	private Map getModuleDataRow(String moduleCode, String componentCode,
			String rowSelector) {
		dtAccess.getModuleAccess(moduleCode);
		List<Map> rows = dtAccess.selectRows(componentCode, rowSelector);
		String sMsg = checkRows(rows);
		if (!(sMsg == null) && !(sMsg.equals(""))) {
			logger.handleError("Failed to get module data row:"
					+ StringUtils.toString(new String[] { moduleCode,
							componentCode, rowSelector }) + ":" + sMsg);
			return null;
		}
		return rows.get(0);
	}

	private Map makeReferences(List<?> refRows) {
		Map oRefs = new HashMap();
		for (int ix = 0; ix < refRows.size(); ix++) {
			Map row = (Map) refRows.get(ix);
			Map masterRow = getModuleDataRow("Master", (String) row
					.get("masterTableName"), (String) row.get("rowId"));
			oRefs.put((String) row.get("name"), masterRow);
		}
		return oRefs;
	}

	private Map<String, Map> moduleReferences = new HashMap<String, Map>();

	private Map makeModuleReferences(String sModuleCode) {

		Map references = null;
		if (moduleReferences.containsKey(sModuleCode)) {
			return moduleReferences.get(sModuleCode);
			// Need to discuss
		}
		dtAccess.getModuleAccess(sModuleCode);
		List<Map> refRows = dtAccess.readRows("References");
		if (refRows != null) {
			references = makeReferences(refRows);
		}
		moduleReferences.put(sModuleCode, references);
		return references;
	}

	private Map resolveOfflineRowId(String moduleCode, String componentCode,
			String rowSelector) {
		if (rowSelector.equals("")) {
			return null;
		}
		Map row = new HashMap();
		row = getModuleDataRow(moduleCode, componentCode, rowSelector);
		if (row != null) {
			row = substitute(row, makeModuleReferences(moduleCode));
			return row;
		}
		logger.handleError("Error in resolving offline row reference:"
				+ StringUtils.arrayToString(new String[] { moduleCode,
						componentCode, rowSelector }));
		return row;
	}

	private Map resolveInlineRowId(String moduleCode, String componentCode,
			String rowSel, Map iterParamsMap, Map references) {
		if (rowSel.equals("")) {
			return null;
		}
		List<Map> rows = null;
		String[] sheetNames = { moduleCode + "-" + componentCode, componentCode };
		for (String sheetName : sheetNames) {
			if (tcDataAccess.isSheetExists(sheetName)) {
				rows = tcDataAccess.readSelectedRows(sheetName, rowSel);
				String sMsg = checkRows(rows);
				if (!(sMsg.equals(""))) {
					logger.handleError("Error in inline row selection:"
							+ rowSel + ":" + sMsg);
					break;
				}
			}
		}
		if (rows == null) {
			logger.handleError("Could not resolve inline data ref:" + rowSel);
			return null;
		}
		return substitute(substitute(rows.get(0), iterParamsMap), references);
	}

	private List<Map> substitute(List<?> stepRow, Map substitutions) {
		List<Map> result = (List<Map>) stepRow;
		if (substitutions != null)
			result = new Substitutor(substitutions).substitute(stepRow);
		return result;
	}

	private Map substitute(Map stepRow, Map substitutions) {
		Map result = stepRow;
		if (substitutions != null)
			result = new Substitutor(substitutions).substitute(stepRow);
		return result;
	}

	/*
	 * // retain it until actual implementation of substitute private Map
	 * convertDataRowToMap(DataRow o) { Map map = new HashMap();
	 * Collection<String> keys = o.keySet(); for (String key : keys) {
	 * map.put(key, (String) o.get(key)); } return map;
	 * 
	 * }
	 */

	private Map resolveInlineParamValues(String paramValueString,
			Map iterParamsMap, Map references) {
		Map params = new HashMap();
		if (paramValueString.equals("")) {
			return null;
		}
		Pattern pattern = Pattern.compile("([^=]*)=([^|]*)\\|?");
		Matcher m = pattern.matcher(paramValueString);
		while (m.find()) {
			params.put(m.group(1).trim(), m.group(2).trim());
		}
		params = substitute(params, iterParamsMap);
		params = substitute(params, references);
		return params;
	}

	private DataRow unionOfMaps(Map[] mapArray) {
		DataRow union = new DataRow();
		for (int ix = 0; ix < mapArray.length; ix++) {
			Map map = mapArray[ix];
			if (map != null && !map.isEmpty()) {
				Collection<String> keys = map.keySet();
				for (String key : keys) {
					String value = (String) map.get(key);
					/*
					 * switch (value) { case "": break;
					 * 
					 * default: break; }
					 */
					if (value.equals("_BLANK")) {
						union.put(key, "");
					} else if (value.equals("_NA")) {
						if (union.containsKey(key)) {
							union.remove(key);
						}
					} else {
						union.put(key, value);
					}
				}
			}
		}
		return union;
	}

	DataAccess dtAccess;
	DTAccess tcDataAccess;
	String testName, sSerializedFileName;

	private final Configuration GCONFIG = Harness.GCONFIG;
	private final String SHN_STEPS = "Steps", SHN_REFERENCES = "References",
	SHN_ITERATIONS = "Iterations", SHN_VARIABLES = "Variables";
	private static LogUtils logger = new LogUtils(new ExcelDeserializer());


}
