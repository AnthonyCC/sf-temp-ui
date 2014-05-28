package com.freshdirect.fdstore;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.ecoupon.FDCouponFactory;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.framework.util.StringUtil;

/**
 * Lightweight information about a product, that is necessary for display on a category page.
 *
 */

public class FDProductInfo extends FDSku  {
	
	private static final long	serialVersionUID	= 308857606199221581L;

	private final String[] materialNumbers;
    
	/** Availability checking rule */
    private final EnumATPRule atpRule;

    /** SAP availability status */
    private final EnumAvailabilityStatus availStatus;
    
    /** SAP availability date */
    private final Date availDate;
     
    /** inventory info for the product but this should only be set in a TEST CASE */
    private final FDInventoryCacheI inventory;
	
    private final EnumOrderLineRating rating;
    /** availability restricted dates for the product */
    private final Date[] availabilityDates;

    
    /*  days guaranteed fresh upon delivery */
    private final String freshness;
    
	/** Default price in USD */

    //Zone Price Info Listing. 
    private final ZonePriceInfoListing zonePriceInfoList;
    
    //FDGroup contains Group identity if applicable.
    private final FDGroup group;
    
    private EnumSustainabilityRating sustainabilityRating;
    
    private String upc;
    
    public FDProductInfo(String skuCode, int version, 
    		String[] materialNumbers, EnumATPRule atpRule, EnumAvailabilityStatus availStatus, Date availDate, 
    		FDInventoryCacheI inventory, EnumOrderLineRating rating, String freshness,
    		ZonePriceInfoListing zonePriceInfoList, FDGroup group, EnumSustainabilityRating sustainabilityRating,
    		String upc, Date[] availabilityDates) {

		super(skuCode, version);

		this.materialNumbers = materialNumbers;
		this.atpRule = atpRule;
        this.availStatus = availStatus;
        this.availDate = availDate;
        
        this.inventory = inventory;
        this.rating=rating;
        this.zonePriceInfoList = zonePriceInfoList;

        this.freshness=freshness;
        this.group = group;
        if(sustainabilityRating!=null) {
        	this.sustainabilityRating=sustainabilityRating;
        }
        this.upc = upc;
        this.availabilityDates = availabilityDates;
	
    }

	public Date[] getAvailabilityDates() {
		return availabilityDates;
	}

	/**
	 * Get inventory (short term availability) information.
	 */
	public ErpInventoryModel getInventory() {
		if(this.inventory != null){
			return this.inventory.getInventory(materialNumbers[0]);
		}
		return FDInventoryCache.getInstance().getInventory(materialNumbers[0]);
	}




	public EnumATPRule getATPRule() {
		return this.atpRule;
	}
	
    /**
	 * Get the SAP availability status of this product.
	 *
	 * @return true if product is discontinued
	 */
	public EnumAvailabilityStatus getAvailabilityStatus() {
		return this.availStatus;
	}
    
    /**
	 * Get the SAP availability date of this product.
	 * This attribute is not used.
	 *
	 * @return the availability date, as returned by SAP.
	 */
	public Date getAvailabilityDate() {
		return this.availDate;
	}
    
    public boolean isAvailable() {
        return EnumAvailabilityStatus.AVAILABLE.equals(this.availStatus);
    }
    
    public boolean isDiscontinued() {
        return EnumAvailabilityStatus.DISCONTINUED.equals(this.availStatus);
    }
    
    public boolean isOutOfSeason() {
        return EnumAvailabilityStatus.OUT_OF_SEASON.equals(this.availStatus);
    }
    
    public boolean isTempUnavailable() {
        return EnumAvailabilityStatus.TEMP_UNAV.equals(this.availStatus);
    }

    public EnumOrderLineRating getRating() {
    	return this.rating;
    }
    
	/**
	 * Get the unit of measure for the "default" price.
	 *
	 * @return unit of measure
	 */
	public String getDefaultPriceUnit() {
		return this.zonePriceInfoList.getZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE).getDefaultPriceUnit();
	}
	
	/**
	 * Get the unit of measure for the "default" price.
	 *
	 * @return pricing unit attribute if one, else return the default Price unit
	 */
	public String getDisplayableDefaultPriceUnit() {
		
		return this.zonePriceInfoList.getZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE).getDisplayableDefaultPriceUnit();
	}

    public String getFreshness() {
    	if(FDStoreProperties.IsFreshnessGuaranteedEnabled()) {
    		 // Freshness Guaranteed list of qualifying sku prefixees
            String skuPrefixes = FDStoreProperties.getFreshnessGuaranteedSkuPrefixes();
            
            // if we have prefixes then check them
            if (skuPrefixes != null && !"".equals(skuPrefixes)) {
                StringTokenizer st = new StringTokenizer(skuPrefixes, ","); // split comma-delimited list
                String curPrefix = ""; // holds prefix to check against
                
                while (st.hasMoreElements()) {
                    curPrefix = st.nextToken();
                    // if prefix matches get product info
                    if (getSkuCode().startsWith(curPrefix)) {
                        
                        if ((freshness != null && freshness.trim().length() > 0) 
                        	&& !"000".equalsIgnoreCase(freshness.trim()) 
                        	&& StringUtil.isNumeric(freshness) 
                        	&& Integer.parseInt(freshness) > 0)  {
                        	    	return freshness;
                        }   
                    }
                }
            } 
    	}
    	
    	return null;
    }
    
    public String toString() {
        return "FDProductInfo[" + this.getSkuCode() + " v" + this.getVersion() + "\n\tmaterialNumbers:" 
                + (this.materialNumbers != null ? StringUtil.encodeString(this.materialNumbers) : null) + "\n\tavailStatus" + this.availStatus.getShortDescription() 
                + "\n\tavailDate:" + this.availDate + "\n\trating:" + this.rating +"\n\tsustainabilityRating:" + this.sustainabilityRating+"\n\tUPC:" + this.upc + "\n\tatpRule:" + this.atpRule 
                + "\n\tfreshness:" + this.freshness + "\n\tdefaultPriceUnit:" + this.getDefaultPriceUnit() + "\n\tavailDate:"+this.availDate 
                + "\n\t" + this.zonePriceInfoList +"\n\t"+this.group + "\n\tinventory:" +this.getInventory()+ "\n]";
    }
    	public List<String> getCountryOfOrigin() {
		if(materialNumbers==null) 
			return null;
		return FDCOOLInfoCache.getInstance().getCOOLInfo(materialNumbers[0]);
	}
	
	public ZonePriceInfoModel getZonePriceInfo(String pZoneId) {
		try {
			ZonePriceInfoModel zpInfo = this.zonePriceInfoList.getZonePriceInfo(pZoneId);
			if(zpInfo == null) {
				//do a item cascading to its parent until we find a price info.
				ErpZoneMasterInfo zoneInfo = FDCachedFactory.getZoneInfo(pZoneId);
				if(null !=zoneInfo.getParentZone()){
					zpInfo = getZonePriceInfo(zoneInfo.getParentZone().getSapId());
				}
			} 
			return zpInfo;
		}
		catch(FDResourceException fe){
			throw new FDRuntimeException(fe, "Unexcepted error happened while fetching the Zone Price Info for SKU : "+this.getSkuCode());
		}
	}

	public ZonePriceInfoListing getZonePriceInfoList() {
		return this.zonePriceInfoList;
	}

	public FDProductInfo copy(int version, EnumAvailabilityStatus availability, EnumOrderLineRating newRating, String newFreshness) {
	    return new FDProductInfo (getSkuCode(), version, materialNumbers, atpRule, availability, new Date(), inventory, newRating, newFreshness, zonePriceInfoList.clone(), group, sustainabilityRating, upc, availabilityDates);
	}

	public FDGroup getGroup() {
		return group;
	}

	public boolean isGroupExists(){
		return group != null;
	}
	/** Getter for property sustainabilityRating.
     * @return Value of property sustainabilityRating.
     */
    public EnumSustainabilityRating getSustainabilityRating() {
        return sustainabilityRating;
    }
    
    public String getUpc() {
		return upc;
	}

	public FDCouponInfo getCoupon() {
		return FDCouponFactory.getInstance().getCouponByUpc(upc);
	}
	
}
