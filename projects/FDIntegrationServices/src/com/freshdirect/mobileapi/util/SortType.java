/**
 * 
 */
package com.freshdirect.mobileapi.util;

import com.freshdirect.storeapi.content.SortOptionModel;

public enum SortType {
    RELEVANCY("relevancy"),
    NAME("name"), 
    PRICE("prce"), 
    POPULARITY("pplr"), 
    SALE("sale"),
	RECENCY("recency"),
	OURFAVES("ourFaves"),
	DEPARTMENT("dept"),
	FREQUENCY("freq" ),
	EXPERT_RATING("expr"),
    START_DATE("strd"),
	EXPIRATION_DATE("expr"),
	PERC_DISCOUNT("poff"),
	DOLLAR_DISCOUNT("doff"),
    SUSTAINABILITY_RATING("sust"),
    ECOUPON("ecoupon"),
    YOURFAVES("yourFaves"),
    CUSTFAVES("custFaves"),
    CUSTOMER_RATING("cxpr");
    String fdSortValue;
    
    
    SortType(String sortValue){
        fdSortValue = sortValue;
    }
    public static SortType valueFromString(String str) {
        SortType result = RELEVANCY;
        if (RELEVANCY.toString().equalsIgnoreCase(str)) {
            result = RELEVANCY;
        } else if (NAME.toString().equalsIgnoreCase(str)) {
            result = NAME;
        } else if (PRICE.toString().equalsIgnoreCase(str)) {
            result = PRICE;
        } else if (SALE.toString().equalsIgnoreCase(str)) {
            result = SALE;
        } else if (POPULARITY.toString().equalsIgnoreCase(str)) {
            result = POPULARITY;
        } else if (RECENCY.toString().equalsIgnoreCase(str)) {
            result = RECENCY;
        } else if (OURFAVES.toString().equalsIgnoreCase(str)) {
            result = OURFAVES;
        } else if (DEPARTMENT.toString().equalsIgnoreCase(str)) {
            result = DEPARTMENT;
        } else if (FREQUENCY.toString().equalsIgnoreCase(str)) {
            result = FREQUENCY;
        } else if (EXPERT_RATING.toString().equalsIgnoreCase(str)) {
            result = EXPERT_RATING;
        } else if (START_DATE.toString().equalsIgnoreCase(str)) {
            result = START_DATE;
        } else if (EXPIRATION_DATE.toString().equalsIgnoreCase(str)) {
            result = EXPIRATION_DATE;
        } else if (PERC_DISCOUNT.toString().equalsIgnoreCase(str)) {
            result = PERC_DISCOUNT;
        } else if (DOLLAR_DISCOUNT.toString().equalsIgnoreCase(str)) {
            result = DOLLAR_DISCOUNT;
        } else if (SUSTAINABILITY_RATING.toString().equalsIgnoreCase(str)) {
            result = SUSTAINABILITY_RATING;
        }
        return result;
    }
    
    public static SortType sortValueStringToSortType(String minSortType){
    	SortType result = RELEVANCY;
    	
    	if (RELEVANCY.getSortValue().equalsIgnoreCase(minSortType)) {
            result = RELEVANCY;
        } else if (NAME.getSortValue().equalsIgnoreCase(minSortType)) {
            result = NAME;
        } else if (PRICE.getSortValue().equalsIgnoreCase(minSortType)) {
            result = PRICE;
        } else if (SALE.getSortValue().equalsIgnoreCase(minSortType)) {
            result = SALE;
        } else if (POPULARITY.getSortValue().equalsIgnoreCase(minSortType)) {
            result = POPULARITY;
        } else if (RECENCY.getSortValue().equalsIgnoreCase(minSortType)) {
            result = RECENCY;
        } else if (OURFAVES.getSortValue().equalsIgnoreCase(minSortType)) {
            result = OURFAVES;
        } else if (DEPARTMENT.getSortValue().equalsIgnoreCase(minSortType)) {
            result = DEPARTMENT;
        } else if (FREQUENCY.getSortValue().equalsIgnoreCase(minSortType)) {
            result = FREQUENCY;
        } else if (EXPERT_RATING.getSortValue().equalsIgnoreCase(minSortType)) {
            result = EXPERT_RATING;
        } else if (START_DATE.getSortValue().equalsIgnoreCase(minSortType)) {
            result = START_DATE;
        } else if (EXPIRATION_DATE.getSortValue().equalsIgnoreCase(minSortType)) {
            result = EXPIRATION_DATE;
        } else if (PERC_DISCOUNT.getSortValue().equalsIgnoreCase(minSortType)) {
            result = PERC_DISCOUNT;
        } else if (DOLLAR_DISCOUNT.getSortValue().equalsIgnoreCase(minSortType)) {
            result = DOLLAR_DISCOUNT;
        } else if (SUSTAINABILITY_RATING.getSortValue().equalsIgnoreCase(minSortType)) {
            result = SUSTAINABILITY_RATING;
        }
    	return result;
    }

    public String getSortValue(){
        return fdSortValue;
    }
        
    public static SortType wrap(SortOptionModel model)
    {
    	if(model == null || model.getContentName() == null)
			return null;
		
    	SortType o = null;
    	String toCheck = model.getContentName().toLowerCase();

    	if(toCheck.contains("expertrating")){
    		o = EXPERT_RATING;
    	} else if(toCheck.contains("popularity")){
    		o = POPULARITY;
    	} else if(toCheck.contains("sale")){
    		o = SALE;
    	} else if(toCheck.contains("price")){
    		o = PRICE;
    	} else if(toCheck.contains("sustainabilityrating")){
    		o = SUSTAINABILITY_RATING;
    	} else if(toCheck.contains("name")){
    		o = NAME;
    	} else if(toCheck.contains("relevancy")){
    		o = RELEVANCY;
    	} else if(toCheck.contains("ourfavorites")){
    		o = OURFAVES;
    	} else if(toCheck.contains("department")){
    		o = DEPARTMENT;
    	} else if(toCheck.contains("recency")){
    		o = RECENCY;
    	} else if(toCheck.contains("ecoupon")){
    		o = ECOUPON;
    	} else if(toCheck.contains("yourfavorites")){
    		o = YOURFAVES;
    	} else if(toCheck.contains("customerfavoritesfirst")){
    		o = CUSTFAVES;
    	} else if(toCheck.contains("customerrating")){
    		o = CUSTOMER_RATING;
    	} 
    	
    	return o;
    }
}