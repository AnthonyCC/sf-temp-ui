package com.freshdirect.webapp.taglib.fdstore;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.delivery.DlvZoneCapacityInfo;
import com.freshdirect.delivery.DlvZoneCutoffInfo;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.customer.ErpAddressModel;

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

		DlvZoneInfoModel zoneInfo = cart.getZoneInfo();

		if (zoneInfo == null && address != null) {
			zoneInfo = FDDeliveryManager.getInstance().getZoneInfo(address, tomorrow);
		}

		String zoneCode = zoneInfo != null ? zoneInfo.getZoneCode() : FDStoreProperties.getCutoffDefaultZoneCode();

		if (zoneCode != null && !"".equals(zoneCode)) {

			FDDeliveryManager dlvMgr = FDDeliveryManager.getInstance();
			DlvZoneCapacityInfo capacity = dlvMgr.getZoneCapacity(zoneCode, tomorrow);
			
			List cutoffTimes = dlvMgr.getCutofftimeForZone(zoneCode, tomorrow);
			
			if(cutoffTimes.isEmpty()) {
				return null;
			}
			
			DlvZoneCutoffInfo nextCutoff = this.getNextCutoff(cutoffTimes, today);
			
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
			
			DlvZoneCutoffInfo lastCutoff = (DlvZoneCutoffInfo) cutoffTimes.get(cutoffTimes.size() - 1);
			cutoffInfo.setLastCutoff(lastCutoff.getCutoffTime());
			
			if(capacity.getRemainingCapacity() <= 0 && cutoffInfo.displayWarning()) {
				return null;
			}

			return cutoffInfo;
		}
		return null;
	}
	
	private DlvZoneCutoffInfo getNextCutoff(List cInfos, TimeOfDay now) {
		
		for(Iterator i = cInfos.iterator(); i.hasNext(); ){
			DlvZoneCutoffInfo info = (DlvZoneCutoffInfo) i.next();
			if(info.getCutoffTime().after(now)){
				return info;
			}
		}
		
		return null;
	}
	
	private DlvZoneCutoffInfo getLastElapsedCutoff(List cInfos, TimeOfDay now) {
		DlvZoneCutoffInfo info = null;
		for(Iterator i = cInfos.iterator(); i.hasNext(); ) {
			DlvZoneCutoffInfo tmpInfo = (DlvZoneCutoffInfo) i.next();
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
