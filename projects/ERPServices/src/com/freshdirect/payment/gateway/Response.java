package com.freshdirect.payment.gateway;

public interface Response  extends java.io.Serializable {
	public TransactionType getTransactionType();
	public BillingInfo getBillingInfo();
	public boolean isRequestProcessed();
	public boolean isApproved();//if isProcessed is false, this is false;
	public boolean isDeclined();//
	public boolean isError();
	public String getResponseTime();
	public String getStatusCode();
	public String getStatusMessage();
	
	/** For chase.. if ProcStatus!=0, set the status from
	 * ProcStatus+StatusMsg;
	 * else 
	 * set the RespCode+ RespMsg TBD.
	 */
	
	public String getAuthCode();
	public boolean isAVSMatch();
	public boolean isCVVMatch();
	public String getAVSResponse();
	public String getCVVResponse();
	public String getRawResponse();
	public String getRawRequest();
	public Request getRequest();
	public Object getGatewayRequest();
	public Object getGatewayResponse();
	public boolean isSuccess();
	public String getResponseCode();
	public String getResponseCodeAlt();

	public String getEwalletId();
	public void setEwalletId(String ewalletId);
	public String getEwalletTxId();
	public void setEwalletTxId(String ewalletId);
}
