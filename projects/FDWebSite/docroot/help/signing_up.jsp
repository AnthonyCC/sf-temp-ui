<%@ page import='com.freshdirect.fdstore.promotion.*'%> 
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();

String tableWidth = "500";
String signupLink = "/registration/signup.jsp";
String forgotPasswordLink = "/login/forget_password_main.jsp";

String hdrImgName = "faq_hdr_signing_up.gif"; 
String hdrImgWidth = "453";
String hdrImgHeight = "30";

if(request.getParameter("page")== null){
tableWidth = "375";

signupLink = "javascript:linkTo('/registration/signup.jsp')";
forgotPasswordLink = "javascript:linkTo('/login/forget_password_main.jsp')";

hdrImgName = "faq_hdr_pop_signing_up.gif"; 
hdrImgWidth = "380";
hdrImgHeight = "30";
}%>

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
			<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="<%=tableWidth%>" HEIGHT="1"><BR><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
			<table cellpadding="0" cellspacing="0">
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question1">How do I sign up?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question2">What if I forget my password?</A></td></tr>
			</table>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question1"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>How do I sign up?</b><BR>
			Signing up is quick and simple. Just fill out our short registration form with your name, address, e-mail, and password, then start getting great food delivered to your door as soon as tomorrow. (You must be 18 years or older to register with us.) 
                        <% if (user.isEligibleForSignupPromotion()) {
                                java.text.DecimalFormat promoFormatter = new java.text.DecimalFormat("$#,##0");%>
                        To get you to try us, we're giving you <%=promoFormatter.format(user.getMaxSignupPromotion())%> off your first order.
                        <%}%>
                        <a href="<%=signupLink%>">Sign up now</a>!<br>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question2"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>What if I forget my password?</b><BR>
			Simple. Just <a href="<%=forgotPasswordLink%>">click here</a>. We'll ask you to supply your user name and then we'll send you an e-mail containing a secure link that you can use to change your password and start shopping again.<br>
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
