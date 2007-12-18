package com.freshdirect.content.nutrition;

import java.util.*;

public class ErpNutritionType {
    
    public final static String SOURCE = "SOURCE";
    public final static String SERVING_SIZE = "SERVING_SIZE";
    public final static String SERVING_WEIGHT = "SERVING_WEIGHT";
    public final static String NUMBER_OF_SERVINGS = "NUMBER_OF_SERVINGS";
    public final static String TOTAL_CALORIES = "TOTAL_CALORIES";
    public final static String TOTAL_CALORIES_FROM_FAT = "TOTAL_CALORIES_FROM_FAT";
    public final static String TOTAL_FAT_QUANTITY = "TOTAL_FAT_QUANTITY";
    public final static String DAILY_FAT_VALUE = "DAILY_FAT_VALUE";
    public final static String TOTAL_SATURATED_FAT_QUANTITY = "TOTAL_SATURATED_FAT_QUANTITY";
    public final static String DAILY_SATURATED_FAT_VALUE = "DAILY_SATURATED_FAT_VALUE";
    public final static String TOTAL_STEARIC_ACID_QUANTITY = "TOTAL_STEARIC_ACID_QUANTITY";
    public final static String TOTAL_POLYUNSATURATED_FAT_QUANTITY = "TOTAL_POLYUNSATURATED_FAT_QUANTITY";
    public final static String TOTAL_MONOSATURATED_FAT_QUANTITY = "TOTAL_MONOSATURATED_FAT_QUANTITY";
    public final static String TOTAL_CHOLESTEROL_QUANTITY = "TOTAL_CHOLESTEROL_QUANTITY";
    public final static String DAILY_CHOLESTROL_VALUE = "DAILY_CHOLESTROL_VALUE";
    public final static String TOTAL_SODIUM_QUANTITY = "TOTAL_SODIUM_QUANTITY";
    public final static String DAILY_SODIUM_VALUE = "DAILY_SODIUM_VALUE";
    public final static String TRANS_FAT = "TRANS_FAT";
    public final static String TOTAL_POTASSIUM_QUANTITY = "TOTAL_POTASSIUM_QUANTITY";
    public final static String DAILY_POTASSIUM_VALUE = "DAILY_POTASSIUM_VALUE";
    public final static String TOTAL_CARBOHYDRATE_QUANTITY = "TOTAL_CARBOHYDRATE_QUANTITY";
    public final static String DAILY_CARBOHYDRATE_VALUE = "DAILY_CARBOHYDRATE_VALUE";
    public final static String TOTAL_DIETARY_FIBER_QUANTITY = "TOTAL_DIETARY_FIBER_QUANTITY";
    public final static String DAILY_DIETARY_FIBER_VALUE = "DAILY_DIETARY_FIBER_VALUE";
    public final static String TOTAL_INSOLUBLE_FIBER_QUANTITY = "TOTAL_INSOLUBLE_FIBER_QUANTITY";
    public final static String TOTAL_SOLUBLE_FIBER_QUANTITY = "TOTAL_SOLUBLE_FIBER_QUANTITY";
    public final static String TOTAL_SUGAR_QUANTITY = "TOTAL_SUGAR_QUANTITY";
    public final static String TOTAL_SUGAR_ALCOHOL_QUANTITY = "TOTAL_SUGAR_ALCOHOL_QUANTITY";
    public final static String TOTAL_OTHER_CARBOHYDRATE_QUANTITY = "TOTAL_OTHER_CARBOHYDRATE_QUANTITY";
    public final static String NET_CARBOHYDRATES = "NET_CARBOHYDRATES";
    public final static String WEIGHT_WATCHERS_POINTS = "WEIGHT_WATCHERS_POINTS";
    public final static String TOTAL_PROTEIN_QUANTITY = "TOTAL_PROTEIN_QUANTITY";
    public final static String VITAMIN_A = "VITAMIN_A";
    public final static String VITAMIN_C = "VITAMIN_C";
    public final static String CALCIUM = "CALCIUM";
    public final static String IRON = "IRON";
    public final static String VITAMIN_D = "VITAMIN_D";
    public final static String THIAMINE = "THIAMINE";
    public final static String RIBFLAVIN = "RIBFLAVIN";
    public final static String NIACIN = "NIACIN";
    public final static String VITAMIN_B6 = "VITAMIN_B6";
    public final static String VITAMIN_B12 = "VITAMIN_B12";
    public final static String PHOSPHOROUS = "PHOSPHOROUS";
    public final static String MAGNESIUM = "MAGNESIUM";
    public final static String ZINC = "ZINC";
    public final static String COPPER = "COPPER";
    public final static String BIOTIN = "BIOTIN";
    public final static String FOLIC_ACID = "FOLIC_ACID";
    public final static String IODINE = "IODINE";
    public final static String PANTOTHENIC_ACID = "PANTOTHENIC_ACID";
    public final static String VITAMIN_E = "VITAMIN_E";
    public final static String VITAMIN_K = "VITAMIN_K";
    public final static String PETS_CRUDE_PROTEIN = "PETS_CRUDE_PROTEIN";
    public final static String PETS_CRUDE_FAT = "PETS_CRUDE_FAT";
    public final static String PETS_CRUDE_FIBER = "PETS_CRUDE_FIBER";
    public final static String PETS_CRUDE_MOISTURE = "PETS_CRUDE_MOISTURE";
    public final static String PETS_ASH = "PETS_ASH";
    public final static String PETS_TAURINE = "PETS_TAURINE";
    public final static String PETS_CALCIUM = "PETS_CALCIUM";
    public final static String OMEGA_3_FAT = "OMEGA_3_FAT";
    public final static String SELENIUM = "SELENIUM";
    public final static String MANGANESE = "MANGANESE";
    public final static String CHROMIUM = "CHROMIUM";
    public final static String MOLYBDENUM = "MOLYBDENUM";
    public final static String CHLORIDE = "CHLORIDE";
    public final static String IGNORE = "IGNORE";
    
    public static TreeSet nutritionTypes = new TreeSet(new TypeComparator());
    
    static{
        nutritionTypes.add(new Type(IGNORE, "Ignore", "", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(SOURCE, "Information Source", "", true, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(SERVING_SIZE, "Serving Size", "", true, nutritionTypes.size(), true, true));
        nutritionTypes.add(new Type(SERVING_WEIGHT, "Serving Weight", "g", true, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(NUMBER_OF_SERVINGS, "Servings Per Container",  "", true, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(TOTAL_CALORIES, "Calories", "", true, nutritionTypes.size(), true, false));
        nutritionTypes.add(new Type(TOTAL_CALORIES_FROM_FAT, "Calories from Fat", "", true, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_FAT_QUANTITY, "Total Fat quantity", "g", true, nutritionTypes.size(), true, false));
        nutritionTypes.add(new Type(DAILY_FAT_VALUE, "Total Fat value", "%", true, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_SATURATED_FAT_QUANTITY, "Saturated Fat quantity", "g", true, nutritionTypes.size(), true, false));
        nutritionTypes.add(new Type(DAILY_SATURATED_FAT_VALUE, "Saturated Fat value", "%", true, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TRANS_FAT,"Trans Fat quantity","g",false,nutritionTypes.size(),false,false));
        nutritionTypes.add(new Type(TOTAL_STEARIC_ACID_QUANTITY, "Stearic Acid quantity", "g", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_POLYUNSATURATED_FAT_QUANTITY, "Polyunsaturated Fat quantity", "g", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_MONOSATURATED_FAT_QUANTITY, "Monounsaturated Fat quantity", "g", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_CHOLESTEROL_QUANTITY, "Cholesterol quantity", "mg", true, nutritionTypes.size(), true, false));
        nutritionTypes.add(new Type(DAILY_CHOLESTROL_VALUE, "Cholesterol value", "%", true, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_SODIUM_QUANTITY, "Sodium quantity", "mg", true, nutritionTypes.size(), true, false));
        nutritionTypes.add(new Type(DAILY_SODIUM_VALUE, "Sodium value", "%", true, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_POTASSIUM_QUANTITY, "Potassium quantity", "mg", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(DAILY_POTASSIUM_VALUE, "Potassium value", "%", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_CARBOHYDRATE_QUANTITY, "Total Carbohydrate quantity", "g", true, nutritionTypes.size(), true, false));
        nutritionTypes.add(new Type(DAILY_CARBOHYDRATE_VALUE, "Total Carbohydrate value", "%", true, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_DIETARY_FIBER_QUANTITY, "Dietary Fiber quantity", "g", true, nutritionTypes.size(), true, true));
        nutritionTypes.add(new Type(DAILY_DIETARY_FIBER_VALUE, "Dietary Fiber value", "%", true, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(TOTAL_INSOLUBLE_FIBER_QUANTITY, "Insoluble Fiber quantity", "g", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(TOTAL_SOLUBLE_FIBER_QUANTITY, "Soluble Fiber quantity", "g", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(TOTAL_SUGAR_QUANTITY, "Sugars quantity", "g", true, nutritionTypes.size(), true, false));
        nutritionTypes.add(new Type(TOTAL_SUGAR_ALCOHOL_QUANTITY, "Sugar Alcohol quantity", "g", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(TOTAL_OTHER_CARBOHYDRATE_QUANTITY, "Other Carbohydrates quantity", "g", false, nutritionTypes.size(), false, false));
		nutritionTypes.add(new Type(NET_CARBOHYDRATES, "Net Carbohydrates", "g", false, nutritionTypes.size(), false, false));
		nutritionTypes.add(new Type(WEIGHT_WATCHERS_POINTS, "Weight Watchers Points", "pts", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(TOTAL_PROTEIN_QUANTITY, "Protein quantity", "g", true, nutritionTypes.size(), true, true));
        nutritionTypes.add(new Type(VITAMIN_A, "Vitamin A", "%", true, nutritionTypes.size(), true, true));
        nutritionTypes.add(new Type(VITAMIN_C, "Vitamin C", "%", true, nutritionTypes.size(), true, true));
        nutritionTypes.add(new Type(CALCIUM, "Calcium", "%", true, nutritionTypes.size(), true, true));
        nutritionTypes.add(new Type(IRON, "Iron", "%", true, nutritionTypes.size(), true, true));
        nutritionTypes.add(new Type(VITAMIN_D, "Vitamin D", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(VITAMIN_E, "Vitamin E", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(VITAMIN_K, "Vitamin K", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(THIAMINE, "Thiamin", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(RIBFLAVIN, "Riboflavin", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(NIACIN, "Niacin", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(VITAMIN_B6, "Vitamin B6", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(FOLIC_ACID, "Folate", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(VITAMIN_B12, "Vitamin B12", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(BIOTIN, "Biotin", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(PANTOTHENIC_ACID, "Pantothenic Acid", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(PHOSPHOROUS, "Phosphorous", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(IODINE, "Iodine", "%", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(MAGNESIUM, "Magnesium", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(ZINC, "Zinc", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(SELENIUM, "Selenium", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(COPPER, "Copper", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(MANGANESE, "Manganese", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(CHROMIUM, "Chromium", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(MOLYBDENUM, "Molybdenum", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(CHLORIDE, "Chloride", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(PETS_CRUDE_PROTEIN, "Pets Crude Protein", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(PETS_CRUDE_FAT, "Pets Crude Fat", "%", false, nutritionTypes.size(), false, false));
        nutritionTypes.add(new Type(PETS_CRUDE_FIBER, "Pets Crude Fiber", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(PETS_CRUDE_MOISTURE, "Pets Crude Moistener" , "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(PETS_ASH, "Pets Ash", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(PETS_TAURINE, "Pets Taurine", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(PETS_CALCIUM, "Pets Calcium", "%", false, nutritionTypes.size(), false, true));
        nutritionTypes.add(new Type(OMEGA_3_FAT, "Omega-3 Fat", "g", false, nutritionTypes.size(), false, true));
        
    }
    
    public static Type getType(String name){
        Type type = null;
        for(Iterator i = nutritionTypes.iterator(); i.hasNext();){
            type = (Type)i.next();
            if(name.equalsIgnoreCase(type.getName())){
                break;
            }
        }
        return type;
    }
    
    public static Iterator getTypesIterator(){
        List ret = new ArrayList();
        for(Iterator i = nutritionTypes.iterator(); i.hasNext();){
            ret.add(((Type)i.next()).getName());
        }
        return ret.iterator();
    }
    
    public static Iterator getStarterSet(){
        List ret = new ArrayList();
        for(Iterator i = nutritionTypes.iterator(); i.hasNext();){
            Type type = (Type)i.next();
            if(type.getStartingSet()){
                ret.add(type.getName());
            }
        }
        return ret.iterator();
    }
    
    public static List getStarterSetList(){
        List ret = new ArrayList();
        for(Iterator i = nutritionTypes.iterator(); i.hasNext();){
            Type type = (Type)i.next();
            if(type.getStartingSet()){
                ret.add(type.getName());
            }
        }
        return ret;
    }
    
	public static List getCommonList(){
		List ret = new ArrayList();
		for(Iterator i = nutritionTypes.iterator(); i.hasNext();){
			Type type = (Type)i.next();
			if(type.isCommon()){
				ret.add(type);
			}
		}
		return ret;
	}
    
    
    public static class Type {
        protected String name;
        protected String displayName;
        protected String uom;
        protected int priority;
        protected boolean startingSet;
        protected boolean isCommon;
        protected boolean isGood;
        
        public Type(String name, String displayName, String uom, boolean startingSet, int priority, boolean isCommon, boolean isGood){
            this.name = name;
            this.displayName = displayName;
            this.uom = uom;
            this.startingSet = startingSet;
            this.priority = priority;
            this.isCommon = isCommon;
            this.isGood = isGood;
        }
        
        public String getName(){
            return name;
        }
        public String getDisplayName(){
            return displayName;
        }
        public String getUom(){
            return uom;
        }
        public boolean getStartingSet(){
            return startingSet;
        }
        public int getPriority(){
            return priority;
        }
        public boolean isCommon(){
        	return isCommon;
        }
        public boolean isGood(){
        	return isGood;
        }
    }
    
    static class TypeComparator implements Comparator {
        public int compare(Object o1, Object o2){
            Type type1 = (Type)o1;
            Type type2 = (Type)o2;
            if(type1.getPriority() < type2.getPriority()){
                return -1;
            }else if(type1.getPriority() > type2.getPriority()){
                return 1;
            }else{
                return 0;
            }
        }
        
        public boolean equals(java.lang.Object obj) {
            return this.equals(obj);
        }
    }
    
}
