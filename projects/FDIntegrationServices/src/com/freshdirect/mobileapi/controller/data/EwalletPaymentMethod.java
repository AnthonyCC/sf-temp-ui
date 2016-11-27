package com.freshdirect.mobileapi.controller.data;


/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletPaymentMethod {

	 private String cardId;
	 private String preferredCard;
	 private String maskedAccountNumber;
	 private String cardType;
     private String name;
     private String expirationMonth;
     private String expirationYear;
	/**
	 * @return the cardId
	 */
	public String getCardId() {
		return cardId;
	}
	/**
	 * @param cardId the cardId to set
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	/**
	 * @return the preferredCard
	 */
	public String getPreferredCard() {
		return preferredCard;
	}
	/**
	 * @param preferredCard the preferredCard to set
	 */
	public void setPreferredCard(String preferredCard) {
		this.preferredCard = preferredCard;
	}
	/**
	 * @return the maskedAccountNumber
	 */
	public String getMaskedAccountNumber() {
		return maskedAccountNumber;
	}
	/**
	 * @param maskedAccountNumber the maskedAccountNumber to set
	 */
	public void setMaskedAccountNumber(String maskedAccountNumber) {
		this.maskedAccountNumber = maskedAccountNumber;
	}
	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}
	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the expirationMonth
	 */
	public String getExpirationMonth() {
		return expirationMonth;
	}
	/**
	 * @param expirationMonth the expirationMonth to set
	 */
	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	/**
	 * @return the expirationYear
	 */
	public String getExpirationYear() {
		return expirationYear;
	}
	/**
	 * @param expirationYear the expirationYear to set
	 */
	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}
     
     
}
