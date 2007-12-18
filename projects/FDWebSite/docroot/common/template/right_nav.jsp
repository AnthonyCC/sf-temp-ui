<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}
*/%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
     <%@ include file="/shared/template/includes/ccl.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %>
<TABLE WIDTH="745" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR><td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"></td>
<td width="733" COLSPAN="2" valign="top" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
<td width="733" COLSPAN="2" valign="top"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</TR>
<TR>
<TD WIDTH="1" BGCOLOR="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></TD>
<TD WIDTH="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></TD>
<TD WIDTH="583"><img src="/media_stat/images/layout/clear.gif" width="583" height="1" border="0"></TD>
<TD WIDTH="150"><img src="/media_stat/images/layout/clear.gif" width="150" height="1" border="0"></TD>
<TD WIDTH="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></TD>
<TD WIDTH="1" BGCOLOR="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></TD>
</TR>
<TR VALIGN="TOP">
<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
<TD WIDTH="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></TD>
<TD width="583" align="center" VALIGN="TOP"><%-- content lands here --%><tmpl:get name='content'/><%-- content ends above here--%><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="14"></TD>
<TD WIDTH="155" COLSPAN="2" align="center">
<img src="/media_stat/images/layout/clear.gif" height="10" width="1"><br>
<%@ include file="/common/template/includes/right_side_nav.jspf" %>
</TD>
<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<tmpl:get name='banner2'/>
<TR VALIGN="BOTTOM">
<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0"></td>
<td width="733" COLSPAN="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
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
