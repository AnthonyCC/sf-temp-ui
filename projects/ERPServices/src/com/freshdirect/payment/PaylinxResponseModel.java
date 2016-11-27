/*
 * PaylinxResponseModel.java
 *
 * Created on February 5, 2002, 8:01 PM
 */

package com.freshdirect.payment;

/**
 *
 * @author  knadeem
 * @version 
 */
import com.freshdirect.customer.EnumPaymentResponse;

import com.freshdirect.framework.core.*;
import com.freshdirect.customer.ErpAuthorizationModel;

public class PaylinxResponseModel extends ModelSupport {
	
	private static final String AVS_SUCCESS 	= "Y";
	
	private ErpAuthorizationModel authorizationModel;
	private String responseCode;
	private String responseMessage;

	/** Creates new PaylinxResponseModel */
    public PaylinxResponseModel() {
    }
	
	public ErpAuthorizationModel getAuthorizationModel(){
		return this.authorizationModel;
	}
	public void setAuthorizationModel(ErpAuthorizationModel authorizationModel){
		this.authorizationModel = authorizationModel;
	}
	
	public boolean isAuthorized(){
		if(authorizationModel == null){
			return false;
		}else{
			return EnumPaymentResponse.APPROVED.equals(authorizationModel.getResponseCode()) && AVS_SUCCESS.equalsIgnoreCase(authorizationModel.getAvs());
		} 
	}
	
	public String getResponseCode(){
		return this.responseCode;
	}
	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}
	
	public String getResponseMessage(){
		return this.responseMessage;
	}
	public void setResponseMessage(String responseMessage){
		this.responseMessage = responseMessage;
	}

}
