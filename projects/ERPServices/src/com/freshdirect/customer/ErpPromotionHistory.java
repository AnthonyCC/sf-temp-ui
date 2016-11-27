package com.freshdirect.customer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ErpPromotionHistory implements Serializable {

	private static final long	serialVersionUID	= -1153376310120005350L;
	
	/** Map saleId -> Set of usedPromotionCodes */
	private final Map<String,Set<String>> erpPromoHistoryInfo;

	public ErpPromotionHistory(Map<String,Set<String>> erpPromoHistoryInfo) {
		this.erpPromoHistoryInfo = erpPromoHistoryInfo;
	}

	public Map<String,Set<String>> getErpPromoHistoryInfo() {
		return this.erpPromoHistoryInfo;
	}

	
	public int getPromotionUsageCount(String promotionCode, String ignoreSaleId) {
		int count = 0;
		for ( String saleId : erpPromoHistoryInfo.keySet() ) {
			if (saleId.equals(ignoreSaleId)) {
				continue;
			}
			Set<String> usedPromoCodes = erpPromoHistoryInfo.get(saleId);
			if (usedPromoCodes.contains(promotionCode)) {
				count++;
			}
		}
		return count;
	}

	public Set<String> getUsedPromotionCodes(String ignoreSaleId) {
		Set<String> allPromos = new HashSet<String>();
		for ( String saleId : erpPromoHistoryInfo.keySet() ) {
			if (saleId.equals(ignoreSaleId)) {
				continue;
			}
			Set<String> usedPromoCodes = erpPromoHistoryInfo.get(saleId);
			allPromos.addAll(usedPromoCodes);
		}
		return allPromos;
	}
	
	public Set<String> getUsedPromotionCodesFor( String saleId ) {
		Set<String> usedPromoCodes = erpPromoHistoryInfo.get( saleId );
		if ( usedPromoCodes == null ) {
			return Collections.<String>emptySet();
		}
		return usedPromoCodes;
	}
}
