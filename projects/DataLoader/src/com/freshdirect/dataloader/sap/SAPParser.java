/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap;

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
    
}
