/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.freshdirect.dataloader.FieldDelimitedFileParser;

/** 
 * an abstract class for all the parsers in SAP packages to extend
 *
 * @version $Revision$
 * @author $Author$
 */
abstract public class SAPParser extends FieldDelimitedFileParser implements SAPConstants {
    
    
    /** creates a new SAPParser
     */    
    protected SAPParser() {
        super();
    }
    
    
    /**
     * @param source
     * 
     * Common export method for all parser
     * Creates a SAP input file for the ERPS data loader based on the given source map
     * The source map contains the fields content so the method can write them into the file in the right order and length
     */
    public void makeFile(List<Map<String, String>> source){
    	List<String> lines=new ArrayList<String>();
    	
    	for(Map<String, String> lineInfo:source){
    		
    		StringBuilder lineBuilder=new StringBuilder();		
    		
    		for(Field field:fields){
    			
    			String fieldText=lineInfo.get(field.getName())==null ? "" : lineInfo.get(field.getName());
    			
    			lineBuilder.append(padRight(fieldText,field.getLength()));
    		}
    		
    		System.out.println(lineBuilder.toString());
    	}
    }
    
    
    /**
     * @param s
     * @param n
     * @return
     * 
     * Helper method for padding a string with white spaces
     */
    public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}
    
}
