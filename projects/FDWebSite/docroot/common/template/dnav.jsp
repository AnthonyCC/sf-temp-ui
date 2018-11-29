<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<% //expanded page dimensions
final int W_DNAV_TOTAL = 970;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">

<head>
    <tmpl:get name="seoMetaTag"/>
	<tmpl:get name='customhead'/>

	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<jwr:script src="/giftcards.js" useRandomParam="false" />
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/includes/sms_alerts/examples_layout.jspf" %>
	
	<jwr:style src="/giftcards.css" media="all" />

	<%
		if ( (request.getRequestURI().indexOf("/your_account/giftcards.jsp")>-1) || (request.getRequestURI().indexOf("/your_account/gc_order_details.jsp")>-1) ) {
			//do nothing
		} else { %>
			<%@ include file="/shared/template/includes/ccl.jspf" %>
	<% } %>
	<tmpl:get name='extraJs'/>
	<tmpl:get name='extraCss'/>
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body data-printdata="<tmpl:get name='printdata'/>" 
	data-pagetype="<tmpl:get name='pageType'/>" 
>
	<%@ include file="/common/template/includes/globalnav.jspf" %> 
	<center class="text10">
		<table role="presentation" width="<%=W_DNAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="<%=W_DNAV_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_DNAV_TOTAL%>" height="5" border="0"></td>
			</tr>
			<tr>
				<td width="<%=W_DNAV_TOTAL%>" class="deptnav-cont"><%@ include file="/common/template/includes/deptnav.jspf" %></td>
			</tr>
			<tr>
				<td width="<%=W_DNAV_TOTAL%>" bgcolor="#999966" colspan="7"><img src="/media_stat/images/layout/999966.gif" alt="" width="1" height="1"></td>
			</tr>
			<tr valign="TOP">
				<td width="<%=W_DNAV_TOTAL%>" align="center">
					<img src="/media_stat/images/layout/clear.gif" alt="" height="20" width="<%=W_DNAV_TOTAL%>"><br>
					<!-- content lands here -->
						<tmpl:get name='content'/>
					<!-- content ends above here-->
				<br><br></td>
			</tr>
			<%-- spacers --%>
			<tr valign="top">
				<td><img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_DNAV_TOTAL%>"></td>
			</tr>
			<tr valign="bottom">
				<td width="<%=W_DNAV_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_DNAV_TOTAL%>" height="5" border="0"></td>
			</tr>
		</table>
	</center>
	<%@ include file="/common/template/includes/footer.jspf" %>
	<tmpl:get name="soytemplates" />
	<tmpl:get name='jsmodules'/>
	<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	<tmpl:get name='extraJsModules'/>
</body>
</html>
