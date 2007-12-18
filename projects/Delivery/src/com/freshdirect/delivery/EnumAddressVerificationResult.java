/*
 * EnumAddressVerificationResult.java
 *
 * Created on July 31, 2002, 1:21 PM
 */

package com.freshdirect.delivery;

/**
 *
 * @author  mrose
 * @version 
 */
public class EnumAddressVerificationResult implements java.io.Serializable {
	
    public final static EnumAddressVerificationResult NOT_VERIFIED         = new EnumAddressVerificationResult(-1, "NOT_VERIFIED");
    public final static EnumAddressVerificationResult ADDRESS_OK           = new EnumAddressVerificationResult( 0, "ADDRESS_OK");
    public final static EnumAddressVerificationResult ADDRESS_BAD          = new EnumAddressVerificationResult( 1, "ADDRESS_BAD");
    public final static EnumAddressVerificationResult ADDRESS_NOT_UNIQUE   = new EnumAddressVerificationResult( 2, "ADDRESS_NOT_UNIQUE");
    public final static EnumAddressVerificationResult STREET_WRONG         = new EnumAddressVerificationResult( 3, "STREET_WRONG");
    public final static EnumAddressVerificationResult BUILDING_WRONG       = new EnumAddressVerificationResult( 4, "BUILDING_WRONG");
    public final static EnumAddressVerificationResult APT_WRONG            = new EnumAddressVerificationResult( 5, "APT_WRONG");
	
	protected final int id;
	private final String code;
	
	/** Creates new EnumAddressVerificationResult */
    private EnumAddressVerificationResult(int id, String code) {
		this.id = id;
		this.code = code;
    }
	
	public String getCode() {
		return this.code;
	}
	
	public boolean equals(Object o) {
		if (o instanceof EnumAddressVerificationResult) {
			return this.id == ((EnumAddressVerificationResult)o).id;
		}
		return false;
    }

    public String toString() {
        return "EnumAddressVerificationResult : " + this.code;
    }
    
}
