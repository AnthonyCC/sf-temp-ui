package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshdirect.webapp.features.service.FeaturesService;

/**
 *	Simple java bean for add to cart requests. 
 *	Class structure is representing the received JSON structure. 	
 * 
 * @author treer
 */
public class AddToCartRequestData implements Serializable {
	
	private static final long	serialVersionUID	= -4196044006826787164L;
	
	private List<AddToCartItem> items;
	private String orderId;
	private boolean newOrder;
	private String eventSource;
	private String siteFeature;
	private String variantId;
	private String tab;
	
	// coremetrics extra fields
	private String coremetricsPageContentHierarchy;
	private String coremetricsPageId;
	private String coremetricsVirtualCategory;
	
	private boolean ignoreRedirect;
	
	/**
	 * Temporary place of request cookies
	 * Some services require this like {@link FeaturesService}
	 * 
	 * @see HttpServletRequest#getCookies()
	 */
	@JsonIgnore
	private Cookie[] cookies;

	/**
	 * The URL the request came from. Required for reporting
	 */
	@JsonIgnore
	private String requestUrl;
	
	public List<AddToCartItem> getItems() {
		return items;
	}	
	public void setItems( List<AddToCartItem> items ) {
		this.items = items;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public boolean isNewOrder() {
		return newOrder;
	}
	public void setNewOrder(boolean newOrder) {
		this.newOrder = newOrder;
	}	
	public String getEventSource() {
		return eventSource;
	}	
	public void setEventSource( String eventSource ) {
		this.eventSource = eventSource;
	}	
	public String getVariantId() {
		return variantId;
	}	
	public void setVariantId( String variantId ) {
		this.variantId = variantId;
	}	
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}	
	public String getSiteFeature() {
		return siteFeature;
	}	
	public void setSiteFeature( String siteFeature ) {
		this.siteFeature = siteFeature;
	}	
	public String getCoremetricsPageContentHierarchy() {
		return coremetricsPageContentHierarchy;
	}	
	public void setCoremetricsPageContentHierarchy( String coremetricsPageContentHierarchy ) {
		this.coremetricsPageContentHierarchy = coremetricsPageContentHierarchy;
	}	
	public String getCoremetricsPageId() {
		return coremetricsPageId;
	}	
	public void setCoremetricsPageId( String coremetricsPageId ) {
		this.coremetricsPageId = coremetricsPageId;
	}
	public String getCoremetricsVirtualCategory() {
		return coremetricsVirtualCategory;
	}
	public void setCoremetricsVirtualCategory(String coremetricsVirtualCategory) {
		this.coremetricsVirtualCategory = coremetricsVirtualCategory;
	}
	public boolean isIgnoreRedirect() {
		return ignoreRedirect;
	}
	public void setIgnoreRedirect(boolean ignoreRedirect) {
		this.ignoreRedirect = ignoreRedirect;
	}
	
    public Cookie[] getCookies() {
        return cookies;
    }
    
    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }

    public String getRequestUrl() {
        return requestUrl;
    }
    
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
