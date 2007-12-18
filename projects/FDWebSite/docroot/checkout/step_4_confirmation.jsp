<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.text.SimpleDateFormat, java.util.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity identity  = user.getIdentity();
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MM/dd/yy");
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/blank.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Order Placed</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
%>
<fd:GetOrder id='cart' saleId='<%=orderNumber%>'>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
<TR VALIGN="BOTTOM">
	<TD WIDTH="350">
		<A HREF="/index.jsp"><img src="/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" border="0" alt="FreshDirect"></A>&nbsp;&nbsp;&nbsp;&nbsp;<img src="/media_stat/images/navigation/egplnt_reg_checkout.jpg" WIDTH="87" HEIGHT="69" border="0" alt="Eggplant" HSPACE="10">
	</TD>
	<TD WIDTH="280" ALIGN="RIGHT">
		<A HREF="javascript:window.print();" onMouseOver="swapImage('print','/media/images/navigation/global_nav/print_page_01.gif')" onMouseOut="swapImage('print','/media/images/navigation/global_nav/print_page.gif')"><img name="print" src="/media_stat/images/navigation/global_nav/print_page.gif" width="54" height="26" border="0" alt="PRINT PAGE"></A>&nbsp;&nbsp;<IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="25">&nbsp;&nbsp;<A HREF="/index.jsp" onMouseOver="swapImage('home_top','/media/images/navigation/global_nav/go_home_01.gif')" onMouseOut="swapImage('home_top','/media/images/navigation/global_nav/go_home.gif')"><img name="home_top" src="/media_stat/images/navigation/global_nav/go_home.gif" width="56" height="26" border="0" alt="GO HOME"></A>&nbsp;&nbsp;<IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="25">&nbsp;&nbsp;<A HREF="/quickshop/index.jsp" onMouseOver="swapImage('quickshop','/media_stat/images/navigation/global_nav/nav_button_quickshop_r.gif')" onMouseOut="swapImage('quickshop','/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif')"><img name="quickshop" src="/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif" width="54" height="26" border=0" alt="QUICKSHOP"></A>
	</TD>
	<TD><BR></TD>
</TR>
<tr><td colspan=3><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<tr>
	<td colspan=3><IMG src="/media_stat/images/layout/669933.gif" HEIGHT="4" WIDTH="630"></td>
</tr>
<tr>
        <td colspan=3 width="630" align="left">
            <img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
            <img src="/media_stat/images/template/checkout/thankyou.gif" border="0"><br>
            Your feedback will help us determine our summer delivery areas.<br>
            <img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
        <IMG src="/media_stat/images/layout/669933.gif" HEIGHT="4" WIDTH="630">
        </td>
</tr>
</TABLE>

<%@include file="/checkout/includes/i_checkout_receipt.jspf"%>
</fd:GetOrder>
</tmpl:put>
</tmpl:insert>