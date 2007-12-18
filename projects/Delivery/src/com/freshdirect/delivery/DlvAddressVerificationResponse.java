/*
 * DlvAddressVerificationResponse.java
 *
 * Created on July 31, 2002, 3:08 PM
 */

package com.freshdirect.delivery;

import com.freshdirect.common.address.AddressModel;

/**
 *
 * @author  mrose
 * @version 
 */
public class DlvAddressVerificationResponse implements java.io.Serializable {
    
    EnumAddressVerificationResult result;
	AddressModel address;

    /** Creates new DlvAddressVerificationResponse */
    public DlvAddressVerificationResponse(EnumAddressVerificationResult result, AddressModel address) {
        this.result = result;
        this.address = address;
    }
    
    public EnumAddressVerificationResult getResult() {
        return this.result;
    }
    
    public AddressModel getAddress() {
        return this.address;
    }

}
