package com.freshdirect.payment;

import java.util.Comparator;

public class BillingRegionInfo implements java.io.Serializable  {
	
	private String countryCode;
	private String code;
	private String name;
	private String codeExt;
	
	public String getCountryCode() {
		return countryCode;
	}

	public String getCode() {
		return code;
	}
	public String getCodeExt() {
		
		return codeExt;
	}
	@Override
	public String toString() {
		return "BillingRegionInfo [code=" + code + ", countryCode="
				+ countryCode + ", name=" + name +", codeExt=" + codeExt + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((codeExt == null) ? 0 : codeExt.hashCode());
		result = prime * result
				+ ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		BillingRegionInfo other = (BillingRegionInfo) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (codeExt == null) {
			if (other.codeExt != null)
				return false;
		} else if (!codeExt.equals(other.codeExt))
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public BillingRegionInfo(String countryCode,String code,String name, String codeExt) {
		this.countryCode=countryCode;
		this.code=code;
		this.name=name;
		if(codeExt==null)
			this.codeExt=code;
		else if("".equals(codeExt.trim()))
			this.codeExt=code;
		else 
			this.codeExt=codeExt;
	}
	public static Comparator<BillingRegionInfo> COMPARE_BY_NAME = new Comparator<BillingRegionInfo>(){
		public int compare(BillingRegionInfo bc1, BillingRegionInfo bc2) {
		   	return bc1.getName().compareTo(bc2.getName());
		}
	};
}
