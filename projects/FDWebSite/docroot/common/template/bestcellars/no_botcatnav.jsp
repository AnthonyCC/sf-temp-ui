<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
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


</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10" 
      onload="<%= request.getAttribute("bodyOnLoad")%>" 
      onunload="<%= request.getAttribute("bodyOnUnload")%>" >
<%
        String tmplCatId = request.getParameter("catId");
        String tmplFldrLbl = "/media_stat/images/layout/clear.gif";
        String tmplNavBar = "/media_stat/images/layout/clear.gif";

         if (tmplCatId!=null) {
            ContentNodeModel tmplCat = ContentFactory.getInstance().getContentNodeByName(tmplCatId);
            ContentNodeModel wokingCat = tmplCat.getAttribute("ALIAS")!=null  
                 ? ((CategoryRef) tmplCat.getAttribute("ALIAS").getValue()).getCategory()
                 : tmplCat;
             if (wokingCat!=null && ContentNodeI.TYPE_CATEGORY.equals(wokingCat.getContentType())) {
               if (wokingCat.getAttribute("CAT_LABEL")!=null) {
                    tmplFldrLbl = ((MediaI)wokingCat.getAttribute("CAT_LABEL").getValue()).getPath();
              }
               if (wokingCat.getAttribute("CATEGORY_NAVBAR")!=null) {
                    tmplNavBar = ((MediaI)wokingCat.getAttribute("CATEGORY_NAVBAR").getValue()).getPath();
               }
                 
            }
        }
	//
	// annotation mode, add overlib stuff
	//
	if (FDStoreProperties.isAnnotationMode()) {
%>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
	<script language="JavaScript" src="/assets/javascript/overlib_mini.js"></script>
<%	} %>
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<TABLE WIDTH="745" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<TD WIDTH="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></TD>
	<TD WIDTH="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></TD>
	<TD WIDTH="125"><img src="/media_stat/images/layout/clear.gif" width="125" height="1" border="0"></TD>
	<TD WIDTH="458"><img src="/media_stat/images/layout/clear.gif" width="458" height="1" border="0"></TD>
	<TD WIDTH="150"><img src="/media_stat/images/layout/clear.gif" width="150" height="1" border="0"></TD>
	<TD WIDTH="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></TD>
	<TD WIDTH="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></TD>
</TR>
<TR><td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"></td>
	<td width="733" COLSPAN="3" valign="top" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
	<td width="733" COLSPAN="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</TR>
<TR VALIGN="TOP">
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
	<TD WIDTH="5" BGCOLOR="#FFFFFF"><BR></TD>
	<TD WIDTH="125" BGCOLOR="#FFFFFF">
		<% try { %><%@ include file="/common/template/includes/left_side_nav_img.jspf" %>
                <% } catch (Exception ex) {ex.printStackTrace();} %><BR>
		<img src="/media_stat/images/layout/clear.gif" height="1" width="125">
	</TD>
	<TD width="458" align="center">
                <TABLE WIDTH="100%" align="center" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                      <td align="center"><img src="/media_stat/images/layout/clear.gif" height="35" width="1"><br><img src="<%=tmplFldrLbl%>" border="0"></td>
                    </tr>
                    <tr>
                      <td align="center"><img usemap="#categoryNav" src="<%=tmplNavBar%>" border="0"></td>
                    </tr>
                </table>
                <tmpl:get name='topLegend'/>
                <!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
  	</TD>
	<TD WIDTH="155" COLSPAN="2" align="center">
		<img src="/media_stat/images/layout/clear.gif" height="10" width="1" border="0"><br>
		<%@ include file="/common/template/includes/right_side_nav.jspf" %>
	</TD>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<tr>
    <TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
    <td align="center" colspan="5">
	<br><br><%@ include file="/includes/wine_system.jspf" %><br><font class="space2pix"><br></font><%@ include file="/shared/includes/wine_copyright.jspf" %> <%@ include file="/includes/wine_privacy.jspf" %><br><br>
    </td>
   <TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</tr>
<TR VALIGN="BOTTOM">
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_left_curve_t.gif" width="6" height="6" border="0"></td>
	<TD WIDTH="125" BGCOLOR="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></TD>
	<td width="608" COLSPAN="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
<td width="733" COLSPAN="3" BGCOLOR="#999966" VALIGN="BOTTOM"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
</TR>
</TABLE>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
<!-- !!!  Should be able to get map file and mapName based on DeptId/Name Image -->
<%@ include file="/common/template/includes/i_imagemap.jspf" %>
</BODY>
</HTML>
