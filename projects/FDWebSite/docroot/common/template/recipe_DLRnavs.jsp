<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.storeapi.*'%>
<%@ page import="com.freshdirect.cms.core.domain.ContentKey" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentKeyFactory" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentType" %>
<%@ page import='com.freshdirect.storeapi.application.*'%>
<%@ page import='java.util.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>

<% //expanded page dimensions
final int W_RECIPE_DLR_NAVS_TOTAL = 970;
final int W_RECIPE_DLR_NAVS_LEFT = 150;
final int W_RECIPE_DLR_NAVS_CENTER = 629;
final int W_RECIPE_DLR_NAVS_RIGHT = 191;
%>


<html lang="en-US" xml:lang="en-US">
<head>
<%--     <title><tmpl:get name='title'/></title> --%>
     <tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  <%@ include file="/shared/template/includes/ccl.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%
	// go find the recipeDepartment...(should be a method on contentFactory
	ContentType cType = ContentType.RecipeDepartment;
	Set s = CmsManager.getInstance().getContentKeysByType(cType);
	// there can only be one (hmmmm, the Highlander effect)
	RecipeDepartment rcpDept = null;

	if (!s.isEmpty()) {
 	   ContentKey cKey = (ContentKey) s.iterator().next();
           rcpDept = (RecipeDepartment) PopulatorUtil.getContentNode(cKey.getId());
	}
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
<TABLE WIDTH="<%=W_RECIPE_DLR_NAVS_TOTAL%>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<TD WIDTH="<%=W_RECIPE_DLR_NAVS_LEFT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_RECIPE_DLR_NAVS_LEFT%>" height="1" border="0"></TD>
	<TD WIDTH="<%=W_RECIPE_DLR_NAVS_CENTER%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_RECIPE_DLR_NAVS_CENTER%>" height="1" border="0"></TD>
	<TD WIDTH="<%=W_RECIPE_DLR_NAVS_RIGHT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_RECIPE_DLR_NAVS_RIGHT%>" height="1" border="0"></TD>
</TR>
<TR>
	<td width="<%=W_RECIPE_DLR_NAVS_TOTAL%>" COLSPAN="3"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
</TR>
<TR>
	<TD WIDTH="<%=W_RECIPE_DLR_NAVS_TOTAL%>" COLSPAN="3">
	<%-- Start Department Nav for Recipe --%> 
	   <%@ include file="/common/template/includes/i_recipe_dept_nav.jspf"%>
	<%-- END Recipe Department Nav --%> 

	</TD>
</TR>
<TR>
	<TD WIDTH="<%=W_RECIPE_DLR_NAVS_TOTAL%>" BGCOLOR="#999966" COLSPAN="3"><IMG src="/media_stat/images/layout/999966.gif" ALT="" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="<%=W_RECIPE_DLR_NAVS_LEFT%>" BGCOLOR="#E0E3D0">
		<!-- Recipe Start Left Nav -->
		<tmpl:get name='leftnav'/>
		<oscache:cache key='<%= "sideNav_"+request.getQueryString() %>' time='300'>
                <%@include file="/common/template/includes/i_recipe_leftnav.jspf"%>
		</oscache:cache>

		<!-- END RECIPE SIDE NAV -->
		<img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_RECIPE_DLR_NAVS_LEFT%>">
	</TD>
	<TD width="<%=W_RECIPE_DLR_NAVS_CENTER%>" align="center">
		<img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_RECIPE_DLR_NAVS_CENTER%>" height="15"><br>
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
		<br><br>
	</TD>
	<TD WIDTH="<%=W_RECIPE_DLR_NAVS_RIGHT%>" align="center">
		<img src="/media_stat/images/layout/clear.gif" alt="" height="10" width="1" border="0"><br>
		<%@ include file="/common/template/includes/right_side_nav.jspf" %>
	</TD>
</TR>
</TABLE>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
