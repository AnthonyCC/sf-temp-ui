
package com.freshdirect.fdlogistics.model;

import java.util.Date;

public class FDDeliveryZipInfo implements java.io.Serializable {
	
    private String zipCode;
	private Date startDate;
    private double coverage;
   
    public FDDeliveryZipInfo(String zip, Date sd, double cov) {
        super();
        this.zipCode = zip;
        this.startDate = sd;
        this.coverage = cov;
    }
    
   	public String getZipCode() {
        return this.zipCode;
    }
	
	public Date getStartDate() {
		return this.startDate;
	}
    
    public double getCoverage() {
        return this.coverage;
    }
    
    public String toString(){
    	return "DlvZipInfoModel["+zipCode+", "+startDate+", "+coverage+",]";
    }
    
}