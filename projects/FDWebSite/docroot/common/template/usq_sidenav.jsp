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
	<link href="/assets/css/wine.css" rel="stylesheet" type="text/css" />
    <%@ include file="/common/template/includes/metatags.jspf" %>
	<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
     <%@ include file="/shared/template/includes/ccl.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<%
        String tmplCatId = request.getParameter("catId");
        String tmplFldrLbl = "/media_stat/images/layout/clear.gif";
        String tmplNavBar = "/media_stat/images/layout/clear.gif";

        String fldrLink = "#";

        if (tmplCatId!=null) {
            ContentNodeModel tmplCat = ContentFactory.getInstance().getContentNodeByName(tmplCatId);
            ContentNodeModel wokingCat = tmplCat.getAttribute("ALIAS")!=null  
                 ? ((CategoryRef) tmplCat.getAttribute("ALIAS").getValue()).getCategory()
                 : tmplCat;
            if (wokingCat!=null && ContentNodeI.TYPE_CATEGORY.equals(wokingCat.getContentType())) {
               if (wokingCat.getAttribute("CAT_LABEL")!=null) {
                    tmplFldrLbl = ((MediaI)wokingCat.getAttribute("CAT_LABEL").getValue()).getPath();
                    fldrLink = "/category.jsp?catId="+tmplCat;
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
<table width="745" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
	<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
	<td width="125"><img src="/media_stat/images/layout/clear.gif" width="125" height="1" border="0"></td>
	<td width="458"><img src="/media_stat/images/layout/clear.gif" width="458" height="1" border="0"></td>
	<td width="150"><img src="/media_stat/images/layout/clear.gif" width="150" height="1" border="0"></td>
	<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
	<td width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
</tr>
<tr valign="top">
  <td colspan="3" rowspan="2"><img src="/media/editorial/win_usq/usq_logo_sidenav_top.gif"></td>
  <td colspan="2" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
  <td colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
  </tr>
<tr>
  <td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
  </tr>
<tr valign="top">
	<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td colspan="2" bgcolor="#FBF1D3"><a href="/department.jsp?deptId=usq&trk=snav"><img src="/media/editorial/win_usq/usq_logo_sidenav_bottom.gif" width="130" height="109" border="0"></a><br>
	<% try { %><%@ include file="/common/template/includes/left_side_nav_usq.jspf" %>
                <% } catch (Exception ex) {ex.printStackTrace();} %></td>
	<td style="padding-left:10px;padding-right:5px;" align="center">   
                <!-- content lands here -->
         		<tmpl:get name='content'/>
                <!-- content ends above here-->
    </td>
    <td colspan="2" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"><br /><%@ include file="/common/template/includes/right_side_nav.jspf" %></td>
    <td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
</tr>
<tr valign="top">
	<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td colspan="2" bgcolor="#FBF1D3"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td style="padding-top:10px;padding-bottom:10px;" align="center"><%@ include file="/shared/includes/usq_copyright.jspf" %></td>
    <td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
</tr>
<tr valign="bottom">
	<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve_nav_usq.gif" width="6" height="6" border="0"></td>
	<td bgcolor="#FBF1D3"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
	<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
	<td colspan="2" rowspan="2" align="right"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</tr>
<tr>
<td colspan="3" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
</tr>
</table>

<%@ include file="/common/template/includes/footer.jspf" %>
</center>

</body>
</html>
