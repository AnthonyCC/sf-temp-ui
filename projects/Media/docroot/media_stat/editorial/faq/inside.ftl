<#include "fd-macros.ftl" />
<#setting locale="en_US"/>
<#assign isPageNull=parameters["isPageNull"]/>
<#assign customerServiceContact=parameters["customerServiceContact"]/>
<#assign careerLink=parameters["careerLink"]/>
<#if isPageNull>
<#assign tableWidth="375"/>
<#assign securityPolicyLink="javascript:linkTo('/help/privacy_policy.jsp')"/>
<#assign hdrImgName="faq_hdr_pop_jobs.gif"/> 
<#assign hdrImgWidth="380"/>
<#assign hdrImgHeight="30"/>
<#else>
<#assign tableWidth="500"/>
<#assign securityPolicyLink="/help/privacy_policy.jsp"/>
<#assign hdrImgName="faq_hrd_jobs.gif"/> 
<#assign hdrImgWidth="453"/>
<#assign hdrImgHeight="30"/>
</#if>
<#assign securityPolicyLink="/help/privacy_policy.jsp"/>
<#assign user=parameters["fd_user"]/>
<#assign helper=parameters["helper"]/>
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
			<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="${tableWidth}" HEIGHT="1"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
			<ul>
				<li><A HREF="#question1">How do I find out more about jobs at FreshDirect?</A></li>
				<li><A HREF="#question2">Where can I find corporate information about FreshDirect?</A></li>
			</ul>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question1"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>How do I find out more about jobs at FreshDirect?</b><BR>
			We're always looking for people to join our team!<br>
			<a href="${careerLink}"><b>Click here to view open positions</b></a>.
			<br>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question2"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>Where can I find corporate information about FreshDirect?</b><BR>
			If you are a member of the press or media please contact:
			<br><br>
			<table>
			<tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
				<td class="bodyCopy">
					${helper.includeMedia("/media/editorial/site_pages/inside_fd.html", parameters)}
				</td>
			</tr>
			</table>
			<br><br>
			For all other inquiries please e-mail <a href="mailto:${user.getCustomerServiceEmail()!"service@freshdirect.com"}">${user.getCustomerServiceEmail()!"service@freshdirect.com"}</a> or call us at ${customerServiceContact}.
			<br>
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
