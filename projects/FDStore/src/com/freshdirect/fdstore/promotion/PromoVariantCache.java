package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.promotion.management.FDPromotionManager;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.cache.CacheI;
import com.freshdirect.framework.cache.ManagedCache;
import com.freshdirect.framework.cache.SimpleLruCache;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
/**
 * This class caches all active promo variants in a linked hash map(variantId -> List of PromoVariantModel). Refreshes the map every 30 minutes.
 * @author skrishnasamy
 *
 */
public class PromoVariantCache {

	private final static PromoVariantCache INSTANCE = new PromoVariantCache();
	private static Category LOGGER = LoggerFactory.getInstance(PromoVariantCache.class);
	
	public final static Comparator PRIORITY_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {

			PromoVariantModel pv1 = (PromoVariantModel) o1;
			PromoVariantModel pv2 = (PromoVariantModel) o2;
			return new Integer(pv2.getPromoPriority()).compareTo(new Integer(pv1.getPromoPriority()));
		}
	};
	
	public final static Comparator FEATURE_PRIORITY_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {

			PromoVariantModel pv1 = (PromoVariantModel) o1;
			PromoVariantModel pv2 = (PromoVariantModel) o2;
			return new Integer(pv2.getVariantPriority()).compareTo(new Integer(pv1.getVariantPriority()));
		}
	};
	
	private ExpiringReference activePromoVariants = new ExpiringReference(60 * 60 * 1000) {
		protected Object load() {
			try {
				List promoVariants = FDPromotionManager.getAllActivePromoVariants(EnumSiteFeature.getSmartSavingsFeatureList());
				String prevVariantId = "";
				Map pvMap = new HashMap();
				List valueList = null;
				for(Iterator iter = promoVariants.iterator(); iter.hasNext();) {
					PromoVariantModel pv = (PromoVariantModel) iter.next();
					String variantId = pv.getVariantId();
					if(!variantId.equals(prevVariantId)){
						valueList = new ArrayList();
						pvMap.put(variantId, valueList);
						prevVariantId = variantId;
					}
					valueList.add(pv);
					Collections.sort(valueList, PRIORITY_COMPARATOR);
				}
				return pvMap;
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
	};

	private PromoVariantCache() {
		
	}
			

	public static PromoVariantCache getInstance() {
		return INSTANCE;
	}
	
	
	public void refreshAll(){
		this.activePromoVariants.forceRefresh();
		// just to confirm this reloads all the cached data
		//this.activePromoVariants.get();		
	}
	

	public  Map getPromoVariantMap() {
		return (Map) this.activePromoVariants.get();
	}
	
	public Collection getPromoVariantIds() {
			//Returns Collection containing variant Ids that are attached to one or more Promo codes.
			return this.getPromoVariantMap().keySet();
	}

	/**
	 * 
	 * @param variantId
	 * @return a collection of PromoVariantModel attached to the given variant.
	 */
	public List getAllPromoVariants(String variantId) {
		List pvlist = (List) getPromoVariantMap().get(variantId);
		if(pvlist != null)
			return Collections.unmodifiableList(pvlist);
		return Collections.EMPTY_LIST;
	}

}
