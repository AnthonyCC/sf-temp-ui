<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML>
<HEAD>
    <title><tmpl:get name='title'/></title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</HEAD>
<BODY BGCOLOR="#ffffff" TEXT="#333333" CLASS="text10">
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%" HEIGHT="100%">
<TR VALIGN="MIDDLE">
	<TD WIDTH="100%" ALIGN="CENTER">
		<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="520">
		<TR VALIGN="TOP">
			<TD WIDTH="160"><img src="/media_stat/images/template/homepages/home_grapes.jpg" width="170" height="141" border="0" alt="Grapes"></TD>
			<TD WIDTH="10"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="1" BORDER="0"></TD>
			<TD WIDTH="350" CLASS="text12">
				<BR><img src="/media_stat/images/logos/fd_logo_lg.gif" width="245" height="52" border="0" alt="FreshDirect"><BR>
				<IMG src="/media_stat/images/layout/clear.gif" WIDTH="310" HEIGHT="10" BORDER="0"><BR>

			<table>
			<tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
				<td>
				<tmpl:get name='content'/>
				</td>
			</tr>
			</table>	


			</TD>
		</TR>
		</TABLE>
	</TD>
</TR>
</TABLE>
<br>
</BODY>
</HTML>
