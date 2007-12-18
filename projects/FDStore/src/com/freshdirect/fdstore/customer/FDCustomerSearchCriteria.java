package com.freshdirect.fdstore.customer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;

public class FDCustomerSearchCriteria extends FDSearchCriteria {

	private String depotCode = null;
	private String address = null;
	private String apartment = null;
	private String zipCode = null;

	public String getDepotCode() {
		return this.depotCode;
	}

	public void setDepotCode(String depotCode) {
		if (depotCode != null && !"".equals(depotCode)) {
			this.depotCode = depotCode;
		}
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		if (address != null && !"".equals(address)) {
			this.address = address;
		}
	}

	public String getApartment() {
		return this.apartment;
	}

	public void setApartment(String apartment) {
		if (apartment != null && !"".equals(apartment)) {
			this.apartment = apartment;
		}
	}
	
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		if (zipCode != null && !"".equals(zipCode.trim())) {
			this.zipCode = zipCode;
		}
	}

	public String getCriteria() throws FDResourceException {
		StringBuffer buf = new StringBuffer(super.getCriteria());
		
		for(Iterator i = this.getCriteriaMap().entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Entry) i.next();
			buf.append(" ").append(e.getKey()).append(": ").append(e.getValue());
		}
		
		return buf.toString().trim();
	}
	
	public Map getCriteriaMap() throws FDResourceException {
		Map m = super.getCriteriaMap();
		
		if (this.address != null && !"".equals(this.address.trim())) {
			m.put("Address", this.address);
		}
		if (this.apartment != null && !"".equals(this.apartment.trim())) {
			m.put("Apartment", this.apartment);
		}
		
		if (this.zipCode != null && !"".equals(this.zipCode.trim())) {
			m.put("Zipcode", this.zipCode);
		}
		
		if (this.depotCode != null && !"".equals(this.depotCode.trim())) {
			if (depotCode != null && !depotCode.equals("")) {
				FDDepotManager dmgr = FDDepotManager.getInstance();
				for (Iterator dIter = dmgr.getDepots().iterator(); dIter.hasNext();) {
					DlvDepotModel dm = (DlvDepotModel) dIter.next();
					if (depotCode.equals(dm.getDepotCode())) {
						m.put("Depot", dm.getName());
						break;
					}
				}
			}
		}
		
		return m;
	}


	public String toString() {

		StringBuffer buf = new StringBuffer();
		buf.append("[FDSearchCriteria First Name: ").append(this.firstName);
		buf.append(" Last Name: ").append(this.lastName);
		buf.append(" Emial: ").append(this.email);
		buf.append(" Phone: ").append(this.phone);
		buf.append(" Order#: ").append(this.orderNumber);
		buf.append(" Address: ").append(this.address);
		buf.append(" Apartment: ").append(this.apartment);
		buf.append(" ZipCode: ").append(this.zipCode);
		buf.append(" DepotCode: ").append(this.depotCode).append("]");

		return buf.toString();
	}

	public boolean isBlank() {
		return super.isBlank() && this.address == null && this.apartment == null && this.zipCode == null && this.depotCode == null;
	}

}
