package com.freshdirect.sap.jco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.bapi.BapiAbapException;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiFunctionI;
import com.freshdirect.sap.bapi.BapiInfo;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

/**
 * @author kkanuganti
 *
 */
@SuppressWarnings("unchecked")
abstract class JcoBapiFunction implements BapiFunctionI {

	private static Category LOG = LoggerFactory.getInstance(JcoBapiFunction.class);

	private static final String FUNCTIONTEMPLATE_CACHE_KEY = "_functiontemplate";

	private static Map cache;

	protected JCoFunction function;
	
	private boolean finished = false;
	
	private BapiInfo[] bapiInfos;

	static
	{
		cache = Collections.synchronizedMap(new HashMap());
	}
	
	public JcoBapiFunction(String functionName) throws JCoException
	{
		this.function = getFunction(functionName);
	}

	public final void execute() throws BapiException
	{
		try
		{
			JcoManager manager = JcoManager.getInstance();

			
			LOG.debug("Executing BAPI " + this.function.getName());

			long startTime = System.currentTimeMillis();

			manager.dumpToXML(this.function, "jco_" +this.function.getName()+ startTime + "_in.xml");
			
			try
			{
				function.execute(manager.getDestination());

				manager.dumpToXML(this.function, "jco_" +this.function.getName()+ startTime + "_out.xml");
			} 
			catch (AbapException ex)
			{
				throw new BapiAbapException("BAPI call failed in SAP (" + ex + ")");
			} 
			catch (JCoException ex)
			{
				throw new BapiException("BAPI call communication failure (" + ex + ")");
			}

			long endTime = System.currentTimeMillis();

			LOG.debug("BAPI call " + this.function.getName() + " completed in " + (endTime - startTime) + " milliseconds");
			this.processResponse();
			
		}
		catch (JCoException ex) 
		{
			throw new BapiException("BAPI call communication failure (" + ex + ")");
		} 
		finally
		{
			this.finished = true;
		}
	}
	
	/**
	 * process response from SAP function module
	 *
	 * @throws BapiException
	 */
	protected void processResponse() throws BapiException
	{
		final List<BapiInfo> retList = new ArrayList<BapiInfo>();

		JCoStructure returnStructure = null;
		JCoTable returnTable = null;

		if (function.getExportParameterList() != null && function.getExportParameterList().getMetaData().hasField("RETURN")
				&& function.getExportParameterList().getMetaData().isStructure("RETURN"))
		{
			returnStructure = function.getExportParameterList().getStructure("RETURN");
			retList.add(this.convertReturn(returnStructure));

		}
		else if (function.getTableParameterList() != null && function.getTableParameterList().getMetaData().hasField("RETURN")
				&& function.getTableParameterList().getMetaData().isTable("RETURN"))
		{
			returnTable = function.getTableParameterList().getTable("RETURN");
			returnTable.firstRow();
			do
			{
				retList.add(this.convertReturn(returnTable));
			}
			while (returnTable.nextRow());
		}

		this.bapiInfos = (BapiInfo[]) retList.toArray(new BapiInfo[0]);
	}
	
	/**
	 *
	 * @param table
	 *           the JCoTable
	 * @return will return a String that represents the columns & values in a JCoTable
	 */
	private BapiInfo convertReturn(final JCoRecord ret)
	{
		String code;
		if (ret.getMetaData().hasField("ID"))
		{
			code = ret.getString("ID") + "/" + ret.getString("NUMBER");
		}
		
			
		
		else if (ret.getMetaData().hasField("VBELN") &&
				ret.getMetaData().hasField("VDATU") &&
				ret.getMetaData().hasField("MESSAGE")){
			return new BapiInfo(ret.getString("VBELN"), ret.getString("VDATU"), ret.getString("MESSAGE"));
		}else {
		code = ret.getString("CODE");
		}
		return new BapiInfo(ret.getString("TYPE"), code, ret.getString("LOG_NO"), ret.getString("LOG_MSG_NO"),
				ret.getString("MESSAGE") + "," + ret.getString("MESSAGE_V1") + "," + ret.getString("MESSAGE_V2") + ","
						+ ret.getString("MESSAGE_V3") + "," + ret.getString("MESSAGE_V4"));
	}
	
	/**
	 * This method will resolve the FunctionTemplate for the given name. It will search the local cache and if not found,
	 * retrieve it from SAP via the repository. It then caches that and returns it.
	 * <p/>
	 * This method NEVER returns null! An exception is thrown when the function can not be resolved.
	 *
	 * @param functionName
	 *           the name of the function to find
	 * @return the function template
	 * @throws JCoException
	 * @throws RuntimeException
	 *            when the requested function can not be found in the configured repositories
	 */
	protected JCoFunction getFunction(final String functionName) throws JCoException
	{
		final String cacheKey = functionName + FUNCTIONTEMPLATE_CACHE_KEY;
		cache = Collections.synchronizedMap(new HashMap());
		JCoFunctionTemplate ftemplate = (JCoFunctionTemplate) cache.get(cacheKey);
		if (ftemplate == null)
		{
			ftemplate = JcoManager.getInstance().getFunctionTemplate(functionName);
			if (ftemplate == null)
			{
				throw new RuntimeException(new StringBuffer("Could not find function '").append(functionName).append("'").toString());
			}
			cache.put(cacheKey, ftemplate);
		}
		return ftemplate.getFunction();
	}
	

	/**
	 * @return the bapiInfos
	 */
	@Override
	public BapiInfo[] getInfos()
	{
		return this.bapiInfos;
	}


	/**
	 * @return the isTaskComplete
	 */
	public boolean isFinished() {
		return finished;
	}
	
}