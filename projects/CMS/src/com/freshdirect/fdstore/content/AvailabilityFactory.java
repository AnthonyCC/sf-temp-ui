package com.freshdirect.fdstore.content;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.FDKosherInfo;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDStockAvailability;
import com.freshdirect.fdstore.atp.NullAvailability;

public class AvailabilityFactory {

	private final static EnumDlvRestrictionReason[] DAY_REASONS = {
		EnumDlvRestrictionReason.BLOCK_SUNDAY,
		EnumDlvRestrictionReason.BLOCK_MONDAY,
		EnumDlvRestrictionReason.BLOCK_TUESDAY,
		EnumDlvRestrictionReason.BLOCK_WEDNESDAY,
		EnumDlvRestrictionReason.BLOCK_THURSDAY,
		EnumDlvRestrictionReason.BLOCK_FRIDAY,
		EnumDlvRestrictionReason.BLOCK_SATURDAY};

	public static Set<EnumDlvRestrictionReason> getApplicableRestrictions(FDProduct product) {
		Set<EnumDlvRestrictionReason> s = new HashSet<EnumDlvRestrictionReason>();

		if (product.isAlcohol()) {
			s.add(EnumDlvRestrictionReason.ALCOHOL);
			if (product.isBeer()) {
				s.add(EnumDlvRestrictionReason.BEER);
			} else if (product.isWine()) {
				s.add(EnumDlvRestrictionReason.WINE);
			}
		}

		if (product.isKosherProduction()) {
			s.add(EnumDlvRestrictionReason.KOSHER);
		}

		if (product.isPlatter()) {
			s.add(EnumDlvRestrictionReason.PLATTER);
		}

		/** treat the roses sku as if it has a valentine restriction */
		if ("VEG0067218".equalsIgnoreCase(product.getSkuCode()) ) {
			s.add(EnumDlvRestrictionReason.VALENTINES);
		}
		
		String restrictions = product.getMaterial().getRestrictions();
		if (restrictions != null) {
			String[] tokens = StringUtils.split(restrictions, ",");
			for (int i = 0; i < tokens.length; i++) {
				EnumDlvRestrictionReason rest = EnumDlvRestrictionReason.getEnum(tokens[i]);
				if (rest != null) {
					s.add(rest);
				}
			}
		}
		
		s.addAll(product.getMaterial().getBlockedDays().translate(DAY_REASONS));

		return s;
	}

	public static FDAvailabilityI createAvailability(SkuModel skuModel, FDProductInfo fdProductInfo) throws FDResourceException {

		FDAvailabilityI av = NullAvailability.AVAILABLE;
		
		if (EnumATPRule.JIT.equals(fdProductInfo.getATPRule())) {
			return av;
		}

		ErpInventoryModel inventory = fdProductInfo.getInventory();
		if (inventory != null) {
			ProductModel productModel = skuModel.getProductModel();
			av = new FDStockAvailability(
				inventory,
				productModel.getQuantityMinimum(),
				productModel.getQuantityMinimum(),
				productModel.getQuantityIncrement());
		}
		
		/*
		Set restrictions = getApplicableRestrictions(fdProduct);
		if (!restrictions.isEmpty()) {

			DlvRestrictionsList applicableRestrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
			DlvRestrictionsList appliedRestrictions = new DlvRestrictionsList(applicableRestrictions.getRestrictions(restrictions));

			av = new FDRestrictedAvailability(av, appliedRestrictions);
		}
		*/

		return av;
	}

}