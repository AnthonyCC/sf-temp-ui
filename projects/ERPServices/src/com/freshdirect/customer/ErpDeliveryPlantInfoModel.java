/**
 * 
 */
package com.freshdirect.customer;

import java.io.Serializable;

import com.freshdirect.framework.core.ModelSupport;

/**
 * @author ksriram
 *
 */
public class ErpDeliveryPlantInfoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4091031891268060377L;
	private String salesOrg;
    private String distChannel;
    private String plantId;
    private String division;
    
	/**
	 * @return the salesOrg
	 */
	public String getSalesOrg() {
		return salesOrg;
	}
	/**
	 * @param salesOrg the salesOrg to set
	 */
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}
	/**
	 * @return the distChannel
	 */
	public String getDistChannel() {
		return distChannel;
	}
	/**
	 * @param distChannel the distChannel to set
	 */
	public void setDistChannel(String distChannel) {
		this.distChannel = distChannel;
	}
	/**
	 * @return the plantId
	 */
	public String getPlantId() {
		return plantId;
	}
	/**
	 * @param plantId the plantId to set
	 */
	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}
	/**
	 * @return the division
	 */
	public String getDivision() {
		return division;
	}
	/**
	 * @param division the division to set
	 */
	public void setDivision(String division) {
		this.division = division;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((distChannel == null) ? 0 : distChannel.hashCode());
		result = prime * result + ((plantId == null) ? 0 : plantId.hashCode());
		result = prime * result
				+ ((salesOrg == null) ? 0 : salesOrg.hashCode());
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
		ErpDeliveryPlantInfoModel other = (ErpDeliveryPlantInfoModel) obj;
		if (distChannel == null) {
			if (other.distChannel != null)
				return false;
		} else if (!distChannel.equals(other.distChannel))
			return false;
		if (plantId == null) {
			if (other.plantId != null)
				return false;
		} else if (!plantId.equals(other.plantId))
			return false;
		if (salesOrg == null) {
			if (other.salesOrg != null)
				return false;
		} else if (!salesOrg.equals(other.salesOrg))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ErpDeliveryPlantInfoModel [salesOrg=" + salesOrg
				+ ", distChannel=" + distChannel + ", plantId=" + plantId
				+ ", division=" + division + "]";
	}
    
	
    
}
