<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>

<% //expanded page dimensions
final int W_NOT_BOTCATNAV_TOTAL = 970;
final int W_NOT_BOTCATNAV_LEFT = 150;
final int W_NOT_BOTCATNAV_CENTER = 629;
final int W_NOT_BOTCATNAV_RIGHT = 191;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
    <title><tmpl:get name='title'/></title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
     <%@ include file="/shared/template/includes/ccl.jspf" %>
<%
    {
       String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
       if (onbeforeunload != null && onbeforeunload.length() > 0) {
%>
    <script language="javascript">
       window.onbeforeunload = <%= onbeforeunload %>;
    </script>
<%
       } // if
    } // local block
%>


<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333"
      onload="<%= request.getAttribute("bodyOnLoad")%>" 
      onunload="<%= request.getAttribute("bodyOnUnload")%>" >
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
<TABLE WIDTH="<%=W_NOT_BOTCATNAV_TOTAL%>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<TD WIDTH="<%=W_NOT_BOTCATNAV_LEFT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_NOT_BOTCATNAV_LEFT%>" height="1" border="0"></TD>
	<TD WIDTH="<%=W_NOT_BOTCATNAV_CENTER%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_NOT_BOTCATNAV_CENTER%>" height="1" border="0"></TD>
	<TD WIDTH="<%=W_NOT_BOTCATNAV_RIGHT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_NOT_BOTCATNAV_RIGHT%>" height="1" border="0"></TD>
</TR>
	<td width="<%=W_NOT_BOTCATNAV_TOTAL%>" colspan="3" valign="top" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" alt="" width="<%=W_NOT_BOTCATNAV_TOTAL%>" height="1" border="0"></td>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="<%=W_NOT_BOTCATNAV_LEFT%>" class="left_nav">
		<% try { %><%@ include file="/common/template/includes/left_side_nav_usq.jspf" %>
                <% } catch (Exception ex) {ex.printStackTrace();} %><BR>
		<img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_NOT_BOTCATNAV_LEFT%>">
	</TD>
	<TD width="<%=W_NOT_BOTCATNAV_CENTER%>" align="center">
                
               
                <!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
  	</TD>
	<TD WIDTH="<%=W_NOT_BOTCATNAV_RIGHT%>" align="center">
		<img src="/media_stat/images/layout/clear.gif" alt="" height="10" width="1" border="0"><br>
		<%@ include file="/common/template/includes/right_side_nav.jspf" %>
	</TD>
</TR>
<tr>
    <td align="center" colspan="3">
	<br><br><%@ include file="/includes/wine_system.jspf" %><br><font class="space2pix"><br></font><%@ include file="/shared/includes/wine_copyright.jspf" %> <%@ include file="/includes/wine_privacy.jspf" %><br><br>
    </td>
</tr>
<TR VALIGN="BOTTOM">
	<TD WIDTH="<%=W_NOT_BOTCATNAV_LEFT%>" BGCOLOR="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></TD>
	<td width="<%=W_NOT_BOTCATNAV_TOTAL - W_NOT_BOTCATNAV_LEFT%>" COLSPAN="2"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
</TR>
<TR>
<td width="<%=W_NOT_BOTCATNAV_TOTAL%>" colspan="3" BGCOLOR="#999966" VALIGN="BOTTOM"><img src="/media_stat/images/layout/999966.gif" alt="" width="<%=W_NOT_BOTCATNAV_TOTAL%>" height="1" border="0"></td>
</TR>
</TABLE>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<!-- !!!  Should be able to get map file and mapName based on DeptId/Name Image -->
<%@ include file="/common/template/includes/i_imagemap.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
