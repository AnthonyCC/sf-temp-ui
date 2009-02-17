package com.freshdirect.webapp.util;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.event.FDAddToCartEvent;
import com.freshdirect.event.FDCartLineEvent;
import com.freshdirect.event.FDEditCartEvent;
import com.freshdirect.event.FDRemoveCartEvent;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.lists.CclUtils;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * @author knadeem Date May 18, 2005
 */
public class FDEventFactory {
	
	private final static String FD_ADD_TO_CART_EVENT = "AddToCart";
	
	private final static String FD_MODIFY_CART_EVENT = "ModifyCart";
	
	private final static String FD_REMOVE_CART_EVENT = "RemoveFromCart";

	/**
	 * This method creates and returns a new FDAddToCartEvent. 
	 * @param cartline
	 * @param request
	 * @return
	 */
	public static FDAddToCartEvent getFDAddToCartEvent (FDCartLineI cartline, HttpServletRequest request) {
		FDAddToCartEvent event = new FDAddToCartEvent();
		populateEvent(event, request, FD_ADD_TO_CART_EVENT);
		setParamsToEvent(cartline, event,request.getParameter(CclUtils.CC_LIST_ID));		
		return event;
	}

	/**
	 * This method creates and returns a new FDEditCartEvent.
	 * @param cartline
	 * @param request
	 * @return
	 */ 	
	public static FDEditCartEvent getFDEditCartEvent (FDCartLineI cartline, HttpServletRequest request) {
		FDEditCartEvent event = new FDEditCartEvent();
		populateEvent(event, request, FD_MODIFY_CART_EVENT);
		setParamsToEvent(cartline, event, request.getParameter(CclUtils.CC_LIST_ID));
		return event;
	}


	/**
	 * This method creates and returns a new FDRemoveCartEvent.
	 * @param cartline
	 * @param request
	 * @return
	 */ 	
	public static FDRemoveCartEvent getFDRemoveCartEvent (FDCartLineI cartline, HttpServletRequest request) {
		FDRemoveCartEvent event = new FDRemoveCartEvent();
		populateEvent(event, request, FD_REMOVE_CART_EVENT);
		setParamsToEvent(cartline, event, request.getParameter(CclUtils.CC_LIST_ID));
		return event;
	}
	
	
	public static FDRecommendationEvent getImpressionEvent(String variantId, ContentKey contentKey) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND,0);
		
		StringBuffer idBuffer = new StringBuffer(contentKey.getId().length() + 12);
		idBuffer.append(contentKey.getType()).append(':').append(contentKey.getId());
		
		return new FDRecommendationEvent.Impression(variantId,idBuffer.toString(),cal.getTime());
	}
	
	public static FDRecommendationEvent getClickThroughEvent(String variantId, ContentKey contentKey) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND,0);
		
		StringBuffer idBuffer = new StringBuffer(contentKey.getId().length() + 12);
		idBuffer.append(contentKey.getType()).append(':').append(contentKey.getId());
		
		return new FDRecommendationEvent.ClickThrough(variantId,idBuffer.toString(),cal.getTime());
	}
	
	private static void populateEvent (FDWebEvent event, HttpServletRequest request, String eventType) {
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		FDIdentity identity = user.getIdentity();
		if(identity != null) {
			event.setCustomerId(identity.getErpCustomerPK());
		}
		
		event.setCookie(user.getCookie());
		event.setEventType(eventType);
		event.setTimestamp(new Date());
		if (FD_ADD_TO_CART_EVENT.equalsIgnoreCase(event.getEventType())) {
			event.setTrackingCode(request.getParameter("trk"));

			if (request.getAttribute("atc_suffix") != null) {
				// special case: ATC is generated during adding multiple items to cart
				String suffix = (String) request.getAttribute("atc_suffix");

				String trkd = request.getParameter("trkd" + suffix);
				if (trkd != null)
					event.setTrackingCodeEx(trkd);
			} else {
				event.setTrackingCodeEx(request.getParameter("trkd"));
			}
		}
		event.setUrl(request.getRequestURI());
		event.setQueryString(request.getQueryString());
		
		EnumTransactionSource src = user.getApplication();
		event.setApplication(src != null ? src.getCode() : EnumTransactionSource.WEBSITE.getCode());
		event.setServer(request.getServerName());
	}

	private static void setParamsToEvent(FDCartLineI cartline, FDCartLineEvent event, String cclId) {
		CategoryModel category = cartline.getProductRef().lookupCategory();
		event.setCategoryId(add(cartline.getCategoryName()));//Param 3
		event.setCartlineId(add(cartline.getCartlineId())); //Param 5
		event.setDepartment(add(category.getDepartment().getFullName())); //Param 4
		event.setSkuCode(add(cartline.getSkuCode())); //Param 2
		event.setProductId(add(cartline.getProductName())); //Param 1
		event.setQuantity(add(String.valueOf(cartline.getQuantity()))); //Param 6
		event.setSalesUnit(add(cartline.getSalesUnit())); //Param 7
		event.setConfiguration(cartline.getConfigurationDesc() != null ? add(cartline.getConfigurationDesc()) : ""); //Param 8
		event.setSource(cartline.getSource());
		event.setYmalCategory(cartline.getYmalCategoryId()); // Param 9
		event.setOriginatingProduct(cartline.getOriginatingProductId()); // Param 10
		event.setYmalSet(cartline.getYmalSetId()); // Param 11
		event.setCclId(cclId); // Param 12

		// [MNT-382] log variant ID when AddToCart event is logged
		if (FD_ADD_TO_CART_EVENT.equalsIgnoreCase(event.getEventType())) {
			String variantId = cartline.getVariantId();
			event.setVariantId(variantId);
			if (variantId != null) {
				// [APPREQ-352] ATC events that come with variant must have 'SS' source
				event.setSource(EnumEventSource.SmartStore);
			}
		}
	}

	private static String add(String value){
		if(value != null && value.length() > 55){
			return value.substring(0, 55);
		}
		return value;
	}
}
