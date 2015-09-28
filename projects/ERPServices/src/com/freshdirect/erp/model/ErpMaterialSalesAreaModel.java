/**
 * 
 */
package com.freshdirect.erp.model;

import java.util.Date;

import com.freshdirect.erp.DurableModelI;
import com.freshdirect.erp.EntityModelI;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;

/**
 * @author ksriram
 *
 */
public class ErpMaterialSalesAreaModel extends ErpModelSupport implements DurableModelI, EntityModelI {


	private static final long serialVersionUID = -8532087430166402932L;

	/** sales organization */
	private String salesOrg;
	
	/** distribution channel */
	private String distChannel;
	
	/** unavailability status code */
    private String unavailabilityStatus;
    
    /** unavailability date */
    private Date unavailabilityDate;
    
    /** unavailability reason */
    private String unavailabilityReason;
    
    /** sku code */
    private String skuCode;
    
    private String dayPartSelling;
    
    
	public ErpMaterialSalesAreaModel(String salesOrg, String distChannel,
			String unavailabilityStatus, Date unavailabilityDate,
			String unavailabilityReason, String skuCode, String dayPartSelling) {
		super();
		this.salesOrg = salesOrg;
		this.distChannel = distChannel;
		if("AVAL".equalsIgnoreCase(unavailabilityStatus))
			this.unavailabilityStatus="";
		else 
			this.unavailabilityStatus = unavailabilityStatus;
		this.unavailabilityDate = unavailabilityDate;
		this.unavailabilityReason = unavailabilityReason;
		this.skuCode = skuCode;
		this.dayPartSelling = dayPartSelling;
	}

	/**
	 * 
	 */
	public ErpMaterialSalesAreaModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDurableId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void visitChildren(ErpVisitorI visitor) {
		// TODO Auto-generated method stub
		
	}

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
	 * @return the unavailabilityStatus
	 */
	public String getUnavailabilityStatus() {
		return unavailabilityStatus;
	}

	/**
	 * @param unavailabilityStatus the unavailabilityStatus to set
	 */
	public void setUnavailabilityStatus(String unavailabilityStatus) {
		this.unavailabilityStatus = unavailabilityStatus;
	}

	/**
	 * @return the unavailabilityDate
	 */
	public Date getUnavailabilityDate() {
		return unavailabilityDate;
	}

	/**
	 * @param unavailabilityDate the unavailabilityDate to set
	 */
	public void setUnavailabilityDate(Date unavailabilityDate) {
		this.unavailabilityDate = unavailabilityDate;
	}

	/**
	 * @return the unavailabilityReason
	 */
	public String getUnavailabilityReason() {
		return unavailabilityReason;
	}

	/**
	 * @param unavailabilityReason the unavailabilityReason to set
	 */
	public void setUnavailabilityReason(String unavailabilityReason) {
		this.unavailabilityReason = unavailabilityReason;
	}

	/**
	 * @return the skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * @param skuCode the skuCode to set
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getDayPartSelling() {
		return dayPartSelling;
	}

	public void setDayPartSelling(String dayPartSelling) {
		this.dayPartSelling = dayPartSelling;
	}

	@Override
	public String toString() {
		return "ErpMaterialSalesAreaModel [salesOrg=" + salesOrg
				+ ", distChannel=" + distChannel + ", unavailabilityStatus="
				+ unavailabilityStatus + ", unavailabilityDate="
				+ unavailabilityDate + ", unavailabilityReason="
				+ unavailabilityReason + ", skuCode=" + skuCode
				+ ", dayPartSelling=" + dayPartSelling + "]";
	}

	

	
}
