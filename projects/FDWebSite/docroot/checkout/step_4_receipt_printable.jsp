<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import ='com.freshdirect.fdstore.survey.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.text.SimpleDateFormat, java.util.*" %>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.fdstore.sempixel.FDSemPixelCache' %>
<%@ page import='com.freshdirect.fdstore.sempixel.SemPixelModel' %>
<%@ page import='java.text.DecimalFormat' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!
// final java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_4_RECEIPT_TOTAL = 970;
%>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/blank.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Checkout - Order Placed"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Checkout - Order Placed</tmpl:put> --%>
<tmpl:put name='content' direct='true'>
<%
//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/checkout");
        request.setAttribute("listPos", "ReceiptTop,ReceiptBotLeft,ReceiptBotRight,SystemMessage,CategoryNote");

		FDSessionUser fdSessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
		MasqueradeContext masqueradeContext = fdSessionUser.getMasqueradeContext();

%>

<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL%>" style="margin-top: 5px;">
<TR VALIGN="BOTTOM">      
    <TD colspan="2" WIDTH="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL%>" ALIGN="RIGHT">
    </TD>
</TR>
<TR VALIGN="BOTTOM">
  <TD WIDTH="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL-200%>"><A HREF="/index.jsp"><img src="/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" border="0" alt=" redirect to FreshDirect homepage"></A></TD>
  <TD WIDTH="200" ALIGN="right">    
        <A HREF="javascript:window.print();" onMouseOver="swapImage('print','/media/images/navigation/global_nav/print_page_01.gif')" onMouseOut="swapImage('print','/media/images/navigation/global_nav/print_page.gif')"><img name="print" src="/media_stat/images/navigation/global_nav/print_page.gif" width="54" height="26" border="0" alt="PRINT PAGE"></A>
  </TD>
</TR>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<tr>
  <td colspan="2"><IMG src="/media_stat/images/layout/669933.gif" alt="" HEIGHT="4" WIDTH="<%=W_CHECKOUT_STEP_4_RECEIPT_TOTAL%>"></td>
</tr>
</TABLE>
<%
	//this needs to be BEFORE the i_checkout_receipt.jspf include, since it's used in it
	String sem_orderNumber = "0";
	sem_orderNumber = NVL.apply((String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER), "0");
%>

<%@include file="/checkout/includes/i_checkout_receipt.jspf"%>

</tmpl:put>
</tmpl:insert>