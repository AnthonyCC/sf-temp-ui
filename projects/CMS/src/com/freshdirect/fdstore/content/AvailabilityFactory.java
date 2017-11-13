package com.freshdirect.fdstore.content;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.util.ProductInfoUtil;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumDayPartValueType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDStockAvailability;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AvailabilityFactory {

    private final static Logger LOG = LoggerFactory.getInstance(AvailabilityFactory.class); 
    
	private final static EnumDlvRestrictionReason[] DAY_REASONS = {
		EnumDlvRestrictionReason.BLOCK_SUNDAY,
		EnumDlvRestrictionReason.BLOCK_MONDAY,
		EnumDlvRestrictionReason.BLOCK_TUESDAY,
		EnumDlvRestrictionReason.BLOCK_WEDNESDAY,
		EnumDlvRestrictionReason.BLOCK_THURSDAY,
		EnumDlvRestrictionReason.BLOCK_FRIDAY,
		EnumDlvRestrictionReason.BLOCK_SATURDAY};

	public static Set<EnumDlvRestrictionReason> getApplicableRestrictions(FDProduct product, FDProductInfo fdpi) {
		Set<EnumDlvRestrictionReason> s = new HashSet<EnumDlvRestrictionReason>();
		String plantID=ProductInfoUtil.getPickingPlantId(fdpi);
		
		if (product.isAlcohol()) {
			s.add(EnumDlvRestrictionReason.ALCOHOL);
			if (product.isBeer()) {
				s.add(EnumDlvRestrictionReason.BEER);
			} else if (product.isWine()) {
				s.add(EnumDlvRestrictionReason.WINE);
			}
		}

		if (product.isKosherProduction(plantID)) {
			s.add(EnumDlvRestrictionReason.KOSHER);
		}

		if (product.isPlatter(plantID)) {
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
		s.addAll(product.getMaterial().getBlockedDays(plantID).translate(DAY_REASONS));
		
		ZoneInfo zoneInfo= (null !=ContentFactory.getInstance().getCurrentUserContext() && null !=ContentFactory.getInstance().getCurrentUserContext().getPricingContext())? ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo(): null;
		if(null !=zoneInfo){
			String salesOrg = zoneInfo.getSalesOrg();
			String distChannel = zoneInfo.getDistributionChanel();
			
			EnumDayPartValueType dayPartValueType = fdpi.getDayPartValueType(salesOrg, distChannel);
			if(null != dayPartValueType){
				 List<EnumDlvRestrictionReason> dayPartDlvRestrictionTypes = EnumDlvRestrictionReason.getDayPartRestrictionsEumList();
				 for (EnumDlvRestrictionReason dayPartDlvRestrictionType : dayPartDlvRestrictionTypes) {
					if(dayPartValueType.getName().equals(dayPartDlvRestrictionType.getName())){
						s.add(dayPartDlvRestrictionType);
						break;
					}
				}
			}
		}

		return s;
	}

	public static FDAvailabilityI createAvailability(SkuModel skuModel, FDProductInfo fdProductInfo,String plantID) {

		FDAvailabilityI fdAvailabilityInterface = NullAvailability.AVAILABLE;
		if (EnumATPRule.JIT.equals(fdProductInfo.getATPRule(plantID)) || (FDStoreProperties.getPreviewMode() && null == fdProductInfo.getATPRule(plantID))) {
			return fdAvailabilityInterface;
		}
		//appdev 6184 changed fdproductinfo.getInventory(plantid) to be  more efficient.
		ErpInventoryModel inventory = fdProductInfo.getInventory(plantID);
//		EnumEStoreId eStore = ContentFactory.getInstance().getCurrentUserContext() != null &&
//				ContentFactory.getInstance().getCurrentUserContext().getStoreContext() != null ?
//								ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId()
//                : EnumEStoreId.FD;
		if (inventory != null) {
			ProductModel productModel = skuModel.getProductModel();
			if (productModel != null) {
			    return new FDStockAvailability(
			            inventory,
			            productModel.getQuantityMinimum(),
			            productModel.getQuantityMinimum(),
			            productModel.getQuantityIncrement()
			            );
			} else {
			    LOG.error("Product model for " + skuModel.getSkuCode() + " not found, however product info is available :" + fdProductInfo);
			}
		}
		
		/*
		Set restrictions = getApplicableRestrictions(fdProduct);
		if (!restrictions.isEmpty()) {

			DlvRestrictionsList applicableRestrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
			DlvRestrictionsList appliedRestrictions = new DlvRestrictionsList(applicableRestrictions.getRestrictions(restrictions));

			av = new FDRestrictedAvailability(av, appliedRestrictions);
		}
		*/

		return fdAvailabilityInterface;
	}
	


}