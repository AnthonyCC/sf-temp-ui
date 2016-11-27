<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %><%
	/*
	 *	This file gets included on site_access pages
	 *		logic for additional includes to include belong in this file
	 */
%><%
	// check for gift card usage
	boolean isGiftCardEnabled = FDStoreProperties.isGiftCardEnabled();
	// check for robin hood usage
	boolean isRobinHoodEnabled = FDStoreProperties.isRobinHoodEnabled();
%><%
	/*
	 *	if either gc or robin hood, add a table & row to the page
	 *	Then for each add a column. Then in that column, add the include call.
	 */

	if (isGiftCardEnabled || isRobinHoodEnabled) {
%>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /><br />
		<img src="/media_stat/images/layout/cccccc.gif" width="541" height="1" alt="" /><br />
		<img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" /><br />	
		<table class="siteAccessInc" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<% if (isRobinHoodEnabled) { %>
					<td class="siteAccessInc_RobinHood">
						<%@ include file="/site_access/includes/i_robin_hood.jspf" %>
					</td>
				<% } %>
				<% if (isGiftCardEnabled && isRobinHoodEnabled) { %>
					<td class="siteAccessInc_spacer_GCRH"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" /></td>
				<% } %>
				<% if (isGiftCardEnabled) { %>
					<td class="siteAccessInc_GiftCard">
						<%@ include file="/site_access/includes/i_gift_card.jspf" %>
					</td>
				<% } %>
			</tr>
		</table>
<% } %>