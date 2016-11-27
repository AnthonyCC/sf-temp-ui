/*
 * DlvApartmentRange.java
 *
 * Created on September 20, 2002, 2:39 PM
 */

package com.freshdirect.fdlogistics.model;

import com.freshdirect.logistics.delivery.model.EnumAddressType;


/**
 *
 * @author  mrose
 * @version 
 */
public class FDDeliveryApartmentRange implements java.io.Serializable {
    
    /** Holds value of property low. */
    private String low;
    
    /** Holds value of property high. */
    private String high;

    /** Holds value of property high. */
    private String addressType;

    /** Holds value of property high. */
    private String addressTypeDetailed;

    /** Creates new DlvApartmentRange */
    public FDDeliveryApartmentRange(String low, String high) {
        super();
        this.low = low;
        this.high = high;
    }
    
    public FDDeliveryApartmentRange(String low, String high, String addressType) {
        super();
        this.low = low;
        this.high = high;
        this.addressType = addressType;
    }

    /** Getter for property low.
     * @return Value of property low.
     */
    public String getLow() {
        return low;
    }    
    
    /** Getter for property high.
     * @return Value of property high.
     */
    public String getHigh() {
        return high;
    }    

    public String getAddressType() {
    	return addressType;
    }
    
    public EnumAddressType getAddressTypeDetailed() {
    	return EnumAddressType.getEnum(this.addressType);
    }
}
