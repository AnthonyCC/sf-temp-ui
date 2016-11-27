package com.freshdirect.erp.model;

import java.util.Date;

import com.freshdirect.erp.DurableModelI;
import com.freshdirect.erp.EntityModelI;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;
import com.freshdirect.framework.util.DayOfWeekSet;

/**
 * 
 * @author ksriram
 *
 */
public class ErpPlantMaterialModel extends ErpModelSupport implements DurableModelI, EntityModelI
{
	private static final long serialVersionUID = -3163726300145481329L;

	private String plantId;
	
	private boolean kosherProduction;

	private boolean platter;

	private DayOfWeekSet blockedDays;	
	
	/** the ATPRule to use when checking for availablility of this material */
	private EnumATPRule atpRule;
	
	/** product rating */
	private String rating;
		
	/** product days in house */
	private String days_in_house;
	
	/** sustainability rating */
	private String sustainabilityRating;

	/** the lead time in days to stock this product **/
	private int leadTime;
	
	/** hide if its out of stock **/
	private boolean hideOutOfStock;
	
	
	@Override
	public void visitChildren(ErpVisitorI visitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDurableId() {
		// TODO Auto-generated method stub
		return this.getPlantId();
	}

	/**
	 * @return the plantId
	 */
	public String getPlantId() {
		return plantId;
	}

	/**
	 * @param plantId the plantId to set
	 */
	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	/**
	 * @return the kosherProduction
	 */
	public boolean isKosherProduction() {
		return kosherProduction;
	}

	/**
	 * @param kosherProduction the kosherProduction to set
	 */
	public void setKosherProduction(boolean kosherProduction) {
		this.kosherProduction = kosherProduction;
	}

	/**
	 * @return the platter
	 */
	public boolean isPlatter() {
		return platter;
	}

	/**
	 * @param platter the platter to set
	 */
	public void setPlatter(boolean platter) {
		this.platter = platter;
	}

	/**
	 * @return the blockedDays
	 */
	public DayOfWeekSet getBlockedDays() {
		return blockedDays;
	}

	/**
	 * @param blockedDays the blockedDays to set
	 */
	public void setBlockedDays(DayOfWeekSet blockedDays) {
		this.blockedDays = blockedDays;
	}

	/**
	 * @return the atpRule
	 */
	public EnumATPRule getAtpRule() {
		return atpRule;
	}

	/**
	 * @param atpRule the atpRule to set
	 */
	public void setAtpRule(EnumATPRule atpRule) {
		this.atpRule = atpRule;
	}

	/**
	 * @return the rating
	 */
	public String getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	/**
	 * @return the days_in_house
	 */
	public String getDays_in_house() {
		return days_in_house;
	}

	/**
	 * @param days_in_house the days_in_house to set
	 */
	public void setDays_in_house(String days_in_house) {
		this.days_in_house = days_in_house;
	}

	/**
	 * @return the sustainabilityRating
	 */
	public String getSustainabilityRating() {
		return sustainabilityRating;
	}

	/**
	 * @param sustainabilityRating the sustainabilityRating to set
	 */
	public void setSustainabilityRating(String sustainabilityRating) {
		this.sustainabilityRating = sustainabilityRating;
	}

	
	/**
	 * @return the leadTime
	 */
	public int getLeadTime() {
		return leadTime;
	}

	/**
	 * @param leadTime the leadTime to set
	 */
	public void setLeadTime(int leadTime) {
		this.leadTime = leadTime;
	}
	

	public boolean isHideOutOfStock() {
		return hideOutOfStock;
	}

	public void setHideOutOfStock(boolean hideOutOfStock) {
		this.hideOutOfStock = hideOutOfStock;
	}

	/**
	 * @param plantId
	 * @param kosherProduction
	 * @param platter
	 * @param blockedDays
	 * @param atpRule
	 * @param rating
	 * @param days_in_house
	 * @param sustainabilityRating
	 * @param unavailabilityStatus
	 * @param unavailabilityDate
	 * @param unavailabilityReason
	 * @param leadTime
	 */
	public ErpPlantMaterialModel(String plantId, boolean kosherProduction,
			boolean platter, DayOfWeekSet blockedDays, EnumATPRule atpRule,
			String rating, String days_in_house,
			String sustainabilityRating,int leadTime, boolean hideOutOfStock) {
		super();
		this.plantId = plantId;
		this.kosherProduction = kosherProduction;
		this.platter = platter;
		this.blockedDays = blockedDays;
		this.atpRule = atpRule;
		this.rating = rating;
		this.days_in_house = days_in_house;
		this.sustainabilityRating = sustainabilityRating;
		this.leadTime = leadTime;
		this.hideOutOfStock = hideOutOfStock;
	}

	/**
	 * Default constructor.
	 */
	public ErpPlantMaterialModel()
	{
		super();
	}
	
	@Override
	public String toString() {
		return "ErpPlantMaterialModel [plantId=" + plantId
				+ ", kosherProduction=" + kosherProduction + ", platter="
				+ platter + ", blockedDays=" + blockedDays + ", atpRule="
				+ atpRule + ", rating=" + rating + ", days_in_house="
				+ days_in_house + ", sustainabilityRating="
				+ sustainabilityRating + ", leadTime=" + leadTime
				+ ", hideOutOfStock=" + hideOutOfStock + "]";
	}

}
