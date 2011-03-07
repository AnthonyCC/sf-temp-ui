/*
 * $Workfile:FDModifyCartModel.java$
 *
 * $Date:5/22/03 7:19:08 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.ExtendDeliveryPassApplicator;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;

/**
 *
 * @version	$Revision:10$
 * @author	 $Author:Viktor Szathmary$
 * @stereotype fd-model
 */
public class FDModifyCartModel extends FDCartModel {

	private static final long	serialVersionUID	= -3926647074819221032L;
	
	private final FDOrderAdapter originalOrder;

	/**
	 * Default constructor.
	 */
	public FDModifyCartModel(FDOrderAdapter originalOrder) {
		this.setCharges(originalOrder.getCharges());
		this.originalOrder = originalOrder;

		// populate orderlines from orig.order
		for (FDCartLineI origLine : originalOrder.getOrderLines()) {
			FDCartLineI cartLine = new FDModifyCartLineModel(origLine);
			Discount d = origLine.getDiscount();
			if( d != null && !(d.getDiscountType().isSample())) {
				cartLine.setDiscount(d);
				cartLine.setDiscountFlag(true);
			}
			if(origLine.getSavingsId() != null)
				cartLine.setSavingsId(origLine.getSavingsId());
			if(origLine.getFDGroup() != null)
				cartLine.setFDGroup(origLine.getFDGroup());			
			
			this.addOrderLine(cartLine);
		}

		// initialize from original order
		this.setDeliveryAddress(originalOrder.getCorrectedDeliveryAddress());
		this.setPaymentMethod(originalOrder.getPaymentMethod());

		this.setDeliveryReservation(originalOrder.getDeliveryReservation());

		// !!! partially reconstruct the original zoneInfo (we don't need the full state, as it will be set later)
		DlvZoneInfoModel zoneInfo = new DlvZoneInfoModel(originalOrder.getDeliveryZone(), null, null, EnumZipCheckResponses.DELIVER,false,false);
		this.setZoneInfo(zoneInfo);

		this.setCustomerServiceMessage(originalOrder.getCustomerServiceMessage());
		this.setMarketingMessage(originalOrder.getMarketingMessage());

	}

	public FDOrderAdapter getOriginalOrder() {
		return this.originalOrder;
	}

	public String getOriginalReservationId() {
		return this.originalOrder.getDeliveryReservationId();
	}

	@Override
	public boolean isDlvPassAlreadyApplied() {
		if(this.originalOrder.getDeliveryPassId() !=  null){
			//Delivery pass was already applied in this order either during create
			//or during last modification. So preserve the applied pass.
			return true;
		}
		return false;
	}
	
	@Override
	protected void checkNewLinesForUpgradedGroup(String pZoneId,
			Map<FDGroup, Double> groupMap,
			Map<FDGroup, Double> qualifiedGroupMap,
			Map<String, FDGroup> qualifiedGrpIdMap) throws FDResourceException {
		//Map to maintain the unqualified groups for further evaluation.
			Map<String, FDGroup> nonQualifiedGrpIdMap = new HashMap<String, FDGroup>(); 
		//Only do this for Modify Order.
		Iterator<FDGroup> it = groupMap.keySet().iterator();
		while(it.hasNext()){
			FDGroup newGroup = it.next();
			String grpId = newGroup.getGroupId();
			if(!qualifiedGroupMap.containsKey(newGroup)){
				if(qualifiedGrpIdMap.containsKey(grpId)) {
					//Group Id is a part of a fully qualified Group
					//This is a different version of same group. Check if the old version and new version
					//has same price and same scale qty. 
					FDGroup qGroup = qualifiedGrpIdMap.get(grpId);
					MaterialPrice qMatPrice = GroupScaleUtil.getGroupScalePrice(qGroup, pZoneId);
					MaterialPrice newMatPrice = GroupScaleUtil.getGroupScalePrice(newGroup, pZoneId);
					//Check if the old version and new version has same price and same scale qty. 
					//If yes add the old qty to new qty and set it both old group and new group.
					if(qMatPrice!= null && newMatPrice != null && qMatPrice.getPrice() == newMatPrice.getPrice() &&
							qMatPrice.getScaleLowerBound() == newMatPrice.getScaleLowerBound()){
						double quantity = groupMap.get(qGroup);
						double newQty = groupMap.get(newGroup);
						quantity += newQty;
						qualifiedGroupMap.put(qGroup, quantity);
						qualifiedGroupMap.put(newGroup, quantity);
						if(newGroup.getVersion() > qGroup.getVersion()){
							qualifiedGrpIdMap.put(grpId, newGroup);
						}
					 }
				}else{
					//Group Id can be a part of a partially qualified Group
					if(nonQualifiedGrpIdMap.containsKey(grpId)){
						//This is a different version of same group. Check if the old version and new version
						//has same price and same scale qty. 
						FDGroup nqGroup = nonQualifiedGrpIdMap.get(grpId);
						MaterialPrice qMatPrice = GroupScaleUtil.getGroupScalePrice(nqGroup, pZoneId);
						MaterialPrice newMatPrice = GroupScaleUtil.getGroupScalePrice(newGroup, pZoneId);
						//Check if the old version and new version has same price and same scale qty. 
						//If yes add the old qty to new qty and set it both old group and new group.
						if(qMatPrice!= null && newMatPrice != null && qMatPrice.getPrice() == newMatPrice.getPrice() &&
								qMatPrice.getScaleLowerBound() == newMatPrice.getScaleLowerBound()){
							double quantity = groupMap.get(nqGroup);
							double newQty = groupMap.get(newGroup);
							quantity += newQty;
							FDGroup maxGroup = nqGroup;
							if(newGroup.getVersion() > nqGroup.getVersion()){
								maxGroup = newGroup;
							} 
							
							if(quantity >= qMatPrice.getScaleLowerBound()){
								//Reached qualified limit. Add both Groups to qualified Map.
								qualifiedGroupMap.put(nqGroup, quantity);
								qualifiedGroupMap.put(newGroup, quantity);
								//Max version group to qualifiedGrpIdMap.
								qualifiedGrpIdMap.put(grpId, maxGroup);
							}
							nonQualifiedGrpIdMap.put(grpId, maxGroup);
						 } 
					} else {
						 //add the group Id to nonQualifiedGrpIdMap
						 nonQualifiedGrpIdMap.put(grpId, newGroup);
					}
				}
			}
		}
	}
}
