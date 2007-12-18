/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.payment.reconciliation.summary;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Type-safe enumeration for reconciliation record types.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumSummaryRecordType implements java.io.Serializable {
    
    public final static EnumSummaryRecordType FILE_HEADER          = new EnumSummaryRecordType("0", "File Header");
    public final static EnumSummaryRecordType SUMMARY_HEADER       = new EnumSummaryRecordType("1", "Summary Header");
    public final static EnumSummaryRecordType VISA_MC_SUMMARY      = new EnumSummaryRecordType("2", "Visa-MasterCard Summary");
    public final static EnumSummaryRecordType VISA_MC_UNBUNDLED    = new EnumSummaryRecordType("C", "Visa-MasterCard Unbundled");
    public final static EnumSummaryRecordType AMEX_JCB_SUMMARY     = new EnumSummaryRecordType("3", "Amex-JCB Summary");
    public final static EnumSummaryRecordType AMEX_JCB_UNBUNDLED   = new EnumSummaryRecordType("D", "Amex-JCB Unbundled");
    public final static EnumSummaryRecordType DC_CB_SUMMARY        = new EnumSummaryRecordType("4", "DinersClub-CarteBlanche Summary");
    public final static EnumSummaryRecordType DC_CB_UNBUNDLED      = new EnumSummaryRecordType("E", "DinersClub-CarteBlanche Unbundled");
    public final static EnumSummaryRecordType NOVUS_SUMMARY        = new EnumSummaryRecordType("5", "Novus Summary");
    public final static EnumSummaryRecordType NOVUS_UNBUNDLED      = new EnumSummaryRecordType("F", "Novus Unbundled");
    public final static EnumSummaryRecordType ADJUSTMENT           = new EnumSummaryRecordType("6", "Adjustment");
    public final static EnumSummaryRecordType ADJUSTMENT_ADDENDUM  = new EnumSummaryRecordType("B", "Adjustment Addendum");
    public final static EnumSummaryRecordType CHARGEBACK           = new EnumSummaryRecordType("7", "Chargeback");
    public final static EnumSummaryRecordType CHARGEBACK_ADDENDUM  = new EnumSummaryRecordType("A", "Chargeback Addendum");
    public final static EnumSummaryRecordType INVOICE              = new EnumSummaryRecordType("8", "Invoice");
    public final static EnumSummaryRecordType EFT                  = new EnumSummaryRecordType("G", "EFT");
    public final static EnumSummaryRecordType EFT_ADDENDUM_1       = new EnumSummaryRecordType("H", "EFT Return Addendum 1");
    public final static EnumSummaryRecordType EFT_ADDENDUM_2       = new EnumSummaryRecordType("I", "EFT Return Addendum 2");
    public final static EnumSummaryRecordType FILE_TRAILER         = new EnumSummaryRecordType("9", "File Trailer");
    
    private static List codeList = null;
    
    static {
        ArrayList cList = new ArrayList();
        cList.add(FILE_HEADER);
        cList.add(SUMMARY_HEADER);
        cList.add(VISA_MC_SUMMARY);
        cList.add(VISA_MC_UNBUNDLED);
        cList.add(AMEX_JCB_SUMMARY);
        cList.add(AMEX_JCB_UNBUNDLED);
        cList.add(DC_CB_SUMMARY);
        cList.add(DC_CB_UNBUNDLED);
        cList.add(NOVUS_SUMMARY);
        cList.add(NOVUS_UNBUNDLED);
        cList.add(ADJUSTMENT);
        cList.add(ADJUSTMENT_ADDENDUM);
        cList.add(CHARGEBACK);
        cList.add(CHARGEBACK_ADDENDUM);
        cList.add(INVOICE);
        cList.add(EFT);
        cList.add(EFT_ADDENDUM_1);
        cList.add(EFT_ADDENDUM_2);
        cList.add(FILE_TRAILER);
        codeList = java.util.Collections.unmodifiableList(cList);
    }
    
    private final String typeCode;
    private final String description;
    
    private EnumSummaryRecordType(String typeCode, String description) {
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
        if (o instanceof EnumSummaryRecordType) {
            return this.typeCode.equals(((EnumSummaryRecordType)o).getTypeCode());
        }
        return false;
    }
    
    public static  List getTypeCodeList() {
        return codeList;
    }
    
    public static EnumSummaryRecordType getRecordType(String code) {
        for (Iterator tIter = codeList.iterator(); tIter.hasNext();) {
            EnumSummaryRecordType rt = (EnumSummaryRecordType) tIter.next();
            if (rt.getTypeCode().equals(code)) {
                return rt;
            }
        }
        return null;
    }
    
}