<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>
<%
String tableWidth = "500";
String securityPolicyLink = "/help/privacy_policy.jsp";
String hdrImgName = "faq_hdr_payment.gif"; 
String hdrImgWidth = "453";
String hdrImgHeight = "30";

boolean isCheckEligible = (user != null && user.isCheckEligible()); 

if(request.getParameter("page")== null){
tableWidth = "375";

securityPolicyLink = "javascript:linkTo('/help/privacy_policy.jsp')";
hdrImgName = "faq_hdr_pop_payment.gif"; 
hdrImgWidth = "380";
hdrImgHeight = "30";

}
%>

<script>
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
</script>
<A NAME="top"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD>
		<img src="/media_stat/images/template/help/<%=hdrImgName%>" width="<%=hdrImgWidth%>" height="<%=hdrImgHeight%>" alt="" border="0">
	   </TD>
	</TR>
</TABLE>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="<%=tableWidth%>" HEIGHT="1"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>			
			<li type="dot"><A HREF="#question1">How do I pay?</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt=""><br></li>
			<li type="dot"><A HREF="#question2">How does credit card payment work?</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt=""><br></li>
			<li type="dot"><A HREF="#question3">How does checking account payment work?</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt=""><br></li>
			<li type="dot"><A HREF="#question4">What is the minimum order?</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt=""><br></li>
			<li type="dot"><A HREF="#question5">Does FreshDirect accept coupons?</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" ><br></li>
			<li type="dot"><A HREF="#question6">Does FreshDirect accept personal checks or cash?</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt=""><br></li>
			<li type="dot"><A HREF="#question7">Does FreshDirect accept food stamps?</A></li>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
<A NAME="question1"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>How do I pay?</b><BR>
			FreshDirect accepts Visa, MasterCard, Discover, and American Express credit cards as well as debit cards bearing the MasterCard or Visa logo. You can also pay for your orders direct from your checking account. We do not accept food stamps or coupons at this time. 
			 </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
<A NAME="question2"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>How does credit card payment work?</b><BR>
			Your credit card will be charged on the day of delivery. You'll receive an e-mail confirming your final order total. We'll include a full, itemized printed receipt with your order. 
			<br><br>
			If your credit card has expired or is otherwise invalid, we'll be unable to process your order. Please note that we reserve the right to collect funds for any fees owed FreshDirect resulting from incorrect credit card information or over-the-limit accounts. 
			<br><br>
			We take every precaution to protect your credit card information. To review our privacy and security policies, <a href="<%=securityPolicyLink%>">click here</a>.				
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
<A NAME="question3"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>How does checking account payment work?</b><BR>
			Just enter your account and routing number when you check out. You'll receive an e-mail confirming your final order total and we'll include a full, itemized printed receipt with your order. The amount you owe will be automatically deducted from your account by your bank.
			<br><br>
			Please note that to pay for your order from a checking account you must have received and paid for at least two prior orders and there must be a valid credit card attached to your FreshDirect customer account. We reserve the right to collect funds for any fees owed FreshDirect resulting from incorrect account information or over-the-limit accounts. Our drivers cannot accept paper checks.<% if (isCheckEligible) { %>  For full details, <a href="javascript:popup('/pay_by_check.jsp','large')">click here</a>. <% } %>
			<br><br>
			We take every precaution to protect your account information. To review our privacy and security policies, <a href="<%=securityPolicyLink%>">click here</a>.
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
<A NAME="question4"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>What is the minimum order?</b><BR>
				The minimum order total is $<%= (int)user.getMinimumOrderAmount() %>, not including tax and the delivery fee.
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
<A NAME="question5"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>Does FreshDirect accept coupons?</b><BR>
			No. Coupons save you money on selected items, some of the time. We give you the lowest possible price on every item, every day, which amounts to a lower total cost for your entire order.
			</TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
<A NAME="question6"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>Does FreshDirect accept personal checks or cash?</b><BR>
			You can pay by check online. We do not accept paper checks, cashier's checks, money orders, or cash at this time.<br>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
<A NAME="question7"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>Does FreshDirect accept food stamps?</b><BR>
			Not at this time.<br>
	   </TD>
	</TR>
</TABLE>
<BR>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<A HREF="#top"><img src="/media_stat/images/template/help/up_arrow.gif" width="17" height="9" hspace="0" vspace="4" border="0" align="left"><img src="/media/images/layout/clear.gif" width="6" height="1" border="0">top of page</A>
			<br><br><BR>			
	   </TD>
	</TR>
</TABLE>