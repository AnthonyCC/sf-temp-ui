<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_NO_RIGHTNAV_TOTAL = 970;
final int W_NO_RIGHTNAV_LEFT = 150;
final int W_NO_RIGHTNAV_CENTER_PADDING = 14;
final int W_NO_RIGHTNAV_RIGHT = 806;
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
<%--     <title><tmpl:get name='title'/></title> --%>
     <tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
     <%@ include file="/shared/template/includes/ccl.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
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
	<fd:javascript src="/assets/javascript/overlib_mini.js"/>
<%	} %>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<CENTER CLASS="text10">
<TABLE WIDTH="<%=W_NO_RIGHTNAV_TOTAL%>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<TD WIDTH="<%=W_NO_RIGHTNAV_LEFT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_NO_RIGHTNAV_LEFT%>" height="1" border="0"></TD>
	<TD WIDTH="<%=W_NO_RIGHTNAV_CENTER_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_NO_RIGHTNAV_CENTER_PADDING%>" height="1" border="0"></TD>
	<TD WIDTH="<%=W_NO_RIGHTNAV_RIGHT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_NO_RIGHTNAV_RIGHT%>" height="1" border="0"></TD>
</TR>
<TR>
	<td width="<%=W_NO_RIGHTNAV_TOTAL%>" COLSPAN="3" valign="top" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" alt="" width="<%=W_NO_RIGHTNAV_TOTAL%>" height="1" border="0"></td>
</TR>
<TR>
	<td width="<%=W_NO_RIGHTNAV_TOTAL%>" COLSPAN="3"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="<%=W_NO_RIGHTNAV_LEFT%>" BGCOLOR="#FFFFFF">
		<% try { %><%@ include file="/common/template/includes/left_side_nav_img.jspf" %>
                <% } catch (Exception ex) {ex.printStackTrace();} %><BR>
		<img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_NO_RIGHTNAV_LEFT%>">
	</TD>
	<td align="left" width="<%=W_NO_RIGHTNAV_CENTER_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_NO_RIGHTNAV_CENTER_PADDING%>"></td>
        <TD width="<%=W_NO_RIGHTNAV_RIGHT%>" align="center">
                <TABLE WIDTH="100%" align="center" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                      <td align="left"><img src="<%=tmplFldrLbl%>" border="0"></td>
                    </tr>
                    <tr>
                      <td align="left"><img usemap="#categoryNav" src="<%=tmplNavBar%>" border="0"></td>
                    </tr>
                </table>
                <tmpl:get name='topLegend'/>
                <!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
		<br><br>
                <TABLE WIDTH="100%" align="center" cellspacing="0" cellpadding="0" border="0">
                     <tr>
                      <td align="left"><img usemap="#categoryNav" src="<%=tmplNavBar%>" border="0"></td>
                    </tr>
                </table>
		<tmpl:get name='bottomLegend'/>
  	</TD>
</TR>
<tr>
    <td align="center" colspan="3">
	<br><%@ include file="/includes/wine_system.jspf" %><br><font class="space2pix"><br></font><%@ include file="/shared/includes/wine_copyright.jspf" %> <%@ include file="/includes/wine_privacy.jspf" %><br><br>
    </td>
</tr>
<TR>
<td width="<%=W_NO_RIGHTNAV_TOTAL%>" COLSPAN="3" BGCOLOR="#999966" VALIGN="BOTTOM"><img src="/media_stat/images/layout/999966.gif" alt="" width="<%=W_NO_RIGHTNAV_TOTAL%>" height="1" border="0"></td>
</TR>
</TABLE>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<!-- !!!  Should be able to get map file and mapName based on DeptId/Name Image -->
<%@ include file="/common/template/includes/i_imagemap.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
