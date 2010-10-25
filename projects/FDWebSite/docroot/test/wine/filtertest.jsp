<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<fd:CheckLoginStatus id="user" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.freshdirect.fdstore.content.WineFilter"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.fdstore.content.EnumWinePrice"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.DomainValue"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.EnumWineRating"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Filter Test</title>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body>
<%
WineFilter filter = new WineFilter(user.getPricingContext());
filter.addFilterValue(EnumWineRating.FIVE);
filter.addFilterValue(ContentFactory.getInstance().getDomainValueForWineCategory((CategoryModel) ContentFactory.getInstance().getContentNode("Category", "usq_aus")));
%>
		<table cellpadding="0" cellspacing="0" width="550" border="0">
			<tr valign="top">
				<logic:iterate id='contentNode' collection="<%=filter.getProducts()%>"
						type="com.freshdirect.fdstore.content.ProductModel">
					<%
						ProductModel productNode = contentNode;
									String rating = "";
									String price = null;
									String basePrice = null;
									boolean hasWas = false; // is deal?
					%>
					<fd:ProduceRatingCheck>
					<%
						rating = JspMethods.getProductRating(productNode);
					%>
					</fd:ProduceRatingCheck>
					<fd:FDProductInfo id="productInfo" skuCode="<%=productNode.getDefaultSku().getSkuCode()%>">
					<%
						price = JspMethods.formatPrice(productInfo, user.getPricingContext());
						
						hasWas = productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).isItemOnSale();
						if (hasWas) {
							basePrice = JspMethods.formatSalePrice(productInfo,user.getPricingContext());
						}
					%>
					</fd:FDProductInfo>
					<%
						String actionURI = FDURLUtil.getProductURI(productNode, "wineFilterTest");
					%>
					<%-- display a product --%>
					<td align="center" WIDTH="105">
					<display:ProductImage product="<%= productNode %>" action="<%= actionURI %>" />
					<%
						if (productNode.isFullyAvailable()) {
					%>
					<div>
						<display:ProductName product="<%= productNode %>" action="<%= actionURI %>"/>
					</div>
					<%
						} else {
					%>
					<div style="color: #999999">
						<display:ProductName product="<%= productNode %>" action="<%= actionURI %>"/>							
					</div>
					<%
						}
						if (rating != null && rating.trim().length() > 0) {
					%>
					<div class="center">
						<img name="<%=rating%>" src="/media_stat/images/ratings/<%=rating%>.gif" width="59"
								height="11" alt="" border="0">
					</div>
					<%
						}
						if (hasWas) {
					%>
					<div style="FONT-WEIGHT: bold; FONT-SIZE: 8pt; COLOR: #c00"><%=price%></div>
					<div style="FONT-SIZE: 7pt; COLOR: #888">(was <%=basePrice%>)</div>
					<%
						} else {
					%>
					<div class="favoritePrice"><%=price%></div>
					<%
						}
					%>
					</td>
					<td width="10">
						<img src="/media/images/layout/clear.gif" width="8" height="1">
					</td>
				</logic:iterate>
			</tr>
		</table>
</body>
</html>