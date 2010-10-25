<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collection"%>
<%@page import="com.freshdirect.common.pricing.PricingContext"%>
<%@page import="com.freshdirect.fdstore.content.ProductRatingGroup"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.Html"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.util.EnumWineViewType"%>
<%@page import="com.freshdirect.fdstore.content.util.WineSorter"%>
<%@page import="com.freshdirect.fdstore.content.util.EnumWinePageSize"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.pricing.ProductPricingFactory"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="java.util.Collection"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.Html"%>
<%@page import="com.freshdirect.fdstore.content.EnumWineFilterDomain"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%
String trk = "cpage";
CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", request.getParameter("catId"));
boolean isFilter = ContentFactory.getInstance().getDomainValueForWineCategory(category) != null;
if (!isFilter) {
	for (EnumWineFilterDomain domain : EnumWineFilterDomain.values()) {
		CategoryModel domainCategory = domain.getCategory();
		if (domainCategory != null && domainCategory.equals(category)) {
			isFilter = true;
			break;
		}
	}
}
if (isFilter) {
%>
<jsp:include page="wine_filter.jsp"/><%
} else {
	%>
	<div style="width: 425px; text-align: center; margin: 0px auto;">
	<!-- header -->
	<table width="425" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td>
				<div class="title18" style="text-align: left; padding: 8px 0px;"><%= category.getFullName() %></div>
				<fd:IncludeHtml html="<%= category.getEditorial() %>"/>
				<div style="padding: 8px 0px;">
					<div class="usq-middlebrown-border" style="font-size: 0px; border-width: 8px 0px 0px;">&nbsp;</div>
				</div>
			</td>
		</tr>
	</table>
	<fd:Parameters id="params">
	<fd:WineFilter filterId="wineFilter" lastClickedId="lastClicked" queryId="wineQuery" useItemGrabber="true">
	<fd:WineSorter sorterId="wineSorter">
	<fd:TrackingQueryParams id="trackingQueryTag"/>

	<% List<ProductRatingGroup> rawGroups = wineSorter.getResults(); %>
	<!-- header -->
	
	<!-- sort by panel -->
	<hr>
	<table width="425" cellpadding="0" cellspacing="0" border="0">
	<tr>
	<td valign="middle">
	Sort by:<%
	   for (WineSorter.Type sortBy : WineSorter.Type.values()) { 
	      %><fd:WineSortByLink sortBy="<%= sortBy %>" className="wine-sortby"><span class="wine-sortby-<%= sortBy.name().toLowerCase() %><%= selected ? "-selected" : "" %>"></span></fd:WineSortByLink><%
	   } %>
	</td>
	</tr>
	</table>
	<hr>
	<fd:WinePager pagedProductsId="groups">
	<div style="text-align: left;">
	<div style="float: right; white-space: nowrap; vertical-align: top; text-align: right; padding-left: 1em;">
	Per page:
	<fd:WinePageSizeLink pageSize="<%= EnumWinePageSize.TWENTY %>"><b>20</b></fd:WinePageSizeLink> | 
	<fd:WinePageSizeLink pageSize="<%= EnumWinePageSize.FOURTY %>"><b>40</b></fd:WinePageSizeLink> |
	<fd:WinePageSizeLink pageSize="<%= EnumWinePageSize.ALL %>"><b>ALL</b></fd:WinePageSizeLink>
	</div>
	<b>Page:</b>
	<% for (int i = 1; i <= winePageCount; i++) { %>
	<fd:WinePageNoLink pageNo="<%= i %>"/>
	<% } %>(<%= wineProductCount %> products)
	<div style="clear: both;"></div>
	</div>
	</fd:WinePager>

	<!-- body -->
	<%
	boolean useProductCategory = true;
	%>
	<%@include file="/includes/wine/i_wine_details_view.jspf" %>	
	
	<% if (!groups.get(0).getProducts().isEmpty()) { %>
	<%@ include file="/shared/includes/wine/i_wine_expert_ratings_key.jspf" %>
	<% } %>
	<% if (wineHasOtherRatings) { %>
	<fd:IncludeMedia name="/media/editorial/win_usq/other_ratings_key.html"/>
	<% } %>

	</fd:WineSorter>
	</fd:WineFilter>
	</fd:Parameters>
	</div>
<%
}
%>
