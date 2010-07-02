/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.sap.helper.BasePriceInfo;
import com.freshdirect.erp.model.ErpMaterialPriceModel;

/** a parser that deals with SAP material price export files
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class MaterialPriceParser extends SAPParser {
    
    /** a collection of material prices
     */    
    Map<String, Set<ErpMaterialPriceModel>> materialPrices = null;
    
    private Map<String, Set<BasePriceInfo>> materialBasePrices=null;
    private static final String BASE_PRICE="PBBS";
    
    /** Creates new MaterialPriceParser */
    public MaterialPriceParser() {
        super();
        materialPrices = new HashMap<String, Set<ErpMaterialPriceModel>>();
        materialBasePrices=new HashMap<String, Set<BasePriceInfo>>();
        /*
         * from ERP Services/Technical Specs/Batch Loads/Material_Price_CSD.doc in VSS repository
         */
        fields.add(new Field(ZONE_ID,            10, true));
        fields.add(new Field(MATERIAL_NUMBER,            18, true));
        fields.add(new Field(MATERIAL_DESCRIPTION,       40, true));
        fields.add(new Field(SALES_ORGANIZATION,          4, true));
        fields.add(new Field(DISTRIBUTION_CHANNEL,        2, true));
        fields.add(new Field(CONDITION_SCALE_QUANTITY,   19, false));
        fields.add(new Field(CONDITION_SCALE_UOM,         3, false));
        fields.add(new Field(PRICE,                      11, true));
        fields.add(new Field(RATE_UNIT,                   5, true));
        fields.add(new Field(CONDITION_UNIT,              3, true));
        fields.add(new Field(SALES_UNIT,                  3, false));
        fields.add(new Field(MATERIAL_GROUP,              9, true));
        fields.add(new Field(CONDITION_TYPE,              4, true));
        fields.add(new Field(VALIDITY_END_DATE,           8, true));
        fields.add(new Field(VALIDITY_START_DATE,         8, true));
        fields.add(new Field(CONDITION_RECORD_NUMBER,    10, true));
    }
    
    /** gets a collection of material prices parsed from a file
     * @return the collection of material prices found in the file
     */    
    public Map<String, Set<ErpMaterialPriceModel>> getMaterialPrices() {
        return materialPrices;
    }
    
    public Map<String, Set<BasePriceInfo>> getMaterialBasePrices() {
    	return materialBasePrices;
    }
    
    /** creates model objects from the supplied tokens
     * @param tokens a HashMap of tokens parsed from a line of an export file
     * @throws BadDataException an problems encountered while assembling the tokens into model objects
     */
    @Override
    public void makeObjects(Map<String, String> tokens) throws BadDataException {
        
        /*
         * from ERP Services/Technical Specs/Mapping Docs/ERPS_SAP_BATCH_MAP.xls in VSS repository
         *
        MATERIAL.MATERIAL_ID                    Material (18)
        MATERIAL_PRICE.SALES_ORGANIZATION       Sales Organization (4)
        MATERIAL_PRICE.DISTRIBUTION_CHANNEL     Distribution Channel (2)
        MATERIAL_PRICE.PRICE                    Price (11)
        MATERIAL_PRICE.RATE_UNIT                Rate unit (5)
        MATERIAL_PRICE.CONDITION_UNIT           Condition unit (3)
        MATERIAL_PRICE.SALES_UNIT               Sales unit (3)
        MATERIAL_PRICE.CONDITION_TYPE           Condition type (4)
        MATERIAL_PRICE.VALID_END_DATE           Validity end date of the condition record (8)
        MATERIAL_PRICE.VALID_START_DATE         Validity start date of the condition record (8)
         */
        
        try {
            //
            // create the material price
            //
        	String priceType=getString(tokens,CONDITION_TYPE);
            //
            // which material does this price belong to?
            //
        	  String zoneId=getString(tokens, ZONE_ID);
            String matlNumber = getString(tokens, MATERIAL_NUMBER);
            if (BASE_PRICE.equals(priceType)) {
            	  Set<BasePriceInfo> basePrices = null;
            	  if (!materialBasePrices.containsKey(matlNumber)) {
  	                //
  	                // no prices yet for this material
  	                // create a new set and add it to the collection
  	                //
            		  basePrices = new HashSet<BasePriceInfo>();
            		  materialBasePrices.put(matlNumber, basePrices);
  	            } else {
  	                //
  	                // find the price set for this material
  	                //
  	            	basePrices = materialBasePrices.get(matlNumber);
  	            }
            	
            	basePrices.add(new BasePriceInfo(matlNumber,getDouble(tokens, PRICE),getString(tokens, CONDITION_UNIT),zoneId));
            	
            	//materialBasePrices.put(matlNumber, new BasePriceInfo(matlNumber,getDouble(tokens, PRICE),getString(tokens, CONDITION_UNIT),zoneId));
            } else {
            
	            ErpMaterialPriceModel materialPrice = new ErpMaterialPriceModel();
	            materialPrice.setPrice(getDouble(tokens, PRICE));
	            materialPrice.setPricingUnit(getString(tokens, CONDITION_UNIT));
	            materialPrice.setScaleQuantity(getDouble(tokens, CONDITION_SCALE_QUANTITY));
	            materialPrice.setScaleUnit(getString(tokens, CONDITION_SCALE_UOM));
	            materialPrice.setSapId(getString(tokens, CONDITION_RECORD_NUMBER));
	          
	            materialPrice.setSapZoneId(zoneId);
	            //
	            // no zero price materials
	            //
	            if (materialPrice.getPrice() == 0.0) {
	                throw new BadDataException("Material " + materialPrice.getSapId() + " has a zero price for scale unit " + materialPrice.getScaleUnit());
	            }
	            //
	            // a little massage
	            //
	            if ((materialPrice.getScaleUnit() == null) || ("".equals(materialPrice.getScaleUnit())))
	                materialPrice.setScaleUnit("   ");
	            //
	            // since there are multiple prices for each material at different quantities
	            // we need to make the material prices we collect a hash of sets
	            //
	            Set<ErpMaterialPriceModel> prices = null;
	            if (!materialPrices.containsKey(matlNumber)) {
	                //
	                // no prices yet for this material
	                // create a new set and add it to the collection
	                //
	                prices = new HashSet<ErpMaterialPriceModel>();
	                materialPrices.put(matlNumber, prices);
	            } else {
	                //
	                // find the price set for this material
	                //
	                prices = materialPrices.get(matlNumber);
	            }
	            //
	            // add the new price to the set
	            //
	            prices.add(materialPrice);
            }
              
        } catch (Exception e) {
            throw new BadDataException(e, "An exception was thrown while trying to parse a MaterialPrice");
        }

    }
    
    
}
