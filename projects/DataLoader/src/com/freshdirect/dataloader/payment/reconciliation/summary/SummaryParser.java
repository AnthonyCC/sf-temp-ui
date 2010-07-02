/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.summary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;
import com.freshdirect.payment.reconciliation.summary.EnumSummaryRecordType;
import com.freshdirect.payment.reconciliation.summary.ReconciliationBatch;


/**
 *
 * @author  mrose
 * @version
 */
public class SummaryParser extends SettlementParser implements SynchronousParserClient {
	
	private SynchronousParserClient client = null;
    
    public final static String RECORD_TYPE    = "RECORD_TYPE";
    public final static String RECORD_BODY    = "RECORD_BODY";
    
    private ReconciliationBatch batch = null;
    
    /** Creates new ReconciliationParser */
    public SummaryParser() {
        super();
        
        fields.add(new Field(RECORD_TYPE,    1, true));
        fields.add(new Field(RECORD_BODY,   -1, true));
        
        batch = new ReconciliationBatch();
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
    
    @Override
    protected Map<String, String> tokenize(String line) throws BadDataException {
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
    @Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
        String recType = getString(tokens, RECORD_TYPE);
        EnumSummaryRecordType rrt = EnumSummaryRecordType.getRecordType(recType);
        if (rrt == null) {
            throw new BadDataException("No such reconciliation record type \"" + recType + "\"");
        }
        
        SettlementParser parser = null;
        if (EnumSummaryRecordType.FILE_HEADER.equals(rrt)) {
            parser = new FileHeaderParser();
        } else if (EnumSummaryRecordType.SUMMARY_HEADER.equals(rrt)) {
            parser = new SummaryHeaderParser();
        } else if (EnumSummaryRecordType.VISA_MC_SUMMARY.equals(rrt)) {
            parser = new VisaMCSummaryParser();
        } else if (EnumSummaryRecordType.VISA_MC_UNBUNDLED.equals(rrt)) {
            parser = new VisaMCUnbundledParser();
        } else if (EnumSummaryRecordType.AMEX_JCB_SUMMARY.equals(rrt)) {
            parser = new AmexJCBSummaryParser();
        } else if (EnumSummaryRecordType.AMEX_JCB_UNBUNDLED.equals(rrt)) {
            parser = new AmexJCBUnbundledParser();
        } else if (EnumSummaryRecordType.DC_CB_SUMMARY.equals(rrt)) {
            parser = new DinersClubCarteBlancheSummaryParser();
        } else if (EnumSummaryRecordType.DC_CB_UNBUNDLED.equals(rrt)) {
            parser = new DinersClubCarteBlancheUnbundledParser();
        } else if (EnumSummaryRecordType.NOVUS_SUMMARY.equals(rrt)) {
            parser = new NovusSummaryParser();
        } else if (EnumSummaryRecordType.NOVUS_UNBUNDLED.equals(rrt)) {
            parser = new NovusUnbundledParser();
        } else if (EnumSummaryRecordType.ADJUSTMENT.equals(rrt)) {
            parser = new AdjustmentParser();
        } else if (EnumSummaryRecordType.ADJUSTMENT_ADDENDUM.equals(rrt)) {
            parser = new AdjustmentAddendumParser();
        } else if (EnumSummaryRecordType.CHARGEBACK.equals(rrt)) {
            parser = new ChargebackParser();
        } else if (EnumSummaryRecordType.CHARGEBACK_ADDENDUM.equals(rrt)) { 
            parser = new ChargebackAddendumParser();
        } else if (EnumSummaryRecordType.INVOICE.equals(rrt)) {
            parser = new InvoiceParser();
        } else if (EnumSummaryRecordType.FILE_TRAILER.equals(rrt)) {
            parser = new FileTrailerParser();
        } else {
            throw new BadDataException("Don't know how to create a parser for " + rrt.getDescription() + " records");
        }
        if (parser != null) {
        	parser.setClient(this);
            parser.parseLine(getString(tokens, RECORD_BODY));
         	
        }
        
        
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
    
    public void accept(Object o){
    	this.client.accept(o);
    }
    
}