package com.freshdirect.fdstore.standingorders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDUserI;
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
		private String 				customerId;
		private String				saleId;
		private String				soId;
		private String				soName;
		private boolean				errorEmailSentToAdmins	= false;
		private boolean				errorEmailSentToCustomer = false;
		
		/**
		 * This message appears in the internal report only.
		 */
		private String internalMessage;
		
		private List<String> unavailableItems;
		
		public Result(FDStandingOrder so) {
			this.soId = so == null ? "null" : so.getId();
			this.soName = so == null ? "null" : so.getCustomerListName();
		}

		@Deprecated
		public Result( ErrorCode errCode, String errorHeader, String errorDetail, String customerId, FDCustomerInfo custInfo, String soId, String soName ) {
			if ( errCode == null ) {
				throw new IllegalArgumentException( "Error code is required." );				
			}
			this.status = Status.FAILURE;
			this.errorCode = errCode;
			this.errorHeader = errorHeader;
			this.errorDetail = errorDetail;
			this.customerId = customerId;
			this.customerInfo = custInfo;
			this.soId = soId;
			this.soName = soName;
		}
		
		public Result setError(ErrorCode errCode, String errorHeader, String errorDetail) {
			this.status = Status.FAILURE;
			this.errorCode = errCode;
			this.errorHeader = errorHeader;
			this.errorDetail = errorDetail;
			return this;
		}
		
		public Result withTechError(String errorDetail) {
			return this.setError(ErrorCode.TECHNICAL, ErrorCode.TECHNICAL.getErrorHeader(), errorDetail);
		}
		
		public Result withUserRelatedError(ErrorCode errCode, FDUserI user) {
			return this.setError(errCode, errCode.getErrorHeader(), errCode.getErrorDetail(user));
		}
		
		public void setCustomerData(String customerId, FDCustomerInfo custInfo) {
			this.customerId = customerId;
			this.customerInfo = custInfo;			
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
		
		public void setInvalidItems(boolean invalidItems) {
			this.hasInvalidItems = invalidItems;
		}
		
		public Status getStatus() {
			return status;
		}		
		
		public Result withStatus(Status status) {
			this.status = status;
			return this;
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
		
		public String getCustId() {
			return customerId;
		}
		
		public FDCustomerInfo getCustomerInfo() {
			return customerInfo;
		}
		
		public String getSaleId() {
			return saleId;
		}
		
		public String getSoId() {
			return soId;
		}

		public String getSoName() {
			return soName;
		}
		
		@Override
		public String toString() {
			return "SOSRR["+status+", "+errorCode+", "+errorHeader+", "+errorDetail+"]";
		}

		
		public boolean isErrorEmailSentToAdmins() {
			return errorEmailSentToAdmins;
		}

		
		public void setErrorEmailSentToAdmins() {
			this.errorEmailSentToAdmins = true;
		}

		
		public boolean isErrorEmailSentToCustomer() {
			return errorEmailSentToCustomer;
		}

		
		public void setErrorEmailSentToCustomer() {
			this.errorEmailSentToCustomer = true;
		}
		
		public String getInternalMessage() {
			return internalMessage;
		}

		public void setInternalMessage(String internalMessage) {
			this.internalMessage = internalMessage;
		}
		
		public List<String> getUnavailableItems() {
			return unavailableItems;
		}

		public void setUnavailableItems(List<String> unavailableItems) {
			this.unavailableItems = unavailableItems;
		}
		
	}
	
	public static class Counter implements Serializable {
		
		private static final long	serialVersionUID	= -7036748124574006340L;
		
		protected int failedCount	= 0;
		protected int successCount	= 0;
		protected int skippedCount	= 0;
		
		List<Result> results = new ArrayList<Result>();
		
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
		
		public List<Result> getResultsList() {
			return Collections.unmodifiableList( results );
		}
		
		protected void countSuccess() {
			successCount++;
		}	
		protected void countFailed() {
			failedCount++;
		}
		protected void countSkipped() {
			skippedCount++;
		}
		
		public void count( Result r ) {
			if ( r != null ) {
				count( r.status );
				results.add( r );
			}
		}
		
		protected void count( Status s ) {
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
