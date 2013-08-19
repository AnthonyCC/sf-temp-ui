package com.freshdirect.dataloader.payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProfileCreatorResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6910437788647577781L;

	public static enum Status implements Serializable { SUCCESS, FAILURE,SKIPPED };
		
	
		
		public static Result createFailure( String customerId,Map<String,String> failedAccounts, Map<String,String> profiledAccounts, Map<String,String> ignoredAccounts) {
			return new Result( Status.FAILURE,customerId,failedAccounts,profiledAccounts,ignoredAccounts);
		}


		public static Result createSuccess( String customerId,Map<String,String> profiledAccounts,Map<String,String> ignoredAccounts ) {
			return new Result( Status.SUCCESS, customerId,null,profiledAccounts,ignoredAccounts );
		}
		
		
		public static class Result implements Serializable {
			
			private Status	status;
			private String customerId;
			private Map<String,String> failedAccounts;
			private Map<String,String> successAccounts;
			private Map<String,String> ignoredAccounts;
			
			

			
			/**
			 *  private constructor, only for internal use
			 *
			 *  Use the factory methods to create instances
			 * 
			 * @param status
			 */
			private Result( Status status, String customerId, Map<String,String> failedAccounts, Map<String,String> successAccounts,Map<String,String> ignoredAccounts ) {
				this.status = status;
				this.customerId=customerId;
				this.failedAccounts=failedAccounts;
				this.successAccounts=successAccounts;
				this.ignoredAccounts=ignoredAccounts;
			}

			public Status getStatus() {
				return status;
			}		
			
			
			
			
			public String getCustomerId() {
				return customerId;
			}		
			
			
			
			public Map<String,String> getFailedAccounts() {
				return Collections.unmodifiableMap(failedAccounts) ;
			}		
			
			public Map<String,String> getSuccessAccounts() {
				return Collections.unmodifiableMap(successAccounts);
			}
			public Map<String,String> getIgnoredAccounts() {
				return Collections.unmodifiableMap(ignoredAccounts);
			}
			
			@Override
			public String toString() {
				return "Result["+status+", "+customerId+", "+failedAccounts+", "+successAccounts+", "+ignoredAccounts+"]";
			}
			
			
		}
		
		/**
		 *	Aggregated result of the background process, 
		 *	contains the list of individual result objects,
		 *	and counters for the three statuses.
		 *
		 *  This will be returned from StandingOrdersServiceSessionBean.placeStandingOrders()
		 *  
		 *  Cron job report is based on this
		 */
		public static class ResultList implements Serializable {
			
			private static final long	serialVersionUID	= -7036748124574006340L;
			
			protected int failedCount	= 0;
			protected int successCount	= 0;
			
			
			List<Result> results = new ArrayList<Result>();
			
			public int getTotalCount() {
				return failedCount + successCount ;
			}	
			public int getFailedCount() {
				return failedCount;
			}	
			public int getSuccessCount() {
				return successCount;
			}	
			
			
			public List<Result> getResultsList() {
				return Collections.unmodifiableList( results );
			}
			
			protected void countSuccess() {
				successCount++;
			}	
			protected void countFailed() {
				failedCount++;
			}
			
			
			
			
			protected void count( Status s ) {
				switch ( s ) {
					case SUCCESS:
						countSuccess();
						break;
					case FAILURE:
						countFailed();
						break;
					default:
						break;
				}
			}		
			
			public void add( Result r ) {
				if ( r != null ) {
					count( r.status );
					results.add( r );
				}
			}
			@Override
			public String toString() {
				return "ResultList["+successCount+" success, "+failedCount+" failed, "+getTotalCount()+" total]"; 
			}
			
		}
		

}
