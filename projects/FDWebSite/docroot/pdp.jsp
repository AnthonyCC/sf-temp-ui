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

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<fd:CheckLoginStatus id="user" />
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

<potato:product name="productPotato" extraName="productExtraPotato" productId='${param.productId}' categoryId='${param.catId}'/>

<tmpl:insert template='/common/template/pdp_template.jsp'>

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
        <%-- Bazaarvoice --%>
        <% 
        if(FDStoreProperties.isBazaarvoiceEnabled()){
          String bvapiUrl = FDStoreProperties.getBazaarvoiceBvapiUrl(); %>
          <script type="text/javascript" src="<%= bvapiUrl %>"></script>
        <% } %>
    	</tmpl:put>
	    <tmpl:put name='deptnav' direct='true'>
		    <% try { %><%@ include file="/common/template/includes/deptnav.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			<hr class="deptnav-separator">
	    </tmpl:put>
    <% } else { %>
    	<tmpl:put name="extraJs">
        <fd:javascript src="/assets/javascript/wine.js"/>
        <fd:javascript src="/assets/javascript/wine-nav.js"/>	
        <%-- Bazaarvoice --%>
        <% 
        if(FDStoreProperties.isBazaarvoiceEnabled()){
          String bvapiUrl = FDStoreProperties.getBazaarvoiceBvapiUrl(); %>
          <script type="text/javascript" src="<%= bvapiUrl %>"></script>
        <% } %>
    	</tmpl:put>
    	<tmpl:put name="deptnav" direct="true">	
    	</tmpl:put>
    	<tmpl:put name="leftnav">
			<td class="wine-sidenav" bgcolor="#e2dfcc" style="z-index: 0;" width="150"><div align="center" style="background: #272324"><a href="/department.jsp?deptId=usq&trk=snav"><img src="/media/editorial/win_usq/usq_logo_sidenav_bottom.gif" width="150" height="109" border="0"></a><br></div>
			<% try { %><%@ include file="/common/template/includes/left_side_nav_usq.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %>
			</td>    	
		</tmpl:put>
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

	<c:set value="${productPotato}" scope="request" var="productPotato" />
	<c:set value="${productExtraPotato}" scope="request" var="productExtraPotato" />
	
	<tmpl:put name='content' direct='true'>
		<fd:CmPageView wrapIntoScriptTag="true" productModel="<%=productNode%>"/>
		<fd:CmProductView quickbuy="false" wrapIntoScriptTag="true" productModel="<%=productNode%>"/>
		
		<jsp:include page="/includes/product/productDetail.jsp" >
			<jsp:param name="catId" value="${ param.catId }"/>
			<jsp:param name="productId" value="${ param.productId }"/>
		</jsp:include>
	</tmpl:put>

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
