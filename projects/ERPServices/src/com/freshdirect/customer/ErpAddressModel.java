/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import com.freshdirect.common.address.*;
import com.freshdirect.common.customer.EnumWebServiceType;

/**
 * ErpAddress model class.
 * 
 * @version $Revision$
 * @author $Author$
 * @stereotype fd-model
 */
public class ErpAddressModel extends ContactAddressModel {

	private static final long serialVersionUID = 3013178029862794550L;

	private String instructions;

	private EnumDeliverySetting altDeliverySetting;

	private String altFirstName;

	private String altLastName;

	private String altApartment;

	private PhoneNumber altPhone;

	private PhoneNumber altContactPhone;

	private EnumUnattendedDeliveryFlag unattendedDeliveryFlag;

	private String unattendedDeliveryInstructions;
	
	//For Robin Hood - Donation.
	private String charityName;
	
	private String companyName;
	
	private boolean optInForDonation;

	//This field is used for storing specific service type when service type is WEB.
	private EnumWebServiceType webServiceType;
	/**
	 * Default constructor.
	 */
	public ErpAddressModel() {
		this.instructions = "";

		this.altDeliverySetting = EnumDeliverySetting.NONE;
		this.altFirstName = "";
		this.altLastName = "";
		this.altApartment = "";
		this.altPhone = new PhoneNumber("");

		this.unattendedDeliveryFlag = EnumUnattendedDeliveryFlag.NOT_SEEN;
		this.unattendedDeliveryInstructions = "";
		
	}

	public ErpAddressModel(BasicAddressI address) {
		this.setFrom(address);
	}

	public ErpAddressModel(BasicContactAddressI address) {
		this.setFrom(address);
	}

	public String getInstructions() {
		return this.instructions;
	}

	public void setInstructions(String s) {
		this.instructions = s;
	}

	public EnumDeliverySetting getAltDelivery() {
		return this.altDeliverySetting;
	}

	public void setAltDelivery(EnumDeliverySetting altDelSet) {
		this.altDeliverySetting = altDelSet;
	}

	public String getAltFirstName() {
		return altFirstName;
	}

	public void setAltFirstName(String altfname) {
		this.altFirstName = altfname;
	}

	public String getAltLastName() {
		return altLastName;
	}

	public void setAltLastName(String altlname) {
		this.altLastName = altlname;
	}

	public String getAltApartment() {
		return altApartment;
	}

	public void setAltApartment(String altapartment) {
		this.altApartment = altapartment;
	}

	public PhoneNumber getAltPhone() {
		return this.altPhone;
	}

	public void setAltPhone(PhoneNumber ap) {
		this.altPhone = ap;
	}

	public void setAltContactPhone(PhoneNumber altContactPhone) {
		this.altContactPhone = altContactPhone;
	}

	public PhoneNumber getAltContactPhone() {
		return this.altContactPhone;
	}

	public void setUnattendedDeliveryFlag(
			EnumUnattendedDeliveryFlag unattendedDeliveryFlag) {
		this.unattendedDeliveryFlag = unattendedDeliveryFlag;
	}

	public EnumUnattendedDeliveryFlag getUnattendedDeliveryFlag() {
		return this.unattendedDeliveryFlag;
	}

	public void setUnattendedDeliveryInstructions(
			String unattendedDeliveryInstructions) {
		this.unattendedDeliveryInstructions = unattendedDeliveryInstructions;
	}

	public String getUnattendedDeliveryInstructions() {
		return this.unattendedDeliveryInstructions;
	}

	public void setFrom(ErpAddressModel erpAddress) {
		super.setFrom(erpAddress);
		this.setCompanyName(erpAddress.getCompanyName());
		this.setServiceType(erpAddress.getServiceType());
		this.setInstructions(erpAddress.getInstructions());
		this.setAddressInfo(erpAddress.getAddressInfo());
		this.setAltDelivery(erpAddress.getAltDelivery());
		this.setAltFirstName(erpAddress.getAltFirstName());
		this.setAltLastName(erpAddress.getAltLastName());
		this.setAltApartment(erpAddress.getAltApartment());
		this.setAltPhone(erpAddress.getAltPhone());
		this.altContactPhone = erpAddress.getAltContactPhone();

		this.setUnattendedDeliveryFlag(erpAddress.getUnattendedDeliveryFlag());
		this.setUnattendedDeliveryInstructions(erpAddress
				.getUnattendedDeliveryInstructions());
	}

	public BasicContactAddressI getAlternateAddress() {
		if (EnumDeliverySetting.NONE.equals(this.altDeliverySetting)) {
			// no alternate dlv address
			return null;
		}

		if (EnumDeliverySetting.DOORMAN.equals(this.altDeliverySetting)) {
			return new ContactAddressAdapter(this, "", "DOORMAN",this.getCustomerId(), this
					.getPhone());
		}

		// deliver to neighbor
		return new BasicContactAddressI() {
			public String getFirstName() {
				return ErpAddressModel.this.getAltFirstName();
			}

			public String getLastName() {
				return ErpAddressModel.this.getAltLastName();
			}

			public String getApartment() {
				return ErpAddressModel.this.getAltApartment();
			}

			public PhoneNumber getPhone() {
				return ErpAddressModel.this.getAltPhone();
			}

			public String getAddress1() {
				return ErpAddressModel.this.getAddress1();
			}

			public String getAddress2() {
				return ErpAddressModel.this.getAddress2();
			}

			public String getCity() {
				return ErpAddressModel.this.getCity();
			}

			public String getZipCode() {
				return ErpAddressModel.this.getZipCode();
			}

			public String getState() {
				return ErpAddressModel.this.getState();
			}

			public String getCountry() {
				return ErpAddressModel.this.getCountry();
			}

			// changes done for GeoCode 
			public AddressInfo getAddressInfo() {
				// TODO Auto-generated method stub
				return ErpAddressModel.this.getAddressInfo();
			}

			@Override
			public String getCustomerId() {
				return ErpAddressModel.this.getCustomerId();
			}
		};

	}

	/**
	 * @return the charityName
	 */
	public String getCharityName() {
		return charityName;
	}

	/**
	 * @param charityName the charityName to set
	 */
	public void setCharityName(String charityName) {
		this.charityName = charityName;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the optInStatus
	 */
	public boolean isOptInForDonation() {
		return optInForDonation;
	}

	/**
	 * @param optInStatus the optInStatus to set
	 */
	public void setOptInForDonation(boolean optInForDonation) {
		this.optInForDonation = optInForDonation;
	}

	public EnumWebServiceType getWebServiceType() {
		return webServiceType;
	}

	public void setWebServiceType(EnumWebServiceType webServiceType) {
		this.webServiceType = webServiceType;
	}

}
