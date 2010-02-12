/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2003 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ProxyProduct;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.StarterList;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.util.QuickCartCache;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class QuickShopControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3424186319116152768L;



	private static Category LOGGER = LoggerFactory.getInstance(QuickShopControllerTag.class);
	
	

	private String action;
	private String orderId = null;
	//CCL
	private String ccListId=null; 
	
	private String starterListId = null;

	public void setAction(String a) {
		this.action = a;
	}

	public void setOrderId(String oid) {
		this.orderId = oid;
	}
	
	public void setCcListId(String lid) {
		this.ccListId = lid;
	}
	
	public void setStarterListId(String starterListId) {
		this.starterListId = starterListId;
	}
	

	public int doStartTag() throws JspException {

		try {

			HttpSession session = pageContext.getSession();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			FDUserI user = (FDUserI) session.getAttribute(USER);

			QuickCart quickCart = QuickCartCache.getCachedInstance(session);
			//
			// if we're trying to load it and we don't already have it loaded up,
			// load it
			//

			if (((this.orderId != null) && (quickCart == null))
				|| ((this.orderId != null) && (quickCart != null) && !this.orderId.equalsIgnoreCase(quickCart.getOrderId()))) {

				quickCart = new QuickCart();
				quickCart.setUserZoneId(user.getPricingZoneId());
				quickCart.setOrderId(this.orderId);
				quickCart.setProductType(QuickCart.PRODUCT_TYPE_PRD);
				

				if ("every".equalsIgnoreCase(this.orderId)) {
					List originalLines = FDListManager.getEveryItemEverOrdered(user.getIdentity());
					quickCart.setDeliveryDate(new Date());
					quickCart.setProducts(originalLines);
				} else if (this.orderId != null && !"".equals(this.orderId)) {
					FDOrderI order = FDCustomerManager.getOrder(user.getIdentity(), this.orderId);
					List originalLines = order.getOrderLines();
					/*
					if(originalLines!=null && originalLines.size()>0){						
						for(int i=0;i<originalLines.size();i++){
							FDCartLineI cartLine=(FDCartLineI)originalLines.get(i);
							cartLine.setPricingContext(user.getPricingContext());
						    //originalLines=ProductPricingFactory.getInstance().getPricingAdapter(originalLines, new PricingContext(user.getPricingZoneId()));
						}
					}
					*/
					quickCart.setDeliveryDate(order.getDeliveryReservation().getStartTime());

					//
					// clean and remove duplicate product selections 
					//
					if (originalLines != null) {
						List cleanLines = OrderLineUtil.cleanAndRemoveDuplicateProductSelections(originalLines,false);
						quickCart.setProducts(cleanLines);
					}
				}

				if (quickCart.isEveryItemEverOrdered()) {
					quickCart.zeroAllQuantities();
				}

			}
			else if (this.ccListId != null && this.orderId == null) {
				
					boolean reload = 
						quickCart == null ||
						!ccListId.equals(quickCart.getOrderId()) || 
						!QuickCart.PRODUCT_TYPE_CCL.equals(quickCart.getProductType()) ||
						quickCart.isEmpty();

					if (quickCart == null) {
						quickCart = new QuickCart();
					} 
					
					if (reload) {
						quickCart.setOrderId(this.ccListId);
						quickCart.setProductType(QuickCart.PRODUCT_TYPE_CCL);
						quickCart.setDeliveryDate(new Date());
						quickCart.setUserZoneId(user.getPricingZoneId());
						quickCart.setName(FDListManager.getListName(user.getIdentity(), EnumCustomerListType.CC_LIST, ccListId));

					
						FDCustomerCreatedList ccList = FDListManager.getCustomerCreatedList(user.getIdentity(), this.ccListId);					
						List cclLines = ccList.getLineItems();	
					
						// convert the line items into  FDProductSelectionI and clean them as needed
						if(cclLines!=null){
							List productSelections = OrderLineUtil.getValidProductSelectionsFromCCLItems(cclLines);
							//List cartLines = OrderLineUtil.update(productSelections);
							quickCart.setProducts(productSelections);
						}
						QuickCartCache.cacheInstance(session, quickCart);
					}
					
					// used by ad_server.jsp
					request.setAttribute("loadedCclList",quickCart);

			}
			else if (this.starterListId != null && 
					 (quickCart == null || 
					  !QuickCart.PRODUCT_TYPE_STARTER_LIST.equals(quickCart.getProductType()) ||
					  !this.starterListId.equals(quickCart.getOrderId()))) {
			 
				
				if (quickCart == null ) quickCart = new QuickCart();

				quickCart.setOrderId(this.starterListId);
				quickCart.setProductType(QuickCart.PRODUCT_TYPE_STARTER_LIST);
				quickCart.clearProducts();
				quickCart.setDeliveryDate(new Date());
                quickCart.setUserZoneId(user.getPricingZoneId());
				StarterList sList = (StarterList)ContentFactory.getInstance().getContentNode(this.starterListId);
				for(Iterator i = sList.getListContents().iterator(); i.hasNext(); ) {
					try {
						
					    ConfiguredProduct product = (ConfiguredProduct)i.next();
					    
					    if (product == null) continue;
					    // this may be a product group
					    if (product instanceof ConfiguredProductGroup) product = (ConfiguredProduct)product.getProduct();
					    if (product == null) continue;
					    
					    FDProductInfo prodInfo = FDCachedFactory.getProductInfo(product.getSkuCode());
					
					    FDProductSelection productSelection =
					    	new FDProductSelection(prodInfo,product,product.getConfiguration(), user.getPricingZoneId());

					    OrderLineUtil.describe(productSelection);
					    quickCart.addProduct(productSelection);
					} catch (FDSkuNotFoundException e) {
						// skip
						LOGGER.warn("SKU NOT FOUND: " + e);
						continue;
					} catch (FDInvalidConfigurationException e) {
						// skip
						LOGGER.warn("INVALID CONFIGURATION " + e);
						continue;
					}
				}
				
				quickCart.setName(sList.getFullName());
			}

			if (quickCart == null) {
				// user doesn't have a cart, this is a bug, as login or site_access should put it there
				throw new FDResourceException("No quick cart found");
			}

			List prods = quickCart.getProducts();
			String qsDeptId = request.getParameter("qsDeptId");
			boolean hasDeptId = qsDeptId != null && !"".equals(qsDeptId);
			
			if ("POST".equalsIgnoreCase(request.getMethod())) {

				if ("deleteItem".equals(action) || "sort".equals(action)) {
					String deleteId = request.getParameter("deleteId");
					int delPos = 0;

					// ensure products are in proper order
					sortQuickCart(quickCart, request.getParameter("sortedBy"));

					int offset = 0;
					int quickCartSize = hasDeptId && !qsDeptId.equalsIgnoreCase("all")
						? quickCart.numberOfProducts(qsDeptId)
						: quickCart.numberOfProducts();
					
					//Get offset
					if(hasDeptId){	
						for (int c = 0; c < prods.size(); c++) {
							FDProductSelectionI sel = (FDProductSelectionI) prods.get(c);
							if(sel.lookupProduct().getDepartment().getContentName().equalsIgnoreCase(qsDeptId)){								
								offset = c;
								break;
							}
						}
					}
					
					for (int c = 0; c < prods.size(); c++) {
						FDProductSelectionI sel = (FDProductSelectionI) prods.get(c);


						if (c >= offset && c <= offset + quickCartSize) {
							int listPos = c - offset;
							
							if (deleteId != null && deleteId.equals(sel.getCustomerListLineId())) {
								delPos = c;
							}
							
							// bind previous quantities to line items
							if (sel.isSoldBySalesUnits()) {
								sel.setSalesUnit(request.getParameter("salesUnit_" + listPos));
							} else {
								sel.setQuantity(request.getParameter("quantity_" + listPos) == null
									|| "".equals(request.getParameter("quantity_" + listPos)) ? 0 : new Double(request
									.getParameter("quantity_" + listPos)).doubleValue());
							}
						}
					}

					if (action.equals("deleteItem")) {
						FDListManager.removeCustomerListItem(user, new PrimaryKey(request.getParameter("deleteId")));
						quickCart.removeProduct(delPos);
					}
				}
			}

			if ("GET".equalsIgnoreCase(request.getMethod())) {
				//
				// if we're not trying to clear it
				//
				if ("clearValues".equalsIgnoreCase(action)) {
					quickCart.zeroAllQuantities();
				}
			}

			sortQuickCart(quickCart, request.getParameter("sortBy"));

			pageContext.setAttribute(this.id, quickCart);
			
			QuickCartCache.cacheInstance(session, quickCart);

			return EVAL_BODY_BUFFERED;

		} catch (FDResourceException fdre) {
			LOGGER.error(fdre);
			throw new JspException(fdre);
		}
	}

	/**
	 * @param sortBy
	 */
	private void sortQuickCart(QuickCart quickCart, String sortBy) {
		if ("frequency".equals(sortBy)) {
			quickCart.sort(FDProductSelectionI.FREQUENCY_COMPARATOR);
		} else if ("recent_order".equals(sortBy)) {
			quickCart.sort(FDProductSelectionI.RECENT_PURCHASE_COMPARATOR_DESC);
		} else {
			quickCart.sort(FDProductSelectionI.DESCRIPTION_COMPARATOR);
		}
	}

	private final static String skuCodePreamble = "skuCode_";
	private final static String quantityPreamble = "quantity_";
	private final static String salesUnitPreamble = "salesUnit_";

	public static class TagEI extends TagExtraInfo {

		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {new VariableInfo(
				data.getAttributeString("id"),
				"com.freshdirect.fdstore.customer.QuickCart",
				true,
				VariableInfo.NESTED)};

		}

	}

}
