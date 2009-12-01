package com.freshdirect.fdstore.giftcard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.giftcard.ErpGiftCardModel;

import java.util.Collections;

public class FDGiftCardInfoList implements Serializable {
	
	//List of ErpGiftCardModel.
	private List giftcards = null;
	
	public final static Comparator GIFT_CARD_RECEIVED_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {

			FDGiftCardModel p1 = (FDGiftCardModel) o1;
			FDGiftCardModel p2 = (FDGiftCardModel) o2;

			int ret = new Double(p2.getBalance()).compareTo(new Double(p1.getBalance()));
			return ret;
		}
	};
	
	public FDGiftCardInfoList(Collection erpgiftcards) {
		this.giftcards = new ArrayList(erpgiftcards.size());
		for(Iterator it = erpgiftcards.iterator(); it.hasNext();){
			ErpGiftCardModel gcModel = (ErpGiftCardModel)it.next();
			this.addGiftCard(new FDGiftCardModel(gcModel));
		}
	}

	public List getGiftcards() {
		Collections.sort(giftcards, GIFT_CARD_RECEIVED_COMPARATOR);
		return Collections.unmodifiableList(giftcards);
	}

	public List getSelectedGiftcards(){
		List selectedCards = new ArrayList();
		for(Iterator it = this.giftcards.iterator(); it.hasNext();) {
			FDGiftCardModel gc = (FDGiftCardModel) it.next();
			if(FDStoreProperties.isGivexBlackHoleEnabled()|| !gc.isRedeemable() || !gc.isSelected() || gc.getBalance() <= 0 ) {
				continue;
			} 
			//Clone Gift card model object.
			ErpGiftCardModel cloneGC = (ErpGiftCardModel) gc.getGiftCardModel().deepCopy();
			//Set the balance in order to apply the hold amount during modify.
			cloneGC.setBalance(gc.getBalance());
			selectedCards.add(cloneGC);
		}
		return Collections.unmodifiableList(selectedCards);
	}
	public void addGiftCard(FDGiftCardI giftcard) {
		if(getGiftCard(giftcard.getCertificateNumber()) != null){
			//Certificate already exists. Do not add it again.
			//This is a unlikely scenario. 
			return;
		}
		this.giftcards.add(giftcard);
	}
	
	public FDGiftCardI getGiftCard(String certificationNum) {
		for(Iterator it = this.giftcards.iterator(); it.hasNext();) {
			FDGiftCardI gc = (FDGiftCardI) it.next();
			if(gc.getCertificateNumber().equals(certificationNum)){
				return gc;
			}
		}
		return null;
	}
	
	public double getTotalBalance() {
		double balance = 0.0;
		for(Iterator it = this.giftcards.iterator(); it.hasNext();) {
			FDGiftCardI gc = (FDGiftCardI) it.next();
			if(FDStoreProperties.isGivexBlackHoleEnabled() || !gc.isRedeemable() || !gc.isSelected()) {
				continue;
			}
			balance += gc.getBalance();
			//balance += gc.getHoldAmount();
		}
		return balance;
	}
	
	public void clearAllHoldAmount() {
		for(Iterator it = this.giftcards.iterator(); it.hasNext();) {
			FDGiftCardI gc = (FDGiftCardI) it.next();
				gc.setHoldAmount(0.0);
		}
	}
	public void clearAllSelection(){
		for(Iterator it = this.giftcards.iterator(); it.hasNext();) {
			FDGiftCardModel gc = (FDGiftCardModel) it.next();
			gc.setSelected(false);
		}
	}
	
	public void setSelected(String certificationNum, boolean selected){
		getGiftCard(certificationNum).setSelected(selected);
	}
	
	public void remove(String certificationNum){
		for(Iterator it = this.giftcards.iterator(); it.hasNext();) {
			FDGiftCardI gc = (FDGiftCardI) it.next();
			if(gc.getCertificateNumber().equals(certificationNum)){
				it.remove();
				break;
			}
		}
	}
	
	public double getGiftcardsTotalBalance() {
		double balance = 0.0;
		for(Iterator it = this.giftcards.iterator(); it.hasNext();) {
			FDGiftCardI gc = (FDGiftCardI) it.next();
			if(FDStoreProperties.isGivexBlackHoleEnabled() || !gc.isRedeemable()) {
				continue;
			}
			balance += gc.getBalance();
			//balance += gc.getHoldAmount();
		}
		return balance;
	}
	
	public int size() {
		return this.giftcards.size();
	}
}
