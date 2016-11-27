/*
 * ZipPlus4Record.java
 *
 * Created on April 10, 2002, 8:04 PM
 */

package com.freshdirect.dataloader.usps.citystate;

/**
 *
 * @author  mrose
 * @version 
 */
public class CityStateRecord {

    /** Holds value of property zipCode. */
    private String zipCode;    

    /** Holds value of property city. */
    private String city;
    
    /** Holds value of property state. */
    private String state;
    
    private String cityStateKey;
    
    private String countyName;
    
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
    
    /** Getter for property city.
     * @return Value of property city.
     */
    public String getCity() {
        return city;
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    /** Getter for property state.
     * @return Value of property state.
     */
    public String getState() {
        return state;
    }
    
    /** Setter for property state.
     * @param state New value of property state.
     */
    public void setState(String state) {
        this.state = state;
    }
    
	public String getCityStateKey() {
		return cityStateKey;
	}

	public void setCityStateKey(String string) {
		cityStateKey = string;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String string) {
		countyName = string;
	}

}
