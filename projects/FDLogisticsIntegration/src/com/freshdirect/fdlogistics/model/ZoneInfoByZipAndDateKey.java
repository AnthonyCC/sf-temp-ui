
package com.freshdirect.fdlogistics.model;

import java.util.Date;

public class ZoneInfoByZipAndDateKey implements java.io.Serializable {
	
	public ZoneInfoByZipAndDateKey(String zipCode, Date startDate,
			String scrubbedStreet, String serviceType, String customerId) {
		super();
		this.zipCode = zipCode;
		this.startDate = startDate;
		this.scrubbedStreet = scrubbedStreet;
		this.serviceType = serviceType;
		this.customerId = customerId;
	}

	private static final long serialVersionUID = -4044731663392603523L;

	private String zipCode;
	private Date startDate;
	private String scrubbedStreet;
	private String serviceType;
	private String customerId;
   
   	public String getZipCode() {
        return this.zipCode;
    }

	public Date getStartDate() {
		return this.startDate;
	}

	public String getScrubbedStreet() {
	    return this.scrubbedStreet;
	}

    public String toString(){
    	return "ZoneInfoByZipAndDateKey["+zipCode+", "+startDate+", "+scrubbedStreet+ "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result
				+ ((scrubbedStreet == null) ? 0 : scrubbedStreet.hashCode());
		result = prime * result
				+ ((serviceType == null) ? 0 : serviceType.hashCode());
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
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (scrubbedStreet == null) {
			if (other.scrubbedStreet != null)
				return false;
		} else if (!scrubbedStreet.equals(other.scrubbedStreet))
			return false;
		if (serviceType == null) {
			if (other.serviceType != null)
				return false;
		} else if (!serviceType.equals(other.serviceType))
			return false;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
    
}