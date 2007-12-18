/*
 * EZFormParser.java
 *
 * Created on September 18, 2001, 8:00 PM
 */

package com.freshdirect.dataloader.nutrition;

import java.util.*;

import com.freshdirect.dataloader.*;
import com.freshdirect.content.nutrition.*;

/**
 *
 * @author  mrose
 * @version 
 */
public class EZFormNutritionParser extends FieldDelimitedFileParser {
    
    HashMap nutrition = null;
    
    private final static String PRODUCT_DESCR   =   "PRODUCT_DESCR";
    private final static String FILLER          =   "FILLER";

    /** Creates new EZFormParser */
    public EZFormNutritionParser() {
        super();
        
        nutrition = new HashMap();
        
        fields.add(new Field(PRODUCT_DESCR,                                    30,  true));
        fields.add(new Field(FILLER,                                            2,  true));
        fields.add(new Field(ErpNutritionType.SERVING_SIZE,                    54,  true));
        fields.add(new Field(ErpNutritionType.NUMBER_OF_SERVINGS,              15,  true));
        fields.add(new Field(ErpNutritionType.TOTAL_CALORIES,                   5,  true));
        fields.add(new Field(ErpNutritionType.TOTAL_CALORIES_FROM_FAT,          5,  true));
        fields.add(new Field(FILLER,                                            5,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_FAT_QUANTITY,               5,  true));
        fields.add(new Field(ErpNutritionType.DAILY_FAT_VALUE,                  5,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_SATURATED_FAT_QUANTITY,     5,  true));
        fields.add(new Field(ErpNutritionType.DAILY_SATURATED_FAT_VALUE,        5,  false));
        fields.add(new Field(FILLER,                                           15,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_CHOLESTEROL_QUANTITY,       5,  true));
        fields.add(new Field(ErpNutritionType.DAILY_CHOLESTROL_VALUE,           5,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_SODIUM_QUANTITY,            5,  true));
        fields.add(new Field(ErpNutritionType.DAILY_SODIUM_VALUE,               5,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_POTASSIUM_QUANTITY,         5,  false));
        fields.add(new Field(ErpNutritionType.DAILY_POTASSIUM_VALUE,            5,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_CARBOHYDRATE_QUANTITY,      5,  true));
        fields.add(new Field(ErpNutritionType.DAILY_CARBOHYDRATE_VALUE,         5,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_DIETARY_FIBER_QUANTITY,     5,  true));
        fields.add(new Field(ErpNutritionType.DAILY_DIETARY_FIBER_VALUE,        5,  false));
        fields.add(new Field(FILLER,                                           10,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_SUGAR_QUANTITY,             5,  true));
        fields.add(new Field(FILLER,                                           10,  false));
        fields.add(new Field(ErpNutritionType.TOTAL_PROTEIN_QUANTITY,           5,  true));
        fields.add(new Field(FILLER,                                            5,  false));
        fields.add(new Field(ErpNutritionType.VITAMIN_A,                        5,  true));
        fields.add(new Field(ErpNutritionType.VITAMIN_C,                        5,  true));
        fields.add(new Field(ErpNutritionType.CALCIUM,                          5,  true));
        fields.add(new Field(ErpNutritionType.IRON,                             5,  true));

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
        ErpNutritionModel nutri = new ErpNutritionModel();
        nutri.setSkuCode(skuCode);
        String servSize = getString(tokens, ErpNutritionType.SERVING_SIZE).trim();
        if ("".equals(servSize)) throw new BadDataException("No serving size specified");
        int firstSpace = servSize.indexOf(" ");
        if (firstSpace == -1) {
            nutri.setValueFor(ErpNutritionType.SERVING_SIZE, Double.parseDouble(servSize));
        } else {
            String numPortion = servSize.substring(0, firstSpace).trim();
            try {
                nutri.setValueFor(ErpNutritionType.SERVING_SIZE, Double.parseDouble(numPortion));
            } catch (NumberFormatException nfe) {
                int slashPos = numPortion.indexOf("/");
                if (slashPos == -1) throw new BadDataException(numPortion + " is not a number");
                nutri.setValueFor(ErpNutritionType.SERVING_SIZE, Double.parseDouble(numPortion.substring(0, slashPos)) / Double.parseDouble(numPortion.substring(slashPos+1, numPortion.length())));
            }
            
            String uom = servSize.substring(firstSpace+1, servSize.length()).trim();
            if (uom.indexOf("(") > -1) {
                // remove any extra junk in parathesis
                uom = uom.substring(0, uom.indexOf("(")).trim();
            }
            while (uom.length() > 30) {
                // if it's too long, keep cutting off words from the end until it fits
                int lastSpace = uom.lastIndexOf(" ");
                if (lastSpace == -1) break;
                uom = uom.substring(0, lastSpace).trim();
            }
            
            nutri.setUomFor(ErpNutritionType.SERVING_SIZE, uom);
        }
        try {
            nutri.setValueFor(ErpNutritionType.NUMBER_OF_SERVINGS,          getDouble(tokens, ErpNutritionType.NUMBER_OF_SERVINGS));
        } catch (BadDataException bde) {
            nutri.setValueFor(ErpNutritionType.NUMBER_OF_SERVINGS,          1);
        }
        nutri.setValueFor(ErpNutritionType.TOTAL_CALORIES,                  getDouble(tokens, ErpNutritionType.TOTAL_CALORIES));
        nutri.setValueFor(ErpNutritionType.TOTAL_CALORIES_FROM_FAT,         getDouble(tokens, ErpNutritionType.TOTAL_CALORIES_FROM_FAT));
        
        nutri.setValueFor(ErpNutritionType.TOTAL_FAT_QUANTITY,              getDouble(tokens, ErpNutritionType.TOTAL_FAT_QUANTITY));
        nutri.setUomFor(ErpNutritionType.TOTAL_FAT_QUANTITY,                "g");
        
        nutri.setValueFor(ErpNutritionType.DAILY_FAT_VALUE,                 getDouble(tokens, ErpNutritionType.DAILY_FAT_VALUE));
        nutri.setUomFor(ErpNutritionType.DAILY_FAT_VALUE,                   "%");
        
        nutri.setValueFor(ErpNutritionType.TOTAL_SATURATED_FAT_QUANTITY,    getDouble(tokens, ErpNutritionType.TOTAL_SATURATED_FAT_QUANTITY));
        nutri.setUomFor(ErpNutritionType.TOTAL_SATURATED_FAT_QUANTITY,      "g");
        
        nutri.setValueFor(ErpNutritionType.DAILY_SATURATED_FAT_VALUE,       getDouble(tokens, ErpNutritionType.DAILY_SATURATED_FAT_VALUE));
        nutri.setUomFor(ErpNutritionType.DAILY_SATURATED_FAT_VALUE,         "%");
        
        nutri.setValueFor(ErpNutritionType.TOTAL_CHOLESTEROL_QUANTITY,      getDouble(tokens, ErpNutritionType.TOTAL_CHOLESTEROL_QUANTITY));
        nutri.setUomFor(ErpNutritionType.TOTAL_CHOLESTEROL_QUANTITY,        "mg");
        
        nutri.setValueFor(ErpNutritionType.DAILY_CHOLESTROL_VALUE,          getDouble(tokens, ErpNutritionType.DAILY_CHOLESTROL_VALUE));
        nutri.setUomFor(ErpNutritionType.DAILY_CHOLESTROL_VALUE,            "%");
        
        nutri.setValueFor(ErpNutritionType.TOTAL_SODIUM_QUANTITY,           getDouble(tokens, ErpNutritionType.TOTAL_SODIUM_QUANTITY));
        nutri.setUomFor(ErpNutritionType.TOTAL_SODIUM_QUANTITY,             "mg");
        
        nutri.setValueFor(ErpNutritionType.DAILY_SODIUM_VALUE,              getDouble(tokens, ErpNutritionType.DAILY_SODIUM_VALUE));
        nutri.setUomFor(ErpNutritionType.DAILY_SODIUM_VALUE,                "%");
        
        if (!"".equals((String)tokens.get(ErpNutritionType.TOTAL_POTASSIUM_QUANTITY))) {
            nutri.setValueFor(ErpNutritionType.TOTAL_POTASSIUM_QUANTITY,        getDouble(tokens, ErpNutritionType.TOTAL_POTASSIUM_QUANTITY));
            nutri.setUomFor(ErpNutritionType.TOTAL_POTASSIUM_QUANTITY,          "mg");
        }
        
        if (!"".equals((String)tokens.get(ErpNutritionType.DAILY_POTASSIUM_VALUE))) {
            nutri.setValueFor(ErpNutritionType.DAILY_POTASSIUM_VALUE,           getDouble(tokens, ErpNutritionType.DAILY_POTASSIUM_VALUE));
            nutri.setUomFor(ErpNutritionType.DAILY_POTASSIUM_VALUE,             "%");
        }
        
        nutri.setValueFor(ErpNutritionType.TOTAL_CARBOHYDRATE_QUANTITY,     getDouble(tokens, ErpNutritionType.TOTAL_CARBOHYDRATE_QUANTITY));
        nutri.setUomFor(ErpNutritionType.TOTAL_CARBOHYDRATE_QUANTITY,       "g");
        
        nutri.setValueFor(ErpNutritionType.DAILY_CARBOHYDRATE_VALUE,        getDouble(tokens, ErpNutritionType.DAILY_CARBOHYDRATE_VALUE));
        nutri.setUomFor(ErpNutritionType.DAILY_CARBOHYDRATE_VALUE,          "%");
        
        nutri.setValueFor(ErpNutritionType.TOTAL_DIETARY_FIBER_QUANTITY,    getDouble(tokens, ErpNutritionType.TOTAL_DIETARY_FIBER_QUANTITY));
        nutri.setUomFor(ErpNutritionType.TOTAL_DIETARY_FIBER_QUANTITY,      "g");
        
        nutri.setValueFor(ErpNutritionType.DAILY_DIETARY_FIBER_VALUE,       getDouble(tokens, ErpNutritionType.DAILY_DIETARY_FIBER_VALUE));
        nutri.setUomFor(ErpNutritionType.DAILY_DIETARY_FIBER_VALUE,         "%");
        
        nutri.setValueFor(ErpNutritionType.TOTAL_SUGAR_QUANTITY,            getDouble(tokens, ErpNutritionType.TOTAL_SUGAR_QUANTITY));
        nutri.setUomFor(ErpNutritionType.TOTAL_SUGAR_QUANTITY,              "g");
        
        nutri.setValueFor(ErpNutritionType.TOTAL_PROTEIN_QUANTITY,          getDouble(tokens, ErpNutritionType.TOTAL_PROTEIN_QUANTITY));
        nutri.setUomFor(ErpNutritionType.TOTAL_PROTEIN_QUANTITY,            "g");
        
        nutri.setValueFor(ErpNutritionType.VITAMIN_A,                       getDouble(tokens, ErpNutritionType.VITAMIN_A));
        nutri.setUomFor(ErpNutritionType.VITAMIN_A,                         "%");
        
        nutri.setValueFor(ErpNutritionType.VITAMIN_C,                       getDouble(tokens, ErpNutritionType.VITAMIN_C));
        nutri.setUomFor(ErpNutritionType.VITAMIN_C,                         "%");
        
        nutri.setValueFor(ErpNutritionType.CALCIUM,                         getDouble(tokens, ErpNutritionType.CALCIUM));
        nutri.setUomFor(ErpNutritionType.CALCIUM,                           "%");
        
        nutri.setValueFor(ErpNutritionType.IRON,                            getDouble(tokens, ErpNutritionType.IRON));
        nutri.setUomFor(ErpNutritionType.IRON,                              "%");
        
        nutrition.put(nutri.getSkuCode(), nutri);
        
    }
    
    public HashMap getNutrition() {
        return this.nutrition;
    }

}
