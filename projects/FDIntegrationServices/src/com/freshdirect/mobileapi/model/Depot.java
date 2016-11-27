package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.fdlogistics.model.FDDeliveryDepotModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;

public class Depot {

    private static final String HAMPTONS_DEPOT_CODE = "HAM";

    private FDDeliveryDepotModel depot;

    private List<DepotLocation> depotLocations = new ArrayList<DepotLocation>();

    private Depot() {
    }

    public String getDepotCode() {
        return depot.getDepotCode();
    }

    public static List<Depot> getPickupDepots() throws FDResourceException {

        boolean theHamptonsIsOn = FDStoreProperties.isSummerServiceEnabled();

        Collection<FDDeliveryDepotModel> pickupDepots = FDDeliveryManager.getInstance().getPickupDepots();
        List<Depot> newList = new ArrayList<Depot>();
        for (FDDeliveryDepotModel pickupDepot : pickupDepots) {
            //if ((!theHamptonsIsOn) && (HAMPTONS_DEPOT_CODE.equalsIgnoreCase(pickupDepot.getDepotCode()))) {
        	// Filtering Hamptons pickup forever use the previous if hamtopns pickup needs to be handled
        	if (HAMPTONS_DEPOT_CODE.equalsIgnoreCase(pickupDepot.getDepotCode()) || pickupDepot.isDeactivated()) {
                //Skip since HAMPTON is OFF
            } else {
                newList.add(Depot.wrap(pickupDepot));
            }
        }
        return newList;
    }

    public static Depot wrap(FDDeliveryDepotModel pickupDepot) {
        Depot newInstance = new Depot();
        newInstance.depot = pickupDepot;
        newInstance.depotLocations = DepotLocation.wrap(pickupDepot.getLocations(), pickupDepot.isPickup());
        return newInstance;
    }

    public List<DepotLocation> getDepotLocations() {
        return depotLocations;
    }

    public DepotLocation getDepotLocation(String locationId) {
        DepotLocation matchedLocation = null;
        for (DepotLocation location : depotLocations) {
            if (location.getId().equals(locationId)) {
                matchedLocation = location;
                break;
            }
        }
        return matchedLocation;
    }
}
