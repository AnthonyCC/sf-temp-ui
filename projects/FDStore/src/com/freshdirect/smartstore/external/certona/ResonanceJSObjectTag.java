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
			assemblyUserCertonaContext();
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
			String deptName = "";
			out.println("	\"pagetype\" : \"" + certonaPageId + "\",");
			out.println("	\"department\" : \"" + deptId + "\",");

		} else 	if ("CATEGORY".equals(certonaPageId)) {
			
			String catId = request.getParameter("catId");
			out.println("	\"pagetype\" : \"" + certonaPageId + "\",");
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
		
			out.println("	\"pagetype\" : \"PURCHASE\",");
			out.println("	\"itemid\" : \"" + (inCartProductIds.length() > 0 ? inCartProductIds.toString().substring(0, inCartProductIds.toString().length() - 1) : "") + "\",");
			out.println("	\"qty\" : \"" + (inCartProductIds.length() > 0 ? inCartProductPrices.toString().toUpperCase().substring(0, inCartProductPrices.toString().length() - 1) : "") + "\",");
			out.println("	\"price\" : \"" + (inCartProductIds.length() > 0 ? inCartProductQuantities.toString().toUpperCase().substring(0, inCartProductQuantities.toString().length() - 1) : "") + "\",");
			out.println("	\"total\" : \"" + order.getSubTotal() + "\",");
			out.println("	\"transactionid\" : \"" + saleId + "\",");

		} else {
			out.println("	\"pagetype\" : \"" + certonaPageId + "\",");
		}
		
		/*
		 * The end...
		 */
		
		out.println("	\"customerid\" : \"" + CertonaUserContextHolder.getCustomerId() + "\",");
		out.println("	\"cohort\" : \"" + CertonaUserContextHolder.getCohort() + "\",");
		out.println("	\"segment\" : \"1\"");

		if (CertonaUserContextHolder.getPageId() != null && CertonaUserContextHolder.getPageId().length() > 0) {
			
			out.println("	\"pageid\" : \"" + CertonaUserContextHolder.getPageId() + "\"");
			List<String> recommendedProductIds = CertonaUserContextHolder.getRecommendedProductIds();
			StringBuffer products = new StringBuffer();
			for (String productId : recommendedProductIds) {
				products.append(productId).append(";");
			}
			out.println("	\"recitems\" : \"" + (products.length() > 0 ? products.toString().substring(0, products.toString().length() - 1) : "") + "\"");

		}
		
		out.println("};");
		out.println("</script>");

	}
	
	private void assemblyUserCertonaContext() throws JspException {
		
		String trackingId = "dummyTrackingId";
		String sessionId = "dummySessionId";
		String userId = "";
		String cohort = "";
		Cookie cookies[] = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if ("RES_TRACKINGID".equals(cookie.getName())) {
					trackingId = cookie.getValue();
				} else if ("RES_SESSIONID".equals(cookie.getName())) {
					sessionId = cookie.getValue();
				} else if ("FDUser".equals(cookie.getName())) {
					
					try {
						FDUserI user = FDCustomerManager.recognize(cookie.getValue());
						if (user != null && user.getIdentity() != null) {
							userId = user.getIdentity().getErpCustomerPK();
							cohort = user.getCohortName();
						}
					} catch (FDAuthenticationException e) {
						throw new JspException();
					} catch (FDResourceException e) {
						throw new JspException();
					}
				}
			}
		}
		CertonaUserContextHolder.createContextObject(userId, cohort, trackingId, sessionId, null);
		
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
