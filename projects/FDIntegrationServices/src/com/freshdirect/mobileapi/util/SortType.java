/**
 * 
 */
package com.freshdirect.mobileapi.util;

public enum SortType {
    RELEVANCY("relevancy"), NAME("name"), PRICE("prce"), POPULARITY("pplr"), SALE("sale");

    String fdSortValue;
    
    
    SortType(String sortValue){
        fdSortValue = sortValue;
    }
    public static SortType valueFromString(String str) {
        SortType result = RELEVANCY;
        if (RELEVANCY.toString().equalsIgnoreCase(str)) {
            result = RELEVANCY;
        }
        if (NAME.toString().equalsIgnoreCase(str)) {
            result = NAME;
        }
        if (PRICE.toString().equalsIgnoreCase(str)) {
            result = PRICE;
        }
        if (POPULARITY.toString().equalsIgnoreCase(str)) {
            result = POPULARITY;
        }
        if (SALE.toString().equalsIgnoreCase(str)) {
            result = SALE;
        }
        return result;
    }

    public String getSortValue(){
        return fdSortValue;
    }
}