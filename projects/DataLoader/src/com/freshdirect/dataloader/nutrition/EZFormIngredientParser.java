/*
 * EZFormParser.java
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
public class EZFormIngredientParser extends FieldDelimitedFileParser {
    
    HashMap ingredients = null;
    
    private final static String PRODUCT_DESCR   =   "PRODUCT_DESCR";
    private final static String FILLER          =   "FILLER";
    private final static String INGREDIENTS     =   "INGREDIENTS";

    /** Creates new EZFormParser */
    public EZFormIngredientParser() {
        super();
        
        ingredients = new HashMap();
        
        fields.add(new Field(PRODUCT_DESCR,     30,  true));
        fields.add(new Field(FILLER,             2,  true));
        fields.add(new Field(INGREDIENTS,       -1,  true));

    }
    
    
    public void makeObjects(HashMap tokens) throws BadDataException {
        //
        // find skucode
        String prdDescr = getString(tokens, PRODUCT_DESCR).trim();
        if ((prdDescr == null) || "".equals(prdDescr)) {
            throw new BadDataException("No skucode supplied");
        }
        StringTokenizer stoke = new StringTokenizer(prdDescr, " ");
        if (!stoke.hasMoreTokens()) {
            throw new BadDataException("No skucode supplied");
        }
        String skuCode = stoke.nextToken().toUpperCase();
        //
        // does it look like a skucode?
        //
        // it should be at least 7 charcters in length
        if (skuCode.length() < 7) {
            throw new BadDataException(skuCode + " is not a valid skucode");
        }
        //
        // first three characters are letters
        for (int i=0; i<3; i++) {
            if (!Character.isLetter(skuCode.charAt(i))) {
                throw new BadDataException(skuCode + " is not a valid skucode");
            }
        }
        //
        // the rest are numbers
        for (int i=3; i<skuCode.length(); i++) {
            if (!Character.isDigit(skuCode.charAt(i))) {
                throw new BadDataException(skuCode + " is not a valid skucode");
            }
        }
        
        String ingred = getString(tokens, INGREDIENTS);
        if (ingred.indexOf(":") > -1) {
            ingred = ingred.substring(ingred.indexOf(":")+1).trim();
        }
        
        ingredients.put(skuCode, ingred);
        
    }
    
    public HashMap getIngredients() {
        return this.ingredients;
    }

}
