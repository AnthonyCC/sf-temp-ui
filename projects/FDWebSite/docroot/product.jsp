<%// this jsp will redirect to the appropriate product page based on either the productPage Layout or some other logic %>

<%@ page import='com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*' %>
<%@ page import='java.io.*'%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import="com.freshdirect.fdstore.ecoupon.*"%>
<%@ page import='java.net.URLEncoder'%>
<%@page import="java.util.Locale"%>
<%@ page import='com.freshdirect.cms.util.ProductInfoUtil'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus id="user" />
<fd:CheckDraftContextTag/>
<fd:PDPRedirector user="<%=user %>"/>
<%!
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<fd:ProductGroup id='productNode' categoryId='<%= request.getParameter("catId") %>' productId='<%= request.getParameter("productId") %>'>
<%
// Handle no-product case
if (productNode==null) {
    throw new JspException("Product not found in Content Management System");
} else if (productNode.isDiscontinued()) {
    throw new JspException("Product Discontinued :"+request.getParameter("productId"));
}




%>
<fd:ClickThru product="<%= productNode %>"/>
<%
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", productNode.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,ProductNote,SideCartBottom");
String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
// [redirection] if the previewmode is true then do not honor the hide-URL settting
if (!ContentFactory.getInstance().getPreviewMode() && productNode.isHidden()) {
    String redirectURL = response.encodeRedirectURL(productNode.getHideUrl());
    if (redirectURL.toUpperCase().indexOf("/PRODUCT.JSP?")==-1) {
        StringBuffer buf = new StringBuffer(redirectURL);
		FDURLUtil.appendCommonParameters(buf, request.getParameterMap()); // append tracking codes, etc.
    	response.sendRedirect(buf.toString());
    	return;
    }
}

// [redirection] if there is a redirect_url setting.. then go to that regardless of the preview mode
String redir = productNode.getRedirectUrl();
ContentNodeModel parentNode =productNode.getParentNode();
boolean isDDPP = false;
if(null !=parentNode && parentNode instanceof CategoryModel){
	CategoryModel catModel = ((CategoryModel)parentNode);
	if(null !=catModel.getProductPromotionType() && ContentFactory.getInstance().isEligibleForDDPP()){
		isDDPP = true;
	}
}
if(!isDDPP){
	if (redir !=null && !"".equals(redir) && !"nm".equalsIgnoreCase(redir)) {
	    String redirectURL = response.encodeRedirectURL(redir);
		if (redirectURL.toUpperCase().indexOf("/PRODUCT.JSP?")==-1) {
	        StringBuffer buf = new StringBuffer(redirectURL);
			FDURLUtil.appendCommonParameters(buf, request.getParameterMap()); // append tracking codes, etc.
	    	response.sendRedirect(buf.toString());
	        return;
	    }
	}
}

// [redirection] if it is a grocery product, then jump to the grocery_product layout within the category page
// get the category layout type
if (productNode.getLayout().isGroceryLayout()) {
    response.sendRedirect( FDURLUtil.toDirectURL( FDURLUtil.getCategoryURI(request, productNode) ) );
    out.close();
    return;
}


// [redirection] Alcohol alert -> redirect health warning page %>
<fd:IsAlcoholic noProduct="true">
	<%
	if( ((CategoryModel)productNode.getParentNode()).isHavingBeer() /*&& !yser.isHealthWarningAcknowledged()*/){
	    String redirectURL = "/health_warning.jsp?successPage=/product.jsp"+URLEncoder.encode("?"+request.getQueryString());
	    response.sendRedirect(response.encodeRedirectURL(redirectURL));
	    return;
	}%>
</fd:IsAlcoholic>
<%

String tgAction = request.getParameter("action")!=null ? request.getParameter("action") :  "addToCart";

EnumProductLayout prodPageLayout = productNode.getProductLayout();

if ( prodPageLayout.canAddMultipleToCart()  ) {
	tgAction="addMultipleToCart";
}
if ("true".equals(request.getParameter("ccl"))) {
	tgAction = "CCL";
}


//** values for the shopping cart controller
String sPage = FDURLUtil.toDirectURL(FDURLUtil.getCartConfirmPageURI(request, productNode));

String jspTemplate;
if (EnumTemplateType.WINE.equals( productNode.getTemplateType() )) {
	jspTemplate = "/common/template/usq_sidenav.jsp";
} else { //assuming the default (Generic) Template
	jspTemplate = "/common/template/both_dnav.jsp";
}

String title = productNode.getPageTitle() != null ? productNode.getPageTitle() : "FreshDirect - " + productNode.getFullName();
title = title.replaceAll("<[^>]*>", "");
String productFullName = productNode.getFullName().replaceAll("<[^>]*>", "");
%>
<tmpl:insert template='<%=jspTemplate%>'>

	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="<%= title%>" metaDescription="<%=productNode.getSEOMetaDescription()%>"/>
	</tmpl:put>

   <%--  <tmpl:put name='title' direct='true'>FreshDirect - <%= productNode.getFullName() %></tmpl:put> --%>
    <tmpl:put name='leftnav' direct='true'>
    </tmpl:put>
<tmpl:put name='facebookmeta' direct='true'>
	<meta property="og:title" content="FreshDirect - <%= productFullName %>"/>
	<meta property="og:site_name" content="FreshDirect"/>

<% if (productNode!=null) {

	Image productImage = productNode.getDetailImage();
	Image zoomImage = (Image) productNode.getZoomImage();

	Object useProdImageObj = pageContext.getAttribute("useProdImage");
	boolean useProdImage = useProdImageObj == null ? false : (Boolean)useProdImageObj;
	boolean isWineProduct = productNode.getDepartment() != null ? JspMethods.getWineAssociateId().equalsIgnoreCase(productNode.getDepartment().getContentKey().getId()) : false;

	if ( zoomImage != null && zoomImage.getPath().indexOf("clear.gif") == -1 && !useProdImage && !isWineProduct ) {
	%>
		<meta property="og:image" content="https://www.freshdirect.com<%= productNode.getZoomImage().getPathWithPublishId() %>"/>
	<%
	} else {
	%>
		<meta property="og:image" content="https://www.freshdirect.com<%= productNode.getDetailImage().getPathWithPublishId() %>"/>
	<%
	}
}%>
		<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
		<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
		<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
</tmpl:put>
<tmpl:put name='content' direct='true'>
	<fd:CmPageView wrapIntoScriptTag="true" productModel="<%=productNode%>"/>
	<fd:CmProductView quickbuy="false" wrapIntoScriptTag="true" productModel="<%=productNode%>"/>
	<%
		SkuModel _dfltSku =null;
		FDProduct _fdprod =null;
		int leadTime =0;
		if(productNode!=null && productNode.getSkus()!=null &&productNode.getSkus().size()>0) {
			_dfltSku=(SkuModel)productNode.getSkus().get( 0 );
			try{
				_fdprod=_dfltSku.getProduct();
				leadTime=_fdprod.getMaterial().getLeadTime();
			} catch (FDSkuNotFoundException fdsnf){
				JspLogger.PRODUCT.warn("Product Page: catching FDSkuNotFoundException and Continuing:\n SkuModel:="+_dfltSku+"\nException message:= "+fdsnf.getMessage());
			}
		}
	%>
<%if (FDStoreProperties.isAdServerEnabled()) {%>

	<script type="text/javascript">
		OAS_AD('ProductNote');
	</script>

<%} else {%>
    <%@ include file="/shared/includes/product/i_product_quality_note.jspf" %>
<%}%>


<% if(leadTime > 0 && FDStoreProperties.isLeadTimeOasAdTurnedOff()) { %>
<CENTER><font class="text11"><b>CURRENTLY AVAILABLE - <font class="text11rbold"><%=JspMethods.convertNumToWord(leadTime)%> DAY LEAD TIME</font>.</b><br></CENTER> To assure the highest quality, our chefs prepare this item to order. <br> Please order at least two days in advance<br>(for example, order on Thursday for Saturday delivery).</font><p>
<% } %>

<fd:FDShoppingCart id='cart' result='actionResult' action='<%= tgAction %>' successPage='<%= sPage %>' source='<%= request.getParameter("fdsc.source")%>' >
<%  //hand the action results off to the dynamic include
	String cartMode = CartName.ADD_TO_CART;
	FDCartLineI templateLine = null ;
	
	request.setAttribute("actionResult", actionResult);
	request.setAttribute("user", user);
	request.setAttribute("productNode", productNode);
	request.setAttribute("cartMode",cartMode);
	request.setAttribute("templateLine",templateLine);
	FDCustomerCoupon custCoupon = null;
	if (productNode != null && productNode.getDefaultSku() != null && productNode.getDefaultSku().getProductInfo() != null) {
		custCoupon = user.getCustomerCoupon(productNode.getDefaultSku().getProductInfo(), EnumCouponContext.PRODUCT,productNode.getParentId(),productNode.getContentName());
	}
	request.setAttribute("custCoupon", custCoupon); //set coupon in to request for includes/tags to use

%>
<%@ include file="/includes/product/cutoff_notice.jspf" %>
<%@ include file="/includes/product/i_dayofweek_notice.jspf" %>
<!-- product layout : <%= prodPageLayout.getLayoutPath() %> --> 

<jsp:include page="<%= prodPageLayout.getLayoutPath() %>" flush="false"/>

</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>
</fd:ProductGroup>
