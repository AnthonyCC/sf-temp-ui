<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%
/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}*/
%>
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
	<head>
		<title><tmpl:get name='title'/></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
	<% /* @ include file="/common/template/includes/i_javascripts.jspf" */ %>
    <% /* @ include file="/shared/template/includes/style_sheet_grid_compat.jspf" */ %>
    <% /* @ include file="/shared/template/includes/style_sheet_detect.jspf" */ %>
    <% /* @ include file="/shared/template/includes/ccl.jspf" */ %>

	<!-- from  /common/template/includes/i_javascripts.jspf -->
	<jwr:script src="/fdlibs.js" useRandomParam="false" />

	<!-- from  /shared/template/includes/style_sheet_grid_compat.jspf -->
	<%-- this file will be included in the <head> tag, when the new grid is used --%>
  	<jwr:style src="/grid.css"/>

	<!-- from  /shared/template/includes/style_sheet_detect.jspf -->
	<%-- global css files, header, footer, etc. --%>
	<jwr:style src="/global.css"/>

	<%-- old css files --%>
	<jwr:style src="/oldglobal.css"/>

	<!-- from  /shared/template/includes/ccl.jspf -->


	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	</head>
	<body>
	<%@ include file="/shared/template/includes/i_body_start.jspf" %>
			<%@ include file="/common/template/includes/globalnav.jspf" %>
    <div id="content">
      <center class="text10">
      <!-- content lands here -->
      <tmpl:get name='content'/>
      <!-- content ends above here-->
      </center>
    </div>
	<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	</body>
</html>
