<#include "fd-macros.ftl" />
<#setting locale="en_US"/>
<#assign isPageNull=parameters["isPageNull"]/>
<#assign eligibleForSignupPromotion=parameters["eligibleForSignupPromotion"]/>
<#assign maxSignupPromotion=parameters["maxSignupPromotion"]/>
<#if isPageNull>
<#assign tableWidth="375"/>
<#assign signupLink="javascript:linkTo('/registration/signup.jsp')"/>
<#assign forgotPasswordLink="javascript:linkTo('/login/forget_password_main.jsp')"/>
<#assign hdrImgName="faq_hdr_pop_signing_up.gif"/> 
<#assign hdrImgWidth="380"/>
<#assign hdrImgHeight="30"/>
<#else>
<#assign tableWidth="500"/>
<#assign signupLink="/registration/signup.jsp"/>
<#assign forgotPasswordLink="/login/forget_password_main.jsp"/>
<#assign hdrImgName="faq_hdr_signing_up.gif"/> 
<#assign hdrImgWidth="453"/>
<#assign hdrImgHeight="30"/>
</#if>
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
		<img src="/media_stat/images/template/help/${hdrImgName}" width="${hdrImgWidth}" height="${hdrImgHeight}" alt="" border="0">
	   </TD>
	</TR>
</TABLE>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="${tableWidth}" HEIGHT="1"><BR><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>

			<ul>
				<li><A HREF="#question1">How do I sign up?</A></li>
				<li><A HREF="#question2">What if I forget my password?</A></li>
			</ul>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question1"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>How do I sign up?</b><BR>
			Signing up is quick and simple. Just fill out our short registration form with your name, address, e-mail, and password, then start getting great food delivered to your door as soon as tomorrow. (You must be 18 years or older to register with us.) 
<#if eligibleForSignupPromotion>
			To get you to try us, we're giving you ${maxSignupPromotion} off your first order.
</#if>
			<a href="${signupLink}">Sign up now</a>!<br>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question2"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>What if I forget my password?</b><BR>
			Simple. Just <a href="${forgotPasswordLink}">click here</a>. We'll ask you to supply your user name and then we'll send you an e-mail containing a secure link that you can use to change your password and start shopping again.<br>
	   </TD>
	</TR>
</TABLE>
<BR>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<A HREF="#top"><img src="/media_stat/images/template/help/up_arrow.gif" width="17" height="9" hspace="0" vspace="4" border="0" align="left"><img src="/media/images/layout/clear.gif" width="6" height="1" border="0">top of page</A>
			<br><br><BR>
	   </TD>
	</TR>
</TABLE>
