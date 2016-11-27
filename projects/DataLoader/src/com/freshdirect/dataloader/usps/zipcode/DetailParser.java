/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.usps.zipcode;

import java.util.ArrayList;
import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.FieldDelimitedFileParser;
import com.freshdirect.dataloader.SynchronousParser;
import com.freshdirect.dataloader.SynchronousParserClient;

/**
 *
 * @author  mrose
 * @version
 */
public class DetailParser extends FieldDelimitedFileParser implements SynchronousParser {
    
    public final static String ZIP_CODE                     = "ZIP_CODE";
    public final static String UPDATE_KEY_NUM               = "UPDATE_KEY_NUM";
    public final static String ACTION_CODE                  = "ACTION_CODE";
    public final static String RECORD_TYPE_CODE             = "RECORD_TYPE_CODE";
    public final static String CARRIER_ROUTE_ID             = "CARRIER_ROUTE_ID";
    public final static String STREET_PRE_DIRECTIONAL       = "STREET_PRE_DIRECTIONAL";
    public final static String STREET_NAME                  = "STREET_NAME";
    public final static String STREET_SUFFIX                = "STREET_SUFFIX";
    public final static String STREET_POST_DIRECTIONAL      = "STREET_POST_DIRECTIONAL";
    public final static String ADDR_PRIMARY_LOW_NUM         = "ADDR_PRIMARY_LOW_NUM";
    public final static String ADDR_PRIMARY_HIGH_NUM        = "ADDR_PRIMARY_HIGH_NUM";
    public final static String ADDR_PRIMARY_ODD_EVEN_CODE   = "ADDR_PRIMARY_ODD_EVEN_CODE";
    public final static String FIRM_NAME                    = "FIRM_NAME";
    public final static String ADDR_SECONDARY_ABBREV        = "ADDR_SECONDARY";
    public final static String ADDR_SECONDARY_LOW_NUM       = "ADDR_SECONDARY_LOW_NUM";
    public final static String ADDR_SECONDARY_HIGH_NUM      = "ADDR_SECONDARY_HIGH_NUM";
    public final static String ADDR_SECONDARY_ODD_EVEN_CODE = "ADDR_SECONDARY_ODD_EVEN_CODE";
    public final static String ZIP_ADDON_LOW_SECTOR_NUM     = "ZIP_ADDON_LOW_SECTOR_NUM";
    public final static String ZIP_ADDON_LOW_SEGMENT_NUM    = "ZIP_ADDON_LOW_SEGMENT_NUM";
    public final static String ZIP_ADDON_HIGH_SECTOR_NUM    = "ZIP_ADDON_HIGH_SECTOR_NUM";
    public final static String ZIP_ADDON_HIGH_SEGMENT_NUM   = "ZIP_ADDON_HIGH_SEGMENT_NUM";
    public final static String ZIP_BASE_ALT_CODE            = "ZIP_BASE_ALT_CODE";
    public final static String LACS_STATUS                  = "LACS_STATUS";
    public final static String GOVT_BLDG                    = "GOVT_BLDG";
	public final static String FINANCE_NO    				= "FINANCE_NO";
	public final static String STATE_ABBREV                 = "STATE_ABBREV";
	public final static String COUNTY_NO                    = "COUNTY_NO";
	public final static String CONGRESSIONAL_DIST_NO        = "CONGRESSIONAL_DIST_NO";
	public final static String MUNCIPALITY_CTYST_KEY        = "MUNCIPALITY_CTYST_KEY";
	public final static String URBANIZATION_CTYST_KEY       = "URBANIZATION_CTYST_KEY";
	public final static String PREFD_LAST_LINE_CTYST_KEY    = "PREFD_LAST_LINE_CTYST_KEY";
	public final static String FILLER                       = "FILLER";
    
    
    /** Creates new ReconciliationParser */
    public DetailParser() {
        super();
        
        fields.add(new Field(ZIP_CODE,                        5, true));
        fields.add(new Field(UPDATE_KEY_NUM,                 10, true));
        fields.add(new Field(ACTION_CODE,                     1, true));
        fields.add(new Field(RECORD_TYPE_CODE,                1, true));
        fields.add(new Field(CARRIER_ROUTE_ID,                4, true));
        fields.add(new Field(STREET_PRE_DIRECTIONAL,          2, false));
        fields.add(new Field(STREET_NAME,                    28, true));
        fields.add(new Field(STREET_SUFFIX,                   4, false));
        fields.add(new Field(STREET_POST_DIRECTIONAL,         2, false));
        fields.add(new Field(ADDR_PRIMARY_LOW_NUM,           10, true));
        fields.add(new Field(ADDR_PRIMARY_HIGH_NUM,          10, true));
        fields.add(new Field(ADDR_PRIMARY_ODD_EVEN_CODE,      1, true));
        fields.add(new Field(FIRM_NAME,                      40, false));
        fields.add(new Field(ADDR_SECONDARY_ABBREV,           4, false));
        fields.add(new Field(ADDR_SECONDARY_LOW_NUM,          8, false));
        fields.add(new Field(ADDR_SECONDARY_HIGH_NUM,         8, false));
        fields.add(new Field(ADDR_SECONDARY_ODD_EVEN_CODE,    1, false));
        fields.add(new Field(ZIP_ADDON_LOW_SECTOR_NUM,        2, true));
        fields.add(new Field(ZIP_ADDON_LOW_SEGMENT_NUM,       2, true));
        fields.add(new Field(ZIP_ADDON_HIGH_SECTOR_NUM,       2, true));
        fields.add(new Field(ZIP_ADDON_HIGH_SEGMENT_NUM,      2, true));
        fields.add(new Field(ZIP_BASE_ALT_CODE,               1, true));
        fields.add(new Field(LACS_STATUS,                     1, false));
        fields.add(new Field(GOVT_BLDG,                       1, false));
        fields.add(new Field(FINANCE_NO,				      6, false));
		fields.add(new Field(STATE_ABBREV,				      2, false));
		fields.add(new Field(COUNTY_NO,				      	  3, false));
		fields.add(new Field(CONGRESSIONAL_DIST_NO,			  2, false));
		fields.add(new Field(MUNCIPALITY_CTYST_KEY,		      6, false));
		fields.add(new Field(URBANIZATION_CTYST_KEY,		  6, false));
		fields.add(new Field(PREFD_LAST_LINE_CTYST_KEY,		  6, true));
        fields.add(new Field(FILLER,                         -1, true));
        
        records = new ArrayList();
        
    }
    
    ZipPlus4Record record;
    ArrayList records;
    
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
        record = new ZipPlus4Record();
        String recordType = getString(tokens, RECORD_TYPE_CODE);
        //
        // ignore PO Boxes and Firms
        //
        //if ("P".equals(recordType) || "F".equals(recordType)) return;
        //
        // ignore govt bldgs
        //
        //String govtBldg = getString(tokens, GOVT_BLDG).trim();
        //if (!"".equals(govtBldg)) {
        //    return;
        //}
        record.setRecordType(recordType);
        record.setZipCode(getString(tokens, ZIP_CODE));
        record.setStreetPreDirectional(getString(tokens, STREET_PRE_DIRECTIONAL));
        record.setStreetName(getString(tokens, STREET_NAME));
        record.setStreetSuffix(getString(tokens, STREET_SUFFIX));
        record.setStreetPostDirectional(getString(tokens, STREET_POST_DIRECTIONAL));
        record.setAddrPrimaryLow(stripLeadingZeros(getString(tokens, ADDR_PRIMARY_LOW_NUM)));
        record.setAddrPrimaryHigh(stripLeadingZeros(getString(tokens, ADDR_PRIMARY_HIGH_NUM)));
        //
        // ignore records without any street number information
        //
        if ("-".equals(record.getAddrPrimaryLow()) && "-".equals(record.getAddrPrimaryHigh()))
            return;
        if ("E".equals(getString(tokens, ADDR_PRIMARY_ODD_EVEN_CODE))) {
            record.setAddrPrimaryEven(true);
            record.setAddrPrimaryOdd(false);
        } else {
            record.setAddrPrimaryEven(false);
            record.setAddrPrimaryOdd(true);
        }
        record.setFirmName(getString(tokens, FIRM_NAME));
        record.setAddrSecondaryAbbrev(getString(tokens, ADDR_SECONDARY_ABBREV));
        record.setAddrSecondaryLow(stripLeadingZeros(getString(tokens, ADDR_SECONDARY_LOW_NUM)));
        record.setAddrSecondaryHigh(stripLeadingZeros(getString(tokens, ADDR_SECONDARY_HIGH_NUM)));
        //
        // ignore highrise records without apt number information
        //
        if ("H".equals(record.getRecordType()) && ("".equals(record.getAddrSecondaryLow()) && "".equals(record.getAddrSecondaryLow())))
            return;
        if ("E".equals(getString(tokens, ADDR_SECONDARY_ODD_EVEN_CODE))) {
            record.setAddrSecondaryEven(true);
            record.setAddrSecondaryOdd(false);
        } else if ("O".equals(getString(tokens, ADDR_SECONDARY_ODD_EVEN_CODE))) {
            record.setAddrSecondaryEven(false);
            record.setAddrSecondaryOdd(true);
        } else {
            record.setAddrSecondaryEven(false);
            record.setAddrSecondaryOdd(false);
        }
        record.setZipAddonLow(getString(tokens, ZIP_ADDON_LOW_SECTOR_NUM) + getString(tokens, ZIP_ADDON_LOW_SEGMENT_NUM));
        record.setZipAddonHigh(getString(tokens, ZIP_ADDON_HIGH_SECTOR_NUM) + getString(tokens, ZIP_ADDON_HIGH_SEGMENT_NUM));
        record.setCityStateKey(getString(tokens, PREFD_LAST_LINE_CTYST_KEY));
        
        //System.out.println(record.getRecordType() + " " + record.getAddrPrimaryLow() + " " + record.getAddrPrimaryHigh() + " " + record.getStreetPreDirectional() + " " + record.getStreetName() + " " + record.getStreetSuffix() + " " + record.getStreetPostDirectional() + " " + record.getAddrSecondaryLow() + " " + record.getAddrSecondaryHigh() + " " + record.getZipCode() + " " + record.getZipAddonLow() + " " + record.getZipAddonHigh());
        getClient().accept(record);
    }
    
    SynchronousParserClient client;
    
    public void setClient(SynchronousParserClient client) {
        this.client = client;
    }
    
    public SynchronousParserClient getClient() {
        return this.client;
    }
    
    
}