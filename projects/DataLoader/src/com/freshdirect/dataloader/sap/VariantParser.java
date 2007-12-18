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

import com.freshdirect.erp.model.*;
import com.freshdirect.dataloader.*;

/** a parser that deals with SAP variant export files
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class VariantParser extends SAPParser {
    
    /** a collection of characteristic model objects parsed from the file
     */    
    HashMap characteristics = null;
    
    /** a collection of material-class associations parsed froma file
     */    
    HashMap materialClasses = null;
    
    /** Creates new VariantParser
     */
    public VariantParser() {
        super();
        characteristics = new HashMap();
        materialClasses = new HashMap();
        /*
         * from ERP Services/Technical Specs/Batch Loads/Variant_Export_CSD.doc in VSS repository
         */
        fields.add(new Field(CONFIGURATION_PROFILE_NAME,  30, true));
        fields.add(new Field(CLASS,                       18, true));
        fields.add(new Field(CHARACTERISTIC_NAME,         30, true));
        fields.add(new Field(CHARACTERISTIC_VALUE,        30, false));
        fields.add(new Field(DEFAULT_VALUE,                1, false));
        fields.add(new Field(MATERIAL_NUMBER,             18, true));
    }
    
    /** gets the collection of characteristic model objects parsed from a file
     * @return the collection of characteristics
     */    
    public HashMap getCharacteristics() {
        return characteristics;
    }
    
    /** gets the collection of material-class associations parsed from a file
     * @return the material-class associations
     */    
    public HashMap getMaterialClasses() {
        return materialClasses;
    }
    
    /** creates model objects from the supplied tokens
     * @param tokens a HashMap of tokens parsed from a line of an export file
     * @throws BadDataException an problems encountered while assembling the tokens into model objects
     */
    public void makeObjects(HashMap tokens) throws BadDataException {
        
        /*
         * from ERP Services/Technical Specs/Mapping Docs/ERPS_SAP_BATCH_MAP.xls in VSS repository
         *
        CLASS.CLASS_NAME                                Class (18)
        CHARACTERISTIC.NAME, CHARACTERISTIC_VALUE.NAME  Characteristic Name (30)
        CHARACTERISTIC_VALUE.CHAR_VALUE                 Characteristic Value (30)
        CHARACTERISTIC_VALUE.DEFAULT_VALUE              Default Value (1)
        MATERIAL.MATERIAL_ID                            Material (18)
         */
        
        //
        // create the characteristic
        //
        ErpCharacteristicModel charac = new ErpCharacteristicModel();
        charac.setName(getString(tokens, CHARACTERISTIC_NAME));
        //
        // grab the extra info the builders will need later on
        //
        HashMap extraInfo = new HashMap();
        extraInfo.put(CLASS, getString(tokens, CLASS));
        extraInfo.put(MATERIAL_NUMBER, getString(tokens, MATERIAL_NUMBER));
        extraInfo.put(CONFIGURATION_PROFILE_NAME, getString(tokens, CONFIGURATION_PROFILE_NAME));
        //
        // add the characteristic to the collection
        //
        characteristics.put(charac, extraInfo);
        //
        // add to material to class mapping
        //
        String matlNumber = getString(tokens, MATERIAL_NUMBER);
        String className = getString(tokens, CLASS);
        HashSet classes = null;
        if (!materialClasses.containsKey(matlNumber)) {
            classes = new HashSet();
            materialClasses.put(matlNumber, classes);
        }
        classes = (HashSet) materialClasses.get(matlNumber);
        classes.add(className);
        
    }
    
    
}
