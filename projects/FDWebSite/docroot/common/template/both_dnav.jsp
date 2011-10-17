<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_BOTH_DNAV_TOTAL = 970;
final int W_BOTH_DNAV_LEFT = 150;
final int W_BOTH_DNAV_PADDING = 14;
final int W_BOTH_DNAV_CENTER = 601;
final int W_BOTH_DNAV_RIGHT = 191;
%>

<html>
<head>
    <title><tmpl:get name='title'/></title>

		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		<%@ include file="/shared/template/includes/ccl.jspf" %>
<%
    {
       String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
       if (onbeforeunload != null && onbeforeunload.length() > 0) {
%>
    <script language="javascript">
       window.onbeforeunload = <%= onbeforeunload %>;
    </script>
<%
       } // if
    } // local block
%>

</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" 
     onload="<%= request.getAttribute("bodyOnLoad")%>" 
     onunload="<%= request.getAttribute("bodyOnUnload")%>" >
<%
	//
	// annotation mode, add overlib stuff
	//
	if (FDStoreProperties.isAnnotationMode()) {
%>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
	<script language="JavaScript" src="/assets/javascript/overlib_mini.js"></script>
<%	} %>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<CENTER class="text10">
<TABLE WIDTH="<%=W_BOTH_DNAV_TOTAL%>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<TD WIDTH="<%=W_BOTH_DNAV_LEFT%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_BOTH_DNAV_LEFT%>" height="1" border="0"></TD>
  <td width="<%=W_BOTH_DNAV_PADDING%>"></td>
	<TD WIDTH="<%=W_BOTH_DNAV_CENTER%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_BOTH_DNAV_CENTER%>" height="1" border="0"></TD>
	<td width="<%=W_BOTH_DNAV_PADDING%>"></td>
	<TD WIDTH="<%=W_BOTH_DNAV_RIGHT%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_BOTH_DNAV_RIGHT%>" height="1" border="0"></TD>
</TR>
<TR>
	<td width="<%=W_BOTH_DNAV_TOTAL%>" COLSPAN="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</TR>
<TR>
	<TD WIDTH="<%=W_BOTH_DNAV_TOTAL%>" COLSPAN="5">
	<%@ include file="/common/template/includes/deptnav.jspf" %>
	</TD>
</TR>
<TR>
	<TD WIDTH="<%=W_BOTH_DNAV_TOTAL%>" BGCOLOR="#999966" COLSPAN="5"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="<%=W_BOTH_DNAV_LEFT%>" BGCOLOR="#E0E3D0">
		<% try { %><%@ include file="/common/template/includes/left_side_nav.jspf" %><% } catch (Exception ex) {ex.printStackTrace();} %><BR>
		<img src="/media_stat/images/layout/clear.gif" height="1" width="<%=W_BOTH_DNAV_LEFT%>">
	</TD>
	<td width="<%=W_BOTH_DNAV_PADDING%>"></td>
	<TD width="<%=W_BOTH_DNAV_CENTER%>" align="center">
		<img src="/media_stat/images/layout/clear.gif" width="<%=W_BOTH_DNAV_CENTER%>" height="15"><br>
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
		<br><br>
	</TD>
	<td width="<%=W_BOTH_DNAV_PADDING%>"></td>
	<TD WIDTH="<%=W_BOTH_DNAV_RIGHT%>" align="center">
		<img src="/media_stat/images/layout/clear.gif" height="10" width="1" border="0"><br>
		<%@ include file="/common/template/includes/right_side_nav.jspf" %>
	</TD>
</TR>
</TABLE>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
