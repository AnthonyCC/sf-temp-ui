package com.freshdirect.erp.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.EnumDayPartValueType;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.StringUtil;

/**
 * ErpProductInfo model class.
 *
 * @version	$Revision$
 * @author	 $Author$
 * @stereotype fd-model
 */
public class ErpProductInfoModel extends ModelSupport {
	
	private static final long	serialVersionUID	= -8185580401974680369L;

	public static class ErpMaterialPrice implements Serializable{
		
		private static final long	serialVersionUID	= 610096190678790338L;

		private final double price;
		
		private final String unit;
		
		private double promoPrice;
		
		private final String scaleUnit;
		
		private final double scaleQuantity;
		
		private final String sapZoneId;
		
		private final String salesOrg;
		
		private final String distChannel;
		
		
		
		
		public ErpMaterialPrice(double price, String unit, double promoPrice, String scaleUnit, double scaleQuantity, String sapZoneId, String salesOrg, String distChannel) {
			this.price = price;
			this.unit = unit;
			this.promoPrice = promoPrice;
			this.scaleUnit = scaleUnit;
			this.scaleQuantity = scaleQuantity;
			this.sapZoneId = sapZoneId;
//			this.salesOrg = salesOrg;
//			this.distChannel = distChannel;
//			this.salesOrg = "1000".equals(salesOrg)?"0001":salesOrg;
//			this.distChannel = "1000".equals(salesOrg)?"01":distChannel;

			this.salesOrg =StringUtil.isEmpty(salesOrg)?"0001":("1000".equals(salesOrg)?"0001":salesOrg);
			this.distChannel = StringUtil.isEmpty(distChannel)?"01":("1000".equals(distChannel)?"01":distChannel);
		}
		
		public double getPrice() {
			return price;
		}
		
		public String getUnit() {
			return unit;
		}
		
		public double getPromoPrice() {
			return promoPrice;
		}
		
		public void setPromoPrice(double promoPrice) {
			this.promoPrice = promoPrice;
		}
		
		public String getScaleUnit() {
			return scaleUnit;
		}
		
		public double getScaleQuantity() {
			return scaleQuantity;
		}
		
		public String getSapZoneId() {
			return sapZoneId;
		}

		/**
		 * @return the salesOrg
		 */
		public String getSalesOrg() {
			return salesOrg;
		}

		/**
		 * @return the distChannel
		 */
		public String getDistChannel() {
			return distChannel;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((distChannel == null) ? 0 : distChannel.hashCode());
			long temp;
			temp = Double.doubleToLongBits(price);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(promoPrice);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result
					+ ((salesOrg == null) ? 0 : salesOrg.hashCode());
			result = prime * result
					+ ((sapZoneId == null) ? 0 : sapZoneId.hashCode());
			temp = Double.doubleToLongBits(scaleQuantity);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result
					+ ((scaleUnit == null) ? 0 : scaleUnit.hashCode());
			result = prime * result + ((unit == null) ? 0 : unit.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ErpMaterialPrice other = (ErpMaterialPrice) obj;
			if (distChannel == null) {
				if (other.distChannel != null)
					return false;
			} else if (!distChannel.equals(other.distChannel))
				return false;
			if (Double.doubleToLongBits(price) != Double
					.doubleToLongBits(other.price))
				return false;
			if (Double.doubleToLongBits(promoPrice) != Double
					.doubleToLongBits(other.promoPrice))
				return false;
			if (salesOrg == null) {
				if (other.salesOrg != null)
					return false;
			} else if (!salesOrg.equals(other.salesOrg))
				return false;
			if (sapZoneId == null) {
				if (other.sapZoneId != null)
					return false;
			} else if (!sapZoneId.equals(other.sapZoneId))
				return false;
			if (Double.doubleToLongBits(scaleQuantity) != Double
					.doubleToLongBits(other.scaleQuantity))
				return false;
			if (scaleUnit == null) {
				if (other.scaleUnit != null)
					return false;
			} else if (!scaleUnit.equals(other.scaleUnit))
				return false;
			if (unit == null) {
				if (other.unit != null)
					return false;
			} else if (!unit.equals(other.unit))
				return false;
			return true;
		}
	}
	
	
	public static class ErpPlantMaterialInfo implements Serializable{
		
		
		private boolean kosherProduction;

		private boolean platter;

		private DayOfWeekSet blockedDays;	
		
		/** the ATPRule to use when checking for availablility of this material */
		private EnumATPRule atpRule;
		
		/** product rating */
		private String rating;
		
		private String freshness;
		
		/** sustainability rating */
		private String sustainabilityRating;

		/** the lead time in days to stock this product **/
		private int leadTime;
		
		private String plantId;
		
		private boolean isLimitedQuantity;
		
		public ErpPlantMaterialInfo(boolean kosherProduction,
				boolean platter, DayOfWeekSet blockedDays, EnumATPRule atpRule,
				String rating, String freshness,
				String sustainabilityRating,String plantId, boolean isLimitedQuantity) {
			super();
			this.kosherProduction = kosherProduction;
			this.platter = platter;
			this.blockedDays = blockedDays;
			this.atpRule = atpRule;
			this.rating = rating;
			this.freshness = freshness;
			this.sustainabilityRating = sustainabilityRating;
			this.plantId = plantId;
			this.isLimitedQuantity = isLimitedQuantity;
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
		 * @return the atpRule
		 */
		public EnumATPRule getAtpRule() {
			return atpRule;
		}

		/**
		 * @return the rating
		 */
		public String getRating() {
			return rating;
		}

		/**
		 * @return the days_in_house
		 */
		public String getFreshness() {
			return freshness;
		}

		/**
		 * @return the sustainabilityRating
		 */
		public String getSustainabilityRating() {
			return sustainabilityRating;
		}

		/**
		 * @return the leadTime
		 */
		public int getLeadTime() {
			return leadTime;
		}

		/**
		 * @return the plantId
		 */
		public String getPlantId() {
			return plantId;
		}
		
		/**
		 * 
		 * @return isLimitedQuantity
		 */
		public boolean isLimitedQuantity() {
			return isLimitedQuantity;
		}



		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((atpRule == null) ? 0 : atpRule.hashCode());
			result = prime * result
					+ ((freshness == null) ? 0 : freshness.hashCode());
			result = prime * result + (kosherProduction ? 1231 : 1237);
			result = prime * result + leadTime;
			result = prime * result
					+ ((plantId == null) ? 0 : plantId.hashCode());
			result = prime * result + (platter ? 1231 : 1237);
			result = prime * result
					+ ((rating == null) ? 0 : rating.hashCode());
			result = prime
					* result
					+ ((sustainabilityRating == null) ? 0
							: sustainabilityRating.hashCode());
			return result;
		}



		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ErpPlantMaterialInfo other = (ErpPlantMaterialInfo) obj;
			if (atpRule == null) {
				if (other.atpRule != null)
					return false;
			} else if (!atpRule.equals(other.atpRule))
				return false;
			if (freshness == null) {
				if (other.freshness != null)
					return false;
			} else if (!freshness.equals(other.freshness))
				return false;
			if (kosherProduction != other.kosherProduction)
				return false;
			if (leadTime != other.leadTime)
				return false;
			if (plantId == null) {
				if (other.plantId != null)
					return false;
			} else if (!plantId.equals(other.plantId))
				return false;
			if (platter != other.platter)
				return false;
			if (rating == null) {
				if (other.rating != null)
					return false;
			} else if (!rating.equals(other.rating))
				return false;
			if (sustainabilityRating == null) {
				if (other.sustainabilityRating != null)
					return false;
			} else if (!sustainabilityRating.equals(other.sustainabilityRating))
				return false;
			return true;
		}



		
	}
	
	public static class ErpMaterialSalesAreaInfo implements Serializable{
		
		private SalesAreaInfo salesAreaInfo;
		private String unavailabilityStatus;
		private Date unavailabilityDate;
		private String unavailabilityReason;
		private String dayPartType;
		private String pickingPlantId;
		/**
		 * @param salesAreaInfo
		 * @param unavailabilityStatus
		 * @param unavailabilityDate
		 * @param unavailabilityReason
		 */
		public ErpMaterialSalesAreaInfo(SalesAreaInfo salesAreaInfo,
				String unavailabilityStatus, Date unavailabilityDate,
				String unavailabilityReason, String dayPartType, String pickingPlantId) {
			super();
			this.salesAreaInfo = salesAreaInfo;
			if(unavailabilityStatus==null)
				this.unavailabilityStatus = EnumAvailabilityStatus.AVAILABLE.getStatusCode();
			else 
				this.unavailabilityStatus=unavailabilityStatus;
			this.unavailabilityDate = unavailabilityDate;
			this.unavailabilityReason = unavailabilityReason;
			this.dayPartType = dayPartType;
			this.pickingPlantId = pickingPlantId;
		}
		/**
		 * @return the salesAreaInfo
		 */
		public SalesAreaInfo getSalesAreaInfo() {
			return salesAreaInfo;
		}
		/**
		 * @param salesAreaInfo the salesAreaInfo to set
		 */
		public void setSalesAreaInfo(SalesAreaInfo salesAreaInfo) {
			this.salesAreaInfo = salesAreaInfo;
		}
		/**
		 * @return the unavailabilityStatus
		 */
		public String getUnavailabilityStatus() {
			return unavailabilityStatus;
		}
		/**
		 * @param unavailabilityStatus the unavailabilityStatus to set
		 */
		public void setUnavailabilityStatus(String unavailabilityStatus) {
			this.unavailabilityStatus = unavailabilityStatus;
		}
		/**
		 * @return the unavailabilityDate
		 */
		public Date getUnavailabilityDate() {
			return unavailabilityDate;
		}
		/**
		 * @param unavailabilityDate the unavailabilityDate to set
		 */
		public void setUnavailabilityDate(Date unavailabilityDate) {
			this.unavailabilityDate = unavailabilityDate;
		}
		/**
		 * @return the unavailabilityReason
		 */
		public String getUnavailabilityReason() {
			return unavailabilityReason;
		}
		/**
		 * @param unavailabilityReason the unavailabilityReason to set
		 */
		public void setUnavailabilityReason(String unavailabilityReason) {
			this.unavailabilityReason = unavailabilityReason;
		}
		/**
		 * @return the dayPartType
		 */
		public String getDayPartType() {
			return dayPartType;
		}
		/**
		 * @param dayPartType the dayPartType to set
		 */
		public void setDayPartType(String dayPartType) {
			this.dayPartType = dayPartType;
		}
		public String getPickingPlantId() {
			return pickingPlantId;
		}
		public void setPickingPlantId(String pickingPlantId) {
			this.pickingPlantId = pickingPlantId;
		}
		
		
		
	}
	/** version number */
	private int version;

	/** SKU code */
	private final String skuCode;


	/** SapIds for materials in this product */
	private final String[] materialNumbers;
	
	/** unit prices of materials for this product */
	private ErpMaterialPrice[] materialPrices;
		
	private String description;
	
	private String upc;
	
	private ErpPlantMaterialInfo materialPlants[];
	
	private ErpMaterialSalesAreaInfo materialSalesAreas[];

//	private final boolean isAlcohol;
	
	private EnumAlcoholicContent alcoholicType;
	
	

	/**
	 * Constructor with all properties.
	 *
	 * @param skuCode SKU code
	 * @param defaultPrice default price
	 * @param defaultPriceUnit pricing unit for default price
	 * @param materialNumbers
	 * @param unavailabilityStatus
	 * @param unavailabilityDate
	 * @param unavailabilityReason
	 * @param description
	 * @param rating
	 * @param freshness 
	 * @param sustainabilityRating
	 *//*
	public ErpProductInfoModel(
		String skuCode,
		int version,
		String[] materialNumbers,
		ErpMaterialPrice[] materialPrices,
		EnumATPRule atpRule,
		String unavailabilityStatus,
		Date unavailabilityDate,
		String unavailabilityReason,
		String description,
		String rating,
		String freshness,
		String sustainabilityRating,
		String upc) {

		super();
		this.skuCode = skuCode;
		this.version = version;
		this.materialNumbers = materialNumbers;
		this.materialPrices = materialPrices;
		this.atpRule = atpRule;
		this.unavailabilityStatus = unavailabilityStatus;
		this.unavailabilityDate = unavailabilityDate;
		this.unavailabilityReason = unavailabilityReason;
		this.description = description;
		this.rating=rating;
		this.freshness=freshness;
		this.sustainabilityRating=sustainabilityRating;
		this.upc = upc;
	}

	public ErpProductInfoModel(String skuCode,
			String[] materialNumbers, EnumATPRule atpRule,
			String unavailabilityStatus, Date unavailabilityDate,
			String unavailabilityReason, String description, String rating,
			String freshness, String sustainabilityRating, String upc) {
		super();
		this.skuCode = skuCode;
		this.materialNumbers = materialNumbers;
		this.atpRule = atpRule;
		this.unavailabilityStatus = unavailabilityStatus;
		this.unavailabilityDate = unavailabilityDate;
		this.unavailabilityReason = unavailabilityReason;
		this.description = description;
		this.rating = rating;
		this.freshness = freshness;
		this.sustainabilityRating = sustainabilityRating;
		this.upc = upc;
	}

	*/
	public ErpProductInfoModel(String skuCode,int version, 
			String[] materialNumbers, String description, ErpMaterialPrice[] materialPrices,
			String upc, ErpPlantMaterialInfo[] materialPlants,ErpMaterialSalesAreaInfo[] materialSalesAreas, EnumAlcoholicContent alcoholicType) {
		super();
		this.version = version;
		this.skuCode = skuCode;
		this.materialNumbers = materialNumbers;
		this.materialPrices = materialPrices;
		this.description = description;
		this.upc = upc;
		this.materialPlants = materialPlants;
		this.materialSalesAreas = materialSalesAreas;
		this.alcoholicType = alcoholicType;
	}

	public ErpProductInfoModel( String skuCode,int version,
			String[] materialNumbers, String description, String upc,
			ErpPlantMaterialInfo[] materialPlants,  EnumAlcoholicContent alcoholicType) {
		super();
		this.version = version;
		this.skuCode = skuCode;
		this.materialNumbers = materialNumbers;
		this.description = description;
		this.upc = upc;
		this.materialPlants = materialPlants;
		this.alcoholicType=alcoholicType;
	}

	/**
	 * Get version number
	 *
	 * @return version number
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Get Product SKU code.
	 *
	 * @return SKU code
	 */
	public String getSkuCode() {
		return this.skuCode;
	}


	/**
	 * Get Material Numbers.
	 *
	 * @return material numbers (SapIds) for this product's materials
	 */
	public String[] getMaterialSapIds() {
		return this.materialNumbers;
	}
	
	/**
	 * Get Material Prices.
	 * 
	 * @return material prices
	 */
	public ErpMaterialPrice[] getMaterialPrices() {
		return materialPrices;
	}
	
	public void setMaterialPrices(ErpMaterialPrice[] materialPrices) {
		this.materialPrices = materialPrices;
	}

	/*public EnumATPRule getATPRule() {
		if(null !=materialPlants && materialPlants.length > 0){
			return materialPlants[0].getAtpRule();
		}
		return null;
	}*/

	/** Getter for property unavailabilityDate.
	 * @return Value of property unavailabilityDate.
	 */
	/*public Date getUnavailabilityDate() {
		if(null !=materialSalesAreas && materialSalesAreas.length > 0){
			return materialSalesAreas[0].getUnavailabilityDate();
		}
		return null;
	}*/

	/** Getter for property unavailabilityReason.
	 * @return Value of property unavailabilityReason.
	 */
	/*public String getUnavailabilityReason() {
		if(null !=materialSalesAreas && materialSalesAreas.length > 0){
			return materialSalesAreas[0].getUnavailabilityReason();
		}
		return null;
	}*/

	/** Getter for property unavailabilityStatus.
	 * @return Value of property unavailabilityStatus.
	 */
	/*public String getUnavailabilityStatus() {
		if(null !=materialSalesAreas && materialSalesAreas.length > 0){
			return materialSalesAreas[0].getUnavailabilityStatus();
		}
		return null;
	}*/

	/** Getter for property description.
	 * @return Value of property description.
	 */
	public String getDescription() {
		return description;
	}

	/** Getter for property rating.
	 * @return Value of property rating.
	 */
	/*public String getRating() {
		if(null !=materialPlants && materialPlants.length > 0){
			return materialPlants[0].getRating();
		}
		return null;
	}*/

	/** Getter for property freshness.
	 * @return Value for property freshness.
	 */
	/*public String getFreshness() {
		if(FDStoreProperties.IsFreshnessGuaranteedEnabled()){
			if(null !=materialPlants && materialPlants.length > 0){
				return materialPlants[0].getFreshness();
			}
			return null;
		}
		return null;
	}*/

	/**
	 * Getter for property sustainabilityRating.
	 * 
	 * @return Value of property sustainabilityRating.
	 */
	/*public String getSustainabilityRating() {
		if(null !=materialPlants && materialPlants.length > 0){
			return materialPlants[0].getSustainabilityRating();
		}
		return null;
	}*/

	public String getUpc() {
		return upc;
	}

	/**
	 * @return the materialPlants
	 */
	public ErpPlantMaterialInfo[] getMaterialPlants() {
		return materialPlants;
	}
	
	public ErpMaterialSalesAreaInfo[] getSalesAreas() {
		return  materialSalesAreas;
	}

	@Override
	public String toString() {
		return "ErpProductInfoModel [version=" + version +" alcoholicType=" +alcoholicType+ ", skuCode="
				+ skuCode + ", materialNumbers="
				+ Arrays.toString(materialNumbers) + ", materialPrices="
				+ description + ", upc=" + upc + ", materialPlants="
				+ Arrays.toString(materialPlants) + ", materialSalesAreas="
				+ Arrays.toString(materialSalesAreas) + "]";
	}

	public EnumAlcoholicContent getAlcoholicType() {
		// TODO Auto-generated method stub
		return alcoholicType;
	}

	

		
}