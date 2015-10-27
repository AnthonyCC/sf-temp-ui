package com.freshdirect.webapp.ajax.expresscheckout.payment.data;

import java.util.List;

public class FormPaymentData {

	private String selected;
	private List<PaymentData> payments;
	private boolean coveredByGiftCard;
	private boolean backupPaymentRequiredForGiftCard;
//	private String mpEWalletID;
//	private String displayedCardType;
//	private String mpCardPaired;
	
	private boolean mpEwalletStatus;
	private String walletErrorMsg;
	private String mpButtonImgURL;
    private List<String> onOpenCoremetrics;

	public boolean isCoveredByGiftCard() {
		return coveredByGiftCard;
	}

	public void setCoveredByGiftCard(boolean coveredByGiftCard) {
		this.coveredByGiftCard = coveredByGiftCard;
	}

	public boolean isBackupPaymentRequiredForGiftCard() {
		return backupPaymentRequiredForGiftCard;
	}

	public void setBackupPaymentRequiredForGiftCard(boolean backupPaymentRequiredForGiftCard) {
		this.backupPaymentRequiredForGiftCard = backupPaymentRequiredForGiftCard;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public List<PaymentData> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentData> payments) {
		this.payments = payments;
	}

	/**
	 * @return the mpEwalletStatus
	 */
	public boolean isMpEwalletStatus() {
		return mpEwalletStatus;
	}

	/**
	 * @param mpEwalletStatus the mpEwalletStatus to set
	 */
	public void setMpEwalletStatus(boolean mpEwalletStatus) {
		this.mpEwalletStatus = mpEwalletStatus;
	}

	/**
	 * @return the walletErrorMsg
	 */
	public String getWalletErrorMsg() {
		return walletErrorMsg;
	}

	/**
	 * @param walletErrorMsg the walletErrorMsg to set
	 */
	public void setWalletErrorMsg(String walletErrorMsg) {
		this.walletErrorMsg = walletErrorMsg;
	}

	/**
	 * @return the mpButtonImgURL
	 */
	public String getMpButtonImgURL() {
		return mpButtonImgURL;
	}

	/**
	 * @param mpButtonImgURL the mpButtonImgURL to set
	 */
	public void setMpButtonImgURL(String mpButtonImgURL) {
		this.mpButtonImgURL = mpButtonImgURL;
	}

    public List<String> getOnOpenCoremetrics() {
        return onOpenCoremetrics;
    }

    public void setOnOpenCoremetrics(List<String> onOpenCoremetrics) {
        this.onOpenCoremetrics = onOpenCoremetrics;
    }

}
