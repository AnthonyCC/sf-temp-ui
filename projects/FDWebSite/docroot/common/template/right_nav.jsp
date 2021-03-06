<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<% //expanded page dimensions
	final int W_RIGHT_NAV_TOTAL = 970;
	final int W_RIGHT_NAV_LEFT = 765;
	final int W_RIGHT_NAV_CENTER_PADDING = 14;
	final int W_RIGHT_NAV_RIGHT = 191;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
	<tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<style>
		.W_RIGHT_NAV_TOTAL { width: <%= W_RIGHT_NAV_TOTAL %>px; }
		.W_RIGHT_NAV_LEFT { width: <%= W_RIGHT_NAV_LEFT %>px; }
		.W_RIGHT_NAV_CENTER_PADDING { width: <%= W_RIGHT_NAV_CENTER_PADDING %>px; }
		.W_RIGHT_NAV_RIGHT { width: <%= W_RIGHT_NAV_RIGHT %>px; }
	</style>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333"
	data-pagetype="<tmpl:get name='pageType'/>"
>
<%@ include file="/common/template/includes/globalnav.jspf" %>
	<section class="container">
		<center CLASS="text10">
		<TABLE class="W_RIGHT_NAV_TOTAL" BORDER="0" CELLPADDING="0" CELLSPACING="0">
		<TR>
		<td class="W_RIGHT_NAV_TOTAL" COLSPAN="3" valign="top"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
		</TR>
		<TR>
		<TD class="W_RIGHT_NAV_LEFT"><img src="/media_stat/images/layout/clear.gif" alt="" class="W_RIGHT_NAV_LEFT" height="1" border="0"></TD>
		<TD class="W_RIGHT_NAV_CENTER_PADDING"><img src="/media_stat/images/layout/clear.gif" alt="" class="W_RIGHT_NAV_CENTER_PADDING" height="1" border="0"></TD>
		<TD class="W_RIGHT_NAV_RIGHT"><img src="/media_stat/images/layout/clear.gif" alt="" class="W_RIGHT_NAV_RIGHT" height="1" border="0"></TD>
		</TR>
		<TR VALIGN="TOP">
		<TD class="W_RIGHT_NAV_LEFT" align="center" VALIGN="TOP"><%-- content lands here --%><tmpl:get name='content'/><%-- content ends above here--%><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="14"></TD>
		<TD class="W_RIGHT_NAV_CENTER_PADDING">
		<TD class="W_RIGHT_NAV_RIGHT" align="center">
		<img src="/media_stat/images/layout/clear.gif" alt="" height="10" width="1"><br>
		<%@ include file="/common/template/includes/right_side_nav.jspf" %>
		</TD>
		</TR>
		<tmpl:get name='banner2'/>
		<TR VALIGN="BOTTOM">
		<td class="W_RIGHT_NAV_TOTAL" COLSPAN="3"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
		</TR>
		</TABLE>
		</center>
	</section>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
