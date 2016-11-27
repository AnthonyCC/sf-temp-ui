package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rob
 *
 */
public class DeliveryAddresses extends CheckoutResponse {

    private String preSelectedId;

    private double residentialDeliveryMinimum = 0;

    private List<ShipToAddress> residentialAddresses = new ArrayList<ShipToAddress>();

    private double corporateDeliveryMinimum = 0;

    private List<ShipToAddress> corporateAddresses = new ArrayList<ShipToAddress>();

    private double depotDeliveryMinimum = 0;

    public double getResidentialDeliveryMinimum() {
        return residentialDeliveryMinimum;
    }

    public void setResidentialDeliveryMinimum(double residentialDeliveryMinimum) {
        this.residentialDeliveryMinimum = residentialDeliveryMinimum;
    }

    public double getCorporateDeliveryMinimum() {
        return corporateDeliveryMinimum;
    }

    public void setCorporateDeliveryMinimum(double corporateDeliveryMinimum) {
        this.corporateDeliveryMinimum = corporateDeliveryMinimum;
    }

    public double getDepotDeliveryMinimum() {
        return depotDeliveryMinimum;
    }

    public void setDepotDeliveryMinimum(double depotDeliveryMinimum) {
        this.depotDeliveryMinimum = depotDeliveryMinimum;
    }

    private List<Depot> depot = new ArrayList<Depot>();

    /**
     * 
     */
    public DeliveryAddresses() {
    }

    /**
     * @param preSelectedId
     * @param residentialAddresses
     * @param corporateAddresses
     * @param depotAddresses
     */
    public DeliveryAddresses(String preSelectedId, List<com.freshdirect.mobileapi.model.ShipToAddress> residentialAddresses,
            List<com.freshdirect.mobileapi.model.ShipToAddress> corporateAddresses,
            List<com.freshdirect.mobileapi.model.Depot> depotAddresses) {

        this.preSelectedId = preSelectedId;

        if (null != residentialAddresses) {
            for (com.freshdirect.mobileapi.model.ShipToAddress residentialAddress : residentialAddresses) {
                this.residentialAddresses.add(new ShipToAddress(residentialAddress));
            }
        }

        if (null != corporateAddresses) {
            for (com.freshdirect.mobileapi.model.ShipToAddress corporateAddress : corporateAddresses) {
                this.corporateAddresses.add(new ShipToAddress(corporateAddress));
            }
        }

        if (null != depotAddresses) {
            for (com.freshdirect.mobileapi.model.Depot depotAddress : depotAddresses) {
                this.depot.add(new Depot(depotAddress));
            }
        }

    }

    public List<ShipToAddress> getResidentialAddresses() {
        return residentialAddresses;
    }

    public void setResidentialAddresses(List<ShipToAddress> residentialAddresses) {
        this.residentialAddresses = residentialAddresses;
    }

    public List<ShipToAddress> getCorporateAddresses() {
        return corporateAddresses;
    }

    public void setCorporateAddresses(List<ShipToAddress> corporateAddresses) {
        this.corporateAddresses = corporateAddresses;
    }

    public List<Depot> getDepot() {
        return depot;
    }

    public void setDepot(List<Depot> depot) {
        this.depot = depot;
    }

    public String getPreSelectedId() {
        return preSelectedId;
    }

    public void setPreSelectedId(String preSelectedId) {
        this.preSelectedId = preSelectedId;
    }

}
