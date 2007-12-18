/*
 * EnumDlvRange.java
 *
 * Created on July 31, 2002, 1:21 PM
 */

package com.freshdirect.delivery;

/**
 *
 * @author  mrose
 * @version 
 */
public class EnumDlvRange implements java.io.Serializable {
	
    public final static EnumDlvRange HOME_DELIVERY        = new EnumDlvRange( 0,  "HOME_DELIVERY");
    public final static EnumDlvRange PICKUP               = new EnumDlvRange( 1,  "PICKUP");
    public final static EnumDlvRange NO_SERVICE           = new EnumDlvRange( 99, "NO_SERVICE");
	
	protected final int id;
	private final String code;
	
	/** Creates new EnumDlvRange */
    private EnumDlvRange(int id, String code) {
		this.id = id;
		this.code = code;
    }
	
	public String getCode() {
		return this.code;
	}
	
	public boolean equals(Object o) {
		if (o instanceof EnumDlvRange) {
			return this.id == ((EnumDlvRange)o).id;
		}
		return false;
    }

    public String toString() {
        return "EnumDlvRange : " + this.code;
    }
    
}
