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
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;

/**
 * a parser that deals with SAP variant price export files
 *
 * @version $Revision$
 * @author $Author$
 */
public class VariantPriceParser extends SAPParser {
    
    /** the collection of characteristic value model objects parsed from the file
     */    
    Map<ErpCharacteristicValueModel, Map<String, String>> charValues = null;
    /** the collection of characteristic value price objects parsed from the file
     */    
    Map<ErpCharacteristicValuePriceModel, Map<String, String>> charValuePrices = null;

    /** Creates new VariantPriceParser
     */
    public VariantPriceParser() {
        super();
        charValues = new HashMap<ErpCharacteristicValueModel, Map<String, String>>();
        charValuePrices = new HashMap<ErpCharacteristicValuePriceModel, Map<String, String>>();
        /*
         * from ERP Services/Technical Specs/Batch Loads/Variant_Knowledge_CSD.doc in VSS repository
         */
        fields.add(new Field(MATERIAL_NUMBER,             18, true));
        fields.add(new Field(SALES_ORGANIZATION,           4, true));
        fields.add(new Field(DISTRIBUTION_CHANNEL,         2, true));
        fields.add(new Field(CONFIGURATION_PROFILE_NAME,  30, true));
        fields.add(new Field(CLASS,                       18, true));
        fields.add(new Field(CHARACTERISTIC_NAME,         30, true));
        fields.add(new Field(CHARACTERISTIC_VALUE,        30, false));
        fields.add(new Field(CHARACTERISTIC_VALUE_DESC,   30, false));
        fields.add(new Field(DEFAULT_VALUE,                1, false));
        fields.add(new Field(PRICE,                       11, false));
        fields.add(new Field(CONDITION_UNIT,               3, false));
        fields.add(new Field(CONDITION_TYPE,               4, false));
        fields.add(new Field(CONDITION_RECORD_NUMBER,     10, false));
        fields.add(new Field(VALIDITY_END_DATE,            8, false));
        fields.add(new Field(VALIDITY_START_DATE,          8, false));
    }
    
    /** gets the collection of characteristics parsed from the file
     * @return the collection of characteristics
     */    
    public Map<ErpCharacteristicValueModel, Map<String, String>> getCharacteristicValues() {
        return charValues;
    }
    
    /** gets the collection of characteristic value prices parsed from the file
     * @return the collection of characteristic value prices
     */    
    public Map<ErpCharacteristicValuePriceModel, Map<String, String>> getCharacteristicValuePrices() {
        return charValuePrices;
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
        MATERIAL.MATERIAL_ID                            Material (18)
        CHARACTERISTIC_VALUE.SALES_ORGANIZATION         Sales Organization (4)
        CHARACTERISTIC_VALUE.DISTRIBUTION_CHANNEL       Distribution Channel (2)
        CHARACTERISTIC.NAME, CHARACTERISTIC_VALUE.NAME  Characteristic Name (30)
        CHARACTERISTIC_VALUE.CHAR_VALUE                 Characteristic Value (30)
        CHARACTERISTIC_VALUE.CHAR_VAL_DESC              Characteristic Value Description (30)
        CHARACTERISTIC_VALUE.DEFAULT_VALUE              Default Value (1)
        CHARACTERISTIC_VALUE.PRICE                      Price (11)
        CHARACTERISTIC_VALUE.CONDITION_TYPE             Condition Type (4)
        */
        
        try {
            //
            // create the characteristic value
            //
            ErpCharacteristicValueModel characVal = new ErpCharacteristicValueModel();
            characVal.setName(getString(tokens, CHARACTERISTIC_VALUE));
            characVal.setDescription(getString(tokens, CHARACTERISTIC_VALUE_DESC));
            //
            // grab the extra info the builders will need later on
            //
            HashMap<String, String> charValExtraInfo = new HashMap<String, String>();
            charValExtraInfo.put(CLASS, getString(tokens, CLASS));
            charValExtraInfo.put(CHARACTERISTIC_NAME, getString(tokens, CHARACTERISTIC_NAME));
            charValExtraInfo.put(MATERIAL_NUMBER, getString(tokens, MATERIAL_NUMBER));
            charValExtraInfo.put(CONFIGURATION_PROFILE_NAME, getString(tokens, CONFIGURATION_PROFILE_NAME));
            //
            // add the characteristic value to the collection
            //
            charValues.put(characVal, charValExtraInfo);
            //
            // create the characteristic value price
            //
            ErpCharacteristicValuePriceModel characValPrice = new ErpCharacteristicValuePriceModel();
            characValPrice.setConditionType(getString(tokens, CONDITION_TYPE));
            characValPrice.setPrice(getDouble(tokens, PRICE));
            characValPrice.setSapId(getString(tokens, CONDITION_RECORD_NUMBER));
            characValPrice.setPricingUnit(getString(tokens, CONDITION_UNIT));
            //
            // grab the extra info the builders will need later on
            //
            HashMap<String, String> charValPriceExtraInfo = new HashMap<String, String>();
            String matlNum = getString(tokens, MATERIAL_NUMBER);
            if ("".equals(matlNum))
                throw new BadDataException("No material number was supplied for characteristic value price.");
            charValPriceExtraInfo.put(MATERIAL_NUMBER, matlNum);
            String className = getString(tokens, CLASS);
            if ("".equals(className))
                throw new BadDataException("No class name was supplied for charcteristic value price.");
            charValPriceExtraInfo.put(CLASS, className);
            String charName = getString(tokens, CHARACTERISTIC_NAME);
            if ("".equals(charName))
                throw new BadDataException("No characteristic name was supplied for characteristic value price.");
            charValPriceExtraInfo.put(CHARACTERISTIC_NAME, charName);
            String charValName = getString(tokens, CHARACTERISTIC_VALUE);
            if ((!charName.endsWith("_QTY")) && "".equals(charValName))
                throw new BadDataException("No characteristic value name was supplied for characteristic value price.");
            charValPriceExtraInfo.put(CHARACTERISTIC_VALUE, charValName);
            //
            // add the characteristic value price to the collection
            // to be loaded except for those things we don't care about
            // characteristic value prices with a price of zero can be safely ignored
            //
            if (characValPrice.getPrice() > 0.0) {
                charValuePrices.put(characValPrice, charValPriceExtraInfo);
            }
             
        } catch (Exception e) {
            throw new BadDataException(e, "An exception was thrown while trying to parse a VariantPrice");
        }
        
       
        
    }
    

}
