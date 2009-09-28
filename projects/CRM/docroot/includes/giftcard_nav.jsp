<%@ taglib uri='crm' prefix='crm' %>
<%
	String snav_pageURI = request.getRequestURI();
	boolean summary_gc = snav_pageURI.indexOf("giftcard/index.jsp") > -1 || snav_pageURI.indexOf("gift_card/giftcard_summary.jsp") > -1;
	boolean bulk_purchase_gc = snav_pageURI.indexOf("bulk") > -1;
		boolean bulk_purchase_gc_s1 = false;
		boolean bulk_purchase_gc_s2 = false;
	boolean purchase_history = snav_pageURI.indexOf("history") > -1;
	boolean purchase_gc=false;
		boolean purchase_gc_s1 = false;
		boolean purchase_gc_s2 = false;
if(bulk_purchase_gc){
	//steps inside bulk purchase
	bulk_purchase_gc_s1 = snav_pageURI.indexOf("/add_bulk_giftcard.jsp") > -1;
	bulk_purchase_gc_s2 = snav_pageURI.indexOf("/purchase_bulk_giftcard.jsp") > -1;
}
if(!bulk_purchase_gc){
	purchase_gc = (snav_pageURI.indexOf("add_giftcard") + snav_pageURI.indexOf("/purchase_giftcard")) > -1;
		//steps inside single purchase
		purchase_gc_s1 = snav_pageURI.indexOf("/add_giftcard.jsp") > -1;
		purchase_gc_s2 = snav_pageURI.indexOf("/purchase_giftcard.jsp") > -1;
}
	boolean isReceiptPage = snav_pageURI.indexOf("/gift_card/purchase/receipt.jsp") > -1;
	boolean delete_reservations = snav_pageURI.indexOf("delete_reservations") > -1;
	boolean mass_cancellation = snav_pageURI.indexOf("mass_cancellation") > -1;
	boolean mass_returns = snav_pageURI.indexOf("mass_returns") > -1;
	boolean delivery_restrictions = snav_pageURI.indexOf("delivery_restrictions") > -1;
	boolean address_restrictions = snav_pageURI.indexOf("address_restrictions") > -1;
	boolean giftcard_added = snav_pageURI.indexOf("giftcard_addused.jsp") > -1;
	boolean giftcard_generate = snav_pageURI.indexOf("generate_new_giftcard.jsp") > -1;
	boolean bt_giftcard = snav_pageURI.indexOf("bt_giftcard.jsp") > -1;
	boolean check_balance = snav_pageURI.indexOf("check_balance") > -1;
	//System.out.println("calling the giftcard nav11"+purchase_gc);
%>

<div class="cust_nav_gc" style="height: 28px;">
	<div class="cust_nav_gc_header" style="width: 30px;"><img src="/media_stat/crm/images/giftcard_box.gif" width="24" height="27" alt="" /></div>
	<div class="cust_nav_gc_header" style="width: 18%;"><div class="cust_nav_gc_header_text">Gift&nbsp;Card:
			<% if (summary_gc) { %>Summary
			<% } else if (purchase_gc) { %>Purchase<%
				if (purchase_gc_s1) { %>
					&nbsp;<span class="gcStepOn">1</span>&nbsp;<span class="gcStepOff">2</span>
				<% } else if (purchase_gc_s2) { %>
					&nbsp;<a href="/gift_card/purchase/add_giftcard.jsp" class="gcStepOff">1</a>&nbsp;<span class="gcStepOn">2</span>
				<% } %>
			<% } else if (bulk_purchase_gc) { %>Buy&nbsp;in&nbsp;Bulk<%
				if (bulk_purchase_gc_s1) { %>
					&nbsp;<span class="gcStepOn">1</span>&nbsp;<span class="gcStepOff">2</span>
				<% } else if (bulk_purchase_gc_s2) { %>
					&nbsp;<a href="/gift_card/purchase/add_bulk_giftcard.jsp" class="gcStepOff">1</a>&nbsp;<span class="gcStepOn">2</span>
				<% } %>
			<% } else if (purchase_history) { %>Purchase&nbsp;History
			<% } else if (check_balance) { %>Check&nbsp;Balance
			<% } else if (giftcard_added) { %>Added/Used
			<% } else if (giftcard_generate) { %>Generate&nbsp;New
			<% } else if (bt_giftcard) { %>Balance&nbsp;Transfer
			<% } else if (isReceiptPage) { %>Receipt
			<% } %>
		</div>
	</div>
	<div class="cust_nav_gc_header" style="float: left; width: 78%;">
		<a href="/gift_card/giftcard_summary.jsp" class="<% if (summary_gc) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Summary</a>

		<a href="/gift_card/purchase/add_giftcard.jsp" class="<% if (purchase_gc) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Purchase</a>
		
		<a href="/gift_card/purchase/add_bulk_giftcard.jsp" class="<% if (bulk_purchase_gc) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Buy&nbsp;in&nbsp;Bulk</a>

		<a href="/gift_card/purchase_history.jsp?showAllPurchased=true" class="<% if (purchase_history) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Purchase&nbsp;History</a>

		<a href="#" onClick="checkBalanceShow(); return false;" class="<% if (check_balance) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Check&nbsp;Balance</a>

		<a href="/gift_card/giftcard_addused.jsp" class="<% if (giftcard_added) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Added/Used</a>

		<a href="/gift_card/generate_new_giftcard.jsp" class="<% if (giftcard_generate) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Generate&nbsp;New</a>

		<a href="/gift_card/bt_giftcard.jsp" class="<% if (bt_giftcard) { %>cust_gc_sub_nav_on<% 
			}else{ %>cust_sub_nav_text cust_gc_sub_nav<% } %>">Balance&nbsp;Transfer</a>

        <div class="cust_gc_sub_nav"><a href="#" onclick="checkAddrShow(); return false;" class="cust_sub_nav_text">Check&nbsp;Delivery&nbsp;Area</a></div>
	</div>
</div>

<% // include file that has all the popup dialog sources %>
	<%@ include file="/gift_card/postbacks/dialogs.jspf" %>