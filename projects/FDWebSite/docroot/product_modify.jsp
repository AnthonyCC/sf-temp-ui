<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import="com.freshdirect.fdstore.ecoupon.*"%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<% //expanded page dimensions
final int W_PRODUCT_MODIFY_TOTAL = 600;
%>
<fd:CheckLoginStatus />
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
	int cartLine = null != request.getParameter("cartLine") ? Integer.parseInt(request.getParameter("cartLine")): 0;
	String snowFlakeImage = "/media_stat/images/template/snwflk_icon.gif";
	FDCartLineI templateLine = null;
	String catId = request.getParameter("catId");
	String productId = request.getParameter("productId");
	String skuCode = request.getParameter("skuCode");
    boolean isCallCenterApp = "callCenter".equalsIgnoreCase((String)session.getAttribute(SessionName.APPLICATION));

	if (catId==null) {
		// it's a new request (only cartLine was specified)

		// !!! ugly, should use some tag to get cartline (but FDShoppingCart is a controller, not really suited for this)
        FDCartModel cart = (FDCartModel) user.getShoppingCart();
        int orderLineIndex = cart.getOrderLineIndex(cartLine);
        if (orderLineIndex<0) {
        	// stale request, orderline no longer in cart, go to view cart
        	response.sendRedirect(response.encodeRedirectURL("/view_cart.jsp?trk=pmod"));
        	return;
       	}

		templateLine = (FDCartLineI) cart.getOrderLine(orderLineIndex);
		skuCode=templateLine.getSkuCode();
		catId = templateLine.getCategoryName();
		productId = templateLine.getProductName();
	}
	
	
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_nav.jsp";
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com/product_modify.jsp";
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}
%>
<fd:ProductGroup id='productNode' categoryId='<%= catId %>' productId='<%= productId %>' skuCode='<%= skuCode%>'>
<%
	EnumProductLayout prodPageLayout = (productNode==null) ? null : productNode.getProductLayout();

	if (productNode==null) {
		throw new JspException("Product not found in Content Management System");
	} else if (productNode.isDiscontinued()) {
		throw new JspException("Product Discontinued");
	}


	String cartMode = CartName.MODIFY_CART;
	String successPage;
	if (isCallCenterApp) {
		//tack on the product id and cat ID
		successPage = "/order/product.jsp?"+request.getQueryString()+"&catId="+productNode.getParentNode()+"&productId="+productNode;
	} else if (request.getParameter("returnPage") != null) {
		successPage = request.getParameter("returnPage");
	} else {
		successPage = "/view_cart.jsp";
	}
	String tagAction = request.getParameter("action")!=null ? request.getParameter("action") :  "changeOrderLine" ;
//	String formAction = request.getRequestURI() + "?" + request.getQueryString();
	FDCustomerCoupon custCoupon = null;
	if (templateLine != null && templateLine.getUpc()!=null) {
		custCoupon = user.getCustomerCoupon(templateLine, EnumCouponContext.PRODUCT);
	}
	request.setAttribute("custCoupon", custCoupon); //set coupon in to request for includes/tags to use

%>
<fd:FDShoppingCart id='cart' result='result' action='<%= tagAction %>' successPage='<%= successPage %>'>
<tmpl:insert template='<%= pageTemplate %>'>

    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Edit Item in Cart"/>
    </tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Modify Item in Cart <% // productNode.getFullName() %></tmpl:put> --%>

    <tmpl:put name='leftnav' direct='true'>
    </tmpl:put>
	<tmpl:put name='extraCss' direct='true'>
	  <jwr:style src="/quickshop.css" media="all" />
	  <jwr:style src="/pdp.css" media="all" />
	</tmpl:put>

<tmpl:put name='content' direct='true'>
	<%
		String pageContentDivWidth = W_PRODUCT_MODIFY_TOTAL+"px";
		if (mobWeb || prodPageLayout.equals(EnumProductLayout.RECIPE_MEALKIT_PRODUCT)) { pageContentDivWidth = "100%"; }
		if (prodPageLayout.equals(EnumProductLayout.HOLIDAY_MEAL_BUNDLE_PRODUCT)) { pageContentDivWidth = "775px"; }
	%>
	<div style="width: <%= pageContentDivWidth %>" class="prodMod-cont">
		<div class="center modProd-banner" style="margin-bottom: 6px; width: <%= (mobWeb) ? "100%" : W_PRODUCT_MODIFY_TOTAL+"px" %>"><span>Edit Item in Cart</span></div>
		
		<%-- leaving this in code since the link is useful for QA. Hidden via css --%>
		<div class="text12px prodMod-modLink" style="margin-bottom: 6px; width: <%= (mobWeb) ? "100%" : W_PRODUCT_MODIFY_TOTAL+"px" %>"">
			This item is now in your cart. After making changes to it, click "save changes" below. 
			To remove it from your cart, click "remove item." To return to the page where you bought it, 
			<a href="/pdp.jsp?productId=<%= productNode %>&catId=<%= productNode.getParentNode() %>&trk=pmod">click here</a>.
		</div>
		<div class="" style="margin-bottom: 6px;">
			<%@ include file="/includes/product/cutoff_notice.jspf" %>
			<%@ include file="/includes/product/i_dayofweek_notice.jspf" %>
		</div>
		<%
			request.setAttribute("actionResult", result);
			request.setAttribute("user", user);
			request.setAttribute("productNode", productNode);
			request.setAttribute("cartMode", cartMode);
			request.setAttribute("templateLine",templateLine);
			request.setAttribute("modProductId", productNode);
			request.setAttribute("modCatId", productNode.getParentNode());
		
		
			// if this is the wine product layout, then modification always uses the perishable product layout
			if (prodPageLayout.equals(EnumProductLayout.WINE)) prodPageLayout= EnumProductLayout.PERISHABLE;
			
			// if this is configuredProduct layout, then use the ComponentGroup layout to render the modify screen
			if (
				prodPageLayout.equals(EnumProductLayout.CONFIGURED_PRODUCT)
				/*|| prodPageLayout.equals(EnumProductLayout.HOLIDAY_MEAL_BUNDLE_PRODUCT)*/
				/*|| prodPageLayout.equals(EnumProductLayout.RECIPE_MEALKIT_PRODUCT)*/
			) {
			    prodPageLayout= EnumProductLayout.COMPONENTGROUP_MEAL;
			}
			
			String productPage = prodPageLayout.getLayoutPath();
		%>
		<jsp:include page="<%=productPage%>" flush="false"/>
		
	</div>
	<script>
    	window.FreshDirect = window.FreshDirect || {};
    	FreshDirect.productModify = {};
    	FreshDirect.productModify.prodPageLayoutName = '<%= prodPageLayout.getName() %>';
    	FreshDirect.productModify.prodPageLayoutId = '<%= prodPageLayout.getId() %>';
    	//update initial pricing display
    	$jq(document).ready(function() { $jq('[data-component="quantitybox.value"]').trigger('change'); });
	</script>
</tmpl:put>

</tmpl:insert>
</fd:FDShoppingCart>

</fd:ProductGroup>
