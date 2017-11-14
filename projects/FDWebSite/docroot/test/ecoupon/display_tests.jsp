<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>
<%@ page import="com.freshdirect.fdstore.ecoupon.model.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ page import="com.freshdirect.fdstore.FDProductInfo" %>
<%@ page import="com.freshdirect.fdstore.FDCachedFactory" %>
<%@ page import="java.util.*" %>

<%@ page import='com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*' %>
<%@ page import='java.io.*'%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import="java.util.Locale"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.display.FDCouponTag" %>

<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='oscache' prefix='oscache' %>


<fd:CheckLoginStatus guestAllowed='true' id="displayTestsUser" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>FDCoupon Display Test Page</title>

    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/yui.jspf" %>
	<style>
		.test_cont, .test_cont1 {
			margin: 6px;
		}
		.test_cont table {
			border: 1px solid #000;
			margin: 3px 0;
		}
		.test_cont table td {
			border-top: 1px solid #000;
		}
		.test_cont table td.bordR {
			border-right: 1px solid #000;
		}
		.test_cont table td.bordL {
			border-left: 1px solid #000;
		}
		.test_cont table td.bordLR {
			border-left: 1px solid #000;
			border-right: 1px solid #000;
		}
		.test_cont th {
			text-align: center;
			background-color: #ccc;
			font-weight: bold;
		}
	</style>
</head>
<body>
<%!
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<%
String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
%>
<fd:ProductGroup id='productNode' categoryId='<%= request.getParameter("catId") %>' productId='<%= request.getParameter("productId") %>'>
<%
	FDCustomerCoupon custCoupon_dispTest = displayTestsUser.getCustomerCoupon(productNode.getDefaultSku().getProductInfo(), EnumCouponContext.PRODUCT,productNode.getParentId(),productNode.getContentName());
	request.setAttribute("custCoupon", custCoupon_dispTest);
%>
	Product Page Test (PRODUCT context):
	<div class="test_cont1">
	<%
		// Handle no-product case
		if (productNode==null) {
		    throw new JspException("Product not found in Content Management System");
		} else if (productNode.isDiscontinued()) {
		    throw new JspException("Product Discontinued :"+request.getParameter("productId"));
		}



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
		%>
			<%
				SkuModel _dfltSku = (SkuModel)productNode.getSkus().get( 0 );
				FDProduct _fdprod = _dfltSku.getProduct();
				int leadTime = _fdprod.getMaterial().getLeadTime();
			%>
		<%if (FDStoreProperties.isAdServerEnabled()) {%>

    <div id='oas_ProductNote'>
			<script type="text/javascript">
				OAS_AD('ProductNote');
			</script>
    </div>

		<%} else {%>
		    <%@ include file="/shared/includes/product/i_product_quality_note.jspf" %>
		<%}%>


		<% if(leadTime > 0 && FDStoreProperties.isLeadTimeOasAdTurnedOff()) { %>
			<CENTER><span class="text11"><b>CURRENTLY AVAILABLE - </b></span><span class="text11rbold text11"><%=JspMethods.convertNumToWord(leadTime)%> DAY LEAD TIME</span>.<br /></CENTER>
			<span class="text11">To assure the highest quality, our chefs prepare this item to order. <br> Please order at least two days in advance<br>(for example, order on Thursday for Saturday delivery).</span><p />
		<% } %>

		<fd:FDShoppingCart id='cart' result='actionResult' action='<%= tgAction %>' successPage='<%= sPage %>' source='<%= request.getParameter("fdsc.source")%>' >
		<%  //hand the action results off to the dynamic include
			FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
			String cartMode = CartName.ADD_TO_CART;
			FDCartLineI templateLine = null ;

			request.setAttribute("actionResult", actionResult);
			request.setAttribute("user", user);
			request.setAttribute("productNode", productNode);
			request.setAttribute("cartMode",cartMode);
			request.setAttribute("templateLine",templateLine);
		%>
		<%@ include file="/includes/product/cutoff_notice.jspf" %>
		<%@ include file="/includes/product/i_dayofweek_notice.jspf" %>
		<!-- product layout : <%= prodPageLayout.getLayoutPath() %> -->

		<jsp:include page="<%= prodPageLayout.getLayoutPath() %>" flush="false"/>

		</fd:FDShoppingCart>
	</div>

	<hr />

		Orderline Test (PRODUCT context):
		<div class="test_cont1">
			<fd:FDShoppingCart id='cart' result='actionResult' source='<%= request.getParameter("fdsc.source")%>' >
				<% FDUserI user = (FDUserI) session.getAttribute(SessionName.USER); %>
				<logic:iterate id="orderLine" collection="<%= cart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
					<div style="margin-left: 8px; text-indent: -8px; font-weight:bold;">
						<%=orderLine.getDescription()%>
						<display:FDCoupon coupon="<%= user.getCustomerCoupon(orderLine, EnumCouponContext.PRODUCT) %>" contClass="fdCoupon_cartline"></display:FDCoupon>
					</div>
				</logic:iterate>
			</fd:FDShoppingCart>
		</div>

	<hr />

		Orderline Test (VIEWCART context):
		<div class="test_cont1">
			<fd:FDShoppingCart id='cart' result='actionResult' source='<%= request.getParameter("fdsc.source")%>' >
				<% FDUserI user = (FDUserI) session.getAttribute(SessionName.USER); %>
				<logic:iterate id="orderLine" collection="<%= cart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
					<div style="margin-left: 8px; text-indent: -8px; font-weight:bold;">
						<%=orderLine.getDescription()%>
						<display:FDCoupon coupon="<%= user.getCustomerCoupon(orderLine, EnumCouponContext.VIEWCART) %>" contClass="fdCoupon_cartline"></display:FDCoupon>
					</div>
				</logic:iterate>
			</fd:FDShoppingCart>
		</div>

	<hr />

		Orderline Test (CHECKOUT context):
		<div class="test_cont1">
			<fd:FDShoppingCart id='cart' result='actionResult' source='<%= request.getParameter("fdsc.source")%>' >
				<% FDUserI user = (FDUserI) session.getAttribute(SessionName.USER); %>
				<logic:iterate id="orderLine" collection="<%= cart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
					<div style="margin-left: 8px; text-indent: -8px; font-weight:bold;">
						<%=orderLine.getDescription()%>
						<display:FDCoupon coupon="<%= user.getCustomerCoupon(orderLine, EnumCouponContext.CHECKOUT) %>" contClass="fdCoupon_cartline"></display:FDCoupon>
					</div>
				</logic:iterate>
			</fd:FDShoppingCart>
		</div>

	<hr />

		Orderline Test (VIEWORDER context):
		<div class="test_cont1">
			<fd:FDShoppingCart id='cart' result='actionResult' source='<%= request.getParameter("fdsc.source")%>' >
				<% FDUserI user = (FDUserI) session.getAttribute(SessionName.USER); %>
				<logic:iterate id="orderLine" collection="<%= cart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
					<div style="margin-left: 8px; text-indent: -8px; font-weight:bold;">
						<%=orderLine.getDescription()%>
						<display:FDCoupon coupon="<%= user.getCustomerCoupon(orderLine, EnumCouponContext.VIEWORDER) %>" contClass="fdCoupon_cartline"></display:FDCoupon>
					</div>
				</logic:iterate>
			</fd:FDShoppingCart>
		</div>

	<hr />

	<% FDCustomerCoupon custCoupon = null; %>
	<fd:FDProductInfo id="productInfo" skuCode="<%= productNode.getDefaultSku().getSkuCode() %>">
		<% custCoupon = displayTestsUser.getCustomerCoupon(productInfo.getUpc(), EnumCouponContext.PRODUCT); %>
	</fd:FDProductInfo>

		FDCouponTag Tests (default Product Context):
		<div class="test_cont">
			<div class="fleft" style="width: 40%;">
				<strong>Manual Sets:</strong>
				<table>
					<tr>
						<th>Default (just coupon set)</th>
					</tr>
					<tr>
						<td><display:FDCoupon coupon="<%= custCoupon %>"></display:FDCoupon></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="2">showClipBox</th>
					</tr>
					<tr>
						<th>true</th>
						<th>false</th>
					</tr>
					<tr>
						<td class="bordR"><display:FDCoupon coupon="<%= custCoupon %>" showClipBox="true"></display:FDCoupon></td>
						<td><display:FDCoupon coupon="<%= custCoupon %>" showClipBox="false"></display:FDCoupon></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="2">isClipped</th>
					</tr>
					<tr>
						<th>true</th>
						<th>false</th>
					</tr>
					<tr>
						<td class="bordR"><display:FDCoupon coupon="<%= custCoupon %>" isClipped="true"></display:FDCoupon></td>
						<td><display:FDCoupon coupon="<%= custCoupon %>" isClipped="false"></display:FDCoupon></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="2">showCouponImage</th>
					</tr>
					<tr>
						<th>true</th>
						<th>false</th>
					</tr>
					<tr>
						<td class="bordR"><display:FDCoupon coupon="<%= custCoupon %>" showCouponImage="true"></display:FDCoupon></td>
						<td><display:FDCoupon coupon="<%= custCoupon %>" showCouponImage="false"></display:FDCoupon></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="2">couponImageUrl</th>
					</tr>
					<tr>
						<th>"/media/images/ecoupon/logo-small.gif"</th>
						<td><display:FDCoupon coupon="<%= custCoupon %>" couponImageUrl="/media/images/ecoupon/logo-small.gif"></display:FDCoupon></td>
					</tr>
					<tr>
						<th>"/media/images/ecoupon/badge-small.gif"</th>
						<td><display:FDCoupon coupon="<%= custCoupon %>" couponImageUrl="/media/images/ecoupon/badge-small.gif"></display:FDCoupon></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="2">showMsg</th>
					</tr>
					<tr>
						<th>true</th>
						<th>false</th>
					</tr>
					<tr>
						<td class="bordR"><display:FDCoupon coupon="<%= custCoupon %>" showMsg="true"></display:FDCoupon></td>
						<td><display:FDCoupon coupon="<%= custCoupon %>" showMsg="false"></display:FDCoupon></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="2">couponMsg</th>
					</tr>
					<tr>
						<th width="150">"Short Test Message"</th>
						<td><display:FDCoupon coupon="<%= custCoupon %>" couponMsg="Short Test Message"></display:FDCoupon></td>
					</tr>
					<tr>
						<th>"Long Test Message Test Message"</th>
						<td><display:FDCoupon coupon="<%= custCoupon %>" couponMsg="Long Test Message Test Message"></display:FDCoupon></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="2">showDetailsLink</th>
					</tr>
					<tr>
						<th>true</th>
						<th>false</th>
					</tr>
					<tr>
						<td class="bordR"><display:FDCoupon coupon="<%= custCoupon %>" showDetailsLink="true"></display:FDCoupon></td>
						<td><display:FDCoupon coupon="<%= custCoupon %>" showDetailsLink="false"></display:FDCoupon></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="2">showStatusMsg</th>
					</tr>
					<tr>
						<th>true</th>
						<th>false</th>
					</tr>
					<tr>
						<td class="bordR"><display:FDCoupon coupon="<%= custCoupon %>" showStatusMsg="true"></display:FDCoupon></td>
						<td><display:FDCoupon coupon="<%= custCoupon %>" showStatusMsg="false"></display:FDCoupon></td>
					</tr>
				</table>

			</div>
			<div class="test_cont fright" style="width: 59%;">
				<strong>Manual Calls (getContent() called after each set call):</strong>
				<%
					/* use display tag directly */
					FDCouponTag manFdCouponTag = new FDCouponTag();
					manFdCouponTag.setCoupon(custCoupon);
				%>
				<table>
					<tr>
						<th>Default (just coupon set), getContent()</th>
					</tr>
					<tr>
						<td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="4">setShowClipBox, getClipBoxHtml()</th>
					</tr>
					<tr>
						<th colspan="2">true</th>
						<th colspan="2">false</th>
					</tr>
					<tr>
						<td><% manFdCouponTag.setShowClipBox(true); %><%= manFdCouponTag.getClipBoxHtml() %></td><td class="bordLR"><%= manFdCouponTag.getContent(pageContext) %></td>
						<td class="bordR"><% manFdCouponTag.setShowClipBox(false); %><%= manFdCouponTag.getClipBoxHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setShowClipBox back to default, true */
					manFdCouponTag.setShowClipBox(true);
				%>

				<table>
					<tr>
						<th colspan="4">setIsClipped, getClipBoxHtml()</th>
					</tr>
					<tr>
						<th colspan="2">true</th>
						<th colspan="2">false</th>
					</tr>
					<tr>
						<td><% manFdCouponTag.setIsClipped(true); %><%= manFdCouponTag.getClipBoxHtml() %></td><td class="bordLR"><%= manFdCouponTag.getContent(pageContext) %></td>
						<td class="bordR"><% manFdCouponTag.setIsClipped(false); %><%= manFdCouponTag.getClipBoxHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setIsClipped back to default, false */
					manFdCouponTag.setIsClipped(false);
				%>

				<table>
					<tr>
						<th colspan="4">setShowCouponImage, getImageHtml()</th>
					</tr>
					<tr>
						<th colspan="2">true</th>
						<th colspan="2">false</th>
					</tr>
					<tr>
						<td><% manFdCouponTag.setShowCouponImage(true); %><%= manFdCouponTag.getImageHtml() %></td><td class="bordLR"><%= manFdCouponTag.getContent(pageContext) %></td>
						<td class="bordR"><% manFdCouponTag.setShowCouponImage(false); %><%= manFdCouponTag.getImageHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setIsClipped back to default, true */
					manFdCouponTag.setShowCouponImage(true);
				%>

				<table>
					<tr>
						<th colspan="3">setCouponImageUrl, getImageHtml()</th>
					</tr>
					<tr>
						<th>default</th><td class="bordLR"><% manFdCouponTag.getCouponImageUrl(); %><%= manFdCouponTag.getImageHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<tr>
						<th>"/media/images/ecoupon/badge-small.gif"</th><td class="bordLR"><% manFdCouponTag.setCouponImageUrl("/media/images/ecoupon/badge-small.gif"); %><%= manFdCouponTag.getImageHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setIsClipped back to default, null */
					manFdCouponTag.setCouponImageUrl(null);
				%>

				<table>
					<tr>
						<th colspan="4">setShowMsg(), getMsgHtml()</th>
					</tr>
					<tr>
						<th colspan="2">true</th>
						<th colspan="2">false</th>
					</tr>
					<tr>
						<td><% manFdCouponTag.setShowMsg(true); %><%= manFdCouponTag.getMsgHtml() %></td><td class="bordLR"><%= manFdCouponTag.getContent(pageContext) %></td>
						<td class="bordR"><% manFdCouponTag.setShowMsg(false); %><%= manFdCouponTag.getMsgHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setShowMsg back to default, true */
					manFdCouponTag.setShowMsg(true);
				%>

				<table>
					<tr>
						<th colspan="6">setCouponMsg(), getMsgHtml()</th>
					</tr>
					<tr>
						<th>"Short Test Message"</th>
						<td class="bordR"><% manFdCouponTag.setCouponMsg("Short Test Message"); %><%= manFdCouponTag.getMsgHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<tr>
						<th>"Long Test Message Test Message"</th>
						<td class="bordR"><% manFdCouponTag.setCouponMsg("Long Test Message Test Message"); %><%= manFdCouponTag.getMsgHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setCouponMsg back to default, null */
					manFdCouponTag.setCouponMsg(null);
				%>

				<table>
					<tr>
						<th colspan="4">setShowDetailsLink(), getDetailsHtml()</th>
					</tr>
					<tr>
						<th colspan="2">true</th>
						<th colspan="2">false</th>
					</tr>
					<tr>
						<td><% manFdCouponTag.setShowDetailsLink(true); %><%= manFdCouponTag.getDetailsHtml() %></td><td class="bordLR"><%= manFdCouponTag.getContent(pageContext) %></td>
						<td class="bordR"><% manFdCouponTag.setShowDetailsLink(false); %><%= manFdCouponTag.getDetailsHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setCouponMsg back to default, true */
					manFdCouponTag.setShowDetailsLink(true);
				%>

				<table>
					<tr>
						<th colspan="2">getDetailsHtml() (no override)</th>
					</tr>
					<tr>
						<td class="bordR"><%= manFdCouponTag.getDetailsHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>

				<table>
					<tr>
						<th colspan="4">setCouponDetailsText(), getCouponDetailsText(), getDetailsContentHtml() (hidden)</th>
					</tr>
					<tr>
						<th>null</th>
						<td class="bordR" style="width: 200px;"><%= manFdCouponTag.getCouponDetailsText() %></td><td class="bordR" style="background-color: #eee; width: 25px;"><%= manFdCouponTag.getDetailsContentHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<tr>
						<th>"Short Test Message"</th>
						<td class="bordR"><% manFdCouponTag.setCouponDetailsText("Short Test Message"); %><%= manFdCouponTag.getCouponDetailsText() %></td>
						<td class="bordR" style="background-color: #eee; width: 25px;"><%= manFdCouponTag.getDetailsContentHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<tr>
						<th>"Long Test Message Test Message"</th>
						<td class="bordR"><% manFdCouponTag.setCouponDetailsText("Long Test Message Test Message"); %><%= manFdCouponTag.getCouponDetailsText() %></td>
						<td class="bordR" style="background-color: #eee; width: 25px;"><%= manFdCouponTag.getDetailsContentHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setCouponMsg back to default, null */
					manFdCouponTag.setCouponDetailsText(null);
				%>

				<table>
					<tr>
						<th colspan="4">[setCouponStatusText("status msg")] setShowStatusMsg(), getStatusTextHtml()</th>
					</tr>
					<tr>
						<th colspan="2">true</th>
						<th colspan="2">false</th>
					</tr>
					<tr>
						<td><% manFdCouponTag.setCouponStatusText("status msg"); manFdCouponTag.setShowStatusMsg(true); %><%= manFdCouponTag.getStatusTextHtml() %></td><td class="bordLR"><%= manFdCouponTag.getContent(pageContext) %></td>
						<td class="bordR"><% manFdCouponTag.setShowStatusMsg(false); %><%= manFdCouponTag.getStatusTextHtml() %></td><td><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
				</table>
				<%
					/* set setShowStatusMsg back to default, true */
					manFdCouponTag.setShowStatusMsg(true);
					manFdCouponTag.setCouponStatusText(null);
				%>

				<table>
					<tr>
						<th colspan="3">setCouponStatusText(), getStatusTextHtml()</th>
					</tr>
					<tr>
						<th>"status msg"</th>
						<td><% manFdCouponTag.setCouponStatusText("status msg"); %><%= manFdCouponTag.getStatusTextHtml() %></td><td class="bordL"><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<%
						/* set setCouponStatusText back to default, null (so it'll auto-set correctly on value changes) */
						manFdCouponTag.setCouponStatusClass(null);
					%>
					<tr>
						<th>"Min. qty not met"</th>
						<td><% manFdCouponTag.setCouponStatusText("Min. qty not met"); %><%= manFdCouponTag.getStatusTextHtml() %></td><td class="bordL"><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<%
						/* set setCouponStatusText back to default, null (so it'll auto-set correctly on value changes) */
						manFdCouponTag.setCouponStatusClass(null);
					%>
					<tr>
						<th>"min qty not met"</th>
						<td><% manFdCouponTag.setCouponStatusText("min qty not met"); %><%= manFdCouponTag.getStatusTextHtml() %></td><td class="bordL"><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<%
						/* set setCouponStatusText back to default, null (so it'll auto-set correctly on value changes) */
						manFdCouponTag.setCouponStatusClass(null);
					%>
					<tr>
						<th>"Coupon applied"</th>
						<td><% manFdCouponTag.setCouponStatusText("Coupon applied"); %><%= manFdCouponTag.getStatusTextHtml() %></td><td class="bordL"><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<%
						/* set setCouponStatusText back to default, null (so it'll auto-set correctly on value changes) */
						manFdCouponTag.setCouponStatusClass(null);
					%>
					<tr>
						<th>"coupon applied"</th>
						<td><% manFdCouponTag.setCouponStatusText("coupon applied"); %><%= manFdCouponTag.getStatusTextHtml() %></td><td class="bordL"><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<%
						/* set setCouponStatusText back to default, null (so it'll auto-set correctly on value changes) */
						manFdCouponTag.setCouponStatusClass(null);
					%>
					<tr>
						<th>"Coupon expired"</th>
						<td><% manFdCouponTag.setCouponStatusText("Coupon expired"); %><%= manFdCouponTag.getStatusTextHtml() %></td><td class="bordL"><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<%
						/* set setCouponStatusText back to default, null (so it'll auto-set correctly on value changes) */
						manFdCouponTag.setCouponStatusClass(null);
					%>
					<tr>
						<th>"coupon expired"</th>
						<td><% manFdCouponTag.setCouponStatusText("coupon expired"); %><%= manFdCouponTag.getStatusTextHtml() %></td><td class="bordL"><%= manFdCouponTag.getContent(pageContext) %></td>
					</tr>
					<%
						/* set setCouponStatusText back to default, null (so it'll auto-set correctly on value changes) */
						manFdCouponTag.setCouponStatusClass(null);
					%>
				</table>
				<%
					/* set setCouponStatusText back to default, false */
					manFdCouponTag.setCouponStatusText(null);
				%>

			</div>
		</div>

</fd:ProductGroup>

</body>
</html>
