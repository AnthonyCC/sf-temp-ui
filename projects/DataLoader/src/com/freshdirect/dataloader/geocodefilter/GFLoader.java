package com.freshdirect.dataloader.geocodefilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.ejb.DlvManagerDAO;
import com.freshdirect.delivery.ejb.DlvManagerSessionBean;
import com.freshdirect.framework.util.log.LoggerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;

public class GFLoader implements SynchronousParserClient {
	private GFParser parser;
	private String filename;
	private String destination;
    private StringBuffer results;
    private DlvManagerSessionBean dlvManager;
    private final BlockingQueue queue = new LinkedBlockingQueue();
    
	private int count = 0;
	private int invalids = 0;
	private int valids = 0;
	private int invalidExceptions = 0;
    private int total = 0;
    private int realTotal = 0;
    private int numThreads = 0;
    private int connReset = 500;
    private String statusMessage;
    private boolean done;
    private boolean filterRestricted;
    
    private int maxRecords=10000;
    
    private long startTime;
    
    private final static Category LOGGER = LoggerFactory.getInstance( GFLoader.class );

	public GFLoader(String filename, String destination, boolean filterRestricted) {
        parser = new GFParser();
        parser.setClient(this);
        
        this.filename = filename;
        this.destination = destination;
        this.filterRestricted = filterRestricted;
        this.numThreads = GFUtil.getNumThreads();
        this.connReset = GFUtil.getConnReset();
        
        results = new StringBuffer();
        
        try {
			total = countLines();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
        try {
        	dlvManager = new DlvManagerSessionBean();
        	
            LOGGER.info("\n----- GeocodeFilter starting -----");
            startTime = System.currentTimeMillis();
            
            
            for(int i = 0; i < numThreads; i++){
            	new Thread(new Consumer(queue)).start();
            }
            
            parser.parseFile(filename);
            
            if (parser.getExceptions().size() > 0) {
                Iterator exIter = parser.getExceptions().iterator();
                while (exIter.hasNext()) {
                    BadDataException bde = (BadDataException) exIter.next();
                    LOGGER.error(bde);
                }
            }
            
            while(true){
            	//all parsed records were tested and all valid records were output
            	if(count == realTotal){
                    statusMessage = "writing out results...";
                    writeDestinationFile(results);
            		done = true;
            		break;
            	} else {
            		Thread.sleep(100);
            	}
            }
            LOGGER.info("\n----- GeocodeFilter done -----");

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e);
            printExceptionToGUI(e);
        }
        
    }
    
	public void accept(Object o) {
		try {
			//some light memory management
			if(queue.size() >= maxRecords){
				Thread.sleep(1000);	
			} 
			queue.put(o);
			realTotal++;
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}

	private synchronized void printExceptionToGUI(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		statusMessage = e.getMessage() + "\n" + sw.toString();
	}
	
	private int countLines() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		int lines = 0;
		
		while(br.readLine() != null){
			lines++;
		}
		
		return lines;
	}
	
	private void writeDestinationFile(StringBuffer string) throws IOException{
		 PrintWriter out
		   = new PrintWriter(new BufferedWriter(new FileWriter(destination)));
		 
		 out.write(string.toString());
		 out.close();
	}

	public synchronized int getCount() {
		return count;
	}

	public int getTotal() {
		return total;
	}
	
	public int getInvalids() {
		return invalids;
	}
	
	public int getInvalidExceptions() {
		return invalidExceptions;
	}
	
	public int getValids() {
		return valids;
	}

	public String getStatusMessage() {
		return statusMessage;
	}
	
	public long getElapsedTime(){
		return System.currentTimeMillis()-startTime;
	}
	
	public boolean isDone() {
		return done;
	}
	
	private synchronized void incrementCount(){
		count++;
	}
	
	private synchronized void incrementValids(){
		valids++;
	}
	
	private synchronized void incrementInvalids(){
		invalids++;
	}
	
	private synchronized void incrementExceptions() {
		invalidExceptions++;
	}
	
	private synchronized void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}
	
	private synchronized void addResult(String resultLine){
		results.append(resultLine);
	}
	
	public int getConnReset() {
		return connReset;
	}
	
	private class Consumer implements Runnable {
		private Connection conn;
		private final BlockingQueue queue;
		private int localCount = 0;
		
		public Consumer(BlockingQueue q) { 
			queue = q; 
		}

		public void run() {
			try{				
				while(!isDone()) { 
					if (localCount % getConnReset() == 0) {
						// refresh connection every 10000 records
						if (conn != null)
							conn.close();
						conn = GFUtil.getConnection();
					}
					
					consume(queue.take()); 
				}
			} catch (Exception e){
				e.printStackTrace();
			} finally {
	        	if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) { }
	            }
	        }
			
		}
		
		private void consume(Object object){			
			try{
				GFRecord record = (GFRecord) object;
				
				AddressModel address = new AddressModel();
				address.setAddress1(record.getBldgNum() + " "
						+ (record.getDirectional() != null ? record.getDirectional() + " " : "")
						+ record.getStreetAddress() + " "
						+ (record.getPostDirectional() != null ? record.getPostDirectional() + " " : ""));
				address.setApartment(record.getAptNum());
				address.setZipCode(record.getZip());
				
				
				EnumDeliveryStatus result = dlvManager.getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.HOME));
				boolean addressGood = EnumDeliveryStatus.DELIVER.equals(result);
				if(addressGood && filterRestricted){
					addressGood = EnumRestrictedAddressReason.NONE.equals(DlvManagerDAO.isAddressRestricted(conn, address));
				}
				if (addressGood) {
					StringBuffer resultLine = new StringBuffer();
					resultLine.append(StringUtils.rightPad(record.getZip(),6))
					   .append(StringUtils.rightPad(record.getZpf(),5))
					   .append(StringUtils.rightPad(record.getSeqNum(),10))
					   .append(StringUtils.leftPad(record.getBldgNum(),10)).append(" ")
					   .append(StringUtils.rightPad(record.getDirectional(),3))
					   .append(StringUtils.rightPad(record.getStreetAddress(),41))
					   .append(StringUtils.rightPad(record.getPostDirectional(),3))
					   .append(StringUtils.rightPad(record.getAptDesignator(),5))
					   .append(StringUtils.rightPad(record.getAptNum(),16))
					   .append("\n");
					addResult(resultLine.toString());
					incrementValids();
				} else {
					incrementInvalids();
				}
				
			} catch (Exception e){
				e.printStackTrace();
				incrementExceptions();
			} finally {
				incrementCount();
				localCount++;
				setStatusMessage(getCount() + " records of " + total + " geocoded...");
			}
		}
		
	}


}
