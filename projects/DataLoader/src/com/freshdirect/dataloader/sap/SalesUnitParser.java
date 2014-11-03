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

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.erp.model.ErpSalesUnitModel;

/** a parser that deals with SAP sales unit export files
 *
 * @version $Revision$
 * @author $Author$
 */
public class SalesUnitParser extends SAPParser {

    /** a collection of sales unit objects parsed from a file
     */    
    HashMap<String, HashSet<ErpSalesUnitModel>> salesUnits = null;
    HashMap<String, ErpSalesUnitModel> salesUnitsPricingInfo = null;
    
    /** Creates new SalesUnitParser
     */
    public SalesUnitParser() {
        super();
        salesUnits = new HashMap<String, HashSet<ErpSalesUnitModel>>();
        salesUnitsPricingInfo = new HashMap<String, ErpSalesUnitModel>();
        /*
         * from ERP Services/Technical Specs/Batch Loads/Material_Export_CSD.doc in VSS repository
         */
        fields.add(new Field(DENOMINATOR,              5, true));
        fields.add(new Field(ALTERNATIVE_UOM,          3, true));
        fields.add(new Field(MEASUREMENT_UNIT_TEXT,   10, true));
        fields.add(new Field(NUMERATOR,                5, true));
        fields.add(new Field(BASE_UNIT,                3, true));
        fields.add(new Field(MATERIAL_NUMBER,         18, true));
        fields.add(new Field(DISPLAY_IND,         3, false));
    }   
    
    /** gets the collection of sales unit object parsed from a file
     * @return the sales unit models
     */    
    public HashMap<String, HashSet<ErpSalesUnitModel>> getSalesUnits() {
        return salesUnits;
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
        SALES_UNIT.DENOMINATOR              Denominator for conversion (5)
        SALES_UNIT.ALTERNATIVE_UMO          Alternative unit of measure (3)
        SALES_UNIT.MEASUREMENT_UNIT_TEXT    Measurement unit text (10)
        SALES_UNIT.NUMERATOR                Numerator for conversion (5)
        SALES_UNIT.BASE_UNIT                Base Unit (3)
        MATERIAL.MATERIAL_ID                Material (18)
         */

        try {
            //
            // create the sales unit
            //
        	if(!"w1".equalsIgnoreCase(getString(tokens,DISPLAY_IND)) || isForDisplay(tokens)){ //Ignore the row for which the display indicator 'W1' is not correct.
	            ErpSalesUnitModel salesUnit = new ErpSalesUnitModel();
	            salesUnit.setDenominator(getInt(tokens, DENOMINATOR));
	            salesUnit.setAlternativeUnit(getString(tokens, ALTERNATIVE_UOM));
	            salesUnit.setDescription(getString(tokens, MEASUREMENT_UNIT_TEXT));
	            salesUnit.setNumerator(getInt(tokens, NUMERATOR));
	            salesUnit.setBaseUnit(getString(tokens, BASE_UNIT));
	            salesUnit.setDisplayInd(isForDisplay(tokens));
	            //
	            // which material does this sales unit belong to?
	            //
	            String matlNumber = getString(tokens, MATERIAL_NUMBER);
	            //
	            // since there are multiple sales units for each material
	            // we need to make the sales units we collect a hash of sets
	            //
	            HashSet<ErpSalesUnitModel> units = null;
	            if (!salesUnits.containsKey(matlNumber)) {
	                //
	                // no sales units yet for this material
	                // create a new set and add it to the collection
	                //
	                units = new HashSet<ErpSalesUnitModel>();
	                salesUnits.put(matlNumber, units);
	            } else {
	                //
	                // find the sales unit set for this material
	                //
	                units = salesUnits.get(matlNumber);
	            }
	            
	            //[APPDEV-3438] set the unit pricing info to each sales unit record.
	            if("U1".equalsIgnoreCase(getString(tokens,DISPLAY_IND))){
//	            	if(!salesUnitsPricingInfo.containsKey(matlNumber)){
	            		salesUnitsPricingInfo.put(matlNumber, salesUnit);
//	            	}
	           
	            }else{
	            	//
		            // add the new sales unit to the set
		            //
	            	units.add(salesUnit);
	            }
	            
	            if(salesUnitsPricingInfo.containsKey(matlNumber)){
	            	ErpSalesUnitModel unitPricingInfo =salesUnitsPricingInfo.get(matlNumber);
		        	for (ErpSalesUnitModel erpSalesUnitModel : units) {
						erpSalesUnitModel.setUnitPriceNumerator(unitPricingInfo.getNumerator());
						erpSalesUnitModel.setUnitPriceDenominator(unitPricingInfo.getDenominator());
						erpSalesUnitModel.setUnitPriceUOM(unitPricingInfo.getAlternativeUnit());
						erpSalesUnitModel.setUnitPriceDescription(unitPricingInfo.getDescription());
					}
	            }
        	}
            
        } catch (Exception e) {
            throw new BadDataException(e, "An exception was thrown while trying to parse a SalesUnit");
        }
        
        
        
        
    }

	/**
	 * @param tokens
	 * @return
	 * @throws BadDataException
	 */
	private boolean isForDisplay(Map<String, String> tokens) throws BadDataException {
		return ("EA".equalsIgnoreCase(getString(tokens, BASE_UNIT)))&&("LB".equalsIgnoreCase(getString(tokens, ALTERNATIVE_UOM)))&&("w1".equalsIgnoreCase(getString(tokens,DISPLAY_IND)));
	}
    
    @Override
    protected int tokenize(String line, Map<String, String> retval, int startPosition,
			String name, int length) throws BadDataException{
    	if(name.equals(DISPLAY_IND)){    	
    		int endPosition = startPosition+length;
    		if(line.length() >= endPosition){
    			retval.put(name, line.substring(startPosition, endPosition).trim());
    		}
    			startPosition = endPosition;
    	}else{
    		startPosition = super.tokenize(line, retval, startPosition, name, length);
    	}
    	
    	return startPosition;
    }

}
