<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_LEFT_DNAV_TOTAL = 970;
final int W_LEFT_DNAV_LEFT = 150;
final int W_LEFT_DNAV_RIGHT = 820;
%>

<%
/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}
		*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
	<tmpl:get name='customhead'/>
	<tmpl:get name='facebookmeta'/>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%if (FDStoreProperties.isAnnotationMode()) {
%>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
	<fd:javascript src="/assets/javascript/overlib_mini.js"/>
<%	} %>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<CENTER CLASS="text10">
<table width="<%=W_LEFT_DNAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
<tr>
<td colspan="2"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
</tr>

<tr>
<td colspan="2"><%@ include file="/common/template/includes/deptnav.jspf" %></td>
</tr>

<tr>
<td width="<%=W_LEFT_DNAV_LEFT%>" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
<td width="<%=W_LEFT_DNAV_RIGHT%>" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
</tr>

<tr valign="top">
	<td bgcolor="#E0E3D0" class="lNavTableContTD"><%@ include file="/common/template/includes/left_side_nav.jspf" %><BR>
		<img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_LEFT_DNAV_LEFT%>"></td>
		<td align="right"><img src="/media_stat/images/layout/clear.gif" alt="" height="15" width="1"><br>
		<%-- content lands here --%>
		<tmpl:get name='content'/>
		<%-- content ends above here--%>
		<br><br></td>
</tr>
</table>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
