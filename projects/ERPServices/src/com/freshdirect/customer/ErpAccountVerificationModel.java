/*
 * Created on May 12, 2005
 *
 */
package com.freshdirect.customer;

/**
 * @author jng
 */
public class ErpAccountVerificationModel extends ErpCaptureModel {
	private String verificationReult;	
	public String getVerificationResult() { return this.verificationReult;}
	public void setVerificationResult(String verificationResult) { this.verificationReult = verificationResult;}
}
