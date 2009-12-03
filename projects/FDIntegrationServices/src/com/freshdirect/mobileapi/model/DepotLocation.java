package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUser;

public class DepotLocation extends DeliveryAddress {

    private DlvLocationModel location;

    private DeliveryAddressType type;

    private DepotLocation() {
    }

    public static List<DepotLocation> wrap(List<DlvLocationModel> locations, boolean isPickup) {
        List<DepotLocation> newList = new ArrayList<DepotLocation>();
        for (DlvLocationModel location : locations) {
            newList.add(DepotLocation.wrap(location, isPickup));
        }
        return newList;
    }

    public static DepotLocation wrap(DlvLocationModel location, boolean isPickup) {
        DepotLocation newInstance = new DepotLocation();
        newInstance.location = location;
        if (isPickup) {
            newInstance.type = DeliveryAddressType.PICKUP;
        } else {
            newInstance.type = DeliveryAddressType.DEPOT;
        }
        return newInstance;
    }

    public boolean isValid() {
        Date now = new Date();
        boolean valid = false;
        if (now.after(location.getStartDate()) && now.before(location.getEndDate())) {
            valid = true;
        }
        return valid;
    }

    public String getId() {
        return location.getId();
    }

    public DeliveryAddressType getType() {
        return type;
    }

    public String getInstructions() {
        return location.getInstructions();
    }

    public String getFacility() {
        return location.getFacility();
    }

    public String getStreet1() {
        return location.getAddress().getAddress1();
    }

    public String getStreet2() {
        return location.getAddress().getAddress2();
    }

    public String getCity() {
        return location.getAddress().getCity();
    }

    public String getState() {
        return location.getAddress().getState();
    }

    public String getPostalCode() {
        return location.getAddress().getZipCode();
    }

    protected AddressModel getAddress() {
        return location.getAddress();
    }

    //Friendly 
    /**
     * Create depot address to be used during checkout
     * 
     * @param depotAddress
     * @param depot
     * @param user
     * @param contactPhone
     * @return
     * @throws FDResourceException
     */
    ErpDepotAddressModel createDepotAddress(AddressModel depotAddress, DlvDepotModel depot, FDUser user, PhoneNumber contactPhone)
            throws FDResourceException {
        ErpDepotAddressModel address = new ErpDepotAddressModel(depotAddress);
        address.setRegionId(depot.getRegionId());
        address.setZoneCode(location.getZoneCode());
        address.setLocationId(location.getPK().getId());
        address.setFacility(location.getFacility());
        if (user.isCorporateUser()) {
            //Dead code since "corpDlvInstructions" used in depot page and depot's been retired
            //String instructions = NVL.apply(request.getParameter("corpDlvInstructions"), "");
            //address.setInstructions(instructions);
        } else {
            address.setInstructions(location.getInstructions());
        }
        address.setPickup(depot.isPickup());
        address.setDeliveryChargeWaived(location.getDeliveryChargeWaived());

        ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(user.getIdentity().getErpCustomerPK());
        address.setFirstName(erpCustomer.getCustomerInfo().getFirstName());
        address.setLastName(erpCustomer.getCustomerInfo().getLastName());
        if (contactPhone != null) {
            address.setPhone(contactPhone);
        } else {
            address.setPhone(erpCustomer.getCustomerInfo().getBusinessPhone());
        }
        return address;

    }

}
