package com.freshdirect.common.pricing;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDRuntimeException;

public class MunicipalityInfoWrapper implements Serializable {
	
	Map municipalityMap;
	
	public MunicipalityInfoWrapper(List municipalityInfos){
		 if (municipalityInfos.equals(Collections.EMPTY_LIST)) {
		 	municipalityMap = Collections.EMPTY_MAP;
		 } else {
		 	municipalityMap = new HashMap();
		 	for (Iterator miItr = municipalityInfos.iterator(); miItr.hasNext(); ){
		 		MunicipalityInfo mi = (MunicipalityInfo)miItr.next();
		 		municipalityMap.put(mi.getState().toUpperCase()
		 			 +(mi.getCounty()!=null ? ":" + mi.getCounty().toUpperCase() : "")
					 + (mi.getCity()!=null ? ":" + mi.getCity().toUpperCase() : ""),mi);
		 	}
		 }
	}

	private String makeMapKey(String part1, String part2, String part3) {
		if (part1!=null && part2!=null & part3!=null) return (part1+":"+part2+":"+part3).toUpperCase();
		if (part1!=null && part2!=null & part3==null) return (part1+":"+part2).toUpperCase();
		if (part1!=null && part2==null & part3==null) return (part1).toUpperCase();
		return "";
	}
	
	public MunicipalityInfo getMunicipalityInfo(String state) {
		return getMunicipalityInfo(state, null, null);
	}
		
	public MunicipalityInfo getMunicipalityInfo(String state, String county,String city) {
		MunicipalityInfo mi = (MunicipalityInfo) municipalityMap.get(makeMapKey(state, county,city));
		if (mi==null) {
			mi = (MunicipalityInfo) municipalityMap.get(makeMapKey(state, county,null));
		}
		if (mi==null ) {
			mi = (MunicipalityInfo) municipalityMap.get(makeMapKey(state, null,null));
		}
		
		return mi;
	}
	

	public double getTaxRate(String state, String county, String city) {
		MunicipalityInfo mi = getMunicipalityInfo(state,county,city);
		return mi!=null ? mi.getTaxRate() : 0.0;
	}
	

	public double getBottleDeposit(String state, String county, String city) {
		MunicipalityInfo mi = getMunicipalityInfo(state,county,city);
		return mi!=null ? mi.getBottleDeposit() : 0.0;
	}

	public String getGlCode(String state, String county, String city) {
		MunicipalityInfo mi = getMunicipalityInfo(state,county,city);
		return mi.getGlCode();
	}
	
	public boolean isAlcoholRestricted(String state, String county, String city){
		MunicipalityInfo mi = getMunicipalityInfo(state,county,city);
		return mi.isAlcoholRestricted();
	}
	
}
