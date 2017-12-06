<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.storeapi.*'%>
<%@ page import='com.freshdirect.cms.core.domain.ContentKey' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentKeyFactory' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentType' %>
<%@ page import='com.freshdirect.storeapi.application.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>

<% //expanded page dimensions
final int W_BDNML_TOTAL = 970;
final int W_BDNML_LEFT = 150;
final int W_BDNML_CENTER = 629;
final int W_BDNML_RIGHT = 191;
%>
<html lang="en-US" xml:lang="en-US">
<head>
     <tmpl:get name="seoMetaTag"/>
<%-- 	<title><tmpl:get name='title'/></title> --%>
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
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" 
	onload="<%= request.getAttribute("bodyOnLoad")%>" 
	onunload="<%= request.getAttribute("bodyOnUnload")%>" >
<%@ include file="/shared/template/includes/i_body_start.jspf" %>      
<%
	//
	// annotation mode, add overlib stuff
	//
	if (FDStoreProperties.isAnnotationMode()) {
%>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<fd:javascript src="/assets/javascript/overlib_mini.js"/>
<%	} %>
		<%@ include file="/common/template/includes/globalnav.jspf" %> 
	<center class="text10">
    <table width="<%= W_BDNML_TOTAL %>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="<%= W_BDNML_LEFT %>"><img src="/media_stat/images/layout/clear.gif" width="<%= W_BDNML_LEFT %>" height="1" border="0" alt="" /></td>
				<td width="<%= W_BDNML_CENTER %>"><img src="/media_stat/images/layout/clear.gif" width="<%= W_BDNML_CENTER %>" height="1" border="0" alt="" /></td>
				<td width="<%= W_BDNML_RIGHT %>"><img src="/media_stat/images/layout/clear.gif" width="<%= W_BDNML_RIGHT %>" height="1" border="0" alt="" /></td>
			</tr>
			<tr>
				<td width="<%= W_BDNML_TOTAL %>" colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt="" /></td>
			</tr>
			<tr>
				<td width="<%= W_BDNML_TOTAL %>" colspan="3">
				<% if ( request.getParameter("catId") !=null && ContentFactory.getInstance().getContentNode(request.getParameter("catId")) instanceof RecipeCategory) {
					
					// go find the recipeDepartment...(should be a method on contentFactory
					ContentType cType = ContentType.RecipeDepartment;
					Set s = CmsManager.getInstance().getContentKeysByType(cType);
					// there can only be one (hmmmm, the Highlander effect)
					RecipeDepartment rcpDept = null;

					if (!s.isEmpty()) {
				 	   ContentKey cKey = (ContentKey) s.iterator().next();
				           rcpDept = (RecipeDepartment) ContentFactory.getInstance().getContentNode(cKey.getId());
					}
					%>
					<%@ include file="/common/template/includes/i_recipe_dept_nav.jspf"%>
				<% } else { %>
					<%@ include file="/common/template/includes/deptnav.jspf" %>
				<% } %>
				</td>
			</tr>
			<tr>
				<td width="<%= W_BDNML_TOTAL %>" bgcolor="#999966" colspan="3"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1" alt="" /></td>
			</tr>
			<tr valign="top">
				<td width="<%= W_BDNML_LEFT %>" bgcolor="#E0E3D0" class="lNavTableContTD">
					<img src="/media_stat/images/layout/clear.gif" height="1" width="<%= W_BDNML_LEFT %>" alt="" />
					<!-- left nav, manual start -->
					<tmpl:get name='left_nav_manual'/><br />
					<!-- left nav, manual end -->
					<img src="/media_stat/images/layout/clear.gif" height="1" width="<%= W_BDNML_LEFT %>" alt="" />
				</td>
				<td width="<%= W_BDNML_CENTER %>" align="center">
					<img src="/media_stat/images/layout/clear.gif" width="<%= W_BDNML_CENTER %>" height="15" alt="" /><br />
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
					<br /><br />
				</td>
				<td width="<%= W_BDNML_RIGHT %>" align="center">
					<img src="/media_stat/images/layout/clear.gif" height="10" width="1" border="0" alt="" /><br />
					<%@ include file="/common/template/includes/right_side_nav.jspf" %>
				</td>
			</tr>
			<tr valign="bottom">
				<td width="<%= W_BDNML_LEFT %>" bgcolor="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt="" /></td>
				<td width="<%= W_BDNML_CENTER + W_BDNML_LEFT %>" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt="" /></td>
			</tr>
		</TABLE>
	</center>
	<%@ include file="/common/template/includes/footer.jspf" %>
  <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>
