<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.cms.*'%>
<%@ page import='com.freshdirect.cms.application.*'%>
<%@ page import='java.util.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}
*/%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<%
	// go find the recipeDepartment...(should be a method on contentFactory
	ContentType cType = ContentType.get("RecipeDepartment");
	Set s = CmsManager.getInstance().getContentKeysByType(cType);
	// there can only be one (hmmmm, the Highlander effect)
	RecipeDepartment rcpDept = null;

	if (!s.isEmpty()) {
 	   ContentKey cKey = (ContentKey) s.iterator().next();
           rcpDept = (RecipeDepartment) ContentFactory.getInstance().getContentNode(cKey.getId());
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
<table width="745" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<tr>
	<td width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
	<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
	<td width="583"><img src="/media_stat/images/layout/clear.gif" width="583" height="1" border="0"></td>
	<td width="150"><img src="/media_stat/images/layout/clear.gif" width="150" height="1" border="0"></td>
	<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
	<td width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
</tr>
<tr><td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"></td>
	<td width="733" colspan="2" valign="top" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
	<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
</tr>
<tr>
	<td width="733" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</tr>
<tr>
	<td width="1" bgcolor="#999966"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="34"></td>
	<td width="743" colspan="4">
	<%-- Start Department Nav for Recipe --%> 
	<%@ include file="/common/template/includes/i_recipe_dept_nav.jspf"%>
	<%-- END Recipe Department Nav --%> 

	</td>
	<td width="1" bgcolor="#999966"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="34"></td>
</tr>
<tr>
	<td width="745" bgcolor="#999966" colspan="6"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
</tr>
<tr valign="TOP">
	<td bgcolor="#999966" valign="BOTTOM" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
	<td width="5"><BR></td>
	<td width="583" align="center">
		<img src="/media_stat/images/layout/clear.gif" width="458" height="15"><br>
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
		<br><br>
	</td>
	<td width="155" colspan="2" align="center">
		<img src="/media_stat/images/layout/clear.gif" height="10" width="1" border="0"><br>
		<%@ include file="/common/template/includes/right_side_nav.jspf" %>
	</td>
	<td bgcolor="#999966" valign="BOTTOM" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
</tr>
<tr valign="BOTTOM">
	<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0"></td>
	<td width="733" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
	<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</tr>
<tr>
<td width="733" colspan="3" bgcolor="#999966" valign="BOTTOM"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
</tr>
</table>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
