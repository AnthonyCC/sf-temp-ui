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
		private String				errorHeader;
		private String				errorDetail;
		private boolean				hasInvalidItems		= false;
		private FDCustomerInfo 		customerInfo;
		private String				saleId;
		
		public Result( ErrorCode errCode, String errorHeader, String errorDetail, FDCustomerInfo custInfo ) {
			if ( errCode == null ) {
				throw new IllegalArgumentException( "Error code is required." );				
			}
			this.status = Status.FAILURE;
			this.errorCode = errCode;
			this.errorHeader = errorHeader;
			this.errorDetail = errorDetail;
			this.customerInfo = custInfo;
		}
		
		public Result( ErrorCode errCode, FDCustomerInfo custInfo ) {
			if ( errCode == null ) {
				throw new IllegalArgumentException( "Error code is required." );				
			}
			status = Status.FAILURE;
			errorCode = errCode;
			errorHeader = errCode.getErrorHeader();
			errorDetail = errCode.getErrorDetail();
			customerInfo = custInfo;
		}
		
		public Result( Status status ) {
			if ( status == Status.FAILURE ) {
				throw new IllegalArgumentException( "Failure state requires error code and text." );
			}			
			this.status = status;
		}
		
		public Result( Status status, boolean hasInvalidItems, String saleId ) {
			if ( status == Status.FAILURE ) {
				throw new IllegalArgumentException( "Failure state requires error code and text." );
			}			
			this.status = status;
			this.hasInvalidItems = hasInvalidItems;
			this.saleId = saleId;
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
		public String getErrorHeader() {
			return errorHeader;
		}
		public String getErrorDetail() {
			return errorDetail;
		}
		
		public FDCustomerInfo getCustomerInfo() {
			return customerInfo;
		}
		
		public String getSaleId() {
			return saleId;
		}
		
		@Override
		public String toString() {
			return "SOSRR["+status+", "+errorCode+", "+errorHeader+", "+errorDetail+"]";
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
