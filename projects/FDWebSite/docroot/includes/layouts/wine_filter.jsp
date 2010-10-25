<%@page import="com.freshdirect.fdstore.content.util.WineSorter"%>
<%@page import="com.freshdirect.fdstore.content.ProductRatingGroup"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.util.EnumWineViewType"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.util.EnumWinePageSize"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<div style="width: 425px; text-align: center; margin: 0px auto;">
<fd:Parameters id="params">
<fd:WineFilter filterId="wineFilter" lastClickedId="lastClicked" queryId="wineQuery">
<fd:WineSorter sorterId="wineSorter">
<fd:TrackingQueryParams id="trackingQueryTag"/>

<% List<ProductRatingGroup> rawGroups = wineSorter.getResults();
   String trk = trackingQueryTag.getSource() != null ? trackingQueryTag.getSource().getValue() : null; %>
<!-- header -->

<!-- view chooser -->
<div class="wine-view-chooser">View: 
	<fd:WineViewLink view="<%= EnumWineViewType.DETAILS %>"><span class="wine-view-link">Details</span></fd:WineViewLink> | 
	<fd:WineViewLink view="<%= EnumWineViewType.COMPACT %>"><span class="wine-view-link">Compact</span></fd:WineViewLink> 
</div>

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
	CategoryModel category = null;
	boolean useProductCategory = true;

	if (wineView == EnumWineViewType.COMPACT) { %>
<%@include file="/includes/wine/i_wine_compact_view.jspf" %>
<% } else { // wineView %>
<%@include file="/includes/wine/i_wine_details_view.jspf" %>
<% } %>
<%@ include file="/shared/includes/wine/i_wine_expert_ratings_key.jspf" %>
<% if (wineView == EnumWineViewType.DETAILS && wineHasOtherRatings) { %>
<fd:IncludeMedia name="/media/editorial/win_usq/other_ratings_key.html"/>
<% } %>
</fd:WineSorter>
</fd:WineFilter>
</fd:Parameters>
</div>