<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%
String snav_pageURI = request.getRequestURI();
boolean landing_gc = snav_pageURI.indexOf("/gift_card/giftcard_landing.jsp") > -1;
boolean delete_reservations = snav_pageURI.indexOf("delete_reservations") > -1;
boolean mass_cancellation = snav_pageURI.indexOf("mass_cancellation") > -1;
boolean mass_returns = snav_pageURI.indexOf("mass_returns") > -1;
boolean delivery_restrictions = snav_pageURI.indexOf("delivery_restrictions") > -1;
boolean address_restrictions = snav_pageURI.indexOf("address_restrictions") > -1;
boolean settlement_batch = snav_pageURI.indexOf("settlement_batch") > -1;
boolean check_balance = snav_pageURI.indexOf("checkBalanceShow") > -1;
%>


<div class="cust_nav_gc" style="height: 28px;">
	<div class="cust_nav_gc_header" style="width: 30px;"><img src="/media_stat/crm/images/giftcard_box.gif" width="24" height="27" alt="" /></div>
	<div class="cust_nav_gc_header" style="width: 18%;"><div class="cust_nav_gc_header_text">Gift&nbsp;Card:
			<% if (landing_gc) { %>Search <a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmGiftCardHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for Gift Card Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" class="help">?</a>
			<% } else if (check_balance) { %>Check&nbsp;Balance
			<% } %>
		</div>
	</div>
	<div class="cust_nav_gc_header" style="float: left; width: 78%;">
		<% if (landing_gc) { %>
			<div class="cust_gc_sub_nav_on"><span class="cust_nav_gc_on">Search</span></div>
		<% }else{ %>
			<div class="cust_gc_sub_nav"><a href="/gift_card/giftcard_landing.jsp" class="cust_sub_nav_text">Search</a></div>
		<% } %>

		<a href="#" onClick="checkBalanceShow(); return false;" class="<% if (check_balance) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Check&nbsp;Balance</a>
	</div>
</div>

<% // include file that has all the popup dialog sources %>
	<%@ include file="/gift_card/postbacks/dialogs.jspf" %>
