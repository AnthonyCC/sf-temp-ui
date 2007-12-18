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
     <%@ include file="/shared/template/includes/ccl.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
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
<TR>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34"></TD>
	<TD WIDTH="743" COLSPAN="5">
	<%-- Start Department Nav for Recipe --%> 
	   <%@ include file="/common/template/includes/i_recipe_dept_nav.jspf"%>
	<%-- END Recipe Department Nav --%> 

	</TD>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34"></TD>
</TR>
<TR>
	<TD WIDTH="745" BGCOLOR="#999966" COLSPAN="7"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="TOP">
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
	<TD WIDTH="5" BGCOLOR="#E0E3D0"><BR></TD>
	<TD WIDTH="125" BGCOLOR="#E0E3D0">
		<!-- Recipe Start Left Nav -->
		<tmpl:get name='leftnav'/>
		<oscache:cache key='<%= "sideNav_"+request.getQueryString() %>' time='300'>
                <%@include file="/common/template/includes/i_recipe_leftnav.jspf"%>
		</oscache:cache>

		<!-- END RECIPE SIDE NAV -->
		<img src="/media_stat/images/layout/clear.gif" height="1" width="125">
	</TD>
	<TD width="458" align="center">
		<img src="/media_stat/images/layout/clear.gif" width="458" height="15"><br>
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
		<br><br>
	</TD>
	<TD WIDTH="155" COLSPAN="2" align="center">
		<img src="/media_stat/images/layout/clear.gif" height="10" width="1" border="0"><br>
		<%@ include file="/common/template/includes/right_side_nav.jspf" %>
	</TD>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="BOTTOM">
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_left_curve_nav.gif" width="6" height="6" border="0"></td>
	<TD WIDTH="125" BGCOLOR="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></TD>
	<td width="608" COLSPAN="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</TR>
<TR>
<td width="733" COLSPAN="3" BGCOLOR="#999966" VALIGN="BOTTOM"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
</TR>
</TABLE>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
