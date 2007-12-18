<%
String tableWidth = "500";
String securityPolicyLink = "/help/privacy_policy.jsp";

String hdrImgName = "faq_hdr_security.gif"; 
String hdrImgWidth = "453";
String hdrImgHeight = "30";

if(request.getParameter("page")== null){
tableWidth = "375";
securityPolicyLink = "javascript:linkTo('/help/privacy_policy.jsp')";

hdrImgName = "faq_pop_hdr_security.gif"; 
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
			<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="375" HEIGHT="1"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
			<table cellpadding="0" cellspacing="0">
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question1">Is FreshDirect secure?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question2">Will my information be kept private?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question2">Does FreshDirect use cookies?</A></td></tr>
			</table>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question1"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>Is FreshDirect secure?</b><BR>
			Yes. We take every precaution to protect your privacy and to prevent misuse of the private information you provide us. Please note that while we make reasonable efforts to safeguard your personal information once we receive it, no transmission of data over the Internet or any other public network can be guaranteed to be 100% secure. <a href="<%=securityPolicyLink%>">Click here</a> to see our full security and privacy policies.<br>

	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question2"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>Will my information be kept private?</b><BR>
			When we ask you to enter private information, such as your credit card number, we encrypt it so that it can't be read by others. We use your information only to process orders, to provide an enhanced and personalized shopping experience, and to inform you of special FreshDirect offers and discounts. We will not release your personal information to any third party unless you have granted us explicit permission to do so.<br>

	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question3"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
			<b>Does FreshDirect use cookies?</b><BR>
			Like many Web sites, ours uses "cookies" to make your shopping experience faster. A cookie is a piece of data stored on your computer's hard drive that allows the FreshDirect Web site to identify you. Cookies cannot be used to access sensitive account information &#151; you will always have to log in with a user name and password to place an order. Please note that if you disable your browser from accepting cookies, certain features of the site may be disabled.<br>

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