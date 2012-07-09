package com.freshdirect.transadmin.model;

public class ZipCodeModel {
	
	private String zipCode;
	private double homeCoverage;
	private double cosCoverage;
	private String ebtAccepted;
		
	public ZipCodeModel(String zipCode, double homeCoverage, double cosCoverage) {
		super();
		this.zipCode = zipCode;
		this.homeCoverage = homeCoverage;
		this.cosCoverage = cosCoverage;
	}
	
	public ZipCodeModel(String zipCode, double homeCoverage, double cosCoverage,String ebtAccepted) {
		super();
		this.zipCode = zipCode;
		this.homeCoverage = homeCoverage;
		this.cosCoverage = cosCoverage;
		this.ebtAccepted = ebtAccepted;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public double getHomeCoverage() {
		return homeCoverage;
	}
	public void setHomeCoverage(double homeCoverage) {
		this.homeCoverage = homeCoverage;
	}
	public double getCosCoverage() {
		return cosCoverage;
	}
	public void setCosCoverage(double cosCoverage) {
		this.cosCoverage = cosCoverage;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		ZipCodeModel other = (ZipCodeModel) obj;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}
	public String getEbtAccepted() {
		return ebtAccepted;
	}
	public void setEbtAccepted(String ebtAccepted) {
		this.ebtAccepted = ebtAccepted;
	}	
	
	
	
}
