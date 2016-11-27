package com.freshdirect.webapp.util;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


/**
 * 
 * @author segabor
 *
 */
public class ShoppingCartUtil {
	/**
	 * Restore customer's original cart
	 * 
	 * @param session
	 * @param user
	 * @throws FDAuthenticationException
	 * @throws FDResourceException
	 */
	private final static Logger LOGGER = LoggerFactory.getInstance(ShoppingCartUtil.class);
	
	public static void restoreCart(HttpSession session) throws FDAuthenticationException, FDResourceException {
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		
		FDCartModel originalCart = FDCustomerManager.getSavedCart(user.getIdentity(), user.getUserContext().getStoreContext().getEStoreId());//FDCustomerManager.recognize(user.getIdentity()).getShoppingCart();

		user.setShoppingCart( originalCart );
		user.invalidateCache();


		session.setAttribute( SessionName.USER, user );
		
		//Evaluate coupons for the restored cart.
		FDCustomerCouponUtil.evaluateCartAndCoupons(session);

		//The previous recommendations of the current user need to be removed.
        session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
	}
	
	public static Double getSubTotal(FDCartI cart) {
		try{
			evaluateCart(cart);
			return cart.getSubTotal();
		}catch(Exception e){
			LOGGER.error("evaluateCart(FDCartModel cart):" + e);
		}
		return null;
	}
		
	
	private static void evaluateCart(FDCartI cart){
	
		try{
			Map<String,FDAvailabilityInfo> unavMap = cart.getUnavailabilityMap();
			for (Iterator<Entry<String,FDAvailabilityInfo>> i = unavMap.entrySet().iterator(); i.hasNext();) {
				Entry<String,FDAvailabilityInfo> entry = i.next();
				String key = entry.getKey();
				FDAvailabilityInfo info = entry.getValue();
				FDCartLineI cartline = cart.getOrderLineById(Integer.parseInt(key));
				int cartIndex = cart.getOrderLineIndex(Integer.parseInt(key));
				if (info instanceof FDStockAvailabilityInfo) {
					FDStockAvailabilityInfo sInfo = (FDStockAvailabilityInfo) info;
	
					if (sInfo.getQuantity() == 0) {
						// remove
						cart.removeOrderLine(cartIndex);
						// Create FD remove cart event.
						//FDEventUtil.logRemoveCartEvent(cartline, request);
	
					} else {
						cartline.setQuantity(sInfo.getQuantity());
						// Create FD Modify cart event.
						//FDEventUtil.logEditCartEvent(cartline, request);
					}
	
				} else {
					// remove
					cart.removeOrderLine(cartIndex);
					//FDEventUtil.logRemoveCartEvent(cartline, request);
				}
			}
			//Remove unavailable delivery passes.
			List<DlvPassAvailabilityInfo> unavailPasses = cart.getUnavailablePasses();
			if(unavailPasses != null && unavailPasses.size() > 0){
				for (Iterator<DlvPassAvailabilityInfo> i = unavailPasses.iterator(); i.hasNext();) {
					DlvPassAvailabilityInfo info = i.next();
					Integer key = info.getKey();
					//FDCartLineI cartline = cart.getOrderLineById(key.intValue());
					int cartIndex = cart.getOrderLineIndex(key.intValue());
					cart.removeOrderLine(cartIndex);
					//FDEventUtil.logRemoveCartEvent(cartline, request);
				}
				unavailPasses.clear();
			}
				cart.refreshAll(true);
		}catch(Exception e){
			LOGGER.error("getSubTotal(FDCartModel cart):" + e);
		}
	}
	
	public static Date getCutoffByContext(Date cutoffTime, FDUserI user){
		if(user.getUserContext()!=null 
				&& user.getUserContext().getStoreContext()!=null 
				&& user.getUserContext().getStoreContext().getEStoreId().equals(EnumEStoreId.FDX)
				&& (user.getShoppingCart() instanceof FDModifyCartModel)){
			return user.getShoppingCart().getModificationCutoffTime();
		}
		return cutoffTime;
	}
}