package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

public class DepotLocation extends DeliveryAddress {

    public DepotLocation(com.freshdirect.mobileapi.model.DepotLocation depotLocation) {
        this.name = depotLocation.getFacility();
        this.city = depotLocation.getCity();
        this.id = depotLocation.getId();
        this.instructions = depotLocation.getInstructions();
        this.postalCode = depotLocation.getPostalCode();

        this.state = depotLocation.getState();
        this.street1 = depotLocation.getStreet1();
        this.street2 = depotLocation.getStreet2();
        this.type = depotLocation.getType().toString();
    }

    public static List<DepotLocation> wrap(List<com.freshdirect.mobileapi.model.DepotLocation> depotLocations) {
        List<DepotLocation> locations = new ArrayList<DepotLocation>();
        for (com.freshdirect.mobileapi.model.DepotLocation depotLocation : depotLocations) {
            locations.add(new DepotLocation(depotLocation));
        }
        return locations;
    }

}
