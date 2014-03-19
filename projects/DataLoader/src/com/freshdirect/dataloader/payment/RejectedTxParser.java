package com.freshdirect.dataloader.payment;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.BINInfoManagerSessionBean;

import au.com.bytecode.opencsv.CSVReader;


public class RejectedTxParser   {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(RejectedTxParser.class);
	
	private enum Fields {
		CardType(1),
		CardNumber(2),
		TxType(3),
		Amount(4),
		ResponseCode(5),
		RejectResponse(6),
		TxDateTime(7),
		RejectDateTime(8),
		OrderID(9),
		OrderDesc(10),
		BatchID(11),
		TxSource(12),
		ProfileID(13);
		private int id;

        private Fields(int id) {
                this.id = id;
        }
        public int id() {
        	return id;
        }
        
	}
	public RejectedTxParser() {
        
    }
	
	
	public ECheckRejectedTxDetail processLine(String[] line) throws BadDataException {
		
		ECheckRejectedTxDetail tx=new ECheckRejectedTxDetail();
		if(line.length!=Fields.values().length)
    		throw new BadDataException(" Length is "+line.length);
		
		
			tx.setOrderID(line[Fields.OrderID.id()-1]);
			tx.setProfileID(line[Fields.ProfileID.id()-1]);
			tx.setRejectResponse(line[Fields.RejectResponse.id()-1]);
		
		return tx;
			
    }
	
    
	public static void main(String[] a) {
		
		RejectedTxParser rp=new RejectedTxParser();
		rp.parseFile("RejectedDetailRpt.csv");
	}
    
	public List<ECheckRejectedTxDetail> parseFile(String filename) {
        
    	FileInputStream inputStream = null;
    	List<ECheckRejectedTxDetail> tx=new ArrayList<ECheckRejectedTxDetail>(10);
        try {
        	inputStream = new FileInputStream(filename);
        	CSVReader reader=new CSVReader(new InputStreamReader(inputStream));
        	String [] nextLine;
            int lineNumber = 0;
            
            while ((nextLine = reader.readNext()) != null) {
                
                ++lineNumber;
                if(lineNumber >9) {
	                try {
	                	tx.add(processLine(nextLine));
	                } catch (BadDataException bde) {
	                	LOGGER.error("Error parsing line: "+lineNumber,bde);
	                }
                }
            }
            
        } catch (IOException ioe) {
           
        } finally {
            if (inputStream != null) {
                try {
                	inputStream.close();
                } catch (IOException ioe) {
                    
                }
            }
        }
        return tx;
	}
        
	
}