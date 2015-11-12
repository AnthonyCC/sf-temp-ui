package com.freshdirect.fdstore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.ecoupon.FDCouponFactory;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Lightweight information about a product, that is necessary for display on a category page.
 *
 */

public class FDProductInfo extends FDSku  {

	private static final long	serialVersionUID	= 308857606199221581L;

	private final String[] materialNumbers;

	private final static Category LOGGER = LoggerFactory.getInstance(FDProductInfo.class);

    /** SAP availability status */
   // private EnumAvailabilityStatus availStatus;//::FDX::

    /** SAP availability date */
    //private Date availDate;

    /** inventory info for the product but this should only be set in a TEST CASE */
    private final FDInventoryCacheI inventory;


    /** availability restricted dates for the product */
   // private final Date[] availabilityDates;//::FDX:: unused-removing it.


    /*  days guaranteed fresh upon delivery */
    private String freshness;

	/** Default price in USD */

    //Zone Price Info Listing.
    private ZonePriceInfoListing zonePriceInfoList;

    //FDGroup contains Group identity if applicable.
    private final  Map<String,FDGroup> groups;


    private String upc;
        private String familyID;


    private Map<String,FDPlantMaterial> plantMaterialInfo=new HashMap<String,FDPlantMaterial>(5);

    private Map<String, FDMaterialSalesArea> materialAvailability=null;

    public FDProductInfo(String skuCode,int version,String[] materials,FDInventoryCacheI inventory,ZonePriceInfoListing zonePriceInfoList,Map<String,FDPlantMaterial> plantMaterialInfo,Map<String, FDMaterialSalesArea> materialAvailability) {
    	super(skuCode, version);
    	this.zonePriceInfoList=zonePriceInfoList;
    	this.inventory=inventory;
    	this.materialNumbers=materials;
    	this.groups=null;
    	this.plantMaterialInfo=plantMaterialInfo;
    	this.materialAvailability=materialAvailability;
    }
     public FDProductInfo(String skuCode, int version,
    		String[] materialNumbers, EnumATPRule atpRule, EnumAvailabilityStatus availStatus, Date availDate,
    		FDInventoryCacheI inventory, EnumOrderLineRating rating, String freshness,
    		ZonePriceInfoListing zonePriceInfoList, Map<String,FDGroup> groups, EnumSustainabilityRating sustainabilityRating,
    		String upc, String familyID,Map<String,FDPlantMaterial> plantMaterialInfo,Map<String, FDMaterialSalesArea> materialSalesArea) {

		super(skuCode, version);

		this.materialNumbers = materialNumbers;

		for(FDMaterialSalesArea msa:materialSalesArea.values()) {
			msa.setUnavailabilityDate(availDate);
			msa.setUnavailabilityStatus(EnumAvailabilityStatus.AVAILABLE.getStatusCode());

		}
		List<FDPlantMaterial> mpInfos=new ArrayList<FDPlantMaterial>(materialSalesArea.size());
		for(FDPlantMaterial mpInfo:plantMaterialInfo.values()) {
			mpInfos.add(new FDPlantMaterial( atpRule,
					                         mpInfo.isKosherProduction(),
					                         mpInfo.isPlatter(),
					                         mpInfo.getBlockedDays(),
					                         mpInfo.getLeadTime(),
					                         mpInfo.getPlantId(),
					                         freshness,
					                         rating,
					                         sustainabilityRating,
					                         mpInfo.isLimitedQuantity()
					                         ));
		}
		_setPlantMaterialInfo(mpInfos);
        this.inventory = inventory;
        this.zonePriceInfoList = zonePriceInfoList;
        this.groups = groups;
        this.upc = upc;
	    this.familyID = familyID;
        this.materialAvailability=materialSalesArea;
    }

     public FDProductInfo(String skuCode, int version,
     		String[] materialNumbers, EnumATPRule atpRule, EnumAvailabilityStatus availStatus, Date availDate,
     		FDInventoryCacheI inventory,ZonePriceInfoListing zonePriceInfoList, Map<String,FDGroup> groups,
     		String upc,String familyID, Map<String,FDPlantMaterial> plantMaterialInfo,Map<String, FDMaterialSalesArea> materialSalesArea) {

 		super(skuCode, version);

 		this.materialNumbers = materialNumbers;

 		for(FDMaterialSalesArea msa:materialSalesArea.values()) {
 			msa.setUnavailabilityDate(availDate);
 			msa.setUnavailabilityStatus(EnumAvailabilityStatus.AVAILABLE.getStatusCode());

 		}
 		List<FDPlantMaterial> mpInfos=new ArrayList<FDPlantMaterial>(materialSalesArea.size());
 		for(FDPlantMaterial mpInfo:plantMaterialInfo.values()) {
 			mpInfos.add(new FDPlantMaterial( atpRule,
 					                         mpInfo.isKosherProduction(),
 					                         mpInfo.isPlatter(),
 					                         mpInfo.getBlockedDays(),
 					                         mpInfo.getLeadTime(),
 					                         mpInfo.getPlantId(),
 					                         mpInfo.getFreshness(),
 					                         mpInfo.getRating(),
 					                         mpInfo.getSustainabilityRating(),
 					                         mpInfo.isLimitedQuantity()
 					                         ));
 		}
 		_setPlantMaterialInfo(mpInfos);
         this.inventory = inventory;
         this.zonePriceInfoList = zonePriceInfoList;
         this.groups = groups;
         this.upc = upc;
         this.materialAvailability=materialSalesArea;
         this.familyID = familyID;
     }
     //::FDX::

    public FDProductInfo(String skuCode, int version, String[] materialNumbers,
			FDInventoryCacheI inventory,Map<String,FDGroup> groups, String upc, List<FDPlantMaterial> plantMaterialInfo,ZonePriceInfoListing zonePriceInfoList,
			Map<String, FDMaterialSalesArea> materialAvailability) {
    	super(skuCode, version);
		this.materialNumbers = materialNumbers;
		this.inventory = inventory;

		this.groups = groups;
		this.upc = upc;
		_setPlantMaterialInfo(plantMaterialInfo);
        this.zonePriceInfoList = zonePriceInfoList;
        this.materialAvailability=materialAvailability;

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

	/**
	 * Get inventory (short term availability) information.
	 */
	public ErpInventoryModel getInventory(String plantId) {

		if(null ==materialNumbers || materialNumbers.length == 0){//To prevent NPEs
			return null;
		}
		if(this.inventory != null){

			return this.inventory.getInventory(materialNumbers[0]);
		}
		ErpInventoryModel inventoryModel= FDInventoryCache.getInstance().getInventory(materialNumbers[0]);
		if(StringUtil.isEmpty(plantId)||inventoryModel==null) {
			return inventoryModel;
		}
		List<ErpInventoryEntryModel> invEntries=inventoryModel.getEntries();
		List<ErpInventoryEntryModel> plantEntries=new ArrayList<ErpInventoryEntryModel>(invEntries.size());
		for(ErpInventoryEntryModel entry:invEntries) {
			if(plantId.equals(entry.getPlantId()))
				plantEntries.add(entry);
		}


		return new ErpInventoryModel(inventoryModel.getSapId(),inventoryModel.getLastUpdated(),plantEntries);
	}


	public EnumATPRule getATPRule(String plantID) {

		if(!StringUtils.isEmpty(plantID)) {

			FDPlantMaterial pm=plantMaterialInfo.get(plantID);
			if(pm!=null)
			     return pm.getAtpRule();
		}
		//throw new FDRuntimeException("No plant ("+plantID+") specific information set up for SKU: "+getSkuCode());
		return null;

	}

    /**
	 * Get the SAP availability status of this product.
	 *
	 * @return true if product is discontinued
	 */
	public EnumAvailabilityStatus getAvailabilityStatus(String salesOrg, String distributionChannel) {
		FDMaterialSalesArea sa= this.materialAvailability.get(new String(salesOrg+distributionChannel).intern());
		if(sa!=null)
			return EnumAvailabilityStatus.getEnumByStatusCode( sa.getUnavailabilityStatus());
		return EnumAvailabilityStatus.TEMP_UNAV;//::FDX::-> Handle this in a better way.
	}

    /**
	 * Get the SAP availability date of this product.
	 * This attribute is not used.
	 *
	 * @return the availability date, as returned by SAP.
	 */
	public Date getAvailabilityDate(String salesOrg, String distributionChannel) {
		FDMaterialSalesArea sa= this.materialAvailability.get(new String(salesOrg+distributionChannel).intern());
		if(sa!=null)
			return sa.getUnavailabilityDate();
		return null;//::FDX::-> Handle this in a better way.
	}

    public boolean isAvailable(String salesOrg, String distributionChannel) {
    	FDMaterialSalesArea sa= this.materialAvailability.get(new String(salesOrg+distributionChannel).intern());
		if(sa!=null)
			return EnumAvailabilityStatus.AVAILABLE.equals(EnumAvailabilityStatus.getEnumByStatusCode( sa.getUnavailabilityStatus()));
		return false;//::FDX::-> Handle this in a better way.
    }

    public boolean isDiscontinued(String salesOrg, String distributionChannel) {
    	FDMaterialSalesArea sa= this.materialAvailability.get(new String(salesOrg+distributionChannel).intern());
		if(sa!=null)
			return EnumAvailabilityStatus.DISCONTINUED.equals(EnumAvailabilityStatus.getEnumByStatusCode( sa.getUnavailabilityStatus()))
					|| "TEST".equalsIgnoreCase(sa.getUnavailabilityStatus());
		return false;//::FDX::-> Handle this in a better way.
    }

    public boolean isOutOfSeason(String salesOrg, String distributionChannel) {
    	FDMaterialSalesArea sa= this.materialAvailability.get(new String(salesOrg+distributionChannel).intern());
		if(sa!=null)
			return EnumAvailabilityStatus.OUT_OF_SEASON.equals(EnumAvailabilityStatus.getEnumByStatusCode( sa.getUnavailabilityStatus()));
		return false;//::FDX::-> Handle this in a better way.
    }

    public boolean isTempUnavailable(String salesOrg, String distributionChannel) {
    	FDMaterialSalesArea sa= this.materialAvailability.get(new String(salesOrg+distributionChannel).intern());
		if(sa!=null)
			return EnumAvailabilityStatus.TEMP_UNAV.equals(EnumAvailabilityStatus.getEnumByStatusCode( sa.getUnavailabilityStatus()));
		return true;//::FDX::-> Handle this in a better way.
    }

    public EnumOrderLineRating getRating(String plantID) {

		if(!StringUtils.isEmpty(plantID)) {
			FDPlantMaterial pm=plantMaterialInfo.get(plantID);
			if(pm!=null)
				return pm.getRating();
		}
		return EnumOrderLineRating.NEVER_RATED;
		//throw new FDRuntimeException("No plant ("+plantID+") specific information setup for SKU: "+getSkuCode());
    }

	/**
	 * Get the unit of measure for the "default" price.
	 *
	 * @return unit of measure
	 */
	public String getDefaultPriceUnit() {

		return this.zonePriceInfoList.getZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO).getDefaultPriceUnit();
	}

	/**
	 * Get the unit of measure for the "default" price.
	 *
	 * @return pricing unit attribute if one, else return the default Price unit
	 */
	public String getDisplayableDefaultPriceUnit() {

		return this.zonePriceInfoList.getZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO).getDisplayableDefaultPriceUnit();
	}

    public String getFreshness(String plantID) {
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
                    	if(!StringUtils.isEmpty(plantID)) {
                			FDPlantMaterial pm=plantMaterialInfo.get(plantID);
                			if(pm!=null){
                				freshness = pm.getFreshness();
                			}
                    	}
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
                + (this.materialNumbers != null ? StringUtil.encodeString(this.materialNumbers) : null)
                + "\n\tUPC:" + this.upc
                + "\n\tfreshness:" + this.freshness + "\n\tdefaultPriceUnit:" + this.getDefaultPriceUnit()
                + "\n\t" + this.zonePriceInfoList +"\n\t"+this.groups + "\n\tinventory:" +this.getInventory()+ "\n]";
    }
    	public List<String> getCountryOfOrigin(String plantID) {
		if(materialNumbers==null)
			return null;
		return FDCOOLInfoCache.getInstance().getCOOLInfo(materialNumbers[0],plantID);
	}

    	public ZonePriceInfoModel getZonePriceInfo(ZoneInfo pricingZoneInfo) {

    		ZoneInfo zone=pricingZoneInfo;
        		ZonePriceInfoModel zpModel=_getZonePriceInfo(zone);
        		while(zpModel==null && zone.hasParentZone()) {
        			zone=zone.getParentZone();
        			zpModel=_getZonePriceInfo(zone);
        		}
        		if(zpModel==null) {
        			LOGGER.debug("No price setup for zone: "+zone+" and sku=>"+this.getSkuCode());
        		}

        		else if(!pricingZoneInfo.getSalesOrg().equals(zpModel.getZoneInfo().getSalesOrg()) && ZoneInfo.PricingIndicator.BASE.equals(pricingZoneInfo.getPricingIndicator())) {

        			zpModel= new ZonePriceInfoModel(zpModel.getSellingPrice(), 0, zpModel.getDefaultPriceUnit(), zpModel.getDisplayableDefaultPriceUnit(),false,0,0,zpModel.getZoneInfo(),false);
        		}
        		return zpModel;
        }
    	private ZonePriceInfoModel _getZonePriceInfo(ZoneInfo zoneInfo) {
    		if (zoneInfo==null)
    				return null;

    		try {
    			ZonePriceInfoModel zpInfo = this.zonePriceInfoList.getZonePriceInfo(zoneInfo.hasParentZone()?new ZoneInfo(zoneInfo.getPricingZoneId(),zoneInfo.getSalesOrg(),zoneInfo.getDistributionChanel()):zoneInfo);
    			if(zpInfo == null) {
    				//do a item cascading to its parent until we find a price info.
    				ErpZoneMasterInfo _zoneInfo = FDCachedFactory.getZoneInfo(zoneInfo.getPricingZoneId());
    				if(null !=_zoneInfo.getParentZone()){
    					zpInfo = _getZonePriceInfo(new ZoneInfo(_zoneInfo.getParentZone().getSapId(),zoneInfo.getSalesOrg(),zoneInfo.getDistributionChanel()));
    				} else {
    					//go to default sales area now?
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

	//public FDProductInfo copy(int version, EnumAvailabilityStatus availability, EnumOrderLineRating newRating, String newFreshness) {
	    //return new FDProductInfo (getSkuCode(), version, materialNumbers, availability, new Date(), inventory, newRating, newFreshness, zonePriceInfoList.clone(), group,  upc, availabilityDates);
		/*return new FDProductInfo(getSkuCode(), version, materialNumbers,null,
				FDInventoryCacheI inventory, Date[] availabilityDates,
				FDGroup group, String upc, List<FDPlantMaterial> plantMaterialInfo,ZonePriceInfoListing zonePriceInfoList)
		*/

	//}

	public  Map<String,FDGroup> getGroups() {
		return groups;
	}

	public FDGroup getGroup(String salesOrg, String distributionChannel) {
		if(groups==null)
			return null;

		if(StringUtils.isEmpty(salesOrg)|| StringUtils.isEmpty(distributionChannel))
			return null;
		return groups.get(salesOrg+"-"+distributionChannel);
	}
	public boolean isGroupExists(String salesOrg, String distributionChannel) {

		if(StringUtils.isEmpty(salesOrg)|| StringUtils.isEmpty(distributionChannel))
			return false;

		if(groups == null)
			return false;

		return groups.containsKey(salesOrg+"-"+distributionChannel);
	}
	/** Getter for property sustainabilityRating.
     * @return Value of property sustainabilityRating.
     */
    public EnumSustainabilityRating getSustainabilityRating(String plantID) {

    	if(!StringUtils.isEmpty(plantID)) {

			FDPlantMaterial pm=plantMaterialInfo.get(plantID);
			if(pm!=null)
			     return pm.getSustainabilityRating();
		}
    	return EnumSustainabilityRating.NEVER_RATED;
		//throw new FDRuntimeException("No plant ("+plantID+") specific information set up for SKU: "+getSkuCode());
    }

    public String getUpc() {
		return upc;
	}

	public FDCouponInfo getCoupon() {
		return FDCouponFactory.getInstance().getCouponByUpc(upc);
	}

	public String getFamilyID() {
		// TODO Auto-generated method stub
		return familyID;
	}
	public String[] getMaterialIds() {
		// TODO Auto-generated method stub
		return materialNumbers;
	}

	/**
	 * @return the plantMaterialInfo
	 */
	public Map<String,FDPlantMaterial> getPlantMaterialInfo() {
		return plantMaterialInfo;
	}

	/**
	 * @param plantMaterialInfo the plantMaterialInfo to set
	 */
	public void setPlantMaterialInfo(List<FDPlantMaterial> plantMaterialInfo) {
		_setPlantMaterialInfo(plantMaterialInfo);
	}

	private void _setPlantMaterialInfo(List<FDPlantMaterial> plantMaterialInfo) {
		if(plantMaterialInfo!=null && !plantMaterialInfo.isEmpty()) {
			for(FDPlantMaterial pm: plantMaterialInfo) {
				this.plantMaterialInfo.put(pm.getPlantId(), pm);

			}
		}
	}

	public FDMaterialSalesArea getFDMaterialSalesArea(SalesAreaInfo salesArea) {
		return materialAvailability.get(salesArea);
	}

	public Map<String, FDMaterialSalesArea> getAvailability() {
		return materialAvailability;
	}

	//TODO: Remove this method.
	public boolean isLimitedQuantity(String salesOrg, String distributionChannel) {
		FDMaterialSalesArea sa= this.materialAvailability.get(new String(salesOrg+distributionChannel).intern());
		if(sa!=null) {
			if(this.getSkuCode().startsWith("VI"))
				return true;
		}
		return false;

	}

	public boolean isLimitedQuantity(String plantID) {
		if(!StringUtils.isEmpty(plantID)) {
			FDPlantMaterial pm=plantMaterialInfo.get(plantID);
			if(pm!=null)
				return pm.isLimitedQuantity();
		}
		return false;

	}

	public EnumDayPartValueType getDayPartValueType(String salesOrg, String distributionChannel) {
		FDMaterialSalesArea sa= this.materialAvailability.get(new String(salesOrg+distributionChannel).intern());
		if(sa!=null)
			return sa.getDayPartValueType();
		return null;
	}
}
