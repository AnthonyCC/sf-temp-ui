/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.detail;

import java.util.*;
import java.io.*;

import com.freshdirect.dataloader.*;

import com.freshdirect.payment.reconciliation.detail.*;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;

/**
 *
 * @author  mrose
 * @version
 */
public class DetailParser extends SettlementParser implements SynchronousParserClient{
    
    public final static String RECORD_TYPE    = "RECORD_TYPE";
    public final static String RECORD_BODY    = "RECORD_BODY";
    
    private SynchronousParserClient client = null;
    
    /** Creates new ReconciliationParser */
    public DetailParser() {
        super();
        
        fields.add(new Field(RECORD_TYPE,    1, true));
        fields.add(new Field(RECORD_BODY,   79, true));
        
    }
    
    /** 
     * parser the given InputStream object
     * @param filename the path to the file to parse
     * @throws BadDataException any unrecoverable problems found within the file.  any other exceptions are returned from this method.
     * @return a list of exceptions encountered while parsing the file
     */
    public void parseFile(InputStream fileStream) {
        BufferedReader lines = null;
        //
        // keep a list of excepttions found in this file
        //
        try {
            
            //
            // run through every line in the file
            //
            lines = new BufferedReader(new InputStreamReader(fileStream));
            String line = null;
            int lineNumber = 0;
            while (null != (line = lines.readLine())) {
                //
                // increment line count for error messages
                //
                ++lineNumber;
                //
                // ignore lines that are blank or are comments
                //
                if ("".equals(line.trim()) || line.startsWith("#"))
                    continue;
                //
                // hook for extra line by line data massaging
                // skip this line if filterLine returns false
                //
                if (!processLine(lineNumber, line))
                    continue;
                //
                // concrete subclasses know how to make objects
                // from a hash of tokens
                //
                try {
                    parseLine(line);
                } catch (BadDataException bde) {
                    //
                    // add exceptions to the exception list
                    //
                    exceptions.add(new BadDataException(bde, "Error at line " + lineNumber + ": " + bde.getMessage()));
                }
            }
        } catch (IOException ioe) {
            exceptions.add(new BadDataException(ioe));
        } finally {
            if (lines != null) {
                try {
                    lines.close();
                } catch (IOException ioe) {
                    exceptions.add(new BadDataException(ioe));
                }
            }
        }
    }
    
    protected HashMap tokenize(String line) throws BadDataException {
        //
        // this is necessary because Chase can't provide us with a proper test file
        //
        if (line.length() < 80) {
            StringBuffer padding = new StringBuffer();
            while ((line.length() + padding.length()) < 80) {
                padding.append(" ");
            }
            line += padding.toString();
        }
        return super.tokenize(line);
    }
    
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
        String recType = getString(tokens, RECORD_TYPE);
        EnumDetailRecordType srt = EnumDetailRecordType.getRecordType(recType);
        if (srt == null) {
            throw new BadDataException("No such reconciliation record type \"" + recType + "\"");
        }
        
        SettlementParser parser = null;
        if (EnumDetailRecordType.FILE_HEADER.equals(srt)) {
            parser = new FileHeaderParser();
        } else if (EnumDetailRecordType.CC_DETAIL_ONE.equals(srt)) {
            parser = new CCDetailOneParser();
        } else if (EnumDetailRecordType.CC_DETAIL_TWO.equals(srt)) {
            parser = new CCDetailTwoParser();
        } else if (EnumDetailRecordType.FILE_TRAILER.equals(srt)) {
            parser = new FileTrailerParser();
        } else {
            //throw new BadDataException("Don't know how to create a parser for " + srt.getDescription() + " records");
        }
        if (parser != null) {
        	parser.setClient(this);
            parser.parseLine(getString(tokens, RECORD_BODY));
        }
         
    }
    
 	public void accept(Object o){
 		this.client.accept(o);
 	}   
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
    
}