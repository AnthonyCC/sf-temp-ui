package com.freshdirect.smartstore.external.certona;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;

public class ResonanceJSObjectTag extends SimpleTagSupport {

	private HttpServletRequest request = null;
	private String action = "";
	private String urlOverride = "";
	private Object contextObjects = null;
	
	
	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		
		request = (HttpServletRequest)((PageContext) getJspContext()).getRequest();

		if ("init".equals(action)) {
			assemblyUserCertonaContext(request);
		} else {
			assemblyResonanceTag();
		}
		
	}
	
	private void assemblyResonanceTag() throws IOException, JspException {

		String pageURI = request.getRequestURI();
		FDUserI user = (FDUserI)request.getSession().getAttribute("fd.user");
		String certonaPageId = pageURI.substring(1, pageURI.indexOf(".")).toUpperCase();
		
		JspWriter out = this.getJspContext().getOut();
		out.println("<script type=\"text/javascript\">");
		out.println("var certona = {");

		/*
		 * Page specific certona JS object decoration
		 */
		
		if (urlOverride != null && urlOverride.length() > 0) {

			if (urlOverride.equals("CUSTOMIZE") || urlOverride.equals("QUICKBUY")) {
				out.println("	\"pagetype\" : \"" + urlOverride + "IFRAME\",");
				String productId =  request.getParameter("productId");
				out.println("	\"itemid\" : \"" + productId + "\",");
			}
		} else if ("DEPARTMENT".equals(certonaPageId)) {
			
			String deptId = request.getParameter("deptId");

			out.println("	\"pagetype\" : \"" + certonaPageId + "\",");
			out.println("	\"department\" : \"" + deptId + "\",");

		} else 	if ("CATEGORY".equals(certonaPageId)) {
			
			final String catId = request.getParameter("catId");
			
			CategoryModel cat = (CategoryModel) ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, catId);
			if (cat != null && !cat.isTopLevelCategory() ) {
				out.println("	\"pagetype\" : \"SUBCATEGORY\",");
			} else {
				out.println("	\"pagetype\" : \"CATEGORY\",");
			}

			out.println("	\"category\" : \"" + catId + "\",");

		} else 	if ("BROWSE".equals(certonaPageId)) {
			
			String id = request.getParameter("id");
			ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNode(id);

			if (contentNodeModel != null) {
				if (contentNodeModel instanceof SuperDepartmentModel) {
					out.println("	\"pagetype\" : \"DEPARTMENT\",");//TODO: change later on to superdepartment
					out.println("	\"department\" : \"" + id + "\",");

				} else if (contentNodeModel instanceof DepartmentModel) {
					out.println("	\"pagetype\" : \"DEPARTMENT\",");
					out.println("	\"department\" : \"" + id + "\",");
					
				} else if (contentNodeModel instanceof CategoryModel) {
					if (contentNodeModel.getParentNode() instanceof DepartmentModel) {
						out.println("	\"pagetype\" : \"CATEGORY\",");
					} else if (contentNodeModel.getParentNode() instanceof CategoryModel) {
						out.println("	\"pagetype\" : \"SUBCATEGORY\",");
					}
					out.println("	\"category\" : \"" + id + "\",");
				
				}
			}

		} else 	if ("BROWSE_SPECIAL".equals(certonaPageId)) {

			String id = request.getParameter("id");
			ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNode(id);

			if (contentNodeModel != null) {
				if (contentNodeModel instanceof CategoryModel) {
					out.println("	\"pagetype\" : \"CATEGORY\",");
					out.println("	\"category\" : \"" + id + "\",");
				
				}
			}

		} else 	if ("SEARCH".equals(certonaPageId)) {
			
			SearchResults searchResults = (SearchResults)request.getAttribute("searchInputForCertona");
			if (searchResults != null && !searchResults.isEmpty()) {
				out.println("	\"pagetype\" : \"SEARCH\",");
			} else {
				out.println("	\"pagetype\" : \"NOSEARCH\",");
			}

		} else 	if ("PDP".equals(certonaPageId)) {
			
			String productId = request.getParameter("productId");
			out.println("	\"pagetype\" : \"PRODUCT\",");
			out.println("	\"itemid\" : \"" + productId + "\",");

		} else 	if ("CART_CONFIRM_PDP".equals(certonaPageId)) {
			
			Map<String, Map<String, Object>> dataMap = (Map<String, Map<String, Object>>)request.getAttribute("cartConfirmInputForCertona");
			StringBuffer inCartProductIds = new StringBuffer("");
			if (dataMap != null) {
				for (HashMap<String, Map<String, Object>> entry : ((List<HashMap<String, Map<String, Object>>>)dataMap.get("cartConfirmPotatoes"))) {
					
					inCartProductIds.append(entry.get("cartLine").get("productId")).append(";");
					
				}
			}

			out.println("	\"pagetype\" : \"" + certonaPageId + "\",");
			out.println("	\"itemid\" : \"" + (inCartProductIds.length() > 0 ? inCartProductIds.toString().substring(0, inCartProductIds.toString().length() - 1) : "") + "\",");

		} else 	if ("QUICKBUY/CONFIRM".equals(certonaPageId)) {

			out.println("	\"pagetype\" : \"CARTCONFIRMIFRAME\",");

			FDCartModel cart = user.getShoppingCart();
			List<FDCartLineI> items = cart.getRecentOrderLines();
			if (items != null && items.size() > 0) {
				// quickbuy should add only one item to cart
				FDCartLineI cartLine = items.get(0);

				out.println("	\"itemid\" : \""+cartLine.getProductRef().getProductId()+"\",");
			}			
			
		} else 	if ("VIEW_CART".equals(certonaPageId)) {
			
			StringBuffer inCartProductIds = new StringBuffer("");
			FDCartModel cart = user.getShoppingCart();
			
			for (FDCartLineI cartLine : cart.getOrderLines()) {
				
				inCartProductIds.append(cartLine.getProductRef().getProductId()).append(";");
				
			}

			out.println("	\"pagetype\" : \"CART\",");
			//Omitting last semicolon
			out.println("	\"itemid\" : \"" + (inCartProductIds.length() > 0 ? inCartProductIds.toString().substring(0, inCartProductIds.toString().length() - 1) : "") + "\",");

		} else 	if ("QUICKSHOP/QS_FD_LISTS".equals(certonaPageId)) {
			
			out.println("	\"pagetype\" : \"RECOMMENDED\",");
			out.println("	\"itemid\" : \"\",");

		} else 	if ("QUICKSHOP/QS_SHOP_FROM_LIST".equals(certonaPageId)) {
			
			out.println("	\"pagetype\" : \"WISHLIST\",");
			out.println("	\"itemid\" : \"\",");

		} else 	if ("CHECKOUT/STEP_4_RECEIPT".equals(certonaPageId)) {
			
			String saleId = (String)request.getSession().getAttribute("fd.recentOrderNumber");
			FDOrderI order = null;
			StringBuffer inCartProductIds = new StringBuffer("");
			StringBuffer inCartProductQuantities = new StringBuffer("");
			StringBuffer inCartProductPrices = new StringBuffer("");

			try {
				if (user != null && user.getIdentity()!=null) {
					order = FDCustomerManager.getOrder(user.getIdentity(), saleId);
					
				} else {
					order = FDCustomerManager.getOrder(saleId);
				}

				for (FDCartLineI cartLine : order.getOrderLines()) {
	
					inCartProductIds.append(cartLine.getProductRef().getProductId()).append(";");
					inCartProductQuantities.append(cartLine.getQuantity()).append(";");
					inCartProductPrices.append(cartLine.getPrice()).append(";");
	
				}
			} catch (FDResourceException fdre) {
				throw new JspException(fdre);
			}
		
			final String prdIds = inCartProductIds.toString();
			final String prdQtys = inCartProductQuantities.toString();
			final String prdPrices = inCartProductPrices.toString();

			final boolean isEmptyLine = prdIds.length() == 0 || prdQtys.length() == 0 || prdPrices.length() == 0;
			
			out.println("	\"pagetype\" : \"PURCHASE\",");
			out.println("	\"total\" : \"" + order.getSubTotal() + "\",");
			out.println("	\"transactionid\" : \"" + saleId + "\",");

			if (isEmptyLine) {
				out.println("	\"itemid\" : \"\",");
				out.println("	\"qty\" : \"\",");
				out.println("	\"price\" : \"\",");
			} else {
				out.println("	\"itemid\" : \"" + prdIds.substring(0, prdIds.length() - 1) + "\",");
				out.println("	\"qty\" : \"" + prdQtys.substring(0, prdQtys.length() - 1) + "\",");
				out.println("	\"price\" : \"" + prdPrices.substring(0, prdPrices.length() - 1) + "\",");
			}

		} else {
			out.println("	\"pagetype\" : \"" + certonaPageId + "\",");
		}
		
		/*
		 * The end...
		 */
		
		out.println("	\"customerid\" : \"" + ((user != null && user.getIdentity() != null) ? user.getIdentity().getErpCustomerPK() : "") + "\",");
		out.println("	\"cohort\" : \"" + (user != null ? user.getCohortName() : "") + "\",");

		if (CertonaUserContextHolder.getPageId() != null && CertonaUserContextHolder.getPageId().length() > 0) {
			
			out.println("	\"segment\" : \"1\",");
			out.println("	\"pageid\" : \"" + CertonaUserContextHolder.getPageId() + "\",");
			List<String> recommendedProductIds = CertonaUserContextHolder.getRecommendedProductIds();
			StringBuffer products = new StringBuffer();
			for (String productId : recommendedProductIds) {
				products.append(productId).append(";");
			}
			out.println("	\"recitems\" : \"" + (products.length() > 0 ? products.toString().substring(0, products.toString().length() - 1) : "") + "\"");

		} else {
			out.println("	\"segment\" : \"1\"");
		}
		
		out.println("};");
		out.println("</script>");
		CertonaUserContextHolder.invalidateCertonaUserContext();

	}
	
	public static void assemblyUserCertonaContext(HttpServletRequest request) throws JspException {
		
		CertonaUserContextHolder.initCertonaContextFromCookies(request);
		String id = request.getParameter("id") == null ? "" : request.getParameter("id").toString();
		String searchParam = request.getParameter("searchParams") == null ? "" : request.getParameter("searchParams").toString();
		CertonaUserContextHolder.setId(id);
		CertonaUserContextHolder.setSearchParam(searchParam);
		
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getContextObjects() {
		return contextObjects;
	}

	public void setContextObjects(Object contextObjects) {
		this.contextObjects = contextObjects;
	}

	public String getUrlOverride() {
		return urlOverride;
	}

	public void setUrlOverride(String urlOverride) {
		this.urlOverride = urlOverride;
	}

}
