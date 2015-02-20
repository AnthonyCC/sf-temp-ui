package com.freshdirect.smartstore.external.certona;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.CertonaTransitionUtil;
import com.freshdirect.fdstore.util.EnumSiteFeature;

public class CertonaUtil {
	
	public static final String SCRIPT_SRC = "//edge1.certona.net/cd/4234f569/freshdirect.com/scripts/resonance.js";
	
	public static JSONObject getCertonaResonanceData(HttpServletRequest request, String certonaPageId
				, FDUserI user, String urlOverride, String browseId) throws FDResourceException {
		
		//using EnumSiteFeature as SRCH since we assume setup is all 50/50. So WHICH feature we check shouldn't matter
		boolean isCertona = CertonaTransitionUtil.isEligibleForCertona(user, EnumSiteFeature.getEnum("SRCH"));
				
		/*
		 * Page specific certona JS object decoration
		 */
		JSONObject certona = new JSONObject();
		
		if (urlOverride != null && urlOverride.length() > 0) {
			if (urlOverride.equals("CUSTOMIZE") || urlOverride.equals("QUICKBUY")) {
				final String pageId = urlOverride + "IFRAME";
				String productId =  request.getParameter("productId");
				
				certona.put("pagetype", pageId);
				certona.put("itemid", productId);
			}
		} else if ("DEPARTMENT".equals(certonaPageId)) {
			String deptId = request.getParameter("deptId");

			certona.put("pagetype", certonaPageId);
			certona.put("department", deptId);
		} else 	if ("CATEGORY".equals(certonaPageId)) {
			final String catId = request.getParameter("catId");
			
			CategoryModel cat = (CategoryModel) ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, catId);
			if (cat != null && !cat.isTopLevelCategory() ) {
				certona.put("pagetype", "SUBCATEGORY");
			} else {
				certona.put("pagetype", "CATEGORY");
			}

			certona.put("category", catId);
		} else 	if ("BROWSE".equals(certonaPageId)) {
			String id = browseId != null ? browseId : request.getParameter("id");
			ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNode(id);

			if (contentNodeModel instanceof SuperDepartmentModel) {
				certona.put("pagetype", "DEPARTMENT"); //TODO: change later on to superdepartment
				certona.put("department", id);
			} else if (contentNodeModel instanceof DepartmentModel) {
				certona.put("pagetype", "DEPARTMENT");
				certona.put("department", id);
			} else if (contentNodeModel instanceof CategoryModel) {
				if (contentNodeModel.getParentNode() instanceof DepartmentModel) {
					certona.put("pagetype", "CATEGORY");
				} else if (contentNodeModel.getParentNode() instanceof CategoryModel) {
					certona.put("pagetype", "SUBCATEGORY");
				}
				certona.put("category", id);
			}

		} else 	if ("BROWSE_SPECIAL".equals(certonaPageId)) {
			String id = request.getParameter("id");
			ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNode(id);

			if (contentNodeModel instanceof CategoryModel) {
				certona.put("pagetype", "CATEGORY");
				certona.put("category", id);
			}

		} else 	if ("SEARCH".equals(certonaPageId)) {
			// Soon to be deprecated
			SearchResults searchResults = (SearchResults)request.getAttribute("searchInputForCertona");
			if (searchResults != null && !searchResults.isEmpty()) {
				certona.put("pagetype", "SEARCH");
			} else {
				certona.put("pagetype", "NOSEARCH");
			}

		} else 	if ("SRCH".equals(certonaPageId)) {
			final boolean result = CertonaUserContextHolder.isSuccessfulSearch();
			if (result) {
				certona.put("pagetype", "SEARCH");
			} else {
				certona.put("pagetype", "NOSEARCH");
			}

		} else 	if ("PDP".equals(certonaPageId)) {
			String productId = request.getParameter("productId");

			certona.put("pagetype", "PRODUCT");
			certona.put("itemid", productId);
		} else 	if ("CART_CONFIRM_PDP".equals(certonaPageId)) {
			Map<String, Map<String, Object>> dataMap = (Map<String, Map<String, Object>>)request.getAttribute("cartConfirmInputForCertona");
			StringBuffer inCartProductIds = new StringBuffer("");
			if (dataMap != null) {
				// FIXME
				for (HashMap<String, Map<String, Object>> entry : ((List<HashMap<String, Map<String, Object>>>)dataMap.get("cartConfirmPotatoes"))) {
					
					inCartProductIds.append(entry.get("cartLine").get("productId")).append(";");
					
				}
			}

			certona.put("pagetype", certonaPageId);
			if (inCartProductIds.length() > 0) {
				certona.put("itemid", inCartProductIds.toString().substring(0, inCartProductIds.toString().length() - 1));
			} else {
				certona.put("itemid", "");
			}
		} else 	if ("QUICKBUY/CONFIRM".equals(certonaPageId)) {
			FDCartModel cart = user.getShoppingCart();
			List<FDCartLineI> items = cart.getRecentOrderLines();
			if (items != null && items.size() > 0) {
				// quickbuy should add only one item to cart
				FDCartLineI cartLine = items.get(0);

				certona.put("itemid", cartLine.getProductRef().getProductId());
			}			
			
			certona.put("pagetype", "CARTCONFIRMIFRAME");
		} else 	if ("VIEW_CART".equals(certonaPageId) || "CHECKOUT/VIEW_CART".equals(certonaPageId)) {
			StringBuffer inCartProductIds = new StringBuffer("");
			FDCartModel cart = user.getShoppingCart();
			
			// FIXME
			for (FDCartLineI cartLine : cart.getOrderLines()) {
				
				inCartProductIds.append(cartLine.getProductRef().getProductId()).append(";");
				
			}

			certona.put("pagetype", "CART");
			if (inCartProductIds.length() > 0) {
				certona.put("itemid", inCartProductIds.toString().substring(0, inCartProductIds.toString().length() - 1));
			} else {
				certona.put("itemid", "");
			}
		} else 	if ("QUICKSHOP/QS_FD_LISTS".equals(certonaPageId)) {
			certona.put("pagetype", "RECOMMENDED");
			certona.put("itemid", "");
		} else 	if ("QUICKSHOP/QS_SHOP_FROM_LIST".equals(certonaPageId)) {
			certona.put("pagetype", "WISHLIST");
			certona.put("itemid", "");
		} else 	if ("CHECKOUT/STEP_4_RECEIPT".equals(certonaPageId)) {
			String saleId = (String)request.getSession().getAttribute("fd.recentOrderNumber");
			FDOrderI order = null;
			StringBuffer inCartProductIds = new StringBuffer("");
			StringBuffer inCartProductQuantities = new StringBuffer("");
			StringBuffer inCartProductPrices = new StringBuffer("");

			if (user != null && user.getIdentity()!=null) {
				order = FDCustomerManager.getOrder(user.getIdentity(), saleId);
				
			} else {
				order = FDCustomerManager.getOrder(saleId);
			}

			// FIXME
			for (FDCartLineI cartLine : order.getOrderLines()) {

				inCartProductIds.append(cartLine.getProductRef().getProductId()).append(";");
				inCartProductQuantities.append(cartLine.getQuantity()).append(";");
				inCartProductPrices.append(cartLine.getPrice()).append(";");

			}
		
			final String prdIds = inCartProductIds.toString();
			final String prdQtys = inCartProductQuantities.toString();
			final String prdPrices = inCartProductPrices.toString();

			final boolean isEmptyLine = prdIds.length() == 0 || prdQtys.length() == 0 || prdPrices.length() == 0;
			
			certona.put("pagetype", "PURCHASE");
			certona.put("total", Double.toString(order.getSubTotal()) );
			certona.put("transactionid", saleId);

			if (isEmptyLine) {
				certona.put("itemid", "");
				certona.put("qty", "");
				certona.put("price", "");
			} else {
				certona.put("itemid", prdIds.substring(0, prdIds.length() - 1));
				certona.put("qty", prdQtys.substring(0, prdQtys.length() - 1));
				certona.put("price", prdPrices.substring(0, prdPrices.length() - 1));
			}
		} else {
			certona.put("pagetype", certonaPageId);
		}
		
		/*
		 * The end...
		 */

		if (user != null) {
			certona.put("customerid", (user.getIdentity() != null ? user.getIdentity().getErpCustomerPK() : ""));
			certona.put("cohort", user.getCohortName());
		} else {
			certona.put("customerid", "");
			certona.put("cohort", "");
		}
		
		if (CertonaUserContextHolder.getPageId() != null && CertonaUserContextHolder.getPageId().length() > 0) {
			List<String> recommendedProductIds = CertonaUserContextHolder.getRecommendedProductIds();
			StringBuffer products = new StringBuffer();
			
			// FIXME
			for (String productId : recommendedProductIds) {
				products.append(productId).append(";");
			}


			certona.put("segment", (isCertona) ? "1" : "2");
			certona.put("pageid", CertonaUserContextHolder.getPageId());
			if (products.length() > 0) {
				certona.put("recitems", products.toString().substring(0, products.toString().length() - 1));
			} else {
				certona.put("recitems", "");
			}
		} else {
			certona.put("segment", (isCertona) ? "1" : "2");
		}
		
		if (isCertona) {
			certona.put("SCRIPT_SRC", SCRIPT_SRC);
		}
		
		return certona;
	}
	
	public static void appendCertonaObjectToPayload(JSONObject jsonObj, Map<String, Object> payload) {
		if (jsonObj == null || payload == null)
			return;
		
		//just put as a string, instead of serializing JSONObject
		payload.put("certona", jsonObj.toString());
	}
}
