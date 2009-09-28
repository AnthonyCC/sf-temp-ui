<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import="com.freshdirect.webapp.util.AccountUtil" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />

<%!
	java.text.SimpleDateFormat cutoffDateFormat = new java.text.SimpleDateFormat("h:mm a 'on' EEEE, MM/d/yy");
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<tmpl:insert template='/common/template/robinhood.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Donation Sign Up</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:DonationBuyerController actionName='registerRobinHoodBuyer' result="result" registrationType='30'>
<fd:ErrorHandler result='<%=result%>' field='<%=checkGiftCardBuyerForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='fraud' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>


<%
FDSessionUser sessionuser = (FDSessionUser) session.getAttribute( SessionName.USER );
FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER );
request.setAttribute("donation", "true");
UserUtil.initializeCartForDonationOrder(user);
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>

<%@ include file="/robin_hood/includes/i_rh_donationTotal.jspf" %>

<form name="address" method="post" >
	<%@ include file="/robin_hood/includes/i_rh_signup.jspf" %>
</form>

 

</fd:DonationBuyerController>
</tmpl:put>
</tmpl:insert>

