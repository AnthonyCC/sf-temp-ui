<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.webapp.util.RequestUtil'%>


<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' />

<%
RequestUtil.appendToAttribute(request,"bodyOnLoad","FormChangeUtil.recordSignature('qs_cart',false)",";");
RequestUtil.appendToAttribute(request,"windowOnBeforeUnload","FormChangeUtil.warnOnSignatureChange('qs_cart')",";");
%>

<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
	String custFirstName = user.getFirstName();
	int firstNameLength = custFirstName.length();
	int firstNameLastIndex = firstNameLength - 1;
	String orderId = request.getParameter("orderId");
	String actionName = request.getParameter("fdAction");
	if (actionName == null) {
		actionName = "";
	}
%>
<fd:QuickShopController id="quickCart" orderId="<%= orderId %>" action="<%= actionName %>">
<fd:OrderHistoryInfo id='orderHistoryInfo'>
<% boolean showDetails = false; %>
<%@ include file="/quickshop/includes/order_nav.jspf" %>
			
<tmpl:insert template='/common/template/quick_shop_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Shop from This Order</tmpl:put>
   	<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br></font><a href="/quickshop/previous_orders.jsp"><img src="/media_stat/images/template/quickshop/qs_prev_orders_catnav.gif" width="80" height="38" border="0"></a><br><font class="space4pix"><br></font><%= orderNav.toString() %><br><br></tmpl:put>	
	<tmpl:put name='content' direct='true'>

<%
	DateFormat dateFormatter = new SimpleDateFormat("EEE MM/dd/yy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
	java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); 

%>


<%@ include file="/quickshop/includes/i_confirm_single.jspf" %>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="bodyCopy">
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
	<tr><td colspan="2" class="title18" align="center"><b><%= custFirstName.toUpperCase().charAt(0) + custFirstName.substring(1,firstNameLength).toLowerCase() %>'<%= custFirstName.toLowerCase().lastIndexOf("s") > -1 && custFirstName.toLowerCase().lastIndexOf("s") == firstNameLastIndex ? "" : "s" %> Order <%= currentOrder %></b></td></tr>
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	<tr><td bgcolor="#996699" height="1" colspan="2"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></td></tr>
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td></tr>
	<tr>
	    <td valign="top"><li></li></td>
	    <td class="text11"><b>Enter quantities in boxes on the left</b> (if you don't want an item, enter "0" quantity, or click the green button to zero all quantities).</td>
	</tr>
	<tr>
	    <td valign="top"><li></li></td>
	    <td class="text11"><b>Add the selected items to your cart</b> by clicking the orange button.</td>
	</tr>
</table>
<%	
	String qsPage = "shop_from_order.jsp"; 
	String qsDeptId = null;
	boolean hasDeptId = false;
	final boolean isStandingOrderPage = false;
%>
<%@ include file="/shared/quickshop/includes/i_vieworder.jspf" %>
<br>

		</tmpl:put>

</tmpl:insert>
</fd:OrderHistoryInfo>
</fd:QuickShopController>

