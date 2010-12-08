package com.freshdirect.transadmin.model.comparator;

import java.util.Comparator;

import com.freshdirect.transadmin.model.TrnAdHocRoute;

public class AlphaNumericComparator implements Comparator{
		/**
		  * The compare method that compares the alphanumeric strings
		  */
	public int compare(Object Obj1, Object Obj2) {
		TrnAdHocRoute t1 = (TrnAdHocRoute)Obj1;
		TrnAdHocRoute t2 = (TrnAdHocRoute)Obj2;
        String objStr1 = t1.getRouteNumber();
        String objStr2 = t2.getRouteNumber();
 
	    if (objStr2 == null || objStr1 == null) {
            return 0;
        }
 
	    int objStrlength1 = objStr1.length();
        int objStrlength2 = objStr2.length();
	 
        int index1 = 0;
        int index2 = 0;
	 
        while (index1 < objStrlength1 && index2 < objStrlength2) {
	            char ch1 = objStr1.charAt(index1);
		        char ch2 = objStr2.charAt(index2);

	            char[] space1 = new char[objStrlength1];
	            char[] space2 = new char[objStrlength2];
		 
	            int loc1 = 0;
		        int loc2 = 0;
	 
		            do {
		                space1[loc1++] = ch1;
		                index1++;
	 
		                if (index1 < objStrlength1) {
		                    ch1 = objStr1.charAt(index1);
		                } else {
		                	break;
		                }
		            } while (Character.isDigit(ch1) == Character.isDigit(space1[0]));
		 
		            do {
		                space2[loc2++] = ch2;
		                index2++;
	 
		                if (index2 < objStrlength2) {
		                	ch2 = objStr2.charAt(index2);
		                } else {
		                	break;
		                }
		            } while (Character.isDigit(ch2) == Character.isDigit(space2[0]));
		 
	            String str1 = new String(space1);
	            String str2 = new String(space2);
	 
	            int result;
	 
	            if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
	                Integer firstNumberToCompare = new Integer(Integer
	                        .parseInt(str1.trim()));
	                Integer secondNumberToCompare = new Integer(Integer
	                        .parseInt(str2.trim()));
	                result = firstNumberToCompare.compareTo(secondNumberToCompare);
	            } else {
	                result = str1.compareToIgnoreCase(str2);
	            }
	 
	            if (result != 0) {
	                return result;
	            }
        }
	    return objStrlength1 - objStrlength2;
    } 
}
