/*
 * DlvInfoModel.java
 *
 * Created on January 22, 2002, 6:46 PM
 */

package com.freshdirect.delivery;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.sql.Timestamp;
import java.util.Date;

public class DlvZipInfoModel implements java.io.Serializable {
	
    private String zipCode;
	private Date startDate;
    private double coverage;
    private boolean cosEnabled;

	public boolean isCosEnabled() {
		return cosEnabled;
	}
	
	/** Creates new DlvInfoModel 
	 * @param b */
    public DlvZipInfoModel(String zip, Date sd, double cov, boolean enableCOS) {
        super();
        this.zipCode = zip;
        this.startDate = sd;
        this.coverage = cov;
        this.cosEnabled=enableCOS;
    }
    
    /** Creates new DlvInfoModel 
	 * @param b */
    public DlvZipInfoModel(String zip, Date sd, double cov) {
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
    	return "DlvZipInfoModel["+zipCode+", "+startDate+", "+coverage+", "+cosEnabled+"]";
    }
    
}