/**
 * 
 */
package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.framework.util.DayOfWeekSet;

/**
 * @author ksriram
 *
 */
public class FDPlantMaterial implements Serializable {	

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3626795779046866731L;
	private final EnumATPRule atpRule;
	private final boolean kosherProduction;
	private final boolean platter;
	private final DayOfWeekSet blockedDays;
	private final int leadTime;
	private final String plantId;
	
	private  String freshness;
	
	private EnumOrderLineRating rating;
	private EnumSustainabilityRating sustainabilityRating;
	
	public FDPlantMaterial(EnumATPRule atpRule, boolean kosherProduction,
			boolean platter, DayOfWeekSet blockedDays, int leadTime,
			String plantId) {
		super();
		this.atpRule = atpRule;
		this.kosherProduction = kosherProduction;
		this.platter = platter;
		this.blockedDays = blockedDays;
		this.leadTime = leadTime;
		this.plantId = plantId;
	}
	
	
	


	public FDPlantMaterial(EnumATPRule atpRule, boolean kosherProduction,
			boolean platter, DayOfWeekSet blockedDays, int leadTime,
			String plantId, 
			String freshness, EnumOrderLineRating rating,
			EnumSustainabilityRating sustainabilityRating) {
		super();
		this.atpRule = atpRule;
		this.kosherProduction = kosherProduction;
		this.platter = platter;
		this.blockedDays = blockedDays;
		this.leadTime = leadTime;
		this.plantId = plantId;
		this.freshness = freshness;
		
		this.rating = rating;
		this.sustainabilityRating = sustainabilityRating;
	}





	/**
	 * @return the atpRule
	 */
	public EnumATPRule getAtpRule() {
		return atpRule;
	}
	/**
	 * @return the kosherProduction
	 */
	public boolean isKosherProduction() {
		return kosherProduction;
	}
	/**
	 * @return the platter
	 */
	public boolean isPlatter() {
		return platter;
	}
	/**
	 * @return the blockedDays
	 */
	public DayOfWeekSet getBlockedDays() {
		return blockedDays;
	}
	/**
	 * @return the leadTime
	 */
	public int getLeadTime() {
		return leadTime;
	}
	


	/**
	 * @return the freshness
	 */
	public String getFreshness() {
		return freshness;
	}


	/**
	 * @param freshness the freshness to set
	 */
	public void setFreshness(String freshness) {
		this.freshness = freshness;
	}


	

	/**
	 * @return the rating
	 */
	public EnumOrderLineRating getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(EnumOrderLineRating rating) {
		this.rating = rating;
	}

	/**
	 * @return the sustainabilityRating
	 */
	public EnumSustainabilityRating getSustainabilityRating() {
		return sustainabilityRating;
	}

	/**
	 * @param sustainabilityRating the sustainabilityRating to set
	 */
	public void setSustainabilityRating(
			EnumSustainabilityRating sustainabilityRating) {
		this.sustainabilityRating = sustainabilityRating;
	}

	/**
	 * @return the plantId
	 */
	public String getPlantId() {
		return plantId;
	}
	
	@Override
	public String toString() {
		return "FDPlantMaterial [atpRule=" + atpRule + ", kosherProduction="
				+ kosherProduction + ", platter=" + platter + ", blockedDays="
				+ blockedDays + ", leadTime=" + leadTime + ", plantId="
				+ plantId + ", freshness=" + freshness + ", rating=" + rating
				+ ", sustainabilityRating=" + sustainabilityRating + "]";
	}

}
