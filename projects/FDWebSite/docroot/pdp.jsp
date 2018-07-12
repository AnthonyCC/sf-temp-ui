<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.freshdirect.webapp.ajax.filtering.CmsFilteringFlow"%>
<%@ page import='com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*' %>
<%@ page import='java.io.*'%>
<%@ page import="java.util.*"%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.*' %>
<%@ page import='com.freshdirect.fdstore.pricing.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import="com.freshdirect.fdstore.ecoupon.*"%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>

<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="unbxd" prefix="unbxd" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<fd:RequiredParameterValidator parameters="productId"/>
<fd:OptionalParameterValidator parameter="version" parameterType="<%=Integer.class.getSimpleName()%>"/>

<fd:CheckLoginStatus id="user"/>
<fd:CheckDraftContextTag/>
<fd:PDPRedirector user="<%=user %>" />

<potato:product name="productPotato" extraName="productExtraPotato" productId='<%=request.getParameter("productId")%>' categoryId='<%=request.getParameter("catId")%>' variantId='<%=request.getParameter("variantId")%>' grpId='<%=request.getParameter("grpId")%>' version='<%=request.getParameter("version")%>' />
<potato:browse name="browsePotato" pdp="true" nodeId='<%=request.getParameter("catId")%>'/>

	
<fd:ProductGroup id='productNode' categoryId='<%=request.getParameter("catId")%>' productId='<%=request.getParameter("productId")%>' >

<%
boolean isWine = EnumTemplateType.WINE.equals( productNode.getTemplateType() );
String title =  productNode.getPageTitle() != null && !productNode.getPageTitle().isEmpty() ? productNode.getPageTitle() : productNode.getFullName();
title = title.replaceAll("<[^>]*>", "");
%>

<%-- OAS page variables --%>
<c:set var="sitePage" scope="request" value="<%= productNode.getPath() %>" />
<c:set var="listPos" scope="request" value="SystemMessage,ProductNote" />
<c:set var="breadCrumbs" scope="request" value="${browsePotato.breadCrumbs}" />

<%
//REDIRECT to the redirect-url IF there is any
String redirectURL = productNode.getRedirectUrl();

ContentNodeModel parentNode =productNode.getParentNode();
boolean isDDPP = false;
if(null !=parentNode && parentNode instanceof CategoryModel){
	CategoryModel catModel = ((CategoryModel)parentNode);
	if(null !=catModel.getProductPromotionType()){
		isDDPP = true;
	}
}

if ( !isDDPP && redirectURL != null && !"".equals(redirectURL.trim()) && !"nm".equals(redirectURL.trim()) ) {
	StringBuffer buf = new StringBuffer( response.encodeRedirectURL( redirectURL ) );	
	FDURLUtil.appendCommonParameters( buf, request.getParameterMap() ); // append tracking codes, etc.
	response.sendRedirect( buf.toString() );
	return;
}

// REDIRECT to the hide-url IF product is hidden ( => has a hide-url )
if ( productNode.isHidden() ) {
	StringBuffer buf = new StringBuffer( response.encodeRedirectURL( productNode.getHideUrl() ) );	
	FDURLUtil.appendCommonParameters( buf, request.getParameterMap() ); // append tracking codes, etc.
   	response.sendRedirect( buf.toString() );
   	return;
}

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/pdp_template.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = request.getAttribute("sitePage").toString();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}

%>

<%//[redirection] Alcohol alert -> redirect health warning page %>
<fd:IsAlcoholic noProduct="true">
<%
	if( ((CategoryModel)productNode.getParentNode()).isHavingBeer() /*&& !yser.isHealthWarningAcknowledged()*/){
		String redirectHWURL = "/health_warning.jsp?successPage=/pdp.jsp"+URLEncoder.encode("?"+request.getQueryString());
		response.sendRedirect(response.encodeRedirectURL(redirectHWURL));
		return;
	}%>
</fd:IsAlcoholic>

<tmpl:insert template="<%= pageTemplate %>">

  <tmpl:put name="seoMetaTag">
    <fd:SEOMetaTag metaDescription="<%= productNode.getSEOMetaDescription() %>" title="<%= title %>"/>
  </tmpl:put>

  <tmpl:put name="seoCanonicalTag">
    <link rel="canonical" href="${productPotato.productPagePrimaryHomeUrl}" />
  </tmpl:put>

  <tmpl:put name='eventsource' direct='true'>pdp_main</tmpl:put>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="pdp" />
    <soy:import packageName="browse" />
  </tmpl:put>

  <tmpl:put name='containerExtraClass' direct='true'>pdp</tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css" media="all" />
    <jwr:style src="/pdp.css" media="all" />
  </tmpl:put>
  
	<tmpl:put name='deptnav' direct='true'>
		<soy:render template="browse.titleBarWrapper" data="${browsePotato.descriptiveContent}" />
	</tmpl:put>

  <tmpl:put name='leftnav' direct='true'>
    <div id="leftnav">
      <soy:render template="browse.menu" data="${browsePotato.menuBoxes}" />
    </div>
    <div class="backbutton">
      <button class="cssbutton back white icon-arrow-left2-before">Back</button>
    </div>
  </tmpl:put>

    <tmpl:put name="extraJs">
	<%-- Bazaarvoice --%>
        <% 
        if(FDStoreProperties.isBazaarvoiceEnabled()){
          String bvapiUrl = FDStoreProperties.getBazaarvoiceBvapiUrl(); %>
          <script type="text/javascript" src="<%= bvapiUrl %>"></script>
        <% } %>
    </tmpl:put>
    
	<tmpl:put name='facebookmeta' direct='true'>
		<meta property="og:title" content="<%= title %>"/>
		<meta property="og:site_name" content="FreshDirect"/>
		<% 
			Image detailImage = productNode.getDetailImage();
			Image zoomImage = productNode.getZoomImage();			
			boolean isWineProduct = productNode.getDepartment() != null ? (JspMethods.getWineAssociateId().toLowerCase()).equals(productNode.getDepartment().getContentKey().getId()) : false;			
			if ( zoomImage != null && zoomImage.getPath().indexOf("clear.gif") == -1 && !isWineProduct ) {
				%><meta property="og:image" content="https://www.freshdirect.com<%= zoomImage.getPathWithPublishId() %>"/><%
			} else {
				%><meta property="og:image" content="https://www.freshdirect.com<%= detailImage.getPathWithPublishId() %>"/><%
			}
		%>
		<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
	</tmpl:put>
<%
    EnumProductLayout prodLayout = productNode.getProductLayout();
	String layoutPath = prodLayout.getLayoutPath();
	/* leaving this, in case we need to go to this solution
	if (mobWeb && prodLayout.equals(EnumProductLayout.COMPONENTGROUP_MEAL)) {
		layoutPath = "/includes/product/componentGroupMeal_mobileWeb.jsp";
	}*/

	//ignore product layout and just use the default
	boolean debugIgnoreProductLayout = request.getParameter("debugIgnoreProductLayout")!=null ? "true".equalsIgnoreCase(request.getParameter("debugIgnoreProductLayout")) : false;
	boolean forceProductLayout = false; /* force the product to just use the normal layout instead of it's CMS-defined one */
	if (
		(mobWeb && prodLayout.equals(EnumProductLayout.COMPONENTGROUP_MEAL)) ||
		(mobWeb && prodLayout.equals(EnumProductLayout.RECIPE_MEALKIT_PRODUCT)) ||
		debugIgnoreProductLayout
	) {
		forceProductLayout = true;
	}
	
%>
	<c:set value="${productPotato}" scope="request" var="productPotato" />
	<c:set value="${productExtraPotato}" scope="request" var="productExtraPotato" />
	<c:set value="${productPotato.productPagePrimaryHomeUrl}" scope="request" var="seoCanonicalUri" />

<% if (productNode.getSpecialLayout()==null || forceProductLayout) { %>
	
	<tmpl:put name='content' direct='true'>
	
		<% if(!mobWeb) {  // mobWeb doesn't show breadcrumbs %>
	    	<div class="browse-breadcrumbs">
	      	<soy:render template="browse.breadCrumb" data="${browsePotato.breadCrumbs}" />
	    	</div>
		<% } %>
		<%@ include file="/includes/product/productDetail.jspf" %>
	    <script>
			window.FreshDirect = window.FreshDirect || {};
			window.FreshDirect.browse = window.FreshDirect.browse || {};
			window.FreshDirect.activeDraft = window.FreshDirect.activeDraft || {};
			
			window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>;
			window.FreshDirect.activeDraft = "${activeDraft}";
			window.FreshDirect.activeDraftDirectLink = "${activeDraftDirectLink}";
			    
			window.FreshDirect.pdp = window.FreshDirect.pdp || {};
			window.FreshDirect.pdp.data = window.FreshDirect.pdp.data || {};
			
			window.FreshDirect.pdp.layout = {
				'forced': '<%= forceProductLayout %>', /* param: debugIgnoreProductLayout=true */
				'id': '<%= prodLayout.getId() %>',
				'name': '<%= prodLayout.getName() %>'
				<%= (productNode.getSpecialLayout()!=null) 
					? ", 'special' : { 'id': '"+productNode.getSpecialLayout().getId()+"', 'name': '"+productNode.getSpecialLayout().getName()+"' }" 
					: "" %>
			};
			window.FreshDirect.pdp.data = <fd:ToJSON object="${productPotato}" noHeaders="true"/>;
			window.FreshDirect.pdp.extraData = <fd:ToJSON object="${productExtraPotato}" noHeaders="true"/>;
	    </script>
	</tmpl:put>
	
<% } else { // special layout include
	String cartMode = ( "true".equals( request.getParameter("ignoreAction") ) ) ? CartName.IGNORE_ACTION : CartName.ADD_TO_CART;
    String tgAction = request.getParameter("action")!=null ? request.getParameter("action") : prodLayout.canAddMultipleToCart() ? "addMultipleToCart" :  "addToCart";
	String successPage;
	successPage = "/cart_confirm_pdp.jsp?" + request.getQueryString();

%>
	<fd:FDShoppingCart id='cart' result='result' action='<%= tgAction %>' successPage='<%= successPage %>'>
<%
  //hand the action results off to the dynamic include
	request.setAttribute("actionResult", result);
	request.setAttribute("user", user);
	request.setAttribute("productNode", productNode);
	request.setAttribute("cartMode",cartMode);
	request.setAttribute("newLeftNav",true);
	FDCustomerCoupon custCoupon = null;
	if (productNode != null && productNode.getDefaultSku() != null && productNode.getDefaultSku().getProductInfo() != null) {
		custCoupon = user.getCustomerCoupon(productNode.getDefaultSku().getProductInfo(), EnumCouponContext.PRODUCT,productNode.getParentId(),productNode.getContentName());
	}
	request.setAttribute("custCoupon", custCoupon); //set coupon in to request for includes/tags to use
%>
	<tmpl:put name='content' direct='true'>
		<jsp:include page="<%= layoutPath %>" flush="false"/>
		
		<script>
			window.FreshDirect = window.FreshDirect || {};
			window.FreshDirect.browse = window.FreshDirect.browse || {};
			window.FreshDirect.activeDraft = window.FreshDirect.activeDraft || {};
			
			window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
			window.FreshDirect.activeDraft = "${activeDraft}"
			window.FreshDirect.activeDraftDirectLink = "${activeDraftDirectLink}";
			
			window.FreshDirect.pdp = window.FreshDirect.pdp || {};
			
			window.FreshDirect.pdp.layout = {
				'forced': '<%= forceProductLayout %>', /* param: debugIgnoreProductLayout=true */
				'id': '<%= prodLayout.getId() %>',
				'name': '<%= prodLayout.getName() %>'
				<%= (productNode.getSpecialLayout()!=null) 
					? ", 'special' : { 'id': '"+productNode.getSpecialLayout().getId()+"', 'name': '"+productNode.getSpecialLayout().getName()+"' }" 
					: "" %>
			};
			window.FreshDirect.pdp.data = <fd:ToJSON object="${productPotato}" noHeaders="true"/>;
			window.FreshDirect.pdp.extraData = <fd:ToJSON object="${productExtraPotato}" noHeaders="true"/>;
	    </script>
	</tmpl:put>
	</fd:FDShoppingCart>
<% } %>

	<% if ( productExtraPotato != null ) {
		// dynamic css generator for different kinds of nutrition panels
		Map nutritionPanel = (Map)productExtraPotato.get("nutritionPanel");
		String nutritionKind = null;

		if(nutritionPanel!=null){
			nutritionKind = (String)nutritionPanel.get("type");
		}

		if(nutritionKind!=null){
			nutritionKind = nutritionKind.toLowerCase();
			%>
			<tmpl:put name='nutritionCss'>
				<fd:css href='<%="/assets/css/"+nutritionKind+"_nutrition_display.css"%>' />
			</tmpl:put>
			<%
		}
	} %>

	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
		<jwr:script src="/fdmodules.js"  useRandomParam="false" />
		<jwr:script src="/fdcomponents.js"  useRandomParam="false" />
		
		<jwr:script src="/pdp.js"  useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
<unbxd:clickThruEvent product="<%= productNode %>"/>
</fd:ProductGroup>
