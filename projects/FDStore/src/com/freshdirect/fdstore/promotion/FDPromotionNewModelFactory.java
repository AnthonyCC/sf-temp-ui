package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.ExpiringReference;

public class FDPromotionNewModelFactory {

private final static FDPromotionNewModelFactory INSTANCE = new FDPromotionNewModelFactory();
	/*
	 * Map of cached promotions where keys are promo codes
	 */
	private ExpiringReference<Map<String,FDPromotionNewModel>> promotions = new ExpiringReference<Map<String,FDPromotionNewModel>>(10 * 60 * 1000) {

		@Override
		protected Map<String,FDPromotionNewModel> load() {
			try {
				List<FDPromotionNewModel> promoList = FDPromotionNewManager.getPromotions();
				Map<String,FDPromotionNewModel> promos = new HashMap<String,FDPromotionNewModel>(promoList.size());
				for (FDPromotionNewModel promo : promoList) {
					promos.put(promo.getPromotionCode(), promo);
				}
				return promos;
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
		
	};

	private FDPromotionNewModelFactory() {
	}

	public static FDPromotionNewModelFactory getInstance() {
		return INSTANCE;
	}

	private Map<String,FDPromotionNewModel> getPromotionMap() {
		return this.promotions.get();
	}

	public Collection<FDPromotionNewModel> getPromotions() {
		return this.getPromotionMap().values();
	}

	public Collection<String> getPromotionCodes() {
		return this.getPromotionMap().keySet();
	}
	
	public FDPromotionNewModel getPromotion(String promotionCode) {
		return (FDPromotionNewModel) this.getPromotionMap().get(promotionCode);
	}

	/*public Set searchByString(String search) {
		Set s = new HashSet();
		if(search == null)
			return s;
		
		search = search.trim().toUpperCase();
		
		for (Iterator i = this.getPromotions().iterator(); i.hasNext();) {
			FDPromotionNewModel promo = (FDPromotionNewModel) i.next();
			String desc = promo.getDescription();
			if(desc != null) {
				desc = desc.toUpperCase();
			} else {
				desc = "";
			}
			
			String name = promo.getName();
			if(name != null) {
				name = name.toUpperCase();
			} else {
				name = "";
			}

			String code = promo.getPromotionCode();
			if(code != null) {
				code = code.toUpperCase();
			} else {
				code = "";
			}
			
			String redempCode = promo.getRedemptionCode();;
			if(redempCode != null) {
				redempCode = redempCode.toUpperCase();
			} else {
				redempCode = "";
			}

			if( (desc.indexOf(search) >= 0) ||
				(code.indexOf(search) >= 0) ||
				(redempCode.indexOf(search) >= 0) ||
				(name.indexOf(search) >= 0)) {
				s.add(promo);
			}
		}
		return s;
	}*/
	
	public void forceRefresh() {
		promotions.forceRefresh();
	}
	
	public List<String> getPromotionsCreatedUsers(){
		Collection<FDPromotionNewModel> promotions = getPromotions();
		List<String> users = new ArrayList<String>();
		for (Object object : promotions) {
			FDPromotionNewModel model = (FDPromotionNewModel)object;
			if(null !=model.getCreatedBy() && !users.contains(model.getCreatedBy())){
				users.add(model.getCreatedBy());
			}
			
		}
		Collections.sort(users);
		return users;
	}
	
	public List<String> getPromotionsModifiedUsers(){
		Collection<FDPromotionNewModel> promotions = getPromotions();
		List<String> users = new ArrayList<String>();
		for (Object object : promotions) {
			FDPromotionNewModel model = (FDPromotionNewModel)object;
			if(null !=model.getModifiedBy() && !users.contains(model.getModifiedBy())){
				users.add(model.getModifiedBy());
			}
			
		}
		Collections.sort(users);
		return users;
	}
}
