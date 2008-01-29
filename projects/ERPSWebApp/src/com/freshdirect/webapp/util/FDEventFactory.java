package com.freshdirect.webapp.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.event.BookRetailerRedirectEvent;
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
import com.freshdirect.framework.event.FDEvent;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * @author knadeem Date May 18, 2005
 */
public class FDEventFactory {
	
	private final static String FD_ADD_TO_CART_EVENT = "AddToCart";
	
	private final static String FD_MODIFY_CART_EVENT = "ModifyCart";
	
	private final static String FD_REMOVE_CART_EVENT = "RemoveFromCart";
	
	private final static String BOOK_RETAILER_REDIRECT_EVENT = "BookRetailerRedirect";

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
	
	public static BookRetailerRedirectEvent getBookRetailerRedirectEvent(HttpServletRequest request) {
		BookRetailerRedirectEvent event = new BookRetailerRedirectEvent();
		populateEvent(event, request, BOOK_RETAILER_REDIRECT_EVENT);
		if (event.getSource() == null) event.setSource(EnumEventSource.UNKNOWN);
		event.setBookRetailerId(request.getParameter("bookRetailerId"));
		event.setRecipeSourceId(request.getParameter("recipeSourceId"));
		return event;
	}
	
	private static void populateEvent (FDEvent event, HttpServletRequest request, String eventType) {
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		FDIdentity identity = user.getIdentity();
		if(identity != null) {
			event.setCustomerId(identity.getErpCustomerPK());
		}
		
		event.setCookie(user.getCookie());
		event.setEventType(eventType);
		event.setTimestamp(new Date());
		event.setTrackingCode(user.getLastTrackingCode());
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
	}
	
	private static String add(String value){
		if(value != null && value.length() > 55){
			return value.substring(0, 55);
		}
		return value;
	}
}
