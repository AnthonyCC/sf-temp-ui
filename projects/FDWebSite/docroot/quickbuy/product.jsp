<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.net.URL"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.freshdirect.fdstore.content.EnumProductLayout"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineI"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.CartName" %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user"/>
<fd:ProductGroup id='productNode' categoryId='<%= request.getParameter("catId") %>' productId='<%= request.getParameter("productId") %>'>
<%
	final String tgAction = "addToCart";
	final String srcPage = request.getParameter("referer"); // mandatory parameter 
	final String srcTitle = request.getParameter("refTitle"); // mandatory parameter
	final String uid = request.getParameter("uid");
	final String iatcNamespace = request.getParameter("iatcNamespace");
	
	String protocol;
	String host;
	int port = 80;
	
	// extract HTTP protocol and host info
	try {
		URL url = new URL( srcPage );
		protocol = url.getProtocol();
		host = url.getHost();
		port = url.getPort();
	} catch (MalformedURLException exc) {
		throw new JspException(exc);
	}

	StringBuilder buf = new StringBuilder();
	// Workaround enforces URL to stick to the same protocol when redirect response is bounced back
	buf.append(protocol);
	buf.append("://");
	buf.append(host);
	if (port != 80 && port>-1) {
		buf.append(":").append(port);		
	}
	buf.append("/quickbuy/confirm.jsp");
	buf.append("?referer=").append(URLEncoder.encode(srcPage, "UTF-8"));
	if (srcTitle != null && !"".equals(srcTitle)) {
		buf.append("&refTitle=").append(srcTitle);
	}
	if (iatcNamespace != null)
		buf.append("&iatcNamespace=").append(iatcNamespace);
	final String sPage = buf.toString();
	
	final boolean __isWineLayout = EnumProductLayout.NEW_WINE_PRODUCT.equals(productNode.getProductLayout());
%>
<fd:FDShoppingCart id='cart' result='result' action='<%= tgAction %>' successPage='<%= sPage %>' source='<%= request.getParameter("fdsc.source")%>' >
<%
	FDCartLineI templateLine = null;
	String cartMode = CartName.ADD_TO_CART;
	
	// Check product existence and avaliability
	if ( productNode == null ) {
		throw new JspException( "Product not found in Content Management System" );
	} else if ( productNode.isDiscontinued() ) {
		throw new JspException( "Product Discontinued" );
	}

	// tell i_product.jspf it's quickbuy
	request.setAttribute("i_product_inner", Boolean.TRUE);
%>
<html>
<head>
	<%@ include file="/common/template/includes/metatags.jspf" %>

	<fd:javascript src="/assets/javascript/common_javascript.js"/>

	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
	<%
	if (FDStoreProperties.isAnnotationMode()) {
		%>

		<fd:javascript src="/assets/javascript/overlib_mini.js"/>

		<%
	}
	%>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body class="qbBody">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<fd:CmProductView quickbuy="true" wrapIntoScriptTag="true" productModel="<%=productNode%>"/>
<div id="qbContainer">
	<% if (FDStoreProperties.isAnnotationMode()) { %>
		<div id="overDiv" style="position: absolute; visibility: hidden; z-index: 10000;"></div>
	<% } %>
	<div class="left">
		<%@ include file="/shared/includes/product/i_product.jspf" %>
	</div>
	<div class="right">
		<div style="padding-bottom: 2em; text-align: right;">
		<% if (__isWineLayout) { %>
			<a class="title12" href="<%= FDURLUtil.getProductURI(productNode, request.getParameter("variant"), request.getParameter("trk"), "qb", -1) %>&amp;backPage=<%= URLEncoder.encode(srcPage, "UTF-8") %>&amp;refTitle=<%= URLEncoder.encode(srcTitle, "UTF-8") %>" target="_top">More about this product</a>
		<% } else { %>
			<a class="title12" href="<%= FDURLUtil.getProductURI(productNode, request.getParameter("variant"), request.getParameter("trk"), "qb", -1) %>" target="_top">More about this product</a>
		<% } %>
		</div>
	<%
	if (__isWineLayout) {
		if (productNode.getAlternateImage() != null) {
			Image descImage = productNode.getDescriptiveImage();
			if(descImage == null){
				descImage = new Image("/media/images/temp/soon_100x100.gif", 100, 100);
			}
	%>		<div>
				<img src="<%= productNode.getAlternateImage().getPath() %>" width="<%= productNode.getAlternateImage().getWidth() %>" height="<%= productNode.getAlternateImage().getHeight() %>" style="vertical-align: top;">
				<img src="<%= descImage.getPath() %>" width="<%= descImage.getWidth() %>" height="<%= descImage.getHeight() %>" style="vertical-align: top;">
			</div>
		<%
		} else {
%>		<div>
			<img src="<%= productNode.getProdImage().getPath() %>" width="<%= productNode.getProdImage().getWidth() %>" height="<%= productNode.getProdImage().getHeight() %>" style="vertical-align: top;">
		</div>
<%
		}
	} else {
	%>		<img src="<%= productNode.getDetailImage().getPath() %>" width="<%= productNode.getDetailImage().getWidth() %>" height="<%= productNode.getDetailImage().getHeight() %>">
	<%
	}
	%>
	</div>
	<% if (__isWineLayout) { %>
		<div class="text9" style="position: absolute; bottom: 1em; left: 10px; color: gray; width: 180px; text-align: right;">Wine sold by Union Square Wines &amp; Spirits.</div>
	<% } %>
	<% if (uid != null && !"".equals(uid)) { %>
		<script>
			YAHOO.util.Event.onDOMReady(function() {
				var frameId = '<%= uid %>_frame';

				setFrameHeight(frameId, 20);
				// re-center panel
				window.parent.document.quickbuyPanel.center();
			});
		</script>
	<% } %>
</div>
</body>
</html>
</fd:FDShoppingCart>
</fd:ProductGroup>
