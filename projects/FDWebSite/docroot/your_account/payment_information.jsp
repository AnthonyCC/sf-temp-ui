<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%request.setAttribute("listPos", "CategoryNote");%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<%-- bring in the common javascript functions  --%>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>

<% String actionName =  request.getParameter("actionName"); %>
<tmpl:insert template='/common/template/dnav.jsp'>
   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Payment Options</tmpl:put>
    <tmpl:put name='content' direct='true'>
<fd:PaymentMethodController actionName='<%=actionName%>' result='result'>
<%
boolean hasCheck = false;
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
Collection paymentMethods = null;
FDIdentity identity = null;
boolean isECheckRestricted = false;
if(user!=null  && user.getIdentity()!=null) {
    identity = user.getIdentity();
    paymentMethods = FDCustomerManager.getPaymentMethods(identity);
	
	Iterator payItr = paymentMethods.iterator();
	while (payItr.hasNext()) {
  		ErpPaymentMethodI  paymentM = (ErpPaymentMethodI) payItr.next();
 		if (EnumPaymentMethodType.ECHECK.equals(paymentM.getPaymentMethodType())) {
            	hasCheck = true;
        }
	}
	
    isECheckRestricted = FDCustomerManager.isECheckRestricted(identity); 
	
}
boolean isCheckEligible	= user.isCheckEligible();

System.out.println("The identity = "+identity);

%>

<!-- error message handling here -->
<TABLE WIDTH="675" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR><TD width="675" class="text11"><font class="title18">Payment Options</font><br><span class="space2pix"><br></span>
Update your payment information.
<br>
To learn more about our <b>Security Policies</b>, <a href="javascript:popup('/help/faq_index.jsp?show=security','large')">click here</a>
<br><fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr></table>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><br><br>

	<SCRIPT LANGUAGE=JavaScript>
				<!--
				OAS_AD('CategoryNote');
				//-->
	</SCRIPT> 

<% if (isCheckEligible && !isECheckRestricted) { %>

	<% if (hasCheck) { %>
		<table width="675" border="0" cellspacing="0" cellpadding="2">
		<tr valign="top">
		<td><img src="/media_stat/images/headers/check_acct_details.gif" width="181" height="9" alt="CHECKING ACCOUNT DETAILS">&nbsp;&nbsp;&nbsp;<a href="javascript:popup('/registration/checkacct_terms.jsp','large')">Terms of Use</a><br>
		<IMG src="/media_stat/images/layout/999966.gif" width="675" height="1" border="0" VSPACE="3"><br></td>
		</tr>
			<tr valign="middle">
			<td class="text11">If you need to enter another checking account: <a href="/your_account/add_checkacct.jsp"><IMG src="/media_stat/images/buttons/add_new_acct.gif" width="108" height="16" ALT="Add New Checking Account" border="0" ALIGN="absmiddle"></a></td>
			</tr>
			<tr>
			<form name=checkacct_form method="post">
			<input type="hidden" name="actionName" value="">
			<input type="hidden" name="deletePaymentId" value="">
			<td><%@ include file="/includes/ckt_acct/checkacct_select.jspf" %></td>
			</form>
			</tr>
		</table>
	<% } else {%>
		<%@ include file="/includes/your_account/add_checkacct.jspf"%> 
	<% } %>
	<br><br>
<% } %>
<table width="675" border="0" cellspacing="0" cellpadding="2">
<tr valign="top">
<td><img src="/media_stat/images/navigation/credit_card_details.gif"
WIDTH="132" HEIGHT="9" border="0" alt="CREDIT CARD DETAILS">&nbsp;&nbsp;&nbsp;<BR>
<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3"><BR></td>
</tr>
	<tr valign="middle">
	<td class="text11">If you need to enter another credit card: <a href="/your_account/add_creditcard.jsp"><IMG src="/media_stat/images/buttons/add_new_card.gif" WIDTH="96" HEIGHT="16" ALT="Add New Credit Card" BORDER="0" ALIGN="absmiddle"></a>
	</td>
	</tr>
	<tr>
	<form name=creditcard_form method="post">
	<input type="hidden" name="actionName" value="">
	<input type="hidden" name="deletePaymentId" value="">
	<td><%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %></td>
	</form>
	</tr>
</table>
<br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<TR VALIGN="TOP">
<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></TD>
<TD WIDTH="640"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
</TR>
</TABLE>
<BR>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>