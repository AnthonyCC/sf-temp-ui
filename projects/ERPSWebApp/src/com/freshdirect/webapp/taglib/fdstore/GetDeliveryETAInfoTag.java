package com.freshdirect.webapp.taglib.fdstore;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdlogistics.model.FDDeliveryETAModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

@SuppressWarnings("serial")
public class GetDeliveryETAInfoTag extends AbstractGetterTag {
	
	private final static Category LOGGER = LoggerFactory.getInstance(GetDeliveryETAInfoTag.class);
	/**
	 * @throws FDResourceException
	 * @see com.freshdirect.webapp.taglib.AbstractGetterTag#getResult()
	 */
	protected Object getResult() throws FDResourceException {

		FDUserI user = (FDUserI) this.pageContext.getSession().getAttribute(SessionName.USER);

		return this.getDeliveryETAInfo(user);

	}

protected DeliveryETAInfo getDeliveryETAInfo(FDUserI user) throws FDResourceException {
		
		if (user != null && user.getLevel() >= FDUserI.RECOGNIZED) {
			
			FDDeliveryManager dlvMgr = FDDeliveryManager.getInstance();
			
			FDOrderInfoI earlyScheduledDlvOrderInfo = null;
			FDDeliveryETAModel earlyScheduledDlvOrderETAInfo = null;
			
			List<FDOrderInfoI> scheduledOrderInfos = user.getScheduledOrdersForDelivery(true);
			//APP DEV 4132 Change start
			boolean isETAEnabled = false;
			if(scheduledOrderInfos != null && scheduledOrderInfos.size() > 0) {
				for(Iterator<FDOrderInfoI> i = scheduledOrderInfos.iterator(); i.hasNext(); ){
					earlyScheduledDlvOrderInfo = (FDOrderInfoI) i.next();
					if(DateUtil.getCurrentTime().before(earlyScheduledDlvOrderInfo.getDeliveryEndTime())) {
						earlyScheduledDlvOrderETAInfo = dlvMgr.getETAWindowBySaleId(earlyScheduledDlvOrderInfo.getErpSalesId());
						if(earlyScheduledDlvOrderETAInfo != null 
								&& (earlyScheduledDlvOrderETAInfo.isEmailETAenabled() || earlyScheduledDlvOrderETAInfo.isSmsETAenabled())) {
							isETAEnabled = true;
							break;
						} else {
							continue;
						}
					}
				}

				if(earlyScheduledDlvOrderInfo != null && earlyScheduledDlvOrderETAInfo != null && isETAEnabled){
					DeliveryETAInfo deliveryETAInfo = new DeliveryETAInfo(earlyScheduledDlvOrderInfo, earlyScheduledDlvOrderETAInfo);
					deliveryETAInfo.setHasMultipleScheduledOrders(scheduledOrderInfos.size() > 1 ? true : false);
					LOGGER.debug("User's ETA window warning info: " + deliveryETAInfo.toString());
					return deliveryETAInfo;
				}
			}
		}
		return null;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.webapp.taglib.fdstore.DeliveryETAInfo";
		}
	}

}