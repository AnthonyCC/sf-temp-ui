package cbf.plugin;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import cbf.harness.ResourcePaths;
import cbf.utils.Configuration;
import cbf.utils.LogUtils;

/**
 * Manages all the plugins related functions
 */
public class PluginManager {

	/*
	 * Set by Harness directly prior to using it TODO: refine bootstrapping
	 */

	public static String masterConfigFileName;

	/**
	 * Returns instance of PluginManager
	 * 
	 * @param masterConfigFileName
	 *            name of Master Config file
	 * @return PluginManager instance
	 */
	public static PluginManager getInstance() {
		if (singleton == null) {
			masterConfigFileName = ResourcePaths.singleton.getFrameworkResource(
					"Resources", "MasterConfig.xml");
			singleton = new PluginManager(masterConfigFileName);
		}
		return singleton;
	}

	/**
	 * Returns instance of specified Plugin
	 * 
	 * @param usageMap
	 *            parameters of plugin
	 * @return instance of Plugin
	 */

	public static Object getPlugin(Map usageMap) {
		try {
			return getPlugin((String) usageMap.get("plugin"), (Map) usageMap
					.get("parameters"));
		} catch (ClassCastException e) {
			getInstance().logger.handleError(
					"Parameters are not proper in user config file ", e);
		}
		return null;
	}

	/**
	 * Returns instance of specified Plugin
	 * 
	 * @param pluginName
	 *            of Plugin
	 * @param usageParams
	 *            parameters of plugin
	 * @return instance of Plugin
	 */
	public static Object getPlugin(String pluginName,
			Map<String, Object> usageParams) {
		return getInstance().parsePlugin(pluginName, usageParams);
	}

	/**
	 * Returns PluginManager format string
	 */
	public String toString() {
		return "PluginManager()";
	}

	private PluginManager(String fileName) {
		try {
			mconfig = new Configuration(fileName);
		} catch (FileNotFoundException e) {
			logger.handleError("File not exist ", fileName, e);
		}
	}

	private Object parsePlugin(String pluginName,
			Map<String, Object> usageParams) {
		String className = null;
		Map<String, Object> masterParamsMap = new HashMap<String, Object>();

		try {
			Map<String, Object> masterPluginMap = (Map<String, Object>) mconfig
					.get(pluginName);
			className = (String) masterPluginMap.get("classname");

			masterParamsMap = (Map<String, Object>) masterPluginMap
					.get("parameters");

		} catch (ClassCastException e) {
			logger.handleError("Class Cast Exception ", e);
		}

		Map<String, Object> finalMap = mergeMaps(masterParamsMap, usageParams);

		for (String key : finalMap.keySet()) {
			if (finalMap.get(key).equals("TBD")) {
				logger.handleError(key
						+ " value must be specified in user config for ",
						pluginName);
			}
		}

		return initializePlugin(className, finalMap);
	}

	private Object initializePlugin(String className, Map finalMap) {
		try {
			return Class.forName(className).getDeclaredConstructor(Map.class)
					.newInstance(finalMap);

		} catch (ClassNotFoundException e) {
			logger.handleError("Class not found ", className, e);
		} catch (NoSuchMethodException e) {
			logger.handleError("Method not found ", className, e);
		} catch (ReflectiveOperationException e) {
			logger.handleError("Class not instantiated ", className, e);
		} catch (RuntimeException e) {
			logger.handleError("Class not instantiated ", className, e);
		}
		return finalMap;
	}

	private Map<String, Object> mergeMaps(Map masterParamsMap, Map usageParams) {
		Map<String, Object> finalMap;
		if (masterParamsMap == null)
			finalMap = usageParams;

		else {
			finalMap = masterParamsMap;
			if (usageParams != null)
				finalMap.putAll(usageParams);
		}
		return finalMap;
	}

	private static PluginManager singleton = null;
	private LogUtils logger = new LogUtils(this);
	private Configuration mconfig;
}
