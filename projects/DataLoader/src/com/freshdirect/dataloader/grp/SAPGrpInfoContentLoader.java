package com.freshdirect.dataloader.grp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import weblogic.auddi.util.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpGrpPriceZoneModel;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.freshdirect.dataloader.sap.ejb.SAPGrpInfoLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SapGrpInfoLoaderSB;
import com.freshdirect.erp.ejb.ErpGrpInfoHome;
import com.freshdirect.erp.ejb.ErpGrpInfoSB;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.sap.mw.jco.JCO;

public class SAPGrpInfoContentLoader implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance( SAPGrpInfoContentLoader.class );

	private final static long TIMEOUT = 5 * 60 * 1000; 

	static JCO.MetaData smeta = null;
	static int totalSize = 0;
	
	static {
		
		DataStructure[] sapData = new DataStructure[] {
			    //NAME      JCO_TYPE     LEN DEC DESCRIPTION
				new DataStructure("ZMATNR", JCO.TYPE_CHAR, 18, 0, "material number"),
				new DataStructure("ZGROUP_ID", JCO.TYPE_CHAR, 10, 0, "Group Id"),
				new DataStructure("ZGROUP_SHORT_DESC", JCO.TYPE_CHAR, 30, 0, "short desc"),
				new DataStructure("ZGROUP_LONG_DESC", JCO.TYPE_CHAR, 50, 0, "long desc"),
				new DataStructure("ZZONE_ID", JCO.TYPE_CHAR, 10, 0, "Zone Id"),
				new DataStructure("ZSGRP_QTY", JCO.TYPE_CHAR, 5, 0, "Group Scale Qty"),
				new DataStructure("ZSGRP_UOM", JCO.TYPE_CHAR, 3, 0, "Group Scale UOM"),
				new DataStructure("ZSGRP_PRICE", JCO.TYPE_CHAR, 13, 0, "Group Scale Price"),
				new DataStructure("ZSGRP_SUOM", JCO.TYPE_CHAR, 3, 0, "Group Scale Price Sale UOM"),
				new DataStructure("ZSGRP_EXP_IND", JCO.TYPE_CHAR, 1, 0, "Expury Indicator"),				
		};
		
		/*
		DataStructure[] sapData1 = new DataStructure[] {					
			new DataStructure("Z_ZONEID_ID", JCO.TYPE_CHAR, 10, 0, "Zone Id"),
			new DataStructure("Z_REGION_ID", JCO.TYPE_CHAR, 10, 0, "Region Id"),
			new DataStructure("Z_SERV_TYPE", JCO.TYPE_NUM, 2, 0, "Service Type"),
			new DataStructure("Z_ZONE_DESC", JCO.TYPE_CHAR, 40, 0, "Zone desc"),
			new DataStructure("Z_ZONE_ZIPCODE", JCO.TYPE_CHAR, 5, 0, "Material number")
		}; */
		smeta = new JCO.MetaData("GRP_SCALE_PRICE");
		for(int i = 0; i < sapData.length; i++) {
			smeta.addInfo(sapData[i].fieldName, sapData[i].type, sapData[i].length, totalSize, sapData[i].decimal);
			totalSize += sapData[i].length;
		}
	}

	public JCO.MetaData[] getStructureMetaData() {
		return new JCO.MetaData[] { smeta };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("Z_BAPI_SGRP_PRICE_WEB");
		fmeta.addInfo("ZSD_BAPI_SGRP_PRICE_WEB",	JCO.TYPE_TABLE,	totalSize, 0, 0, 0,	"GRP_SCALE_PRICE");
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("BapiErpsCartonContent execute invoked");
		JCO.Table groupTable = tables.getTable("ZSD_BAPI_SGRP_PRICE_WEB");

		groupTable.firstRow();

		// build carton details

		List grpInfos = new ArrayList();
	
		for (int i = 0; i < groupTable.getNumRows(); i++) {
			
			// First retrieve all data from current row.
			String matNumber = groupTable.getString("ZMATNR").trim();
			String grpId = groupTable.getString("ZGROUP_ID").trim();			
			String shortDesc = groupTable.getString("ZGROUP_SHORT_DESC").trim();
			String LongDesc = groupTable.getString("ZGROUP_LONG_DESC").trim();			
			String zoneId = groupTable.getString("ZZONE_ID").trim();
			String grpQtyStr = groupTable.getString("ZSGRP_QTY").trim();
			String grpUOM = groupTable.getString("ZSGRP_UOM").trim();
			String grpPriceStr = groupTable.getString("ZSGRP_PRICE").trim();
			String grpSUOM = groupTable.getString("ZSGRP_SUOM").trim();
			String grpExpiryInd = groupTable.getString("ZSGRP_EXP_IND").trim();
			double  grpQty=0;
			double grpPrice=0;
			/*
			 System.out.println("matNumber :"+matNumber);
			 System.out.println("grpId :"+grpId);
			 System.out.println("shortDesc :"+shortDesc);
			 System.out.println("LongDesc :"+LongDesc);
			 System.out.println("zoneId :"+zoneId);
			 System.out.println("grpQty :"+grpQtyStr);
			 System.out.println("grpUOM :"+grpUOM);
			 System.out.println("grpPrice :"+grpPriceStr);
			 System.out.println("grpExpiryInd :"+grpExpiryInd);			 			 
			 */
			try {
				
				if(grpQtyStr!=null && grpQtyStr.trim().length()>0) grpQty =Double.parseDouble(grpQtyStr);
				
				if(grpPriceStr!=null && grpPriceStr.trim().length()>0) grpPrice =Double.parseDouble(grpPriceStr);
				//Validate if any material in the incoming message is already participating in an active group
				//validateIfMaterialAlreadyExistsInActiveGroup(grpId, matNumber);
				constructGrpInfoModel(grpId,shortDesc,LongDesc,zoneId,grpQty,grpUOM,grpPrice,"X".equalsIgnoreCase(grpExpiryInd),matNumber,grpInfos, grpSUOM);
			} catch (LoaderException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
				LOGGER.warn("Failed to store Group info", ex);
				String errorMsg = ex.toString();
				errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
				LOGGER.info("Error message to SAP: '" + errorMsg + "'");
				output.setValue("E", "RETURN");
				output.setValue(errorMsg, "MESSAGE");
				return;
			} 
            //zoneInfos.add(zoneDetail); 			 
			//cartonDetails.add(detail);
			
			groupTable.nextRow();
		}

		try {
			LOGGER.debug("Storing group content info");

			this.storeGrpInfo(grpInfos);

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
	
	private void validateIfMaterialAlreadyExistsInActiveGroup(String grpId, String matId) throws LoaderException{
		Context ctx = null;
		try{
			ctx = ErpServicesProperties.getInitialContext();
			ErpGrpInfoHome mgr = (ErpGrpInfoHome) ctx.lookup("freshdirect.erp.GrpInfoManager");
			ErpGrpInfoSB sb = mgr.create();
			FDGroup group = sb.getGroupIdentityForMaterial(matId);
			if(group != null && !group.getGroupId().equals(grpId)) {//and its does not match with current active group.
				throw new LoaderException("Material Number : "+matId+" Already exists in Active Group "+group.getGroupId());
			}
		}catch(Exception ex) {
			throw new EJBException("Failed to validate if Material Already Exists In Active Group: " + matId + "Msg: " + ex.toString());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				}
			}
		}
	}
	
	public void constructGrpInfoModel(String grpId,String shortDesc,String longDesc,String zoneId,double grpQty,String grpUOM ,double grpPrice,boolean isExpired,String matId,List grpInfos, String grpSUOM ) throws LoaderException {
		ErpGrpPriceModel grpModel=null;
		ErpGrpPriceZoneModel zoneModel=null;
		//ErpZoneRegionZipInfo zipInfo=null;
		Set matList=null;
		Set zoneSet=null;
		try{
			
			for(int i=0;i<grpInfos.size();i++){
				ErpGrpPriceModel masterInfo=(ErpGrpPriceModel)grpInfos.get(i);
				if(masterInfo.getGrpId().equalsIgnoreCase(grpId))
				{
					grpModel=masterInfo;
					zoneSet=masterInfo.getZoneModelList();
					matList=masterInfo.getMatList();
					break;
				}				
			}
			
			if(zoneSet==null){
				zoneSet= new HashSet();
			}
			
			if(matList==null){
				matList= new HashSet();				
			}
			
			
			if(grpModel==null){
				grpModel=new ErpGrpPriceModel(grpId,longDesc,shortDesc,!isExpired);
				grpModel.setMatList(matList);
				grpModel.setZoneModelList(zoneSet);
				grpInfos.add(grpModel);
			}
			
			if(!matList.contains(matId)){
				matList.add(matId);
			}
			
			zoneModel=new ErpGrpPriceZoneModel(zoneId,grpQty,grpUOM,grpPrice, grpSUOM);
			
			if(!zoneSet.contains(zoneModel)){
				zoneSet.add(zoneModel);
			}
								
			
		}catch(Exception e){
		   Logger.error(e);		
		   throw new LoaderException(e, "Unable to complete a failed batch. Error in constructing ErpGrpPriceModel: "+e.getMessage());
		}
		
	}
	

	private void storeGrpInfo(List zoneInfos) throws NamingException, EJBException, CreateException, FinderException, FDResourceException, RemoteException {
		Context ctx = null;
		String saleId = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			System.out.println("grpInfos size:"+zoneInfos.size());
			SAPGrpInfoLoaderHome mgr = (SAPGrpInfoLoaderHome) ctx.lookup("freshdirect.dataloader.SAPGrpInfoLoader");
			SapGrpInfoLoaderSB sb = mgr.create();
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
