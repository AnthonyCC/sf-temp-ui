package com.freshdirect.giftcard;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;

public class ErpGCDlvInformationHolder extends ModelSupport {

	private String giftCardId;
	private ErpRecipentModel recepientModel;
	private String givexNum;
	private Date purchaseDate;

	public ErpGCDlvInformationHolder(){
		
	}
	
	public ErpRecipentModel getRecepientModel() {
		return recepientModel;
	}

	public void setRecepientModel(ErpRecipentModel recepientModel) {
		this.recepientModel = recepientModel;
	}

   
	public void setGiftCardId(String id) {
		this.giftCardId=id;
	}

	public String getGiftCardId() {
		return giftCardId;
	}

	public String getGivexNum() {
		return givexNum;
	}

	public void setGivexNum(String givexNum) {
		this.givexNum = givexNum;
	}
	
	public String getCertificationNumber(){
		if(this.givexNum != null)
			return ErpGiftCardUtil.getCertificateNumber(this.givexNum);
		return null;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public String getPurchaseSaleId(){
          if(this.getRecepientModel()!=null) return this.getRecepientModel().getSale_id();
          else return "";
	}
	
	public String getMaskedGivexNum() {
    	int numDisplayedDigits = Math.min(givexNum.length(), 5); 
    	int numMaskedDigits = 10 - numDisplayedDigits;
    	return  "xxxxxxxx".substring(0, numMaskedDigits) + givexNum.substring(givexNum.length()-numDisplayedDigits);  
		//int pmLen = this.accountNumber.length()-4;
		//return "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".substring(0,pmLen) + this.accountNumber.substring(pmLen);
	}
}
