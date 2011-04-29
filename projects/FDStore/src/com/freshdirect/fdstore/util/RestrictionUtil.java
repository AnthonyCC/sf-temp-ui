package com.freshdirect.fdstore.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.NVL;

public class RestrictionUtil {
	private final static Comparator<AlcoholRestriction> ACL_RESTRICTION_DATE_COMPARATOR = new Comparator<AlcoholRestriction>() {

		public int compare(AlcoholRestriction r1, AlcoholRestriction r2) {
			if(r1 == null || r1.getDateRange() == null || r2 == null || r2.getDateRange() == null ) return -1;
			return r2.getDateRange().getStartDate().compareTo(r1.getDateRange().getStartDate());
		}
	};

	public static List<RestrictionI> filterAlcoholRestrictionsForStateCounty(String state, String county, List<RestrictionI> restrictions){
		List<RestrictionI> filteredList = new ArrayList<RestrictionI>();
		List<AlcoholRestriction> stateRestrictions = new ArrayList<AlcoholRestriction>();
		List<AlcoholRestriction> countyRestrictions = new ArrayList<AlcoholRestriction>();
		
		Iterator<RestrictionI> it = restrictions.iterator();
		while(it.hasNext()){
			RestrictionI restriction = it.next();
			if(restriction instanceof AlcoholRestriction){
				AlcoholRestriction res = (AlcoholRestriction) restriction;
				if(res.getState() != null && res.getCounty() != null){
					//Restriction defined at county level. 
					if(res.getState().equalsIgnoreCase(state) && res.getCounty().equalsIgnoreCase(county)){
						countyRestrictions.add(res);
					}
				}
				if(res.getState() != null && res.getCounty() == null){
					//Restriction defined at state level.
					if(res.getState().equalsIgnoreCase(state)){
						stateRestrictions.add(res);
					}
				}
			}else {
				filteredList.add(restriction);
			}
		}
		if(countyRestrictions.size() > 0){
			if(countyRestrictions.size() > 1) //Sort by highest start date.
				Collections.sort(countyRestrictions, ACL_RESTRICTION_DATE_COMPARATOR);
			filteredList.addAll(countyRestrictions);
		}
		else if(stateRestrictions.size() > 0) {
			if(stateRestrictions.size() > 1) //Sort by highest start date.
				Collections.sort(stateRestrictions, ACL_RESTRICTION_DATE_COMPARATOR);
			filteredList.addAll(stateRestrictions);
		}
		
		return filteredList; 
	}

}
