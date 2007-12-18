/*
 * VirtualProduct.java
 *
 * 
 */

package com.freshdirect.dataloader.sap;

import java.util.*;

import com.freshdirect.dataloader.*;

/**
 *
 * @author  mrose
 * @version
 */
public class VirtualProductParser extends TabDelimitedFileParser {
    
    private HashMap products = null;
    
    public static String ORIGINAL_SKU   = "ORIGINAL_SKU";
    public static String VIRTUAL_SKU    = "VIRTUAL_SKU";
    public static String NAME           = "NAME";
    public static String SALES_UNITS    = "SALES_UNITS";
    public static String CHAR_VALUES    = "CHAR_VALUES";
    
    /** Creates new NutritionParser */
    public VirtualProductParser() {
        super();
        products = new HashMap();
        
        fields.add(new Field(ORIGINAL_SKU,  0, true));
        fields.add(new Field(VIRTUAL_SKU,   0, true));
        fields.add(new Field(NAME,          0, false));
        fields.add(new Field(SALES_UNITS,   0, false));
        fields.add(new Field(CHAR_VALUES,   0, false));
    }
    
    /**
     * a template method that must be defined by implementors
     * subclasses will know how to assemble model objects
     * from a a hash of tokens
     * @param tokens a HashMap containing parsed tokens from a single line
     * of a text file, keyed by their field names
     * @throws BadDataException an problems while trying to assemble objects from the
     * supplied tokens
     */
    protected void makeObjects(HashMap tokens) throws BadDataException {
        HashMap extraInfo = new HashMap();
        
        String vSku = getString(tokens, VIRTUAL_SKU);
        
        extraInfo.put(ORIGINAL_SKU, getString(tokens, ORIGINAL_SKU));
        
        StringTokenizer suStoke = new StringTokenizer(getString(tokens, SALES_UNITS), ",");
        LinkedList sUnits = new LinkedList();
        while (suStoke.hasMoreTokens()) {
            sUnits.add(suStoke.nextToken());
        }
        extraInfo.put(SALES_UNITS, sUnits);
        
        StringTokenizer chStoke = new StringTokenizer(getString(tokens, CHAR_VALUES), ",");
        ArrayList cVals = new ArrayList();
        while (chStoke.hasMoreTokens()) {
            String cvPair = chStoke.nextToken();
            int comma = cvPair.indexOf(":");
            if (comma <= 0) throw new BadDataException("Can't understand characteristic/characteristic value pair \"" + cvPair + "\"");
            String charac = cvPair.substring(0, comma);
            String charVal = cvPair.substring(comma+1);
            HashMap cVal = new HashMap();
            cVal.put(charac, charVal);
            cVals.add(cVal);
        }
        extraInfo.put(CHAR_VALUES, cVals);
        
        products.put(vSku, extraInfo);
        
    }
    
    
    public HashMap getProducts(){
        return products;
    }
    
    
    
}