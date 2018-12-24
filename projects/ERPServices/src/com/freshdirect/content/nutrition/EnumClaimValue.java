/*
 * $Workfile:EnumClaimValue.java$
 *
 * $Date:7/18/2003 12:12:11 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.nutrition;

import java.util.*;

/**
 * Type-safe enumeration for attributes' data types.
 *
 * @version $Revision:4$
 * @author $Author:Mike Rose$
 */
public class EnumClaimValue implements NutritionValueEnum {
	
	private static final long	serialVersionUID	= -4887294112082084899L;
	
	public final static EnumClaimValue OAN_ORGANIC = new EnumClaimValue("OAN",       "Approved O&AN Store", 1);
	public final static EnumClaimValue KOSHER_FOR_PASSOVER = new EnumClaimValue("KOS_PAS", "Kosher for Passover", 1);

	public final static EnumClaimValue VEGGY = new EnumClaimValue("VEGGY", "Vegetarian", 2);

	// 4mm nutrition claims
	public final static EnumClaimValue NUTRITION_4MM_LOWCALORIE 	= new EnumClaimValue("4MM_LOWCALORIE", "Nutritionist Recommended", 2);
	public final static EnumClaimValue NUTRITION_4MM_VEGETARIAN 	= VEGGY;
	public final static EnumClaimValue NUTRITION_4MM_2VEGETABLE 	= new EnumClaimValue("4MM_2VEGETABLE", "2+ Servings of Vegetables", 2);
	public final static EnumClaimValue NUTRITION_4MM_WHOLEGRAINS 	= new EnumClaimValue("4MM_WHOLEGRAINS", "Whole Grains", 2);
	public final static EnumClaimValue NUTRITION_4MM_HIGHFIBER 		= new EnumClaimValue("4MM_HIGHFIBER", "High Fiber", 2);
	public final static EnumClaimValue NUTRITION_4MM_SODIUM 		= new EnumClaimValue("4MM_SODIUM", "Sodium-Conscious", 2);

	//[APPDEV-4411]-Add 4 new ERPSy claim options for FDX
	public final static EnumClaimValue PASTURED = new EnumClaimValue("PASTURED", "Pastured", 1);
	public final static EnumClaimValue FRUCTOSE_CORNSYR = new EnumClaimValue("FRUCTOSE_CORNSYR", "No High Fructose Corn Syrup", 1);
	public final static EnumClaimValue SULFATE_PARABEN = new EnumClaimValue("SULFATE_PARABEN", "Sulfate & Paraben Free", 1);
	public final static EnumClaimValue BIO_DYNAMIC = new EnumClaimValue("BIO_DYNAMIC", "Biodynamic", 1);

	private final String code;
    private final String name;
    private final int priority;

	private EnumClaimValue(String code, String name, int priority) {
		this.code = code;
        this.name = name;
        this.priority = priority;
	}

	public String getCode() {
		return this.code;
	}

    public String getName() {
        return this.name;
    }
	
	public String toString() {
		return this.name;
	}
	
	public int getPriority(){
		return this.priority;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumClaimValue) {
			return this.code.equals(((EnumClaimValue)o).getCode());
		}
		return false;
	}
    
    private final static List<EnumClaimValue> types = new ArrayList<EnumClaimValue>();
    static {
    	types.add(new EnumClaimValue("NONE", "None", 0));
        types.add(new EnumClaimValue("FR_GLUT", "Gluten Free", 1));
        types.add(new EnumClaimValue("FR_WHET", "Wheat Free", 1));
        types.add(new EnumClaimValue("FR_LACT", "Lactose Free", 1));
        types.add(new EnumClaimValue("FR_SODM", "Sodium Free", 1));
        types.add(new EnumClaimValue("FR_SUGR", "Sugar Free", 1));
        types.add(new EnumClaimValue("FR_FAT",  "Fat Free", 1));
        types.add(new EnumClaimValue("FR_CHOL", "Cholesterol Free", 1));
        types.add(new EnumClaimValue("FR_DAIR", "Dairy Free", 1));
        types.add(new EnumClaimValue("FR_CAFF", "Caffeine Free", 1));
        types.add(new EnumClaimValue("FR_GMO",  "GMO Free", 1));
        types.add(new EnumClaimValue("FR_HRM",  "Hormone Free", 1));
        types.add(new EnumClaimValue("FR_RBST", "RBST Free", 1));
        //Changed for APPDEV-705
        //types.add(new EnumClaimValue("FR_ANTI", "Antibiotic Free", 1));
        types.add(new EnumClaimValue("FR_ANTI", "Raised w/o Antibiotics, No Antibiotics Used Ever", 1));
        
        types.add(new EnumClaimValue("LO_SODM", "Low Sodium", 1));
        types.add(new EnumClaimValue("LO_FAT",  "Low Fat", 1));
        types.add(new EnumClaimValue("LO_SALT", "Low Salt", 1));
        types.add(new EnumClaimValue("LO_CALR", "Low Calorie", 1));
        types.add(new EnumClaimValue("LO_CARB", "Low Carbohydrate", 1));
        types.add(new EnumClaimValue("LO_CHOL", "Low Cholesterol", 1));
		// as per the JIRA APPDEV-4176 changing Low sugar to vegan
		//types.add(new EnumClaimValue("LO_SUGR", "Low Sugar", 1));
        types.add(new EnumClaimValue("VE_GAN", "Vegan", 1));
        
        types.add(new EnumClaimValue("RD_FAT",  "Reduced Fat", 1));
        types.add(new EnumClaimValue("RD_SODM", "Reduced Sodium", 1));
        types.add(new EnumClaimValue("RD_CALR", "Reduced Calorie", 1));
        
        types.add(new EnumClaimValue("NO_SALT", "No Salt Added", 1));
        types.add(new EnumClaimValue("NO_SUGR", "No Sugar Added", 1));
        types.add(new EnumClaimValue("NO_TFAD", "Trans Fat Free", 1));
        types.add(new EnumClaimValue("NO_PRES", "No Preservatives", 1));
        types.add(new EnumClaimValue("NO_MSG",  "No MSG", 1));
        types.add(new EnumClaimValue("NO_REN",  "No Animal Rennet", 1));
        types.add(new EnumClaimValue("NO_PEST", "No Pesticides", 1));
        types.add(new EnumClaimValue("NO_ARTF", "No Artificial Ingredients", 1));
        types.add(new EnumClaimValue("NO_COLR", "No Artificial Color", 1));
        types.add(new EnumClaimValue("NO_HYDR", "No Hydrogenated Oils", 1));
        
        types.add(new EnumClaimValue("AL_NATR", "All Natural", 1));
		types.add(new EnumClaimValue("AHA", "American Heart Association", 1));
		
		types.add( VEGGY );
		types.add(new EnumClaimValue("ATKINS",    "Atkins", 1));
		// as per the JIRA APPDEV-4176 changing Net carbs to  Grass-Fed
		//types.add(new EnumClaimValue("NET_CARBS", "Net Carbs", 1));
		types.add(new EnumClaimValue("GRASS_FED", "Grass-Fed", 1));
		types.add(new EnumClaimValue("NOS",       "Naturally Occurring Sulfites", 1));
		types.add(new EnumClaimValue("SOY",       "Soy Free", 1));
		types.add(new EnumClaimValue("PA",        "Paleo", 1));
		types.add(OAN_ORGANIC);
		types.add(KOSHER_FOR_PASSOVER);
		
		types.add(new EnumClaimValue("KOS_NOPAS", "Not certified for Passover.", 1));
		//As per the JIIRA APPDEV-4176 changing Produced in our bakery to Not tested on animal
		//types.add(new EnumClaimValue("PEANUTS", "Produced in our bakery, where tree nuts and peanuts are used.", 1));
		types.add(new EnumClaimValue("NOT_TESTED_ANIMAL", "Not Tested On animals", 1));
		//As per the JIIRA APPDEV-4176 changing FreshDirect's kitchen eggs to Fair Trade
		//types.add(new EnumClaimValue("LEGAL","Please note: FreshDirect's kitchen uses eggs, fish, milk, shellfish, soy, peanuts, tree nuts and wheat ingredients.",1));
		types.add(new EnumClaimValue("FAIR_TRADE","Fair Trade",1));

		// 4mm nutrition claims
		types.add( NUTRITION_4MM_LOWCALORIE );
		//types.add( NUTRITION_4MM_VEGETARIAN ); veggy is already in the list.
		types.add( NUTRITION_4MM_2VEGETABLE );
		types.add( NUTRITION_4MM_WHOLEGRAINS );
		types.add( NUTRITION_4MM_HIGHFIBER );
		types.add( NUTRITION_4MM_SODIUM );
		
		//[APPDEV-4411]-Add 4 new ERPSy claim options for FDX
		types.add(PASTURED);
		types.add(FRUCTOSE_CORNSYR);
		types.add(SULFATE_PARABEN);
		types.add(BIO_DYNAMIC);
    }

    public static List<EnumClaimValue> getValues() {
        Collections.sort(types, ClaimValueComparator);
        return Collections.unmodifiableList(types);
    }

    public static EnumClaimValue getValueForCode(String code) {
        for ( EnumClaimValue value : types ) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static Comparator<EnumClaimValue> ClaimValueComparator = new Comparator<EnumClaimValue>(){
		public int compare(EnumClaimValue claim1, EnumClaimValue claim2) {
		   	int diff = claim1.getPriority()- claim2.getPriority();
		   	if (diff==0) {
		   		diff = claim1.getName().compareTo(claim2.getName());
		   	}
		   	return diff;
		}
	};
}
