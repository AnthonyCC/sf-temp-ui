/*
 * Created on May 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.common.pricing;

import java.io.Serializable;

public class MunicipalityInfo implements Serializable{
	private String state;
	private String county;
	private String city;
	private String glCode;
	private double taxRate;
	private double bottleDeposit;
	private boolean alcoholRestricted;
	
	public MunicipalityInfo(String state, String county, String city, String gl_code, 
		                     double tax_rate, double bottle_deposit, boolean alcohol_restricted) {
		this.state = state;
		this.county = county;
		this.city = city;
		this.glCode = gl_code;
		this.taxRate = tax_rate;
		this.bottleDeposit = bottle_deposit;
		this.alcoholRestricted = alcohol_restricted;
	}
	
	public boolean hasBottleDeposit() {
		return this.bottleDeposit > 0;
	}
	
	public double getBottleDeposit() {
		return this.bottleDeposit;
	}
	
	public double getTaxRate() {
		return this.taxRate;
	}
	
	public String getGlCode() {
		return this.glCode;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public String getCounty() {
		return this.county;
	}
	
	public String getState() {
		return this.state;
	}
	
	public boolean isAlcoholRestricted() {
		return alcoholRestricted;
	}
}
