<%@page import="com.freshdirect.fdstore.content.util.WineSorter"%>
<%@page import="com.freshdirect.fdstore.content.ProductRatingGroup"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.util.EnumWineViewType"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.util.EnumWinePageSize"%>
<%@page import="com.freshdirect.fdstore.content.util.QueryParameter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>

<% //expanded page dimensions
final int W_WINE_FILTER_TOTAL = 601;
%>

<div style="width: <%=W_WINE_FILTER_TOTAL%>px; text-align: center; margin: 0px auto;">
<fd:Parameters id="params">
<fd:WineFilter filterId="wineFilter" lastClickedId="lastClicked" queryId="wineQuery">
<fd:WineSorter sorterId="wineSorter">
<fd:TrackingQueryParams id="trackingQueryTag"/>

<% List<ProductRatingGroup> rawGroups = wineSorter.getResults();
   String trk = trackingQueryTag.getSource() != null ? trackingQueryTag.getSource().getValue() : null; %>
<!-- header -->
<fd:CmPageView wrapIntoScriptTag="true" wineFilterValue="<%= request.getParameter(QueryParameter.WINE_CLEAR_FILTER_CLICKED)==null && wineFilter!=null && wineFilter.isFiltering() ? lastClicked : null%>"/>

<!-- view chooser -->
<div class="wine-view-chooser">View: 
	<fd:WineViewLink view="<%= EnumWineViewType.DETAILS %>"><span class="wine-view-link">Details</span></fd:WineViewLink> | 
	<fd:WineViewLink view="<%= EnumWineViewType.COMPACT %>"><span class="wine-view-link">Compact</span></fd:WineViewLink> 
</div>

<!-- sort by panel -->
<hr>
<table width="<%=W_WINE_FILTER_TOTAL%>" cellpadding="0" cellspacing="0" border="0" style="padding-top: 4px;">
<tr>
<td valign="middle" align="right">
Sort by:<%
   for (WineSorter.Type sortBy : WineSorter.Type.values()) {
	   if (sortBy.equals(WineSorter.Type.EXPERT_RATING) && !"USQ".equalsIgnoreCase(JspMethods.getWineAssociateId()) ) {
		   continue;
	   }
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
	<% String oRatingKeyMedia = "/media/editorial/win_"+JspMethods.getWineAssociateId().toLowerCase()+"/other_ratings_key.html";  %>
	<fd:IncludeMedia name="<%= oRatingKeyMedia %>" />
<% } %>
</fd:WineSorter>
</fd:WineFilter>
</fd:Parameters>
</div>
