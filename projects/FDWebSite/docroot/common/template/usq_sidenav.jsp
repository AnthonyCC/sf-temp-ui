<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_USQ_SIDENAV_TOTAL = 970;
final int W_USQ_SIDENAV_LEFT = 150;
final int W_USQ_PADDING = 14;
final int W_USQ_SIDENAV_CENTER = 601;
final int W_USQ_SIDENAV_RIGHT = 191;
%>


<html>
<head>
    <title><tmpl:get name='title'/></title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%

	JspMethods.CategoryInfo c = JspMethods.getCategoryInfo(request);
	String tmplCatId = c.getCatId();
	String tmplFldrLbl = c.getFldrLbl();
	String tmplNavBar = c.getNavBar();
	
	String fldrLink = c.getLink();

	//
	// annotation mode, add overlib stuff
	//
	if (FDStoreProperties.isAnnotationMode()) {
%>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
	<script language="JavaScript" src="/assets/javascript/overlib_mini.js"></script>
<%	} %>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<CENTER CLASS="text10">
<table width="<%=W_USQ_SIDENAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td width="<%=W_USQ_SIDENAV_LEFT%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_SIDENAV_LEFT%>" height="1" border="0"></td>
	<td width="<%=W_USQ_PADDING%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_PADDING%>" height="1" border="0"></td>
	<td width="<%=W_USQ_SIDENAV_CENTER%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_SIDENAV_CENTER%>" height="1" border="0"></td>
	<td width="<%=W_USQ_PADDING%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_PADDING%>" height="1" border="0"></td>
	<td width="<%=W_USQ_SIDENAV_RIGHT%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_SIDENAV_RIGHT%>" height="1" border="0"></td>
</tr>
<tr valign="top">
	<td class="wine-sidenav" bgcolor="#e2dfcc" style="z-index: 0;"><div align="center" style="background: #272324"><a href="/department.jsp?deptId=usq&trk=snav"><img src="/media/editorial/win_usq/usq_logo_sidenav_bottom.gif" width="150" height="109" border="0"></a><br></div>
	<% try { %><%@ include file="/common/template/includes/left_side_nav_usq.jspf" %>
                <% } catch (Exception ex) {ex.printStackTrace();} %></td>
	<td width="<%=W_USQ_PADDING%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_PADDING%>" height="1" border="0"></td>
	<td align="center">   
                <!-- content lands here -->
         		<tmpl:get name='content'/>
                <!-- content ends above here-->
    </td>
	<td width="<%=W_USQ_PADDING%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_PADDING%>" height="1" border="0"></td>
    <td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"><br /><%@ include file="/common/template/includes/right_side_nav.jspf" %></td>
</tr>
<tr valign="top">
	<td bgcolor="#e2dfcc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td width="<%=W_USQ_PADDING%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_PADDING%>" height="1" border="0"></td>
	<td style="padding-top:20px;padding-bottom:5px;" align="center">
		<%@ include file="/shared/includes/usq_bottom_links.jspf" %>
	</td>
	<td width="<%=W_USQ_PADDING%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_PADDING%>" height="1" border="0"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>
<tr valign="top">
	<td bgcolor="#e2dfcc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td width="<%=W_USQ_PADDING%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_PADDING%>" height="1" border="0"></td>
	<td style="padding-top:5px;padding-bottom:10px;" align="center">
		<%@ include file="/shared/includes/usq_copyright.jspf" %>
	</td>
	<td width="<%=W_USQ_PADDING%>"><img src="/media_stat/images/layout/clear.gif" width="<%=W_USQ_PADDING%>" height="1" border="0"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>
</table>
</center>
<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>

</body>
</html>
