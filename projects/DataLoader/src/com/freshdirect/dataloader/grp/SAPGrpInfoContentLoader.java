package com.freshdirect.dataloader.grp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

/**
 * @author skrishnasamy
 *
 */
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

		List<ErpGrpPriceModel> grpInfos = new ArrayList<ErpGrpPriceModel>();
		Map<String, String> existingGrps = new HashMap<String, String>();
		Map<String, String> matNumbers = new HashMap<String, String>();
		List<List<String>> rawData = new ArrayList<List<String>>();
		List<String> tmpRawRowData = null;
		List<String> intermittentErrors = new ArrayList<String>();
		
		try {
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
				
				tmpRawRowData = new ArrayList<String>();
				tmpRawRowData.add(matNumber);
				tmpRawRowData.add(grpId);
				tmpRawRowData.add(shortDesc);
				tmpRawRowData.add(LongDesc);
				tmpRawRowData.add(zoneId);
				tmpRawRowData.add(grpQtyStr);
				tmpRawRowData.add(grpUOM);
				tmpRawRowData.add(grpPriceStr);
				tmpRawRowData.add(grpSUOM);
				tmpRawRowData.add(grpExpiryInd);
				
				rawData.add(tmpRawRowData);
				
				double  grpQty=0;
				double grpPrice=0;

	
				
				if(grpQtyStr!=null && grpQtyStr.trim().length()>0) grpQty =Double.parseDouble(grpQtyStr);
				
				if(grpPriceStr!=null && grpPriceStr.trim().length()>0) grpPrice =Double.parseDouble(grpPriceStr);

				if(!"X".equals(grpExpiryInd)){
					if(matNumbers.containsKey(matNumber) && !matNumbers.get(matNumber).equals(grpId)){
						//Same Material appears in More than one Group in the export; throw Loader Exception						
						intermittentErrors.add(matNumber);
					}else{
						matNumbers.put(matNumber, grpId);
					}
				}

				if(!existingGrps.containsValue(matNumber)) {//Only if material id is not found so far any existing groups.
					//Validate if this material in the incoming message is already participating in an existing group
					String existingGrpId = checkIfMaterialAlreadyExistsInActiveGroup(grpId, matNumber);
					if(existingGrpId != null && existingGrpId.length() > 0){
						existingGrps.put(existingGrpId, matNumber);
					} 
				}
				
				constructGrpInfoModel(grpId,shortDesc,LongDesc,zoneId,grpQty,grpUOM,grpPrice,"X".equalsIgnoreCase(grpExpiryInd),matNumber,grpInfos, grpSUOM);
			
				groupTable.nextRow();
			}
			if(intermittentErrors.size() > 0) {
				throw new LoaderException("Same Material Appears in More than One Active Group in the Batch. Please check the Batch for Material Number: "+intermittentErrors.toString());
			}
			if(existingGrps.size() > 0){//IF there are materials in existing groups
				//Validate for material present more than one active group.
				Map<String, String> activeGrps = new HashMap<String, String>(existingGrps); 
				for(Iterator<ErpGrpPriceModel> it = grpInfos.iterator(); it.hasNext();){
					ErpGrpPriceModel grpModel = it.next();
					if(existingGrps.containsKey(grpModel.getGrpId())){
						//remove the group from active group map if active group is expired or if this group no more contains the material
						//being checked against.  
						Set<String> grpMatList = grpModel.getMatList();
						String checkMatId = existingGrps.get(grpModel.getGrpId());
						if(!grpModel.isActive() || !grpMatList.contains(checkMatId)) {
							activeGrps.remove(grpModel.getGrpId());
						}
					}
				}
				if(activeGrps.size() > 0){
					//Active groups exists. Stop further processing the export.
					throw new LoaderException("Material(s) From this Export Already exists in an Active Group. "+activeGrps.toString());
				}
			}
			LOGGER.debug ("Storing group content info");
			this.storeGrpInfo(grpInfos);

			output.setValue("S", "RETURN");
			output.setValue("All good...", "MESSAGE");

		} catch (LoaderException ex) {
			LOGGER.warn("Failed to store Group info", ex);
			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
			//return;
		}  catch (Exception ex) {
			LOGGER.warn("Failed to store wave info", ex);
			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
		} finally {
			generateInputDumpFile(rawData);
		}
	}

	private String checkIfMaterialAlreadyExistsInActiveGroup(String grpId, String matId) {
		Context ctx = null;
		String existingGrpId = null;
		try{
			ctx = ErpServicesProperties.getInitialContext();
			ErpGrpInfoHome mgr = (ErpGrpInfoHome) ctx.lookup("freshdirect.erp.GrpInfoManager");
			ErpGrpInfoSB sb = mgr.create();
			FDGroup group = sb.getGroupIdentityForMaterial(matId);
			
			if(group != null && !group.getGroupId().equals(grpId)) {//and its does not match with current active group.
				//throw new LoaderException("Material Number : "+matId+" Already exists in Active Group "+group.getGroupId());
				existingGrpId = group.getGroupId();
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
		return existingGrpId;
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
	
	public boolean generateInputDumpFile(List<List<String>> rawData) {
		StringBuffer strBuf = new StringBuffer();
		for(List<String> row : rawData) {
			boolean isFirst = true;
			for(String cell : row) {
				if(!isFirst) {
					strBuf.append(",");
				}
				strBuf.append(cell);
				isFirst = false;
			}
			strBuf.append("\n");
		}
        try {
        	
        	BufferedWriter out = new BufferedWriter(new FileWriter("GROUPEXPORT_"+System.currentTimeMillis()+".csv"));
            out.write(strBuf.toString());
            out.close();

            return true;
        } catch (Exception ioExp) {
        	ioExp.printStackTrace();  
        } 
        return false;
	}
	/*
	String matNumber = groupTable.get("ZMATNR").trim();
	String grpId = groupTable.get("ZGROUP_ID").trim();			
	String shortDesc = groupTable.get("ZGROUP_SHORT_DESC").trim();
	String LongDesc = groupTable.get("ZGROUP_LONG_DESC").trim();			
	String zoneId = groupTable.get("ZZONE_ID").trim();
	String grpQtyStr = groupTable.get("ZSGRP_QTY").trim();
	String grpUOM = groupTable.get("ZSGRP_UOM").trim();
	String grpPriceStr = groupTable.get("ZSGRP_PRICE").trim();
	String grpSUOM = groupTable.get("ZSGRP_SUOM").trim();
	String grpExpiryInd = groupTable.get("ZSGRP_EXP_IND").trim();
	*/
	public static void main(String[] values){
		SAPGrpInfoContentLoader loader = new SAPGrpInfoContentLoader();
		Map<String, Map<String, String>> groupTables = new HashMap<String, Map<String, String>>();
		Map<String, String> group1M1Z1 = new HashMap<String, String>();

		group1M1Z1.put("ZMATNR", "000000000300700326");
		group1M1Z1.put("ZGROUP_ID", "LOCAL_VEG");
		group1M1Z1.put("ZGROUP_SHORT_DESC", "LOCAL_VEG");
		group1M1Z1.put("ZGROUP_LONG_DESC", "LOCAL_VEG");
		group1M1Z1.put("ZZONE_ID", "0000100000");
		group1M1Z1.put("ZSGRP_QTY", "5");
		group1M1Z1.put("ZSGRP_UOM", "EA");
		group1M1Z1.put("ZSGRP_PRICE", "3.59");
		group1M1Z1.put("ZSGRP_SUOM", "EA");
		group1M1Z1.put("ZSGRP_EXP_IND", "X");
		groupTables.put("1", group1M1Z1);
		
		Map<String, String> group1M1Z2 = new HashMap<String, String>();
		group1M1Z2.put("ZMATNR", "000000000300700326");
		group1M1Z2.put("ZGROUP_ID", "LOCAL_VEG");
		group1M1Z2.put("ZGROUP_SHORT_DESC", "LOCAL_VEG");
		group1M1Z2.put("ZGROUP_LONG_DESC", "LOCAL_VEG");
		group1M1Z2.put("ZZONE_ID", "0000100001");
		group1M1Z2.put("ZSGRP_QTY", "5");
		group1M1Z2.put("ZSGRP_UOM", "EA");
		group1M1Z2.put("ZSGRP_PRICE", "3.59");
		group1M1Z2.put("ZSGRP_SUOM", "EA");
		group1M1Z2.put("ZSGRP_EXP_IND", "X");
		groupTables.put("2", group1M1Z2);
		
		Map<String, String> group1M2Z1 = new HashMap<String, String>();
		group1M2Z1.put("ZMATNR", "000000000300700323");
		group1M2Z1.put("ZGROUP_ID", "LOCAL_VEG");
		group1M2Z1.put("ZGROUP_SHORT_DESC", "LOCAL_VEG");
		group1M2Z1.put("ZGROUP_LONG_DESC", "LOCAL_VEG");
		group1M2Z1.put("ZZONE_ID", "0000100000");
		group1M2Z1.put("ZSGRP_QTY", "5");
		group1M2Z1.put("ZSGRP_UOM", "EA");
		group1M2Z1.put("ZSGRP_PRICE", "3.59");
		group1M2Z1.put("ZSGRP_SUOM", "EA");
		group1M2Z1.put("ZSGRP_EXP_IND", "X");
		groupTables.put("3", group1M2Z1);
		
		Map<String, String> group1M2Z2 = new HashMap<String, String>();
		group1M2Z2.put("ZMATNR", "000000000300700323");
		group1M2Z2.put("ZGROUP_ID", "LOCAL_VEG");
		group1M2Z2.put("ZGROUP_SHORT_DESC", "LOCAL_VEG");
		group1M2Z2.put("ZGROUP_LONG_DESC", "LOCAL_VEG");
		group1M2Z2.put("ZZONE_ID", "0000100001");
		group1M2Z2.put("ZSGRP_QTY", "5");
		group1M2Z2.put("ZSGRP_UOM", "EA");
		group1M2Z2.put("ZSGRP_PRICE", "3.59");
		group1M2Z2.put("ZSGRP_SUOM", "EA");
		group1M2Z2.put("ZSGRP_EXP_IND", "X");
		groupTables.put("4", group1M2Z2);
		/*
		Map<String, String> group2M1Z1 = new HashMap<String, String>();
		group2M1Z1.put("ZMATNR", "000000000200001798");
		group2M1Z1.put("ZGROUP_ID", "LOCAL_FRU");
		group2M1Z1.put("ZGROUP_SHORT_DESC", "LOCAL_FRU");
		group2M1Z1.put("ZGROUP_LONG_DESC", "LOCAL_FRU");
		group2M1Z1.put("ZZONE_ID", "0000100000");
		group2M1Z1.put("ZSGRP_QTY", "6");
		group2M1Z1.put("ZSGRP_UOM", "EA");
		group2M1Z1.put("ZSGRP_PRICE", "2.59");
		group2M1Z1.put("ZSGRP_SUOM", "EA");
		group2M1Z1.put("ZSGRP_EXP_IND", "X");
		
		groupTables.put("5", group2M1Z1);
		Map<String, String> group2M1Z2 = new HashMap<String, String>();
		group2M1Z2.put("ZMATNR", "000000000200001798");
		group2M1Z2.put("ZGROUP_ID", "LOCAL_FRU");
		group2M1Z2.put("ZGROUP_SHORT_DESC", "LOCAL_FRU");
		group2M1Z2.put("ZGROUP_LONG_DESC", "LOCAL_FRU");
		group2M1Z2.put("ZZONE_ID", "0000100001");
		group2M1Z2.put("ZSGRP_QTY", "6");
		group2M1Z2.put("ZSGRP_UOM", "EA");
		group2M1Z2.put("ZSGRP_PRICE", "2.59");
		group2M1Z2.put("ZSGRP_SUOM", "EA");
		group2M1Z2.put("ZSGRP_EXP_IND", "X");
		groupTables.put("6", group2M1Z2);
		
		Map<String, String> group2M2Z1 = new HashMap<String, String>();
		group2M2Z1.put("ZMATNR", "000000000200001799");
		group2M2Z1.put("ZGROUP_ID", "LOCAL_FRU");
		group2M2Z1.put("ZGROUP_SHORT_DESC", "LOCAL_FRU");
		group2M2Z1.put("ZGROUP_LONG_DESC", "LOCAL_FRU");
		group2M2Z1.put("ZZONE_ID", "0000100000");
		group2M2Z1.put("ZSGRP_QTY", "6");
		group2M2Z1.put("ZSGRP_UOM", "EA");
		group2M2Z1.put("ZSGRP_PRICE", "2.59");
		group2M2Z1.put("ZSGRP_SUOM", "EA");
		group2M2Z1.put("ZSGRP_EXP_IND", "X");
		groupTables.put("7", group2M2Z1);
		
		Map<String, String> group2M2Z2 = new HashMap<String, String>();
		group2M2Z2.put("ZMATNR", "000000000200001799");
		group2M2Z2.put("ZGROUP_ID", "LOCAL_FRU");
		group2M2Z2.put("ZGROUP_SHORT_DESC", "LOCAL_FRU");
		group2M2Z2.put("ZGROUP_LONG_DESC", "LOCAL_FRU");
		group2M2Z2.put("ZZONE_ID", "0000100001");
		group2M2Z2.put("ZSGRP_QTY", "6");
		group2M2Z2.put("ZSGRP_UOM", "EA");
		group2M2Z2.put("ZSGRP_PRICE", "2.59");
		group2M2Z2.put("ZSGRP_SUOM", "EA");
		group2M2Z2.put("ZSGRP_EXP_IND", "X");
		groupTables.put("8", group2M2Z2);
		
		Map<String, String> groupM1Z1 = new HashMap<String, String>();

		groupM1Z1.put("ZMATNR", "000000000300700326");
		groupM1Z1.put("ZGROUP_ID", "LOCAL_WINE");
		groupM1Z1.put("ZGROUP_SHORT_DESC", "LOCAL_WINE");
		groupM1Z1.put("ZGROUP_LONG_DESC", "LOCAL_WINE");
		groupM1Z1.put("ZZONE_ID", "0000100000");
		groupM1Z1.put("ZSGRP_QTY", "5");
		groupM1Z1.put("ZSGRP_UOM", "EA");
		groupM1Z1.put("ZSGRP_PRICE", "3.59");
		groupM1Z1.put("ZSGRP_SUOM", "EA");
		groupM1Z1.put("ZSGRP_EXP_IND", "X");
		
		groupTables.put("9", groupM1Z1);
		Map<String, String> groupM1Z2 = new HashMap<String, String>();
		groupM1Z2.put("ZMATNR", "000000000300700326");
		groupM1Z2.put("ZGROUP_ID", "LOCAL_WINE");
		groupM1Z2.put("ZGROUP_SHORT_DESC", "LOCAL_WINE");
		groupM1Z2.put("ZGROUP_LONG_DESC", "LOCAL_WINE");
		groupM1Z2.put("ZZONE_ID", "0000100001");
		groupM1Z2.put("ZSGRP_QTY", "5");
		groupM1Z2.put("ZSGRP_UOM", "EA");
		groupM1Z2.put("ZSGRP_PRICE", "3.59");
		groupM1Z2.put("ZSGRP_SUOM", "EA");
		groupM1Z2.put("ZSGRP_EXP_IND", "X");
		groupTables.put("10", groupM1Z2);
		Map<String, String> groupM2Z1 = new HashMap<String, String>();
		groupM2Z1.put("ZMATNR", "000000000300700323");
		groupM2Z1.put("ZGROUP_ID", "LOCAL_WINE");
		groupM2Z1.put("ZGROUP_SHORT_DESC", "LOCAL_WINE");
		groupM2Z1.put("ZGROUP_LONG_DESC", "LOCAL_WINE");
		groupM2Z1.put("ZZONE_ID", "0000100000");
		groupM2Z1.put("ZSGRP_QTY", "5");
		groupM2Z1.put("ZSGRP_UOM", "EA");
		groupM2Z1.put("ZSGRP_PRICE", "3.59");
		groupM2Z1.put("ZSGRP_SUOM", "EA");
		groupM2Z1.put("ZSGRP_EXP_IND", "X");
		groupTables.put("11", groupM2Z1);
		Map<String, String> groupM2Z2 = new HashMap<String, String>();
		groupM2Z2.put("ZMATNR", "000000000300700323");
		groupM2Z2.put("ZGROUP_ID", "LOCAL_WINE");
		groupM2Z2.put("ZGROUP_SHORT_DESC", "LOCAL_WINE");
		groupM2Z2.put("ZGROUP_LONG_DESC", "LOCAL_WINE");
		groupM2Z2.put("ZZONE_ID", "0000100001");
		groupM2Z2.put("ZSGRP_QTY", "5");
		groupM2Z2.put("ZSGRP_UOM", "EA");
		groupM2Z2.put("ZSGRP_PRICE", "3.59");
		groupM2Z2.put("ZSGRP_SUOM", "EA");
		groupM2Z2.put("ZSGRP_EXP_IND", "X");
		groupTables.put("12", groupM2Z2);
	*/
		loader.testExecute(groupTables);
	}
	private void testExecute(Map<String, Map<String, String>> groupTables) {
		List<ErpGrpPriceModel> grpInfos = new ArrayList<ErpGrpPriceModel>();
		Map<String, String> existingGrps = new HashMap<String, String>();
		Map<String, String> matNumbers = new HashMap<String, String>();
		try {
			for (Iterator<String> it = groupTables.keySet().iterator(); it.hasNext();) {
				Map<String, String> groupTable = groupTables.get(it.next());
				// First retrieve all data from current row.
				String matNumber = groupTable.get("ZMATNR").trim();
				String grpId = groupTable.get("ZGROUP_ID").trim();			
				String shortDesc = groupTable.get("ZGROUP_SHORT_DESC").trim();
				String LongDesc = groupTable.get("ZGROUP_LONG_DESC").trim();			
				String zoneId = groupTable.get("ZZONE_ID").trim();
				String grpQtyStr = groupTable.get("ZSGRP_QTY").trim();
				String grpUOM = groupTable.get("ZSGRP_UOM").trim();
				String grpPriceStr = groupTable.get("ZSGRP_PRICE").trim();
				String grpSUOM = groupTable.get("ZSGRP_SUOM").trim();
				String grpExpiryInd = groupTable.get("ZSGRP_EXP_IND").trim();
				double  grpQty=0;
				double grpPrice=0;

	
				
				if(grpQtyStr!=null && grpQtyStr.trim().length()>0) grpQty =Double.parseDouble(grpQtyStr);
				
				if(grpPriceStr!=null && grpPriceStr.trim().length()>0) grpPrice =Double.parseDouble(grpPriceStr);
				
				if(!"X".equals(grpExpiryInd)){
					if(matNumbers.containsKey(matNumber) && !matNumbers.get(matNumber).equals(grpId)){
						//Same Material appears in More than one Group; throw Loader Exception
					 	throw new LoaderException("Same Material Appears in More than One Active Group in the Batch. Please check the Batch for Material Number: "+matNumber);
					}else{
						matNumbers.put(matNumber, grpId);
					}
				}
				if(!existingGrps.containsValue(matNumber)) {//Only if material id is not found so far any existing groups.
					//Validate if this material in the incoming message is already participating in an existing group
					String existingGrpId = checkIfMaterialAlreadyExistsInActiveGroup(grpId, matNumber);
					if(existingGrpId != null && existingGrpId.length() > 0){
						existingGrps.put(existingGrpId, matNumber);
					} 
				}
				
				constructGrpInfoModel(grpId,shortDesc,LongDesc,zoneId,grpQty,grpUOM,grpPrice,"X".equalsIgnoreCase(grpExpiryInd),matNumber,grpInfos, grpSUOM);
			}
			if(existingGrps.size() > 0){//IF there are materials in existing groups
				//Validate for material present more than one active group.
				Map<String, String> activeGrps = new HashMap<String, String>(existingGrps); 
				for(Iterator<ErpGrpPriceModel> it = grpInfos.iterator(); it.hasNext();){
					ErpGrpPriceModel grpModel = it.next();
					if(existingGrps.containsKey(grpModel.getGrpId())){
						//remove the group from active group map if active group is expired or if this group no more contains the material
						//being checked against.  
						Set<String> grpMatList = grpModel.getMatList();
						String checkMatId = existingGrps.get(grpModel.getGrpId());
						if(!grpModel.isActive() || !grpMatList.contains(checkMatId)) {
							activeGrps.remove(grpModel.getGrpId());
						}
					}
				}
				if(activeGrps.size() > 0){
					//Active groups exists. Stop further processing the export.
					throw new LoaderException("Material(s) From this Export Already exists in an Active Group. "+activeGrps.toString());
				}
			}
			LOGGER.debug ("Storing group content info");
			this.storeGrpInfo(grpInfos);

			LOGGER.info("SUCCESS");

		} catch (LoaderException ex) {
			LOGGER.warn("Failed to store Group info", ex);
			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
		}  catch (Exception ex) {
			LOGGER.warn("Failed to store wave info", ex);
			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
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
