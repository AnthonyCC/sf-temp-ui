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
import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.framework.util.DayOfWeekSet;


/** a parser that deals with SAP material export files
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class MaterialParser extends SAPParser {
    
    /** a collection of materials that need to be added or updated from an export
     */
    private Map<ErpMaterialModel, Map<String, Object>> activeMaterials = null;
    
    /** creates a new material export parser
     */
    public MaterialParser() {
        super();
        activeMaterials = new HashMap<ErpMaterialModel, Map<String, Object>>();
        /*
         * from ERP Services/Technical Specs/Batch Loads/Material_Export_CSD.doc in VSS repository
         */
        fields.add(new Field(MATERIAL_NUMBER,               18, true));
        fields.add(new Field(MATERIAL_DESCRIPTION,          40, true));
        fields.add(new Field(SKU,                           18, true));
        fields.add(new Field(SALES_ORGANIZATION,             4, true));
        fields.add(new Field(DISTRIBUTION_CHANNEL,           2, true));
        fields.add(new Field(DEPARTMENT,                     3, true));
        fields.add(new Field(DEPARTMENT_DESC,               30, true));
        fields.add(new Field(BASE_UNIT,                      3, true));
        fields.add(new Field(SALES_UNIT,                     3, false));
        fields.add(new Field(MATERIAL_TYPE,                  4, true));
        fields.add(new Field(MATERIAL_TYPE_DESC,            25, true));
        fields.add(new Field(MATERIAL_GROUP,                 9, true));
        fields.add(new Field(CONFIGURABLE_ITEM,              1, false));
        fields.add(new Field(CROSS_CHAIN_SALES_STATUS,       2, false));
        fields.add(new Field(SALES_STATUS_DATE,              8, false));
        fields.add(new Field(DIST_CHAIN_SPEC_STATUS,         2, false));
        fields.add(new Field(DIST_CHAIN_SPEC_STATUS_DESC,   20, false));
        fields.add(new Field(DIST_CHAIN_SPEC_STATUS_DATE,    8, false));
        fields.add(new Field(AVAILABILITY_CHECK,             2, true));
        fields.add(new Field(PLANNED_DELIVERY_TIME,          3, false));
        fields.add(new Field(INHOUSE_PRODUCTION_TIME,        3, false));
        fields.add(new Field(DELETION_FLAG,                  1, false));
        fields.add(new Field(ATP_RULE_1,                     2, false));
        fields.add(new Field(ATP_RULE_2,                     2, false));
        fields.add(new Field(ATP_RULE_3,                     2, false));
        fields.add(new Field(ATP_RULE_4,                     2, false));
        fields.add(new Field(ATP_RULE_5,                     2, false));
        fields.add(new Field(ATP_RULE_6,                     2, false));
        fields.add(new Field(ATP_RULE_7,                     2, false));
        fields.add(new Field(UPC,                           18, false));
        fields.add(new Field(PLANT,                          4, true));
        fields.add(new Field(TAXABLE,                        1, true));
		fields.add(new Field(KOSHER_PRODUCTION,              1, true));
		fields.add(new Field(PLATTER,                        1, false));
		fields.add(new Field(DAY_INDICATOR,                  6, false));
		fields.add(new Field(RATING,                         3, false));
		fields.add(new Field(DAYS_FRESH,                     3, false));
		fields.add(new Field(DAYS_IN_HOUSE,                  3, false));
		fields.add(new Field(SUSTAINABILITY_RATING,          2, false));
    }
    
    /** gets the collection of materials that need to be created or updated in an export
     * @return the collection of active materials
     */
    public Map<ErpMaterialModel, Map<String, Object>> getActiveMaterials() {
        return activeMaterials;
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
        Material.MATERIAL_NUMBER                Material (18)
        Material.MATERIAL_DESCRIPTION           Material Description (40)
        Material.SKU                            Old Material Number (18)
        Material.BASE_UNIT                      Base Unit (3)
        Material.MATERIAL_TYPE                  Material Type (4)
        Material.MATERIAL_TYPE_DESCRIPTION      Material Type Description (25)
        Material.MATERIAL_GROUP                 Material Group (9)
        Material.CONFIGURABLE_ITEM              Configurable Item (1)
        Material.CROSS_CHAIN_SALES_STATUS       Cross Chain Sales Status (2)
        Material.SALES_STATUS_DATE              Sales Status Date (8)
        Material.DCHAIN_SPEC_STATUS             Dchain-spec status (2)
        Material.DCHAIN_SPEC_STATUS_DATE        Dchain-spec status date (8)
        Material.DELETED                        Deletion Flag indicator (1)
         *
         *  Values for CROSS_CHAIN_SALES_STATUS and DCHAIN_SPEC_STATUS
         *  ----------------------------------------------------------
         *  31   Temporarily Unavailable
         *  32   Out Of Season
         *  33   Discontinued
         *
         *
         */
        
        //
        // create the material
        //
        ErpMaterialModel material = new ErpMaterialModel();
        material.setSapId(getString(tokens, MATERIAL_NUMBER));
        material.setBaseUnit(getString(tokens, BASE_UNIT));
        material.setDescription(getString(tokens, MATERIAL_DESCRIPTION));
        material.setATPRule(decodeATPRule(getString(tokens, AVAILABILITY_CHECK)));
        material.setLeadTime(getInt(tokens, PLANNED_DELIVERY_TIME));
        material.setUPC(getString(tokens, UPC));
        //
        // alcoholic content information is now preferred to be derived
        // from the material group
        //  if the material group starts with "B", it is a beer product
        //  if the material group starts with "W", it is a BC wine product
        //	if the material group starts with "U", it is a USQ wine product
        //  any other value has no alcoholic content
        //
        String matGroup = getString(tokens, MATERIAL_GROUP);
        if (matGroup == null) matGroup = "";
        matGroup = matGroup.toUpperCase();
        if (matGroup.startsWith("B") || matGroup.startsWith("A")) {
        	material.setAlcoholicContent(EnumAlcoholicContent.BEER);
        } else if (matGroup.startsWith("W")) {
        	material.setAlcoholicContent(EnumAlcoholicContent.BC_WINE);
        }else if (matGroup.startsWith("U")) {
        	material.setAlcoholicContent(EnumAlcoholicContent.USQ_WINE);
        } else {
        	//
        	// fall back to the old method of deriving alcoholic content
	        //
	        // alcoholic content is determined from the PLANT field
	        //  1000 - regular product, non-alcoholic
	        //  1100 - beer
	        //
	        String plant = getString(tokens, PLANT);
	        if ("1000".equals(plant)) {
	            material.setAlcoholicContent(EnumAlcoholicContent.NONE);
	        } else if ("1100".equals(plant)){
	            material.setAlcoholicContent(EnumAlcoholicContent.BEER);
	        } else {
	            throw new BadDataException("Unrecognized value for PLANT for Material " + material.getSapId());
	        }
    	}
    	
    	
    	material.setTaxable("1".equals(getString(tokens, TAXABLE)));

		material.setKosherProduction("1".equals(getString(tokens, KOSHER_PRODUCTION)));

		material.setPlatter("1".equals(getString(tokens, PLATTER)));
		
		DayOfWeekSet allowedDays = DayOfWeekSet.decode(getString(tokens, DAY_INDICATOR));
		if (!allowedDays.isEmpty()) {
			material.setBlockedDays(allowedDays.inverted());
		}
    	
        //
        // get some of the extra info that comes along in the export
        // and hang on to it so the builders can access it later and
        // place the material and its extra info to the active materials map
        //
        Map<String, Object> extraInfo = new HashMap<String, Object>();
        String sku = getString(tokens, SKU);
        String rating=getString(tokens,RATING);
        String days_fresh = getString(tokens,DAYS_FRESH);
        String days_in_house = getString(tokens, DAYS_IN_HOUSE);
        String sustainabilityRating=getString(tokens,SUSTAINABILITY_RATING);
        
        if ((sku == null) || (sku.trim().equals(""))) {
            //
            // materials must have a sku assigned to be used in creating a default product
            // its an error if a sku is not provided
            //
            throw new BadDataException("No SKU was provided for Material " + material.getSapId());
        }
        extraInfo.put(SKU, sku);
        extraInfo.put("RATING", rating);
        extraInfo.put("DAYS_FRESH", days_fresh);
        extraInfo.put("DAYS_IN_HOUSE", days_in_house);
        extraInfo.put("SUSTAINABILITY_RATING", sustainabilityRating);
        if ((!"".equals(getString(tokens, DELETION_FLAG).trim())) ||
            "33".equals(getString(tokens, CROSS_CHAIN_SALES_STATUS)) || "33".equals(getString(tokens, DIST_CHAIN_SPEC_STATUS)) ||
            "30".equals(getString(tokens, CROSS_CHAIN_SALES_STATUS)) || "30".equals(getString(tokens, DIST_CHAIN_SPEC_STATUS))
        ) {
            //
            // discontinued, or otherwise blocked
            //
            extraInfo.put("UNAVAILABILITY_DATE", new java.util.Date());
            extraInfo.put("UNAVAILABILITY_STATUS", "DISC");
            extraInfo.put("UNAVAILABILITY_REASON", "Discontinued by SAP");
        } else if ("31".equals(getString(tokens, CROSS_CHAIN_SALES_STATUS)) || "31".equals(getString(tokens, DIST_CHAIN_SPEC_STATUS))) {
            //
            // temporarily unavailable
            //
            extraInfo.put("UNAVAILABILITY_DATE", THE_FUTURE);
            extraInfo.put("UNAVAILABILITY_STATUS", "UNAV");
            extraInfo.put("UNAVAILABILITY_REASON", "Temporarily Unavailable");
        } else if ("32".equals(getString(tokens, CROSS_CHAIN_SALES_STATUS)) || "32".equals(getString(tokens, DIST_CHAIN_SPEC_STATUS))) {
            //
            // out of season
            //
            extraInfo.put("UNAVAILABILITY_DATE", THE_FUTURE);
            extraInfo.put("UNAVAILABILITY_STATUS", "SEAS");
            extraInfo.put("UNAVAILABILITY_REASON", "Out Of Season");
        } else if ("34".equals(getString(tokens, CROSS_CHAIN_SALES_STATUS)) || "34".equals(getString(tokens, DIST_CHAIN_SPEC_STATUS))) {
            //
            // in testing
            //
            extraInfo.put("UNAVAILABILITY_DATE", THE_FUTURE);
            extraInfo.put("UNAVAILABILITY_STATUS", "TEST");
            extraInfo.put("UNAVAILABILITY_REASON", "Testing");
        } else {
            //
            // available now
            //
            extraInfo.put("UNAVAILABILITY_DATE", THE_FUTURE);
            extraInfo.put("UNAVAILABILITY_STATUS", "");
            extraInfo.put("UNAVAILABILITY_REASON", "");
        }
        activeMaterials.put(material, extraInfo);
        
    }
    
    private EnumATPRule decodeATPRule(String token) {
    	if ("ZP".equals(token)) {
    		return EnumATPRule.SIMULATE;
    	}
    	if ("KP".equals(token) || "ZA".equals(token)) {
    		return EnumATPRule.JIT;
    	}
    	if ("ZC".equals(token)) {
    		return EnumATPRule.COMPONENT;
    	}
    	return EnumATPRule.MATERIAL;
    }
  
}
