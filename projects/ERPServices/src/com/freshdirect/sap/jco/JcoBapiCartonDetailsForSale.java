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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpCartonDetails;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.sap.bapi.BapiCartonDetailsForSale;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;


/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiCartonDetailsForSale extends JcoBapiFunction implements BapiCartonDetailsForSale {
	
	private JCoTable lstCartonInfo;
	
	private String saleId;
	
	private String saleSapId;
	
	public JcoBapiCartonDetailsForSale() throws JCoException {
		super("ZBAPI_CARTON_DETAILS");
	}
	
	@Override
	public void setParameters(String plantCode, Date deliveryDate, String saleId, String saleSapId, String waveNumber)
	{
		this.function.getImportParameterList().setValue("WERKS", plantCode);
		this.function.getImportParameterList().setValue("VDATU", deliveryDate);
		this.function.getImportParameterList().setValue("VBELN", saleSapId);
		this.function.getImportParameterList().setValue("WAVENO", waveNumber);
		
		this.saleId = saleId;
		this.saleSapId = saleSapId;
	}
	
	public List<ErpCartonInfo> getCartonInfos() {
		
		List<ErpCartonInfo> cartons = new ArrayList<ErpCartonInfo>();
		ErpCartonInfo currentCartonInfo = null;
		
		lstCartonInfo = this.function.getTableParameterList().getTable("CARTON_DET");
		
		Map<String, ErpCartonInfo> cartonNoToCartonInfo = new HashMap<String, ErpCartonInfo>();
		Map<String, Map<String, List<ErpCartonDetails>>> cartonNoToHeaderLineToComponent = new HashMap<String, Map<String, List<ErpCartonDetails>>>();
		Map<String, ErpCartonDetails> headerLineItemToCartonDetails = new HashMap<String, ErpCartonDetails>();
		
		for (int loop = 0; loop < lstCartonInfo.getNumRows(); loop++)
		{
			String cartonNumber = lstCartonInfo.getString("CARTON");	
			String cartonType = lstCartonInfo.getString("CRT_TYP");	
			String parentId = lstCartonInfo.getString("PARENT_LN");	
			String childId = lstCartonInfo.getString("CHILD_LN");	
			String materialNumber = lstCartonInfo.getString("MATNR");	
			String barCode = lstCartonInfo.getString("BARCODE");	
			String actualQty = lstCartonInfo.getString("PACK_QTY");	
			String netWeight = lstCartonInfo.getString("NTGEW");	
			String weightUnit = lstCartonInfo.getString("GEWEI");
			String skuCode = lstCartonInfo.getString("WEBID");
			String materialDesc = lstCartonInfo.getString("MAKTX");
			boolean shortShipped = "X".equalsIgnoreCase(lstCartonInfo.getString("SHORTSHP"))?true:false;
			
			String unit = lstCartonInfo.getString("PACK_UOM");
			String orderedQty = lstCartonInfo.getString("ACT_QTY");
			
			
			if(cartonType.equals("FR")) {
				cartonType = ErpCartonInfo.CARTON_TYPE_FREEZER;
			}
			if(cartonType.equals("CS")){
				cartonType = ErpCartonInfo.CARTON_TYPE_CASEPICK;
			}
			if(cartonType.equals("RG")){
				cartonType = ErpCartonInfo.CARTON_TYPE_REGULAR;
			}
			if(cartonType.equals("PL")){
				cartonType = ErpCartonInfo.CARTON_TYPE_PLATTER;
			}
			if(cartonType.equals("BR")){
				cartonType = ErpCartonInfo.CARTON_TYPE_BEER;
			}
			
			System.out.println(cartonNumber + "-" + skuCode + " -" + parentId + "-" + childId);
			if("0000000000".equalsIgnoreCase(cartonNumber) && !shortShipped) { // This is a header item and will have components
				headerLineItemToCartonDetails.put(parentId, new ErpCartonDetails(null, parentId, materialNumber, barCode
						, Double.parseDouble(actualQty), Double.parseDouble(netWeight), weightUnit, skuCode, materialDesc,shortShipped));
			} else {
				if(!cartonNoToCartonInfo.containsKey(cartonNumber)) {
					cartonNoToCartonInfo.put(cartonNumber, new ErpCartonInfo(saleId, saleSapId, cartonNumber, cartonType));
				}
				currentCartonInfo = cartonNoToCartonInfo.get(cartonNumber);
				if("000000".equalsIgnoreCase(childId)) { // This is a normal line item so don't expect components
					currentCartonInfo.getDetails().add(new ErpCartonDetails(currentCartonInfo, parentId, materialNumber, barCode
							, Double.parseDouble(actualQty), Double.parseDouble(netWeight), weightUnit, skuCode, materialDesc,shortShipped));
				} else {
					if(!cartonNoToHeaderLineToComponent.containsKey(cartonNumber)) {
						cartonNoToHeaderLineToComponent.put(cartonNumber, new HashMap<String, List<ErpCartonDetails>>());
					}
					if(!cartonNoToHeaderLineToComponent.get(cartonNumber).containsKey(parentId)) {
						cartonNoToHeaderLineToComponent.get(cartonNumber).put(parentId, new ArrayList<ErpCartonDetails>());
					}
					cartonNoToHeaderLineToComponent.get(cartonNumber).get(parentId).add(new ErpCartonDetails(currentCartonInfo, childId, materialNumber, barCode
																	, Double.parseDouble(actualQty), Double.parseDouble(netWeight), weightUnit, skuCode, materialDesc,shortShipped));
				}
			}									
							
			lstCartonInfo.nextRow();
		}
		
		ErpCartonDetails currentCartonDetail = null;
		ErpCartonDetails tempCartonDetail = null;
		for(Map.Entry<String, Map<String, List<ErpCartonDetails>>> cartonEntrySet : cartonNoToHeaderLineToComponent.entrySet()) {
			
			currentCartonInfo = cartonNoToCartonInfo.get(cartonEntrySet.getKey());		
			
			for(Map.Entry<String, List<ErpCartonDetails>> headerEntrySet : cartonEntrySet.getValue().entrySet()) {			
				currentCartonDetail = new ErpCartonDetails();
				tempCartonDetail = headerLineItemToCartonDetails.get(headerEntrySet.getKey());
				if(tempCartonDetail != null) {
					currentCartonDetail.setCartonInfo(tempCartonDetail.getCartonInfo());
					currentCartonDetail.setOrderLineNumber(tempCartonDetail.getOrderLineNumber());
					currentCartonDetail.setMaterialNumber(tempCartonDetail.getMaterialNumber());
					currentCartonDetail.setBarcode(tempCartonDetail.getBarcode());
					currentCartonDetail.setMaterialDesc(tempCartonDetail.getMaterialDesc());
					currentCartonDetail.setActualQuantity(tempCartonDetail.getActualQuantity());
					currentCartonDetail.setWeightUnit(tempCartonDetail.getWeightUnit());				
					currentCartonDetail.setNetWeight(tempCartonDetail.getNetWeight());	
					currentCartonDetail.setCartonInfo(currentCartonInfo); // Initial data from SAP will not have correct carton no, so updating it back
					currentCartonDetail.getComponents().addAll(headerEntrySet.getValue());
				}				
				currentCartonInfo.getDetails().add(currentCartonDetail);				
			}
		}
		cartons.addAll(cartonNoToCartonInfo.values());
		return cartons;
	}
	
	

}
