package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.tagwrapper.DeliveryTimeSlotTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ReserveTimeslotControllerTagWrapper;
import com.freshdirect.webapp.taglib.fdstore.Result;

public class ShipToAddress extends DeliveryAddress {

    private AltAddressType altType;

    public AltAddressType getAltType() {
        return altType;
    }

    /**
     * Filters list of addresses based on specified type
     * 
     * @param addresses
     * @param wantType
     * @return
     */
    public static List<ShipToAddress> filter(List<ShipToAddress> addresses, DeliveryAddressType wantType) {
        List<ShipToAddress> newList = new ArrayList<ShipToAddress>();
        for (ShipToAddress address : addresses) {
            if (wantType.equals(address.getType())) {
                newList.add(address);
            }
        }
        return newList;
    }

    public DeliveryAddressType getType() {
        return type;
    }

    private DeliveryAddressType type;

    private ShipToAddress() {
    }

    /**
     * @author Rob
     *
     */
    //    public static class DeliveryTimeSlotResult {
    //        private List<Timeslot> timeslots;
    //
    //        private Map zones;
    //
    //        private List messages;
    //
    //        private boolean zoneCtActive; //zone chef table active
    //
    //        public DeliveryTimeSlotResult(Result deliveryTimeSlotResult) {
    //            this.zones = deliveryTimeSlotResult.getZones();
    //            this.messages = deliveryTimeSlotResult.getMessages();
    //            this.zoneCtActive = deliveryTimeSlotResult.isZoneCtActive();
    //            this.timeslots = Timeslot.wrap(deliveryTimeSlotResult.getTimeslots());
    //        }
    //    }

    /**
     * @param user
     * @return
     * @throws FDException
     */

    public static List<ShipToAddress> wrap(List<ErpAddressModel> addresses) {
        List<ShipToAddress> wrappedAddresses = new ArrayList<ShipToAddress>();
        for (ErpAddressModel address : addresses) {
            wrappedAddresses.add(wrap(address));
        }
        return wrappedAddresses;
    }

    public static ShipToAddress wrap(ErpAddressModel address) {
        ShipToAddress newInstance = new ShipToAddress();
        newInstance.address = address;
        if (EnumServiceType.CORPORATE.equals(address.getServiceType())) {
            newInstance.type = DeliveryAddressType.CORP;
        } else if (EnumServiceType.HOME.equals(address.getServiceType())) {
            newInstance.type = DeliveryAddressType.RESIDENTIAL;
        }

        if(null != address.getAltDelivery()) {
            if (AltAddressType.NEIGHBOR.getCode().equalsIgnoreCase(address.getAltDelivery().getDeliveryCode())) {
                newInstance.altType = AltAddressType.NEIGHBOR;
            } else if (AltAddressType.DOORMAN.getCode().equalsIgnoreCase(address.getAltDelivery().getDeliveryCode())) {
                newInstance.altType = AltAddressType.DOORMAN;
            } else if (AltAddressType.NONE.getCode().equalsIgnoreCase(address.getAltDelivery().getDeliveryCode())) {
                newInstance.altType = AltAddressType.NONE;
            }
        } else {
            newInstance.altType = AltAddressType.NONE;
        }

        return newInstance;
    }

    public String getInstructions() {
        return address.getInstructions();
    }

    public String getLabel() {
        if (DeliveryAddressType.CORP.equals(type)) {
            return address.getCompanyName();
        } else {
            return new StringBuilder(address.getFirstName()).append(" ").append(address.getLastName()).toString();
        }
    }

    /**
     * @see com.freshdirect.mobileapi.model.DeliveryAddress
     * @return
     * @throws FDResourceException
     * @throws FDInvalidAddressException
     */
    public ShipToAddress getAddressGeocode() throws FDResourceException, FDInvalidAddressException {
        return ShipToAddress.wrap((ErpAddressModel) getAddressGeocode(address));
    }

    /**
     * @return
     * @throws FDResourceException
     */
    public String getCounty() throws FDResourceException {
        return getCounty(address);
    }

    public String getId() {
        String id = null;
        if (null != address.getPK()) {
            id = address.getPK().getId();
        }
        return id;
    }

    public String getFirstName() {
        return address.getFirstName();
    }

    public String getLastName() {
        return address.getLastName();
    }

    public String getCompanyName() {
        return address.getCompanyName();
    }

    public PhoneNumber getContactPhone() {
        return address.getPhone();
    }

    public PhoneNumber getAltContactPhone() {
        return address.getAltContactPhone();
    }

    public String getStreet1() {
        return address.getAddress1();
    }

    public String getApartment() {
        return address.getApartment();
    }

    public String getStreet2() {
        return address.getAddress2();
    }

    public String getCity() {
        return address.getCity();
    }

    public String getState() {
        return address.getState();
    }

    public String getPostalCode() {
        return address.getZipCode();
    }

    public String getAltDeliveryName() {
        String altDeliveryName = null;
        if (null != address.getAltDelivery()) {
            altDeliveryName = address.getAltDelivery().getName();
        }
        return altDeliveryName;
    }

    public String getAltDeliveryFirstName() {
        return address.getAltFirstName();
    }

    public String getAltDeliveryLastName() {
        return address.getAltLastName();
    }

    public PhoneNumber getAltDeliveryPhone() {
        return address.getAltPhone();
    }

    public String getAltDeliveryApt() {
        return address.getAltApartment();
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        address.setAddressInfo(addressInfo);
    }

    public void setInstructions(String instructions) {
        address.setInstructions(instructions);
    }

    public AddressInfo getAddressInfo() {
        return address.getAddressInfo();
    }

    //    public Object getUnattendedDeliveryFlag() {
    //        return address.getUnattendedDeliveryFlag();
    //    }

    public String getUnattendedDeliveryInstructions() {
        return address.getUnattendedDeliveryInstructions();
    }

    public void setUnattendedDeliveryInstructions(String code) {
        address.setUnattendedDeliveryInstructions(code);
    }

    public static AddressModel setZoningInfoOnAddress(DlvZoneInfoModel dlvResponse, AddressModel address) {
        AddressInfo info = address.getAddressInfo();
        if (info == null) {
            info = new AddressInfo();
        }
        info.setZoneId(dlvResponse.getZoneId());
        info.setZoneCode(dlvResponse.getZoneCode());
        address.setAddressInfo(info);
        return address;
    }

    /*
     * Friendly methods.  Available for other models but not outside.
     * This approach avoid leaking FD logic classes outside packages, where they shouldn't be accessible
     */

    void setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag flag) {
        address.setUnattendedDeliveryFlag(flag);
    }

    void setAltDelivery(EnumDeliverySetting doorman) {
        address.setAltDelivery(doorman);
    }

    static EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) throws FDResourceException {
        return FDDeliveryManager.getInstance().checkAddressForRestrictions(address);
    }

    ErpAddressModel getAddress() {
        return address;
    }

    EnumUnattendedDeliveryFlag getUnattendedDeliveryFlag() {
        return address.getUnattendedDeliveryFlag();
    }

    PrimaryKey getPK() {
        return address.getPK();
    }



}
