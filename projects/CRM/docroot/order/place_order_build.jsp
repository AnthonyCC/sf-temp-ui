<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!	DecimalFormat quantityFormatter = new DecimalFormat("0.##");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM.dd.yyyy");
%>
<%	
    if ((request.getQueryString() != null) && request.getQueryString().equalsIgnoreCase("clearSearch")) {
        session.removeAttribute(SessionName.LIST_SEARCH_RAW);
    }
%>

<tmpl:insert template='/template/top_nav.jsp'>
	
	<tmpl:put name='title' direct='true'>New Order > Search Items</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<jsp:include page='/includes/order_header.jsp'/>
	
	<script language="JavaScript" type="text/javascript">
		<!--
		function init() {
			setfocus();
		}
		function setfocus() { 
			document.build_list.search_pad.focus(); 
		}
		function searchAnew(idx) {
			document.bulksearch.searchIndex.value=idx;
		}
	
		function confirmClearSearchPad() {
			var clearSearch = confirm("Are you sure you want to clear these terms?");
			if (clearSearch == true) {
				clearFormFields(document.bulksearch);
				window.location.href="/order/place_order_build.jsp?clearSearch";
			}
		}
	// -->
	</script>

<div class="order_content">
<table width="100%" cellpadding="2" cellspacing="0" border="0" class="order">
	<form action="/order/place_order_batch_search_results.jsp" method="post" name="bulksearch" id="bulksearch">
	<tr valign="top">
	<td width="70%">
		Please enter customer's items here. Separate items with a comma. <br>
		<textarea name="search_pad" wrap="virtual" style="width: 400px;" rows="10"><%= (String) session.getAttribute(SessionName.LIST_SEARCH_RAW) %></textarea><br><br>
		<input type="submit" value="SEARCH" class="submit"> <input type="reset" class="clear" value="CLEAR"><br>
		<br>
		</td>

	<td width="30%">
	Department Checklist...<br>
			<div class="column" style="padding: 4px; width: 50%;">
				Bakery<br>
				Cheese<br>
				Coffee/Tea<br>
				Dairy<br>
				Deli<br>
				Frozen<br>
				Fruit<br>
				Grocery<br>
			</div>
			<div class="column" style="padding: 4px; width: 50%;">
				Meat<br>
				Meals<br>
				Pasta<br>
				Seafood<br>
				Specialty<br>
				Vegetables<br>
			</div>
	</td>
	</tr>
	<tr><td colspan="2"><b>Instructions</b><br>Click "<b>Search</b>" after entering a comma-separated list of items for the customer.<br>You will see a set of product matches for the items in your list.<br><br><b>Phone Handling Charge</b>:<br>An additional fee of <b>$9.99</b> is applied <b>after 3 phone orders</b> are placed for customer.<br>At the 4<sup>th</sup> phone order, the fee will appear on step 4 of checkout. You will have the option to waive this charge then.
	</td></tr>
	</form>
	</table>
</div>
	
<div class="order_list">
	<%@ include file="/includes/cart_header.jspf"%>
</div>
<br clear="all">
	</tmpl:put>

</tmpl:insert>
