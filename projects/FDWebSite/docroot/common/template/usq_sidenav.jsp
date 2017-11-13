<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>

<% //expanded page dimensions
final int W_WINE_SIDENAV_TOTAL = 970;
final int W_WINE_SIDENAV_LEFT = 150;
final int W_WINE_PADDING = 14;
final int W_WINE_SIDENAV_CENTER = 601;
final int W_WINE_SIDENAV_RIGHT = 191;
%>

<html lang="en-US" xml:lang="en-US">
<head>
	<tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
	<tmpl:get name='facebookmeta'/>
  <% 
  if(FDStoreProperties.isBazaarvoiceEnabled()){
    String bvapiUrl = FDStoreProperties.getBazaarvoiceBvapiUrl(); %>
    <script type="text/javascript" src="<%= bvapiUrl %>"></script>
  <% } %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
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
	<fd:javascript src="/assets/javascript/overlib_mini.js"/>
<%	} %>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<CENTER CLASS="text10">
<table width="<%=W_WINE_SIDENAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td width="<%=W_WINE_SIDENAV_LEFT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_SIDENAV_LEFT%>" height="1" border="0"></td>
	<td width="<%=W_WINE_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_PADDING%>" height="1" border="0"></td>
	<td width="<%=W_WINE_SIDENAV_CENTER%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_SIDENAV_CENTER%>" height="1" border="0"></td>
	<td width="<%=W_WINE_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_PADDING%>" height="1" border="0"></td>
	<td width="<%=W_WINE_SIDENAV_RIGHT%>"><img src="/media_stat/images/layout/clear.gif"alt=""  width="<%=W_WINE_SIDENAV_RIGHT%>" height="1" border="0"></td>
</tr>
<tr valign="top">
	<td class="wine-sidenav" bgcolor="#e2dfcc" style="z-index: 0;"><div align="center"><a href="/department.jsp?deptId=<%= JspMethods.getWineAssociateId().toLowerCase() %>&trk=snav"><img src="/media/editorial/win_<%= JspMethods.getWineAssociateId().toLowerCase() %>/<%= JspMethods.getWineAssociateId().toLowerCase() %>_logo_sidenav_bottom.gif" width="150" height="109" border="0"></a><br></div>
	<% try { %><%@ include file="/common/template/includes/left_side_nav_usq.jspf" %>
                <% } catch (Exception ex) {ex.printStackTrace();} %></td>
	<td width="<%=W_WINE_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_PADDING%>" height="1" border="0"></td>
	<td align="center">   
                <!-- content lands here -->
         		<tmpl:get name='content'/>
                <!-- content ends above here-->
    </td>
	<td width="<%=W_WINE_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_PADDING%>" height="1" border="0"></td>
    <td align="center"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10" border="0"><br /><%@ include file="/common/template/includes/right_side_nav.jspf" %></td>
</tr>
<tr valign="top">
	<td bgcolor="#e2dfcc"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
	<td width="<%=W_WINE_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_PADDING%>" height="1" border="0"></td>
	<td style="padding-top:20px;padding-bottom:5px;" align="center">
		<%@ include file="/shared/includes/usq_bottom_links.jspf" %>
	</td>
	<td width="<%=W_WINE_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_PADDING%>" height="1" border="0"></td>
    <td><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
</tr>
<tr valign="top">
	<td bgcolor="#e2dfcc"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
	<td width="<%=W_WINE_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_PADDING%>" height="1" border="0"></td>
	<td style="padding-top:5px;padding-bottom:10px;" align="center">
		<%@ include file="/shared/includes/usq_copyright.jspf" %>
	</td>
	<td width="<%=W_WINE_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_WINE_PADDING%>" height="1" border="0"></td>
    <td><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
</tr>
</table>
</center>
<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>

</body>
</html>
