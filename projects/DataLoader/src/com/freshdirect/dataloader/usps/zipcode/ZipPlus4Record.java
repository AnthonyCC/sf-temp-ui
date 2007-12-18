/*
 * ZipPlus4Record.java
 *
 * Created on April 10, 2002, 8:04 PM
 */

package com.freshdirect.dataloader.usps.zipcode;

/**
 *
 * @author  mrose
 * @version 
 */
public class ZipPlus4Record {

    /** Holds value of property zipCode. */
    private String zipCode;
    
    /** Holds value of property recordType. */
    private String recordType;
    
    /** Holds value of property streetPreDirectional. */
    private String streetPreDirectional;
    
    /** Holds value of property streetName. */
    private String streetName;
    
    /** Holds value of property streetSuffix. */
    private String streetSuffix;
    
    /** Holds value of property streetPostDirectional. */
    private String streetPostDirectional;
    
    /** Holds value of property addrPrimaryLow. */
    private String addrPrimaryLow;
    
    /** Holds value of property addrPrimaryHigh. */
    private String addrPrimaryHigh;
    
    /** Holds value of property addrPrimaryEven. */
    private boolean addrPrimaryEven;
    
    /** Holds value of property addrPrimaryOdd. */
    private boolean addrPrimaryOdd;
    
    /** Holds value of property firmName. */
    private String firmName;
    
    /** Holds value of property addrSecondaryAbbrev. */
    private String addrSecondaryAbbrev;
    
    /** Holds value of property addrSecondaryLow. */
    private String addrSecondaryLow;
    
    /** Holds value of property addrSecondaryHigh. */
    private String addrSecondaryHigh;
    
    /** Holds value of property addrSecondaryEven. */
    private boolean addrSecondaryEven;
    
    /** Holds value of property addrSecondaryOdd. */
    private boolean addrSecondaryOdd;
    
    /** Holds value of property zipAddonLow. */
    private String zipAddonLow;
    
    /** Holds value of property zipAddonHigh. */
    private String zipAddonHigh;
    
    /** Holds value of property baseAltCode. */
    private String baseAltCode;
    
    private String cityStateKey;
    
    /** Creates new ZipPlus4Record */
    public ZipPlus4Record() {
    }

    /** Getter for property zipCode.
     * @return Value of property zipCode.
     */
    public String getZipCode() {
        return zipCode;
    }
    
    /** Setter for property zipCode.
     * @param zipCode New value of property zipCode.
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    /** Getter for property recordType.
     * @return Value of property recordType.
     */
    public String getRecordType() {
        return recordType;
    }
    
    /** Setter for property recordType.
     * @param recordType New value of property recordType.
     */
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
    
    /** Getter for property streetPreDirectional.
     * @return Value of property streetPreDirectional.
     */
    public String getStreetPreDirectional() {
        return streetPreDirectional;
    }
    
    /** Setter for property streetPreDirectional.
     * @param streetPreDirectional New value of property streetPreDirectional.
     */
    public void setStreetPreDirectional(String streetPreDirectional) {
        this.streetPreDirectional = streetPreDirectional;
    }
    
    /** Getter for property streetName.
     * @return Value of property streetName.
     */
    public String getStreetName() {
        return streetName;
    }
    
    /** Setter for property streetName.
     * @param streetName New value of property streetName.
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    
    /** Getter for property streetSuffix.
     * @return Value of property streetSuffix.
     */
    public String getStreetSuffix() {
        return streetSuffix;
    }
    
    /** Setter for property streetSuffix.
     * @param streetSuffix New value of property streetSuffix.
     */
    public void setStreetSuffix(String streetSuffix) {
        this.streetSuffix = streetSuffix;
    }
    
    /** Getter for property streetPostDirectional.
     * @return Value of property streetPostDirectional.
     */
    public String getStreetPostDirectional() {
        return streetPostDirectional;
    }
    
    /** Setter for property streetPostDirectional.
     * @param streetPostDirectional New value of property streetPostDirectional.
     */
    public void setStreetPostDirectional(String streetPostDirectional) {
        this.streetPostDirectional = streetPostDirectional;
    }
    
    /** Getter for property addrPrimaryLow.
     * @return Value of property addrPrimaryLow.
     */
    public String getAddrPrimaryLow() {
        return addrPrimaryLow;
    }
    
    /** Setter for property addrPrimaryLow.
     * @param addrPrimaryLow New value of property addrPrimaryLow.
     */
    public void setAddrPrimaryLow(String addrPrimaryLow) {
        this.addrPrimaryLow = addrPrimaryLow;
    }
    
    /** Getter for property addrPrimaryHigh.
     * @return Value of property addrPrimaryHigh.
     */
    public String getAddrPrimaryHigh() {
        return addrPrimaryHigh;
    }
    
    /** Setter for property addrPrimaryHigh.
     * @param addrPrimaryHigh New value of property addrPrimaryHigh.
     */
    public void setAddrPrimaryHigh(String addrPrimaryHigh) {
        this.addrPrimaryHigh = addrPrimaryHigh;
    }
    
    /** Getter for property addrPrimaryEven.
     * @return Value of property addrPrimaryEven.
     */
    public boolean isAddrPrimaryEven() {
        return addrPrimaryEven;
    }
    
    /** Setter for property addrPrimaryEven.
     * @param addrPrimaryEven New value of property addrPrimaryEven.
     */
    public void setAddrPrimaryEven(boolean addrPrimaryEven) {
        this.addrPrimaryEven = addrPrimaryEven;
    }
    
    /** Getter for property addrPrimaryOdd.
     * @return Value of property addrPrimaryOdd.
     */
    public boolean isAddrPrimaryOdd() {
        return addrPrimaryOdd;
    }
    
    /** Setter for property addrPrimaryOdd.
     * @param addrPrimaryOdd New value of property addrPrimaryOdd.
     */
    public void setAddrPrimaryOdd(boolean addrPrimaryOdd) {
        this.addrPrimaryOdd = addrPrimaryOdd;
    }
    
    /** Getter for property firmName.
     * @return Value of property firmName.
     */
    public String getFirmName() {
        return firmName;
    }
    
    /** Setter for property firmName.
     * @param firmName New value of property firmName.
     */
    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
    
    /** Getter for property addrSecondaryAbbrev.
     * @return Value of property addrSecondaryAbbrev.
     */
    public String getAddrSecondaryAbbrev() {
        return addrSecondaryAbbrev;
    }
    
    /** Setter for property addrSecondaryAbbrev.
     * @param addrSecondaryAbbrev New value of property addrSecondaryAbbrev.
     */
    public void setAddrSecondaryAbbrev(String addrSecondaryAbbrev) {
        this.addrSecondaryAbbrev = addrSecondaryAbbrev;
    }
    
    /** Getter for property addrSecondaryLow.
     * @return Value of property addrSecondaryLow.
     */
    public String getAddrSecondaryLow() {
        return addrSecondaryLow;
    }
    
    /** Setter for property addrSecondaryLow.
     * @param addrSecondaryLow New value of property addrSecondaryLow.
     */
    public void setAddrSecondaryLow(String addrSecondaryLow) {
        this.addrSecondaryLow = addrSecondaryLow;
    }
    
    /** Getter for property addrSecondaryHigh.
     * @return Value of property addrSecondaryHigh.
     */
    public String getAddrSecondaryHigh() {
        return addrSecondaryHigh;
    }
    
    /** Setter for property addrSecondaryHigh.
     * @param addrSecondaryHigh New value of property addrSecondaryHigh.
     */
    public void setAddrSecondaryHigh(String addrSecondaryHigh) {
        this.addrSecondaryHigh = addrSecondaryHigh;
    }
    
    /** Getter for property addrSecondaryEven.
     * @return Value of property addrSecondaryEven.
     */
    public boolean isAddrSecondaryEven() {
        return addrSecondaryEven;
    }
    
    /** Setter for property addrSecondaryEven.
     * @param addrSecondaryEven New value of property addrSecondaryEven.
     */
    public void setAddrSecondaryEven(boolean addrSecondaryEven) {
        this.addrSecondaryEven = addrSecondaryEven;
    }
    
    /** Getter for property addrSecondaryOdd.
     * @return Value of property addrSecondaryOdd.
     */
    public boolean isAddrSecondaryOdd() {
        return addrSecondaryOdd;
    }
    
    /** Setter for property addrSecondaryOdd.
     * @param addrSecondaryOdd New value of property addrSecondaryOdd.
     */
    public void setAddrSecondaryOdd(boolean addrSecondaryOdd) {
        this.addrSecondaryOdd = addrSecondaryOdd;
    }
    
    /** Getter for property zipAddonLow.
     * @return Value of property zipAddonLow.
     */
    public String getZipAddonLow() {
        return zipAddonLow;
    }
    
    /** Setter for property zipAddonLow.
     * @param zipAddonLow New value of property zipAddonLow.
     */
    public void setZipAddonLow(String zipAddonLow) {
        this.zipAddonLow = zipAddonLow;
    }
    
    /** Getter for property zipAddonHigh.
     * @return Value of property zipAddonHigh.
     */
    public String getZipAddonHigh() {
        return zipAddonHigh;
    }
    
    /** Setter for property zipAddonHigh.
     * @param zipAddonHigh New value of property zipAddonHigh.
     */
    public void setZipAddonHigh(String zipAddonHigh) {
        this.zipAddonHigh = zipAddonHigh;
    }
    
    /** Getter for property baseAltCode.
     * @return Value of property baseAltCode.
     */
    public String getBaseAltCode() {
        return baseAltCode;
    }
    
    /** Setter for property baseAltCode.
     * @param baseAltCode New value of property baseAltCode.
     */
    public void setBaseAltCode(String baseAltCode) {
        this.baseAltCode = baseAltCode;
    }
    
	public String getCityStateKey() {
		return cityStateKey;
	}

	public void setCityStateKey(String string) {
		cityStateKey = string;
	}

}
