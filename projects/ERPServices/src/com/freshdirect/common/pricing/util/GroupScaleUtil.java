package com.freshdirect.common.pricing.util;

import java.util.Iterator;
import java.util.List;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.GrpZonePriceModel;

public class GroupScaleUtil {
	public static MaterialPrice getGroupScalePrice(FDGroup group, String pricingZoneId) throws FDResourceException{
		MaterialPrice matPrice = null;
		try{
			GroupScalePricing grpPricing = FDCachedFactory.getGrpInfo(group);
			if(grpPricing != null && grpPricing.isActive()){
				GrpZonePriceModel grpPriceModel = grpPricing.getGrpZonePrice(pricingZoneId);
				if(grpPriceModel != null){
					MaterialPrice[] matPrices =grpPriceModel.getMaterialPrices();
					if(matPrices != null && matPrices.length > 0) {
						//Current Implementation only supports single Group Scale Price.
						//This has to change when multiple group scale prices are supported.
						matPrice = matPrices[0];
						if(!isGroupPriceValid(grpPricing, pricingZoneId, matPrice)){
							//validate the price of group to be less than all group products price.
							return null;
						}
					}
					
				}
			}
		} catch(FDGroupNotFoundException fe){
			return null;
		} catch(FDResourceException fe){
			throw fe;
		}
		return matPrice;
	}

	public static MaterialPrice getGroupScalePriceByQty(FDGroup group, String pricingZoneId, double grpQty) throws FDResourceException{
		MaterialPrice matPrice = null;
		try{
			GroupScalePricing grpPricing = FDCachedFactory.getGrpInfo(group);
			if(grpPricing != null){
				GrpZonePriceModel grpPriceModel = grpPricing.getGrpZonePrice(pricingZoneId);
				if(grpPriceModel != null){
					matPrice = grpPriceModel.findMaterialPrice(grpQty);
					if(matPrice != null) {
						if(!isGroupPriceValid(grpPricing, pricingZoneId, matPrice)){
							//validate the price of group to be less than all group products price.
							return null;
						}
					}
					
				}
			}
		} catch(FDGroupNotFoundException fe){
			return null;
		} catch(FDResourceException fe){
			throw fe;
		}
		return matPrice;
	}

	public static boolean isGroupPriceValid(GroupScalePricing grpPricing, String pricingZoneId, MaterialPrice grpMatPrice) throws FDResourceException{
		List<String> skuList = grpPricing.getSkuList();
		for(Iterator<String> it = skuList.iterator(); it.hasNext();){
			try {
				FDProductInfo pInfo = FDCachedFactory.getProductInfo(it.next());
				if(pInfo.getZonePriceInfo(pricingZoneId).getDefaultPrice() <= grpMatPrice.getPrice()){
					//At least one item is having a price <= group price. Group price is invalidated.
					return false;
				}
			} catch(FDSkuNotFoundException e){
				//invalid group
				return false;
			}
		}
		//All items price were > group price. group price is valid.
		return true;
	}
	public static MaterialPrice[] getGroupScalePrices(FDGroup group, String pricingZoneId) throws FDResourceException{
		MaterialPrice[] matPrices = null;
		try{
			GroupScalePricing grpPricing = FDCachedFactory.getGrpInfo(group);
			if(grpPricing != null && grpPricing.isActive()){
				GrpZonePriceModel grpPriceModel = grpPricing.getGrpZonePrice(pricingZoneId);
				if(grpPriceModel != null){
					
					matPrices =grpPriceModel.getMaterialPrices();
					if(matPrices != null && matPrices.length > 0) {
						MaterialPrice matPrice = matPrices[0];
						if(!isGroupPriceValid(grpPricing, pricingZoneId, matPrice)){
							//validate the price of group to be less than all group products price.
							return null;
						}
					}
					
				}
			}
		} catch(FDGroupNotFoundException fe){
			return null;
		} catch(FDResourceException fe){
			throw fe;
		}
		return matPrices;
	}

	public static GroupScalePricing lookupGroupPricing(FDGroup group) throws FDResourceException{
		try{
			GroupScalePricing grpPricing = FDCachedFactory.getGrpInfo(group);
			return grpPricing;
		} catch(FDGroupNotFoundException fe){
			return null;
		} catch(FDResourceException fe){
			throw fe;
		}
	}
	public static String getGroupPricingZoneId(FDGroup group, String pricingZoneId) throws FDResourceException{
		try{
			GroupScalePricing groupPricing = GroupScaleUtil.lookupGroupPricing(group);
			if(groupPricing!=null) {
				GrpZonePriceModel grpPriceModel = groupPricing.getGrpZonePrice(pricingZoneId);
				if(grpPriceModel != null) {
					return grpPriceModel.getSapZoneId();
				} 
			}
		} catch(FDResourceException fe){
			throw fe;
		}
		return null;
	}
}
