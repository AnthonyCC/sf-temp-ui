/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.payment.reconciliation.detail;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Type-safe enumeration for reconciliation record types.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumDetailRecordType implements java.io.Serializable {
    
    public final static EnumDetailRecordType FILE_HEADER        = new EnumDetailRecordType("0", "File Header");
    public final static EnumDetailRecordType CC_DETAIL_ONE      = new EnumDetailRecordType("1", "Credit Card Detail 1");
    public final static EnumDetailRecordType CC_DETAIL_TWO      = new EnumDetailRecordType("2", "Credit Card Detail 2");
    public final static EnumDetailRecordType EFT_DETAIL_ONE     = new EnumDetailRecordType("3", "EFT Detail 1");
    public final static EnumDetailRecordType EFT_DETAIL_TWO     = new EnumDetailRecordType("4", "EFT Detail 2");
    public final static EnumDetailRecordType FILE_TRAILER       = new EnumDetailRecordType("9", "File Trailer");
    
    private static List codeList = null;
    
    static {
        ArrayList cList = new ArrayList();
        cList.add(FILE_HEADER);
        cList.add(CC_DETAIL_ONE);
        cList.add(CC_DETAIL_TWO);
        cList.add(EFT_DETAIL_ONE);
        cList.add(EFT_DETAIL_TWO);
        cList.add(FILE_TRAILER);
        codeList = java.util.Collections.unmodifiableList(cList);
    }
    
    private final String typeCode;
    private final String description;
    
    private EnumDetailRecordType(String typeCode, String description) {
        this.typeCode = typeCode;
        this.description = description;
    }
    
    public String getTypeCode() {
        return this.typeCode;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String toString() {
        return this.typeCode + " : " + this.description;
    }
    
    public boolean equals(Object o) {
        if (o instanceof EnumDetailRecordType) {
            return this.typeCode.equals(((EnumDetailRecordType)o).getTypeCode());
        }
        return false;
    }
    
    public static  List getTypeCodeList() {
        return codeList;
    }
    
    public static EnumDetailRecordType getRecordType(String code) {
        for (Iterator tIter = codeList.iterator(); tIter.hasNext();) {
            EnumDetailRecordType rt = (EnumDetailRecordType) tIter.next();
            if (rt.getTypeCode().equals(code)) {
                return rt;
            }
        }
        return null;
    }
    
}