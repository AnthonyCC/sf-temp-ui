package com.freshdirect.dataloader.autoorder.create.command;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.dataloader.autoorder.create.util.DataUtil;
import com.freshdirect.dataloader.autoorder.create.util.IConstants;
import com.freshdirect.dataloader.autoorder.create.util.ResourceUtil;

public class TesterCommand implements ITesterCommand,IAccept {
	
	private String skuPath;
	private Date refDate;
	private String customerNo;
	private String customerPrefix;
	private String type;
	
	private int count = 0;
	private int total = 0;
    private int numThreads = 0;    
    private String statusMessage;
    private boolean done;
    
    private long startTime;
    
    private int realTotal = 0;
    
    private int connReset = 500;
    
    private int maxRecords=10000;
    
    private final BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
            
    private IConsumer consumerProxy = null;
        
    
    public void init(Date refDate, String skuPath, String customerNo, String customerPrefix, String type) {
    	
    	if(refDate == null || (StringUtils.isEmpty(skuPath) && IConstants.CREATE_ORDERS.equalsIgnoreCase(type)) 
    				|| StringUtils.isEmpty(customerNo) || StringUtils.isEmpty(customerPrefix)
    			|| !StringUtils.isNumeric(customerNo)) {
    		throw new IllegalArgumentException("Invalid Input Argument");
    	}
    	this.skuPath = skuPath;
    	this.refDate = refDate;
    	this.customerNo = customerNo;
    	this.customerPrefix = customerPrefix;
    	this.type = type;
    	
    	this.numThreads = ResourceUtil.getNumThreads();
        this.connReset = ResourceUtil.getConnReset();
        try {
        	if(IConstants.CREATE_CUSTOMER.equalsIgnoreCase(type)) {
        		this.consumerProxy = new CustomerConsumer();
        	} else {
        		this.consumerProxy = new OrderConsumer();
        	}
        	consumerProxy.start(skuPath, refDate);
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
	public synchronized int getCount() {
		return count;
	}

	public int getTotal() {
		return total;
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
	
	private synchronized void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}
	
	public int getConnReset() {
		return connReset;
	}
	
	public void load() {
		Connection tmpCon = null;
		Connection tmpDevCon = null;
		try {
        	
			tmpCon = ResourceUtil.getConnection();
			tmpDevCon = ResourceUtil.getDevConnection();
			total = Integer.parseInt(customerNo);
			
            startTime = System.currentTimeMillis();
                       
            for(int i = 0; i < numThreads; i++){            	
            	new Thread(new Consumer(queue)).start();
            }
            if(type.equalsIgnoreCase(IConstants.CREATE_CUSTOMER)) { 
            	System.out.println("I'm Hererere >>>>>>>");
            	DataUtil.getCustomers(tmpCon,tmpDevCon, total, customerPrefix, this);
            } else {
            	System.out.println("I'm Hererere");
            	DataUtil.getOrders(tmpDevCon, customerPrefix, total, this);
            }
            while(true){
            	//all parsed records were tested and all valid records were output
            	
            	if(count == realTotal){
                    statusMessage = "writing out results...";                    
            		done = true;
            		consumerProxy.end();
            		break;
            	} else {
            		Thread.sleep(100);
            	}
            }
            

        } catch (Exception e) {
            e.printStackTrace();            
        } finally {
        	if (tmpCon != null) {
                try {
                	tmpCon.close();
                } catch (SQLException e) { }
            }
        	if (tmpDevCon != null) {
                try {
                	tmpDevCon.close();
                } catch (SQLException e) { }
            }
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
	
	private class Consumer implements Runnable {
		Connection conn = null;		
		private final BlockingQueue<Object> queue;
		private int localCount = 0;
		
		public Consumer(BlockingQueue<Object> q) { 
			queue = q; 
		}

		public void run() {
			try{
				while(!isDone()) {
					
					if (localCount % getConnReset() == 0) {
						// refresh connection every 10000 records
						if (conn != null)
							conn.close();						
						conn = ResourceUtil.getDevConnection();
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
				consumerProxy.consume(object, conn);				
			} catch (Exception e){
				e.printStackTrace();				
			} finally {
				incrementCount();
				localCount++;
				setStatusMessage(getCount() + " records of " + total + " processed...");
			}
		}
		
		
	}
}
