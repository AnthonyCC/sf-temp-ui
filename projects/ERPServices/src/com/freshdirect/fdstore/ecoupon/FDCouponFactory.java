package com.freshdirect.fdstore.ecoupon;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Category;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponUPCInfo;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDCouponFactory {

	private static Category LOGGER = LoggerFactory.getInstance( FDCouponFactory.class );
	
	private final static Object lock = new Object();
	
	private static FDCouponFactory INSTANCE = null;
	private Date lastRefreshed;
	
	public static FDCouponFactory getInstance() {
		synchronized(lock) {
			if (INSTANCE == null) {
				INSTANCE = new FDCouponFactory();
			}
		}
		return INSTANCE;
	}
	private Map<String, FDCouponInfo> fdCouponMap = new ConcurrentHashMap<String, FDCouponInfo>();//CouponId,CouponInfo map.
	private Map<String, Set<FDCouponUPCInfo>> fdUpcCouponMap = new LinkedHashMap<String, Set<FDCouponUPCInfo>>();//Upc,CouponId map.
	private Map<String, Set<FDCouponUPCInfo>> upcCouponMap = new LinkedHashMap<String, Set<FDCouponUPCInfo>>();//Upc,CouponId map.
	private ExpiringReference<List<FDCouponInfo>> coupons = new ExpiringReference<List<FDCouponInfo>>(FDCouponProperties.getCouponCacheRefreshPeriod()* 60 * 1000) {

		@Override
		protected List<FDCouponInfo> load() {
			try {
				LOGGER.info("REFRESHING COUPON MAPS FOR ANY NEW COUPONS FROM LAST REFRESHED TIME "+lastRefreshed);
				List<FDCouponInfo> coupons = new ArrayList<FDCouponInfo>();
				Date prevRefreshed =lastRefreshed;
				lastRefreshed=new Date();				
				if(null !=prevRefreshed && !fdCouponMap.isEmpty() && !fdUpcCouponMap.isEmpty()){
//					loadAllCoupons();
					coupons = FDCouponManager.getActiveCoupons(prevRefreshed);
				}else{
					coupons = loadAllCoupons();
				}
				LOGGER.info("REFRESHED COUPON MAPS FOR ANY NEW COUPONS. FOUND "+(null!=coupons?coupons.size():coupons));
				return coupons;
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
		
	};
	
	
	private List<FDCouponInfo> loadAllCoupons(){
		List<FDCouponInfo> coupons = new ArrayList<FDCouponInfo>();
		try {
			coupons = FDCouponManager.getActiveCoupons();
			if(null !=coupons){
				updateCaches(coupons);
			}
		} catch (FDResourceException ex) {
			LOGGER.error("Failed to load coupons ", ex);
		}
		return coupons;
	}

	public  Map<String, FDCouponInfo> getCouponMap() {
		updateCaches();
		return this.fdCouponMap;
	}

	public  Map<String, Set<FDCouponUPCInfo>> getUpcCouponMap() {
		updateCaches();
		return this.upcCouponMap;
	}
	
	public synchronized Map<String, Set<FDCouponUPCInfo>> getFdUpcCouponMap() {
		if(!fdCouponMap.isEmpty() && fdUpcCouponMap.isEmpty()){
			loadAllCoupons();
		}else{
			updateCaches();
		}
		return this.fdUpcCouponMap;
	}
	
	private synchronized void updateCaches() {
		List<FDCouponInfo> coupons = this.coupons.get();
		if(null !=coupons ){
			updateCaches(coupons);
		}
	}
	
	private void updateCaches(List<FDCouponInfo> coupons) {
		for (FDCouponInfo coupon : coupons ) {						
			this.fdCouponMap.put(coupon.getCouponId(), coupon);
			if(null !=coupon.getRequiredUpcs()){
				for (Iterator iterator = coupon.getRequiredUpcs().iterator(); iterator
						.hasNext();) {
					FDCouponUPCInfo fdCouponUpcInfo = (FDCouponUPCInfo) iterator.next();
					Set<FDCouponUPCInfo> couponUpcList =upcCouponMap.get(fdCouponUpcInfo.getUpc());
					if(null==couponUpcList){
						couponUpcList = new TreeSet<FDCouponUPCInfo>();
						upcCouponMap.put(fdCouponUpcInfo.getUpc(),couponUpcList);
					}
					couponUpcList.add(fdCouponUpcInfo);
					
					String couponUPC = StringUtil.convertToUPCA(fdCouponUpcInfo.getUpc());//EAN13 to UPCA conversion
					Set<FDCouponUPCInfo> fdCouponUpcList =fdUpcCouponMap.get(couponUPC);
					FDProductInfo cachedProductInfo = FDCachedFactory.getProductInfoByUpc(couponUPC);
					if(null !=cachedProductInfo) {
						if(null==fdCouponUpcList){							
							fdCouponUpcList = new TreeSet<FDCouponUPCInfo>();
							fdUpcCouponMap.put(couponUPC,fdCouponUpcList);						
						}
						fdCouponUpcList.add(fdCouponUpcInfo);
					}
				}
			}					
		}
	}
	
	public FDCouponInfo getCoupon(String couponId){
		return getCouponMap().get(couponId);
	}
	
	public FDCouponInfo getCouponByUpc(String upc){
		upc=StringUtil.convertToEan13(upc);
		FDCouponInfo fdCouponInfo = null;
		Set<FDCouponUPCInfo> set =getUpcCouponMap().get(upc);
		if(null !=set && !set.isEmpty()){
			fdCouponInfo = fdCouponMap.get(set.iterator().next().getCouponId());
		}
		return fdCouponInfo;
	}
	
	public List<FDCouponInfo> getCouponsByUpc(String upc){
		List<FDCouponInfo> fdCouponInfoList = new ArrayList<FDCouponInfo>();
		upc=StringUtil.convertToEan13(upc);
		Set<FDCouponUPCInfo> set =getUpcCouponMap().get(upc);
		if(null !=set){
			for (Iterator<FDCouponUPCInfo> iterator = set.iterator(); iterator.hasNext();) {
				FDCouponUPCInfo fdCouponUPCInfo = (FDCouponUPCInfo) iterator.next();
				FDCouponInfo fdCouponInfo = fdCouponMap.get(fdCouponUPCInfo.getCouponId());
				fdCouponInfoList.add(fdCouponInfo);
			}			
		}
		return fdCouponInfoList;
	}
	
	public List<FDCouponInfo> getCoupons() {
		Map<String, FDCouponInfo> fdCouponMap =		getCouponMap();
		List<FDCouponInfo> coupons = new ArrayList<FDCouponInfo>();
		coupons.addAll(fdCouponMap.values());
		return coupons;
	}
	
	public void forceRefresh() {
		coupons.forceRefresh();
	}
}
