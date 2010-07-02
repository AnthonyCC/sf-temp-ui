package com.freshdirect.dataloader.cool;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ejb.ErpCOOLManagerHome;
import com.freshdirect.erp.ejb.ErpCOOLManagerSB;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.sap.mw.jco.JCO;

public class BapiErpsCOOLChange implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance( BapiErpsCOOLChange.class );

	private final static long TIMEOUT = 5 * 60 * 1000; 

	static JCO.MetaData smeta = null;
	static int totalSize = 0;
	static {
		DataStructure[] sapData = new DataStructure[] {
						    //NAME      JCO_TYPE     LEN DEC DESCRIPTION
			new DataStructure("MATNR", JCO.TYPE_CHAR, 18, 0, "Material number"),
			new DataStructure("MAKTX", JCO.TYPE_CHAR, 40, 0, "Material description"),
			new DataStructure("COUNTRY1", JCO.TYPE_CHAR, 15, 0, "COUNTRY1"),
			new DataStructure("COUNTRY2", JCO.TYPE_CHAR, 15, 0, "COUNTRY2"),
			new DataStructure("COUNTRY3", JCO.TYPE_CHAR, 15, 0, "COUNTRY3"),
			new DataStructure("COUNTRY4", JCO.TYPE_CHAR, 15, 0, "COUNTRY4"),
			new DataStructure("COUNTRY5", JCO.TYPE_CHAR, 15, 0, "COUNTRY5"),
			};
		smeta = new JCO.MetaData("COOLDETAILS");//Structure Name
		for (DataStructure element : sapData) {
			smeta.addInfo(element.fieldName, element.type, element.length, totalSize, element.decimal);
			totalSize += element.length;
		}
	}

	public JCO.MetaData[] getStructureMetaData() {
		return new JCO.MetaData[] { smeta };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData(ErpServicesProperties.getCOOLInfoFunctionName());//BAPI Name
		fmeta.addInfo("COUNTRY",	JCO.TYPE_TABLE,	totalSize, 0, 0, 0,	"COOLDETAILS");//
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("BapiErpsCOOLChange execute invoked");
		JCO.Table coolTable = tables.getTable("COUNTRY");

		coolTable.firstRow();

		// build COOL Info
		List<ErpCOOLInfo> erpCOOLInfoList = new ArrayList<ErpCOOLInfo>();
		ErpCOOLInfo erpCOOLInfo = null;
		for (int i = 0; i < coolTable.getNumRows(); i++) {
			
			
			// First retrieve all data from current row.
			String matNum = coolTable.getString("MATNR").trim();
			String matDesc = coolTable.getString("MAKTX").trim();
			
			String country1 = coolTable.getString("COUNTRY1").trim();
			String country2 = coolTable.getString("COUNTRY2").trim();
			String country3 = coolTable.getString("COUNTRY3").trim();
			String country4 = coolTable.getString("COUNTRY4").trim();
			String country5 = coolTable.getString("COUNTRY5").trim();
			List<String> countryInfo=getCountryInfo(country1,country2,country3,country4,country5);
			erpCOOLInfo=new ErpCOOLInfo(matNum,matDesc,countryInfo);
			erpCOOLInfoList.add(erpCOOLInfo);
			
			LOGGER.debug(erpCOOLInfo);			
            coolTable.nextRow();
		}

		try {
			LOGGER.debug("Storing COOL info");

			this.storeCOOLInfo(erpCOOLInfoList);
			LOGGER.debug("Stored COOL info");
			output.setValue("S", "RETURN");
			output.setValue("All good...", "MESSAGE");

		} catch (Exception ex) {
			LOGGER.warn("Failed to store COOL info", ex);

			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
		}
	}

	private void storeCOOLInfo(List<ErpCOOLInfo> erpCOOLInfoList) {
		Context ctx = null;
		
		try {
			ctx = ErpServicesProperties.getInitialContext();
			ErpCOOLManagerHome mgr = (ErpCOOLManagerHome)ctx.lookup(ErpServicesProperties.getCOOLManagerHome());//  (ErpCOOLManagerHome) ctx.lookup("freshdirect.erp.COOLManager");
			ErpCOOLManagerSB sb = mgr.create();
			sb.updateCOOLInfo(erpCOOLInfoList);
			
		} catch(Exception ex) {
			throw new EJBException("Failed to store COOL info " + ex.toString());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				}
			}
		}
	}

	private List<String> getCountryInfo(String country1, String country2, String country3, String country4, String country5) {
		
		
		List<String> countryInfo=new ArrayList<String>(3);
		
		
		if(!StringUtil.isEmpty(country1))
			countryInfo.add(country1);
		if(!StringUtil.isEmpty(country2)&&!StringUtil.isEmpty(country1))
			countryInfo.add(country2);
		if(!StringUtil.isEmpty(country3)&&!StringUtil.isEmpty(country2)&&!StringUtil.isEmpty(country1))
			countryInfo.add(country3);
		if(!StringUtil.isEmpty(country4)&&!StringUtil.isEmpty(country3)&&!StringUtil.isEmpty(country2)&&!StringUtil.isEmpty(country1))
			countryInfo.add(country4);
		if(!StringUtil.isEmpty(country5)&&!StringUtil.isEmpty(country4)&&!StringUtil.isEmpty(country3)&&!StringUtil.isEmpty(country2)&&!StringUtil.isEmpty(country1))
			countryInfo.add(country5);
		return countryInfo;
	}
}


class DataStructure {
	String fieldName;
	int type;
	int length;
	int decimal;
	String description;
	DataStructure (String fieldName,
				   int type,
				   int length,
				   int decimal,
				   String description) {
		this.fieldName = fieldName;
		this.type = type;
		this.length = length;
		this.decimal = decimal;
		this.description = description;
	}
}