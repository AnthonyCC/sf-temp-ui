<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<TABLE WIDTH="745" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<TD VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></TD>
	<TD WIDTH="165"><img src="/media_stat/images/layout/clear.gif" height="1" width="165" border="0"></TD>
	<TD width="568" align="center"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="568" HEIGHT="1"></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></TD>
	<TD VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"></td>
	<td width="733" COLSPAN="2" valign="top" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
	<td width="733" COLSPAN="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</TR>
<TR>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34"></TD>
	<TD WIDTH="743" COLSPAN="4">
		<table width="743" cellpadding="0" cellspacing="0"><tr><td><img src="/media_stat/images/template/search/search_deptnav.gif" width="130" height="36" alt="" border="0"></td>
		<td align="right" style="padding-right: 8px;"><a href="/search.jsp?searchParams=<%=request.getParameter("q")%>">Back to traditional search results view &raquo;</a></td></tr></table>
	</TD>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34"></TD>
</TR>
<TR>
	<TD WIDTH="745" BGCOLOR="#999966" COLSPAN="6"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="1" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0"></td>
	<TD WIDTH="170" COLSPAN="2" BGCOLOR="#E0E3D0"><BR><BR>
		
		<form name="advSearch" action="/search2.jsp" method="GET">
		<table width="150" border="0" cellspacing="0" cellpadding="1" align="center">
			<tr valign="bottom">
				<td width="100"><input type="text" name="q" value="<%= request.getParameter("q") %>" size="16" maxlength="50" class="text11" style="width:100px"></td>
				<td width="40"><input type="image" src="/media_stat/images/navigation/global_nav/nav_button_find.gif" width="35" height="14" border="0" alt="FIND" hspace="3" vspace="3" align="bottom" border="0"></td>
			</tr>
		</table>
		</form>

		<table width="150" border="0" cellspacing="0" cellpadding="1" align="center">
			<tr><td>
				<tmpl:get name='leftnav'/>
			</td></tr>
		</table>
		
		<table width="150" border="0" cellspacing="0" cellpadding="1" align="center">
			<tr><td colspan="2"><hr/></td></tr>
			<TR>
				<TD WIDTH="135"><a href="/search2.jsp">Search Tips</A></TD>
				<TD WIDTH="15"><a href="/search2.jsp"><img src="/media_stat/images/template/search/tips_icon.gif" WIDTH="15" HEIGHT="15" ALT="Tips" BORDER="0" VSPACE="4"></a></TD>
			</TR>
		</table>
		
	</TD>
	<td width="568" valign="top" align="center">
	<BR><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="568" HEIGHT="1" BORDER="0"><BR>
	<!-- content lands here -->
	<tmpl:get name='content'/>
	<!-- content ends above here-->
	<br><br></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></TD>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
	<TD WIDTH="5" BGCOLOR="#E0E3D0" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></TD>
	<TD WIDTH="165" BGCOLOR="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" height="1" width="1"></TD>
	<TD width="568" align="center"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></TD>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="BOTTOM">
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_left_curve_nav.gif" width="6" height="6" border="0"></td>
	<TD WIDTH="165" BGCOLOR="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></TD>
	<td width="568"><img src="/media_stat/images/layout/clear.gif" width="568" height="5" border="0"></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
	<td width="733" COLSPAN="2" BGCOLOR="#999966" VALIGN="BOTTOM"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
</TR>
</TABLE>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
