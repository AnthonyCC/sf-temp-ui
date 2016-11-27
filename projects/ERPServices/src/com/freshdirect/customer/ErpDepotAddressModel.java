package com.freshdirect.customer;

/**
 * 
 * @author knadeem
 */
import com.freshdirect.common.address.BasicAddressI;
import com.freshdirect.common.address.BasicContactAddressI;
import com.freshdirect.common.customer.EnumServiceType;

public class ErpDepotAddressModel extends ErpAddressModel {
	
	private static final long	serialVersionUID	= 4364276404888344963L;
	
	private String regionId;
	private String zoneCode;
	private String locationId;
    private String facility;
    private boolean pickup = false;
    private boolean deliveryChargeWaived = false;
	
	/**
	 * Default constructor.
	 */
	public ErpDepotAddressModel() {
		super();
	}

	public ErpDepotAddressModel(BasicAddressI address) {
		super(address);
	}

	public ErpDepotAddressModel(BasicContactAddressI address) {
		super(address);
	}
	
	public String getRegionId(){
		return this.regionId;
	}
	
	public void setRegionId(String regionId){
		this.regionId = regionId;
	}
	
	public String getZoneCode(){
		return this.zoneCode;
	}
	
	public void setZoneCode(String zoneCode){
		this.zoneCode = zoneCode;
	}
	
	public String getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(String locationId){
		this.locationId = locationId;
	}
    
    public String getFacility(){
		return this.facility;
	}
    
	public void setFacility(String facility){
		this.facility = facility;	
	}
    
    public boolean isPickup() {
        return this.pickup;
    }
    
    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

	public boolean isDeliveryChargeWaived() {
		return deliveryChargeWaived;
	}

	public void setDeliveryChargeWaived(boolean deliveryChargeWaived) {
		this.deliveryChargeWaived = deliveryChargeWaived;
	}

	public EnumServiceType getServiceType() {
		return pickup ? EnumServiceType.PICKUP : EnumServiceType.DEPOT;
	}
	public String getId() {
		return getLocationId();
	}
}
