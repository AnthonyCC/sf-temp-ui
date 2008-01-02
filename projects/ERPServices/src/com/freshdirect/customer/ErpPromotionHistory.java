package com.freshdirect.customer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpPromotionHistory implements Serializable {

	/** Map saleId -> Set of usedPromotionCodes */
	private final Map erpPromoHistoryInfo;

	public ErpPromotionHistory(Map erpPromoHistoryInfo) {
		this.erpPromoHistoryInfo = erpPromoHistoryInfo;
	}

	public Map getErpPromoHistoryInfo() {
		return this.erpPromoHistoryInfo;
	}

	
	public int getPromotionUsageCount(String promotionCode, String ignoreSaleId) {
		int count = 0;
		for (Iterator i=this.erpPromoHistoryInfo.keySet().iterator(); i.hasNext(); ) {
			String saleId = (String) i.next();
			if (saleId.equals(ignoreSaleId)) {
				continue;
			}
			Set usedPromoCodes = (Set)erpPromoHistoryInfo.get(saleId);
			if (usedPromoCodes.contains(promotionCode)) {
				count++;
			}
		}
		return count;
	}

	public Set getUsedPromotionCodes(String ignoreSaleId) {
		Set allPromos = new HashSet();
		for (Iterator i=this.erpPromoHistoryInfo.keySet().iterator(); i.hasNext(); ) {
			String saleId = (String) i.next();
			if (saleId.equals(ignoreSaleId)) {
				continue;
			}
			Set usedPromoCodes = (Set)erpPromoHistoryInfo.get(saleId);
			allPromos.addAll(usedPromoCodes);
		}
		return allPromos;
	}
	
	public Set getUsedPromotionCodesFor(String saleId){
		Set usedPromoCodes = (Set)this.erpPromoHistoryInfo.get(saleId);
		if(usedPromoCodes == null){
			return Collections.EMPTY_SET;
		}
		return usedPromoCodes;
	}
}
