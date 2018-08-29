<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.storeapi.*'%>
<%@ page import='com.freshdirect.cms.core.domain.ContentKey' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentKeyFactory' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentType' %>
<%@ page import='com.freshdirect.storeapi.application.*'%>
<%@ page import='java.util.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_RECIPE_DL_NAVS_TOTAL = 970;
final int W_RECIPE_DL_NAVS_LEFT = 150;
final int W_RECIPE_DL_NAVS_RIGHT = 820;
%>

<%/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}
*/%>
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
	<tmpl:get name='customhead'/>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
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
<table width="<%=W_RECIPE_DL_NAVS_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td width="<%=W_RECIPE_DL_NAVS_LEFT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_RECIPE_DL_NAVS_LEFT%>" height="1" border="0"></td>
	<td width="<%=W_RECIPE_DL_NAVS_RIGHT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_RECIPE_DL_NAVS_RIGHT%>" height="1" border="0"></td>
</tr>
<tr>
	<td width="<%=W_RECIPE_DL_NAVS_TOTAL%>" colspan="2"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5" border="0"></td>
</tr>
<tr>
	<td width="<%=W_RECIPE_DL_NAVS_TOTAL%>" colspan="2">
	<%-- Start Department Nav for Recipe --%> 
	  <%@ include file="/common/template/includes/i_recipe_dept_nav.jspf"%>
	<%-- END Recipe Department Nav --%> 

	</td>
</tr>
<tr>
	<td width="<%=W_RECIPE_DL_NAVS_TOTAL%>" bgcolor="#999966" colspan="6"><img src="/media_stat/images/layout/999966.gif" alt="" width="1" height="1"></td>
</tr>
<tr valign="top">
	<td width="<%=W_RECIPE_DL_NAVS_LEFT%>" bgcolor="#E0E3D0">
		<!-- Recipe Start Left Nav -->
                  <%@include file="/common/template/includes/i_recipe_leftnav.jspf"%>

		<!-- END RECIPE SIDE NAV -->
		<img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_RECIPE_DL_NAVS_LEFT%>">
	</td>
	<td width="<%=W_RECIPE_DL_NAVS_RIGHT%>" align="right">
		<img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_RECIPE_DL_NAVS_RIGHT%>" height="15"><br>
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
		<br><br>
	</td>
</tr>
</table>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
