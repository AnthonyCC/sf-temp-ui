<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>
<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_PAYMENT_INFO_TOTAL = 970;
%>

<%request.setAttribute("listPos", "CategoryNote");%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<% String actionName =  request.getParameter("actionName"); %>
<tmpl:insert template='/common/template/dnav.jsp'>
   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Payment Options</tmpl:put>
    <tmpl:put name='content' direct='true'>
<fd:PaymentMethodController actionName='<%=actionName%>' result='result'>

<%
boolean hasCheck = false;
boolean hasEBT = false;
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
        }else if (EnumPaymentMethodType.EBT.equals(paymentM.getPaymentMethodType())) {
            	hasEBT = true;
        }
	}
	
    isECheckRestricted = FDCustomerManager.isECheckRestricted(identity); 
	
}
boolean isCheckEligible	= user.isCheckEligible();

%>

<!-- error message handling here -->
<TABLE WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
    <TR>
        <TD width="<%= W_YA_PAYMENT_INFO_TOTAL %>" class="text11"><font class="title18">Payment Options</font><br><span class="space2pix"><br></span>
Update your payment information.<br>
To learn more about our <b>Security Policies</b>, <a href="javascript:popup('/help/faq_index.jsp?show=security','large')">click here</a>
<br><fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
        </td>
    </tr>
</table>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><br><br>

<SCRIPT LANGUAGE=JavaScript>
	<!--
	OAS_AD('CategoryNote');
	//-->
</SCRIPT> 

<fd:GetStandingOrderDependencyIds id="standingOrderDependencyIds" type="paymentMethod">
	<fd:GetStandingOrderHelpInfo id="helpSoInfo">
		<script type="text/javascript">var helpSoInfo=<%=helpSoInfo%>;</script>
		<% if (isCheckEligible && !isECheckRestricted) { %>
		
			<% if (hasCheck) { %>
		<table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
		    <td><img src="/media_stat/images/headers/check_acct_details.gif" width="181" height="9" alt="CHECKING ACCOUNT DETAILS">&nbsp;&nbsp;&nbsp;<a href="javascript:popup('/registration/checkacct_terms.jsp','large')">Terms of Use</a><br>
		    <IMG src="/media_stat/images/layout/999966.gif" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" height="1" border="0" VSPACE="3"><br>
		    </td>
		</tr>
		<tr valign="middle">
			<td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another checking account: <a href="/your_account/add_checkacct.jsp"><IMG src="/media_stat/images/buttons/add_new_acct.gif" width="108" height="16" ALT="Add New Checking Account" border="0" ALIGN="absmiddle"></a></td>
			</tr>
			<tr><td>
				<form name=checkacct_form method="post">
				<input type="hidden" name="actionName" value="">
				<input type="hidden" name="deletePaymentId" value="">
				<%@ include file="/includes/ckt_acct/checkacct_select.jspf" %>
				</form>
			</td>
		</tr>
		</table>
			<% } else {%>
				<%@ include file="/includes/your_account/add_checkacct.jspf"%> 
			<% } %>
			<br><br>
		<% } %>
		<table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
		    <td><img src="/media_stat/images/navigation/credit_card_details.gif"
		WIDTH="152" HEIGHT="15" border="0" alt="CREDIT CARD DETAILS">&nbsp;&nbsp;&nbsp;<BR>
		    <IMG src="/media_stat/images/layout/999966.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
		    </td>
		</tr>
		<tr valign="middle">
			<td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another credit card: <a href="/your_account/add_creditcard.jsp"><IMG src="/media_stat/images/buttons/add_new_credit_card.jpg" WIDTH="137" HEIGHT="16" ALT="Add New Credit Card" BORDER="0" ALIGN="absmiddle"></a>
			</td>
			</tr>
			<tr><td>
				<form name=creditcard_form method="post">
				<input type="hidden" name="actionName" value="">
				<input type="hidden" name="deletePaymentId" value="">
				<%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %>
				</form>
			</td></tr>
		</table>
		<br>
		<% if(user.isEbtAccepted()|| hasEBT){ %>
		<table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
		    <td><img src="/media_stat/images/navigation/ebt_card_details.gif" WIDTH="119" HEIGHT="11" border="0" alt="EBT CARD DETAILS">&nbsp;&nbsp;&nbsp;<BR>
		    <IMG src="/media_stat/images/layout/999966.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
		    </td>
		</tr>
		<%if(user.isEbtAccepted()){ %>
		<tr valign="middle">
			<td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another EBT card: <a href="/your_account/add_ebt_card.jsp"><IMG src="/media_stat/images/buttons/add_new_ebt_card.jpg" WIDTH="117" HEIGHT="16" ALT="Add New Credit Card" BORDER="0" ALIGN="absmiddle"></a>
			</td>
			</tr>
			<% } %>
			<tr><td>
				<form name=ebtcard_form method="post">
				<input type="hidden" name="actionName" value="">
				<input type="hidden" name="deletePaymentId" value="">
				<%@ include file="/includes/ckt_acct/i_ebtcard_select.jspf" %>
				</form>
			</td></tr>
		</table>
		<% } %>
		<br>
		<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0"><BR>
		<FONT CLASS="space4pix"><BR><BR></FONT>
		<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>">
		<TR VALIGN="TOP">
		<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></TD>
		<TD WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL - 35 %>"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
		<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
		</TR>
		</TABLE>
	</fd:GetStandingOrderHelpInfo>
</fd:GetStandingOrderDependencyIds>
<BR>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>
