<%@page import="com.freshdirect.webapp.ajax.filtering.CmsFilteringFlow"%>
<%@ page import='com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*' %>
<%@ page import='java.io.*'%>
<%@ page import="java.util.*"%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.pricing.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
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
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<fd:CheckLoginStatus id="user"/>
<fd:PDPRedirector user="<%=user %>" />

<%
ProductModel productNode = ProductPricingFactory.getInstance().getPricingAdapter( ContentFactory.getInstance().getProductByName( request.getParameter("catId"), request.getParameter("productId") ), user.getPricingContext() );

// Handle no-product case
if (productNode==null) {
    throw new JspException("Product not found in Content Management System");
} else if (productNode.isDiscontinued()) {
    throw new JspException("Product Discontinued :"+request.getParameter("productId"));
}

boolean isWine = EnumTemplateType.WINE.equals( productNode.getTemplateType() );

//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", productNode.getPath());
request.setAttribute("listPos", "SystemMessage,ProductNote");


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

<%
EnumProductLayout prodLayout = productNode.getProductLayout();

// should we show the new leftnav
boolean shouldBeOnNew = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user);
%>

<potato:product name="productPotato" extraName="productExtraPotato" productId='${param.productId}' categoryId='${param.catId}'/>

<% if(shouldBeOnNew) {  // new leftnav, TODO: remove this after full rollout%>
	<potato:browse name="browsePotato" pdp="true" nodeId='${param.catId}'/>
<%}%>

<tmpl:insert template='/common/template/pdp_template.jsp'>

  <tmpl:put name='cmeventsource' direct='true'>pdp_main</tmpl:put>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="pdp" />
    <soy:import packageName="browse" />
  </tmpl:put>

  <tmpl:put name='containerExtraClass' direct='true'>pdp</tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css"/>
    <jwr:style src="/pdp.css"/>
  </tmpl:put>
  
<% if(shouldBeOnNew) {  // new leftnav, TODO: remove this after full rollout%>

  <tmpl:put name='title' direct='true'>FreshDirect - <%= productNode.getFullName() %></tmpl:put>    
    
  <tmpl:put name='deptnav' direct='true'>
    <div class="browse-titlebar">
      <soy:render template="browse.titleBar" data="${browsePotato.descriptiveContent}" />
    </div>
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
    
<% } else { //old leftnav %>

 <tmpl:put name='title' direct='true'>FreshDirect - <%= productNode.getFullName() %></tmpl:put>    
    
    <% if ( !isWine ) { // Wine template has no deptnav, and special leftnav, so only put these for regular layouts %>
	    <tmpl:put name='leftnav' direct='true'>	    	
	    	<td width="150" BGCOLOR="#E0E3D0" class="lNavTableConttd">		
			<!-- start : leftnav -->
			<% try { %><%@ include file="/common/template/includes/left_side_nav.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			<!-- end : leftnav -->			
			</td>
	    </tmpl:put>
    	<tmpl:put name="extraJs">
    	</tmpl:put>
	    <tmpl:put name='deptnav' direct='true'>
		    <% try { %><%@ include file="/common/template/includes/deptnav.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			<hr class="deptnav-separator">
	    </tmpl:put>
    <% } else { %>
    	<tmpl:put name="extraJs">
			<fd:javascript src="/assets/javascript/wine.js"/>
			<fd:javascript src="/assets/javascript/wine-nav.js"/>	
    	</tmpl:put>
    	<tmpl:put name="deptnav" direct="true">	
    	</tmpl:put>
    	<tmpl:put name="leftnav">
			<td class="wine-sidenav" bgcolor="#e2dfcc" style="z-index: 0;" width="150"><div align="center" style="background: #272324"><a href="/department.jsp?deptId=usq&trk=snav"><img src="/media/editorial/win_usq/usq_logo_sidenav_bottom.gif" width="150" height="109" border="0"></a><br></div>
			<% try { %><%@ include file="/common/template/includes/left_side_nav_usq.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			</td>    	
		</tmpl:put>
    <% } %>

<% } %>
    
	<tmpl:put name='facebookmeta' direct='true'>
		<meta property="og:title" content="FreshDirect - <%= productNode.getFullName() %>"/>
		<meta property="og:site_name" content="FreshDirect"/>
		<% 
			Image detailImage = productNode.getDetailImage();
			Image zoomImage = productNode.getZoomImage();			
			boolean isWineProduct = productNode.getDepartment() != null ? "usq".equals(productNode.getDepartment().getContentKey().getId()) : false;			
			if ( zoomImage != null && zoomImage.getPath().indexOf("clear.gif") == -1 && !isWineProduct ) {
				%><meta property="og:image" content="https://www.freshdirect.com<%= zoomImage.getPathWithPublishId() %>"/><%
			} else {
				%><meta property="og:image" content="https://www.freshdirect.com<%= detailImage.getPathWithPublishId() %>"/><%
			}
		%>
		<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
		<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
		<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
	</tmpl:put>

<% if(prodLayout != EnumProductLayout.COMPONENTGROUP_MEAL && prodLayout != EnumProductLayout.MULTI_ITEM_MEAL) { %>
	<c:set value="${productPotato}" scope="request" var="productPotato" />
	<c:set value="${productExtraPotato}" scope="request" var="productExtraPotato" />
	
	<tmpl:put name='content' direct='true'>
		<fd:CmPageView wrapIntoScriptTag="true" productModel="<%=productNode%>"/>
		<fd:CmProductView quickbuy="false" wrapIntoScriptTag="true" productModel="<%=productNode%>"/>
	
	<% if(shouldBeOnNew) {  // TODO: remove this after full rollout%>
    	<div class="browse-breadcrumbs">
      	<soy:render template="browse.breadCrumb" data="${browsePotato.breadCrumbs}" />
    	</div>
	<% } %>
		<jsp:include page="/includes/product/productDetail.jsp" >
			<jsp:param name="catId" value="${ param.catId }"/>
			<jsp:param name="productId" value="${ param.productId }"/>
		</jsp:include>
    <script>
      window.FreshDirect = window.FreshDirect || {};
      window.FreshDirect.browse = window.FreshDirect.browse || {};

      window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
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
		<jsp:include page="<%= prodLayout.getLayoutPath() %>" flush="false"/>
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

</tmpl:insert>
