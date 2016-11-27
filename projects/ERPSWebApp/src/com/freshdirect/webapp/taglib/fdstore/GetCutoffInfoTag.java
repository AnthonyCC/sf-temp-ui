package com.freshdirect.webapp.taglib.fdstore;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDZoneCutoffInfo;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.logistics.delivery.model.DlvZoneCapacityInfo;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetCutoffInfoTag extends AbstractGetterTag {

	/* don't display post-cutoff warning for depot users on these days */
	private final static int[] SPECIAL_DAYS = {Calendar.FRIDAY, Calendar.SATURDAY};

	private boolean isSpecialDay(Calendar today) {
		int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
		for (int i = 0; i < SPECIAL_DAYS.length; i++) {
			if (dayOfWeek == SPECIAL_DAYS[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @throws FDResourceException
	 * @throws FDInvalidAddressException
	 * @see com.freshdirect.webapp.taglib.AbstractGetterTag#getResult()
	 */
	protected Object getResult() throws FDResourceException, FDInvalidAddressException {

		FDUserI user = (FDUserI) this.pageContext.getSession().getAttribute(SessionName.USER);
		
		if (user.isDepotUser() && isSpecialDay(Calendar.getInstance())) {
			return null;
		}

		return this.getCutoffInfo(new TimeOfDay(new Date()), user);

	}

	protected CutoffInfo getCutoffInfo(TimeOfDay today, FDUserI user) throws FDResourceException, FDInvalidAddressException {
		FDCartModel cart = user.getShoppingCart();
		
		ErpAddressModel address = cart.getDeliveryAddress();

		if (address == null) {
			//try to get defaultAddress from user
			FDIdentity identity = user.getIdentity();

			if (identity != null) {
				String defaultAddressPK = NVL.apply(FDCustomerManager.getDefaultShipToAddressPK(identity), "");

				if (!"".equals(defaultAddressPK)) {
					address = FDCustomerManager.getShipToAddress(identity, defaultAddressPK);
				}
			}
		}

		Date tomorrow = DateUtil.addDays(new Date(), 1);

		FDDeliveryZoneInfo zoneInfo = cart.getZoneInfo();

		if (zoneInfo == null && address != null) {
			zoneInfo = FDDeliveryManager.getInstance().getZoneInfo(address, tomorrow, user.getHistoricOrderSize(),  null, (user.getIdentity()!=null)?user.getIdentity().getErpCustomerPK():null);
		}

		String zoneCode = zoneInfo != null ? zoneInfo.getZoneCode() : FDStoreProperties.getCutoffDefaultZoneCode();

		if (zoneCode != null && !"".equals(zoneCode)) {

			FDDeliveryManager dlvMgr = FDDeliveryManager.getInstance();
			DlvZoneCapacityInfo capacity = dlvMgr.getZoneCapacity(zoneCode, tomorrow);
			
			List cutoffTimes = dlvMgr.getCutofftimeForZone(zoneCode, tomorrow);
			
			if(cutoffTimes.isEmpty()) {
				return null;
			}
			
			FDZoneCutoffInfo nextCutoff = this.getNextCutoff(cutoffTimes, today);
			
			if(nextCutoff == null) {
				nextCutoff = this.getLastElapsedCutoff(cutoffTimes, today);
				if(nextCutoff == null) {
					return null;
				}
			}

			CutoffInfo cutoffInfo = new CutoffInfo(zoneCode, tomorrow);
			
			if (address != null) {
				cutoffInfo.setZipCode(address.getZipCode());
			}
			
			TimeOfDay cutoffTime = nextCutoff.getCutoffTime();
			cutoffInfo.setHasMultipleCutoff(cutoffTimes.size() > 1 ? true : false);
			cutoffInfo.setCutoffTime(nextCutoff.getCutoffTime());
			cutoffInfo.setNextCutoff(cutoffTime);
			cutoffInfo.setNextEarliestTimeslot(nextCutoff.getStartTime());
			cutoffInfo.setNextLatestTimeslot(nextCutoff.getEndTime());
			
			FDZoneCutoffInfo lastCutoff = this.getLastCutoff(cutoffTimes);
			cutoffInfo.setLastCutoff(lastCutoff.getCutoffTime());
			
			if(capacity.getRemainingCapacity() <= 0 && cutoffInfo.displayWarning()) {
				return null;
			}

			return cutoffInfo;
		}
		return null;
	}
	
	private FDZoneCutoffInfo getNextCutoff(List cInfos, TimeOfDay now) {
		Collections.sort(cInfos, new Comparator<FDZoneCutoffInfo>() {

			public int compare(FDZoneCutoffInfo o1, FDZoneCutoffInfo o2) {
				return o1.getCutoffTime().compareTo(o2.getCutoffTime());
			}
	    });
		
		for(Iterator i = cInfos.iterator(); i.hasNext(); ){
			FDZoneCutoffInfo info = (FDZoneCutoffInfo) i.next();
			if(info.getCutoffTime().after(now)){
				return info;
			}
		}
		
		return null;
	}

	private FDZoneCutoffInfo getLastCutoff(List cInfos) {
		
		TimeOfDay last = new TimeOfDay(new Date());
		FDZoneCutoffInfo lastInfo = null; 
		
		for(Iterator i = cInfos.iterator(); i.hasNext(); ){
			FDZoneCutoffInfo info = (FDZoneCutoffInfo) i.next();
			if(info.getCutoffTime().after(last) || lastInfo == null){
				last = info.getCutoffTime();
				lastInfo = info;
			}
		}
		
		return lastInfo;
	}
	
	private FDZoneCutoffInfo getLastElapsedCutoff(List cInfos, TimeOfDay now) {
		FDZoneCutoffInfo info = null;
		for(Iterator i = cInfos.iterator(); i.hasNext(); ) {
			FDZoneCutoffInfo tmpInfo = (FDZoneCutoffInfo) i.next();
			if(info == null || (tmpInfo.getCutoffTime().after(info.getCutoffTime()) && tmpInfo.getCutoffTime().before(now))){
				info = tmpInfo;
			}
		}
		return info;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.webapp.taglib.fdstore.CutoffInfo";
		}
	}

}
