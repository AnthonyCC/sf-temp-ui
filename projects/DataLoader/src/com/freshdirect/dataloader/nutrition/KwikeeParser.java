/*
 * NutritionParser.java
 *
 * Created on August 20, 2001, 8:39 PM
 */

package com.freshdirect.dataloader.nutrition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.TabDelimitedFileParser;

/**
 *
 * @author  knadeem
 * @version
 */
public class KwikeeParser extends TabDelimitedFileParser {
    
    private ArrayList<ErpNutritionModel> nutritionModels = null;
    
    /** Creates new NutritionParser */
    public KwikeeParser() {
        super();
        nutritionModels = new ArrayList<ErpNutritionModel>();
    }
    
    
    /**
     * provides special line by line handling for subclasses that need to massage the
     * data a bit before parsing
     *
     * @param lineNumber the line number of the file the parsing in currently operating on
     * @param line the contents of the current line before it is tokenized/parsed
     */
    @Override
    protected boolean processLine(int lineNumber, String line) {
        System.out.println(lineNumber);
        boolean process = true;
        if (lineNumber == 1) {
            buildFieldList(line);
            process = false;
        }
        return process;
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
    @Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
        ErpNutritionModel nutritionModel = new ErpNutritionModel();
        nutritionModel.setUpc(getString(tokens, "UPC"));
        //clean up the tokens hashmap
        Iterator<String> it = tokens.keySet().iterator();
        while (it.hasNext()) {
            String name = it.next();
            if (name.endsWith("UOM")) {
                String fdKey = KwikeeToFDMapping.getFDUomKey(name);
                if(fdKey != null)
                    nutritionModel.setUomFor(fdKey, getString(tokens, name).toLowerCase());
                it.remove();
            }
        }
        it = tokens.keySet().iterator();
        while (it.hasNext()) {
            String name = it.next();
            String fdKey = KwikeeToFDMapping.getFDValueKey(name);
            if(fdKey != null){
                double value = getDouble(tokens, name);
                nutritionModel.setValueFor(fdKey, value);
            }
        }
        nutritionModel.setValueFor(ErpNutritionType.SOURCE, 0.0);
        nutritionModel.setUomFor(ErpNutritionType.SOURCE, "Kwikee");
        nutritionModels.add(nutritionModel);
        
    }
    
    protected void buildFieldList(String line){
        StringTokenizer st = new StringTokenizer(line, "\t");
        while(st.hasMoreTokens()){
            String f = st.nextToken();
            fields.add(new Field(f.toUpperCase(), 0, true));
        }
    }
    
    public ArrayList<ErpNutritionModel> getNutritionModels(){
        return nutritionModels;
    }
    
    static class KwikeeToFDMapping{
        public static HashMap<String, String> kwikeeToFD = new HashMap<String, String>();
        public static HashMap<String, String> kwikeeUomToFDUom = new HashMap<String, String>();
        static{
            kwikeeUomToFDUom.put("SERVING_SIZE_UOM", ErpNutritionType.SERVING_SIZE);
            kwikeeUomToFDUom.put("SATURATED_FAT_UOM", ErpNutritionType.TOTAL_SATURATED_FAT_QUANTITY);
            kwikeeUomToFDUom.put("TOTAL_FAT_UOM", ErpNutritionType.TOTAL_FAT_QUANTITY);
            kwikeeUomToFDUom.put("CHOLESTEROL_UOM", ErpNutritionType.TOTAL_CHOLESTEROL_QUANTITY);
            kwikeeUomToFDUom.put("SODIUM_UOM", ErpNutritionType.TOTAL_SODIUM_QUANTITY);
            kwikeeUomToFDUom.put("POTASSIUM_UOM", ErpNutritionType.TOTAL_POTASSIUM_QUANTITY);
            kwikeeUomToFDUom.put("CARBOHYDRATES_UOM", ErpNutritionType.TOTAL_CARBOHYDRATE_QUANTITY);
            kwikeeUomToFDUom.put("FIBER_UOM", ErpNutritionType.TOTAL_DIETARY_FIBER_QUANTITY);
            kwikeeUomToFDUom.put("SUGARS_UOM", ErpNutritionType.TOTAL_SUGAR_QUANTITY);
            kwikeeUomToFDUom.put("PROTEIN_UOM", ErpNutritionType.TOTAL_PROTEIN_QUANTITY);
            
            kwikeeToFD.put("SERVING_SIZE", ErpNutritionType.SERVING_SIZE);
            kwikeeToFD.put("SERVINGS_PER_CONTAINER", ErpNutritionType.NUMBER_OF_SERVINGS);
            kwikeeToFD.put("TOTAL_CALORIES_PER_SERVING", ErpNutritionType.TOTAL_CALORIES);
            kwikeeToFD.put("FAT_CALORIES_PER_SERVING", ErpNutritionType.TOTAL_CALORIES_FROM_FAT);
            kwikeeToFD.put("TOTAL_FAT_PER_SERVING", ErpNutritionType.TOTAL_FAT_QUANTITY);
            kwikeeToFD.put("DVP_TOTAL_FAT", ErpNutritionType.DAILY_FAT_VALUE);
            kwikeeToFD.put("SATURATED_FAT_PER_SERVING", ErpNutritionType.TOTAL_SATURATED_FAT_QUANTITY);
            kwikeeToFD.put("DVP_SATURATED_FAT", ErpNutritionType.DAILY_SATURATED_FAT_VALUE);
            //kwikeeToFD.put(, ErpNutritionType.TOTAL_POLYUNSATURATED_FAT_QUANTITY);
            //kwikeeToFD.put(, ErpNutritionType.DAILY_POLYUNSATURATED_FAT_VALUE);
            //kwikeeToFD.put(, ErpNutritionType.TOTAL_MONOSATURATED_FAT_QUANTITY);
            //kwikeeToFD.put(, ErpNutritionType.DAILY_MONOSATURATED_FAT_VALUE);
            kwikeeToFD.put("CHOLESTEROL_PER_SERVING", ErpNutritionType.TOTAL_CHOLESTEROL_QUANTITY);
            kwikeeToFD.put("DVP_CHOLESTEROL", ErpNutritionType.DAILY_CHOLESTROL_VALUE);
            kwikeeToFD.put("SODIUM_PER_SERVING", ErpNutritionType.TOTAL_SODIUM_QUANTITY);
            kwikeeToFD.put("DVP_SODIUM", ErpNutritionType.DAILY_SODIUM_VALUE);
            kwikeeToFD.put("POTASSIUM_PER_SERVING", ErpNutritionType.TOTAL_POTASSIUM_QUANTITY);
            kwikeeToFD.put("DVP_POTASSIUM", ErpNutritionType.DAILY_POTASSIUM_VALUE);
            kwikeeToFD.put("CARBOHYDRATES_PER_SERVING", ErpNutritionType.TOTAL_CARBOHYDRATE_QUANTITY);
            kwikeeToFD.put("DVP_CARBOHYDRATES", ErpNutritionType.DAILY_CARBOHYDRATE_VALUE);
            kwikeeToFD.put("FIBER_PER_SERVING", ErpNutritionType.TOTAL_DIETARY_FIBER_QUANTITY);
            kwikeeToFD.put("DVP_DIETARY_FIBER", ErpNutritionType.DAILY_DIETARY_FIBER_VALUE);
            //kwikeeToFD.put(, ErpNutritionType.TOTAL_INSOLUBLE_FIBER_QUANTITY);
            //kwikeeToFD.put(, ErpNutritionType.DAILY_INSOLUBLE_FIBER_VALUE);
            //kwikeeToFD.put(, ErpNutritionType.TOTAL_SOLUBLE_FIBER_QUANTITY);
            //kwikeeToFD.put(, ErpNutritionType.DAILY_SOLUBLE_FIBER_VALUE);
            kwikeeToFD.put("SUGARS_PER_SERVING", ErpNutritionType.TOTAL_SUGAR_QUANTITY);
            //kwikeeToFD.put("DVP_SUGAR", ErpNutritionType.DAILY_SUGAR_VALUE);
            //kwikeeToFD.put(, ErpNutritionType.TOTAL_SUGAR_ALCOHOL_QUANTITY);
            //kwikeeToFD.put(, ErpNutritionType.DAILY_SUGAR_ALCOHOL_VALUE);
            //kwikeeToFD.put(, ErpNutritionType.TOTAL_OTHER_CARBOHYDRATE_QUANTITY);
            //kwikeeToFD.put(, ErpNutritionType.DAILY_OTHER_CARBOHYDRATE_VALUE);
            kwikeeToFD.put("PROTEIN_PER_SERVING", ErpNutritionType.TOTAL_PROTEIN_QUANTITY);
            //kwikeeToFD.put("DVP_PROTEIN", ErpNutritionType.DAILY_PROTEIN_VALUE);
            kwikeeToFD.put("DVP_VITAMIN_A", ErpNutritionType.VITAMIN_A);
            kwikeeToFD.put("DVP_VITAMIN_C", ErpNutritionType.VITAMIN_C);
            kwikeeToFD.put("DVP_CALCIUM", ErpNutritionType.CALCIUM);
            kwikeeToFD.put("DVP_IRON", ErpNutritionType.IRON);
            kwikeeToFD.put("DVP_VITAMIN_D", ErpNutritionType.VITAMIN_D);
            kwikeeToFD.put("DVP_THIAMIN", ErpNutritionType.THIAMINE);
            kwikeeToFD.put("DVP_RIBOFLAVIN", ErpNutritionType.RIBFLAVIN);
            kwikeeToFD.put("DVP_NIACIN", ErpNutritionType.NIACIN);
            kwikeeToFD.put("DVP_VITAMIN_B6", ErpNutritionType.VITAMIN_B6);
            kwikeeToFD.put("DVP_VITAMIN_B12", ErpNutritionType.VITAMIN_B12);
            kwikeeToFD.put("DVP_PHOSPHOROUS", ErpNutritionType.PHOSPHOROUS);
            kwikeeToFD.put("DVP_MAGNESIUM", ErpNutritionType.MAGNESIUM);
            kwikeeToFD.put("DVP_ZINC", ErpNutritionType.ZINC);
            kwikeeToFD.put("DVP_COPPER", ErpNutritionType.COPPER);
            //kwikeeToFD.put(, ErpNutritionType.BIOTIN);
            kwikeeToFD.put("DVP_FOLIC_ACID", ErpNutritionType.FOLIC_ACID);
            kwikeeToFD.put("DVP_IODINE", ErpNutritionType.IODINE);
            //kwikeeToFD.put(, ErpNutritionType.PANTOTHENIC_ACID);
            kwikeeToFD.put("DVP_VITAMIN_E", ErpNutritionType.VITAMIN_E);
            //kwikeeToFD.put(, ErpNutritionType.VITAMIN_K);
            //kwikeeToFD.put(, ErpNutritionType.PETS_CRUDE_PROTEIN);
            //kwikeeToFD.put(, ErpNutritionType.PETS_CRUDE_FAT);
            //kwikeeToFD.put(, ErpNutritionType.PETS_CRUDE_FIBER);
            //kwikeeToFD.put(, ErpNutritionType.PETS_CRUDE_MOISTURE);
            //kwikeeToFD.put(, ErpNutritionType.PETS_ASH);
            //kwikeeToFD.put(, ErpNutritionType.PETS_TAURINE);
            //kwikeeToFD.put(, ErpNutritionType.PETS_CALCIUM);
        }
        
        public static String getFDUomKey(String kwikeeKey){
            return kwikeeUomToFDUom.get(kwikeeKey);
        }
        public static String getFDValueKey(String kwikeeKey){
            return kwikeeToFD.get(kwikeeKey);
        }
        
        
    }
    
}