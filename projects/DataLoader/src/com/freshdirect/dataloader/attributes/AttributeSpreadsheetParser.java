/*
 * AttributeSpreadsheetParser.java
 *
 * Created on September 18, 2001, 8:00 PM
 */

package com.freshdirect.dataloader.attributes;

import java.util.ArrayList;
import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.TabDelimitedFileParser;

/**
 *
 * @author  mrose
 * @version 
 */
public class AttributeSpreadsheetParser extends TabDelimitedFileParser {
    
    ArrayList<AttributeRow> attributes = null;
    
    protected static String SAP_ID = "sapid";
    protected static String SKU_CODE = "sku_code";
    protected static String SALES_UNIT = "salesunit";
    protected static String ATR_VALUE = "description";

    /** Creates new AttributeSpreadsheetParser */
    public AttributeSpreadsheetParser() {
        super();
        
        attributes = new ArrayList<AttributeRow>();
        
        fields.add(new Field(SKU_CODE,      0, true));
        fields.add(new Field(ATR_VALUE,     0, false));
    }
    
    
    @Override
    public void makeObjects(Map<String, String> tokens) throws BadDataException {
        //
        // create the attribute row
        //
        AttributeRow ar = new AttributeRow();
        ar.rootId = getString(tokens, SKU_CODE).trim();
        ar.atrValue = getString(tokens, ATR_VALUE).trim();
        
        attributes.add(ar);
        
    }
    
    public ArrayList<AttributeRow> getAttributes() {
        return this.attributes;
    }

}
