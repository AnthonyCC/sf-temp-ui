
package com.freshdirect.fdlogistics.model;

import java.util.Date;

public class ZoneInfoByZipAndDateKey implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4044731663392603523L;
	private String zipCode;
	private Date startDate;
   
    public ZoneInfoByZipAndDateKey(String zip, Date startDate) {
        super();
        this.zipCode = zip;
        this.startDate = startDate;
    }
    
   	public String getZipCode() {
        return this.zipCode;
    }
	
	public Date getStartDate() {
		return this.startDate;
	}
   
    public String toString(){
    	return "ZoneInfoByZipAndDateKey["+zipCode+", "+startDate+"]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		ZoneInfoByZipAndDateKey other = (ZoneInfoByZipAndDateKey) obj;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}
    
}