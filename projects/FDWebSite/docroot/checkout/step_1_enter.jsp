<%@ page import="java.net.*"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<%

    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

//	String addressId = request.getParameter("addressId");
StringBuffer redirectString = new StringBuffer();

redirectString.append("/login/login.jsp?successPage=");
redirectString.append(request.getRequestURI());
String requestQryString = request.getQueryString();

if (requestQryString !=null && requestQryString.trim().length() > 0 ) {
    redirectString.append(URLEncoder.encode("?"+request.getQueryString()));
}
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='<%=redirectString.toString()%>' />
<%
	double cartTotal = ((FDUserI)session.getAttribute(SessionName.USER)).getShoppingCart().getTotal();
%>
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Add Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:CheckLoginStatus guestAllowed="false" redirectPage='/checkout/signup_ckt.jsp' />
<fd:RegistrationController actionName="addDeliveryAddress" result="result" successPage='/checkout/step_1_choose.jsp?addressStatus=new'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<FORM name="address" method="post">
	<TR VALIGN="TOP">
	<TD CLASS="text11" WIDTH="675" VALIGN="bottom">
		<FONT CLASS="title18">Enter Delivery Address (Step 1 of 4)</FONT><BR>
		Please choose a delivery address for this order.<BR>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="675" HEIGHT="1" BORDER="0"></TD>
	</TR>
	</TABLE>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<BR>
	<BR>
	
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
		<TD WIDTH="675">
<%if(user.isDepotUser()){%>
			<img src="/media_stat/images/template/depot/home_address.gif" width="96" height="9" alt="" border="0">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT><BR>
<%}else{%>
			<img src="/media_stat/images/navigation/delivery_address.gif" WIDTH="116" HEIGHT="9" border="0" alt="DELIVERY ADDRESS">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT><BR>
<%}%>
			<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
		</TD>
	</TR>
	</TABLE>
	<%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br><br>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
	<TD WIDTH="675" ALIGN="RIGHT"><a href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>"><image name="checkout_delivery_address_cancel" src="/media_stat/images/buttons/cancel.gif" WIDTH="54" HEIGHT="16"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_delivery_address_add" src="/media_stat/images/buttons/save_changes.gif" WIDTH="84" HEIGHT="16" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0"></TD>
</TR>
</TABLE>
<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>