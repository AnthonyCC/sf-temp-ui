package com.freshdirect.fdlogistics.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.logistics.controller.data.ApartmentRange;
import com.freshdirect.logistics.controller.data.response.DeliveryZone;
import com.freshdirect.logistics.delivery.model.EnumAddressVerificationResult;

public class FDDeliveryAddressCheckResponse implements Serializable  {



	private AddressModel address;
    private List<AddressModel> suggestions;
    private List<FDDeliveryApartmentRange> aptRanges;
    private String deliveryStatus;
    private List<FDDeliveryZoneInfo> zoneInfo;
    private String county;
    private Set<EnumServiceType> availServices;
    private String addressOk;
    private String geocodeOk;
    private String geocodeResult;
	private EnumAddressVerificationResult verifyResult;

    
    public List<AddressModel> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<AddressModel> suggestions) {
        this.suggestions = suggestions;
    }

    public List<FDDeliveryApartmentRange> getAptRanges() {
        return aptRanges;
    }

    public void setAptRanges(List<FDDeliveryApartmentRange> aptRanges) {
        this.aptRanges = aptRanges;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public List<FDDeliveryZoneInfo> getZoneInfo() {
        return zoneInfo;
    }

    public void setZoneInfo(List<FDDeliveryZoneInfo> zoneInfo) {
        this.zoneInfo = zoneInfo;
    }

    public Set<EnumServiceType> getAvailServices() {
        return availServices;
    }

    public void setAvailServices(Set<EnumServiceType> availServices) {
        this.availServices = availServices;
    }

	public AddressModel getAddress() {
		return address;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
	}
	
	public String getAddressOk() {
		return addressOk;
	}

	public void setAddressOk(String addressOk) {
		this.addressOk = addressOk;
	}

	public String getGeocodeOk() {
		return geocodeOk;
	}

	public void setGeocodeOk(String geocodeOk) {
		this.geocodeOk = geocodeOk;
	}

	public String getGeocodeResult() {
		return geocodeResult;
	}

	public void setGeocodeResult(String geocodeResult) {
		this.geocodeResult = geocodeResult;
	}

	public EnumAddressVerificationResult getVerifyResult() {
		return verifyResult;
	}

	public void setVerifyResult(EnumAddressVerificationResult verifyResult) {
		this.verifyResult = verifyResult;
	}

}
