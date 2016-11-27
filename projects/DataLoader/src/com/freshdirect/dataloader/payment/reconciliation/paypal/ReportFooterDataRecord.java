package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.Serializable;

public class ReportFooterDataRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4358347785759828871L;

	public	 String currencyOfBalance = null;
	public	 long totalGrossAmountCredits = 0;
	public	 long totalGrossAmountDebits = 0;
	public	 long totalTransactionFeeCredits = 0;
	public	 long totalTransactionFeeDebits = 0;
	public	 String beginningAvailableBalanceDebitOrCredit = null;
	public	 long beginningAvailableBalanceInThisCurrency = 0;
	public	 String endingAvailableBalanceDebitOrCredit = null;
	public	 long endingAvailableBalanceInThisCurrency = 0;
	public	 String beginningTotalBalanceDebitOrCredit = null;
	public	 long beginningTotalBalanceInThisCurrency = 0;
	public	 String endingTotalBalanceDebitOrCredit = null;
	public	 long endingTotalBalanceInThisCurrency = 0;
	public	 String beginningPayableBalanceDebitOrCredit = null;
	public	 long beginningPayableBalance = 0;
	public	 String endingPayableBalanceDebitOrCredit = null;
	public	 long endingPayableBalance = 0;
	public String getCurrencyOfBalance() {
		return currencyOfBalance;
	}
	public void setCurrencyOfBalance(String currencyOfBalance) {
		this.currencyOfBalance = currencyOfBalance;
	}
	public long getTotalGrossAmountCredits() {
		return totalGrossAmountCredits;
	}
	public void setTotalGrossAmountCredits(long totalGrossAmountCredits) {
		this.totalGrossAmountCredits = totalGrossAmountCredits;
	}
	public long getTotalGrossAmountDebits() {
		return totalGrossAmountDebits;
	}
	public void setTotalGrossAmountDebits(long totalGrossAmountDebits) {
		this.totalGrossAmountDebits = totalGrossAmountDebits;
	}
	public long getTotalTransactionFeeCredits() {
		return totalTransactionFeeCredits;
	}
	public void setTotalTransactionFeeCredits(long totalTransactionFeeCredits) {
		this.totalTransactionFeeCredits = totalTransactionFeeCredits;
	}
	public long getTotalTransactionFeeDebits() {
		return totalTransactionFeeDebits;
	}
	public void setTotalTransactionFeeDebits(long totalTransactionFeeDebits) {
		this.totalTransactionFeeDebits = totalTransactionFeeDebits;
	}
	public String getBeginningAvailableBalanceDebitOrCredit() {
		return beginningAvailableBalanceDebitOrCredit;
	}
	public void setBeginningAvailableBalanceDebitOrCredit(
			String beginningAvailableBalanceDebitOrCredit) {
		this.beginningAvailableBalanceDebitOrCredit = beginningAvailableBalanceDebitOrCredit;
	}
	public long getBeginningAvailableBalanceInThisCurrency() {
		return beginningAvailableBalanceInThisCurrency;
	}
	public void setBeginningAvailableBalanceInThisCurrency(
			long beginningAvailableBalanceInThisCurrency) {
		this.beginningAvailableBalanceInThisCurrency = beginningAvailableBalanceInThisCurrency;
	}
	public String getEndingAvailableBalanceDebitOrCredit() {
		return endingAvailableBalanceDebitOrCredit;
	}
	public void setEndingAvailableBalanceDebitOrCredit(
			String endingAvailableBalanceDebitOrCredit) {
		this.endingAvailableBalanceDebitOrCredit = endingAvailableBalanceDebitOrCredit;
	}
	public long getEndingAvailableBalanceInThisCurrency() {
		return endingAvailableBalanceInThisCurrency;
	}
	public void setEndingAvailableBalanceInThisCurrency(
			long endingAvailableBalanceInThisCurrency) {
		this.endingAvailableBalanceInThisCurrency = endingAvailableBalanceInThisCurrency;
	}
	public String getBeginningTotalBalanceDebitOrCredit() {
		return beginningTotalBalanceDebitOrCredit;
	}
	public void setBeginningTotalBalanceDebitOrCredit(
			String beginningTotalBalanceDebitOrCredit) {
		this.beginningTotalBalanceDebitOrCredit = beginningTotalBalanceDebitOrCredit;
	}
	public long getBeginningTotalBalanceInThisCurrency() {
		return beginningTotalBalanceInThisCurrency;
	}
	public void setBeginningTotalBalanceInThisCurrency(
			long beginningTotalBalanceInThisCurrency) {
		this.beginningTotalBalanceInThisCurrency = beginningTotalBalanceInThisCurrency;
	}
	public String getEndingTotalBalanceDebitOrCredit() {
		return endingTotalBalanceDebitOrCredit;
	}
	public void setEndingTotalBalanceDebitOrCredit(
			String endingTotalBalanceDebitOrCredit) {
		this.endingTotalBalanceDebitOrCredit = endingTotalBalanceDebitOrCredit;
	}
	public long getEndingTotalBalanceInThisCurrency() {
		return endingTotalBalanceInThisCurrency;
	}
	public void setEndingTotalBalanceInThisCurrency(
			long endingTotalBalanceInThisCurrency) {
		this.endingTotalBalanceInThisCurrency = endingTotalBalanceInThisCurrency;
	}
	public String getBeginningPayableBalanceDebitOrCredit() {
		return beginningPayableBalanceDebitOrCredit;
	}
	public void setBeginningPayableBalanceDebitOrCredit(
			String beginningPayableBalanceDebitOrCredit) {
		this.beginningPayableBalanceDebitOrCredit = beginningPayableBalanceDebitOrCredit;
	}
	public long getBeginningPayableBalance() {
		return beginningPayableBalance;
	}
	public void setBeginningPayableBalance(long beginningPayableBalance) {
		this.beginningPayableBalance = beginningPayableBalance;
	}
	public String getEndingPayableBalanceDebitOrCredit() {
		return endingPayableBalanceDebitOrCredit;
	}
	public void setEndingPayableBalanceDebitOrCredit(
			String endingPayableBalanceDebitOrCredit) {
		this.endingPayableBalanceDebitOrCredit = endingPayableBalanceDebitOrCredit;
	}
	public long getEndingPayableBalance() {
		return endingPayableBalance;
	}
	public void setEndingPayableBalance(long endingPayableBalance) {
		this.endingPayableBalance = endingPayableBalance;
	}
	
	
}
