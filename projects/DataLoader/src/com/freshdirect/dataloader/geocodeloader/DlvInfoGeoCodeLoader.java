package com.freshdirect.dataloader.geocodeloader;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.payment.SaleCronRunner;
import com.freshdirect.dataloader.payment.ejb.SaleCronHome;
import com.freshdirect.dataloader.payment.ejb.SaleCronSB;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvInfoGeoCodeLoader {
	
	// this class will get all the dlv info null geocode records
	// then start generating and updating the geocode for n records
	// this will span threads for generating the geocode and updating the records
	
	// file name where names of the address cannot be geocoded
	// time for which this job will be running
	// numner of threads this job will be spanning
	// number of records at time application will be reading and updating
	private static final Category LOGGER = LoggerFactory.getInstance(DlvInfoGeoCodeLoader.class);
	
	
	private static final int DEFAULT_NUMBER_OF_THREADS=6;	
	private static final int DEFAULT_JOB_EXECUTION_TIME=3; // 3 hours
	private static final int DEFAULT_ONE_TIME_RCD_SIZE=5000;
	private static final int threadCount=0;
	private static int rcdCount=0;
		
	
	public static void main(String args[]) {
							
		int numberOfThreads=DEFAULT_NUMBER_OF_THREADS;
		int jobExecutionTime=DEFAULT_JOB_EXECUTION_TIME;
		int oneTimeCrdSize=DEFAULT_ONE_TIME_RCD_SIZE;
		
		if (args.length >= 1) {
			for (int i = 0; i < args.length; i++) {
				try { 
					if (args[i].startsWith("thread_count=")) {
						numberOfThreads = Integer.parseInt(args[i].substring("thread_count=".length()).trim());
					} else if (args[i].startsWith("execution_time=")) {				
						jobExecutionTime = new Integer(args[i].substring("execution_time=".length())).intValue(); 
					} else if (args[i].startsWith("oneTimeRcdSize=")) {
						oneTimeCrdSize = new Integer(args[i].substring("oneTimeRcdSize=".length())).intValue();
					}
				} catch (Exception e) {
					System.err.println("Usage: java com.freshdirect.dataloader.geocodeloader.DlvInfoGeoCodeLoader [thread_count={int value}] [output_filename={String value}] [execution_time={int value in hrs}] [oneTimeRcdSize={int value}]");
					System.exit(-1);
				}
			}
		}
		
		Date currentTime=new Date();
		Calendar cal = DateUtil.toCalendar(currentTime);
		cal.add(Calendar.HOUR, jobExecutionTime);
		LOGGER.debug("JOB START TIME :"+currentTime);
		LOGGER.debug("JOB END TIME :"+cal.getTime());
		while(true){
						
			runGeoCodeThreads(numberOfThreads,oneTimeCrdSize);
			
			// wait for 10 minutes till thread execution amost completes  	
			
			try{
				Thread.sleep(1000*60*10);
			}catch(Exception e){
				e.printStackTrace();
			}	
			
			Calendar currCal = DateUtil.toCalendar(new Date());
			if(currCal.getTime().after(cal.getTime())){
				// time to exit
				LOGGER.debug("EXITING THE JOB TIME:"+currCal.getTime());
				System.exit(-1);
			}			
		}	
	}
		
	public static void runGeoCodeThreads(int numberOfThreads,int oneTimeRcdSize){
			 
		for(int i=0;i<numberOfThreads;i++){			
			GeoCodeLoaderThread t=new GeoCodeLoaderThread(rcdCount,rcdCount*oneTimeRcdSize,(rcdCount*oneTimeRcdSize+oneTimeRcdSize));
			Thread thread=new Thread(t);
			thread.start();
			rcdCount=rcdCount+1;
			// wait for 4 minute since this involves heavy backend operation
			try{
				Thread.sleep(1000*60*1);
			}catch(Exception e){
				e.printStackTrace();
			}	
		}		
	}	
}
