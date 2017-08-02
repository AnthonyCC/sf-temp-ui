package com.freshdirect.fdstore.payments.util;

import java.util.Comparator;

import com.freshdirect.customer.ErpPaymentMethodI;

public class PaymentMethodDefaultComparator implements Comparator<ErpPaymentMethodI>{
	
		@Override
	public int compare(ErpPaymentMethodI p1, ErpPaymentMethodI p2) {
		if(p1.isDebitCard() && !p2.isDebitCard()){
			return -1;
		}else if(!p1.isDebitCard() && p2.isDebitCard()){
			return 1;
		}
		else if((p1.isDebitCard() && p2.isDebitCard()) || (!p1.isDebitCard() && !p2.isDebitCard())){
		if(p1.getCardType().getPriority() < p2.getCardType().getPriority()){
			return -1;
		}else if(p1.getCardType().getPriority() > p2.getCardType().getPriority()){
			return 1;
		}else
		if(p1.getCardType().getPriority() == p2.getCardType().getPriority()){
			if(Long.parseLong(p1.getPK().getId().trim()) > Long.parseLong(p2.getPK().getId().trim())){
				return -1;
			}else{
				return 1;
			}
		}
		}
		return 0;
	}

}
