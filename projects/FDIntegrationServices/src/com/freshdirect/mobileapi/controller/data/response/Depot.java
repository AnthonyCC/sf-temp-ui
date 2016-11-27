package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

/**
 * @author Rob
 *
 */
public class Depot {

    private String code;

    private List<DepotLocation> depotLocations;

    /**
     * @param depot
     */
    public Depot(com.freshdirect.mobileapi.model.Depot depot) {
        code = depot.getDepotCode();
        depotLocations = DepotLocation.wrap(depot.getDepotLocations());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DepotLocation> getDepotLocations() {
        return depotLocations;
    }

    public void setDepotLocations(List<DepotLocation> depotLocations) {
        this.depotLocations = depotLocations;
    }

}
