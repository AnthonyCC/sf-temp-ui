package com.freshdirect.fdstore.promotion;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.promotion.management.FDPromotionManager;
import com.freshdirect.framework.cache.CacheI;
import com.freshdirect.framework.cache.ManagedCache;
import com.freshdirect.framework.cache.SimpleLruCache;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;
/**
 * This cache factory class is provide methods used by website/CRM for runtime 
 * evaluations of promotions.
 * The cache contains a Map of all automatic promotion codes with value as last
 * modified timestamp. This map is refreshed every few miniutes which reloads all automatic
 * promo codes with recent modified timestamp along with any newly added promo codes.  
 * The requested automatic promotion object residing in the cache are refreshed only if the corresponding
 * timestamp in the map has changed.
 * @author skrishnasamy
 *
 */
public class PromotionFactory {

	private final static PromotionFactory INSTANCE = new PromotionFactory();
	private static Category LOGGER = LoggerFactory.getInstance(PromotionFactory.class);
	private CacheI cache;
	private Map promotionMap = new LinkedHashMap();
	private Date maxLastModified;
	//private int refreshPeriod = 1200; //20 Minutes
	
	private ExpiringReference automaticpromotions = new ExpiringReference(10 * 60 * 1000) {
		protected Object load() {
			try {
				LOGGER.info("REFRESHING AUTOMATIC PROMOTION MAP FOR ANY NEW PROMOTIONS FROM LAST MODIFIED TIME "+maxLastModified);
				List promoList = FDPromotionManager.getModifiedOnlyPromos(maxLastModified);
				LOGGER.info("REFRESHED AUTOMATIC PROMOTION MAP FOR ANY NEW PROMOTIONS. FOUND "+promoList.size());
				return promoList;
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
	};

	private PromotionFactory() {
		this.cache = new ManagedCache("PROMOTION", constructCache());
		loadAutomaticPromotions();
	}

	private void loadAutomaticPromotions(){
		try {
			List promoList = FDPromotionManager.getAllAutomtaticPromotions();
			for (Iterator i = promoList.iterator(); i.hasNext();) {
				PromotionI promo = (PromotionI) i.next();
				Date promoModifyDate = promo.getModifyDate();
				Date now = new Date();
				if(this.maxLastModified == null || (this.maxLastModified.before(promoModifyDate) && !promoModifyDate.after(now))){
					this.maxLastModified = new Date(promoModifyDate.getTime());
				}
				this.promotionMap.put(promo.getPromotionCode(), promo);
			}
		} catch (FDResourceException ex) {
			ex.printStackTrace();
			throw new FDRuntimeException(ex);
		}
	}
	
	public static PromotionFactory getInstance() {
		return INSTANCE;
	}

	protected synchronized Map getAutomaticPromotionMap() {
		List promoList = (List) this.automaticpromotions.get();
		if(promoList.size() > 0){
			for (Iterator i = promoList.iterator(); i.hasNext();) {
				PromotionI promo = (PromotionI) i.next();
				Date promoModifyDate = promo.getModifyDate();
				Date now = new Date();
				if(this.maxLastModified == null  || (this.maxLastModified.before(promoModifyDate) && !promoModifyDate.after(now))){
					this.maxLastModified = new Date(promoModifyDate.getTime());
				}
				String promoId = promo.getPromotionCode();
				if(!promo.isRedemption()){
					//If the modified promo is a redemption ignore it.
					this.promotionMap.put(promoId, promo);	
				}else{
					//If the modified promo was a automatic before, after modification it is 
					//redemption then remove from the Map.
					if(this.promotionMap.containsKey(promoId)){
						this.promotionMap.remove(promoId);
					}
				}
				
			}
			promoList.clear();
		}
		return this.promotionMap;
	}
	
	public Collection getAllAutomaticPromotions() {
		return this.getAutomaticPromotionMap().values();
	}

	public Collection getAllAutomaticCodes() {
			//Returns Collection containing Automatic Promo codes -->
			return this.getAutomaticPromotionMap().keySet();
	}

	public PromotionI getPromotion(String promoId) {
		PromotionI promotion = null;
		if(getAllAutomaticCodes().contains(promoId)){
			/*
			 * This is an active/recently expired automatic promotion.
			 * In this case, Call getAutomaticPromotion(promoId)
			 */ 
			promotion = getAutomaticPromotion(promoId);
			
		}else {
			/* 
			 * It is a redemption promotion or a expired promotion that
			 * part of a past order. In this case,  
			 * Call getRedemptionPromotion(promoId) */
			promotion = getRedemptionPromotion(promoId);
		}
		return promotion;
	}
	
	/**
	 * 
	 * @param promoId
	 * @return
	 */
	public PromotionI getAutomaticPromotion(String promoId) {
		return (PromotionI) this.getAutomaticPromotionMap().get(promoId);
	}
	/**
	 * 
	 * @param promoId
	 * @return
	 */
	public PromotionI getRedemptionPromotion(String promoId) {
		PromotionI promotion = null;
		try{
			if(promoId == null){
				//This happens when promotion_popup page passes a null promotion code.
				return null;
			}
			promotion = (Promotion) getCache().get(promoId);
			
			if(promotion == null){
				LOGGER.info("REFRESHING REDEMPTION PROMOTION "+promoId);
				//The object has become stale or it's yet to be loaded into the cache.
				promotion = FDPromotionManager.getPromotionForRT(promoId);
				if(promotion != null){
					//Promotion can be null if the promotion has a incomplete configuration.
					getCache().put(promoId, promotion);	
				}
				LOGGER.info("REFRESHING REDEMPTION PROMOTION DONE.");
			}
		}catch (FDResourceException ex) {
			LOGGER.error("Exception Occurred while getting Redemption Promotion "+promoId, ex);
			throw new FDRuntimeException(ex);
		}
		return promotion;
	}
	

	/**
	 * Thie method returns a set of Automatic Promotion codes that matches
	 * with the EnumPromotionType passed as an arguement. 
	 * TODO this method in future need to modified to get all promotions from DB
	 * if used for searching redemption promotions as well for the matching 
	 * EnumPromotionType.
	 * @param type
	 * @return
	 * @throws FDResourceException
	 */
	public Set getPromotionCodesByType(EnumPromotionType type) {
		Set s = new HashSet();
		for (Iterator i = this.getAllAutomaticPromotions().iterator(); i.hasNext();) {
			Promotion promo = (Promotion) i.next();
			if (promo.getPromotionType().equals(type)) {
				s.add(promo.getPromotionCode());
			}
		}
		return s;
	}

	public void forceRefresh(String promoId) {
		automaticpromotions.forceRefresh();
		if(getCache().get(promoId) != null){
			/*
			 * The updated promotion is a redemption promotion.So Remove the promotion 
			 * object to reload during next request.
			 */
			getCache().remove(promoId);
		}
	}

	private CacheI getCache() {
		return this.cache;
	}
	
	private CacheI constructCache(){
		SimpleLruCache lruCache = new SimpleLruCache();
		lruCache.setName("PROMOTION");
		lruCache.setCapacity(ErpServicesProperties.getPromotionRTSizeLimit());
		lruCache.setTimeout(FDStoreProperties.getPromotionRTRefreshPeriod());
		return lruCache;
	}
}
