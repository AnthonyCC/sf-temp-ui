package com.freshdirect.dataloader.payment;

public class ECheckRejectedTxDetail implements java.io.Serializable {
	
	/*Amount(4),
	ResponseCode(5),
	RejectResponse(6),
	TxDateTime(7),
	RejectDateTime(8),
	OrderID(9),
	OrderDesc(10),
	BatchID(11),
	TxSource(12),
	ProfileID(13);*/
	
	@Override
	public String toString() {
		return "ECheckRejectedTxDetail [orderID=" + orderID + ", profileID="
				+ profileID + ", rejectResponse=" + rejectResponse + "]";
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getProfileID() {
		return profileID;
	}

	public void setProfileID(String profileID) {
		this.profileID = profileID;
	}

	public String getRejectResponse() {
		return rejectResponse;
	}

	public void setRejectResponse(String rejectResponse) {
		this.rejectResponse = rejectResponse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderID == null) ? 0 : orderID.hashCode());
		result = prime * result
				+ ((profileID == null) ? 0 : profileID.hashCode());
		result = prime * result
				+ ((rejectResponse == null) ? 0 : rejectResponse.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ECheckRejectedTxDetail other = (ECheckRejectedTxDetail) obj;
		if (orderID == null) {
			if (other.orderID != null)
				return false;
		} else if (!orderID.equals(other.orderID))
			return false;
		if (profileID == null) {
			if (other.profileID != null)
				return false;
		} else if (!profileID.equals(other.profileID))
			return false;
		if (rejectResponse == null) {
			if (other.rejectResponse != null)
				return false;
		} else if (!rejectResponse.equals(other.rejectResponse))
			return false;
		return true;
	}

	private String orderID;
	
	private String profileID;
	
	private String rejectResponse;
	
	
	
	

}
