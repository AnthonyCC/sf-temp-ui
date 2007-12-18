/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.usps.citystate;

import java.util.*;

import com.freshdirect.dataloader.*;

/**
 *
 * @author  mrose
 * @version
 */
public class DetailParser extends FieldDelimitedFileParser implements SynchronousParser {
	private Set validStates;
    
    public final static String ZIP_CODE                 = "ZIP_CODE";
    public final static String CITY_STATE_KEY           = "CITY_STATE_KEY";
    public final static String ZIP_CLASS_CODE           = "ZIP_CLASS_CODE";
    public final static String CITY_STATE_NAME          = "CITY_STATE_NAME";
    public final static String CITY_STATE_NAME_ABBREV   = "CITY_STATE_NAME_ABBREV";
    public final static String CSN_FACILITY_CODE        = "CSN_FACILITY_CODE";
    public final static String CSN_MAILING_NAME         = "CSN_MAILING_NAME";
    public final static String PREF_LAST_LINE_KEY       = "PREF_LAST_LINE_KEY";
    public final static String PREF_LAST_LINE_NAME      = "PREF_LAST_LINE_NAME";
    public final static String CITY_DELV_IND            = "CITY_DELV_IND";
    public final static String CARRIER_ROUTE_IND        = "CARRIER_ROUTE_IND";
    public final static String UNIQ_ZIP_NAME            = "UNIQ_ZIP_NAME";
    public final static String FINANCE_NUM              = "FINANCE_NUM";
    public final static String STATE_ABBREV             = "STATE_ABBREV";
    public final static String COUNTY_NUM               = "COUNTY_NUM";
    public final static String COUNTY_NAME              = "COUNTY_NAME";
    
    /** Creates new ReconciliationParser */
    public DetailParser() {
        super();
        
        validStates = new HashSet();
        validStates.add("NY");
        validStates.add("NJ");
        validStates.add("CT");
        validStates.add("AE");
        
        fields.add(new Field(ZIP_CODE,                  5, true));
        fields.add(new Field(CITY_STATE_KEY,            6, true));
        fields.add(new Field(ZIP_CLASS_CODE,            1, false));
        fields.add(new Field(CITY_STATE_NAME,          28, true));
        fields.add(new Field(CITY_STATE_NAME_ABBREV,   13, true));
        fields.add(new Field(CSN_FACILITY_CODE,         1, true));
        fields.add(new Field(CSN_MAILING_NAME,          1, true));
        fields.add(new Field(PREF_LAST_LINE_KEY,        6, false));
        fields.add(new Field(PREF_LAST_LINE_NAME,      28, true));
        fields.add(new Field(CITY_DELV_IND,             1, true));
        fields.add(new Field(CARRIER_ROUTE_IND,         1, true));
        fields.add(new Field(UNIQ_ZIP_NAME,             1, false));
        fields.add(new Field(FINANCE_NUM,               6, true));
        fields.add(new Field(STATE_ABBREV,              2, true));
        fields.add(new Field(COUNTY_NUM,                3, false));
        fields.add(new Field(COUNTY_NAME,              -1, true));

        
    }
    
    CityStateRecord record;
    
    /**
     * a template method that must be defined by implementors
     * subclasses will know how to assemble model objects
     * from a a hash of tokens
     * @param tokens a HashMap containing parsed tokens from a single line
     * of a text file, keyed by their field names
     * @throws BadDataException an problems while trying to assemble objects from the
     * supplied tokens
     */
    protected void makeObjects(HashMap tokens) throws BadDataException {
        record = new CityStateRecord();

        try {
            String type = getString(tokens, CSN_FACILITY_CODE);
            if ("N".equals(type)) return;
            
            String stateAbbrev = getString(tokens, STATE_ABBREV);
            if(!(validStates.contains(stateAbbrev))) return;
            
            record.setZipCode(getString(tokens, ZIP_CODE));
            record.setCity(getString(tokens, CITY_STATE_NAME));
            record.setState(stateAbbrev);
            record.setCityStateKey(getString(tokens, CITY_STATE_KEY));
            record.setCountyName(getString(tokens, COUNTY_NAME));
        } catch (BadDataException bde) {
            bde.printStackTrace();
            throw bde;
        }
        
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