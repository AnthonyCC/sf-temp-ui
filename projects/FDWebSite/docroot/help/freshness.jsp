<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>	

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<HTML>
<HEAD>

<TITLE>FreshDirect - Our Freshness Guarantee</TITLE>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</HEAD>

<body bgcolor="#FFFFFF" text="#333333" onLoad="window.resizeTo(375,335); window.focus();">
<table border="0" cellpadding="0" cellspacing="0" align="center">
<%-- spacers --%>
<tr>
<td rowspan="6"><img src="/media_stat/images/layout/clear.gif" width="12" height="10"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="312" height="10"></td>
<td rowspan="6"><img src="/media_stat/images/layout/clear.gif" width="12" height="10"></td>
</tr>

<tr><td align="center"><img src="/media_stat/images/template/freshness/fresh_guar_hdr.gif" width="310" height="58" alt="" border="0" align="center"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" border="0"></td></tr>
<tr>
<td class="text11">
We take pride in the high quality of our fresh food and packaged goods. That's why we guarantee your satisfaction with every product, every time. If you are dissatisfied with any item in your order, please contact us right away &mdash; we want to make it right. E-mail us at <a href="mailto:<fd:GetServiceEmail />"><fd:GetServiceEmail /></a><% if(user != null) { %> or call <%=user.getCustomerServiceContact()%>. <%}%><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="16" alt="" border="0">
</td>
</tr>

<tr>
<td align="center"><img src="/media_stat/images/template/freshness/fresh_guar_photos.jpg" width="312" height="40" border="0"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8">
</td>
</tr>

<tr>
	<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>	
</tr>

<tr>
	<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br><img src="/media_stat/images/template/freshness/fresh_guar_logo.gif" width="128" height="25" alt="" border="0"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br><a href="javascript:window.close();">close window</a></td>	
</tr>
</table>

</body>
</HTML>
