package com.freshdirect.fdstore.standingorders.service;

import java.io.Serializable;

import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;


public class StandingOrdersServiceResult implements Serializable {

	private static final long	serialVersionUID	= -4890832579467292432L;
	
	public static enum Status { SUCCESS, FAILURE, SKIPPED };
	
	public static class Result implements Serializable {
		
		private static final long	serialVersionUID	= 7481950658518757363L;
		
		private Status				status;
		private ErrorCode			errorCode;
		private String				detailText;
		private boolean				hasInvalidItems		= false;
		private FDCustomerInfo 		customerInfo;
		
		public Result( ErrorCode errCode, String errText, FDCustomerInfo custInfo ) {
			if ( errCode == null ) {
				throw new IllegalArgumentException( "Error code is required." );				
			}
			status = Status.FAILURE;
			errorCode = errCode;
			detailText = errText;
			customerInfo = custInfo;
		}
		
		public Result( Status status ) {
			if ( status == Status.FAILURE ) {
				throw new IllegalArgumentException( "Failure state requires error code and text." );
			}			
			this.status = status;
		}
		
		public Result( Status status, boolean hasInvalidItems ) {
			if ( status == Status.FAILURE ) {
				throw new IllegalArgumentException( "Failure state requires error code and text." );
			}			
			this.status = status;
			this.hasInvalidItems = hasInvalidItems;
		}
		
		public boolean isError() {
			return errorCode != null;
		}
		
		public boolean isSkipped() {
			return status == Status.SKIPPED;
		}
		
		public boolean isTechnicalError() {
			return errorCode != null && errorCode.isTechnical();
		}

		public boolean hasInvalidItems() {
			return hasInvalidItems;			
		}
		
		public Status getStatus() {
			return status;
		}		
		public ErrorCode getErrorCode() {
			return errorCode;
		}
		public String getDetailText() {
			return detailText;
		}
		
		public FDCustomerInfo getCustomerInfo() {
			return customerInfo;
		}
		
		
		@Override
		public String toString() {
			return "SOSRR["+status+", "+errorCode+", "+detailText+"]";
		}
	}
	
	public static class Counter implements Serializable {
		
		private static final long	serialVersionUID	= -7036748124574006340L;
		
		protected int failedCount	= 0;
		protected int successCount	= 0;
		protected int skippedCount	= 0;
		
		public int getTotalCount() {
			return failedCount + successCount + skippedCount;
		}	
		public int getFailedCount() {
			return failedCount;
		}	
		public int getSuccessCount() {
			return successCount;
		}	
		public int getSkippedCount() {
			return skippedCount;
		}	
		
		public void countSuccess() {
			successCount++;
		}	
		public void countFailed() {
			failedCount++;
		}
		public void countSkipped() {
			skippedCount++;
		}
		
		public void count( Result r ) {
			count( r.status );
		}
		
		public void count( Status s ) {
			switch ( s ) {
				case SUCCESS:
					countSuccess();
					break;
				case FAILURE:
					countFailed();
					break;
				case SKIPPED:
					countSkipped();
					break;
				default:
					break;
			}
		}		
		
		@Override
		public String toString() {
			return "SOSRC["+successCount+" success, "+failedCount+" failed, "+skippedCount+" skipped, "+getTotalCount()+" total]"; 
		}
	}
	
}
