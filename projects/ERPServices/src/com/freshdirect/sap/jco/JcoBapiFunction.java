/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.bapi.BapiAbapException;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiFunctionI;
import com.freshdirect.sap.bapi.BapiInfo;
import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
abstract class JcoBapiFunction implements BapiFunctionI {

	private static Category LOGGER = LoggerFactory.getInstance(JcoBapiFunction.class);

	protected JCO.Function function;

	private boolean finished = false;
	private BapiInfo[] bapiInfos;

	public JcoBapiFunction(String functionName) {
		IFunctionTemplate ftemplate = JcoManager.getInstance().getFunctionTemplate(functionName);
		this.function = new JCO.Function(ftemplate);
	}

	public final void execute() throws BapiException {
		try {
			JcoManager manager = JcoManager.getInstance();

			LOGGER.debug("Executing BAPI " + this.function.getName());

			long startTime = System.currentTimeMillis();

			manager.dump(this.function, "jco_" + startTime + "_in.html");

			JCO.Client sapClient = null;
			try {
				sapClient = manager.getClient();
				if (sapClient == null) {
					throw new BapiException("No available connections in JCO pool");
				}
				sapClient.execute(this.function);

				manager.dump(this.function, "jco_" + startTime + "_out.html");

			} catch (JCO.AbapException ex) {
				throw new BapiAbapException("BAPI call failed in SAP (" + ex + ")");

			} catch (JCO.Exception ex) {
				throw new BapiException("BAPI call communication failure (" + ex + ")");

			} finally {
				if (sapClient != null) {
					JCO.releaseClient(sapClient);
				}
			}

			long endTime = System.currentTimeMillis();

			LOGGER.debug("BAPI call " + this.function.getName() + " completed in " + (endTime - startTime) + " milliseconds");

			this.processResponse();
		} finally {
			this.finished = true;
		}
	}

	public boolean isFinished() {
		return finished;
	}

	public BapiInfo[] getInfos() {
		return this.bapiInfos;
	}

	protected void processResponse() throws BapiException {
		List retList = new ArrayList();

		if (function.getExportParameterList() != null && function.getExportParameterList().hasField("RETURN")) {
			// get "return" as a struct
			JCO.Structure ret = function.getExportParameterList().getStructure("RETURN");
			retList.add(this.convertReturn(ret));

		} else if (function.getTableParameterList() != null && function.getTableParameterList().hasField("RETURN")) {
			// get "return" as a table
			JCO.Table ret = function.getTableParameterList().getTable("RETURN");
			ret.firstRow();
			do {
				retList.add(this.convertReturn(ret));
			} while (ret.nextRow());
		}

		this.bapiInfos = (BapiInfo[]) retList.toArray(new BapiInfo[0]);
	}

	private BapiInfo convertReturn(JCO.Record ret) {
		String code;
		if (ret.hasField("ID")) {
			code = ret.getString("ID") + "/" + ret.getString("NUMBER");
		} else {
			code = ret.getString("CODE");
		}
		return new BapiInfo(
			ret.getString("TYPE"),
			code,
			ret.getString("LOG_NO"),
			ret.getString("LOG_MSG_NO"),
			ret.getString("MESSAGE")
				+ ","
				+ ret.getString("MESSAGE_V1")
				+ ","
				+ ret.getString("MESSAGE_V2")
				+ ","
				+ ret.getString("MESSAGE_V3")
				+ ","
				+ ret.getString("MESSAGE_V4"));
	}

}