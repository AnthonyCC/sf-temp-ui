package com.freshdirect.fdlogistics.model;


public enum EnumDeliveryFeeTier {

	TIER1, TIER2;
	 
	public static EnumDeliveryFeeTier getEnum(String tier) {
        try {
            return tier != null ? EnumDeliveryFeeTier.valueOf(tier) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
