package com.freshdirect.dataloader.geocodefilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.dataloader.*;

public class GFParser extends FieldDelimitedFileParser implements IParser{
    public final static String ZIP_CODE                = "ZIP_CODE";
    public final static String ZPF                     = "ZPF";
    public final static String SEQ_NUM                 = "SEQ_NUM";
    public final static String BLDG_NUM                = "BLDG_NUM";
    public final static String DIRECTIONAL             = "DIRECTIONAL";
    public final static String STREET_ADDRESS          = "STREET_ADDRESS";
    public final static String POST_DIRECTIONAL        = "POST_DIRECTIONAL";
    public final static String APT_DESIGNATOR          = "APT_DESIGNATOR";
    public final static String APT_NUMBER              = "APT_NUMBER";
    
    public GFParser() {
        super();
        
        fields.add(new Field(ZIP_CODE,                   6, false));
        fields.add(new Field(ZPF,                        5, false));
        fields.add(new Field(SEQ_NUM,                    10, false));
        fields.add(new Field(BLDG_NUM,                   11, false));
        fields.add(new Field(DIRECTIONAL,                3, false));
        fields.add(new Field(STREET_ADDRESS,             41, false));
        fields.add(new Field(POST_DIRECTIONAL,           3, false));
        fields.add(new Field(APT_DESIGNATOR,             5, false));
        fields.add(new Field(APT_NUMBER,                 5, false));
    }
    
    SynchronousParserClient client;
    GFRecord record;
    
	protected void makeObjects(HashMap tokens) throws BadDataException {
		record = new GFRecord();
		
		record.setZip(getString(tokens, ZIP_CODE));
		record.setZpf(getString(tokens,ZPF));
		record.setSeqNum(getString(tokens,SEQ_NUM));
		record.setBldgNum(getString(tokens,BLDG_NUM));
		record.setDirectional(getString(tokens,DIRECTIONAL));
		record.setStreetAddress(getString(tokens,STREET_ADDRESS));
		record.setPostDirectional(getString(tokens,POST_DIRECTIONAL));
		record.setAptDesignator(getString(tokens,APT_DESIGNATOR));
		record.setAptNum(getString(tokens,APT_NUMBER));
		
		getClient().accept(record);
	}
	
	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
	
	

}
