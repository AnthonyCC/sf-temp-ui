package com.freshdirect.dataloader.zoneinfo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import weblogic.auddi.util.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumZoneServiceType;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
import com.freshdirect.customer.ErpZoneRegionZipInfo;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.freshdirect.dataloader.sap.ejb.SAPZoneInfoLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPZoneInfoLoaderSB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.sap.mw.jco.JCO;

public class BapiErpsZoneInfoContentLoader implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance( BapiErpsZoneInfoContentLoader.class );

	private final static long TIMEOUT = 5 * 60 * 1000; 

	static JCO.MetaData smeta = null;
	static int totalSize = 0;
	
	static {
		
		DataStructure[] sapData = new DataStructure[] {
			    //NAME      JCO_TYPE     LEN DEC DESCRIPTION
				new DataStructure("Z_ZONEID_ID", JCO.TYPE_CHAR, 10, 0, "Zone Id"),
				new DataStructure("Z_REGION_ID", JCO.TYPE_CHAR, 10, 0, "Region Id"),
				new DataStructure("Z_SERV_TYPE", JCO.TYPE_NUM, 2, 0, "Service Type"),
				new DataStructure("Z_ZONE_DESC", JCO.TYPE_CHAR, 40, 0, "Zone desc"),
				new DataStructure("Z_ZONE_ZIPCODE", JCO.TYPE_CHAR, 5, 0, "Material number"),
};
		
		/*
		DataStructure[] sapData1 = new DataStructure[] {					
			new DataStructure("Z_ZONEID_ID", JCO.TYPE_CHAR, 10, 0, "Zone Id"),
			new DataStructure("Z_REGION_ID", JCO.TYPE_CHAR, 10, 0, "Region Id"),
			new DataStructure("Z_SERV_TYPE", JCO.TYPE_NUM, 2, 0, "Service Type"),
			new DataStructure("Z_ZONE_DESC", JCO.TYPE_CHAR, 40, 0, "Zone desc"),
			new DataStructure("Z_ZONE_ZIPCODE", JCO.TYPE_CHAR, 5, 0, "Material number")
		}; */
		smeta = new JCO.MetaData("ZONE_INFO");
		for (DataStructure element : sapData) {
			smeta.addInfo(element.fieldName, element.type, element.length, totalSize, element.decimal);
			totalSize += element.length;
		}
	}

	public JCO.MetaData[] getStructureMetaData() {
		return new JCO.MetaData[] { smeta };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("Z_BAPI_ZONE_INFO");
		fmeta.addInfo("ZSD_ZONE_INFO",	JCO.TYPE_TABLE,	totalSize, 0, 0, 0,	"ZONE_INFO");
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("BapiErpsCartonContent execute invoked");
		JCO.Table cartonTable = tables.getTable("ZSD_ZONE_INFO");

		cartonTable.firstRow();

		// build carton details

		List<ErpZoneMasterInfo> zoneInfos = new ArrayList<ErpZoneMasterInfo>();
	
		for (int i = 0; i < cartonTable.getNumRows(); i++) {
			
			// First retrieve all data from current row.
			String zoneId = cartonTable.getString("Z_ZONEID_ID").trim();
			String regionId = cartonTable.getString("Z_REGION_ID").trim();			
			String serviceType = cartonTable.getString("Z_SERV_TYPE").trim();
			String zoneDesc = cartonTable.getString("Z_ZONE_DESC").trim();			
			String zipCode = cartonTable.getString("Z_ZONE_ZIPCODE").trim();

			 System.out.println("zoneId :"+zoneId);
			 System.out.println("regionId :"+regionId);
			 System.out.println("serviceType :"+serviceType);
			 System.out.println("zoneDesc :"+zoneDesc);
			 System.out.println("zipCode :"+zipCode);
			try {
				constructZoneInfoModel(zoneId, regionId, serviceType, zoneDesc, zipCode, zoneInfos);
			} catch (LoaderException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
				LOGGER.warn("Failed to store wave info", ex);
				String errorMsg = ex.toString();
				errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
				LOGGER.info("Error message to SAP: '" + errorMsg + "'");
				output.setValue("E", "RETURN");
				output.setValue(errorMsg, "MESSAGE");
				return;
			} 
            //zoneInfos.add(zoneDetail); 			 
			//cartonDetails.add(detail);
			
			cartonTable.nextRow();
		}

		try {
			LOGGER.debug("Storing zone content info");

			this.storeZoneInfo(zoneInfos);

			output.setValue("S", "RETURN");
			output.setValue("All good...", "MESSAGE");

		} catch (Exception ex) {
			LOGGER.warn("Failed to store wave info", ex);

			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
		}
	}
	
	
	
	public void constructZoneInfoModel(String zoneId,String regionId,String serviceType,String desc,String zipCode, List<ErpZoneMasterInfo> zoneInfos ) throws LoaderException {
		ErpZoneMasterInfo zone=null;
		ErpZoneRegionInfo region=null;
		ErpZoneRegionZipInfo zipInfo=null;
		List erpZoneRegionZipList=null;
		try{
			
			for(int i=0;i<zoneInfos.size();i++){
				ErpZoneMasterInfo masterInfo=zoneInfos.get(i);
				if(masterInfo.getSapId().equalsIgnoreCase(zoneId))
				{
					zone=masterInfo;
					region=masterInfo.getRegion();
					erpZoneRegionZipList=region.getZoneRegionZipList();
					break;
				}				
			}
			if(region==null){
				region= new ErpZoneRegionInfo(regionId,null);	
			}
			if(erpZoneRegionZipList==null && (zipCode!=null && zipCode.trim().length()>0)){
				erpZoneRegionZipList=new ArrayList();
				region.setZoneRegionZipList(erpZoneRegionZipList);
			}
			
			if(zipCode!=null && zipCode.trim().length()>0){
				zipInfo = new ErpZoneRegionZipInfo(region,zipCode);
				region.addZoneRegionZipModel(zipInfo);
			}	
			EnumZoneServiceType enumServType=EnumZoneServiceType.getEnumByCode(serviceType);
			if(zone==null){
			    zone = new ErpZoneMasterInfo(zoneId,region,enumServType,desc);
			    zoneInfos.add(zone);
			}    
			
		}catch(Exception e){
		   Logger.error(e);		
		   throw new LoaderException(e, "Unable to complete a failed batch. Error in constructing ZoneInfoModel: "+e.getMessage());
		}
		
	}
	

	private void storeZoneInfo(List<ErpZoneMasterInfo> zoneInfos) throws NamingException, EJBException, CreateException, FinderException, FDResourceException, RemoteException {
		Context ctx = null;
		String saleId = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			SAPZoneInfoLoaderHome mgr = (SAPZoneInfoLoaderHome) ctx.lookup("freshdirect.dataloader.SAPZoneInfoLoader");
			SAPZoneInfoLoaderSB sb = mgr.create();
	        sb.loadData(zoneInfos);			 
			
		} catch(Exception ex) {
			throw new EJBException("Failed to store: " + saleId + "Msg: " + ex.toString());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				}
			}
		}
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
