package com.freshdirect.webapp.ajax.expresscheckout.payment.data;

public class PaymentData {

	private String id;
	private boolean selected;
	private String title;
	private String type;
	private String nameOnCard;
	private String address1;
	private String address2;
	private String apartment;
	private String city;
	private String state;
	private String zip;
	private String bestNumber;
	private String expiration;
	private String accountNumber;
	private String bankAccountType;
	private String bankName;
	private String abaRouteNumber;
    private String referencedOrderId;
    private String paymentTypeDescription;
	private String eWalletID;
	private String vendorEWalletID;
	private String mpLogoURL;
	private String emailID;
	private boolean canDelete = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getBestNumber() {
		return bestNumber;
	}

	public void setBestNumber(String bestNumber) {
		this.bestNumber = bestNumber;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAbaRouteNumber() {
		return abaRouteNumber;
	}

	public void setAbaRouteNumber(String abaRouteNumber) {
		this.abaRouteNumber = abaRouteNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

    public String getReferencedOrderId() {
        return referencedOrderId;
    }

    public void setReferencedOrderId(String referencedOrderId) {
        this.referencedOrderId = referencedOrderId;
    }

    public String getPaymentTypeDescription() {
        return paymentTypeDescription;
    }

    public void setPaymentTypeDescription(String paymentTypeDescription) {
        this.paymentTypeDescription = paymentTypeDescription;
    }

    /**
     * @return the eWalletID
     */
	public String geteWalletID() {
		return eWalletID;
	}

	/**
	 * @param eWalletID the eWalletID to set
	 */
	public void seteWalletID(String eWalletID) {
		this.eWalletID = eWalletID;
	}

	/**
	 * @return the vendorEWalletID
	 */
	public String getVendorEWalletID() {
		return vendorEWalletID;
	}

	/**
	 * @param vendorEWalletID the vendorEWalletID to set
	 */
	public void setVendorEWalletID(String vendorEWalletID) {
		this.vendorEWalletID = vendorEWalletID;
	}

	/**
	 * @return the mpLogoURL
	 */
	public String getMpLogoURL() {
		return mpLogoURL;
	}

	/**
	 * @param mpLogoURL the mpLogoURL to set
	 */
	public void setMpLogoURL(String mpLogoURL) {
		this.mpLogoURL = mpLogoURL;
	}

	/**
	 * @return emailID
	 */
	public String getEmailID() {
		return emailID;
	}

	/**
	 * @param emailID
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	/**
	 * @return the canDelete
	 */
	public boolean isCanDelete() {
		return canDelete;
	}

	/**
	 * @param canDelete the canDelete to set
	 */
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	
}
