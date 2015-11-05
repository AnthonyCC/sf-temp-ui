<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!

//expanded page dimensions
public static final int W_NO_SPACE_BORDER_TOTAL = 970;
%>

<%
/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}*/
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
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<CENTER CLASS="text10">
<table width="<%=W_NO_SPACE_BORDER_TOTAL%>" border="0" cellpadding="0" cellspacing="0">

<tr>
	<td valign="top"><img src="/media_stat/images/layout/clear.gif" width="<%=W_NO_SPACE_BORDER_TOTAL%>" height="5" border="0"></td>
</tr>

<tr valign="top">
	<td align="center">
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
	</td>
</tr>
</table>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
