package com.freshdirect.fdstore.customer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.PromotionFactory;

public class FDPromotionEligibility implements Serializable {

	/** Set of String (promotionCode) */
	private final Set eligibilePromos = new HashSet();

	/** Set of String (promotionCode) */
	private final Set appliedPromos = new HashSet();

	public boolean isEligible(String promotionCode) {
		return this.eligibilePromos.contains(promotionCode);
	}

	public void setEligibility(String promotionCode, boolean eligibile) {
		if (eligibile) {
			this.eligibilePromos.add(promotionCode);
		} else {
			this.eligibilePromos.remove(promotionCode);
		}
	}

	/**
	 * @param redemptionPromos Set of String
	 */
	public void setEligiblity(Set promotionCodes, boolean eligibile) {
		if (eligibile) {
			this.eligibilePromos.addAll(promotionCodes);
		} else {
			this.eligibilePromos.removeAll(promotionCodes);
		}
	}

	public void setApplied(String promotionCode) {
		if (!this.eligibilePromos.contains(promotionCode)) {
			throw new IllegalArgumentException("Attempted to apply non-eligible promotion " + promotionCode);
		}
		this.appliedPromos.add(promotionCode);
	}

	public boolean isApplied(String promotionCode) {
		return this.appliedPromos.contains(promotionCode);
	}

	/** @return Set of String (promotionCode) */
	public Set getAppliedPromotionCodes() {
		return Collections.unmodifiableSet(this.appliedPromos);
	}

	/** @return Set of String (promotionCode) */
	public Set getEligiblePromotionCodes() {
		return Collections.unmodifiableSet(this.eligibilePromos);
	}

	/** @return Set of String (promotionCode) */
	public Set getEligiblePromotionCodes(EnumPromotionType type) {
		Set s = PromotionFactory.getInstance().getPromotionCodesByType(type);
		s.retainAll(this.eligibilePromos);
		return s;
	}

	public boolean isEligibleForType(EnumPromotionType type) {
		return this.getEligiblePromotionCodes(type).size() > 0;
	}

	public String toString() {
		return "FDPromotionEligibility[eligible=" + this.eligibilePromos.toString() + ", applied=" + this.appliedPromos + "] ";
	}

}
