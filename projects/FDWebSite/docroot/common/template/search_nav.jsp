<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.webapp.util.SiteFeatureUtils"%>

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
		<table width="743" cellpadding="0" cellspacing="0">
			<tr>
				<td><img src="/media_stat/images/template/search/search_deptnav.gif" width="130" height="36" alt="" border="0"></td>
				<td align="right" style="padding-right: 8px;">
					<%
					if (SiteFeatureUtils.isEnabled(EnumSiteFeature.NEW_SEARCH,request)) {
						%>
											<img src="/media_stat/images/layout/star11.gif" width="11" height="11" vspace="0"> <a href="/search2.jsp?q=<%=request.getParameter("searchParams")%>"><b>New!</b> Alternate search results view &raquo;</a>
						<% 
					}
					%>
				</td>
			</tr>
		</table>
	</TD>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34"></TD>
</TR>
<TR>
	<TD WIDTH="745" BGCOLOR="#999966" COLSPAN="6"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="1" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0"></td>
	<TD WIDTH="170" COLSPAN="2" BGCOLOR="#E0E3D0"><BR><BR>
		
		<table width="170" border="0" cellspacing="0" cellpadding="1" align="center">
		<FORM name="adv_search" action="/search.jsp" method="GET">
		<TR><TD WIDTH="10" ROWSPAN="17"><BR></TD><TD COLSPAN="2" width="150"><font class="text11pkbold">Search</font></td><TD WIDTH="10" ROWSPAN="17"><BR></TD></tr>
		<tr>
			<TD WIDTH="150" COLSPAN="2"><INPUT TYPE="text" name="searchParams" value="<%= request.getParameter("searchParams") %>" size="16" maxlength="50" class="text11" STYLE="width:140px"></td></tr>
		<%--
		<tr><td width="15"><input type="radio" name="words" checked></TD><TD class="text11" WIDTH="135"><B>All</B> of the Words</td></tr>
		--%>
		<tr><TD WIDTH="150" COLSPAN="2"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="100" HEIGHT="6"></td></tr>
		<%--
		<tr><td COLSPAN="2" width="150">&nbsp;<font class="text11pkbold">Find Only</font><br></TD></TR>
		<tr><td width="15"><input type="checkbox" name="kosher" value="true" <%= request.getParameter("kosher")!=null ? "CHECKED" :"" %>></TD><TD WIDTH="135">Kosher<br></TD></TR>
		<tr><td width="15"><input type="checkbox" name="organic" value="true" <%= request.getParameter("organic")!=null ? "CHECKED" :"" %>></TD><TD WIDTH="135">Organic</td></tr>
		<tr><TD WIDTH="150" COLSPAN="2"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="100" HEIGHT="8"></td></tr>
		--%>
		<tr><TD WIDTH="150" COLSPAN="2"><input name="submit" type="image" src="/media_stat/images/navigation/global_nav/nav_button_find.gif" WIDTH="35" HEIGHT="14" BORDER="0" ALIGN="absmiddle" ALT="FIND" ></td></tr>
		<TR>
			<td COLSPAN="2" align="right" width="150" class="text11bold"><FONT CLASS="space2pix"><br></FONT>
			<IMG src="/media_stat/images/layout/999999.gif" WIDTH="150" HEIGHT="1" VSPACE="2"><BR>
				<TABLE WIDTH="150" CELLPADDING="0" CELLSPACING="0" BORDER="0">
				<TR>
					<TD WIDTH="135"><a href="/search.jsp">Search Tips</A></TD>
					<TD WIDTH="15"><a href="/search.jsp"><img src="/media_stat/images/template/search/tips_icon.gif" WIDTH="15" HEIGHT="15" ALT="Tips" BORDER="0" VSPACE="4"></a></TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
		</form>
		</TABLE>
		
		
		<BR>
		<img src="/media_stat/images/layout/clear.gif" height="1" width="170">
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
