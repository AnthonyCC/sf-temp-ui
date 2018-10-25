package com.freshdirect.customer;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.BasicAddressI;
import com.freshdirect.common.address.BasicContactAddressI;
import com.freshdirect.common.address.ContactAddressAdapter;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.address.PhoneNumberDeserializer;
import com.freshdirect.common.customer.EnumWebServiceType;
import com.freshdirect.framework.util.StringUtil;

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
	@JsonDeserialize(using = PhoneNumberDeserializer.class)
	private PhoneNumber altPhone;
	@JsonDeserialize(using = PhoneNumberDeserializer.class)
	private PhoneNumber altContactPhone;

	private EnumUnattendedDeliveryFlag unattendedDeliveryFlag;

	private String unattendedDeliveryInstructions;
	
	//For Robin Hood - Donation.
	private String charityName;
	
	private boolean optInForDonation;

	//This field is used for storing specific service type when service type is WEB.
	private EnumWebServiceType webServiceType;
	
	//This is a non-persistent field.
	private boolean isEbtAccepted;
	
	private String scrubbedStreet;
	
	//COS17-76 2nd emil for COS users
		private boolean notifyOrderPlaceToSecondEmail;
		private boolean  notifyOrderModifyToSecondEmail;
		private boolean notifyOrderInvoiceToSecondEmail;
		private boolean notifySoReminderToSecondEmail;
		private boolean notifyCreditsSecondEmail;
		private boolean notifyVoiceshotToSecondEmail;
		
	public String getScrubbedStreet() {
		return scrubbedStreet;
	}

	public void setScrubbedStreet(String scrubbedStreet) {
		this.scrubbedStreet = scrubbedStreet;
	}

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

		this.unattendedDeliveryFlag = EnumUnattendedDeliveryFlag.OPT_IN;
		this.unattendedDeliveryInstructions = "OK";
		this.setAddressInfo(new AddressInfo());
		
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
		this.setScrubbedStreet((erpAddress.getAddressInfo()!=null)?erpAddress.getAddressInfo().getScrubbedStreet():null);
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
			
			private static final long	serialVersionUID	= 8009887805313335384L;

			public String getFirstName() {
				if(StringUtils.isNotBlank(ErpAddressModel.this.getAltFirstName()))
					return ErpAddressModel.this.getAltFirstName();
				else
					return ErpAddressModel.this.getFirstName();
			}

			public String getLastName() {
				if(StringUtils.isNotBlank(ErpAddressModel.this.getAltLastName()))
					return ErpAddressModel.this.getAltLastName();
				else
					return ErpAddressModel.this.getLastName();
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
	
	/**
	 * Returns a short String representation of this address.
	 * @param isCorporate If it's a corporate user.
	 * @return Short string representation of this address.
	 */
	public String toShortString1(boolean isCorporate) {
		StringBuilder sb = new StringBuilder(getAddress1());
		if(!StringUtil.isEmpty(getApartment())) {
			sb.append(isCorporate ? ", Floor/Suite " : ", Apt. ");
			sb.append(getApartment());
		}
		return sb.toString();
	}

	public boolean isEbtAccepted() {
		return isEbtAccepted;
	}

	public void setEbtAccepted(boolean isEbtAccepted) {
		this.isEbtAccepted = isEbtAccepted;
	}

	public EnumDeliverySetting getAltDeliverySetting() {
		return altDeliverySetting;
	}

	public void setAltDeliverySetting(EnumDeliverySetting altDeliverySetting) {
		this.altDeliverySetting = altDeliverySetting;
	}

	public boolean isNotifyOrderPlaceToSecondEmail() {
		return notifyOrderPlaceToSecondEmail;
	}

	public void setNotifyOrderPlaceToSecondEmail(boolean notifyOrderPlaceToSecondEmail) {
		this.notifyOrderPlaceToSecondEmail = notifyOrderPlaceToSecondEmail;
	}

	public boolean isNotifyOrderModifyToSecondEmail() {
		return notifyOrderModifyToSecondEmail;
	}

	public void setNotifyOrderModifyToSecondEmail(boolean notifyOrderModifyToSecondEmail) {
		this.notifyOrderModifyToSecondEmail = notifyOrderModifyToSecondEmail;
	}

	public boolean isNotifyOrderInvoiceToSecondEmail() {
		return notifyOrderInvoiceToSecondEmail;
	}

	public void setNotifyOrderInvoiceToSecondEmail(boolean notifyOrderInvoiceToSecondEmail) {
		this.notifyOrderInvoiceToSecondEmail = notifyOrderInvoiceToSecondEmail;
	}

	public boolean isNotifySoReminderToSecondEmail() {
		return notifySoReminderToSecondEmail;
	}

	public void setNotifySoReminderToSecondEmail(boolean notifySoReminderToSecondEmail) {
		this.notifySoReminderToSecondEmail = notifySoReminderToSecondEmail;
	}

	public boolean isNotifyCreditsToSecondEmail() {
		return notifyCreditsSecondEmail;
	}

	public void setNotifyCreditsToSecondEmail(boolean notifyCreditsSecondEmail) {
		this.notifyCreditsSecondEmail = notifyCreditsSecondEmail;
	}

	public boolean isNotifyVoiceshotToSecondEmail() {
		return notifyVoiceshotToSecondEmail;
	}

	public void setNotifyVoiceshotToSecondEmail(boolean notifyVoiceshotAltMail) {
		this.notifyVoiceshotToSecondEmail = notifyVoiceshotToSecondEmail;
	}

}
