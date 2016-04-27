/*
 * DlvAddressVerificationResponse.java
 *
 * Created on July 31, 2002, 3:08 PM
 */

package com.freshdirect.fdlogistics.model;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumAddressVerificationResult;

/**
 *
 * @author  mrose
 * @version 
 */
public class FDDeliveryAddressVerificationResponse implements java.io.Serializable {
    
    public FDDeliveryAddressVerificationResponse(AddressModel address,
			ActionResult result, EnumAddressVerificationResult verifyResult, FDDeliveryServiceSelectionResult serviceResult,
			String geocodeResult) {
		super();
		this.address = address;
		this.result = result;
		this.verifyResult = verifyResult;
		this.serviceResult = serviceResult;
		this.geocodeResult = geocodeResult;
	}

	public FDDeliveryAddressVerificationResponse() {
		super();
	}

	AddressModel address;
	private ActionResult result;
	private FDDeliveryServiceSelectionResult serviceResult;
	private String geocodeResult;
	private EnumAddressVerificationResult verifyResult;
	
    public AddressModel getAddress() {
        return this.address;
    }

	public ActionResult getResult() {
		return result;
	}

	public String getGeocodeResult() {
		return geocodeResult;
	}

	public void setGeocodeResult(String geocodeResult) {
		this.geocodeResult = geocodeResult;
	}

	public EnumAddressVerificationResult getVerifyResult() {
		return verifyResult;
	}

	public void setVerifyResult(EnumAddressVerificationResult verifyResult) {
		this.verifyResult = verifyResult;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
	}

	public void setResult(ActionResult result) {
		this.result = result;
	}

	public FDDeliveryServiceSelectionResult getServiceResult() {
		return serviceResult;
	}

	public void setServiceResult(FDDeliveryServiceSelectionResult serviceResult) {
		this.serviceResult = serviceResult;
	}

}
