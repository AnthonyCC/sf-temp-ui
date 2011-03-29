<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><tmpl:get name='title'/></title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333"  MARGINHEIGHT="0" MARGINWIDTH="0" TOPMARGIN="0" LEFTMARGIN="0" onLoad="window.focus()">
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="550">
<TR VALIGN="TOP">
<TD COLSPAN="3" WIDTH="550"><A HREF="javascript:window.close();"><img src="/media_stat/images/template/pop_up_header_lg.gif" width="550" height="43" border="0" alt="FreshDirect"></A><BR>
<FONT CLASS="space10pix"><BR></FONT>
</TD>
</TR>
<TR>
<TD VALIGN="BOTTOM" WIDTH="10"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
<TD VALIGN="TOP" WIDTH="480"><P>
<tmpl:get name='content'/>
</TD>
<TD VALIGN="BOTTOM" WIDTH="10"><IMG src="/media_stat/images/layout/clear.gif"
WIDTH="10" HEIGHT="1" HSPACE="0" VSPACE="0"></TD>
</TR>
<TR VALIGN="TOP">
<TD COLSPAN="3" WIDTH="550" ALIGN="RIGHT">
<A HREF="javascript:window.close();">close window</A>&nbsp;&nbsp;</TD>
</TR>
</TABLE>
</BODY>
</HTML>
