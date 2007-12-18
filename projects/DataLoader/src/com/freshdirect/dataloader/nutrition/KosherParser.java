/*
 * AttributeSpreadsheetParser.java
 *
 * Created on September 18, 2001, 8:00 PM
 */

package com.freshdirect.dataloader.nutrition;

import java.util.*;

import com.freshdirect.dataloader.*;

/**
 *
 * @author  mrose
 * @version 
 */
public class KosherParser extends TabDelimitedFileParser {
    
    ArrayList kosherInfo = null;
    
    protected static String SKU_CODE    = "sku_code";
    protected static String K_SYMBOL    = "kosher_symbol";
    protected static String K_TYPE      = "kosher_type";
    protected static String DESCRIPTION = "description";
    protected static String NOTES       = "notes";

    /** Creates new AttributeSpreadsheetParser */
    public KosherParser() {
        super();
        
        kosherInfo = new ArrayList();
        
        fields.add(new Field(SKU_CODE, 0, true));
        fields.add(new Field(DESCRIPTION, 0, true));
        fields.add(new Field(K_SYMBOL, 0, true));
        fields.add(new Field(K_TYPE, 0, false));
        fields.add(new Field(NOTES, 0, false));
        
    }
    
    
    public void makeObjects(HashMap tokens) throws BadDataException {
        //
        // create the attribute row
        //
        HashMap ingr = new HashMap();
        ingr.put(SKU_CODE, getString(tokens, SKU_CODE).trim());
        ingr.put(K_SYMBOL, getString(tokens, K_SYMBOL).trim());
        ingr.put(K_TYPE,   getString(tokens, K_TYPE).trim());
        
        kosherInfo.add(ingr);
        
    }
    
    public ArrayList getKosherInfo() {
        return this.kosherInfo;
    }

}
